package com.sh3h.workmanagerjm.service;


public class SyncConst {
    public static final int NONE = 0;
    // 下载任务列表
    public static final int DOWNLOAD_ALL_TASK = 1;
    //查询
    public static final int QUERY_TASK_WITH_CONDITION = 2;
    //同步所有数据
    public static final int UPLOAD_ALL_TASK = 3;
    //上传一条数据
    public static final int UPLOAD_ONE_WORK = 4;
    //上传一条工单的照片文件
    public static final int UPLOAD_ONE_MEDIA = 5;
    //下载多媒体
    public static final int DOWNLOAD_MEDIAS = 6;
    //下载标卡信息
    public static final int DOWNLOAD_METER_CARD = 7;
    //下载派遣数据
    public static final int DOWNLOAD_DISPATCH = 8;
    //下载统计数据
    public static final int DOWNLOAD_STATISTICS = 9;
    //下载审核数据
    public static final int DOWNLOAD_VERIFY = 10;


    public static final String SYNC_OPERATE = "syncOperate";
    public static final String TASK_ID = "taskId";
    public static final String TASK_TYPE = "taskType";
    public static final String TASK_SUB_TYPE = "taskSubType";
    public static final String CARD_ID = "cardId";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String GANG_YIN_HAO = "gangYinHao";
    public static final String RESOLVE_PERSON = "resolvePerson";
    public static final String RESOLVE_TIME = "resolveTime";
    public static final String FUZZY_SEARCH = "fuzzySearch";
    public static final String FILE_URL = "fileUrl";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_TYPE = "fileType";
    public static final String STATION = "station";
    public static final String VOLUME = "volume";
    public static final String BEGIN_TIME = "beginTime";
    public static final String END_TIME = "endTime";
}
