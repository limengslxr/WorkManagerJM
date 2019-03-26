package com.sh3h.dataprovider.data.local.xml;

import android.content.Context;

import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;


import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class XmlHelper {
    private final Context mContext;

    @Inject
    public XmlHelper(@ApplicationContext Context context,
                     ConfigHelper configHelper) {
        mContext = context;
    }
}
