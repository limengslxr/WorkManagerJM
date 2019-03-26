package com.sh3h.workmanagerjm.ui.handler.chargeBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ChargeBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChargeBackFragment extends HandlerFragment {

    @BindView(R.id.et_back_reason)
    EditText etBackReason;
    @BindView(R.id.tv_back_reason)
    TextView tvBackReason;
    private Unbinder unbinder;

    // TODO: Rename and change types and number of parameters
    public static ChargeBackFragment newInstance() {
        return new ChargeBackFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_back, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDUDate();
        initView();
        enableView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void temporaryStorage() {
        checkDataInvalid();
    }

    @Override
    public boolean checkDataInvalid() {
        DUTask duTask = data.getDuTask();
        duTask.setState(ConstantUtil.State.BACK);
        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        DUTaskHandle duTaskHandle = handle.toDUTaskHandle();
        duTaskHandle.setProcessReason(etBackReason.getText().toString());
        handle.setReply(duTaskHandle);
        return true;
    }

    @Override
    public boolean isNeedPicture() {
        return true;
    }

    @Override
    public void saveMaterial(DUMaterial material) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemClick(int type, int i) {

    }

    @Override
    public void onItemLongClick(int fileType, int position) {

    }

    private void initView() {
        if (handle != null) {
            DUTaskHandle duTaskHandle = handle.toDUTaskHandle();
            etBackReason.setText(duTaskHandle.getProcessReason() == null ? TextUtil.EMPTY : duTaskHandle.getProcessReason());
        }

        String entrance = data.getTaskEntrance();
        tvBackReason.setVisibility(ConstantUtil.TaskEntrance.HISTORY.equals(entrance)
                || ConstantUtil.TaskEntrance.SEARCH.equals(entrance)
                ? View.INVISIBLE : View.VISIBLE);
    }

    private void enableView() {
        etBackReason.setEnabled(permitWrite);
    }

}
