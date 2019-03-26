package com.sh3h.workmanagerjm.ui.detail.hot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
public class HotDetailFragment extends DetailFragment {

    @BindView(R.id.tv_work_id) TextView tvWorkId;
    @BindView(R.id.tv_hot_work_id) TextView tvHotWorkId;
    @BindView(R.id.tv_card_id) TextView tvCardId;
    @BindView(R.id.tv_station) TextView tvStation;
    @BindView(R.id.tv_response_person) TextView tvResponsePerson;
    @BindView(R.id.tv_response_type) TextView tvResponseType;
    @BindView(R.id.tv_response_content) TextView tvResponseContent;
    @BindView(R.id.tv_response_time) TextView tvResponseTime;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.ib_map) ImageButton ibMap;
    @BindView(R.id.tv_telephone) TextView tvTelephone;
    @BindView(R.id.ib_telephone) ImageButton ibTelephone;
    @BindView(R.id.tv_host_person) TextView tvHostPerson;
    @BindView(R.id.ll_host_person) LinearLayout llHostPerson;
    @BindView(R.id.tv_assist_person) TextView tvAssistPerson;
    @BindView(R.id.ll_assist_person) LinearLayout llAssistPerson;
    @BindView(R.id.tv_driver) TextView tvDriver;
    @BindView(R.id.ll_driver) LinearLayout llDriver;
    @BindView(R.id.tv_resolve_level) TextView tvResolveLevel;
    @BindView(R.id.tv_response_original) TextView tvResponseOriginal;
    @BindView(R.id.tv_response_area) TextView tvResponseArea;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_task_status) TextView tvTaskStatus;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;

    private Unbinder unbinder;

    public static HotDetailFragment newInstance() {
        return new HotDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_detail, container, false);
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
        tvWorkId.setText(String.valueOf(task.getTaskId()));
        tvHotWorkId.setText(String.valueOf(task.getHotTaskId()));
        tvCardId.setText(String.valueOf(task.getCardId()));
        tvStation.setText(task.getStation());
        tvResponsePerson.setText(task.getProcessPerson());
        tvResponseType.setText(task.getResponseType());
        tvResponseContent.setText(task.getResponseContent());
        tvResponseTime.setText(task.getStrProcessDate());
        tvAddress.setText(task.getAddress());
        tvTelephone.setText(task.getTelephone());
        tvHostPerson.setText(task.getAcceptName());
        tvAssistPerson.setText(task.getAssistPerson());
        tvDriver.setText(task.getDriver());
        tvResolveLevel.setText(task.getResolveLevel());
        tvResponseOriginal.setText(task.getTaskOrigin());
        tvResponseArea.setText(task.getResponseArea());
        tvRemark.setText(task.getRemark());
        tvTaskStatus.setText(task.getState() >= ConstantUtil.State.HANDLE
                ? R.string.text_already_resolve : R.string.text_not_resolve);
        tvRegisterTime.setText(task.getStrDispatchTime());
        tvEndTime.setText(task.getStrEndDate());
    }

    private void hideView(){
        if (duData.isNeedHideDispatchDate()){
            llRegisterTime.setVisibility(View.GONE);
            llEndTime.setVisibility(View.GONE);
        }

        if (duData.isNeedHideHostPerson()){
            llHostPerson.setVisibility(View.GONE);
            llAssistPerson.setVisibility(View.GONE);
            llDriver.setVisibility(View.GONE);
        }
    }

    private void setOnListener(){
        ibMap.setOnClickListener(this);
        ibTelephone.setOnClickListener(this);
    }

}
