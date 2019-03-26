package com.sh3h.workmanagerjm.ui.handler.report;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.workmanagerjm.ui.base.MvpView;

/**
 * Created by LiMeng on 2018/1/17.
 */

interface ReportMvpView extends MvpView{
    void getReportHistory(DUData data);
}
