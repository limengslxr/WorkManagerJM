package com.sh3h.workmanagerjm.adapter;

/**
 * Created by limeng on 2016/12/7.
 * item点击事件
 */

public interface OnItemClickListener {

    /**
     * @param type 接单、到场、处理、退单、协助、item点击事件
     * @param i    位置下标
     */
    void onItemClick(int type, int i);
}
