package com.sh3h.workmanagerjm.ui.handler.notice;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2018/1/9.
 */

interface NoticeHandlerMvpView extends MvpView{
    void getResolveResult(List<DUWord> list);
}
