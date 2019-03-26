package com.sh3h.workmanagerjm.myinterface;

/**
 * Created by limeng on 2016/9/27.
 */
public interface OnItemLongClickListener {
    /**
     * @param fileType 文件类型：图片、语音
     * @param position 下标
     */
    void onItemLongClick(int fileType, int position);
}
