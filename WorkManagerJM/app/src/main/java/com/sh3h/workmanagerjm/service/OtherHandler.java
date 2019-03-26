package com.sh3h.workmanagerjm.service;


import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.workmanagerjm.MainApplication;
import com.squareup.otto.Bus;

class OtherHandler extends BaseHandler {
    OtherHandler(MainApplication mainApplication,
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
            default:
                break;
        }
        return true;
    }

}
