package com.sh3h.dataprovider.condition;

import com.sh3h.dataprovider.data.entity.ui.DUMaterial;

/**
 * Created by LiMeng on 2017/6/8.
 */

public class MaterialCondition {
    public static final int GET_TYPE = 1;
    public static final int GET_NAME = 2;
    public static final int GET_STANDARD = 3;
    public static final int GET_UNIT = 4;
    public static final int GET_PRICE = 5;

    private int operate;
    private DUMaterial material;

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public DUMaterial getMaterial() {
        return material;
    }

    public void setMaterial(DUMaterial material) {
        this.material = material;
    }
}
