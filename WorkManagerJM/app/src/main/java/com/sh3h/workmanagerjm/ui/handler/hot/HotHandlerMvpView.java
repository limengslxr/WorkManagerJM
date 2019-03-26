package com.sh3h.workmanagerjm.ui.handler.hot;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2017/6/13.
 */
interface HotHandlerMvpView extends MvpView{
    void getWord(String group, List<DUWord> list);
}
