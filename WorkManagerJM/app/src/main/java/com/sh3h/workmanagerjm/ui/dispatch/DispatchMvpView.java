package com.sh3h.workmanagerjm.ui.dispatch;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2018/2/7.
 */

public interface DispatchMvpView extends MvpView {
    void getStation(List<DUWord> list);
}
