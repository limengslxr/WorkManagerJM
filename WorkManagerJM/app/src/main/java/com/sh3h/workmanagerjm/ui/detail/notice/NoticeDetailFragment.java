package com.sh3h.workmanagerjm.ui.detail.notice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.detail.DetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeDetailFragment extends DetailFragment {

    @BindView(R.id.tv_host_person) TextView tvHostPerson;
    @BindView(R.id.ll_host_person) LinearLayout llHostPerson;
    @BindView(R.id.tv_assist_person) TextView tvAssistPerson;
    @BindView(R.id.ll_assist_person) LinearLayout llAssistPerson;
    @BindView(R.id.tv_driver) TextView tvDriver;
    @BindView(R.id.ll_driver) LinearLayout llDriver;
    @BindView(R.id.tv_remark) TextView tvRemark;
    @BindView(R.id.tv_task_original) TextView tvTaskOriginal;
    @BindView(R.id.tv_task_type) TextView tvTaskType;
    @BindView(R.id.tv_register_time) TextView tvRegisterTime;
    @BindView(R.id.ll_register_time) LinearLayout llRegisterTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.ll_end_time) LinearLayout llEndTime;

    private Unbinder unbinder;

    public static NoticeDetailFragment newInstance() {
        return new NoticeDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDUTask();
        hideView();
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
    }

    private void initView() {
        tvHostPerson.setText(task.getAcceptName());
        tvAssistPerson.setText(task.getAssistPerson());
        tvDriver.setText(task.getDriver());
        tvRemark.setText(task.getRemark());
        tvTaskOriginal.setText(task.getTaskOrigin());
        tvTaskType.setText(R.string.title_notice);
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

}