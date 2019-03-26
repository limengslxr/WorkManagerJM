package com.sh3h.workmanagerjm.ui.handler.reject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUDownloadVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;
import com.sh3h.workmanagerjm.util.TransformUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RejectHandlerFragment extends HandlerFragment {

    private Unbinder unbinder;
    @BindView(R.id.tv_task_id) TextView tvTaskId;
    @BindView(R.id.tv_task_type) TextView tvTaskType;
    @BindView(R.id.tv_verify_status) TextView tvVerifyStatus;
    @BindView(R.id.tv_reject_person) TextView tvRejectPerson;
    @BindView(R.id.tv_reject_reason) TextView tvRejectReason;
    @BindView(R.id.tv_reject_date) TextView tvRejectDate;

    public static RejectHandlerFragment newInstance() {
        return new RejectHandlerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reject_handler, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDUDate();
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemLongClick(int fileType, int position) {

    }

    @Override
    public void temporaryStorage() {

    }

    @Override
    public boolean checkDataInvalid() {
        return false;
    }

    @Override
    public boolean isNeedPicture() {
        return false;
    }

    @Override
    public void saveMaterial(DUMaterial material) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int type, int i) {

    }

    private void initView() {
        tvTaskId.setText(String.valueOf(task.getTaskId()));
        tvTaskType.setText(TransformUtil.getTaskMultiType(getContext(), task));
        tvVerifyStatus.setText(TransformUtil.getApplyTypeResourceId(data.getApplyType()));
        if (handle != null){
            DUDownloadVerifyHandle verifyHandle = handle.toDUDownloadVerifyHandle();
            tvRejectPerson.setText(verifyHandle.getApplyPerson());
            tvRejectReason.setText(verifyHandle.getApplyReason());
            tvRejectDate.setText(verifyHandle.getStrApplyTime());
        }
    }

}
