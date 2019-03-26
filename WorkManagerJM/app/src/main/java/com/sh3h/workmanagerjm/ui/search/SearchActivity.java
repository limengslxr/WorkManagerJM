package com.sh3h.workmanagerjm.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.qrcode.Intents;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.list.ListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends ParentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.et_task_id) EditText etTaskId;
    @BindView(R.id.sp_task_type) Spinner spTaskType;
    @BindView(R.id.sp_task_sub_type) Spinner spSubType;
    @BindView(R.id.et_card_id) EditText etCardId;
    @BindView(R.id.et_card_name) EditText etCardName;
    @BindView(R.id.et_address) EditText etAddress;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_resolve_person) EditText etResolvePerson;
    @BindView(R.id.tv_resolve_time) TextView tvResolveTime;
    @BindView(R.id.et_bar_code) EditText etBarCode;
    @BindView(R.id.ib_scan) ImageButton ibScan;
    @BindView(R.id.cb_fuzzy_search) CheckBox cbFuzzySearch;
    @BindView(R.id.tv_search) TextView tvSearch;

    private Unbinder unbinder;
    private long resolveTime;
    private List<DUWord> typeList, subTypeList, invalidSubTypeList;
    private SpinnerAdapter subTypeAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        unbinder = ButterKnife.bind(this);

        data.setTaskEntrance(ConstantUtil.TaskEntrance.SEARCH);
        saveUserInfo(getIntent());

        initToolBar();

        initView();

        setOnListener();
        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null || resultCode != Activity.RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null){
            ApplicationsUtil.showMessage(this, R.string.toast_scan_bar_code_error);
            return;
        }
        String scanResult = bundle.getString(Intents.Scan.RESULT);
        if (TextUtil.isNullOrEmpty(scanResult)){
            ApplicationsUtil.showMessage(this, R.string.toast_scan_bar_code_error);
            return;
        }
        etBarCode.setText(scanResult);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                search();
                break;
            case R.id.tv_resolve_time:
                selectTime(ConstantUtil.SelectTimeType.NORMAL);
                break;
            case R.id.ib_scan:
                startActivityForResult(new Intent(Intents.Scan.ACTION), ConstantUtil.RequestCode.SCAN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_task_type:
                subTypeAdapter.setList(position == 0 ? subTypeList : invalidSubTypeList);
                subTypeAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void selectTimeResult(int type, long time) {
        super.selectTimeResult(type, time);
        switch (type){
            case ConstantUtil.SelectTimeType.NORMAL:
                resolveTime = time;
                tvResolveTime.setText(TextUtil.format(resolveTime, TextUtil.FORMAT_DATE));
                break;
            default:
                break;
        }

    }

    private void search() {
        String taskIdStr = etTaskId.getText().toString();
        String cardId = etCardId.getText().toString();
        String cardName = etCardName.getText().toString();
        String address = etAddress.getText().toString();
        String resolvePerson = etResolvePerson.getText().toString();
        String phone = etPhone.getText().toString();
        String gangYinHao = etBarCode.getText().toString();
        if (TextUtil.isNullOrEmpty(taskIdStr) && TextUtil.isNullOrEmpty(cardId)
                && TextUtil.isNullOrEmpty(cardName) &&TextUtil.isNullOrEmpty(address)
                && TextUtil.isNullOrEmpty(phone) && TextUtil.isNullOrEmpty(gangYinHao)
                && spTaskType.getSelectedItemPosition() == Spinner.INVALID_POSITION
                && resolveTime == 0 && TextUtil.isNullOrEmpty(resolvePerson)) {
            ApplicationsUtil.showMessage(this, R.string.search_content_can_not_null);
            return;
        }

        long taskId = TextUtil.EMPTY.equals(taskIdStr.trim()) ? ConstantUtil.UPLOAD_NULL : Long.parseLong(taskIdStr);
        cardName = TextUtil.EMPTY.equals(cardName.trim()) ? String.valueOf(ConstantUtil.UPLOAD_NULL) : cardName;
        address = TextUtil.EMPTY.equals(address.trim()) ? String.valueOf(ConstantUtil.UPLOAD_NULL) : address;
        phone = TextUtil.EMPTY.equals(phone.trim()) ? String.valueOf(ConstantUtil.UPLOAD_NULL) : phone;
        gangYinHao = TextUtil.EMPTY.equals(gangYinHao.trim()) ? String.valueOf(ConstantUtil.UPLOAD_NULL) : gangYinHao;
        int position = spTaskType.getSelectedItemPosition();
        int type = position == Spinner.INVALID_POSITION
                ? ConstantUtil.UPLOAD_NULL : Integer.parseInt(typeList.get(position).getValue());
        position = spSubType.getSelectedItemPosition();
        int subType = position == Spinner.INVALID_POSITION || position == 0
                ? ConstantUtil.UPLOAD_NULL : Integer.parseInt(subTypeList.get(position).getValue());

        searchTaskWithCondition(taskId, type, subType, cardId, cardName, address, resolvePerson,
                resolveTime, gangYinHao, phone, cbFuzzySearch.isChecked());

        Intent intent = new Intent(SearchActivity.this, ListActivity.class);
        startActivity(intent);
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.title_task_search);
        setSupportActionBar(toolbar);
    }

    private void initView(){
        typeList = new ArrayList<>();
        typeList.add(new DUWord(getString(R.string.title_order_bw_list), String.valueOf(ConstantUtil.WorkType.TASK_BIAOWU)));
        typeList.add(new DUWord(getString(R.string.title_call_pay_list), String.valueOf(ConstantUtil.WorkType.TASK_CALL_PAY)));
        typeList.add(new DUWord(getString(R.string.title_meter_install_list), String.valueOf(ConstantUtil.WorkType.TASK_INSTALL_METER)));
        typeList.add(new DUWord(getString(R.string.title_inside), String.valueOf(ConstantUtil.WorkType.TASK_INSIDE)));
        typeList.add(new DUWord(getString(R.string.title_hot), String.valueOf(ConstantUtil.WorkType.TASK_HOT)));
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(typeList);
        spTaskType.setAdapter(adapter);

        subTypeList = new ArrayList<>();
        subTypeList.add(new DUWord(TextUtil.EMPTY, String.valueOf(0)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_split), String.valueOf(ConstantUtil.WorkSubType.SPLIT_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_replace), String.valueOf(ConstantUtil.WorkSubType.REPLACE_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_reinstall), String.valueOf(ConstantUtil.WorkSubType.INSTALL_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_stop), String.valueOf(ConstantUtil.WorkSubType.STOP_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_move), String.valueOf(ConstantUtil.WorkSubType.MOVE_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_check), String.valueOf(ConstantUtil.WorkSubType.CHECK_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_reuse), String.valueOf(ConstantUtil.WorkSubType.REUSE)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_notice_simple), String.valueOf(ConstantUtil.WorkSubType.NOTICE)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_verify_simple), String.valueOf(ConstantUtil.WorkSubType.VERIFY)));

        invalidSubTypeList = new ArrayList<>();
        invalidSubTypeList.add(new DUWord(TextUtil.EMPTY, String.valueOf(0)));

        subTypeAdapter = new SpinnerAdapter();
        subTypeAdapter.setList(invalidSubTypeList);
        spSubType.setAdapter(subTypeAdapter);
    }

    private void setOnListener(){
        ibScan.setOnClickListener(this);
        spTaskType.setOnItemSelectedListener(this);
        tvResolveTime.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

}
