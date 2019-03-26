package com.sh3h.workmanagerjm.ui.handler.hot;

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
 * Created by LiMeng on 2017/6/13.
 */
class HotHandlerPresenter extends ParentPresenter<HotHandlerMvpView>{
    private static final String TAG = "HotPresenter";

    @Inject
    HotHandlerPresenter(DataManager dataManager) {
        super(dataManager);
    }

    void getWord(long parentId, String group){
        mSubscription.add(mDataManager.getWord(parentId, group)
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
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().getWord(group, duWords);
                    }
                }));
    }

}
