package com.sh3h.workmanagerjm.injection.module;

import android.app.Activity;
import android.content.Context;

import com.sh3h.dataprovider.data.model.Ribot;
import com.sh3h.workmanagerjm.injection.annotation.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Ribot providerRibot() {
        return new Ribot();
    }

}
