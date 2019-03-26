package com.sh3h.workmanagerjm.ui.handler.reinstall;


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
import android.widget.ImageButton;
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
public class ReinstallHandlerFragment extends HandlerFragment implements ReinstallHandlerMvpView {

    @Inject
    ReinstallHandlerPresenter presenter;
    @BindView(R.id.sp_new_meter_type)
    Spinner spNewMeterType;       //新表类型
    @BindView(R.id.tv_new_meter_type)
    TextView tvNewMeterType;       //新表类型
    @BindView(R.id.sp_new_meter_caliber)
    Spinner spNewMeterCaliber;    //新表口径
    @BindView(R.id.tv_new_meter_caliber)
    TextView tvNewMeterCaliber;    //新表口径
    @BindView(R.id.et_bar_code)
    EditText etBarCode;   //新表表身码
    @BindView(R.id.ib_scan)
    ImageButton ibScan;
    @BindView(R.id.tv_bar_code)
    TextView tvBarCode;   //新表表身码
    @BindView(R.id.sp_new_meter_producer)
    Spinner spNewMeterProducer;//新表厂商
    @BindView(R.id.tv_new_meter_producer)
    TextView tvNewMeterProducer;//新表厂商
    @BindView(R.id.et_new_meter_number)
    EditText etNewMeterNumber;          //新表读数
    @BindView(R.id.tv_new_meter_number)
    TextView tvNewMeterNumber;          //新表读数
    @BindView(R.id.sp_handle_result)
    Spinner spResult;   //处理结果
    /*上面是必填项，下面非必填*/
    @BindView(R.id.et_new_meter_position)
    EditText etNewMeterPosition;//新表表位
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
    private List<DUWord> meterTypeList, meterCaliberList, meterProducerList, resolveResultList;
    private int position;

