package com.sh3h.workmanagerjm.ui.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.OnItemClickListener;
import com.sh3h.workmanagerjm.adapter.ProcessListAdapter;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.chargeBack.ChargeBackActivity;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProcessActivity extends ParentActivity implements ProcessMvpView, OnItemClickListener {
    @Inject
    ProcessPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_process)
    RecyclerView rvProcess;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_process);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        presenter.attachView(this);
        initToolbar();
        initRecycleView();
        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.setHandlePosition(0);
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int type, int i) {
        Intent intent;
        switch (type) {
            case ConstantUtil.ClickType.TYPE_HANDLE:
                intent = new Intent(ProcessActivity.this, ManagerActivity.class);
                break;
            case ConstantUtil.ClickType.TYPE_BACK:
                intent = new Intent(ProcessActivity.this, ChargeBackActivity.class);
                break;
            default:
                intent = new Intent(ProcessActivity.this, ManagerActivity.class);
                break;
        }
        data.setHandlePosition(i);
        startActivity(intent);
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.text_process);
        setSupportActionBar(toolbar);
    }

    private void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProcess.setLayoutManager(manager);
        rvProcess.setHasFixedSize(true);
        ProcessListAdapter adapter = new ProcessListAdapter();
        adapter.setList(data.getHandles());
        adapter.setOnItemClickListener(this);
        rvProcess.setAdapter(adapter);
    }

}

