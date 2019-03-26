package com.sh3h.workmanagerjm.ui.chargeBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.qrcode.Intents;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.myinterface.OnHandlerInterface;
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
import com.sh3h.workmanagerjm.ui.handler.chargeBack.ChargeBackFragment;
import com.sh3h.workmanagerjm.ui.handler.report.ReportFragment;
import com.sh3h.workmanagerjm.ui.multimedia.MultiMediaFragment;
import com.sh3h.workmanagerjm.ui.slide.SlideShowFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChargeBackActivity extends ParentActivity implements ChargeBackMvpView,
        View.OnClickListener, OnHandlerInterface {
    private static final String TAG = "ChargeBackActivity";
    //按返回键显示
    private static final int SHOW_HANDLE = 1;
    private static final int SHOW_MEDIA = 2;
    private static final int ACTIVITY_FINISH = 3;

    @Inject
    ChargeBackPresenter presenter;
    @Inject
    Bus bus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_tail)
    LinearLayout linearLayout;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.rb_detail)
    RadioButton rbDetail;
    @BindView(R.id.rb_handle)
    RadioButton rbHandle;
    @BindView(R.id.rb_multimedia)
    RadioButton rbMultiMedia;

    private Unbinder unbinder;
    private ParentFragment detailFragment; //详细fragment
    private HandlerFragment handlerFragment; //处理fragment
    private MultiMediaFragment mediaFragment;//多媒体fragment
    private SlideShowFragment slideShowFragment;//放映幻灯片的fragment
    private ParentFragment currentFragment;//当前fragment
    private MenuItem refreshItem;//刷新按钮
    private boolean initOver;//控制初始化
    private int pressBack;//点击back键的界面跳转
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_charge_back);
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
                if (bundle == null) {
                    ApplicationsUtil.showMessage(this, R.string.toast_scan_bar_code_error);
                    return;
                }
                String scanResult = bundle.getString(Intents.Scan.RESULT);
                if (TextUtil.isNullOrEmpty(scanResult)) {
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
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.CALL_PAY:
                getMenuInflater().inflate(R.menu.menu_charge_back, menu);
                refreshItem = menu.findItem(R.id.action_task);
                handlerTaskMenuItem();
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                getMenuInflater().inflate(R.menu.menu_manager_history, menu);
                refreshItem = menu.findItem(R.id.action_history);
                refreshItem.setVisible(!data.updateAllDate());
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
                switch (pressBack) {
                    case SHOW_MEDIA:
                        hideSlideShowFragment();
                        return false;
                    case ACTIVITY_FINISH:
                        if (data.isDirectFinish()) {
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (pressBack) {
                case SHOW_MEDIA:
                    hideSlideShowFragment();
                    return false;
                case ACTIVITY_FINISH:
                    if (data.isDirectFinish()) {
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
    public void handleError(int resourceId) {
        hideProgress();
        ApplicationsUtil.showMessage(this, resourceId);
    }

    @Override
    public void onReportNext(DUData duData) {
        hideProgress();

        int reportType = duData.getHandle().getReportType();
        switch (reportType) {
            case ConstantUtil.ReportType.Handle:
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
    protected void initConfigEnd() {
        super.initConfigEnd();
        if (!initOver) {
            initOver = true;
            showFirstFragment(null);
            linearLayout.setVisibility(View.VISIBLE);
            if (refreshItem != null) {
                refreshItem.setVisible(true);
            }
        }
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
    }

    //恢复fragment
    private void initFragment(Bundle bundle) {
        if (bundle != null) {
            DUTask task = data.getDuTask();
            switch (task.getType()) {
                case ConstantUtil.WorkType.TASK_BIAOWU:
                    switch (task.getSubType()) {
                        case ConstantUtil.WorkSubType.SPLIT_METER:
                            detailFragment = (SplitDetailFragment) getSupportFragmentManager().findFragmentByTag(SplitDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.REPLACE_METER:
                            detailFragment = (ReplaceDetailFragment) getSupportFragmentManager().findFragmentByTag(ReplaceDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.INSTALL_METER:
                            detailFragment = (ReinstallDetailFragment) getSupportFragmentManager().findFragmentByTag(ReinstallDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.STOP_METER:
                            detailFragment = (StopDetailFragment) getSupportFragmentManager().findFragmentByTag(StopDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.MOVE_METER:
                            detailFragment = (MoveDetailFragment) getSupportFragmentManager().findFragmentByTag(MoveDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.CHECK_METER:
                            detailFragment = (CheckDetailFragment) getSupportFragmentManager().findFragmentByTag(CheckDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.REUSE:
                            detailFragment = (ReuseDetailFragment) getSupportFragmentManager().findFragmentByTag(ReuseDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.NOTICE:
                            detailFragment = (NoticeDetailFragment) getSupportFragmentManager().findFragmentByTag(NoticeDetailFragment.class.getName());
                            break;
                        case ConstantUtil.WorkSubType.VERIFY:
                            detailFragment = (VerifyDetailFragment) getSupportFragmentManager().findFragmentByTag(VerifyDetailFragment.class.getName());
                            break;
                        default:
                            detailFragment = (SplitDetailFragment) getSupportFragmentManager().findFragmentByTag(SplitDetailFragment.class.getName());
                            break;
                    }
                    break;
                case ConstantUtil.WorkType.TASK_HOT:
                    detailFragment = (HotDetailFragment) getSupportFragmentManager().findFragmentByTag(HotDetailFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_INSIDE:
                    detailFragment = (InsideDetailFragment) getSupportFragmentManager().findFragmentByTag(InsideDetailFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_INSTALL_METER:
                    detailFragment = (InstallDetailFragment) getSupportFragmentManager().findFragmentByTag(InstallDetailFragment.class.getName());
                    break;
                case ConstantUtil.WorkType.TASK_CALL_PAY:
                    detailFragment = (CallPayDetailFragment) getSupportFragmentManager().findFragmentByTag(CallPayDetailFragment.class.getName());
                    break;
                default:
                    detailFragment = (InstallDetailFragment) getSupportFragmentManager().findFragmentByTag(InstallDetailFragment.class.getName());
                    break;
            }
            handlerFragment = (HandlerFragment) getSupportFragmentManager().findFragmentByTag(ChargeBackFragment.class.getName());
            mediaFragment = (MultiMediaFragment) getSupportFragmentManager().findFragmentByTag(MultiMediaFragment.class.getName());
            slideShowFragment = (SlideShowFragment) getSupportFragmentManager().findFragmentByTag(SlideShowFragment.class.getName());
        }
    }

    //协助时只能看到详细界面
    private void hideView() {
        switch (pressBack) {
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
    }

    //初始化toolBar
    private void initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setTitle(R.string.text_charge_back);
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
        }
    }

    private void showFirstFragment(Bundle bundle) {
        ParentFragment firstFragment;
        if (bundle == null) {
            firstFragment = getDetailFragment();
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
            handlerFragment = ChargeBackFragment.newInstance();
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
            } else {
                reportData();
            }
        } else {
            reportData();
        }
    }

    private void uploadHistory() {
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
            } else {
                reportData();
            }
        } else {
            reportData();
        }
    }

    private void reportData() {
        DUHandle handle = handlerFragment.getHandle();
        handle.setState(ConstantUtil.State.BACK);
        handle.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
        handle.setReportType(ConstantUtil.ReportType.Handle);
        historyCondition.setDuHandle(handle);
        historyCondition.setType(handle.getType());
        presenter.report(historyCondition);
    }

    private void temporaryData() {
        DUHandle handle = handlerFragment.getHandle();
        handle.setState(ConstantUtil.State.BACK);
        handle.setUploadFlag(ConstantUtil.UploadFlag.TEMPORARY_STORAGE);
        handle.setReportType(ConstantUtil.ReportType.SAVE);
        presenter.temporaryData(handle);
    }

    private void directUploadHistory() {
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

    private void confirmBack() {
        if (handlerFragment == null) {
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

    private void handlerTaskMenuItem() {
        if (pressBack == SHOW_HANDLE) {
            refreshItem.setVisible(false);
        } else {
            switch (data.getOperateType()) {
                case ConstantUtil.ClickType.TYPE_ITEM:
                    refreshItem.setVisible(false);
                    break;
                case ConstantUtil.ClickType.TYPE_HANDLE:
                    break;
                default:
                    break;
            }
        }
    }

}
