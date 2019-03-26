package com.sh3h.workmanagerjm.ui.handler.stop;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.InputMaterialAdapter;
import com.sh3h.workmanagerjm.myinterface.OnInputMaterialListener;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StopHandlerFragment extends HandlerFragment implements StopHandlerMvpView {

    @Inject
    StopHandlerPresenter presenter;
    @BindView(R.id.sp_stop_method)
    Spinner spStopMethod;   //停水方式
    @BindView(R.id.tv_stop_method)
    TextView tvStopMethod;   //停水方式
    @BindView(R.id.sp_stop_type)
    Spinner spStopType;   //停水类型
    @BindView(R.id.tv_stop_type)
    TextView tvStopType;   //停水类型
    @BindView(R.id.et_old_meter_number)
    EditText etOldMeterNumber;  //旧表读数
    @BindView(R.id.tv_old_meter_number)
    TextView tvOldMeterNumber;  //旧表读数
    @BindView(R.id.sp_handle_result)
    Spinner spResult;   //处理结果
    /*上面是必填项，下面非必填*/
    @BindView(R.id.et_remark)
    EditText etRemark;//备注
    @BindView(R.id.btn_input_material)
    Button btnInputMaterial;
    @BindView(R.id.rv_material)
    RecyclerView rvMaterial;

    private OnInputMaterialListener listener;
    private InputMaterialAdapter adapter;
    private Unbinder unbinder;
    private ArrayList<DUMaterial> list;
    private List<DUWord> resolveResultList;
    private int position;

    public static StopHandlerFragment newInstance() {
        return new StopHandlerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList(ConstantUtil.Parcel.DUMATERIAL);
            position = savedInstanceState.getInt(ConstantUtil.Key.POSITION);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInputMaterialListener) {
            listener = (OnInputMaterialListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_handler, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ManagerActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        initDUDate();
        initView();
        enableView();
        setOnListener();
        presenter.getWord(ConstantUtil.ParentId.RESOLVE_RESULT, ConstantUtil.Group.RESOLVE_RESULT);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ConstantUtil.Parcel.DUMATERIAL, list);
        outState.putInt(ConstantUtil.Key.POSITION, position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input_material:
                if (listener != null) {
                    listener.showInputMaterial(null);
                    position = -1;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_handle_result:
                showHideStar(position);
                break;
            default:
                break;
        }
    }

    @Override
    public void temporaryStorage() {
        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.SAVE);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        taskHandle.setStopWaterMethod(spStopMethod.getSelectedItemPosition() == 0
                ? ConstantUtil.StopMethod.STOP_USE : ConstantUtil.StopMethod.SPLIT);
        int position = spStopType.getSelectedItemPosition();
        switch (position) {
            case 0:
                taskHandle.setStopWaterType(ConstantUtil.StopType.REPORT_STOP);
                break;
            case 1:
                taskHandle.setStopWaterType(ConstantUtil.StopType.ARREARAGE);
                break;
            default:
                taskHandle.setStopWaterType(ConstantUtil.StopType.REPORT_STOP);
                break;
        }
        String oldMeterNumber = etOldMeterNumber.getText().toString();
        taskHandle.setOldMeterReading(NumberUtil.transformNumber(oldMeterNumber));
        int resolverResultPosition = spResult.getSelectedItemPosition();
        if (resolverResultPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolverResult(NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue()));
        }
        taskHandle.setRemark(etRemark.getText().toString());
        taskHandle.setMaterials(list);
        handle.setReply(taskHandle);
    }

    @Override
    public boolean checkDataInvalid() {
        String oldMeterNumber = etOldMeterNumber.getText().toString();
        int resolverResultPosition = spResult.getSelectedItemPosition();
        if (resolverResultPosition == Spinner.INVALID_POSITION) {
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_handle_result_invalid);
            return false;
        }

        int resolveResultValue = NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue());
        if (isNeedCheckDataInvalid(resolveResultValue)) {
            if (TextUtil.isNullOrEmpty(oldMeterNumber)) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_old_read_null);
                return false;
            }
        }

        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        taskHandle.setStopWaterMethod(spStopMethod.getSelectedItemPosition() == 0
                ? ConstantUtil.StopMethod.STOP_USE : ConstantUtil.StopMethod.SPLIT);
        int position = spStopType.getSelectedItemPosition();
        switch (position) {
            case 0:
                taskHandle.setStopWaterType(ConstantUtil.StopType.REPORT_STOP);
                break;
            case 1:
                taskHandle.setStopWaterType(ConstantUtil.StopType.ARREARAGE);
                break;
            default:
                taskHandle.setStopWaterType(ConstantUtil.StopType.REPORT_STOP);
                break;
        }
        taskHandle.setOldMeterReading(NumberUtil.transformNumber(oldMeterNumber));
        taskHandle.setResolverResult(resolveResultValue);
        taskHandle.setRemark(etRemark.getText().toString());
        taskHandle.setMaterials(list);
        handle.setReply(taskHandle);
        return true;
    }

    @Override
    public boolean isNeedPicture() {
        return true;
    }

    @Override
    public void onItemClick(int type, int i) {
        if (listener != null) {
            listener.showInputMaterial(list.get(i));
            position = i;
        }
    }

    @Override
    public void onItemLongClick(int fileType, int position) {
        new MaterialDialog.Builder(getContext())
                .title(R.string.text_prompt)
                .content(R.string.toast_if_sure_delete_material)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                })
                .build()
                .show();
    }

    @Override
    public void saveMaterial(DUMaterial material) {
        if (position < 0) {
            for (DUMaterial duMaterial : list) {
                if (!TextUtil.isNullOrEmpty(material.getMaterialNo())
                        && material.getMaterialNo().equals(duMaterial.getMaterialNo())
                        && material.getType().equals(duMaterial.getType())
                        && material.getName().equals(duMaterial.getName())
                        && material.getSpec().equals(duMaterial.getSpec())
                        && material.getUnit().equals(duMaterial.getUnit())
                        && material.getPrice() == duMaterial.getPrice()) {
                    duMaterial.setCount(duMaterial.getCount() + material.getCount());
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
            list.add(material);
        } else {
            list.set(position, material);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getResolveResult(List<DUWord> list) {
        resolveResultList = list;
        initSpinner(spResult, handle == null ? 0 : handle.toDUTaskHandle().getResolverResult(), list);
    }

    private void initView() {
        List<DUWord> stopMethodList = new ArrayList<>();
        stopMethodList.add(new DUWord(getString(R.string.text_stop_use), String.valueOf(ConstantUtil.StopMethod.STOP_USE)));
        stopMethodList.add(new DUWord(getString(R.string.text_split), String.valueOf(ConstantUtil.StopMethod.SPLIT)));
        List<DUWord> stopTypeList = new ArrayList<>();
        stopTypeList.add(new DUWord(getString(R.string.text_report_stop), String.valueOf(ConstantUtil.StopType.REPORT_STOP)));
        stopTypeList.add(new DUWord(getString(R.string.text_arrearage), String.valueOf(ConstantUtil.StopType.ARREARAGE)));
        int stopMethodValue = ConstantUtil.StopMethod.STOP_USE;
        int stopTypeValue = ConstantUtil.StopType.REPORT_STOP;
        if (handle != null) {
            DUTaskHandle taskHandle = handle.toDUTaskHandle();
            etOldMeterNumber.setText(taskHandle.getOldMeterReading() == 0 ? TextUtil.EMPTY : String.valueOf(taskHandle.getOldMeterReading()));
            etRemark.setText(taskHandle.getRemark() == null ? TextUtil.EMPTY : taskHandle.getRemark());
            stopMethodValue = taskHandle.getStopWaterMethod();
            stopTypeValue = taskHandle.getStopWaterType();
            list = taskHandle.getMaterials();
        }

        list = list == null ? new ArrayList<>() : list;
        adapter = new InputMaterialAdapter();
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.setMaterials(list);
        rvMaterial.setAdapter(adapter);
        rvMaterial.setHasFixedSize(true);
        rvMaterial.setNestedScrollingEnabled(false);
        rvMaterial.setLayoutManager(new LinearLayoutManager(getContext()));

        initSpinner(spStopMethod, stopMethodValue, stopMethodList);
        initSpinner(spStopType, stopTypeValue, stopTypeList);
    }

    private void enableView() {
        etOldMeterNumber.setEnabled(permitWrite);
        spResult.setEnabled(permitWrite);
        etRemark.setEnabled(permitWrite);
        btnInputMaterial.setEnabled(permitWrite);
    }

    private void setOnListener() {
        spResult.setOnItemSelectedListener(this);
        btnInputMaterial.setOnClickListener(this);
    }

    private void showHideStar(int position) {
        int value = NumberUtil.transformNumber(resolveResultList.get(position).getValue());
        int visibility = isNeedCheckDataInvalid(value) ? View.VISIBLE : View.INVISIBLE;
        tvStopMethod.setVisibility(visibility);
        tvStopType.setVisibility(visibility);
        tvOldMeterNumber.setVisibility(visibility);
    }
}
