package com.sh3h.localprovider.condition;

import com.sh3h.localprovider.entity.History;

/**
 * Created by limeng on 2016/10/20.
 * 历史纪录
 */

public class HistoryConditionDb {
    //无效值
    public static final int INVALID = 0;
    //根据关键字分页获取历史
    public static final int GET_PAGE_WORK_BY_KEY = 1;
    //分页获取任务
    public static final int GET_PAGE_WORK = 2;
    //根据类型分页获取任务
    public static final int GET_PAGE_WORK_BY_TYPE = 3;
    //查询问题上报的历史记录
    public static final int GET_REPORT_HISTORY = 4;
    //根据任务号获取
    public static final int GET_BY_TASK_ID = 20;
    //根据taskId查询tasks
    public static final int GET_TASKS_BY_TASK_ID = 21;
    //查询未上传的taskId
    public static final int GET_UOT_UPLOAD_TASK_ID = 22;
    //根据taskId查询tasks,设置为正在上传
    public static final int GET_TASKS_BY_TASK_ID_SET_UPLOADING =  23;
    //查找全部
    public static final int GET_ALL_HISTORY = 24;

    //上报未上传的文本，并修改上传标志
    public static final int GET_NOT_UPLOAD_UPDATE_FALG = 40;

    //执行哪种操作
    private int operate;
    //关键字（搜索按钮搜索）
    private String key;
    //类型    2表务工单，5报装工单；6水表抽查；
    private int type;
    //处理情况
    private int handleType;
    //分页查询的起始位置
    private int offset;
    //分页查询的数量
    private int limit;
    //数据
    private History history;
    private long taskId;
    private int id;
    private int uploadFlag;
    private String account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public HistoryConditionDb() {
        operate = INVALID;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHandleType() {
        return handleType;
    }

    public void setHandleType(int handleType) {
        this.handleType = handleType;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
