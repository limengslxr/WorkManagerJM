package com.sh3h.workmanagerjm.ui.handler.report;

import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.TemporaryCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LiMeng on 2018/1/17.
 */

class ReportPresenter extends ParentPresenter<ReportMvpView> {
    private UserSession session;
    @Inject
    ReportPresenter(DataManager dataManager, PreferencesHelper helper) {
        super(dataManager);
        session = helper.getUserSession();
    }

    void getReportTemporary(){
        TemporaryCondition condition = new TemporaryCondition();
        condition.setOperate(TemporaryCondition.GET_REPORT_TEMPORARY);
        condition.setAccount(session.getAccount());
        mSubscription.add(mDataManager.getReportTemporary(condition)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DUData data = new DUData();
                        data.setTaskEntrance(ConstantUtil.TaskEntrance.REPORT);
                        data.setOperateType(ConstantUtil.ClickType.TYPE_HANDLE);
                        DUTask task = new DUTask();
                        long time = System.currentTimeMillis();
                        task.setAccount(session.getAccount());
                        task.setTaskId(time);
                        task.setType(ConstantUtil.WorkType.TASK_REPORT);
                        task.setSubType(0);
                        task.setState(ConstantUtil.State.HANDLE);
                        data.setDuTask(task);
                        data.setHandles(new ArrayList<>());

                        getMvpView().getReportHistory(data);
                    }

                    @Override
                    public void onNext(DUData duData) {
                        duData.setTaskEntrance(ConstantUtil.TaskEntrance.REPORT);
                        getMvpView().getReportHistory(duData);
                    }
                })
        );
    }

}
