package com.sh3h.dataprovider.condition;

import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.util.ConstantUtil;

import retrofit2.http.PUT;

/**
 * Created by limeng on 2016/10/20.
 * 历史纪录
 */

public class HistoryCondition {
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
    //执行哪种操作
    private int operate;
    //任务号
    private long taskId;
    //账号
    private String account;
    //关键字（搜索按钮搜索）
    private String key;
    //类型    2表务工单，5报装工单；6水表抽查；
    private int type;
    //处理情况
    private int handleType;
    //分页查询的起始位置
    private int offset;
    //分页查询的数量
    private int limit = ConstantUtil.GET_PAGE_DATA_LIMIT;
    //数据
    private DUHandle duHandle;

    public HistoryCondition() {
        operate = INVALID;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public DUHandle getDuHandle() {
        return duHandle;
    }

    public void setDuHandle(DUHandle duHandle) {
        this.duHandle = duHandle;
    }
}
