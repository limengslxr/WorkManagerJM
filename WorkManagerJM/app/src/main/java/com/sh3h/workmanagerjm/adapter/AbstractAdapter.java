package com.sh3h.workmanagerjm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.workmanagerjm.R;

import java.util.List;

/**
 * Created by limeng on 2016/12/14.
 * 抽象
 */
public abstract class AbstractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private boolean showProgress;
    protected List<DUData> list;
    OnItemClickListener onItemClickListener;
    static final int TYPE_LOADING = -1;

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public List<DUData> getList() {
        return list;
    }

    public void setList(List<DUData> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (showProgress) {
            return list == null || list.size() <= 0 ? 0 : list.size() + 1;
        }
        return list == null || list.size() <= 0 ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < list.size()) {
            return super.getItemViewType(position);
        } else {
            return TYPE_LOADING;
        }
    }

    public List<DUData> getData() {
        return list;
    }

    /**
     * @param data       数据源
     * @param clickState 点的状态
     * @return 是否可响应点击事件
     */
    boolean checkState(Context context, DUData data, int clickState) {
        DUTask task = data.getDuTask();
        int state = task.getState();
        int hangUpState = task.getHangUpState();
        int delayState = task.getDelayState();
        switch (clickState) {
            case ConstantUtil.ClickType.TYPE_RECEIVE://接单
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        return handleHangUp(context, hangUpState);
                    case ConstantUtil.State.RECEIVEORDER:
                    case ConstantUtil.State.ONSPOT:
                    case ConstantUtil.State.HANDLE:
                        ApplicationsUtil.showMessage(context, R.string.toast_already_receive_task);
                        return false;
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_ARRIVE://到场
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                        return handleHangUp(context, hangUpState);
                    case ConstantUtil.State.ONSPOT:
                    case ConstantUtil.State.HANDLE:
                        ApplicationsUtil.showMessage(context, R.string.toast_already_arrive_task);
                        return false;
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_HANDLE://处理
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_arrive_task);
                        return false;
                    case ConstantUtil.State.ONSPOT:
                        return handleHangUp(context, hangUpState);
                    case ConstantUtil.State.HANDLE:
                        ApplicationsUtil.showMessage(context, R.string.toast_already_handle_task);
                        return false;
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_ASSIST://协助
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                    case ConstantUtil.State.ONSPOT:
                        //if (handle == null || handle.getAssist() == ConstantUtil.Assist.NO) {
                        return true;
//                        } else {
//                            ApplicationsUtil.showMessage(context, R.string.toast_already_assist_task);
//                            return false;
//                        }
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_BACK: //退单
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_arrive_task);
                        return false;
                    case ConstantUtil.State.ONSPOT:
                        return handleHangUp(context, hangUpState);
                    case ConstantUtil.State.BACK:
                        return true;
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_DELAY://延期
                //延期通过不通过都可以申请延期  延期通过按钮蓝色、延期失败按钮灰色
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                    case ConstantUtil.State.ONSPOT:
                        switch (delayState) {
                            case ConstantUtil.DelayState.NORMAL:
                            case ConstantUtil.DelayState.DELAY_SUCCESS:
                            case ConstantUtil.DelayState.DELAY_FAIL:
                                return true;
                            case ConstantUtil.DelayState.REPORT_DELAY:
                                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_inspect_report_delay);
                                return false;
                            default:
                                ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                                return false;
                        }
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_HANG_UP://挂起、恢复
                switch (state) {
                    case ConstantUtil.State.DISPACHORDER:
                        ApplicationsUtil.showMessage(context, R.string.toast_please_receive_task);
                        return false;
                    case ConstantUtil.State.RECEIVEORDER:
                    case ConstantUtil.State.ONSPOT:
                        switch (hangUpState) {
                            case ConstantUtil.HangUpState.NORMAL:
                                return true;
                            case ConstantUtil.HangUpState.WAIT_HANG_UP:
                                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_inspect_hang_up);
                                return false;
                            case ConstantUtil.HangUpState.HANG_UP:
                                return true;
                            case ConstantUtil.HangUpState.WAIT_RECOVERY:
                                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_inspect_recovery);
                                return false;
                            default:
                                ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                                return false;
                        }
                    default:
                        ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                        return false;
                }
            case ConstantUtil.ClickType.TYPE_PROCESS: //流程
                return true;
            case ConstantUtil.ClickType.TYPE_ITEM: //项点击
                return true;
            default:
                return false;
        }
    }

    private boolean handleHangUp(Context context, int hangUpState) {
        switch (hangUpState) {
            case ConstantUtil.HangUpState.NORMAL:
                return true;
            case ConstantUtil.HangUpState.WAIT_HANG_UP:
                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_inspect_hang_up);
                return false;
            case ConstantUtil.HangUpState.HANG_UP:
                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_hang_up);
                return false;
            case ConstantUtil.HangUpState.WAIT_RECOVERY:
                ApplicationsUtil.showMessage(context, R.string.toast_not_operate_inspect_recovery);
                return false;
            default:
                ApplicationsUtil.showMessage(context, R.string.toast_unknown_error);
                return false;
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;

        LoadingHolder(View view) {
            super(view);
            pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        }
    }

}
