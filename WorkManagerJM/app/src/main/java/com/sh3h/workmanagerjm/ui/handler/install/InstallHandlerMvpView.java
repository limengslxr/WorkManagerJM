package com.sh3h.workmanagerjm.ui.handler.install;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2018/1/9.
 */

interface InstallHandlerMvpView extends MvpView {
    void getWord(int parentId, List<DUWord> list);
}
