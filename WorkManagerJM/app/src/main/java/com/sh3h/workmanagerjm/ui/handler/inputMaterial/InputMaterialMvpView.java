package com.sh3h.workmanagerjm.ui.handler.inputMaterial;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.workmanagerjm.ui.base.MvpView;

import java.util.List;

/**
 * Created by LiMeng on 2017/6/7.
 * 输入材料
 */
interface InputMaterialMvpView extends MvpView{
    void getTypeResult(List<DUMaterial> materials);
    void getNameResult(List<DUMaterial> materials);
    void getStandardResult(List<DUMaterial> materials);
    void getUnitResult(List<DUMaterial> materials);
}
