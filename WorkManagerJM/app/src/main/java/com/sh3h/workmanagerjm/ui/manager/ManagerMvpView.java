package com.sh3h.workmanagerjm.ui.manager;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by limeng on 2016/12/14.
 * 具体操作
 */

interface ManagerMvpView extends MvpView {
    void showProgress(int id);

    void handleError(int resourceId);

    void handleError(String error);

    void onReportNext(DUData duData);

    void getAllPerson(String account, List<DUPerson> duPeople);

    void dispatchError(int resourceId);

    void dispatchSuccess(int resourceId);

    void getAllStation(List<DUWord> list);

    void verifyError(int resourceId);

    void verifySuccess(int resourceId);

}
