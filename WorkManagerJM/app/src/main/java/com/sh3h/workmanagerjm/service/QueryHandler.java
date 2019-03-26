package com.sh3h.workmanagerjm.service;


import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailInfoEntity;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.event.UIBusEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

class QueryHandler extends BaseHandler {
    private static final String TAG = "QueryHandler";

    QueryHandler(MainApplication mainApplication,
                 DataManager dataManager,
                 ConfigHelper configHelper,
                 PreferencesHelper preferencesHelper,
                 EventPosterHelper eventPosterHelper,
                 Bus bus) {
        super(mainApplication, dataManager, configHelper, preferencesHelper, eventPosterHelper, bus);
    }

    @Override
    protected boolean process(SyncMessage syncMessage) {
        if (!super.process(syncMessage)) {
            return false;
        }

        switch (syncMessage.getOperate()) {
            case SyncConst.QUERY_TASK_WITH_CONDITION:
                search(syncMessage);
                break;
            default:
                break;
        }
        return true;
    }

    private void search(SyncMessage message) {
        SearchDetailInfoEntity infoEntity = new SearchDetailInfoEntity();
        infoEntity.setTaskId(message.getTaskId());
        infoEntity.setType(message.getType());
        infoEntity.setSubType(message.getSubType());
        infoEntity.setCardId(message.getCardId());
        infoEntity.setName(message.getCardName());
        infoEntity.setAddress(message.getAddress());
        infoEntity.setTelephone(message.getPhone());
        infoEntity.setSealNumber(message.getGangYinHao());
        infoEntity.setResolvePerson(message.getResolvePerson());
        infoEntity.setResolveTime(message.getResolveTime());
        infoEntity.setFuzzySearch(message.isFuzzySearch());
        mSubscription = mDataManager.searchWork(infoEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<DUData>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "----search onCompleted----");
                    }

                    @Override
                    public void onError(Throwable e) {
                        searchTaskEnd(false, null);
                    }

                    @Override
                    public void onNext(ArrayList<DUData> duDatas) {
                        searchTaskEnd(true, duDatas);
                    }
                });
    }

    private void searchTaskEnd(boolean result, List<DUData> duDatas) {
        UIBusEvent.SearchTask searchTask = new UIBusEvent.SearchTask();
        searchTask.setMessage(result ? R.string.toast_search_task_success :
                R.string.toast_search_task_fail);
        searchTask.setSuccess(result);
        searchTask.setList(duDatas);
        mEventPosterHelper.postEventSafely(searchTask);
    }

}
