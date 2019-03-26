package com.sh3h.localprovider.condition;

import com.sh3h.localprovider.entity.Material;

/**
 * Created by LiMeng on 2017/6/8.
 */

public class MaterialConditionDb {
    public static final int GET_TYPE = 1;
    public static final int GET_NAME = 2;
    public static final int GET_STANDARD = 3;
    public static final int GET_UNIT = 4;
    public static final int GET_PRICE = 5;

    private int operate;
    private Material material;

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
