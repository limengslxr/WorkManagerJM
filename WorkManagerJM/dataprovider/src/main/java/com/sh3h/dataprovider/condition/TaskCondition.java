package com.sh3h.dataprovider.condition;


import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.util.ConstantUtil;

import java.util.List;

/**
 * Created by limeng on 2016/10/22.
 * 工单
 */

public class TaskCondition {
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

    //执行哪种操作
    private int operate;
    //账号
    private String account;
    //关键字（搜索按钮搜索）
    private String key;
    //类型    2表务工单，5报装工单；6水表抽查；
    private int type;
    //子类型
    private int subType;
    //分页查询的起始位置
    private int offset;
    //状态更新
    private List<MessageUpdate> list;
    //分页查询的数量
    private int limit = ConstantUtil.GET_PAGE_DATA_LIMIT;

    public TaskCondition() {
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

    public List<MessageUpdate> getList() {
        return list;
    }

    public void setList(List<MessageUpdate> list) {
        this.list = list;
    }

    public void clear() {
        operate = INVALID;
    }

}
