package com.sh3h.workmanagerjm.ui.list;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by limeng on 2016/12/14.
 * 列表
 */

interface ListMvpView extends MvpView {

    void showProgress(int id);

    void hideProgress();

    //获取数据
    void getData(List<DUData> list, boolean controlSync);

    //获取数据结束
    void getDataComplete();

    //获取数据出错
    void getDataError(Throwable e);

    void handleError(int resourceId);

    void handleError(String error);

    void handleSuccess(DUData duData);

    void searchHandleError(Throwable e);

    void searchHandleEnd(DUData duData);

    void getAllPerson(String account, List<DUPerson> duPeople);

    void dispatchOver(int resourceId);

    void dispatchNext(String taskIdStr);

    void getAllStation(List<DUWord> list);

    void verifyError(int resourceId);

    void handleSuccess(int resourceId);

    void verifySingleOver(int resourceId);

    void verifySingleNext(String taskIdStr, int type);

    void verifyMultipleNext(String taskIdStr, int type);

    //获取数据结束
    void getTaskCount(Long number);

    void getTaskOver();

}
