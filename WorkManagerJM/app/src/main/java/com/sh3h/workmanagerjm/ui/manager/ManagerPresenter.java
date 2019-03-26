package com.sh3h.workmanagerjm.ui.manager;


import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;
import com.sh3h.workmanagerjm.util.NumberUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limeng on 2016/12/14.
 * 具体操作
 */
class ManagerPresenter extends ParentPresenter<ManagerMvpView> {
    private EventPosterHelper eventPosterHelper;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    ManagerPresenter(DataManager dataManager, EventPosterHelper eventPosterHelper, PreferencesHelper mPreferencesHelper) {
        super(dataManager);
        this.eventPosterHelper = eventPosterHelper;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    String getAccount() {
        return TextUtil.isNullOrEmpty(mPreferencesHelper.getUserSession().getAccount()) ? "289"
                : mPreferencesHelper.getUserSession().getAccount();
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
                                   DUHandle handle = condition.getDuHandle();
                                   switch (handle.getReportType()) {
                                       case ConstantUtil.ReportType.Handle:
                                           getMvpView().handleError(R.string.toast_handle_fail);
                                           break;
                                       case ConstantUtil.ReportType.Assist:
                                           getMvpView().handleError(e.toString());
                                           break;
                                       case ConstantUtil.ReportType.Report:
                                           getMvpView().handleError(R.string.toast_report_error);
                                           break;
                                       default:
                                           getMvpView().handleError(R.string.toast_unknown_error);
                                           break;
                                   }
                               }

                               @Override
                               public void onNext(DUData duData) {
                                   getMvpView().onReportNext(duData);
                                   DUHandle handle = duData.getHandle();
                                   if (handle.getReportType() == ConstantUtil.ReportType.SAVE){
                                       return;
                                   }

                                   UIBusEvent.HandleTask task = new UIBusEvent.HandleTask();
                                   task.setTaskId(handle.getTaskId());
                                   eventPosterHelper.postEventSafely(
                                           handle.getReportType() == ConstantUtil.ReportType.Assist ?
                                                   new UIBusEvent.ApplyAssist(duData.getHandle()) : task);
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
                                   if (handle.getReportType() == ConstantUtil.ReportType.SAVE){
                                       eventPosterHelper.postEventSafely(
                                               new UIBusEvent.TemporaryResult());
                                   }
                               }
                           }
                ));
    }

    void getAllPerson(){
        mSubscription.add(mDataManager.getAllPerson()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUPerson>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DUPerson> duPeople) {
                        getMvpView().getAllPerson(getAccount(), duPeople);
                    }
                }));
    }

    void dispatch(DUDispatchHandle handle){
        mSubscription.add(mDataManager.dispatch(handle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().showProgress(R.string.toast_report);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dispatchError(R.string.toast_dispatch_error);
                    }

                    @Override
                    public void onNext(String taskIds) {
                        if (NumberUtil.isNumber(taskIds)){
                            getMvpView().dispatchSuccess(R.string.toast_dispatch_success);
                            eventPosterHelper.postEventSafely(
                                    new UIBusEvent.SingleDispatch(Long.parseLong(taskIds)));
                        } else {
                            getMvpView().dispatchSuccess(R.string.toast_null);
                        }
                    }
                }));
    }

    void getAllStation(){
        mSubscription.add(mDataManager.getOneGroup(ConstantUtil.Group.STATION)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DUWord> list) {
                        getMvpView().getAllStation(list);
                    }
                }));
    }

    void transformCancel(DUTransformCancelHandle handle){
        mSubscription.add(mDataManager.transformCancel(handle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().showProgress(R.string.toast_report);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        int operateType = handle.getOperateType();
                        getMvpView().dispatchError(operateType == ConstantUtil.DispatchOperate.TRANSFORM
                                ? R.string.toast_transform_error : R.string.toast_cancel_error);
                    }

                    @Override
                    public void onNext(String taskIds) {
                        int operateType = handle.getOperateType();
                        if (NumberUtil.isNumber(taskIds)){
                            getMvpView().dispatchSuccess(operateType == ConstantUtil.DispatchOperate.TRANSFORM
                                    ? R.string.toast_transform_success : R.string.toast_cancel_success);
                            eventPosterHelper.postEventSafely(
                                    new UIBusEvent.SingleDispatch(Long.parseLong(taskIds)));
                        } else {
                            getMvpView().dispatchSuccess(R.string.toast_null);
                        }
                    }
                }));
    }

    void verify(DUVerifyHandle handle){
        mSubscription.add(mDataManager.verifyTask(handle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUVerifyHandle>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().showProgress(R.string.toast_report);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().verifyError(R.string.toast_verify_error);
                    }

                    @Override
                    public void onNext(DUVerifyHandle duVerifyHandle) {
                        String taskIds = duVerifyHandle.getTaskIds();
                        if (NumberUtil.isNumber(taskIds)){
                            getMvpView().verifySuccess(R.string.toast_verify_success);
                            eventPosterHelper.postEventSafely(
                                    new UIBusEvent.SingleVerify(Long.parseLong(taskIds),
                                            duVerifyHandle.getApplyType()));
                        } else {
                            getMvpView().verifySuccess(R.string.toast_null);
                        }
                    }
                }));
    }

}
