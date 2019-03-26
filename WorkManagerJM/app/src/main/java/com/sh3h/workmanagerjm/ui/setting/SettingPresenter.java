package com.sh3h.workmanagerjm.ui.setting;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SettingPresenter extends ParentPresenter<SettingMvpView> {

    private static final String TAG = "SettingPresenter";

    @Inject
    public SettingPresenter(DataManager dataManager) {
        super(dataManager);
    }

    String getBaseUri() {
        return mDataManager.getBaseUri();
    }

    String getReverseBaseUri() {
        return mDataManager.getReverseBaseUri();
    }

    boolean isUsingReservedAddress() {
        return mDataManager.isUsingReservedAddress();
    }

    void saveNetWorkSetting(String baseUri, String reservedBaseUri, boolean isUsingReservedAddress) {
        LogUtil.i(TAG, "---saveNetWork---");
        if (TextUtil.isNullOrEmpty(baseUri)
                || TextUtil.isNullOrEmpty(reservedBaseUri)) {
            getMvpView().showMessage("param is invalid");
            return;
        }

        mSubscription.add(mDataManager.saveNetWorkSetting(baseUri, reservedBaseUri,
                isUsingReservedAddress)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "---saveNetWork onCompleted---");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "---saveNetWork onError---" + e.getMessage());
                        getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "---saveNetWork onNext---");
                        if (aBoolean) {
                            getMvpView().showMessage(R.string.text_saving_success);
                        } else {
                            getMvpView().showMessage(R.string.text_saving_failure);
                        }
                    }
                }));
    }

   /* public void getPicQuality() {
        mSubscription.add(mDataManager.getPicQuality().
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getMvpView().updatePicQuality(aBoolean);
                    }
                }));
    }*/
}
