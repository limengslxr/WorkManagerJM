package com.sh3h.workmanagerjm.ui.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatisticsActivity extends ParentActivity implements View.OnClickListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_begin_time) TextView tvBeginTime;
    @BindView(R.id.tv_end_time) TextView tvEndTime;
    @BindView(R.id.tv_search) TextView tvSearch;

    private Unbinder unbinder;
    private long beginTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_statistics);
        unbinder = ButterKnife.bind(this);

        data.setTaskEntrance(ConstantUtil.TaskEntrance.STATISTICS);
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
    protected void selectTimeResult(int type, long time) {
        super.selectTimeResult(type, time);
        switch (type){
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

    private void search() {
        downloadStatistics(beginTime, endTime);
        Intent intent = new Intent(StatisticsActivity.this, StatisticsListActivity.class);
        startActivity(intent);
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.title_task_statistics);
        setSupportActionBar(toolbar);
    }

    private void initView(){
        Calendar calendar = Calendar.getInstance();
        endTime = calendar.getTimeInMillis();
        tvEndTime.setText(TextUtil.format(endTime, TextUtil.FORMAT_DATE));

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        beginTime = calendar.getTimeInMillis();
        tvBeginTime.setText(TextUtil.format(beginTime, TextUtil.FORMAT_DATE));
    }

    private void setOnListener(){
        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

}
