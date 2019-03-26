package com.sh3h.workmanagerjm.ui.detail.reinstall;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.detail.DetailFragment;
import com.sh3h.workmanagerjm.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReinstallDetailFragment extends DetailFragment {
    @BindView(R.id.tv_card_id) TextView tvCardId;
    @BindView(R.id.tv_card_name) TextView tvCardName;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.ib_map) ImageButton ibMap;
    @BindView(R.id.tv_caliber_value) TextView tvCaliber;
    @BindView(R.id.tv_bar_code) TextView tvBarCode;
    @BindView(R.id.tv_meter_position) TextView tvMeterPosition;
    @BindView(R.id.tv_meter_type) TextView tvMeterType;
    @BindView(R.id.tv_contact) TextView tvContact;
    @BindView(R.id.tv_telephone) TextView tvTelephone;
    @BindView(R.id.ib_telephone) ImageButton ibTelephone;
    @BindView(R.id.tv_host_person) TextView tvHostPerson;
    @BindView(R.id.ll_host_person) LinearLayout llHostPerson;
    @BindView(R.id.tv_assist_person) TextView tvAssistPerson;
    @BindView(R.id.ll_assist_person) LinearLayout llAssistPerson;
    @BindView(R.id.tv_driver) TextView tvDriver;
    @BindView(R.id.ll_driver) LinearLayout llDriver;
    @BindView(R.id.tv_average_water) TextView tvAverageWater;
    @BindView(R.id.tv_reinstall_reason) TextView tvReinstallReason;
    @BindView(R.id.tv_task_original) TextView tvTaskOriginal;
    @BindView(R.id.tv_task_type) TextView tvTaskType;
    @BindView(R.id.tv_last_read) TextView tvLastRead;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;

    private Unbinder unbinder;

    public static ReinstallDetailFragment newInstance() {
        return new ReinstallDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reinstall_detail, container, false);
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
        tvCardId.setText(task.getCardId());
        tvCardName.setText(task.getCardName());
        tvAddress.setText(task.getAddress());
        tvCaliber.setText(task.getCaliber());
        tvBarCode.setText(task.getBarCode());
        tvMeterPosition.setText(task.getMeterPosition());
        tvMeterType.setText(task.getMeterType());
        tvContact.setText(task.getContacts());
        tvTelephone.setText(task.getTelephone());
        tvHostPerson.setText(task.getAcceptName());
        tvAssistPerson.setText(task.getAssistPerson());
        tvDriver.setText(task.getDriver());
        tvAverageWater.setText(task.getAverageWaterQuantity());
        tvReinstallReason.setText(task.getProcessReason());
        tvTaskOriginal.setText(task.getTaskOrigin());
        tvTaskType.setText(R.string.text_sub_type_reinstall);
        tvLastRead.setText(String.valueOf(task.getLastReading()));
        tvRemark.setText(task.getRemark());
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
        ibTelephone.setOnClickListener(this);
        ibMap.setOnClickListener(this);
    }

}
