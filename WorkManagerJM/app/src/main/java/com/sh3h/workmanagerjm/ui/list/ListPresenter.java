package com.sh3h.workmanagerjm.ui.list;

import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUApplyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limeng on 2016/12/14.
 * 列表
 */

class ListPresenter extends ParentPresenter<ListMvpView> {

    @Inject
    ListPresenter(DataManager dataManager) {
        super(dataManager);
    }

    void getTaskList(TaskCondition condition, boolean controlSync) {
        mSubscription.add(mDataManager.getTaskList(condition)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUData>>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().getDataComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getDataError(e);
                    }

                    @Override
                    public void onNext(List<DUData> duTasks) {
                        getMvpView().getData(duTasks, controlSync);
                    }
                }));
    }

    void getHistoryList(HistoryCondition condition, boolean controlSync) {
        mSubscription.add(mDataManager.getHistoryList(condition)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUData>>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().getDataComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getDataError(e);
                    }

                    @Override
                    public void onNext(List<DUData> duTasks) {
                        getMvpView().getData(duTasks, controlSync);
                    }
                }));
    }

    void handle(HistoryCondition condition) {
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
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        DUHandle handle = condition.getDuHandle();
                        switch (handle.getReportType()) {
                            case ConstantUtil.ReportType.Handle:
                                switch (handle.getState()) {
                                    case ConstantUtil.State.RECEIVEORDER:
                                        getMvpView().handleError(R.string.toast_receive_fail);
                                        break;
                                    case ConstantUtil.State.ONSPOT:
                                        getMvpView().handleError(R.string.toast_arrive_fail);
                                        break;
                                    case ConstantUtil.State.BACK:
                                        getMvpView().handleError(R.string.toast_back_fail);
                                        break;
                                    default:
                                        getMvpView().handleError(R.string.toast_unknown_error);
                                        break;
                                }
                                break;
                            case ConstantUtil.ReportType.Assist:
                                getMvpView().handleError(e.getMessage());
                                break;
                            case ConstantUtil.ReportType.Apply:
                                DUApplyHandle applyHandle = handle.toDUApplyHandle();
                                switch (applyHandle.getApplyType()) {
                                    case ConstantUtil.ApplyType.DELAY:
                                        getMvpView().handleError(R.string.toast_delay_fail);
                                        break;
                                    case ConstantUtil.ApplyType.HANG_UP:
                                        getMvpView().handleError(R.string.toast_hang_up_fail);
                                        break;
                                    case ConstantUtil.ApplyType.RECOVERY:
                                        getMvpView().handleError(R.string.toast_recovery_fail);
                                        break;
                                    default:
                                        getMvpView().handleError(R.string.toast_unknown_error);
                                        break;
                                }
                                break;
                            default:
                                getMvpView().handleError(R.string.toast_unknown_error);
                                break;
                        }
                    }

                    @Override
                    public void onNext(DUData duData) {
                        getMvpView().handleSuccess(duData);
                    }
                }));
    }

    void searchHandle(DUData data) {
        mSubscription.add(mDataManager.searchHandle(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUData>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().showProgress(R.string.dialog_search_now);
                    }

                    @Override
                    public void onCompleted() {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().searchHandleError(e);
                    }

                    @Override
                    public void onNext(DUData data) {
                        getMvpView().searchHandleEnd(data);
                    }
                }));
    }

    void getAllPerson() {
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
                        getMvpView().getAllPerson(mDataManager.getAccount(), duPeople);
                    }
                }));
    }

    void dispatch(DUDispatchHandle handle) {
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
                        getMvpView().dispatchOver(R.string.toast_dispatch_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dispatchOver(R.string.toast_dispatch_error);
                    }

                    @Override
                    public void onNext(String taskIds) {
                        getMvpView().dispatchNext(taskIds);
                    }
                }));
    }

    void getAllStation() {
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

    void transformCancel(DUTransformCancelHandle handle) {
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
                        int operateType = handle.getOperateType();
                        getMvpView().dispatchOver(operateType == ConstantUtil.DispatchOperate.TRANSFORM
                                ? R.string.toast_transform_success : R.string.toast_cancel_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        int operateType = handle.getOperateType();
                        getMvpView().dispatchOver(operateType == ConstantUtil.DispatchOperate.TRANSFORM
                                ? R.string.toast_transform_error : R.string.toast_cancel_error);
                    }

                    @Override
                    public void onNext(String taskIds) {
                        getMvpView().dispatchNext(taskIds);
                    }
                }));
    }

    void verifyTask(List<DUVerifyHandle> handles, int applyType) {
        mSubscription.add(Observable.from(handles)
                .concatMap(mDataManager::verifyTask)
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
                        getMvpView().handleSuccess(R.string.toast_verify_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().verifyError(R.string.toast_verify_error);
                    }

                    @Override
                    public void onNext(DUVerifyHandle handle) {
                        getMvpView().verifyMultipleNext(handle.getTaskIds(), applyType);
                    }
                }));
    }

    void verifySingleTask(DUVerifyHandle handle) {
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
                        getMvpView().verifySingleOver(R.string.toast_verify_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().verifySingleOver(R.string.toast_verify_error);
                    }

                    @Override
                    public void onNext(DUVerifyHandle duVerifyHandle) {
                        getMvpView().verifySingleNext(duVerifyHandle.getTaskIds(), duVerifyHandle.getApplyType());
                    }
                }));
    }

    void cancelVerify(List<DUTransformCancelHandle> handles, int applyType) {
        mSubscription.add(Observable.from(handles)
                .concatMap(mDataManager::transformCancel)
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
                        getMvpView().handleSuccess(R.string.toast_verify_success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().verifyError(R.string.toast_verify_error);
                    }

                    @Override
                    public void onNext(String taskIds) {
                        getMvpView().verifyMultipleNext(taskIds, applyType);
                    }
                }));
    }

    void getTaskCount(TaskCondition condition) {
        mSubscription.add(mDataManager.getTaskCount(condition)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().getTaskOver();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getTaskOver();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        getMvpView().getTaskCount(aLong);
                    }
                }));
    }

}
