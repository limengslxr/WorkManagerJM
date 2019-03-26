package com.sh3h.workmanagerjm.ui.chargeBack;

import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChargeBackPresenter extends ParentPresenter<ChargeBackMvpView> {
    private EventPosterHelper eventPosterHelper;

    @Inject
    public ChargeBackPresenter(DataManager dataManager, EventPosterHelper eventPosterHelper) {
        super(dataManager);
        this.eventPosterHelper = eventPosterHelper;
    }

    void report(HistoryCondition condition) {
        mSubscription.add(mDataManager.handleTask(condition)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUData>() {
                               @Override
                               public void onStart() {
                                   super.onStart();
                                   getMvpView().showProgress(R.string.toast_null);
                               }

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   getMvpView().handleError(R.string.toast_handle_fail);
                               }

                               @Override
                               public void onNext(DUData duData) {
                                   getMvpView().onReportNext(duData);
                                   DUHandle handle = duData.getHandle();
                                   UIBusEvent.HandleTask task = new UIBusEvent.HandleTask();
                                   task.setTaskId(handle.getTaskId());
                                   eventPosterHelper.postEventSafely(task);
                               }
                           }
                ));
    }

    void temporaryData(DUHandle handle) {
        mSubscription.add(mDataManager.temporaryData(handle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUData>() {
                               @Override
                               public void onStart() {
                                   super.onStart();
                                   getMvpView().showProgress(R.string.toast_null);
                               }

                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   getMvpView().handleError(R.string.toast_save_data_error);
                               }

                               @Override
                               public void onNext(DUData duData) {
                                   getMvpView().onReportNext(duData);

                                   DUHandle handle = duData.getHandle();
                                   if (handle.getReportType() == ConstantUtil.ReportType.SAVE) {
                                       eventPosterHelper.postEventSafely(
                                               new UIBusEvent.TemporaryResult());
                                   }
                               }
                           }
                ));
    }

}
