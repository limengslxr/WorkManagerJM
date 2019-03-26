package com.sh3h.workmanagerjm.ui.statistics;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.StatisticsListAdapter;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatisticsListActivity extends ParentActivity implements View.OnClickListener,
        SearchView.OnQueryTextListener{
    @Inject Bus bus;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.floating_action_button) FloatingActionButton floatingActionButton;

    private MenuItem searchItem;
    private Unbinder unbinder;
    private StatisticsListAdapter adapter;
    /*DUData：的取值详解
    * 任务表中：（表务工单、装表工单、报装工单）DUTask非空，DUHandle：如果点击过接单、到场、协助（非空），没有（空）
    * 历史表中：DUTask非空，DUHandle：非空
    * */
    private List<StatisticsResult> list;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_statistics_list);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        bus.register(this);
        initToolBar();
        floatingActionButton.setOnClickListener(this);
        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        unbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_list_other, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button:
                recyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtil.isNullOrEmpty(query)) {
            return false;
        }

        queryList(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Subscribe
    public void searchEnd(UIBusEvent.StatisticsTask searchTask) {
        ApplicationsUtil.showMessage(this, searchTask.getMessage());

        if (searchTask.isSuccess()) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
            adapter = new StatisticsListAdapter();
            list = searchTask.getList();
            searchItem.setVisible(list != null && list.size() > 0);
            adapter.setList(list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.title_task_statistics);
        setSupportActionBar(toolbar);
    }

    private void queryList(String query){
        if (TextUtil.isNullOrEmpty(query) || list == null || list.size() == 0){
            return;
        }

        List<StatisticsResult> queryList = new ArrayList<>();
        String name, account;
        for (StatisticsResult result : list){
            name = result.getName();
            if (!TextUtil.isNullOrEmpty(name) && name.contains(query)){
                queryList.add(result);
                continue;
            }

            account = result.getAccount();
            if (!TextUtil.isNullOrEmpty(account) && account.contains(query)){
                queryList.add(result);
            }
        }

        adapter.setList(queryList);
        adapter.notifyDataSetChanged();
    }

}
