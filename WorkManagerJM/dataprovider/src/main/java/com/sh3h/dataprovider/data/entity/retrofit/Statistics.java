package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2018/2/7.
 */

public class Statistics {
    private int type;// number 2.表务，3.催缴，5.报装，8.内部，9.热线
    private int subType;// number 1.拆表，2.换表，3.复装，4.停用，5.迁表，6.验表
    private int approveDelay;//延迟已审批数量
    private int noneDelay;//延期未审批数量
    private int approveAssist;//协助已审批数量
    private int noneAssist;//协助未审批数量
    private int approveReject;//退单已审批数量
    private int noneReject;//退单未审批数量
    private int approveHangUp;//挂起已审批数量
    private int noneHangUp;//挂起未审批数量
    private int approveRecover;//恢复已审批数量
    private int noneRecover;//恢复未审批数量

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

    public int getApproveDelay() {
        return approveDelay;
    }

    public void setApproveDelay(int approveDelay) {
        this.approveDelay = approveDelay;
    }

    public int getNoneDelay() {
        return noneDelay;
    }

    public void setNoneDelay(int noneDelay) {
        this.noneDelay = noneDelay;
    }

    public int getApproveAssist() {
        return approveAssist;
    }

    public void setApproveAssist(int approveAssist) {
        this.approveAssist = approveAssist;
    }

    public int getNoneAssist() {
        return noneAssist;
    }

    public void setNoneAssist(int noneAssist) {
        this.noneAssist = noneAssist;
    }

    public int getApproveReject() {
        return approveReject;
    }

    public void setApproveReject(int approveReject) {
        this.approveReject = approveReject;
    }

    public int getNoneReject() {
        return noneReject;
    }

    public void setNoneReject(int noneReject) {
        this.noneReject = noneReject;
    }

    public int getApproveHangUp() {
        return approveHangUp;
    }

    public void setApproveHangUp(int approveHangUp) {
        this.approveHangUp = approveHangUp;
    }

    public int getNoneHangUp() {
        return noneHangUp;
    }

    public void setNoneHangUp(int noneHangUp) {
        this.noneHangUp = noneHangUp;
    }

    public int getApproveRecover() {
        return approveRecover;
    }

    public void setApproveRecover(int approveRecover) {
        this.approveRecover = approveRecover;
    }

    public int getNoneRecover() {
        return noneRecover;
    }

    public void setNoneRecover(int noneRecover) {
        this.noneRecover = noneRecover;
    }
}
