package com.sh3h.workmanagerjm.ui.chargeBack;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.workmanagerjm.ui.base.MvpView;

interface ChargeBackMvpView extends MvpView {

    void showProgress(int id);

    void handleError(int resourceId);

    void onReportNext(DUData duData);


}
