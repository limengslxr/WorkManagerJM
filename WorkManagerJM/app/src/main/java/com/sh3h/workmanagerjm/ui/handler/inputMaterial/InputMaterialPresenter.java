package com.sh3h.workmanagerjm.ui.handler.inputMaterial;

import android.util.Log;

import com.sh3h.dataprovider.condition.MaterialCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by LiMeng on 2017/6/7.
 */
class InputMaterialPresenter extends ParentPresenter<InputMaterialMvpView>{
    @Inject
    InputMaterialPresenter(DataManager dataManager) {
        super(dataManager);
    }

    void getMaterial(DUMaterial material, int operate){
        MaterialCondition condition = new MaterialCondition();
        condition.setOperate(operate);
        condition.setMaterial(material);
        mSubscription.add(mDataManager.getMaterial(condition)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<DUMaterial>>() {
                @Override
                public void onCompleted() {
                    Log.i("abc", "onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("abc", "onError");
                }

                @Override
                public void onNext(List<DUMaterial> materials) {
                    switch (condition.getOperate()){
                        case MaterialCondition.GET_TYPE:
                            getMvpView().getTypeResult(materials);
                            break;
                        case MaterialCondition.GET_NAME:
                            getMvpView().getNameResult(materials);
                            break;
                        case MaterialCondition.GET_STANDARD:
                            getMvpView().getStandardResult(materials);
                            break;
                        case MaterialCondition.GET_UNIT:
                            getMvpView().getUnitResult(materials);
                            break;
                        default:
                            break;
                    }
                    Log.i("abc", "onNext");
                }
            }));
    }

}
