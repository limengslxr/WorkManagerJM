package com.sh3h.workmanagerjm.ui.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.dataprovider.data.entity.ui.DUAssistHandle;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUSearchPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.qrcode.Intents;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SpinnerAdapter;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.myinterface.OnHandlerInterface;
import com.sh3h.workmanagerjm.myinterface.OnInputMaterialListener;
import com.sh3h.workmanagerjm.service.SyncConst;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.ui.detail.callPay.CallPayDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.check.CheckDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.hot.HotDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.inside.InsideDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.install.InstallDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.move.MoveDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.notice.NoticeDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.reinstall.ReinstallDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.replace.ReplaceDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.reuse.ReuseDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.split.SplitDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.stop.StopDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.verify.VerifyDetailFragment;
import com.sh3h.workmanagerjm.ui.handler.HandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.assist.AssistHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.callPay.CallPayHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.check.CheckHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.delay.DelayHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.hangUp.HangUpHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.hot.HotHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.inputMaterial.InputMaterialHandleFragment;
import com.sh3h.workmanagerjm.ui.handler.inside.InsideHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.install.InstallHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.move.MoveHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.notice.NoticeHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.recovery.RecoveryHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.reinstall.ReinstallHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.reject.RejectHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.replace.ReplaceHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.report.ReportFragment;
import com.sh3h.workmanagerjm.ui.handler.reuse.ReuseHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.split.SplitHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.stop.StopHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.verify.VerifyHandlerFragment;
import com.sh3h.workmanagerjm.ui.multimedia.MultiMediaFragment;
import com.sh3h.workmanagerjm.ui.slide.SlideShowFragment;
import com.sh3h.workmanagerjm.util.DispatchUtil;
import com.sh3h.workmanagerjm.util.NumberUtil;
import com.sh3h.workmanagerjm.util.TransformUtil;
import com.sh3h.workmanagerjm.view.MultiSpinnerSearch;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManagerActivity extends ParentActivity implements View.OnClickListener,
        ManagerMvpView, OnHandlerInterface, OnInputMaterialListener{
    private static final String TAG = "ManagerActivity";
    //按返回键显示
    private static final int SHOW_HANDLE = 1;
    private static final int SHOW_MEDIA = 2;
    private static final int ACTIVITY_FINISH = 3;

    @Inject ManagerPresenter presenter;
    @Inject Bus bus;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ll_tail) LinearLayout linearLayout;
    @BindView(R.id.fl_content) FrameLayout flContent;
    @BindView(R.id.rb_detail) RadioButton rbDetail;
    @BindView(R.id.rb_handle) RadioButton rbHandle;
    @BindView(R.id.rb_multimedia) RadioButton rbMultiMedia;

    private Unbinder unbinder;
    private ParentFragment detailFragment; //详细fragment
    private HandlerFragment handlerFragment; //处理fragment
    private InputMaterialHandleFragment inputMaterialFragment;//输入材料的fragment
    private MultiMediaFragment mediaFragment;//多媒体fragment
    private SlideShowFragment slideShowFragment;//放映幻灯片的fragment
    private ParentFragment currentFragment;//当前fragment
    private MenuItem saveItem;//保存按鈕
    private MenuItem assistItem;//协助按钮
    private MenuItem refreshItem;//刷新按钮
    private MenuItem dispatchItem;//派遣
    private MenuItem cancelItem;//作废
    private boolean initOver;//控制初始化
    private int pressBack;//点击back键的界面跳转

    //申请延期的截止时间
    private TextView tvTime;
    private EditText etInput;//dialog
    private Spinner spDifficult, spDriver, spStation, spResult, spSubType;
    private long chooseTime;
    private List<DUSearchPerson> acceptPersons, assistPersons;
    private List<DUWord> difficultList, driverList, stationList;
    private List<DUPerson> allPersons;
    private boolean isHaveCancelPower = false;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manager);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        bus.register(this);
        presenter.attachView(this);

        initDUData(bundle);

        hideView();

        initToolBar();

        initFragment(bundle);

        controlLoadData(bundle);

        setOnListener();

        downloadMeterCard();

        setSwipeBackEnable(/*data.isDirectFinish()*/false);
    }

    @Override
    protected void onDestroy() {
        DUHandle handle = data.getHandle();
        if (handle != null && handle.getUploadFlag() == ConstantUtil.UploadFlag.INVAILD) {
            data.getHandles().remove(handle);
        }

        super.onDestroy();
        if (materialDialog != null){
            materialDialog.dismiss();
        }
        presenter.detachView();
        bus.unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantUtil.Parcel.DUDATA, data);
        outState.putString(ConstantUtil.CURRENT_FRAGMENT, currentFragment.getClass().getName());
        outState.putInt(ConstantUtil.Key.PRESS_BACK, pressBack);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_detail:
                showFragment(getDetailFragment());
                break;
            case R.id.rb_handle:
                showFragment(getHandlerFragment());
                break;
            case R.id.rb_multimedia:
                showMediaFragment();
                break;
            case R.id.tv_complete_date:
                selectTime(ConstantUtil.SelectTimeType.COMPLETE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case ConstantUtil.RequestCode.TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK && mediaFragment != null) {
                    mediaFragment.takePhotoResult();
                }
                break;
            case ConstantUtil.RequestCode.SELECT_PHOTO:
                if (mediaFragment != null) {
                    mediaFragment.selectPhotoResult(intent);
                }
                break;
            case ConstantUtil.RequestCode.LOCATE_MAP:
                if ((handlerFragment != null) &&
                        (data.getDuTask().getType() == ConstantUtil.WorkType.TASK_REPORT)
                        && (handlerFragment instanceof ReportFragment)) {
                    ((ReportFragment) handlerFragment).markMap(intent);
                }
                break;
            case ConstantUtil.RequestCode.SCAN:
                if (handlerFragment == null || intent == null || resultCode != Activity.RESULT_OK) {
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
                handlerFragment.scanResult(scanResult);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (data.getTaskEntrance()){
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
                getMenuInflater().inflate(R.menu.menu_manager_task, menu);
                assistItem = menu.findItem(R.id.action_help);
                refreshItem = menu.findItem(R.id.action_task);
                saveItem = menu.findItem(R.id.action_save);
                handlerTaskMenuItem();
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                getMenuInflater().inflate(R.menu.menu_manager_task, menu);
                assistItem = menu.findItem(R.id.action_help);
                refreshItem = menu.findItem(R.id.action_task);
                saveItem = menu.findItem(R.id.action_save);
                handlerCallPayMenuItem();
                break;
            case ConstantUtil.TaskEntrance.REPORT:
                getMenuInflater().inflate(R.menu.menu_manager_report, menu);
                refreshItem = menu.findItem(R.id.action_report);
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                getMenuInflater().inflate(R.menu.menu_manager_history, menu);
                refreshItem = menu.findItem(R.id.action_history);
                refreshItem.setVisible(!data.updateAllDate());
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                getMenuInflater().inflate(R.menu.menu_manager_dispatch, menu);
                dispatchItem = menu.findItem(R.id.action_dispatch_dispatch);
                MenuItem transformItem = menu.findItem(R.id.action_transform_dispatch);
                cancelItem = menu.findItem(R.id.action_cancel_dispatch);
                cancelItem.setVisible(isHaveCancelPower);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                getMenuInflater().inflate(R.menu.menu_manager_verify, menu);
                dispatchItem = menu.findItem(R.id.action_dispatch_verify);
                cancelItem = menu.findItem(R.id.action_cancel_verify);
                cancelItem.setVisible(isHaveCancelPower);
                handlerVerifyMenuItem();
                break;
            default:
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                switch (pressBack){
                    case SHOW_HANDLE:
                        hideMaterial();
                        return false;
                    case SHOW_MEDIA:
                        hideSlideShowFragment();
                        return false;
                    case ACTIVITY_FINISH:
                        if (data.isDirectFinish()){
                            finish();
                            break;
                        }
                        confirmBack();
                        return false;
                    default:
                        break;
                }
                break;
            case R.id.action_task:
                uploadTask();
                break;
            case R.id.action_history:
                uploadHistory();
                break;
            case R.id.action_save:
                saveMaterial();
                break;
            case R.id.action_report:
                uploadTask();
                break;
            case R.id.action_help:
                assist();
                break;
            case R.id.action_dispatch_dispatch:
                dispatch();
                break;
            case R.id.action_transform_dispatch:
                presenter.getAllStation();
                break;
            case R.id.action_cancel_dispatch:
                cancelTask();
                break;
            case R.id.action_dispatch_verify:
                dispatchVerify();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            switch (pressBack){
                case SHOW_HANDLE:
                    hideMaterial();
                    return false;
                case SHOW_MEDIA:
                    hideSlideShowFragment();
                    return false;
                case ACTIVITY_FINISH:
                    if (data.isDirectFinish()){
                        finish();
                        break;
                    }
                    confirmBack();
                    return false;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnShowPicDetail(int position, ArrayList<DUMedia> pictures) {
        linearLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        flContent.setBackgroundResource(R.color.color_black_000000);
        pressBack = SHOW_MEDIA;

        showSlideShowFragment(position, pictures);
    }

    @Override
    public void hideSlideShowFragment() {
        linearLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        flContent.setBackgroundResource(R.color.white);
        rbMultiMedia.setChecked(true);
        pressBack = ACTIVITY_FINISH;

        showMediaFragment();
    }

    @Override
    public void showInputMaterial(DUMaterial material) {
        if (inputMaterialFragment == null){
            inputMaterialFragment = InputMaterialHandleFragment.newInstance(material);
        }else {
            inputMaterialFragment.initView(material);
        }

        if (refreshItem != null){
            refreshItem.setVisible(false);
        }

        if (assistItem != null){
            assistItem.setVisible(false);
        }

        if (saveItem != null){
            saveItem.setVisible(true);
        }

        pressBack = SHOW_HANDLE;
        linearLayout.setVisibility(View.INVISIBLE);
        showFragment(inputMaterialFragment);
    }

    @Override
    public void handleError(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void handleError(String error) {
        hideProgress();
        ApplicationsUtil.showMessage(this, error);
    }

    @Override
    public void onReportNext(DUData duData) {
        hideProgress();

        int reportType = duData.getHandle().getReportType();
        switch (reportType) {
            case ConstantUtil.ReportType.Assist:
                assistItem.setVisible(false);
                duData.getHandle().setAssist(ConstantUtil.Assist.OK);
                ApplicationsUtil.showMessage(this, R.string.toast_assist_success);
                break;
            case ConstantUtil.ReportType.Handle:
            case ConstantUtil.ReportType.Report:
                syncOneTask(duData.getDuTask().getTaskId());
                break;
            case ConstantUtil.ReportType.SAVE:
                ApplicationsUtil.showMessage(this, R.string.toast_save_data_success);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getAllPerson(String account, List<DUPerson> listPerson) {
        allPersons = listPerson;

        if (listPerson == null || listPerson.size() == 0 || TextUtil.isNullOrEmpty(account)){
            return;
        }

        String platformRoles;
        for (DUPerson person : listPerson){
            if (!account.equalsIgnoreCase(person.getAccount())){
                continue;
            }

            platformRoles = person.getPlatformRoles();
            isHaveCancelPower = !TextUtil.isNullOrEmpty(platformRoles)
                    && platformRoles.contains("工单撤销");
            break;
        }

        if (cancelItem != null){
            cancelItem.setVisible(isHaveCancelPower);
        }
    }

    @Override
    public void dispatchError(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void dispatchSuccess(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
        finish();
    }

    @Override
    public void getAllStation(List<DUWord> list) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_transform)
                .customView(R.layout.dialog_transform, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spStation == null || spStation.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_station);
                        return;
                    }

                    String station = stationList.get(spStation.getSelectedItemPosition()).getValue();
                    DUTask task = data.getDuTask();
                    if (!TextUtil.isNullOrEmpty(station) && station.equalsIgnoreCase(task.getStation())){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_transform_station_select_current_station);
                        return;
                    }

                    if (etInput == null){
                        ApplicationsUtil.showMessage(this, R.string.toast_reason_null);
                        return;
                    }

                    String reason = etInput.getText().toString();
                    if (TextUtil.isNullOrEmpty(reason)){
                        ApplicationsUtil.showMessage(this, R.string.toast_reason_null);
                        return;
                    }

                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    handle.setIds(String.valueOf(task.getTaskId()));
                    handle.setType(task.getType());
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
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void verifySuccess(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
        finish();
    }

    @Override
    protected void initConfigEnd() {
        super.initConfigEnd();
        if (!initOver) {
            initOver = true;
            showFirstFragment(null);
            linearLayout.setVisibility(View.VISIBLE);
            if (refreshItem != null) {
                refreshItem.setVisible(true);
            }
            getAllPerson();
        }
    }

    @Override
    protected void selectTimeResult(int type, long time) {
        super.selectTimeResult(type, time);
        switch (type){
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

    public void scan(){
        Intent intent = new Intent(Intents.Scan.ACTION);
        startActivityForResult(intent, ConstantUtil.RequestCode.SCAN);
    }

    @Subscribe
    public void uploadOneTaskResult(UIBusEvent.UploadOneTaskResult result) {
        hideProgress();
        if (result.getTaskId() > 0 && result.isSuccess()) {
            ApplicationsUtil.showMessage(this, R.string.work_upload_success);
        } else {
            showErrorMessageAndFinish(R.string.work_upload_failure);
        }
    }

    @Subscribe
    public void uploadOneTaskMediaResult(UIBusEvent.UploadOneTaskMediaResult result) {
        hideProgress();
        if (result.getTaskId() > 0 && result.isSuccess()) {
            ApplicationsUtil.showMessage(this, result.getMediaNumber() > 0 ?
                    R.string.upload_pic_success : R.string.work_upload_success);
            finish();
        } else {
            showErrorMessageAndFinish(R.string.upload_pic_failure);
        }
    }

    @Subscribe
    public void networkError(UIBusEvent.NetworkNotConnect connect) {
        switch (connect.getOperate()) {
            case SyncConst.UPLOAD_ONE_WORK:
                hideProgress();
                showErrorMessageAndFinish(R.string.work_upload_failure);
                break;
            case SyncConst.UPLOAD_ONE_MEDIA:
                hideProgress();
                showErrorMessageAndFinish(R.string.upload_pic_failure);
                break;
            default:
                break;
        }
    }

    //数据源
    private void initDUData(Bundle bundle) {
        pressBack = bundle == null ? ACTIVITY_FINISH
                : bundle.getInt(ConstantUtil.Key.PRESS_BACK, ACTIVITY_FINISH);

        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.REPORT:
                initReportDUData(bundle);
                break;
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.CALL_PAY:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.HISTORY:
            case ConstantUtil.TaskEntrance.SEARCH:
                break;
            default:
                break;
        }
    }

    //恢复fragment
    private void initFragment(Bundle bundle) {
        if (bundle != null) {
            boolean isFromVerify = ConstantUtil.TaskEntrance.VERIFY.equals(data.getTaskEntrance());
            DUTask task = data.getDuTask();
            switch (task.getType()) {
                case ConstantUtil.WorkType.TASK_BIAOWU:
                    switch (task.getSubType()) {
                        case ConstantUtil.WorkSubType.SPLIT_METER:
                            detailFragment = (SplitDetailFragment) getSupportFragmentManager().findFragmentByTag(SplitDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (SplitHandlerFragment) getSupportFragmentManager().findFragmentByTag(SplitHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.REPLACE_METER:
                            detailFragment = (ReplaceDetailFragment) getSupportFragmentManager().findFragmentByTag(ReplaceDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (ReplaceHandlerFragment) getSupportFragmentManager().findFragmentByTag(ReplaceHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.INSTALL_METER:
                            detailFragment = (ReinstallDetailFragment) getSupportFragmentManager().findFragmentByTag(ReinstallDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (ReinstallHandlerFragment) getSupportFragmentManager().findFragmentByTag(ReinstallHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.STOP_METER:
                            detailFragment = (StopDetailFragment) getSupportFragmentManager().findFragmentByTag(StopDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (StopHandlerFragment) getSupportFragmentManager().findFragmentByTag(StopHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.MOVE_METER:
                            detailFragment = (MoveDetailFragment) getSupportFragmentManager().findFragmentByTag(MoveDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (MoveHandlerFragment) getSupportFragmentManager().findFragmentByTag(MoveHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.CHECK_METER:
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (CheckHandlerFragment) getSupportFragmentManager().findFragmentByTag(CheckHandlerFragment.class.getName());
                            detailFragment = (CheckDetailFragment) getSupportFragmentManager().findFragmentByTag(CheckDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.REUSE:
                            detailFragment = (ReuseDetailFragment) getSupportFragmentManager().findFragmentByTag(ReuseDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (ReuseHandlerFragment) getSupportFragmentManager().findFragmentByTag(ReuseHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.NOTICE:
                            detailFragment = (NoticeDetailFragment) getSupportFragmentManager().findFragmentByTag(NoticeDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (NoticeHandlerFragment) getSupportFragmentManager().findFragmentByTag(NoticeHandlerFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.VERIFY:
                            detailFragment = (VerifyDetailFragment) getSupportFragmentManager().findFragmentByTag(VerifyDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (VerifyHandlerFragment) getSupportFragmentManager().findFragmentByTag(VerifyHandlerFragment.class.getName());
                            break;
                        default:
                            detailFragment = (SplitDetailFragment) getSupportFragmentManager().findFragmentByTag(SplitDetailFragment.class.getName());
                            handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                                    : (SplitHandlerFragment) getSupportFragmentManager().findFragmentByTag(SplitHandlerFragment.class.getName());
                            break;
                    }
                    break;
                case ConstantUtil.WorkType.TASK_HOT:
                    detailFragment = (HotDetailFragment) getSupportFragmentManager().findFragmentByTag(HotDetailFragment.class.getName());
                    handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                            : (HotHandlerFragment) getSupportFragmentManager().findFragmentByTag(HotHandlerFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_INSIDE:
                    detailFragment = (InsideDetailFragment) getSupportFragmentManager().findFragmentByTag(InsideDetailFragment.class.getName());
                    handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                            : (InsideHandlerFragment) getSupportFragmentManager().findFragmentByTag(InsideHandlerFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_INSTALL_METER:
                    detailFragment = (InstallDetailFragment) getSupportFragmentManager().findFragmentByTag(InstallDetailFragment.class.getName());
                    handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                            : (InstallHandlerFragment) getSupportFragmentManager().findFragmentByTag(InstallHandlerFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_CALL_PAY:
                    detailFragment = (CallPayDetailFragment) getSupportFragmentManager().findFragmentByTag(CallPayDetailFragment.class.getName());
                    handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                            : (CallPayHandlerFragment) getSupportFragmentManager().findFragmentByTag(CallPayHandlerFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_REPORT:
                    handlerFragment = (ReportFragment) getSupportFragmentManager().findFragmentByTag(ReportFragment.class.getName());
                    break;
                default:
                    detailFragment = (InstallDetailFragment) getSupportFragmentManager().findFragmentByTag(InstallDetailFragment.class.getName());
                    handlerFragment = isFromVerify ? getVerifyHandlerFragment()
                            : (InstallHandlerFragment) getSupportFragmentManager().findFragmentByTag(InstallHandlerFragment.class.getName());
                    break;
            }
            mediaFragment = (MultiMediaFragment) getSupportFragmentManager().findFragmentByTag(MultiMediaFragment.class.getName());
            slideShowFragment = (SlideShowFragment) getSupportFragmentManager().findFragmentByTag(SlideShowFragment.class.getName());
            inputMaterialFragment = (InputMaterialHandleFragment) getSupportFragmentManager().findFragmentByTag(InputMaterialHandleFragment.class.getName());
        }
    }

    //协助时只能看到详细界面
    private void hideView() {
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.CALL_PAY:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.HISTORY:
                switch (pressBack){
                    case ACTIVITY_FINISH:
                        break;
                    case SHOW_HANDLE:
                        linearLayout.setVisibility(View.GONE);
                        break;
                    case SHOW_MEDIA:
                        break;
                    default:
                        break;
                }

                if (ConstantUtil.ClickType.TYPE_ITEM == data.getOperateType()) {
                    linearLayout.setVisibility(View.GONE);
                }

                if (data.getDuTask().getType() == ConstantUtil.WorkType.TASK_REPORT) {
                    rbDetail.setVisibility(View.GONE);
                    rbHandle.setChecked(true);
                }
                break;
            case ConstantUtil.TaskEntrance.REPORT:
                rbDetail.setVisibility(View.GONE);
                rbHandle.setChecked(true);
                break; //历史工单并且工单类型为上报
            case ConstantUtil.TaskEntrance.SEARCH:
                DUHandle handle = data.getHandle();
                if (ConstantUtil.ClickType.TYPE_ITEM == data.getOperateType()
                    && (handle == null || handle.getState() != ConstantUtil.State.HANDLE )) {
                    linearLayout.setVisibility(View.GONE);
                }
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                linearLayout.setVisibility(View.GONE);
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                switch (data.getApplyType()){
                    case ConstantUtil.ApplyType.DEFAULT:
                        rbMultiMedia.setVisibility(View.GONE);
                        break;
                    case ConstantUtil.ApplyType.DELAY:
                        rbMultiMedia.setVisibility(View.GONE);
                        break;
                    case ConstantUtil.ApplyType.HANG_UP:
                        rbMultiMedia.setVisibility(View.GONE);
                        break;
                    case ConstantUtil.ApplyType.RECOVERY:
                        rbMultiMedia.setVisibility(View.GONE);
                        break;
                    case ConstantUtil.ApplyType.ASSIST:
                        rbMultiMedia.setVisibility(View.GONE);
                        break;
                    case ConstantUtil.ApplyType.REPORT:
                        rbDetail.setVisibility(View.GONE);
                        rbHandle.setChecked(true);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    //初始化toolBar
    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(TransformUtil.getTypeResourceId(data.getTaskEntrance()));
        setSupportActionBar(toolbar);
    }

    private void controlLoadData(Bundle bundle) {
        if (initStep < ConstantUtil.InitStep.CLEAN_DATA) {
            initOver = false;
            linearLayout.setVisibility(View.GONE);
            if (refreshItem != null) {
                refreshItem.setVisible(false);
            }
        } else {
            initOver = true;
            showFirstFragment(bundle);
            getAllPerson();
        }
    }

    private void showFirstFragment(Bundle bundle) {
        ParentFragment firstFragment;
        if (bundle == null) {
            if (ConstantUtil.WorkType.TASK_REPORT == data.getDuTask().getType()
                    || (ConstantUtil.TaskEntrance.VERIFY.equals(data.getTaskEntrance())
                        && data.getApplyType() == ConstantUtil.ApplyType.REPORT)) {
                firstFragment = getHandlerFragment();
            } else {
                firstFragment = getDetailFragment();
            }
        } else {
            firstFragment = (ParentFragment) getSupportFragmentManager().findFragmentByTag(bundle.getString(ConstantUtil.CURRENT_FRAGMENT));
            if (SlideShowFragment.class.getName().equals(firstFragment.getClass().getName())) {
                linearLayout.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                flContent.setBackgroundResource(R.color.color_black_000000);
            }
        }
        showFragment(firstFragment);
    }

    //获取detailFragment
    private ParentFragment getDetailFragment() {
        if (detailFragment == null) {
            DUTask task = data.getDuTask();
            switch (task.getType()) {
                case ConstantUtil.WorkType.TASK_BIAOWU:
                    switch (task.getSubType()) {
                        case ConstantUtil.WorkSubType.SPLIT_METER:
                            detailFragment = SplitDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.REPLACE_METER:
                            detailFragment = ReplaceDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.INSTALL_METER:
                            detailFragment = ReinstallDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.STOP_METER:
                            detailFragment = StopDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.MOVE_METER:
                            detailFragment = MoveDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.CHECK_METER:
                            detailFragment = CheckDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.REUSE:
                            detailFragment = ReuseDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.NOTICE:
                            detailFragment = NoticeDetailFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.VERIFY:
                            detailFragment = VerifyDetailFragment.newInstance();
                            break;
                        default:
                            detailFragment = InstallDetailFragment.newInstance();
                            break;
                    }
                    break;
                case ConstantUtil.WorkType.TASK_HOT:
                    detailFragment = HotDetailFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_INSIDE:
                    detailFragment = InsideDetailFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_INSTALL_METER:
                    detailFragment = InstallDetailFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_CALL_PAY:
                    detailFragment = CallPayDetailFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_REPORT:
                    break;
                default:
                    detailFragment = InstallDetailFragment.newInstance();
                    break;
            }
        }
        return detailFragment;
    }

    //获取handleFragment
    private HandlerFragment getHandlerFragment() {
        if (handlerFragment == null) {
            if (ConstantUtil.TaskEntrance.VERIFY.equals(data.getTaskEntrance())){
                handlerFragment = getVerifyHandlerFragment();
                return handlerFragment;
            }

            DUTask task = data.getDuTask();
            switch (task.getType()) {
                case ConstantUtil.WorkType.TASK_BIAOWU:
                    switch (task.getSubType()) {
                        case ConstantUtil.WorkSubType.SPLIT_METER:
                            handlerFragment = SplitHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.REPLACE_METER:
                            handlerFragment = ReplaceHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.INSTALL_METER:
                            handlerFragment = ReinstallHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.STOP_METER:
                            handlerFragment = StopHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.MOVE_METER:
                            handlerFragment = MoveHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.CHECK_METER:
                            handlerFragment = CheckHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.REUSE:
                            handlerFragment = ReuseHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.NOTICE:
                            handlerFragment = NoticeHandlerFragment.newInstance();
                            break;
                        case ConstantUtil.WorkSubType.VERIFY:
                            handlerFragment = VerifyHandlerFragment.newInstance();
                            break;
                        default:
                            handlerFragment = SplitHandlerFragment.newInstance();
                            break;
                    }
                    break;
                case ConstantUtil.WorkType.TASK_HOT:
                    handlerFragment = HotHandlerFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_INSIDE:
                    handlerFragment = InsideHandlerFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_INSTALL_METER:
                    handlerFragment = InstallHandlerFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_CALL_PAY:
                    handlerFragment = CallPayHandlerFragment.newInstance();
                    break;
                case ConstantUtil.WorkType.TASK_REPORT:
                    handlerFragment = ReportFragment.newInstance(ConstantUtil.TaskEntrance.REPORT.equals(data.getTaskEntrance()));
                    break;
                default:
                    handlerFragment = InstallHandlerFragment.newInstance();
                    break;
            }
        }
        return handlerFragment;
    }

    //展示fragment
    private void showMediaFragment() {
        if (mediaFragment == null) {
            mediaFragment = MultiMediaFragment.newInstance();
        }
        showFragment(mediaFragment);
    }

    private HandlerFragment getVerifyHandlerFragment(){
        HandlerFragment handlerFragment;
        int applyType = data.getApplyType();
        switch (applyType){
            case ConstantUtil.ApplyType.DEFAULT:
                handlerFragment = RejectHandlerFragment.newInstance();
                break;
            case ConstantUtil.ApplyType.DELAY:
                handlerFragment = DelayHandlerFragment.newInstance();
                break;
            case ConstantUtil.ApplyType.HANG_UP:
                handlerFragment = HangUpHandlerFragment.newInstance();
                break;
            case ConstantUtil.ApplyType.RECOVERY:
                handlerFragment = RecoveryHandlerFragment.newInstance();
                break;
            case ConstantUtil.ApplyType.ASSIST:
                handlerFragment = AssistHandlerFragment.newInstance();
                break;
            case ConstantUtil.ApplyType.REPORT:
                handlerFragment = ReportFragment.newInstance(false);
                break;
            default:
                handlerFragment = RejectHandlerFragment.newInstance();
                break;
        }

        return handlerFragment;
    }

    private void showSlideShowFragment(int position, ArrayList<DUMedia> duMedias) {
        if (slideShowFragment == null) {
            slideShowFragment = SlideShowFragment.newInstance(position, duMedias);
        } else {
            slideShowFragment.notifyDataChange(position, duMedias);
        }
        showFragment(slideShowFragment);
    }

    /**
     * 使用show() hide()切换界面
     */
    private void showFragment(ParentFragment fragment) {
        if (fragment == null) {
            return;
        }
        if (fragment != currentFragment) {
            FragmentTransaction transaction = getSupportFragmentManager().
                    beginTransaction();
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            if (!fragment.isAdded()) {
                //没有被添加过
                transaction.add(R.id.fl_content, fragment, fragment.getClass().getName()).commit();
            } else {
                transaction.show(fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    //添加监听器
    private void setOnListener() {
        rbDetail.setOnClickListener(this);
        rbHandle.setOnClickListener(this);
        rbMultiMedia.setOnClickListener(this);
    }

    private void getAllPerson(){
        String entrance = data.getTaskEntrance();
        if (ConstantUtil.TaskEntrance.DISPATCH.equalsIgnoreCase(entrance)
                || ConstantUtil.TaskEntrance.VERIFY.equalsIgnoreCase(entrance)){
            presenter.getAllPerson();
        }
    }

    //上报、表务、装表、历史
    private void uploadTask() {
        if (handlerFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.toast_please_inspect_handle);
            showFragment(getHandlerFragment());
            rbHandle.setChecked(true);
        } else if (!handlerFragment.checkDataInvalid()) {
            showFragment(getHandlerFragment());
            rbHandle.setChecked(true);
        } else if (handlerFragment.isNeedPicture()) {
            if (mediaFragment == null) {
                ApplicationsUtil.showMessage(this, R.string.toast_media_fragment_null);
                hideKeyBoard();
                showMediaFragment();
                rbMultiMedia.setChecked(true);
            } else if (!mediaFragment.havePhoto()) {
                ApplicationsUtil.showMessage(this, R.string.toast_media_null);
                showMediaFragment();
                rbMultiMedia.setChecked(true);
            }else {
                reportData();
            }
        } else {
            reportData();
        }
    }

    private void reportData(){
        DUHandle handle = handlerFragment.getHandle();
        handle.setState(ConstantUtil.State.HANDLE);
        handle.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
//        handle.setReportType(ConstantUtil.TaskEntrance.REPORT.equals(data.getTaskEntrance())
//                ? ConstantUtil.ReportType.Report : ConstantUtil.ReportType.Handle);
        historyCondition.setDuHandle(handle);
        historyCondition.setType(handle.getType());
        presenter.report(historyCondition);
    }

    private void temporaryData(){
        DUHandle handle = handlerFragment.getHandle();
        handle.setState(ConstantUtil.State.HANDLE);
        handle.setUploadFlag(ConstantUtil.UploadFlag.TEMPORARY_STORAGE);
        handle.setReportType(ConstantUtil.ReportType.SAVE);
        presenter.temporaryData(handle);
    }

    private void uploadHistory(){
        if (handlerFragment == null) {
            directUploadHistory();
        } else if (!handlerFragment.checkDataInvalid()) {
            showFragment(getHandlerFragment());
            rbHandle.setChecked(true);
        } else if (handlerFragment.isNeedPicture()) {
            if (mediaFragment == null) {
                ApplicationsUtil.showMessage(this, R.string.toast_media_fragment_null);
                hideKeyBoard();
                showMediaFragment();
                rbMultiMedia.setChecked(true);
            } else if (!mediaFragment.havePhoto()) {
                ApplicationsUtil.showMessage(this, R.string.toast_media_null);
                showMediaFragment();
                rbMultiMedia.setChecked(true);
            }else {
                reportData();
            }
        } else {
            reportData();
        }
    }

    private void directUploadHistory(){
        List<DUHandle> handles = data.getHandles();
        boolean infoUpload = false;
        if (handles != null && handles.size() > 0) {
            for (DUHandle handle : handles) {
                if (handle.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                    infoUpload = true;
                    break;
                }
            }
        }

        if (infoUpload) {
            syncOneTask(data.getDuTask().getTaskId());
        } else {
            syncOneMedias(data.getDuTask().getTaskId());
        }
    }

    private void saveMaterial(){
        if (!inputMaterialFragment.checkDataInvalid()){
            return;
        }

        hideMaterial();
        DUMaterial material = inputMaterialFragment.provideMaterial();
        handlerFragment.saveMaterial(material);
    }

    private void hideMaterial(){
        hideKeyBoard();
        if (!(ConstantUtil.TaskEntrance.SEARCH.equals(data.getTaskEntrance()) ||
                (ConstantUtil.TaskEntrance.HISTORY.equals(data.getTaskEntrance()) && data.updateAllDate()))) {
            refreshItem.setVisible(true);
        }
        if (saveItem != null){
            saveItem.setVisible(false);
        }
        linearLayout.setVisibility(View.VISIBLE);
        pressBack = ACTIVITY_FINISH;
        showFragment(getHandlerFragment());
    }

    private void assist() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_apply_assist)
                .customView(R.layout.dialog_apply_assist, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    View customView = dialog.getCustomView();
                    if (customView == null) {
                        return;
                    }

                    EditText etPerson = (EditText) customView.findViewById(R.id.et_assist_person);
                    EditText etRemark = (EditText) customView.findViewById(R.id.et_remark);
                    String remark = etRemark.getText().toString();
                    String person = etPerson.getText().toString();
                    DUHandle handle = data.initDUHandle();
                    handle.setReportType(ConstantUtil.ReportType.Assist);
                    DUAssistHandle assistHandle = handle.toDUAssistHandle();
                    assistHandle.setAssistPersonCount(Integer.parseInt(person));
                    assistHandle.setRemark(remark);
                    handle.setReply(assistHandle);
                    handle.setAssist(ConstantUtil.Assist.OK);
                    historyCondition.setDuHandle(handle);
                    presenter.report(historyCondition);
                    hideKeyBoard();
                })
                .build();
        materialDialog.show();
    }

    private void dispatch() {
        if (allPersons == null || allPersons.size() == 0){
            ApplicationsUtil.showMessage(this, R.string.toast_null_person_info);
            presenter.getAllPerson();
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_dispatch)
                .customView(R.layout.dialog_dispatch, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (acceptPersons == null || acceptPersons.size() == 0){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_accept_person);
                        return;
                    }

                    String acceptAccount = TextUtil.EMPTY;
                    for (DUSearchPerson person : acceptPersons){
                        if (person.isSelected()){
                            acceptAccount = person.getAccount();
                            break;
                        }
                    }

                    if (TextUtil.isNullOrEmpty(acceptAccount)){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_select_accept_person);
                        return;
                    }

                    StringBuilder assistPerson = new StringBuilder(TextUtil.EMPTY);
                    if (assistPersons != null && assistPersons.size() > 0){
                        for (DUSearchPerson person : assistPersons){
                            if (!person.isSelected()){
                                continue;
                            }

                            if (acceptAccount.equalsIgnoreCase(person.getAccount())){
                                ApplicationsUtil.showMessage(this,
                                        R.string.toast_please_accept_person_equals_assist_person);
                                return;
                            }
                            assistPerson.append(ConstantUtil.CONNECT_SIGN).append(person.getAccount());
                        }
                    }

                    if (spDifficult == null || spDifficult.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_select_difficult_index);
                        return;
                    }

                    String assistAccount = TextUtil.isNullOrEmpty(assistPerson.toString())
                            ? TextUtil.EMPTY :  assistPerson.substring(1);
                    String entrance = data.getTaskEntrance();
                    if (ConstantUtil.TaskEntrance.DISPATCH.equalsIgnoreCase(entrance)){
                        dispatchDispatchTask(acceptAccount, assistAccount);
                    } else if (ConstantUtil.TaskEntrance.VERIFY.equalsIgnoreCase(entrance)){
                        dispatchVerifyTask(acceptAccount, assistAccount);
                    }
                })
                .build();

        DispatchUtil dispatchUtil = new DispatchUtil();
        View view = materialDialog.getView();
        MultiSpinnerSearch mspAcceptPerson = (MultiSpinnerSearch) view.findViewById(R.id.mss_accept_person);
        acceptPersons = dispatchUtil.provideAcceptPerson(allPersons);
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
        assistPersons = dispatchUtil.provideAssistPerson(allPersons);
        mspAssistPerson.setItems(assistPersons, -1, true,
                items -> assistPersons = items);

        tvTime = (TextView) view.findViewById(R.id.tv_complete_date);
        chooseTime = Calendar.getInstance().getTimeInMillis();
        tvTime.setText(TextUtil.format(chooseTime, TextUtil.FORMAT_DATE));
        tvTime.setOnClickListener(this);
        materialDialog.show();
    }

    private void dispatchDispatchTask(String acceptAccount, String assistPerson){
        DUTask task = data.getDuTask();
        DUDispatchHandle handle = new DUDispatchHandle();
        handle.setIds(String.valueOf(task.getTaskId()));
        handle.setType(task.getType());
        handle.setAcceptPerson(acceptAccount);
        handle.setDifficultIndex(difficultList.get(spDifficult.getSelectedItemPosition()).getValue());
        if (spDriver != null && spDriver.getSelectedItemPosition() != Spinner.INVALID_POSITION){
            handle.setDriver(driverList.get(spDriver.getSelectedItemPosition()).getValue());
        }
        handle.setAssistPerson(assistPerson);
        handle.setCompleteTime(chooseTime);
        presenter.dispatch(handle);
    }

    private void dispatchVerifyTask(String acceptAccount, String assistPerson) {
        DUTask task = data.getDuTask();
        DUVerifyHandle handle = new DUVerifyHandle();
        handle.setTaskIds(String.valueOf(task.getTaskId()));
        handle.setVerifyTime(System.currentTimeMillis());
        handle.setApplyType(data.getApplyType());
        handle.setType(task.getType());
        handle.setAcceptPerson(acceptAccount);
        handle.setDifficultIndex(difficultList.get(spDifficult.getSelectedItemPosition()).getValue());
        if (spDriver != null && spDriver.getSelectedItemPosition() != Spinner.INVALID_POSITION){
            handle.setDriver(driverList.get(spDriver.getSelectedItemPosition()).getValue());
        }
        handle.setAssistPerson(assistPerson);
        handle.setCompleteTime(chooseTime);
        presenter.verify(handle);
    }

    private void cancelTask(){
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_cancel)
                .content(R.string.text_if_cancel)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    handle.setIds(String.valueOf(data.getDuTask().getTaskId()));
                    handle.setOperateType(ConstantUtil.DispatchOperate.CANCEL);
                    presenter.transformCancel(handle);
                })
                .build();
        materialDialog.show();
    }

    private void dispatchVerify(){
        int applyType = data.getApplyType();
        switch (applyType){
            case ConstantUtil.ApplyType.DEFAULT:
                dispatch();
                break;
            case ConstantUtil.ApplyType.DELAY:
                verifyDelay();
                break;
            case ConstantUtil.ApplyType.HANG_UP:
                verifyHangUp();
                break;
            case ConstantUtil.ApplyType.RECOVERY:
                verifyHangUp();
                break;
            case ConstantUtil.ApplyType.ASSIST:
                verifyAssist();
                break;
            case ConstantUtil.ApplyType.REPORT:
                verifyReport();
                break;
            default:
                break;
        }
    }

    private void verifyDelay(){
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_approve)
                .customView(R.layout.dialog_delay_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (chooseTime == 0){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_complete_time);
                        return;
                    }

                    DUTask task = data.getDuTask();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    handle.setTaskIds(String.valueOf(task.getTaskId()));
                    handle.setVerifyTime(System.currentTimeMillis());
                    handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                            ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                    handle.setApplyType(data.getApplyType());
                    handle.setType(task.getType());
                    handle.setCompleteTime(chooseTime);
                    handle.setRemark(etInput == null
                            ? TextUtil.EMPTY : etInput.getText().toString());
                    presenter.verify(handle);
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

    private void verifyAssist() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_dispatch)
                .customView(R.layout.dialog_assist_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (etInput == null){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_input_approve_person_number);
                        return;
                    }

                    String input = etInput.getText().toString();
                    if (TextUtil.isNullOrEmpty(input)){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_please_input_approve_person_number);
                        return;
                    }

                    if (!NumberUtil.isNumber(input)){
                        ApplicationsUtil.showMessage(this,
                                R.string.toast_approve_person_number_input_error);
                        return;
                    }

                    StringBuilder assistPerson = new StringBuilder(TextUtil.EMPTY);
                    if (assistPersons != null && assistPersons.size() > 0){
                        for (DUSearchPerson person : assistPersons){
                            if (!person.isSelected()){
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
                    if (!TextUtil.isNullOrEmpty(assistPerson.toString())){
                        handle.setAssistPerson(assistPerson.substring(1));
                    }
                    presenter.verify(handle);
                })
                .build();

        DispatchUtil dispatchUtil = new DispatchUtil();
        View view = materialDialog.getView();
        initResultDialogSpinner(view);

        etInput = (EditText) view.findViewById(R.id.et_approve_person_number);

        MultiSpinnerSearch mspAssistPerson = (MultiSpinnerSearch) view.findViewById(R.id.mss_assist_person);
        assistPersons = dispatchUtil.provideAssistPerson(allPersons);
        mspAssistPerson.setItems(assistPersons, -1, true,
                items -> assistPersons = items);

        materialDialog.show();
    }

    private void verifyHangUp() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_task_approve)
                .customView(R.layout.dialog_hang_up_approve, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    DUTask task = data.getDuTask();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    handle.setTaskIds(String.valueOf(task.getTaskId()));
                    handle.setVerifyTime(System.currentTimeMillis());
                    handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                            ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                    handle.setApplyType(data.getApplyType());
                    handle.setType(task.getType());
                    handle.setRemark(etInput == null
                            ? TextUtil.EMPTY : etInput.getText().toString());
                    presenter.verify(handle);
                })
                .build();

        View view = materialDialog.getView();
        initResultDialogSpinner(view);
        etInput = (EditText) view.findViewById(R.id.et_remark);
        materialDialog.show();
    }

    private void verifyReport() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_report_verify)
                .customView(R.layout.dialog_report_verify, false)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    if (spResult == null || spResult.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_approve_result);
                        return;
                    }

                    if (spSubType == null || spSubType.getSelectedItemPosition() == Spinner.INVALID_POSITION){
                        ApplicationsUtil.showMessage(this, R.string.toast_please_select_sub_type);
                        return;
                    }

                    DUTask task = data.getDuTask();
                    DUVerifyHandle handle = new DUVerifyHandle();
                    handle.setTaskIds(String.valueOf(task.getTaskId()));
                    handle.setVerifyTime(System.currentTimeMillis());
                    handle.setVerifyResult(spResult.getSelectedItemPosition() == 0
                            ? ConstantUtil.VerifyResult.PASS : ConstantUtil.VerifyResult.REJECT);
                    handle.setApplyType(data.getApplyType());
                    handle.setType(ConstantUtil.WorkType.TASK_BIAOWU);
                    handle.setSubType(spSubType.getSelectedItemPosition() + 1);
                    presenter.verify(handle);
                })
                .build();

        View view = materialDialog.getView();

        spResult = (Spinner) view.findViewById(R.id.sp_status);
        SpinnerAdapter adapter = new SpinnerAdapter();
        List<DUWord> results = new ArrayList<>();
        results.add(new DUWord(getString(R.string.text_pass),
                String.valueOf(ConstantUtil.VerifyResult.PASS)));
        results.add(new DUWord(getString(R.string.text_not_pass),
                String.valueOf(ConstantUtil.VerifyResult.REJECT)));
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

        materialDialog.show();
    }

    private void cancelVerify(){
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_cancel)
                .content(R.string.text_if_cancel)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    DUTask task = data.getDuTask();
                    DUTransformCancelHandle handle = new DUTransformCancelHandle();
                    handle.setIds(String.valueOf(task.getTaskId()));
                    handle.setOperateType(ConstantUtil.DispatchOperate.CANCEL);
                    handle.setType(task.getType());
                    presenter.transformCancel(handle);
                })
                .build();
        materialDialog.show();
    }

    private void confirmBack() {
        if (handlerFragment == null){
            finish();
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.text_prompt)
                .content(R.string.toast_if_sure_back)
                .positiveText(R.string.text_ok)
                .negativeText(R.string.text_cancel)
                .onPositive((dialog, which) -> {
                    handlerFragment.temporaryStorage();
                    temporaryData();
                })
                .onNegative((dialog, which) -> finish())
                .build();
        materialDialog.show();
    }

    private void initReportDUData(Bundle bundle) {
        if (bundle != null){
            return;
        }

        data = new DUData();
        data.setTaskEntrance(ConstantUtil.TaskEntrance.REPORT);
        data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);

        DUTask task = new DUTask();
        long time = System.currentTimeMillis();
        task.setAccount(presenter.getAccount());
        task.setTaskId(time);
        task.setType(ConstantUtil.WorkType.TASK_REPORT);
        task.setSubType(0);
        task.setState(ConstantUtil.State.HANDLE);
        data.setDuTask(task);

        data.setHandles(new ArrayList<>());
    }

    private void handlerTaskMenuItem(){
        if (pressBack == SHOW_HANDLE){
            assistItem.setVisible(false);
            refreshItem.setVisible(false);
            saveItem.setVisible(true);
        } else {
            switch (data.getOperateType()) {
                case ConstantUtil.ClickType.TYPE_ITEM:
                    assistItem.setVisible(false);
                    refreshItem.setVisible(false);
                    break;
                case ConstantUtil.ClickType.TYPE_HANDLE:
                    DUHandle handle = data.getHandle();
                    assistItem.setVisible(handle == null || handle.getAssist() != ConstantUtil.Assist.OK);
                    break;
                default:
                    break;
            }
        }
    }

    private void handlerCallPayMenuItem(){
        if (pressBack == SHOW_HANDLE){
            assistItem.setVisible(false);
            refreshItem.setVisible(false);
            saveItem.setVisible(true);
        } else {
            refreshItem.setVisible(data.getOperateType() != ConstantUtil.ClickType.TYPE_PROCESS);
            assistItem.setVisible(false);
        }
    }

    private void handlerVerifyMenuItem(){
        switch (data.getApplyType()){
            case ConstantUtil.ApplyType.DEFAULT:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_dispatch);
                cancelItem.setVisible(true);
                break;
            case ConstantUtil.ApplyType.DELAY:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case ConstantUtil.ApplyType.ASSIST:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_dispatch);
                cancelItem.setVisible(false);
                break;
            case ConstantUtil.ApplyType.HANG_UP:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case ConstantUtil.ApplyType.RECOVERY:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_approve);
                cancelItem.setVisible(false);
                break;
            case ConstantUtil.ApplyType.REPORT:
                dispatchItem.setVisible(true);
                dispatchItem.setTitle(R.string.menu_verify);
                cancelItem.setVisible(false);
                break;
            default:
                break;
        }
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

}
