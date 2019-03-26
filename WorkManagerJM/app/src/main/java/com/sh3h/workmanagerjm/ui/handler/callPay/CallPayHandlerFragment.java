package com.sh3h.workmanagerjm.ui.handler.callPay;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class CallPayHandlerFragment extends HandlerFragment {

    @BindView(R.id.sp_call_pay_method)
    Spinner spCallPayMethod;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.cb_stop_water)
    CheckBox cbStopWater;

    private Unbinder unbinder;

    public static CallPayHandlerFragment newInstance() {
        return new CallPayHandlerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_pay_handle, container, false);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void temporaryStorage() {
        checkDataInvalid();
    }

    @Override
    public boolean checkDataInvalid() {
        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        DUTaskHandle duTaskHandle = handle.toDUTaskHandle();
        duTaskHandle.setUrgingPaymentMethod(spCallPayMethod.getSelectedItemPosition() == 0
                ? ConstantUtil.CallPayMethod.PHONE : ConstantUtil.CallPayMethod.ARRIVE);
        duTaskHandle.setStopWater(cbStopWater.isChecked()
                ? ConstantUtil.StopWater.OK : ConstantUtil.StopWater.NO);
        duTaskHandle.setRemark(etRemark.getText().toString());
        handle.setReply(duTaskHandle);
        return true;
    }

    @Override
    public boolean isNeedPicture() {
        //电话催缴不需要照片
        return spCallPayMethod.getSelectedItemPosition() != 0;
    }

    @Override
    public void saveMaterial(DUMaterial material) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemLongClick(int fileType, int position) {

    }

    @Override
    public void onItemClick(int type, int i) {

    }

    private void initView() {
        List<DUWord> list = new ArrayList<>();
        list.add(new DUWord(getString(R.string.text_call_pay_by_phone), String.valueOf(ConstantUtil.CallPayMethod.PHONE)));
        list.add(new DUWord(getString(R.string.text_call_pay_by_arrive), String.valueOf(ConstantUtil.CallPayMethod.ARRIVE)));
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(list);
        spCallPayMethod.setAdapter(adapter);

        if (handle != null) {
            DUTaskHandle duTaskHandle = handle.toDUTaskHandle();
            spCallPayMethod.setSelection(duTaskHandle.getUrgingPaymentMethod() == ConstantUtil.CallPayMethod.ARRIVE ? 1 : 0);
            etRemark.setText(duTaskHandle.getRemark() == null ? TextUtil.EMPTY : duTaskHandle.getRemark());
            cbStopWater.setChecked(duTaskHandle.getStopWater() == ConstantUtil.StopWater.OK);
        }
    }

    private void enableView() {
        spCallPayMethod.setEnabled(permitWrite);
        etRemark.setEnabled(permitWrite);
        cbStopWater.setEnabled(permitWrite);
    }
}
