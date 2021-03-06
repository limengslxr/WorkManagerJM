package com.sh3h.workmanagerjm.ui.handler.move;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LiMeng on 2018/1/9.
 */

public class MoveHandlerPresenter extends ParentPresenter<MoveHandlerMvpView> {

    @Inject
    MoveHandlerPresenter(DataManager dataManager) {
        super(dataManager);
    }

    void getWord(int parentId, String group){
        mSubscription.add(mDataManager.getWord(parentId, group)
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
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().getWord(parentId, duWords);
                    }
                }));
    }

}
