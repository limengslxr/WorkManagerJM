package com.sh3h.workmanagerjm.ui.web;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.config.SystemConfig;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import javax.inject.Inject;


class WebPresenter extends ParentPresenter<WebMvpView> {
    private final ConfigHelper mConfigHelper;

    @Inject
    WebPresenter(DataManager dataManager, ConfigHelper configHelper) {
        super(dataManager);
        mConfigHelper = configHelper;
    }

    String getGrabUrl() {
        return mConfigHelper.getGrabUri();
    }

    String getBaseUrl() {
        SystemConfig systemConfig = mConfigHelper.getSystemConfig();
        return systemConfig.getBoolean(SystemConfig.PARAM_USING_RESERVED_ADDRESS, false) ?
                mConfigHelper.getReverseBaseUri() :
                mConfigHelper.getBaseUri();
    }

}
