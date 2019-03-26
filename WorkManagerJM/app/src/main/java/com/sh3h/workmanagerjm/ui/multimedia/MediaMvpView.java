package com.sh3h.workmanagerjm.ui.multimedia;

import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * 多媒体
 * Created by limeng on 2016/9/18.
 */
interface MediaMvpView extends MvpView {

    void getMultiMedias(List<DUMedia> duMedias);

    void onShowMedia(DUMedia duMedia);

    void saveMediaError();

    void deleteMedia(int type,boolean result, int position);

    void saveVoiceFile(boolean result, DUMedia duMedia);

    void refreshSystemPhoto(DUMedia duMedia);
}
