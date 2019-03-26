package com.sh3h.workmanagerjm.ui.handler.report;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUReportHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 上报工单
 */
public class ReportFragment extends HandlerFragment implements ReportMvpView{

    @BindView(R.id.et_card_number) EditText etCardId;       //表卡编号
    @BindView(R.id.et_card_name) EditText etCardName;         //户名
    @BindView(R.id.et_contact_tel) EditText telephone;         //联系电话
    @BindView(R.id.et_address) EditText etAddress;           //地址
    @BindView(R.id.et_bar_code) EditText etBarCode;           //钢印号
    @BindView(R.id.ib_scan) ImageButton ibScan;           //钢印号
    @BindView(R.id.et_new_read) EditText etNewRead;           //最新抄码
    @BindView(R.id.ib_map) ImageView ivMap;
    @BindView(R.id.sp_new_meter_order_type) Spinner spType;  //工单类型
    @BindView(R.id.ll_verify_status) LinearLayout llVerifyStatus;
    @BindView(R.id.tv_verify_status) TextView tvVerifyStatus;
    @BindView(R.id.et_describe) EditText etDescribe;          //描述
    @Inject ReportPresenter presenter;

    private Unbinder unbinder;
    private List<DUWord> list;
    private double longitude;
    private double latitude;
    private boolean isFromReport = false;

