package com.sh3h.workmanagerjm.ui.process;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by BJB147 on 2017/3/28.
 */
class ProcessPresenter extends ParentPresenter<ProcessMvpView>{
    @Inject
    public ProcessPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
