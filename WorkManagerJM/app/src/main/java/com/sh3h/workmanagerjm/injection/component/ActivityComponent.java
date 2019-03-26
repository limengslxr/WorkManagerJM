package com.sh3h.workmanagerjm.injection.component;


import com.sh3h.workmanagerjm.injection.annotation.PerActivity;
import com.sh3h.workmanagerjm.injection.module.ActivityModule;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.chargeBack.ChargeBackActivity;
import com.sh3h.workmanagerjm.ui.detail.split.SplitDetailFragment;
import com.sh3h.workmanagerjm.ui.detail.stop.StopDetailFragment;
import com.sh3h.workmanagerjm.ui.dispatch.DispatchActivity;
import com.sh3h.workmanagerjm.ui.handler.check.CheckHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.hot.HotHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.inputMaterial.InputMaterialHandleFragment;
import com.sh3h.workmanagerjm.ui.handler.inside.InsideHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.install.InstallHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.move.MoveHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.notice.NoticeHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.reinstall.ReinstallHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.replace.ReplaceHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.report.ReportFragment;
import com.sh3h.workmanagerjm.ui.handler.reuse.ReuseHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.split.SplitHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.stop.StopHandlerFragment;
import com.sh3h.workmanagerjm.ui.handler.verify.VerifyHandlerFragment;
import com.sh3h.workmanagerjm.ui.list.ListActivity;
import com.sh3h.workmanagerjm.ui.main.MainActivity;
import com.sh3h.workmanagerjm.ui.manager.ManagerActivity;
import com.sh3h.workmanagerjm.ui.multimedia.MultiMediaFragment;
import com.sh3h.workmanagerjm.ui.process.ProcessActivity;
import com.sh3h.workmanagerjm.ui.setting.SettingActivity;
import com.sh3h.workmanagerjm.ui.slide.SlideShowFragment;
import com.sh3h.workmanagerjm.ui.statistics.StatisticsListActivity;
import com.sh3h.workmanagerjm.ui.web.WebActivity;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 * 生命周期跟Activity一样
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ParentActivity parentActivity);

    void inject(MainActivity mainActivity);

    void inject(ListActivity listActivity);

    void inject(ManagerActivity activity);

    void inject(DispatchActivity dispatchActivity);

    void inject(StatisticsListActivity statisticsListActivity);

    void inject(WebActivity webActivity);

    void inject(SettingActivity settingActivity);

    void inject(ProcessActivity processActivity);

    void inject(ChargeBackActivity activity);

    void inject(SplitDetailFragment fragment);

    void inject(StopDetailFragment fragment);

    void inject(MultiMediaFragment fragment);

    void inject(SlideShowFragment fragment);

    void inject(InputMaterialHandleFragment fragment);

    void inject(ReplaceHandlerFragment fragment);

    void inject(SplitHandlerFragment fragment);

    void inject(ReinstallHandlerFragment fragment);

    void inject(StopHandlerFragment fragment);

    void inject(ReuseHandlerFragment fragment);

    void inject(NoticeHandlerFragment fragment);

    void inject(VerifyHandlerFragment fragment);

    void inject(MoveHandlerFragment fragment);

    void inject(CheckHandlerFragment fragment);

    void inject(InsideHandlerFragment fragment);

    void inject(InstallHandlerFragment fragment);

    void inject(HotHandlerFragment fragment);

    void inject(ReportFragment fragment);

}