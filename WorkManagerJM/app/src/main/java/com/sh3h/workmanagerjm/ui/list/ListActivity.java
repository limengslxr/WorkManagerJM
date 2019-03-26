package com.sh3h.workmanagerjm.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteTask;
import com.sh3h.dataprovider.data.entity.retrofit.DeleteVerifyTask;
import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.data.entity.ui.DUApplyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUAssistHandle;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUSearchPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.AbstractAdapter;
import com.sh3h.workmanagerjm.adapter.CallPayListAdapter;
import com.sh3h.workmanagerjm.adapter.DispatchListAdapter;
import com.sh3h.workmanagerjm.adapter.HistoryListAdapter;
import com.sh3h.workmanagerjm.adapter.HotListAdapter;
import com.sh3h.workmanagerjm.adapter.InsideListAdapter;
import com.sh3h.workmanagerjm.adapter.MeterInstallListAdapter;
import com.sh3h.workmanagerjm.adapter.OnItemClickListener;
import com.sh3h.workmanagerjm.adapter.OrderTaskListAdapter;
import com.sh3h.workmanagerjm.adapter.SearchListAdapter;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.adapter.VerifyListAdapter;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.service.SyncConst;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.chargeBack.ChargeBackActivity;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.ui.process.ProcessActivity;
import com.sh3h.workmanagerjm.util.DispatchUtil;
import com.sh3h.workmanagerjm.util.MapUtil;
import com.sh3h.workmanagerjm.util.NumberUtil;
import com.sh3h.workmanagerjm.view.MultiSpinnerSearch;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListActivity extends ParentActivity implements ListMvpView,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener, OnItemClickListener, MenuItemCompat.OnActionExpandListener {

    @Inject
    ListPresenter presenter;
    @Inject
    Bus bus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    //申请延期的截止时间
    private TextView tvTime;
    private EditText etInput;//dialog
    private Spinner spDifficult, spDriver, spStation, spResult, spSubType;

    private MenuItem searchItem, filterItem, statusItem, dispatchItem, transformItem, cancelItem;
    private Unbinder unbinder;
    private AbstractAdapter adapter;
    /*DUData：的取值详解
     * 任务表中：（表务工单、装表工单、报装工单）DUTask非空，DUHandle：如果点击过接单、到场、协助（非空），没有（空）
     * 历史表中：DUTask非空，DUHandle：非空
     * */
    private List<DUData> list;
    private LinearLayoutManager manager;
    //是否正在下拉加载更多
    private boolean isLoading;
    //进度条是否正在上拉刷新
    private boolean isRefresh;
    //分页查找的起始位置
    private int offset = 0;
    //搜索的关键字
    private String key;
    //点击项下标
    private int clickIndex;
    //控制初始化
    private boolean initOver;
    private boolean isScrollingFrozen = false;
    private long chooseTime;
    private int filterPosition = 0;
    private List<DUSearchPerson> acceptPersons, assistPersons;
    private List<DUWord> difficultList, driverList, stationList;
    private List<DUPerson> allPersons;
    private boolean isHaveCancelPower = false;
    private MaterialDialog materialDialog;
    private int filterSubTypePosition = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_list);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        bus.register(this);
        presenter.attachView(this);

        initToolBar();

        initRecyclerView();

        initSwipeRefreshLayout();

        floatingActionButton.setOnClickListener(this);

        taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_TYPE);
        historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK);
        historyCondition.setHandleType(0);

        downloadVerify();
        controlLoadData();

        setSwipeBackEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
        presenter.detachView();
        bus.unregister(this);
        unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtil.RequestCode.LOCATE_MAP) {
            reportArrive(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String entrance = data.getTaskEntrance();
        entrance = TextUtil.isNullOrEmpty(entrance) ? ConstantUtil.TaskEntrance.INSIDE : entrance;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.HISTORY:
                getMenuInflater().inflate(R.menu.menu_list_history, menu);
                searchItem = menu.findItem(R.id.action_search);
                statusItem = menu.findItem(R.id.action_status_history);
                filterItem = menu.findItem(R.id.action_filter_history);
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                getMenuInflater().inflate(R.menu.menu_list_call_pay, menu);
                searchItem = menu.findItem(R.id.action_search);
                filterItem = menu.findItem(R.id.action_filter_call_pay);
                break;
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                getMenuInflater().inflate(R.menu.menu_list_order_task, menu);
                searchItem = menu.findItem(R.id.action_search);
                filterItem = menu.findItem(R.id.action_filter_order_task);
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                getMenuInflater().inflate(R.menu.menu_list_dispatch, menu);
                searchItem = menu.findItem(R.id.action_search);
                dispatchItem = menu.findItem(R.id.action_dispatch_dispatch);
                transformItem = menu.findItem(R.id.action_transform_dispatch);
                cancelItem = menu.findItem(R.id.action_cancel_dispatch);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                getMenuInflater().inflate(R.menu.menu_list_verify, menu);
                searchItem = menu.findItem(R.id.action_search);
                filterItem = menu.findItem(R.id.action_filter_verify);
                dispatchItem = menu.findItem(R.id.action_dispatch_verify);
                cancelItem = menu.findItem(R.id.action_cancel_verify);
                break;
            default:
                getMenuInflater().inflate(R.menu.menu_list_other, menu);
                searchItem = menu.findItem(R.id.action_search);
                break;
        }

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(this);
        //给searchView设置展开收缩事件，展开时过滤按钮去消失，收缩时过滤按钮显示
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        String entrance = data.getTaskEntrance();
        entrance = TextUtil.isNullOrEmpty(entrance) ? ConstantUtil.TaskEntrance.INSIDE : entrance;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.HISTORY:
                statusItem.setVisible(false);
                filterItem.setVisible(false);
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                filterItem.setVisible(false);
                break;
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                filterItem.setVisible(false);
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                dispatchItem.setVisible(false);
                transformItem.setVisible(false);
                cancelItem.setVisible(false);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                filterItem.setVisible(false);
                dispatchItem.setVisible(false);
                cancelItem.setVisible(false);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        String entrance = data.getTaskEntrance();
        entrance = TextUtil.isNullOrEmpty(entrance) ? ConstantUtil.TaskEntrance.INSIDE : entrance;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.HISTORY:
                statusItem.setVisible(true);
                filterItem.setVisible(true);
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                filterItem.setVisible(true);
                break;
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                filterItem.setVisible(true);
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                dispatchItem.setVisible(true);
                transformItem.setVisible(true);
                cancelItem.setVisible(false);
//                cancelItem.setVisible(isHaveCancelPower);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                filterItem.setVisible(true);
                verifyItem(adapter.getData());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getTaskCount()) {
                    return false;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                if (getTaskCount()) {
                    return false;
                }
                break;
            case R.id.action_search:
                break;
            case R.id.action_status_history:
                filterStatus();
                break;
            case R.id.action_filter_history:
                filterType();
                break;
            case R.id.action_filter_call_pay:
                filterCallPay();
                break;
            case R.id.action_filter_order_task:
                filterOrderTask();
                break;
            case R.id.action_dispatch_dispatch:
                dispatch();
                break;
            case R.id.action_transform_dispatch:
                transformDispatch();
                break;
            case R.id.action_cancel_dispatch:
                cancelDispatch();
                break;
            case R.id.action_filter_verify:
                filterVerify();
                break;
            case R.id.action_dispatch_verify:
                verify();
                break;
            case R.id.action_cancel_verify:
                cancelVerify();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button:
                recyclerView.smoothScrollToPosition(0);
                break;
            case R.id.tv_delay_time:
                selectTime(ConstantUtil.SelectTimeType.END);
                break;
            case R.id.tv_complete_date:
                selectTime(ConstantUtil.SelectTimeType.COMPLETE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (!initOver) {
            ApplicationsUtil.showMessage(this, R.string.toast_init_not_complete);
            isRefresh = false;
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        if (!isRefresh) {
            isRefresh = true;
            switch (data.getTaskEntrance()) {
                case ConstantUtil.TaskEntrance.ORDERTASKBW:
                    downloadTask(ConstantUtil.WorkType.TASK_BIAOWU);
                    break;
                case ConstantUtil.TaskEntrance.METER_INSTALL:
                    downloadTask(ConstantUtil.WorkType.TASK_INSTALL_METER);
                    break;
                case ConstantUtil.TaskEntrance.CALL_PAY:
                    downloadTask(ConstantUtil.WorkType.TASK_CALL_PAY);
                    break;
                case ConstantUtil.TaskEntrance.HOT:
                    downloadTask(ConstantUtil.WorkType.TASK_HOT);
                    break;
                case ConstantUtil.TaskEntrance.INSIDE:
                    downloadTask(ConstantUtil.WorkType.TASK_INSIDE);
                    break;
                case ConstantUtil.TaskEntrance.HISTORY:
                    uploadData();
                    break;
                case ConstantUtil.TaskEntrance.SEARCH:
                case ConstantUtil.TaskEntrance.DISPATCH:
                    isRefresh = false;
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case ConstantUtil.TaskEntrance.VERIFY:
                    downloadVerify();
                    break;
                default:
                    isRefresh = false;
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtil.isNullOrEmpty(query)) {
            return false;
        }

        key = query;
        taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_KEY);
        historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_KEY);

        offset = 0;
        getList(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /*只有下载的任务才有接单、到场、处理、协助、退单的功能*/
    @Override
    public void onItemClick(int type, int position) {
        clickIndex = position;
        String entrance = data.getTaskEntrance();
        data = adapter.getData().get(position);
        data.setTaskEntrance(entrance);
        data.setOperateType(type);

        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.CALL_PAY:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
                onTaskItemClick();
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                onHistoryItemClick();
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
                presenter.searchHandle(data);
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                onDispatchItemClick();
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                onVerifyItemClick();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initConfigEnd() {
        super.initConfigEnd();
        if (!initOver) {
            initOver = true;
            if (data.isNeedDownloadTask()) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            } else {
                offset = 0;
                getList(true);
            }
            getAllPerson();
        }
    }

    @Override
    protected void selectTimeResult(int type, long time) {
        super.selectTimeResult(type, time);
        switch (type) {
            case ConstantUtil.SelectTimeType.END:
            case ConstantUtil.SelectTimeType.COMPLETE:
                chooseTime = time;
                if (tvTime != null) {
                    tvTime.setText(TextUtil.format(chooseTime, TextUtil.FORMAT_DATE));
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param controlSync 控制多线程异步操作时，数据加载重复
     */
    @Override
    public void getData(List<DUData> list, boolean controlSync) {
        if (controlSync) {
            offset = 0;
            this.list.clear();
        }

        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }

        isScrollingFrozen = offset >= this.list.size();
        offset = this.list.size();

        hideMenuItem();
    }

    @Override
    public void getDataComplete() {
        isLoading = false;
        adapter.setShowProgress(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(Throwable e) {
        adapter.setShowProgress(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleSuccess(DUData duData) {
        if (duData == null || duData.getDuTask() == null || duData.getHandle() == null) {
            return;
        }

        DUHandle handle = duData.getHandle();
        DUTask task = duData.getDuTask();
        switch (handle.getReportType()) {
            case ConstantUtil.ReportType.Handle:
                syncOneTask(task.getTaskId());
                break;
            case ConstantUtil.ReportType.Assist:
                ApplicationsUtil.showMessage(this, R.string.toast_assist_success);
                break;
            case ConstantUtil.ReportType.Apply:
                DUApplyHandle applyHandle = handle.toDUApplyHandle();
                switch (applyHandle.getApplyType()) {
                    case ConstantUtil.ApplyType.HANG_UP:
                        ApplicationsUtil.showMessage(this, R.string.toast_hang_up_success);
                        break;
                    case ConstantUtil.ApplyType.RECOVERY:
                        ApplicationsUtil.showMessage(this, R.string.toast_recovery_success);
                        break;
                    case ConstantUtil.ApplyType.DELAY:
                        ApplicationsUtil.showMessage(this, R.string.toast_delay_success);
                        break;
                    default:
                        ApplicationsUtil.showMessage(this, R.string.toast_unknown_error);
                        break;
                }
                break;
            default:
                break;
        }

        switch (task.getState()) {
            case ConstantUtil.State.RECEIVEORDER:
                list.set(clickIndex, duData);
                break;
            case ConstantUtil.State.ONSPOT:
                list.set(clickIndex, duData);
                break;
            case ConstantUtil.State.BACK:
                removeList(task.getTaskId());
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleError(int resourceId) {
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void handleError(String error) {
        ApplicationsUtil.showMessage(this, error);
    }

    @Override
    public void searchHandleEnd(DUData duData) {
        Intent intent;
        switch (duData.getOperateType()) {
            case ConstantUtil.ClickType.TYPE_ITEM:
                DUHandle handle = duData.getHandle();
                int state = ConstantUtil.State.INVALID;
                if (handle != null) {
                    state = handle.getState();
                }
                switch (state) {
                    case ConstantUtil.State.HANDLE:
                        intent = new Intent(this, ManagerActivity.class);
                        duData.setTaskEntrance(ConstantUtil.TaskEntrance.SEARCH);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(this, ManagerActivity.class);
                        data.setOperateType(ConstantUtil.ClickType.TYPE_ITEM);
                        startActivity(intent);
                        break;
                }
                break;
            case ConstantUtil.ClickType.TYPE_PROCESS:
                intent = new Intent(this, ProcessActivity.class);
                duData.setTaskEntrance(ConstantUtil.TaskEntrance.SEARCH);
                intent.putExtra(ConstantUtil.Parcel.DUDATA, duData);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void searchHandleError(Throwable e) {
        ApplicationsUtil.showMessage(this, R.string.toast_search_task_fail);
    }

    @Override
    public void getAllPerson(String account, List<DUPerson> listPerson) {
        allPersons = listPerson;

        if (listPerson == null || listPerson.size() == 0 || TextUtil.isNullOrEmpty(account)) {
            return;
        }

        String platformRoles;
        for (DUPerson person : listPerson) {
            if (!account.equalsIgnoreCase(person.getAccount())) {
                continue;
            }

            platformRoles = person.getPlatformRoles();
            isHaveCancelPower = !TextUtil.isNullOrEmpty(platformRoles)
                    && platformRoles.contains("工单撤销");
            break;
        }
    }

    @Override
    public void dispatchOver(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void dispatchNext(String taskIdStr) {
        if (TextUtil.isNullOrEmpty(taskIdStr)) {
            return;
        }

        String[] taskIds = taskIdStr.split(ConstantUtil.CONNECT_SIGN);
        long taskId;
        for (String string : taskIds) {
            if (!NumberUtil.isNumber(string)) {
                continue;
            }

            taskId = Long.parseLong(string);
            removeList(taskId);
        }

        List<DUData> currentList = adapter.getList();
        LongSparseArray<DUData> sparseArray = new LongSparseArray<>();
        DUTask task;
        if (currentList != null && currentList.size() > 0) {
            for (DUData entity : currentList) {
                task = entity.getDuTask();
                if (task == null) {
                    continue;
                }
                sparseArray.append(task.getTaskId(), entity);
            }
        }

        DUData data;
        for (String string : taskIds) {
            if (!NumberUtil.isNumber(string)) {
                continue;
            }

            taskId = Long.parseLong(string);
            data = sparseArray.get(taskId);
            if (data == null) {
                continue;
            }

            currentList.remove(data);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void getAllStation(List<DUWord> list) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_transform)
                .customView(R.layout.dialog_transform, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spStation == null || spStation.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_station);
                        return;
                    }

                    List<DUData> dataList = adapter.getList();
                    String station = stationList.get(spStation.getSelectedItemPosition()).getName();
                    DUTask task;
                    int type = 0;
                    StringBuilder taskIdStr = new StringBuilder(TextUtil.EMPTY);
                    for (DUData data : dataList) {
                        if (!data.isCheck()) {
                            continue;
                        }

                        task = data.getDuTask();
                        if (task == null) {
                            continue;
                        }

                        if (task.getStation() == null) {
                            continue;
                        }

                        if (task.getStation().equalsIgnoreCase(station)) {
                            ApplicationsUtil.showMessage(this,
                                    R.string.toast_transform_station_select_current_station);
                            return;
                        }

                        taskIdStr.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
                        type = task.getType();
                    }

                    if (etInput == null) {
                        ApplicationsUtil.showMessage(this, R.string.toast_reason_null);
                        return;
                    }

                    String reason = etInput.getText().toString();
                    if (TextUtil.isNullOrEmpty(reason)) {
                        ApplicationsUtil.showMessage(this, R.string.toast_reason_null);
                        return;
                    }

                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    handle.setIds(taskIdStr.substring(1));
                    handle.setType(type);
                    handle.setStation(station);
                    handle.setReason(reason);
                    handle.setOperateType(ConstantUtil.DispatchOperate.TRANSFORM);
                    presenter.transformCancel(handle);
                })
                .build();
        View view = materialDialog.getView();
        spStation = (Spinner) view.findViewById(R.id.sp_station);
        SpinnerAdapter stationAdapter = new SpinnerAdapter();
        this.stationList = list;
        stationAdapter.setList(list);
        spStation.setAdapter(stationAdapter);

        etInput = (EditText) view.findViewById(R.id.et_reason);

        materialDialog.show();
    }

    @Override
    public void verifyError(int resourceId) {
        hideProgress();
        adapter.notifyDataSetChanged();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void handleSuccess(int resourceId) {
        hideProgress();
        adapter.notifyDataSetChanged();
        ApplicationsUtil.showMessage(this, resourceId);

//        swipeRefreshLayout.setRefreshing(true);
//        onRefresh();
    }

    @Override
    public void verifySingleOver(int resourceId) {
        hideProgress();
        adapter.notifyDataSetChanged();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void verifySingleNext(String taskIdStr, int type) {
        if (TextUtil.isNullOrEmpty(taskIdStr)) {
            return;
        }

        String[] taskIds = taskIdStr.split(ConstantUtil.CONNECT_SIGN);
        taskIdStr = taskIds[0];
        if (NumberUtil.isNumber(taskIdStr)) {
            removeVerifyList(NumberUtil.transformNumber(taskIdStr), type);
        }
    }

    @Override
    public void verifyMultipleNext(String taskIdStr, int type) {
        if (TextUtil.isNullOrEmpty(taskIdStr)) {
            return;
        }

        String[] taskIds = taskIdStr.split(ConstantUtil.CONNECT_SIGN);
        for (String taskId : taskIds) {
            if (!NumberUtil.isNumber(taskId)) {
                continue;
            }

            removeVerifyList(NumberUtil.transformNumber(taskId), type);
        }
    }

    @Override
    public void getTaskCount(Long number) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
            jsonObject.put(MyModule.ACTIVITY_NAME, ListActivity.class.getName() + "#" + data.getTaskEntrance());
            JSONArray array = new JSONArray();
            array.put(MyModule.COUNT + "#" + number);
            jsonObject.put(MyModule.DATA, array);
            MyModule myModule = new MyModule(jsonObject.toString());
            MainApplication.get(getApplicationContext()).setMyModule(myModule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTaskOver() {
        finish();
    }

    @Subscribe
    public void refreshEnd(UIBusEvent.DownloadTask downloadTask) {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
        ApplicationsUtil.showMessage(this, downloadTask.getMessage());

        if (downloadTask.isSuccess()) {
            data.removeDownloadTask();
            offset = 0;
            getList(true);
        } else if (list == null || list.size() == 0) {
            offset = 0;
            getList(true);
        }
    }

    @Subscribe
    public void syncTaskEnd(UIBusEvent.SyncTask syncTask) {
        hideProgress();

        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);

        if (syncTask.isSuccess()) {
            ApplicationsUtil.showMessage(this, syncTask.getMessage());
            offset = 0;
            getList(true);
        } else {
            showErrorMessage(R.string.toast_sync_task_fail);
        }
    }

    @Subscribe
    public void uploadOneTaskResult(UIBusEvent.UploadOneTaskResult result) {
        ApplicationsUtil.showMessage(this, result.getTaskId() > 0 && result.isSuccess() ?
                R.string.work_upload_success : R.string.work_upload_failure);
    }

    @Subscribe
    public void uploadOneTaskMediaResult(UIBusEvent.UploadOneTaskMediaResult result) {
        ApplicationsUtil.showMessage(this, result.getTaskId() > 0 && result.isSuccess() ?
                R.string.work_upload_success : R.string.work_upload_failure);
        if (result.isSuccess()) {
            offset = 0;
            getList(true);
        }
    }

    @Subscribe
    public void networkError(UIBusEvent.NetworkNotConnect connect) {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);

        switch (connect.getOperate()) {
            case SyncConst.UPLOAD_ALL_TASK:
                hideProgress();
                showErrorMessage(R.string.toast_sync_task_fail);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void temporaryResult(UIBusEvent.TemporaryResult result) {
        offset = 0;
        getList(true);
    }

    @Subscribe
    public void searchEnd(UIBusEvent.SearchTask searchTask) {
        ApplicationsUtil.showMessage(this, searchTask.getMessage());

        if (searchTask.isSuccess()) {
            getData(searchTask.getList(), true);
            getDataComplete();
        }
    }

    @Subscribe
    public void getDispath(UIBusEvent.DispatchTask dispatchTask) {
        ApplicationsUtil.showMessage(this, dispatchTask.getMessage());

        if (dispatchTask.isSuccess()) {
            List<DUData> dispatchList = dispatchTask.getList();
            getData(dispatchList, true);
            getDataComplete();
        }
    }

    @Subscribe
    public void getVerify(UIBusEvent.VerifyTask verifyTask) {
        ApplicationsUtil.showMessage(this, verifyTask.getMessage());

        if (verifyTask.isSuccess()) {
            List<DUData> verifyList = verifyTask.getList();
            getVerifyData(verifyList);
        }

        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void applyAssistEnd(UIBusEvent.ApplyAssist applyAssist) {
        DUData data = list.get(clickIndex);
        data.getHandles().add(0, applyAssist.getHandle());
        adapter.notifyDataSetChanged();
        ApplicationsUtil.showMessage(this, R.string.toast_assist_success);
    }

    @Subscribe
    public void handleTaskEnd(UIBusEvent.HandleTask handleTask) {
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                //case ConstantUtil.TaskEntrance.CALL_PAY:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
                removeList(handleTask.getTaskId());
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void deleteTask(UIBusEvent.DeleteTasks deleteTasks) {
        if (deleteTasks == null) {
            return;
        }

        String deleteEntrance = deleteTasks.getEntrance();
        String entrance = data.getTaskEntrance();
        if (TextUtil.isNullOrEmpty(deleteEntrance)) {
            if (ConstantUtil.TaskEntrance.DISPATCH.equals(entrance)
                    || ConstantUtil.TaskEntrance.VERIFY.equals(entrance)
                    || ConstantUtil.TaskEntrance.STATISTICS.equals(entrance)
                    || ConstantUtil.TaskEntrance.SEARCH.equals(entrance)) {
                return;
            }
        } else {
            if (!deleteEntrance.equals(entrance)) {
                return;
            }
        }

        List<DeleteTask> deleteList = deleteTasks.getDeleteTasks();
        if (deleteList == null || deleteList.size() == 0) {
            return;
        }

        LongSparseArray<DeleteTask> map = new LongSparseArray<>();
        for (DeleteTask deleteTask : deleteList) {
            map.put(deleteTask.getTaskId(), deleteTask);
        }

        DUTask task;
        DeleteTask deleteTask;
        int size = (list == null || list.size() == 0) ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            task = list.get(i).getDuTask();
            deleteTask = map.get(task.getTaskId());
            if (deleteTask == null) {
                continue;
            }

            list.remove(i);
            break;
        }

        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void deleteVerifyTask(UIBusEvent.DeleteVerifyTasks deleteTasks) {
        if (deleteTasks == null) {
            return;
        }

        String entrance = data.getTaskEntrance();
        if (!ConstantUtil.TaskEntrance.VERIFY.equals(entrance)) {
            return;
        }

        List<DeleteVerifyTask> deleteList = deleteTasks.getDeleteTasks();
        if (deleteList == null || deleteList.size() == 0) {
            return;
        }

        LongSparseArray<DeleteVerifyTask> map = new LongSparseArray<>();
        for (DeleteVerifyTask deleteTask : deleteList) {
            map.put(deleteTask.getTaskId(), deleteTask);
        }

        DUTask task;
        DeleteVerifyTask deleteTask;
        int size = (list == null || list.size() == 0) ? 0 : list.size();
        int applyType = judgeApplyType();
        for (int i = 0; i < size; i++) {
            task = list.get(i).getDuTask();
            deleteTask = map.get(task.getTaskId());
            if (deleteTask == null) {
                continue;
            }

            if (applyType != deleteTask.getApplyType()) {
                continue;
            }

            list.remove(i);
            break;
        }

        List<DUData> dataList = adapter.getList();
        size = (dataList == null || dataList.size() == 0) ? 0 : dataList.size();
        for (int i = 0; i < size; i++) {
            task = dataList.get(i).getDuTask();
            deleteTask = map.get(task.getTaskId());
            if (deleteTask == null) {
                continue;
            }

            if (applyType != deleteTask.getApplyType()) {
                continue;
            }

            dataList.remove(i);
            break;
        }

        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void updateState(UIBusEvent.UpdateTaskState updateTaskState) {
        if (updateTaskState == null) {
            return;
        }

        List<MessageUpdate> messageUpdates = updateTaskState.getUpdateStates();
        if (messageUpdates == null || messageUpdates.size() == 0) {
            return;
        }

        for (MessageUpdate messageUpdate : messageUpdates) {
            updateState(messageUpdate);
        }

        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void updateState(UIBusEvent.ApplyVerify applyVerify) {
        if (applyVerify == null) {
            return;
        }

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Subscribe
    public void updateStationList(UIBusEvent.TransformStation transformStation) {
        if (transformStation == null || !ConstantUtil.TaskEntrance.DISPATCH.equals(data.getTaskEntrance())) {
            return;
        }

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Subscribe
    public void addMultipleTask(UIBusEvent.MultipleTask multipleTasks) {
        if (multipleTasks == null) {
            return;
        }

        List<Integer> types = multipleTasks.getTypes();
        if (types == null || types.size() == 0) {
            return;
        }

        data.addDownloadTask(types);
        if (data.isNeedDownloadTask()) {
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Subscribe
    public void singleDispatchResult(UIBusEvent.SingleDispatch singleDispatch) {
        removeList(singleDispatch.getTaskId());
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void singleVerifyResult(UIBusEvent.SingleVerify singleVerify) {
        removeVerifyList(singleVerify.getTaskId(), singleVerify.getApplyType());
        adapter.notifyDataSetChanged();
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        int titleId;
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                titleId = R.string.title_order_bw_list;
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                titleId = R.string.title_meter_install_list;
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                titleId = R.string.title_call_pay_list;
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                titleId = R.string.text_history_list;
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
                titleId = R.string.title_task_search;
                break;
            case ConstantUtil.TaskEntrance.HOT:
                titleId = R.string.title_hot;
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                titleId = R.string.title_inside;
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                titleId = R.string.title_task_dispatch;
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                titleId = R.string.title_task_verify;
                break;
            default:
                titleId = R.string.text_work_list;
                break;
        }
        toolbar.setTitle(titleId);
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                adapter = new OrderTaskListAdapter();
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                adapter = new MeterInstallListAdapter();
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                adapter = new HistoryListAdapter();
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                adapter = new CallPayListAdapter();
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
                adapter = new SearchListAdapter();
                break;
            case ConstantUtil.TaskEntrance.HOT:
                adapter = new HotListAdapter();
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                adapter = new InsideListAdapter();
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                adapter = new VerifyListAdapter();
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                adapter = new DispatchListAdapter();
                break;
            default:
                adapter = new SearchListAdapter();
                break;
        }
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        adapter.setShowProgress(true);
        adapter.setOnItemClickListener(this);

        isLoading = false;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //正在滑动时，防止activity被finish造成空指针
                if (floatingActionButton == null) {
                    return;
                }

                int firstItemPosition = manager.findFirstVisibleItemPosition();
                if (firstItemPosition > ConstantUtil.FIRST_VISIBLE_ITEM_POSITION
                        && newState == 0) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                } else {
                    floatingActionButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemIndex = manager.findLastVisibleItemPosition();
                if (list != null && lastItemIndex >= list.size() - 1
                        && !isLoading && dy > 0 && !isScrollingFrozen) {
                    isLoading = true;
                    String entrance = data.getTaskEntrance();
                    entrance = TextUtil.isNullOrEmpty(entrance)
                            ? ConstantUtil.TaskEntrance.METER_INSTALL : entrance;
                    switch (entrance) {
                        case ConstantUtil.TaskEntrance.SEARCH:
                        case ConstantUtil.TaskEntrance.DISPATCH:
                        case ConstantUtil.TaskEntrance.VERIFY:
                            adapter.setShowProgress(false);
                            break;
                        default:
                            adapter.setShowProgress(true);
                            break;
                    }
                    getList(false);
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.color_blue_00ddff);
        isRefresh = false;
        swipeRefreshLayout.setProgressViewOffset(false, ConstantUtil.SRL_START_PROGRESS_OFFSET, ConstantUtil.SRL_END_PROGRESS_OFFSET);
        swipeRefreshLayout.setRefreshing(false);

        String entrance = data.getTaskEntrance();
        entrance = TextUtil.isNullOrEmpty(entrance)
                ? ConstantUtil.TaskEntrance.METER_INSTALL : entrance;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.SEARCH:
            case ConstantUtil.TaskEntrance.DISPATCH:
            case ConstantUtil.TaskEntrance.STATISTICS:
                swipeRefreshLayout.setEnabled(false);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                isRefresh = true;
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshLayout.setOnRefreshListener(this);
                break;
            default:
                swipeRefreshLayout.setOnRefreshListener(this);
                break;
        }
    }

    private void controlLoadData() {
        if (initStep < ConstantUtil.InitStep.CLEAN_DATA) {
            initOver = false;
        } else {
            initOver = true;
            MainApplication application = MainApplication.get(this);
            List<Integer> downloadTypes = application.getDownloadTypes();
            data.addDownloadTask(downloadTypes);
            application.setDownloadTypes(null);
            if (data.isNeedDownloadTask()) {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            } else {
                offset = 0;
                getList(true);
            }
            getAllPerson();
        }
    }

    private void getAllPerson() {
        String entrance = data.getTaskEntrance();
        if (ConstantUtil.TaskEntrance.VERIFY.equalsIgnoreCase(entrance)
                || ConstantUtil.TaskEntrance.DISPATCH.equalsIgnoreCase(entrance)) {
            presenter.getAllPerson();
        }
    }

    private void getList(boolean controlSync) {
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                getLocalTask(ConstantUtil.WorkType.TASK_BIAOWU, controlSync);
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                getLocalTask(ConstantUtil.WorkType.TASK_INSTALL_METER, controlSync);
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                getLocalTask(ConstantUtil.WorkType.TASK_CALL_PAY, controlSync);
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                getHistory(controlSync);
                break;
            case ConstantUtil.TaskEntrance.HOT:
                getLocalTask(ConstantUtil.WorkType.TASK_HOT, controlSync);
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                getLocalTask(ConstantUtil.WorkType.TASK_INSIDE, controlSync);
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
            case ConstantUtil.TaskEntrance.DISPATCH:
                searchDownload();
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                searchVerify(controlSync);
                break;
            default:
                break;
        }
    }

    private void getLocalTask(int workType, boolean controlSync) {
        taskCondition.setKey(key);
        taskCondition.setOffset(offset);
        taskCondition.setType(workType);
        presenter.getTaskList(taskCondition, controlSync);
    }

    private void getHistory(boolean controlSync) {
        historyCondition.setKey(key);
        historyCondition.setOffset(offset);
        presenter.getHistoryList(historyCondition, controlSync);
    }

    private boolean getTaskCount() {
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.CALL_PAY:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.HOT:
                list.clear();
                offset = 0;
                taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_TYPE);
                presenter.getTaskCount(taskCondition);
                return true;
            default:
                return false;
        }
    }

    private void filterStatus() {
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.title_all));
        items.add(getString(R.string.text_handler));
        items.add(getString(R.string.text_charge_back));
        items.add(getString(R.string.text_delay));
        items.add(getString(R.string.text_hang_up));
        items.add(getString(R.string.text_recovery));
        items.add(getString(R.string.text_apply_help));
        items.add(getString(R.string.text_on_spot));
        items.add(getString(R.string.text_receive_order));

        materialDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.text_prompt)
                .items(items)
                .itemsCallbackSingleChoice(filterSubTypePosition, (dialog, itemView, which, text) -> {
                    list.clear();
                    offset = 0;
                    filterSubTypePosition = which;
                    if (historyCondition.getOperate() != HistoryCondition.GET_PAGE_WORK_BY_TYPE) {
                        historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK);
                    }
                    historyCondition.setHandleType(which);
                    getList(true);
                    return true;
                })
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .build();
        materialDialog.show();
    }

    private void filterType() {
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.title_all));
        items.add(getString(R.string.title_order_bw_list));
        items.add(getString(R.string.title_meter_install_list));
        items.add(getString(R.string.title_call_pay_list));
        items.add(getString(R.string.title_hot));
        items.add(getString(R.string.title_inside));

        materialDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.text_prompt)
                .items(items)
                .itemsCallbackSingleChoice(filterPosition, (dialog, itemView, which, text) -> {
                    list.clear();
                    offset = 0;
                    filterPosition = which;
                    switch (which) {
                        case 0:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK);
                            break;
                        case 1:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_TYPE);
                            historyCondition.setType(ConstantUtil.WorkType.TASK_BIAOWU);
                            break;
                        case 2:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_TYPE);
                            historyCondition.setType(ConstantUtil.WorkType.TASK_INSTALL_METER);
                            break;
                        case 3:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_TYPE);
                            historyCondition.setType(ConstantUtil.WorkType.TASK_CALL_PAY);
                            break;
                        case 4:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_TYPE);
                            historyCondition.setType(ConstantUtil.WorkType.TASK_HOT);
                            break;
                        case 5:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK_BY_TYPE);
                            historyCondition.setType(ConstantUtil.WorkType.TASK_INSIDE);
                            break;
                        default:
                            historyCondition.setOperate(HistoryCondition.GET_PAGE_WORK);
                            break;
                    }

                    getList(true);
                    return true;
                })
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .build();
        materialDialog.show();
    }

    private void filterCallPay() {
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.text_show_all));
        items.add(getString(R.string.text_show_already_call_pay));
        items.add(getString(R.string.text_show_no_call_pay));

        materialDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.text_prompt)
                .items(items)
                .itemsCallbackSingleChoice(filterPosition, (dialog, itemView, which, text) -> {
                    list.clear();
                    offset = 0;
                    filterPosition = which;
                    switch (which) {
                        case 0:
                            taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_TYPE);
                            break;
                        case 1:
                            taskCondition.setOperate(TaskCondition.GET_ALREADY_CALL_PAY);
                            break;
                        case 2:
                            taskCondition.setOperate(TaskCondition.GET_NO_CALL_PAY);
                            break;
                        default:
                            taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_TYPE);
                            break;
                    }

                    getList(true);
                    return true;
                })
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .build();
        materialDialog.show();
    }

    private void filterOrderTask() {
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.text_show_all));
        items.add(getString(R.string.text_sub_type_split));
        items.add(getString(R.string.text_sub_type_replace));
        items.add(getString(R.string.text_sub_type_reinstall));
        items.add(getString(R.string.text_sub_type_stop));
        items.add(getString(R.string.text_sub_type_move));
        items.add(getString(R.string.text_sub_type_check));
        items.add(getString(R.string.text_sub_type_reuse));
        items.add(getString(R.string.text_sub_type_notice));
        items.add(getString(R.string.text_sub_type_verify));

        materialDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.text_prompt)
                .items(items)
                .itemsCallbackSingleChoice(filterPosition, (dialog, itemView, which, text) -> {
                    list.clear();
                    offset = 0;
                    filterPosition = which;
                    switch (which) {
                        case 0:
                            taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_TYPE);
                            break;
                        default:
                            taskCondition.setOperate(TaskCondition.GET_PAGE_WORK_BY_SUB_TYPE);
                            break;
                    }
                    taskCondition.setSubType(filterPosition);

                    getList(true);
                    return true;
                })
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .build();
        materialDialog.show();
    }

    private void rejectBack() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.text_back_reason)
                .customView(R.layout.dialog_input, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    if (view == null) {
                        return;
                    }
                    EditText editText = (EditText) view.findViewById(R.id.et_input);
                    String text = editText.getText().toString();
                    if (TextUtil.isNullOrEmpty(text)) {
                        ApplicationsUtil.showMessage(ListActivity.this,
                                R.string.toast_please_input_reject_reason);
                        return;
                    }

                    DUData duData = list.get(clickIndex);
                    DUTask duTask = duData.getDuTask();
                    duTask.setState(ConstantUtil.State.BACK);
                    DUHandle handle = data.initDUHandle();
                    handle.setReportType(ConstantUtil.ReportType.Handle);
                    if (duData.getHandles() != null && duData.getHandles().size() > 0) {
                        handle.setAssist(duData.getHandles().get(0).getAssist());
                    }
                    DUTaskHandle duTaskHandle = handle.toDUTaskHandle();
                    duTaskHandle.setProcessReason(text);
                    handle.setReply(duTaskHandle);
                    historyCondition.setDuHandle(handle);
                    presenter.handle(historyCondition);

                    hideKeyBoard();
                })
                .build();
        materialDialog.show();
    }

    private void applyAssist() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_apply_assist)
                .customView(R.layout.dialog_apply_assist, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    if (view == null) {
                        return;
                    }

                    EditText etRemark = (EditText) view.findViewById(R.id.et_remark);
                    String remark = etRemark.getText().toString();
                    if (TextUtil.isNullOrEmpty(remark)) {
                        ApplicationsUtil.showMessage(ListActivity.this,
                                R.string.toast_please_input_assist_reason);
                        return;
                    }

                    EditText etPerson = (EditText) view.findViewById(R.id.et_assist_person);
                    String person = etPerson.getText().toString();
                    if (TextUtil.isNullOrEmpty(person) || person.length() > 5) {
                        ApplicationsUtil.showMessage(ListActivity.this,
                                R.string.toast_please_input_assist_person_count);
                        return;
                    }

                    DUData duData = list.get(clickIndex);
                    DUHandle handle = duData.initDUHandle();
                    handle.setReportType(ConstantUtil.ReportType.Assist);
                    DUAssistHandle assistHandle = handle.toDUAssistHandle();
                    assistHandle.setAssistPersonCount(Integer.parseInt(person));
                    assistHandle.setRemark(remark);
                    handle.setReply(assistHandle);
                    handle.setAssist(ConstantUtil.Assist.OK);
                    historyCondition.setDuHandle(handle);
                    presenter.handle(historyCondition);

                    hideKeyBoard();
                })
                .build();
        materialDialog.show();
    }

    private void applyHangUp(DUTask task) {
        int title;
        switch (task.getHangUpState()) {
            case ConstantUtil.HangUpState.HANG_UP:
                title = R.string.title_recovery_reason;
                break;
            case ConstantUtil.HangUpState.NORMAL:
                title = R.string.title_hang_up_reason;
                break;
            default:
                return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(R.layout.dialog_input, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    if (view == null) {
                        return;
                    }

                    EditText etReason = (EditText) view.findViewById(R.id.et_input);
                    String remark = etReason.getText().toString();
                    boolean hangUp = task.getHangUpState() == ConstantUtil.HangUpState.HANG_UP;
                    if (TextUtil.isNullOrEmpty(remark)) {
                        ApplicationsUtil.showMessage(ListActivity.this, hangUp ?
                                R.string.toast_please_input_recovery_reason :
                                R.string.toast_please_input_hang_up_reason);
                        return;
                    }

                    DUData duData = list.get(clickIndex);
                    DUHandle handle = duData.initDUHandle();
                    handle.setReportType(ConstantUtil.ReportType.Apply);
                    DUApplyHandle applyHandle = handle.toDUApplyHandle();
                    applyHandle.setApplyType(hangUp ? ConstantUtil.ApplyType.RECOVERY : ConstantUtil.ApplyType.HANG_UP);
                    applyHandle.setReason(remark);
                    handle.setHangUpState(hangUp ? ConstantUtil.HangUpState.WAIT_RECOVERY
                            : ConstantUtil.HangUpState.WAIT_HANG_UP);
                    handle.setReply(applyHandle);
                    handle.setAssist(duData.getHandle() == null ?
                            ConstantUtil.Assist.NO : duData.getHandle().getAssist());
                    historyCondition.setDuHandle(handle);
                    presenter.handle(historyCondition);

                    hideKeyBoard();
                })
                .build();
        materialDialog.show();
    }

    private void applyDelay() {
        chooseTime = 0L;
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.text_delay)
                .customView(R.layout.dialog_apply_delay, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    View view = dialog.getCustomView();
                    if (view == null || etInput == null) {
                        return;
                    }

                    if (chooseTime == 0L) {
                        ApplicationsUtil.showMessage(ListActivity.this, R.string.toast_please_select_end_time);
                        return;
                    }

                    String reason = etInput.getText().toString();
                    if (TextUtil.isNullOrEmpty(reason)) {
                        ApplicationsUtil.showMessage(this, R.string.toast_reason_null);
                        return;
                    }

                    DUData duData = list.get(clickIndex);
                    DUHandle handle = duData.initDUHandle();
                    handle.setReportType(ConstantUtil.ReportType.Apply);
                    DUApplyHandle applyHandle = handle.toDUApplyHandle();
                    applyHandle.setApplyType(ConstantUtil.ApplyType.DELAY);
                    applyHandle.setReason(reason);
                    applyHandle.setApplyTimeEx(chooseTime);
                    handle.setDelayState(ConstantUtil.DelayState.REPORT_DELAY);
                    handle.setReply(applyHandle);
                    handle.setAssist(duData.getHandle() == null
                            ? ConstantUtil.Assist.NO : duData.getHandle().getAssist());
                    historyCondition.setDuHandle(handle);
                    presenter.handle(historyCondition);

                    hideKeyBoard();
                })
                .build();
        View view = materialDialog.getCustomView();
        assert view != null;
        tvTime = (TextView) view.findViewById(R.id.tv_delay_time);
        etInput = (EditText) view.findViewById(R.id.et_delay_reason);
        materialDialog.show();
    }

    private void onTaskItemClick() {
        Intent intent;
        DUHandle handle;
        DUTaskHandle taskHandle;
        DUTask duTask = data.getDuTask();
        switch (data.getOperateType()) {
            case ConstantUtil.ClickType.TYPE_RECEIVE:
                handle = data.initDUHandle();
                handle.setReportType(ConstantUtil.ReportType.Apply);
                handle.setState(ConstantUtil.State.RECEIVEORDER);
                taskHandle = handle.toDUTaskHandle();
                if (data.getHandles() != null && data.getHandles().size() > 0) {
                    handle.setAssist(data.getHandles().get(0).getAssist());
                }
                handle.setReply(taskHandle);
                historyCondition.setDuHandle(handle);
                presenter.handle(historyCondition);
                break;
            case ConstantUtil.ClickType.TYPE_ARRIVE:
                if (ConstantUtil.TaskEntrance.HOT.equals(data.getTaskEntrance())) {
                    markMap(duTask);
                } else {
                    reportArrive(0.0, 0.0);
                }
                break;
            case ConstantUtil.ClickType.TYPE_HANDLE:
                intent = new Intent(this, ManagerActivity.class);
                data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);
                startActivity(intent);
                break;
            case ConstantUtil.ClickType.TYPE_ASSIST:
                applyAssist();
                break;
            case ConstantUtil.ClickType.TYPE_BACK:
                intent = new Intent(this, ChargeBackActivity.class);
                data.setOperateType(ConstantUtil.ClickType.TYPE_BACK);
                startActivity(intent);
                //rejectBack();
                break;
            case ConstantUtil.ClickType.TYPE_ITEM:
                intent = new Intent(this, ManagerActivity.class);
                data.setOperateType(ConstantUtil.ClickType.TYPE_ITEM);
                startActivity(intent);
                break;
            case ConstantUtil.ClickType.TYPE_HANG_UP:
                applyHangUp(duTask);
                break;
            case ConstantUtil.ClickType.TYPE_DELAY:
                applyDelay();
                break;
            case ConstantUtil.ClickType.TYPE_PROCESS:
                intent = new Intent(this, ProcessActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void onHistoryItemClick() {
        Intent intent;
        switch (data.getOperateType()) {
            case ConstantUtil.ClickType.TYPE_ITEM:
                DUHandle handle = data.getHandle();
                if (handle == null) {
                    return;
                }

                if ((handle.getReportType() == ConstantUtil.ReportType.Handle
                        && ConstantUtil.State.HANDLE == handle.getState())
                        || handle.getReportType() == ConstantUtil.ReportType.Report) {
                    intent = new Intent(this, ManagerActivity.class);
                    data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);
                } else if (handle.getReportType() == ConstantUtil.ReportType.Handle
                        && ConstantUtil.State.BACK == handle.getState()) {
                    intent = new Intent(this, ChargeBackActivity.class);
                    data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);
                } else {
                    intent = new Intent(this, ManagerActivity.class);
                    data.setOperateType(ConstantUtil.ClickType.TYPE_ITEM);
                }
                startActivity(intent);
                break;
            case ConstantUtil.ClickType.TYPE_PROCESS:
                intent = new Intent(this, ProcessActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void onDispatchItemClick() {
        Intent intent = new Intent(this, ManagerActivity.class);
        data.setOperateType(ConstantUtil.ClickType.TYPE_ITEM);
        startActivity(intent);
    }

    private void onVerifyItemClick() {
        Intent intent;
        switch (data.getOperateType()) {
            case ConstantUtil.ClickType.TYPE_ASSIST_VERIFY_DISPATCH:
                verifyAssist();
                break;
            case ConstantUtil.ClickType.TYPE_HANDLE:
                intent = new Intent(this, ManagerActivity.class);
                data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void uploadData() {
        if (list.size() == 0) {
            isRefresh = false;
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        boolean unUpload = false;
        out:
        for (DUData duData : list) {
            List<DUHandle> duDataHandles = duData.getHandles();
            if (duDataHandles == null || duDataHandles.size() <= 0) {
                continue;
            }

            for (DUHandle handle : duDataHandles) {
                if (handle.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                    unUpload = true;
                    break out;
                }

                List<DUMedia> medias = handle.getMedias();
                if (medias == null || medias.size() <= 0) {
                    continue;
                }

                for (DUMedia media : medias) {
                    if (media.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                        unUpload = true;
                        break out;
                    }
                }
            }
        }

        if (unUpload) {
            syncTask();
        } else {
            isRefresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void markMap(DUTask task) {
        Intent intent = MapUtil.getMapIntent(getBaseContext());
        if (intent != null) {
            intent.putExtra(ConstantUtil.Map.MAP_STATUS, ConstantUtil.Map.MARK_MAP);
            intent.putExtra(ConstantUtil.Map.LONGITUDE, task.getLongitude());
            intent.putExtra(ConstantUtil.Map.LATITUDE, task.getLatitude());
            intent.putExtra(ConstantUtil.Map.ADDRESS, task.getAddress());
            startActivityForResult(intent, ConstantUtil.RequestCode.LOCATE_MAP);
        }
    }

    private void reportArrive(Intent intent) {
        if (intent == null) {
            return;
        }

        double longitude = intent.getDoubleExtra(ConstantUtil.Map.LONGITUDE, 0.0);
        double latitude = intent.getDoubleExtra(ConstantUtil.Map.LATITUDE, 0.0);
        //24225.787442405137
        //45031.37388854544
        if (Math.abs(latitude) > ConstantUtil.Map.ERROR_VALUE
                && Math.abs(longitude) > ConstantUtil.Map.ERROR_VALUE) {
            reportArrive(longitude, latitude);
        }
    }

    private void reportArrive(double longitude, double latitude) {
        DUData data = list.get(clickIndex);
        DUHandle handle = data.initDUHandle();
        handle.setReportType(ConstantUtil.ReportType.Handle);
        handle.setState(ConstantUtil.State.ONSPOT);
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        taskHandle.setLongitude(longitude);
        taskHandle.setLatitude(latitude);
        if (data.getHandles() != null && data.getHandles().size() > 0) {
            handle.setAssist(data.getHandles().get(0).getAssist());
        }
        handle.setReply(taskHandle);
        historyCondition.setDuHandle(handle);
        presenter.handle(historyCondition);
    }

    private void searchDownload() {
        if (list == null || list.size() == 0 || TextUtil.isNullOrEmpty(key)) {
            return;
        }

        isScrollingFrozen = offset >= this.list.size();
        offset = this.list.size();

        List<DUData> filterList = new ArrayList<>();
        DUTask task;
        String address, cardName, barCode, cardId;
        long taskId;
        for (DUData data : list) {
            task = data.getDuTask();

            if (task == null) {
                continue;
            }

            address = task.getAddress();
            if (!TextUtil.isNullOrEmpty(address) && address.contains(key)) {
                filterList.add(data);
                continue;
            }

            cardName = task.getCardName();
            if (!TextUtil.isNullOrEmpty(cardName) && cardName.contains(key)) {
                filterList.add(data);
                continue;
            }

            barCode = task.getBarCode();
            if (!TextUtil.isNullOrEmpty(barCode) && barCode.contains(key)) {
                filterList.add(data);
                continue;
            }

            cardId = task.getCardId();
            if (!TextUtil.isNullOrEmpty(cardId) && cardId.contains(key)) {
                filterList.add(data);
                continue;
            }

            taskId = task.getTaskId();
            if (taskId == NumberUtil.transformNumber(key)) {
                filterList.add(data);
            }
        }

        adapter.setList(filterList);
        getDataComplete();
        hideMenuItem();
    }

    private void searchVerify(boolean isUseKey) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (isUseKey && TextUtil.isNullOrEmpty(key)) {
            return;
        }

        DUTask task;
        long taskId;
        String address, cardName, barCode, cardId;
        List<DUData> filterList = new ArrayList<>();
        int applyType = judgeApplyType();
        for (DUData data : list) {
            if (data.getApplyType() != applyType) {
                continue;
            }

            if (!isUseKey) {
                filterList.add(data);
                continue;
            }

            task = data.getDuTask();
            if (task == null) {
                continue;
            }

            address = task.getAddress();
            if (!TextUtil.isNullOrEmpty(address) && address.contains(key)) {
                filterList.add(data);
                continue;
            }

            cardName = task.getCardName();
            if (!TextUtil.isNullOrEmpty(cardName) && cardName.contains(key)) {
                filterList.add(data);
                continue;
            }

            barCode = task.getBarCode();
            if (!TextUtil.isNullOrEmpty(barCode) && barCode.contains(key)) {
                filterList.add(data);
                continue;
            }

            cardId = task.getCardId();
            if (!TextUtil.isNullOrEmpty(cardId) && cardId.contains(key)) {
                filterList.add(data);
                continue;
            }

            taskId = task.getTaskId();
            if (taskId == NumberUtil.transformNumber(key)) {
                filterList.add(data);
            }
        }

        adapter.setShowProgress(false);
        adapter.setList(filterList);
        adapter.notifyDataSetChanged();
    }

    private int judgeApplyType() {
        int applyType;
        switch (filterPosition) {
            case 0:
                applyType = ConstantUtil.ApplyType.DELAY;
                break;
            case 1:
                applyType = ConstantUtil.ApplyType.DEFAULT;
                break;
            case 2:
                applyType = ConstantUtil.ApplyType.ASSIST;
                break;
            case 3:
                applyType = ConstantUtil.ApplyType.HANG_UP;
                break;
            case 4:
                applyType = ConstantUtil.ApplyType.RECOVERY;
                break;
            case 5:
                applyType = ConstantUtil.ApplyType.REPORT;
                break;
            default:
                applyType = ConstantUtil.ApplyType.DEFAULT;
                break;
        }

        return applyType;
    }

    private void hideMenuItem() {
        String entrance = data.getTaskEntrance();
        boolean haveTotalData = list != null && list.size() > 0;
        entrance = TextUtil.isNullOrEmpty(entrance) ? ConstantUtil.TaskEntrance.INSIDE : entrance;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.HISTORY:
                if (historyCondition.getOperate() == HistoryCondition.GET_PAGE_WORK
                        && historyCondition.getHandleType() == 0
                        && !haveTotalData) {
                    filterItem.setVisible(false);
                    statusItem.setVisible(false);
                    searchItem.setVisible(false);
                }
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                filterItem.setVisible(haveTotalData);
                searchItem.setVisible(haveTotalData);
                break;
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                if (taskCondition.getOperate() == TaskCondition.GET_PAGE_WORK_BY_TYPE) {
                    filterItem.setVisible(haveTotalData);
                    searchItem.setVisible(haveTotalData);
                }
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                if (searchItem != null) {
                    searchItem.setVisible(haveTotalData);
                }
                if (dispatchItem != null) {
                    dispatchItem.setVisible(haveTotalData);
                }
                if (transformItem != null) {
                    transformItem.setVisible(haveTotalData);
                }
                if (cancelItem != null) {
                    cancelItem.setVisible(false);
//                    cancelItem.setVisible(isHaveCancelPower && haveTotalData);
                }
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                verifyItem(adapter.getData());
                break;
            default:
                if (searchItem != null) {
                    searchItem.setVisible(haveTotalData);
                }
                break;
        }
    }

    private void verifyItem(List<DUData> dataList) {
        ///
        boolean haveSectionData = dataList != null && dataList.size() > 0;
        switch (filterPosition) {
            case 0:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(haveSectionData);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case 1:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(haveSectionData);
                dispatchItem.setTitle(R.string.menu_dispatch);
                cancelItem.setVisible(isHaveCancelPower && haveSectionData);
                break;
            case 2:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(false);
                cancelItem.setVisible(false);
                break;
            case 3:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(haveSectionData);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case 4:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(haveSectionData);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case 5:
                searchItem.setVisible(haveSectionData);
                dispatchItem.setVisible(haveSectionData);
                dispatchItem.setTitle(R.string.menu_verify);
                cancelItem.setVisible(false);
                break;
            default:
                break;
        }
    }

    private void removeList(long taskId) {
        if (list == null || list.size() == 0) {
            return;
        }

        DUTask task;
        for (DUData data : list) {
            task = data.getDuTask();
            if (task.getTaskId() == taskId) {
                list.remove(data);
                break;
            }
        }
    }

    private void removeVerifyList(long taskId, int applyType) {
        if (list == null || list.size() == 0) {
            return;
        }

        int length = list.size();
        DUData duData;
        DUTask task;
        for (int i = 0; i < length; i++) {
            duData = list.get(i);
            task = duData.getDuTask();
            if (duData.getApplyType() == applyType && task.getTaskId() == taskId) {
                list.remove(i);
                length--;
                i--;
            }
        }

        List<DUData> filterList = adapter.getList();
        if (filterList == null || filterList.size() == 0) {
            return;
        }

        length = filterList.size();
        for (int i = 0; i < length; i++) {
            duData = filterList.get(i);
            task = duData.getDuTask();
            if (duData.getApplyType() == applyType && task.getTaskId() == taskId) {
                filterList.remove(i);
                length--;
                i--;
            }
        }
    }

    private void updateState(MessageUpdate messageUpdate) {
        DUTask task;
        for (DUData data : list) {
            task = data.getDuTask();
            if (task.getTaskId() == messageUpdate.getTaskId()) {
                task.setDelayState(messageUpdate.getDelayState());
                task.setHangUpState(messageUpdate.getHangUpState());
                break;
            }
        }
    }

    private void getVerifyData(List<DUData> list) {
        offset = 0;
        this.list.clear();

        List<DUData> filterList = new ArrayList<>();
        int applyType = judgeApplyType();

        if (list != null && list.size() > 0) {
            this.list.addAll(list);
            for (DUData data : list) {
                if (data.getApplyType() == applyType) {
                    filterList.add(data);
                }
            }
        }

        isScrollingFrozen = offset >= this.list.size();
        offset = this.list.size();

        isLoading = false;
        adapter.setShowProgress(false);
        adapter.setList(filterList);
        adapter.notifyDataSetChanged();

        hideMenuItem();
    }

    private void dispatch() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        showDispatchDialog();
    }

    private void showDispatchDialog() {
        DispatchUtil dispatchUtil = new DispatchUtil();
        acceptPersons = dispatchUtil.provideAcceptPerson(allPersons);
        assistPersons = dispatchUtil.provideAssistPerson(allPersons);
        if (acceptPersons == null || acceptPersons.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_get_accept_person_null);
            return;
        }

        if (assistPersons == null || assistPersons.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_get_assist_person_null);
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_dispatch)
                .customView(R.layout.dialog_dispatch, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (acceptPersons == null || acceptPersons.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_accept_person);
                        return;
                    }

                    String acceptAccount = TextUtil.EMPTY;
                    for (DUSearchPerson person : acceptPersons) {
                        if (person.isSelected()) {
                            acceptAccount = person.getAccount();
                            break;
                        }
                    }

                    if (TextUtil.isNullOrEmpty(acceptAccount)) {
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_select_accept_person);
                        return;
                    }

                    StringBuilder assistPerson = new StringBuilder(TextUtil.EMPTY);
                    if (assistPersons != null && assistPersons.size() > 0) {
                        for (DUSearchPerson person : assistPersons) {
                            if (!person.isSelected()) {
                                continue;
                            }

                            if (acceptAccount.equalsIgnoreCase(person.getAccount())) {
                                ApplicationsUtil.showMessage(this,
                                        R.string.toast_please_accept_person_equals_assist_person);
                                return;
                            }
                            assistPerson.append(ConstantUtil.CONNECT_SIGN).append(person.getAccount());
                        }
                    }

                    if (spDifficult == null || spDifficult.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_select_difficult_index);
                        return;
                    }

                    String assistAccount = TextUtil.isNullOrEmpty(assistPerson.toString())
                            ? TextUtil.EMPTY : assistPerson.substring(1);
                    String entrance = data.getTaskEntrance();
                    if (ConstantUtil.TaskEntrance.DISPATCH.equalsIgnoreCase(entrance)) {
                        dispatchDispatchTask(acceptAccount, assistAccount);
                    } else if (ConstantUtil.TaskEntrance.VERIFY.equalsIgnoreCase(entrance)) {
                        dispatchVerifyTask(acceptAccount, assistAccount);
                    }
                })
                .build();

        View view = materialDialog.getView();
        MultiSpinnerSearch mspAcceptPerson = (MultiSpinnerSearch) view.findViewById(R.id.mss_accept_person);
        mspAcceptPerson.setItems(acceptPersons, 0, false,
                items -> acceptPersons = items);

        spDifficult = (Spinner) view.findViewById(R.id.sp_difficult_index);
        SpinnerAdapter difficultAdapter = new SpinnerAdapter();
        difficultList = dispatchUtil.provideDifficult();
        difficultAdapter.setList(difficultList);
        spDifficult.setAdapter(difficultAdapter);

        spDriver = (Spinner) view.findViewById(R.id.sp_driver);
        SpinnerAdapter driverAdapter = new SpinnerAdapter();
        driverList = dispatchUtil.provideDriver(allPersons);
        driverAdapter.setList(driverList);
        spDriver.setAdapter(driverAdapter);

        MultiSpinnerSearch mspAssistPerson = (MultiSpinnerSearch) view.findViewById(R.id.mss_assist_person);
        mspAssistPerson.setItems(assistPersons, -1, true,
                items -> assistPersons = items);

        tvTime = (TextView) view.findViewById(R.id.tv_complete_date);
        chooseTime = Calendar.getInstance().getTimeInMillis();
        tvTime.setText(TextUtil.format(chooseTime, TextUtil.FORMAT_DATE));
        tvTime.setOnClickListener(this);
        materialDialog.show();
    }

    private void dispatchDispatchTask(String acceptAccount, String assistPerson) {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        StringBuilder taskIdStr = new StringBuilder(TextUtil.EMPTY);
        for (Long taskId : taskIds) {
            taskIdStr.append(ConstantUtil.CONNECT_SIGN).append(taskId);
        }

        DUDispatchHandle handle = new DUDispatchHandle();
        handle.setIds(taskIdStr.substring(1));
        handle.setType(list.get(0).getDuTask().getType());
        handle.setAcceptPerson(acceptAccount);
        handle.setDifficultIndex(difficultList.get(spDifficult.getSelectedItemPosition()).getValue());
        if (spDriver != null && spDriver.getSelectedItemPosition() != Spinner.INVALID_POSITION) {
            handle.setDriver(driverList.get(spDriver.getSelectedItemPosition()).getValue());
        }
        handle.setAssistPerson(assistPerson);
        handle.setCompleteTime(chooseTime);
        presenter.dispatch(handle);
    }

    private void dispatchVerifyTask(String acceptAccount, String assistPerson) {
        List<DUData> chooseList = new ArrayList<>();
        List<DUData> dataList = adapter.getList();
        for (DUData data : dataList) {
            if (data.isCheck()) {
                chooseList.add(data);
            }
        }

        if (chooseList.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
            return;
        }

        Collections.sort(chooseList, (o1, o2) -> o1.getDuTask().getType() - o2.getDuTask().getType());

        List<DUVerifyHandle> handles = new ArrayList<>();
        DUData duData = dataList.get(0);
        DUTask task = duData.getDuTask();
        int type = task.getType();
        DUVerifyHandle handle = new DUVerifyHandle();
        StringBuilder stringBuilder = new StringBuilder(TextUtil.EMPTY);
        handles.add(handle);
        for (DUData data : chooseList) {
            task = data.getDuTask();
            if (type != task.getType()) {
                type = task.getType();
                handle.setTaskIds(stringBuilder.substring(1));
                stringBuilder.setLength(0);
                handle = new DUVerifyHandle();
                handles.add(handle);
            }
            stringBuilder.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
            handle.setVerifyTime(System.currentTimeMillis());
            handle.setApplyType(data.getApplyType());
            handle.setType(type);
            handle.setAcceptPerson(acceptAccount);
            handle.setDifficultIndex(difficultList.get(spDifficult.getSelectedItemPosition()).getValue());
            if (spDriver != null && spDriver.getSelectedItemPosition() != Spinner.INVALID_POSITION) {
                handle.setDriver(driverList.get(spDriver.getSelectedItemPosition()).getValue());
            }
            handle.setAssistPerson(assistPerson);
            handle.setCompleteTime(chooseTime);
        }
        handle.setTaskIds(stringBuilder.substring(1));
        presenter.verifyTask(handles, duData.getApplyType());
    }

    private void transformDispatch() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        presenter.getAllStation();
    }

    private void cancelDispatch() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_cancel)
                .content(R.string.text_if_cancel)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    StringBuilder taskIdStr = new StringBuilder(TextUtil.EMPTY);
                    List<Long> taskList = getCheckTaskIds();
                    for (Long taskId : taskList) {
                        taskIdStr.append(ConstantUtil.CONNECT_SIGN).append(taskId);
                    }

                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    handle.setIds(taskIdStr.substring(1));
                    handle.setType(list.get(0).getDuTask().getType());
                    handle.setOperateType(ConstantUtil.DispatchOperate.CANCEL);
                    presenter.transformCancel(handle);
                })
                .build();
        materialDialog.show();
    }

    private void filterVerify() {
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.text_delay));
        items.add(getString(R.string.text_charge_back));
        items.add(getString(R.string.text_apply_help));
        items.add(getString(R.string.text_hang_up));
        items.add(getString(R.string.text_recovery));
        items.add(getString(R.string.title_report));

        materialDialog = new MaterialDialog.Builder(ListActivity.this)
                .title(R.string.text_prompt)
                .items(items)
                .itemsCallbackSingleChoice(filterPosition, (dialog, itemView, which, text) -> {
                    filterPosition = which;
                    floatingActionButton.setVisibility(View.GONE);
                    List<DUData> dataList = new ArrayList<>();
                    int applyType = judgeApplyType();
                    if (list != null && list.size() > 0) {
                        for (DUData data : list) {
                            if (data.getApplyType() == applyType) {
                                dataList.add(data);
                            }
                        }
                    }

                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                    verifyItem(dataList);
                    return true;
                })
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .build();
        materialDialog.show();
    }

    private void verify() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        switch (filterPosition) {
            case 0:
                verifyDelay();
                break;
            case 1:
                verifyReject();
                break;
            case 2:
                Log.i("abc", "协助");
                break;
            case 3:
                verifyHangUp();
                break;
            case 4:
                verifyHangUp();
                break;
            case 5:
                verifyReport();
                break;
            default:
                break;
        }
    }

    private void verifyDelay() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_approve)
                .customView(R.layout.dialog_delay_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (chooseTime == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_complete_time);
                        return;
                    }

                    List<DUData> dataList = adapter.getList();
                    if (dataList == null || dataList.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
                        return;
                    }

                    List<DUData> chooseList = new ArrayList<>();
                    for (DUData data : dataList) {
                        if (data.isCheck()) {
                            chooseList.add(data);
                        }
                    }

                    if (chooseList.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
                        return;
                    }

                    Collections.sort(chooseList, (o1, o2) -> o1.getDuTask().getType() - o2.getDuTask().getType());

                    String remark = etInput == null ? TextUtil.EMPTY : etInput.getText().toString();
                    List<DUVerifyHandle> handles = new ArrayList<>();
                    DUData duData = chooseList.get(0);
                    DUTask task = duData.getDuTask();
                    int type = task.getType();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    StringBuilder stringBuilder = new StringBuilder(TextUtil.EMPTY);
                    handles.add(handle);
                    for (DUData data : chooseList) {
                        task = data.getDuTask();
                        if (type != task.getType()) {
                            type = task.getType();
                            handle.setTaskIds(stringBuilder.substring(1));
                            stringBuilder.setLength(0);
                            handle = new DUVerifyHandle();
                            handles.add(handle);
                        }
                        stringBuilder.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
                        handle.setVerifyTime(System.currentTimeMillis());
                        handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                                ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                        handle.setApplyType(data.getApplyType());
                        handle.setType(type);
                        handle.setCompleteTime(chooseTime);
                        handle.setRemark(remark);
                    }
                    handle.setTaskIds(stringBuilder.substring(1));
                    presenter.verifyTask(handles, duData.getApplyType());
                })
                .build();

        View view = materialDialog.getView();
        initResultDialogSpinner(view);

        tvTime = (TextView) view.findViewById(R.id.tv_complete_date);
        chooseTime = Calendar.getInstance().getTimeInMillis();
        tvTime.setText(TextUtil.format(chooseTime, TextUtil.FORMAT_DATE));
        tvTime.setOnClickListener(this);

        etInput = (EditText) view.findViewById(R.id.et_remark);

        materialDialog.show();
    }

    private void verifyReject() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        showDispatchDialog();
    }

    private void verifyAssist() {
        if (allPersons == null || allPersons.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_null_person_info);
            presenter.getAllPerson();
            return;
        }

        DispatchUtil dispatchUtil = new DispatchUtil();
        assistPersons = dispatchUtil.provideAssistPerson(allPersons);
        if (assistPersons == null || assistPersons.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_get_assist_person_null);
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_dispatch)
                .customView(R.layout.dialog_assist_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (etInput == null) {
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_input_approve_person_number);
                        return;
                    }

                    String input = etInput.getText().toString();
                    if (TextUtil.isNullOrEmpty(input)) {
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_input_approve_person_number);
                        return;
                    }

                    if (!NumberUtil.isNumber(input)) {
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_approve_person_number_input_error);
                        return;
                    }

                    StringBuilder assistPerson = new StringBuilder(TextUtil.EMPTY);
                    if (assistPersons != null && assistPersons.size() > 0) {
                        for (DUSearchPerson person : assistPersons) {
                            if (!person.isSelected()) {
                                continue;
                            }

                            assistPerson.append(ConstantUtil.CONNECT_SIGN).append(person.getAccount());
                        }
                    }

                    DUTask task = data.getDuTask();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    handle.setTaskIds(String.valueOf(task.getTaskId()));
                    handle.setVerifyTime(System.currentTimeMillis());
                    handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                            ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                    handle.setApplyType(data.getApplyType());
                    handle.setType(task.getType());
                    handle.setPersonCount(NumberUtil.transformNumber(etInput.getText().toString()));
                    if (!TextUtil.isNullOrEmpty(assistPerson.toString())) {
                        handle.setAssistPerson(assistPerson.substring(1));
                    }
                    presenter.verifySingleTask(handle);
                })
                .build();

        View view = materialDialog.getView();
        initResultDialogSpinner(view);

        etInput = (EditText) view.findViewById(R.id.et_approve_person_number);

        MultiSpinnerSearch mspAssistPerson = (MultiSpinnerSearch) view.findViewById(R.id.mss_assist_person);
        mspAssistPerson.setItems(assistPersons, -1, true,
                items -> assistPersons = items);

        materialDialog.show();
    }

    private void verifyHangUp() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_approve)
                .customView(R.layout.dialog_hang_up_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    List<DUData> chooseList = new ArrayList<>();
                    List<DUData> dataList = adapter.getList();
                    for (DUData data : dataList) {
                        if (data.isCheck()) {
                            chooseList.add(data);
                        }
                    }

                    if (chooseList.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
                        return;
                    }

                    Collections.sort(chooseList, (o1, o2) -> o1.getDuTask().getType() - o2.getDuTask().getType());

                    String remark = etInput == null ? TextUtil.EMPTY : etInput.getText().toString();
                    List<DUVerifyHandle> handles = new ArrayList<>();
                    DUData duData = dataList.get(0);
                    DUTask task = duData.getDuTask();
                    int type = task.getType();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    StringBuilder stringBuilder = new StringBuilder(TextUtil.EMPTY);
                    handles.add(handle);
                    for (DUData data : chooseList) {
                        task = data.getDuTask();
                        if (type != task.getType()) {
                            type = task.getType();
                            handle.setTaskIds(stringBuilder.substring(1));
                            stringBuilder.setLength(0);
                            handle = new DUVerifyHandle();
                            handles.add(handle);
                        }
                        stringBuilder.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
                        handle.setVerifyTime(System.currentTimeMillis());
                        handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                                ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                        handle.setApplyType(data.getApplyType());
                        handle.setType(type);
                        handle.setRemark(remark);
                    }
                    handle.setTaskIds(stringBuilder.substring(1));
                    presenter.verifyTask(handles, duData.getApplyType());
                })
                .build();

        View view = materialDialog.getView();
        initResultDialogSpinner(view);
        etInput = (EditText) view.findViewById(R.id.et_remark);
        materialDialog.show();
    }

    private void verifyReport() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_report_verify)
                .customView(R.layout.dialog_report_verify, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (spSubType == null || spSubType.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_sub_type);
                        return;
                    }

                    List<DUData> chooseList = new ArrayList<>();
                    List<DUData> dataList = adapter.getList();
                    for (DUData data : dataList) {
                        if (data.isCheck()) {
                            chooseList.add(data);
                        }
                    }

                    if (chooseList.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
                        return;
                    }

                    Collections.sort(chooseList, (o1, o2) -> o1.getDuTask().getType() - o2.getDuTask().getType());

                    List<DUVerifyHandle> handles = new ArrayList<>();
                    DUData duData = dataList.get(0);
                    DUTask task = duData.getDuTask();
                    int type = task.getType();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    StringBuilder stringBuilder = new StringBuilder(TextUtil.EMPTY);
                    handles.add(handle);
                    for (DUData data : chooseList) {
                        task = data.getDuTask();
                        if (type != task.getType()) {
                            type = task.getType();
                            handle.setTaskIds(stringBuilder.substring(1));
                            stringBuilder.setLength(0);
                            handle = new DUVerifyHandle();
                            handles.add(handle);
                        }
                        stringBuilder.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
                        handle.setVerifyTime(System.currentTimeMillis());
                        handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                                ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                        handle.setApplyType(data.getApplyType());
                        handle.setType(ConstantUtil.WorkType.TASK_BIAOWU);
                        handle.setSubType(spSubType.getSelectedItemPosition() + 1);
                    }
                    handle.setTaskIds(stringBuilder.substring(1));
                    presenter.verifyTask(handles, duData.getApplyType());
                })
                .build();

        View view = materialDialog.getView();

        spResult = (Spinner) view.findViewById(R.id.sp_status);
        SpinnerAdapter adapter = new SpinnerAdapter();
        List<DUWord> results = new ArrayList<>();
        results.add(new DUWord(getString(R.string.text_pass), String.valueOf(ConstantUtil.VerifyResult.PASS)));
        results.add(new DUWord(getString(R.string.text_not_pass), String.valueOf(ConstantUtil.VerifyResult.REJECT)));
        adapter.setList(results);
        spResult.setAdapter(adapter);

        spSubType = (Spinner) view.findViewById(R.id.sp_task_sub_type);
        SpinnerAdapter subTypeAdapter = new SpinnerAdapter();
        List<DUWord> subTypeList = new ArrayList<>();
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_split),
                String.valueOf(ConstantUtil.WorkSubType.SPLIT_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_replace),
                String.valueOf(ConstantUtil.WorkSubType.REPLACE_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_reinstall),
                String.valueOf(ConstantUtil.WorkSubType.INSTALL_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_stop),
                String.valueOf(ConstantUtil.WorkSubType.STOP_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_move),
                String.valueOf(ConstantUtil.WorkSubType.MOVE_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_check),
                String.valueOf(ConstantUtil.WorkSubType.CHECK_METER)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_reuse),
                String.valueOf(ConstantUtil.WorkSubType.REUSE)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_notice),
                String.valueOf(ConstantUtil.WorkSubType.NOTICE)));
        subTypeList.add(new DUWord(getString(R.string.text_sub_type_verify),
                String.valueOf(ConstantUtil.WorkSubType.VERIFY)));
        subTypeAdapter.setList(subTypeList);
        spSubType.setAdapter(subTypeAdapter);

        etInput = (EditText) view.findViewById(R.id.et_approve_person_number);
        materialDialog.show();
    }

    private void cancelVerify() {
        List<Long> taskIds = getCheckTaskIds();
        if (taskIds == null || taskIds.size() == 0) {
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_cancel)
                .content(R.string.text_if_cancel)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    List<DUData> chooseList = new ArrayList<>();
                    List<DUData> dataList = adapter.getList();
                    for (DUData data : dataList) {
                        if (data.isCheck()) {
                            chooseList.add(data);
                        }
                    }

                    if (chooseList.size() == 0) {
                        ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
                        return;
                    }

                    Collections.sort(chooseList, (o1, o2) -> o1.getDuTask().getType() - o2.getDuTask().getType());

                    List<DUTransformCancelHandle> handles = new ArrayList<>();
                    DUData duData = dataList.get(0);
                    DUTask task = duData.getDuTask();
                    int type = task.getType();
                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    StringBuilder stringBuilder = new StringBuilder(TextUtil.EMPTY);
                    handles.add(handle);
                    for (DUData data : chooseList) {
                        task = data.getDuTask();
                        if (type != task.getType()) {
                            type = task.getType();
                            handle.setIds(stringBuilder.substring(1));
                            stringBuilder.setLength(0);
                            handle = new DUTransformCancelHandle();
                            handles.add(handle);
                        }
                        stringBuilder.append(ConstantUtil.CONNECT_SIGN).append(task.getTaskId());
                        handle.setOperateType(ConstantUtil.DispatchOperate.CANCEL);
                        handle.setType(type);
                    }
                    handle.setIds(stringBuilder.substring(1));
                    presenter.cancelVerify(handles, duData.getApplyType());
                })
                .build();
        materialDialog.show();
    }

    private void initResultDialogSpinner(View view) {
        spResult = (Spinner) view.findViewById(R.id.sp_approve_result);
        SpinnerAdapter adapter = new SpinnerAdapter();
        List<DUWord> results = new ArrayList<>();
        results.add(new DUWord(getString(R.string.text_pass), String.valueOf(ConstantUtil.VerifyResult.PASS)));
        results.add(new DUWord(getString(R.string.text_reject), String.valueOf(ConstantUtil.VerifyResult.REJECT)));
        adapter.setList(results);
        spResult.setAdapter(adapter);
    }

    private List<Long> getCheckTaskIds() {
        List<DUData> dataList = adapter.getList();

        if (dataList == null || dataList.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_null_data);
            return null;
        }

        DUTask task;
        List<Long> taskIds = new ArrayList<>();
        for (DUData data : dataList) {
            if (!data.isCheck()) {
                continue;
            }

            task = data.getDuTask();
            if (task == null) {
                continue;
            }

            taskIds.add(task.getTaskId());
        }

        if (taskIds.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_check_data);
        }

        return taskIds;
    }

}