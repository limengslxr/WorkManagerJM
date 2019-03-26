package com.sh3h.dataprovider.data.remote;


import android.content.Context;

import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;


import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class Downloader {
    private static final int TIMEOUT = 10 * 1000;// 超时
    private final Context mContext;
    private final ConfigHelper mConfigHelper;

    @Inject
    public Downloader(@ApplicationContext Context context,
                      ConfigHelper configHelper) {
        mContext = context;
        mConfigHelper = configHelper;
    }
}
