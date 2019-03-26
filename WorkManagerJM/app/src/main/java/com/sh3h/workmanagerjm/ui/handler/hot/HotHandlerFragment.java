package com.sh3h.workmanagerjm.ui.handler.hot;


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
public class HotHandlerFragment extends HandlerFragment implements HotHandlerMvpView,
        AdapterView.OnItemSelectedListener {

    @Inject
    HotHandlerPresenter presenter;
    @BindView(R.id.sp_handle_type)
    Spinner spResolveType;  //处理类别
    @BindView(R.id.tv_handle_type)
    TextView tvResolveType;  //处理类别
    @BindView(R.id.sp_handle_content)
    Spinner spResolveContent;  //处理内容
    @BindView(R.id.tv_handle_content)
    TextView tvResolveContent;  //处理内容
    @BindView(R.id.sp_handle_result)
    Spinner spResult;   //处理结果
    @BindView(R.id.sp_handle_department)
    Spinner spResolveDepartment;   //处理部门
    @BindView(R.id.tv_handle_department)
    TextView tvResolveDepartment;   //处理部门
    @BindView(R.id.sp_valve_type)
    Spinner spValveType;   //表阀门分类
    @BindView(R.id.tv_valve_type)
    TextView tvValveType;   //表阀门分类
    @BindView(R.id.sp_new_meter_caliber)
    Spinner spCaliber;   //口径
    @BindView(R.id.tv_new_meter_caliber)
    TextView tvCaliber;   //口径
    @BindView(R.id.sp_happen_reason)
    Spinner spHappenReason;   //发生原因
    @BindView(R.id.tv_happen_reason)
    TextView tvHappenReason;   //发生原因
    @BindView(R.id.sp_resolve_method)
    Spinner spResolveMethod; //处理方式
    @BindView(R.id.tv_resolve_method)
    TextView tvResolveMethod; //处理方式
    @BindView(R.id.et_remark)
    EditText etRemark;//备注
    @BindView(R.id.tv_remark)
    TextView tvRemark;//备注
    /*上面是必填项，下面非必填*/
    @BindView(R.id.btn_input_material)
    Button btnInputMaterial;
    @BindView(R.id.rv_material)
    RecyclerView rvMaterial;

    private OnInputMaterialListener listener;
    private InputMaterialAdapter adapter;
    private Unbinder unbinder;
    private ArrayList<DUMaterial> list;
    private List<DUWord> resolveTypeList, resolveContentList, resolveDepartmentList,
            meterValveList, caliberList, happenReasonList, resolveResultList, resolveMethodList;
    private int position;

    public static HotHandlerFragment newInstance() {
        return new HotHandlerFragment();
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
        View view = inflater.inflate(R.layout.fragment_hot_handle, container, false);
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
            case R.id.sp_handle_type:
                int resolveTypePosition = spResolveType.getSelectedItemPosition();
                if (resolveTypePosition == Spinner.INVALID_POSITION) {
                    return;
                }
                presenter.getWord(resolveTypeList.get(resolveTypePosition).getId(), ConstantUtil.Group.HAPPEN_REASON);
                presenter.getWord(resolveTypeList.get(resolveTypePosition).getId(), ConstantUtil.Group.RESOLVE_CONTENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void temporaryStorage() {
        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.SAVE);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        int resolveTypePosition = spResolveType.getSelectedItemPosition();
        if (resolveTypePosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveType(resolveTypeList.get(resolveTypePosition).getName());
        }
        int resolveContentPosition = spResolveContent.getSelectedItemPosition();
        if (resolveContentPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveContent(resolveContentList.get(resolveContentPosition).getName());
        }
        int resolverResultPosition = spResult.getSelectedItemPosition();
        if (resolverResultPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolverResult(NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue()));
        }
        taskHandle.setRemark(etRemark.getText().toString());
        int resolverDepartmentPosition = spResolveDepartment.getSelectedItemPosition();
        if (resolverDepartmentPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveDepartment(resolveDepartmentList.get(resolverDepartmentPosition).getName());
        }
        int valveTyePosition = spValveType.getSelectedItemPosition();
        if (valveTyePosition != Spinner.INVALID_POSITION) {
            taskHandle.setValveType(meterValveList.get(valveTyePosition).getName());
        }
        int caliberPosition = spCaliber.getSelectedItemPosition();
        if (caliberPosition != Spinner.INVALID_POSITION) {
            taskHandle.setCaliber(caliberList.get(caliberPosition).getName());
        }
        int happenReasonPosition = spHappenReason.getSelectedItemPosition();
        if (happenReasonPosition != Spinner.INVALID_POSITION) {
            taskHandle.setProcessReason(happenReasonList.get(happenReasonPosition).getName());
        }
        int resolveMethodPosition = spResolveMethod.getSelectedItemPosition();
        if (resolveMethodPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveMethod(NumberUtil.transformNumber(resolveMethodList.get(resolveMethodPosition).getValue()));
        }
        taskHandle.setMaterials(list);
        handle.setReply(taskHandle);
    }

    @Override
    public boolean checkDataInvalid() {
        int resolveTypePosition = spResolveType.getSelectedItemPosition();
        int resolveContentPosition = spResolveContent.getSelectedItemPosition();
        int resolverResultPosition = spResult.getSelectedItemPosition();
        String remark = etRemark.getText().toString();
        int resolverDepartmentPosition = spResolveDepartment.getSelectedItemPosition();
        int valveTyePosition = spValveType.getSelectedItemPosition();
        int caliberPosition = spCaliber.getSelectedItemPosition();
        int happenReasonPosition = spHappenReason.getSelectedItemPosition();
        int resolveMethodPosition = spResolveMethod.getSelectedItemPosition();

        if (resolverResultPosition == Spinner.INVALID_POSITION) {
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_handle_result_invalid);
            return false;
        }

        int resolveResultValue = NumberUtil.transformNumber(resolveResultList.get(resolverResultPosition).getValue());
        if (isNeedCheckDataInvalid(resolveResultValue)) {
            if (resolveTypePosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_resolve_type_invalid);
                return false;
            }

            if (resolveContentPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_resolve_content_invalid);
                return false;
            }

            if (TextUtil.isNullOrEmpty(remark)) {
                ApplicationsUtil.showMessage(getActivity(), R.string.card_resolve_remark_null);
                return false;
            }

            if (resolverDepartmentPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_resolve_department_invalid);
                return false;
            }

            if (valveTyePosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_valve_type_invalid);
                return false;
            }

            if (caliberPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_caliber_invalid);
                return false;
            }

            if (happenReasonPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_happen_reason_invalid);
                return false;
            }

            if (resolveMethodPosition == Spinner.INVALID_POSITION) {
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_resolve_method_invalid);
                return false;
            }
        }

        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        if (resolveTypePosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveType(resolveTypeList.get(resolveTypePosition).getName());
        }
        if (resolveContentPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveContent(resolveContentList.get(resolveContentPosition).getName());
        }
        taskHandle.setResolverResult(resolveResultValue);
        taskHandle.setRemark(remark);
        if (resolverDepartmentPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveDepartment(resolveDepartmentList.get(resolverDepartmentPosition).getName());
        }
        if (valveTyePosition != Spinner.INVALID_POSITION) {
            taskHandle.setValveType(meterValveList.get(valveTyePosition).getName());
        }
        if (caliberPosition != Spinner.INVALID_POSITION) {
            taskHandle.setCaliber(caliberList.get(caliberPosition).getName());
        }
        if (happenReasonPosition != Spinner.INVALID_POSITION) {
            taskHandle.setProcessReason(happenReasonList.get(happenReasonPosition).getName());
        }
        if (resolveMethodPosition != Spinner.INVALID_POSITION) {
            taskHandle.setResolveMethod(NumberUtil.transformNumber(resolveMethodList.get(resolveMethodPosition).getValue()));
        }
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
    public void getWord(String group, List<DUWord> list) {
        switch (group) {
            case ConstantUtil.Group.RESOLVE_RESULT:
                resolveResultList = list;
                initSpinner(spResult, handle == null
                        ? 0 : handle.toDUTaskHandle().getResolverResult(), list);
                presenter.getWord(ConstantUtil.ParentId.RESOLVE_TYPE, ConstantUtil.Group.RESOLVE_TYPE);
                break;
            case ConstantUtil.Group.RESOLVE_TYPE:
                resolveTypeList = list;
                //处理类别默认和反映类型一致，支持修改
                initSpinner(spResolveType, handle == null
                        ? task.getResponseType() : handle.toDUTaskHandle().getResolveType(), list);
                presenter.getWord(ConstantUtil.ParentId.RESOLVE_DEPARTMENT, ConstantUtil.Group.RESOLVE_DEPARTMENT);
                break;
            case ConstantUtil.Group.RESOLVE_DEPARTMENT:
                resolveDepartmentList = list;
                initSpinner(spResolveDepartment, handle == null
                        ? TextUtil.EMPTY : handle.toDUTaskHandle().getResolveDepartment(), list);
                presenter.getWord(ConstantUtil.ParentId.VALVE_TYPE, ConstantUtil.Group.VALVE_TYPE);
                break;
            case ConstantUtil.Group.VALVE_TYPE:
                meterValveList = list;
                initSpinner(spValveType, handle == null
                        ? TextUtil.EMPTY : handle.toDUTaskHandle().getValveType(), list);
                presenter.getWord(ConstantUtil.ParentId.METER_CALIBER, ConstantUtil.Group.METER_CALIBER);
                break;
            case ConstantUtil.Group.METER_CALIBER:
                caliberList = list;
                initSpinner(spCaliber, handle == null ? ConstantUtil.Default.CALIBER : handle.toDUTaskHandle().getCaliber(), list);
                break;
            case ConstantUtil.Group.HAPPEN_REASON:
                happenReasonList = list;
                initSpinner(spHappenReason, handle == null
                        ? TextUtil.EMPTY : handle.toDUTaskHandle().getProcessReason(), list);
                break;
            case ConstantUtil.Group.RESOLVE_CONTENT:
                resolveContentList = list;
                //处理內容默认和反映內容一致，支持修改
                initSpinner(spResolveContent, handle == null
                        ? task.getResponseContent() : handle.toDUTaskHandle().getResolveContent(), list);
                break;
            default:
                break;
        }
    }

    private void initView() {
        if (handle != null) {
            DUTaskHandle taskHandle = handle.toDUTaskHandle();
            etRemark.setText(taskHandle.getRemark() == null ? TextUtil.EMPTY : taskHandle.getRemark());
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

        resolveMethodList = new ArrayList<>();
        resolveMethodList.add(new DUWord(getString(R.string.text_site_resolve), String.valueOf(ConstantUtil.ResolveMethod.ARRIVE)));
        resolveMethodList.add(new DUWord(getString(R.string.text_phone_resolve), String.valueOf(ConstantUtil.ResolveMethod.PHONE)));
        initSpinner(spResolveMethod, handle == null
                ? 0 : handle.toDUTaskHandle().getResolveMethod(), resolveMethodList);
    }

    private void enableView() {
        spResolveType.setEnabled(permitWrite);
        spResolveContent.setEnabled(permitWrite);
        spResult.setEnabled(permitWrite);
        spResolveDepartment.setEnabled(permitWrite);
        spValveType.setEnabled(permitWrite);
        spCaliber.setEnabled(permitWrite);
        spHappenReason.setEnabled(permitWrite);
        spResolveMethod.setEnabled(permitWrite);
        etRemark.setEnabled(permitWrite);
        btnInputMaterial.setEnabled(permitWrite);
    }

    private void setOnListener() {
        btnInputMaterial.setOnClickListener(this);
        spResult.setOnItemSelectedListener(this);
        spResolveType.setOnItemSelectedListener(this);
    }

    private void showHideStar(int position) {
        int value = NumberUtil.transformNumber(resolveResultList.get(position).getValue());
        int visibility = isNeedCheckDataInvalid(value) ? View.VISIBLE : View.INVISIBLE;
        tvResolveType.setVisibility(visibility);
        tvResolveContent.setVisibility(visibility);
        tvResolveDepartment.setVisibility(visibility);
        tvValveType.setVisibility(visibility);
        tvCaliber.setVisibility(visibility);
        tvHappenReason.setVisibility(visibility);
        tvResolveMethod.setVisibility(visibility);
        tvRemark.setVisibility(visibility);
    }
}