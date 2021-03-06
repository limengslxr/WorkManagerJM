package com.sh3h.workmanagerjm.ui.detail.check;

import android.os.Bundle;
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
 * create an instance of this fragment.
 */
public class CheckDetailFragment extends DetailFragment{

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
    @BindView(R.id.tv_meter_degree) TextView tvMeterDegree;
    @BindView(R.id.tv_check_reason) TextView tvCheckReason;
    @BindView(R.id.tv_average_water) TextView tvAverageWater;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_accept_station) TextView tvStation;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;
    
    private Unbinder unbinder;

    public static CheckDetailFragment newInstance() {
        return  new CheckDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_detail, container, false);
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
        tvMeterDegree.setText(task.getMeterDegree());
        tvCheckReason.setText(task.getProcessReason());
        tvAverageWater.setText(task.getArrearageMoney());
        tvRemark.setText(task.getRemark());
        tvStation.setText(task.getStation());
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
