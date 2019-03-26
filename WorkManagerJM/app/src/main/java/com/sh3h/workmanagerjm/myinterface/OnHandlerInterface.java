package com.sh3h.workmanagerjm.myinterface;


import com.sh3h.dataprovider.data.entity.ui.DUMedia;

import java.util.ArrayList;

public interface OnHandlerInterface {
    void OnShowPicDetail(int i, ArrayList<DUMedia> pictures);

    void hideSlideShowFragment();
}
