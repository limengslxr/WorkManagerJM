package com.sh3h.dataprovider.condition;

/**
 * Created by limeng on 2016/10/23.
 * 词语信息
 */

public class WordCondition {
    public static final int GET_HANDLE_TYPE = 1;
    public static final int GET_HANDLE_CONTENT = 2;
    public static final int GET_HAPPEN_REASON = 3;
    public static final int GET_MEASURE_SOLUTION = 4;

    private int operate;
    private long parentId;

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