    public static ReportFragment newInstance(boolean isReport) {
        ReportFragment fragment = new ReportFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstantUtil.Key.IS_REPORT, isReport);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Bundle arguments = getArguments();
            if (arguments != null){
                isFromReport = arguments.getBoolean(ConstantUtil.Key.IS_REPORT);
            }
        } else {
            isFromReport = savedInstanceState.getBoolean(ConstantUtil.Key.IS_REPORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ((ManagerActivity) getActivity()).getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        initDUDate();
        if (isFromReport && savedInstanceState == null){
            presenter.getReportTemporary();
        } else {
            initData(savedInstanceState);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(ConstantUtil.Map.LONGITUDE, longitude);
        outState.putDouble(ConstantUtil.Map.LATITUDE, latitude);
        outState.putBoolean(ConstantUtil.Key.IS_REPORT, isFromReport);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_map:
                markMap();
                break;
            case R.id.ib_scan:
                scan();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void temporaryStorage() {
        String cardName = etCardName.getText().toString();
        String address = etAddress.getText().toString();
        task.setCardName(cardName);
        task.setAddress(address);

        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.SAVE);
        DUReportHandle reportHandle = handle.toDUReportHandle();
        reportHandle.setCardId(etCardId.getText().toString());
        reportHandle.setCardName(cardName);
        reportHandle.setTelephone(telephone.getText().toString());
        String meterReading = etNewRead.getText().toString();
        reportHandle.setMeterReading(TextUtil.isNullOrEmpty(meterReading) ? 0 : Integer.parseInt(meterReading));
        reportHandle.setBarCode(etBarCode.getText().toString());
        reportHandle.setAddress(address);
        reportHandle.setLongitude(longitude);
        reportHandle.setLatitude(latitude);
        int position = spType.getSelectedItemPosition();
        if (position != Spinner.INVALID_POSITION) {
            reportHandle.setType(getWorkType(list.get(position).getName()));
        }
        reportHandle.setRemark(etDescribe.getText().toString());
        handle.setReply(reportHandle);
    }

    @Override
    public boolean checkDataInvalid() {
        String address = etAddress.getText().toString();
        if (TextUtil.isNullOrEmpty(address)) {
            ApplicationsUtil.showMessage(getActivity(), R.string.card_address_null);
            return false;
        }

        if (Math.abs(latitude) < ConstantUtil.SMALL_NUMBER || Math.abs(longitude) < ConstantUtil.SMALL_NUMBER){
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_invalid_coordinate);
            return false;
        }

        int position = spType.getSelectedItemPosition();
        if (position == Spinner.INVALID_POSITION) {
            ApplicationsUtil.showMessage(getActivity(), R.string.order_type_null);
            return false;
        }

        String remark = etDescribe.getText().toString();
        if (TextUtil.isNullOrEmpty(remark)) {
            ApplicationsUtil.showMessage(getActivity(), R.string.card_remark_null);
            return false;
        }

        String meterReading = etNewRead.getText().toString();
        String cardName = etCardName.getText().toString();
        task.setCardName(cardName);
        task.setAddress(address);

        handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Report);
        DUReportHandle reportHandle = handle.toDUReportHandle();
        reportHandle.setCardId(etCardId.getText().toString());
        reportHandle.setCardName(cardName);
        reportHandle.setTelephone(telephone.getText().toString());
        reportHandle.setMeterReading(TextUtil.isNullOrEmpty(meterReading) ? 0 : Integer.parseInt(meterReading));
        reportHandle.setBarCode(etBarCode.getText().toString());
        reportHandle.setAddress(address);
        reportHandle.setLongitude(longitude);
        reportHandle.setLatitude(latitude);
        reportHandle.setType(getWorkType(list.get(position).getName()));
        reportHandle.setRemark(remark);
        handle.setReply(reportHandle);
        return true;
    }

    @Override
    public boolean isNeedPicture() {
        return false;
    }

    @Override
    public void onItemLongClick(int fileType, int position) {

    }

    @Override
    public void onItemClick(int type, int i) {

    }

    @Override
    public void saveMaterial(DUMaterial material) {

    }

    @Override
    public void scanResult(String result) {
        super.scanResult(result);
        etBarCode.setText(result);
    }

    @Override
    public void getReportHistory(DUData data) {
        this.data.setTaskEntrance(data.getTaskEntrance());
        this.data.setOperateType(data.getOperateType());
        this.data.setDuTask(data.getDuTask());
        this.data.setHandles(data.getHandles());
        initDUDate();
        initData(null);
    }

    public void markMap(Intent intent) {
        if (intent == null) {
            return;
        }

        double longitude = intent.getDoubleExtra(ConstantUtil.Map.LONGITUDE, 0.0);
        double latitude = intent.getDoubleExtra(ConstantUtil.Map.LATITUDE, 0.0);
        if (Math.abs(latitude) > ConstantUtil.Map.ERROR_VALUE
                && Math.abs(longitude) > ConstantUtil.Map.ERROR_VALUE) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    private int getWorkType(String name) {
        int i;
        switch (name) {
            case ConstantUtil.SelfReportType.WORK_TASK:
                i = ConstantUtil.SelfReportType.WORK_TASK_NUMBER;
                break;
            case ConstantUtil.SelfReportType.OFFICIAL_TASK:
                i = ConstantUtil.SelfReportType.OFFICIAL_TASK_NUMBER;
                break;
            case ConstantUtil.SelfReportType.LEAKAGE:
                i = ConstantUtil.SelfReportType.LEAKAGE_NUMBER;
                break;
            case ConstantUtil.SelfReportType.ILLEGAL_WATER:
                i = ConstantUtil.SelfReportType.ILLEGAL_WATER_NUMBER;
                break;
            case ConstantUtil.SelfReportType.CHANGE_METER:
                i = ConstantUtil.SelfReportType.CHANGE_METER_NUMBER;
                break;
            case ConstantUtil.SelfReportType.ADJUST_INFO:
                i = ConstantUtil.SelfReportType.ADJUST_INFO_NUMBER;
                break;
            case ConstantUtil.SelfReportType.OTHER:
                i = ConstantUtil.SelfReportType.OTHER_NUMBER;
                break;
            default:
                i = 0;
                break;
        }
        return i;
    }

    private void initMap(Bundle bundle){
        if (bundle != null){
            longitude = bundle.getDouble(ConstantUtil.Map.LONGITUDE);
            latitude = bundle.getDouble(ConstantUtil.Map.LATITUDE);
        }
    }

    private void initView() {
        if (handle != null){
            DUReportHandle reportHandle = handle.toDUReportHandle();
            etCardId.setText(reportHandle.getCardId());
            etCardName.setText(reportHandle.getCardName());
            telephone.setText(reportHandle.getTelephone());
            etAddress.setText(reportHandle.getAddress());
            etDescribe.setText(reportHandle.getRemark());
            etNewRead.setText(String.valueOf(reportHandle.getMeterReading()));
            etBarCode.setText(reportHandle.getBarCode());
            longitude = reportHandle.getLongitude();
            latitude = reportHandle.getLatitude();
        }

        if (ConstantUtil.TaskEntrance.VERIFY.equals(data.getTaskEntrance())){
            llVerifyStatus.setVisibility(View.VISIBLE);
            tvVerifyStatus.setText(R.string.title_report);
        }

        initReportSpinner(spType, handle);
    }

    private void enableView() {
        etCardId.setEnabled(permitWrite);
        etCardName.setEnabled(permitWrite);
        telephone.setEnabled(permitWrite);
        etAddress.setEnabled(permitWrite);
        etBarCode.setEnabled(permitWrite);
        ibScan.setEnabled(permitWrite);
        etNewRead.setEnabled(permitWrite);
        spType.setEnabled(permitWrite);
        etDescribe.setEnabled(permitWrite);
    }

    private void setOnListener(){
        ibScan.setOnClickListener(this);
        ivMap.setOnClickListener(this);
    }

    private void markMap() {
        Intent intent = MapUtil.getMapIntent(getContext());
        if (intent != null) {
            intent.putExtra(ConstantUtil.Map.MAP_STATUS, handle != null
                    && !ConstantUtil.TaskEntrance.REPORT.equals(data.getTaskEntrance())?
                            ConstantUtil.Map.LOCATE_MAP : ConstantUtil.Map.MARK_MAP);
            intent.putExtra(ConstantUtil.Map.LONGITUDE, longitude);
            intent.putExtra(ConstantUtil.Map.LATITUDE, latitude);
            intent.putExtra(ConstantUtil.Map.ADDRESS, etAddress.getText().toString());
            getActivity().startActivityForResult(intent, ConstantUtil.RequestCode.LOCATE_MAP);
        }
    }

    private void initReportSpinner(Spinner spinner, DUHandle handle) {
        list = new ArrayList<>();
        list.add(new DUWord(ConstantUtil.SelfReportType.WORK_TASK, String.valueOf(ConstantUtil.SelfReportType.WORK_TASK_NUMBER)));
//        list.add(new DUWord(ConstantUtil.SelfReportType.OFFICIAL_TASK, ConstantUtil.SelfReportType.OFFICIAL_TASK_NUMBER));
//        list.add(new DUWord(ConstantUtil.SelfReportType.LEAKAGE, ConstantUtil.SelfReportType.LEAKAGE_NUMBER));
//        list.add(new DUWord(ConstantUtil.SelfReportType.ILLEGAL_WATER, ConstantUtil.SelfReportType.ILLEGAL_WATER_NUMBER));
//        list.add(new DUWord(ConstantUtil.SelfReportType.CHANGE_METER, ConstantUtil.SelfReportType.CHANGE_METER_NUMBER));
//        list.add(new DUWord(ConstantUtil.SelfReportType.ADJUST_INFO, ConstantUtil.SelfReportType.ADJUST_INFO_NUMBER));
//        list.add(new DUWord(ConstantUtil.SelfReportType.OTHER, ConstantUtil.SelfReportType.OTHER_NUMBER));
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(list);
        spinner.setAdapter(adapter);
        int type = handle == null ? 0 : handle.toDUReportHandle().getType();
        int position = 0;
        int index = 0;
        for (DUWord word : list) {
            if (String.valueOf(type).equals(word.getValue())) {
                position = index;
                break;
            }
            index++;
        }
        spinner.setSelection(position, true);
    }

    private void initData(Bundle bundle){
        initMap(bundle);
        initView();
        enableView();
        setOnListener();
    }

}
