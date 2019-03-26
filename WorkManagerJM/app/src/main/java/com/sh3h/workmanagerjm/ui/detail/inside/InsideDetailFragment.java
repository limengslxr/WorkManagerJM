package com.sh3h.workmanagerjm.ui.detail.inside;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.detail.DetailFragment;
import com.sh3h.workmanagerjm.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsideDetailFragment extends DetailFragment {

    @BindView(R.id.tv_task_type) TextView tvTaskType;
    @BindView(R.id.tv_task_sub_type) TextView tvTaskSubType;
    @BindView(R.id.tv_apply_person) TextView tvApplyPerson;
    @BindView(R.id.tv_telephone) TextView tvTelephone;
    @BindView(R.id.ib_telephone) ImageButton ibTelephone;
    @BindView(R.id.tv_assist_person) TextView tvAssistPerson;
    @BindView(R.id.tv_driver) TextView tvDriver;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.ib_map) ImageView ibMap;
    @BindView(R.id.tv_accept_group) TextView tvAcceptGroup;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;

    private Unbinder unbinder;

    public static InsideDetailFragment newInstance() {
        return new InsideDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDUTask();
        initView();
        hideView();
        setOnListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_telephone:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_CALL_PHONE, permissionGrant);
                break;
            case R.id.ib_map:
                navigationMap(task);
                break;
            default:
                break;
        }
    }

    @Override
    protected void checkCallPhone() {
        super.checkCallPhone();
        callPhone(task.getTelephone());
    }

    private void initView() {
        tvTaskType.setText(R.string.title_inside_list);
        switch (task.getSubType()){
            case ConstantUtil.WorkSubType.REPAIR:
                tvTaskSubType.setText(R.string.text_repair);
                break;
            case ConstantUtil.WorkSubType.ELECTRICIAN:
                tvTaskSubType.setText(R.string.text_electrician);
                break;
            case ConstantUtil.WorkSubType.OTHER:
                tvTaskSubType.setText(R.string.text_other);
                break;
            case ConstantUtil.WorkSubType.SITE_METER:
                tvTaskSubType.setText(R.string.text_site_meter);
                break;
            case ConstantUtil.WorkSubType.FLOOR_ACCEPTANCE:
                tvTaskSubType.setText(R.string.text_floor_acceptance);
                break;
            default:
                break;
        }
        tvApplyPerson.setText(task.getProcessPerson());
        tvTelephone.setText(task.getTelephone());
        tvAssistPerson.setText(task.getAssistPerson());
        tvDriver.setText(task.getDriver());
        tvAddress.setText(task.getAddress());
        tvAcceptGroup.setTag(task.getAcceptGroup());
        tvRemark.setText(task.getRemark());
        tvRegisterTime.setText(task.getStrDispatchTime());
        tvEndTime.setText(task.getStrEndDate());
    }

    private void hideView(){
        if (duData.isNeedHideDispatchDate()){
            llRegisterTime.setVisibility(View.GONE);
            llEndTime.setVisibility(View.GONE);
        }
    }

    private void setOnListener(){
        ibTelephone.setOnClickListener(this);
        ibMap.setOnClickListener(this);
    }

}
