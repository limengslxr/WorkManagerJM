package com.sh3h.workmanagerjm.ui.search;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by limeng on 2016/12/7.
 * 查询
 */

public class SearchPresenter extends ParentPresenter<SearchMvpView> {
    @Inject
    public SearchPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
