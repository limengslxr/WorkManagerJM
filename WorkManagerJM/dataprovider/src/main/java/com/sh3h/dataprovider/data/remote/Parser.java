package com.sh3h.dataprovider.data.remote;


import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.db.DbHelper;
import com.sh3h.dataprovider.data.local.xml.XmlHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.FileUtil;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Parser {
    private static final String TAG = "Parser";

    private final DbHelper mDbHelper;
    private final ConfigHelper mConfigHelper;
    private final EventPosterHelper mEventPosterHelper;
    private final XmlHelper mXmlHelper;

    @Inject
    public Parser(DbHelper dbHelper, ConfigHelper configHelper,
                  EventPosterHelper eventPosterHelper, XmlHelper xmlHelper) {
        mDbHelper = dbHelper;
        mConfigHelper = configHelper;
        mEventPosterHelper = eventPosterHelper;
        mXmlHelper = xmlHelper;
    }

    /**
     * @param zipFile
     * @param destFolder
     */
    private void deleteFiles(File zipFile, File destFolder) {
        try {
            FileUtil.deleteFile(zipFile);
            FileUtil.deleteFile(destFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
