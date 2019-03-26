package com.sh3h.workmanagerjm.ui.detail.callPay;


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
public class CallPayDetailFragment extends DetailFragment {

    @BindView(R.id.tv_card_name) TextView tvCardName;
    @BindView(R.id.tv_card_id) TextView tvCardId;
    @BindView(R.id.tv_volume_id) TextView tvVolumeId;
    @BindView(R.id.tv_volume_index) TextView tvVolumeIndex;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.ib_map) ImageButton ibMap;
    @BindView(R.id.tv_contact) TextView tvContact;
    @BindView(R.id.tv_telephone) TextView tvTelephone;
    @BindView(R.id.ib_telephone) ImageButton ibTelephone;
    @BindView(R.id.tv_task_original) TextView tvTaskOriginal;
    //欠费时间跨度
    @BindView(R.id.tv_arrears_time_range) TextView tvArrearsTimeRange;
    //欠费金额
    @BindView(R.id.tv_the_amount_of_arrears) TextView tvMoneyOfArrears;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;

    private Unbinder unbinder;

    public static CallPayDetailFragment newInstance() {
        return new CallPayDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_pay_detail, container, false);
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
        tvVolumeId.setText(task.getVolume());
        tvVolumeIndex.setText(String.valueOf(task.getVolumeIndex()));
        tvAddress.setText(task.getAddress());
        tvContact.setText(task.getContacts());
        tvTelephone.setText(task.getTelephone());
        tvTaskOriginal.setText(task.getTaskOrigin());
        tvArrearsTimeRange.setText(task.getArrearageRange());
        tvMoneyOfArrears.setText(task.getArrearageMoney());
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
        ibMap.setOnClickListener(this);
        ibTelephone.setOnClickListener(this);
    }

}
