package com.sh3h.localprovider.condition;


import com.sh3h.localprovider.entity.Task;

import java.util.List;

/**
 * Created by limeng on 2016/10/22.
 * 工单
 */

public class TaskConditionDb {
    //无效值
    public static final int INVALID = 0;
    //根据关键字分页获取任务
    public static final int GET_PAGE_WORK_BY_KEY = 1;
    //根据类型分页获取任务
    public static final int GET_PAGE_WORK_BY_TYPE = 2;
    //根据更新任务状态
    public static final int UPDATE_TASK_LIST_STATE = 3;
    //查找已催缴工单
    public static final int GET_ALREADY_CALL_PAY = 4;
    //查找未催缴工单
    public static final int GET_NO_CALL_PAY = 5;
    //根据表务子类型分页获取任务
    public static final int GET_PAGE_WORK_BY_SUB_TYPE = 6;
    //获取所有的任务
    public static final int GET_ALL_TASK = 20;

    //执行哪种操作
    private int operate;
    //关键字（搜索按钮搜索）
    private String key;
    //类型    2表务工单，5报装工单；6水表抽查；
    private int type;
    //子类型
    private int subType;
    //分页查询的起始位置
    private int offset;
    //分页查询的数量
    private int limit;
    //状态更新
    private List<Task> list;

    private String account;

    public TaskConditionDb() {
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

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
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

    public void clear() {
        operate = INVALID;
    }

    public String getAccount() {
        return account;
    }

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
