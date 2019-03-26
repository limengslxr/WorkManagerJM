package com.sh3h.workmanagerjm.ui.dispatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.list.ListActivity;
import com.sh3h.workmanagerjm.util.NumberUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DispatchActivity extends ParentActivity implements DispatchMvpView,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Inject
    DispatchPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sp_task_type)
    Spinner spTaskType;
    @BindView(R.id.sp_task_sub_type)
    Spinner spSubType;
    @BindView(R.id.sp_station)
    Spinner spStation;
    @BindView(R.id.et_volume_id)
    EditText etVolume;
    @BindView(R.id.tv_begin_time)
    TextView tvBeginTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_search)
    TextView tvSearch;

    private Unbinder unbinder;
    private long beginTime;
    private long endTime;
    private List<DUWord> typeList, subTypeList, invalidSubTypeList, stationList;
    private SpinnerAdapter subTypeAdapter;
    //控制初始化
    private boolean initOver = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dispatch);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        unbinder = ButterKnife.bind(this);

        data.setTaskEntrance(ConstantUtil.TaskEntrance.DISPATCH);
        saveUserInfo(getIntent());

        initToolBar();

        initView();

        setOnListener();

        controlLoadData();
        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                search();
                break;
            case R.id.tv_begin_time:
                selectTime(ConstantUtil.SelectTimeType.BEGIN);
                break;
            case R.id.tv_end_time:
                selectTime(ConstantUtil.SelectTimeType.END);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
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
    protected void initConfigEnd() {
        super.initConfigEnd();
        if (!initOver) {
            initOver = true;
            presenter.getStation();
        }
    }

    @Override
    protected void selectTimeResult(int type, long time) {
        super.selectTimeResult(type, time);
        switch (type) {
            case ConstantUtil.SelectTimeType.BEGIN:
                beginTime = time;
                tvBeginTime.setText(TextUtil.format(beginTime, TextUtil.FORMAT_DATE));
                break;
            case ConstantUtil.SelectTimeType.END:
                endTime = time;
                tvEndTime.setText(TextUtil.format(endTime, TextUtil.FORMAT_DATE));
                break;
            default:
                break;
        }
    }

    @Override
    public void getStation(List<DUWord> list) {
        if (list == null){
            list = new ArrayList<>();
        }

        stationList = list;
        SpinnerAdapter adapter = new SpinnerAdapter();
        adapter.setList(list);
        spStation.setAdapter(adapter);
    }

    private void search() {
        int typePosition = spTaskType.getSelectedItemPosition();
        if (typePosition == Spinner.INVALID_POSITION) {
            return;
        }

        int subTypePosition = spSubType.getSelectedItemPosition();
        if (subTypePosition == Spinner.INVALID_POSITION) {
            return;
        }

        int stationPosition = spStation.getSelectedItemPosition();
        if (stationList == null || stationList.size() == 0 || stationPosition == Spinner.INVALID_POSITION) {
            return;
        }

        long hour = 1000 * 3600 * 12;
        if (beginTime > endTime + hour) {
            ApplicationsUtil.showMessage(this, R.string.toast_begin_time_greater_than_end_time);
            return;
        }

        if (beginTime > endTime) {
            ApplicationsUtil.showMessage(this, R.string.toast_begin_time_same_day_end_time);
            return;
        }

        List<DUWord> actualList = typePosition == 0 ? subTypeList : invalidSubTypeList;
        downloadDispatch(NumberUtil.transformNumber(typeList.get(typePosition).getValue()),
                NumberUtil.transformNumber(actualList.get(subTypePosition).getValue()),
                stationList.get(stationPosition).getValue(),
                etVolume.getText().toString(),
                beginTime, endTime);

        Intent intent = new Intent(DispatchActivity.this, ListActivity.class);
        startActivity(intent);
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.title_task_dispatch);
        setSupportActionBar(toolbar);
    }

    private void initView() {
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
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_notice), String.valueOf(ConstantUtil.WorkSubType.NOTICE)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_verify), String.valueOf(ConstantUtil.WorkSubType.VERIFY)));

        invalidSubTypeList = new ArrayList<>();
        invalidSubTypeList.add(new DUWord(TextUtil.EMPTY, String.valueOf(ConstantUtil.UPLOAD_NULL)));

        subTypeAdapter = new SpinnerAdapter();
        subTypeAdapter.setList(invalidSubTypeList);
        spSubType.setAdapter(subTypeAdapter);

        Calendar calendar = Calendar.getInstance();
        endTime = calendar.getTimeInMillis();
        tvEndTime.setText(TextUtil.format(endTime, TextUtil.FORMAT_DATE));

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        beginTime = calendar.getTimeInMillis();
        tvBeginTime.setText(TextUtil.format(beginTime, TextUtil.FORMAT_DATE));
    }

    private void setOnListener() {
        spTaskType.setOnItemSelectedListener(this);
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

    private void controlLoadData() {
        if (initStep < ConstantUtil.InitStep.CLEAN_DATA) {
            initOver = false;
        } else {
            initOver = true;
            presenter.getStation();
        }
    }

}
