package com.sh3h.workmanagerjm.ui.slide;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import java.io.File;

import javax.inject.Inject;


class SlideShowPresenter extends ParentPresenter<SlideShowMvpView> {
    @Inject
    public SlideShowPresenter(DataManager dataManager) {
        super(dataManager);
    }

    File getImageFolderPath() {
        return mDataManager.getImageFolderPath();
    }
}
