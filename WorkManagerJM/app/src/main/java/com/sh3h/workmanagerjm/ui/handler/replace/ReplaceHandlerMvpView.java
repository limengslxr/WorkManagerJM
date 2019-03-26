package com.sh3h.workmanagerjm.ui.handler.replace;

import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2018/1/8.
 */

interface ReplaceHandlerMvpView extends MvpView{
    void getWord(int parentId, List<DUWord> list);
}