    public static ReinstallHandlerFragment newInstance() {
        return new ReinstallHandlerFragment();
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
        View view = inflater.inflate(R.layout.fragment_reinstall_handler, container, false);
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
            case R.id.ib_scan:
                scan();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
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
        int meterTypePosition = spNewMeterType.getSelectedItemPosition();
        if (meterTypePosition != Spinner.INVALID_POSITION) {
            taskHandle.setMeterType(NumberUtil.transformNumber(meterTypeList.get(meterTypePosition).getValue()));
        }
        int meterCaliberPosition = spNewMeterCaliber.getSelectedItemPosition();
        if (meterCaliberPosition != Spinner.INVALID_POSITION) {
            taskHandle.setCaliber(meterCaliberList.get(meterCaliberPosition).getName());
        }
        taskHandle.setBarCode(etBarCode.getText().toString());
        int meterProducerPosition = spNewMeterProducer.getSelectedItemPosition();
        if (meterProducerPosition != Spinner.INVALID_POSITION) {
            taskHandle.setMeterProducer(NumberUtil.transformNumber(meterProducerList.get(meterProducerPosition).getValue()));
        }
        String newMeterNumber = etNewMeterNumber.getText().toString();
        taskHandle.setMeterReading(NumberUtil.transformNumber(newMeterNumber));
        int resolverResultPosition = spResult.getSelectedItemPosition();
        if (resolverResultPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolverResult(NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue()));
        }
        taskHandle.setMeterPosition(etNewMeterPosition.getText().toString());
        taskHandle.setRemark(etRemark.getText().toString());
        taskHandle.setMaterials(list);
        handle.setReply(taskHandle);
    }

    @Override
    public boolean checkDataInvalid() {
        int meterTypePosition = spNewMeterType.getSelectedItemPosition();
        int meterCaliberPosition = spNewMeterCaliber.getSelectedItemPosition();
        String barcode = etBarCode.getText().toString();
        int meterProducerPosition = spNewMeterProducer.getSelectedItemPosition();
        String newMeterNumber = etNewMeterNumber.getText().toString();
        int resolverResultPosition = spResult.getSelectedItemPosition();

        if (resolverResultPosition == Spinner.INVALID_POSITION) {
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_handle_result_invalid);
            return false;
        }

        int resolveResultValue = NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue());
        if (isNeedCheckDataInvalid(resolveResultValue)) {
            if (meterTypePosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_new_meter_type_invalid);
                return false;
            }

            if (meterCaliberPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_new_meter_caliber_invalid);
                return false;
            }

            if (TextUtil.isNullOrEmpty(barcode)) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_new_bar_code_null);
                return false;
            }

            if (meterProducerPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_new_meter_producer_invalid);
                return false;
            }

            if (TextUtil.isNullOrEmpty(newMeterNumber)) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_new_read_null);
                return false;
            }
        }

        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        if (meterTypePosition != Spinner.INVALID_POSITION) {
            taskHandle.setMeterType(NumberUtil.transformNumber(meterTypeList.get(meterTypePosition).getValue()));
        }
        if (meterCaliberPosition != Spinner.INVALID_POSITION) {
            taskHandle.setCaliber(meterCaliberList.get(meterCaliberPosition).getName());
        }
        taskHandle.setBarCode(barcode);
        if (meterProducerPosition != Spinner.INVALID_POSITION) {
            taskHandle.setMeterProducer(NumberUtil.transformNumber(meterProducerList.get(meterProducerPosition).getValue()));
        }
        taskHandle.setMeterReading(NumberUtil.transformNumber(newMeterNumber));
        taskHandle.setResolverResult(resolveResultValue);
        taskHandle.setMeterPosition(etNewMeterPosition.getText().toString());
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
    public void scanResult(String result) {
        super.scanResult(result);
        etBarCode.setText(result);
    }

    @Override
    public void getWord(int parentId, List<DUWord> list) {
        switch (parentId) {
            case ConstantUtil.ParentId.RESOLVE_RESULT:
                resolveResultList = list;
                initSpinner(spResult, handle == null ? 0 : handle.toDUTaskHandle().getResolverResult(), list);
                presenter.getWord(ConstantUtil.ParentId.METER_TYPE, ConstantUtil.Group.METER_TYPE);
                break;
            case ConstantUtil.ParentId.METER_TYPE:
                meterTypeList = list;
                initSpinner(spNewMeterType, handle == null ? ConstantUtil.Default.METER_TYPE : handle.toDUTaskHandle().getMeterType(), list);
                presenter.getWord(ConstantUtil.ParentId.METER_CALIBER, ConstantUtil.Group.METER_CALIBER);
                break;
            case ConstantUtil.ParentId.METER_CALIBER:
                meterCaliberList = list;
                initSpinner(spNewMeterCaliber, handle == null ? ConstantUtil.Default.CALIBER : handle.toDUTaskHandle().getCaliber(), list);
                presenter.getWord(ConstantUtil.ParentId.METER_PRODUCER, ConstantUtil.Group.METER_PRODUCER);
                break;
            case ConstantUtil.ParentId.METER_PRODUCER:
                meterProducerList = list;
                initSpinner(spNewMeterProducer, handle == null ? ConstantUtil.Default.METER_PRODUCER : handle.toDUTaskHandle().getMeterProducer(), list);
                break;
            default:
                break;
        }
    }

    private void initView() {
        if (handle != null) {
            DUTaskHandle taskHandle = handle.toDUTaskHandle();
            etBarCode.setText(taskHandle.getBarCode());
            int meterReading = taskHandle.getMeterReading();
            etNewMeterNumber.setText(meterReading == 0 ? TextUtil.EMPTY : String.valueOf(meterReading));
            etNewMeterPosition.setText(taskHandle.getMeterPosition());
            etRemark.setText(taskHandle.getRemark());
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
    }

    private void enableView() {
        spNewMeterType.setEnabled(permitWrite);
        spNewMeterCaliber.setEnabled(permitWrite);
        spNewMeterCaliber.setEnabled(permitWrite);
        etBarCode.setEnabled(permitWrite);
        spNewMeterProducer.setEnabled(permitWrite);
        etNewMeterNumber.setEnabled(permitWrite);
        spResult.setEnabled(permitWrite);
        etNewMeterPosition.setEnabled(permitWrite);
        etRemark.setEnabled(permitWrite);
        btnInputMaterial.setEnabled(permitWrite);
    }

    private void setOnListener() {
        ibScan.setOnClickListener(this);
        spResult.setOnItemSelectedListener(this);
        btnInputMaterial.setOnClickListener(this);
    }

    private void showHideStar(int position) {
        int value = NumberUtil.transformNumber(resolveResultList.get(position).getValue());
        int visibility = isNeedCheckDataInvalid(value) ? View.VISIBLE : View.INVISIBLE;
        tvNewMeterType.setVisibility(visibility);
        tvNewMeterCaliber.setVisibility(visibility);
        tvBarCode.setVisibility(visibility);
        tvNewMeterProducer.setVisibility(visibility);
        tvNewMeterNumber.setVisibility(visibility);
    }
}
