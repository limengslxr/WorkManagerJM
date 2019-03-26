package com.sh3h.workmanagerjm.ui.dispatch;

import android.util.Log;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LiMeng on 2018/2/7.
 */

public class DispatchPresenter extends ParentPresenter<DispatchMvpView> {
    @Inject
    DispatchPresenter(DataManager dataManager) {
        super(dataManager);
    }

    void getStation() {
        mSubscription.add(mDataManager.getCurrentStation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("abc", e.toString());
                    }

                    @Override
                    public void onNext(List<DUWord> list) {
                        getMvpView().getStation(list);
                    }
                }));
    }

}
