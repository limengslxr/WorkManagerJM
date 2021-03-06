package com.sh3h.workmanagerjm.injection.component;

import android.content.Context;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.xml.XmlHelper;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.workmanagerjm.MainApplication;
import com.sh3h.workmanagerjm.injection.module.ApplicationModule;
import com.sh3h.workmanagerjm.service.SyncService;
import com.sh3h.workmanagerjm.util.PermissionsChecker;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 生命周期跟Application一样
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainApplication mainApplication);

    void inject(SyncService syncService);

    @ApplicationContext
    Context context();

    DataManager dataManager();

    PreferencesHelper preferencesHelper();

    ConfigHelper configHelper();

    Bus eventBus();

    XmlHelper xmlHelper();

    EventPosterHelper eventPosterHelper();

    PermissionsChecker permissionsChecher();
}