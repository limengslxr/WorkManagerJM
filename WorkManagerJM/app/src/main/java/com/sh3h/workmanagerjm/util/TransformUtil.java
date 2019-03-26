package com.sh3h.workmanagerjm.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;

import java.io.File;

/**
 * Created by LiMeng on 2017/6/7.
 */
public class TransformUtil {

    public static int getTypeResourceId(@NonNull String entrance){
        int titleId;
        switch (entrance) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                titleId = R.string.title_order_bw_list;
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                titleId = R.string.title_meter_install_list;
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                titleId = R.string.title_call_pay_list;
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
                titleId = R.string.title_history;
                break;
            case ConstantUtil.TaskEntrance.REPORT:
                titleId = R.string.title_report;
                break;
            case ConstantUtil.TaskEntrance.SEARCH:
                titleId = R.string.title_task_search;
                break;
            case ConstantUtil.TaskEntrance.HOT:
                titleId = R.string.title_hot;
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                titleId = R.string.title_inside;
                break;
            case ConstantUtil.TaskEntrance.DISPATCH:
                titleId = R.string.title_task_dispatch;
                break;
            case ConstantUtil.TaskEntrance.VERIFY:
                titleId = R.string.title_task_verify;
                break;
            default:
                titleId = R.string.title_task_search;
                break;
        }

        return titleId;
    }

    public static int getTaskTypeResourceId(int type){
        int titleId;
        switch (type) {
            case ConstantUtil.WorkType.TASK_BIAOWU:
                titleId = R.string.title_order_bw_list;
                break;
            case ConstantUtil.WorkType.TASK_INSTALL_METER:
                titleId = R.string.title_meter_install_list;
                break;
            case ConstantUtil.WorkType.TASK_CALL_PAY:
                titleId = R.string.title_call_pay_list;
                break;
            case ConstantUtil.WorkType.TASK_REPORT:
                titleId = R.string.title_report;
                break;
            case ConstantUtil.WorkType.TASK_HOT:
                titleId = R.string.title_hot;
                break;
            case ConstantUtil.WorkType.TASK_INSIDE:
                titleId = R.string.title_inside;
                break;
            default:
                titleId = R.string.title_task_search;
                break;
        }

        return titleId;
    }

    public static int getSubTypeResourceId(int subType){
        int strId;
        switch (subType) {
            case ConstantUtil.WorkSubType.SPLIT_METER:
                strId = R.string.text_sub_type_split;
                break;
            case ConstantUtil.WorkSubType.REPLACE_METER:
                strId = R.string.text_sub_type_replace;
                break;
            case ConstantUtil.WorkSubType.INSTALL_METER:
                strId = R.string.text_sub_type_reinstall;
                break;
            case ConstantUtil.WorkSubType.STOP_METER:
                strId = R.string.text_sub_type_stop;
                break;
            case ConstantUtil.WorkSubType.MOVE_METER:
                strId = R.string.text_sub_type_move;
                break;
            case ConstantUtil.WorkSubType.CHECK_METER:
                strId = R.string.text_sub_type_check;
                break;
            case ConstantUtil.WorkSubType.REUSE:
                strId = R.string.text_sub_type_reuse;
                break;
            case ConstantUtil.WorkSubType.NOTICE:
                strId = R.string.text_sub_type_notice;
                break;
            case ConstantUtil.WorkSubType.VERIFY:
                strId = R.string.text_sub_type_verify;
                break;
            default:
                strId = R.string.text_work_null;
                break;
        }
        return strId;
    }

    public static int getApplyTypeResourceId(int applyType){
        int strId;
        switch (applyType) {
            case ConstantUtil.ApplyType.DEFAULT:
                strId = R.string.text_charge_back;
                break;
            case ConstantUtil.ApplyType.DELAY:
                strId = R.string.text_delay;
                break;
            case ConstantUtil.ApplyType.HANG_UP:
                strId = R.string.text_hang_up;
                break;
            case ConstantUtil.ApplyType.RECOVERY:
                strId = R.string.text_recovery;
                break;
            case ConstantUtil.ApplyType.ASSIST:
                strId = R.string.text_apply_help;
                break;
            case ConstantUtil.ApplyType.REPORT:
                strId = R.string.title_report;
                break;
            default:
                strId = R.string.text_work_null;
                break;
        }
        return strId;
    }

    public static String getTaskMultiType(Context context, DUTask task){
        if (task == null || context == null){
            return TextUtil.EMPTY;
        }

        switch (task.getType()) {
            case ConstantUtil.WorkType.TASK_BIAOWU:
                StringBuilder stringBuilder = new StringBuilder(context.getString(R.string.title_order_bw_list));
                stringBuilder.append(File.separator);
                switch (task.getSubType()){
                    case ConstantUtil.WorkSubType.SPLIT_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_split));
                        break;
                    case ConstantUtil.WorkSubType.REPLACE_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_replace));
                        break;
                    case ConstantUtil.WorkSubType.INSTALL_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_reinstall));
                        break;
                    case ConstantUtil.WorkSubType.STOP_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_stop));
                        break;
                    case ConstantUtil.WorkSubType.MOVE_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_move));
                        break;
                    case ConstantUtil.WorkSubType.CHECK_METER:
                        stringBuilder.append(context.getString(R.string.text_sub_type_check));
                        break;
                    case ConstantUtil.WorkSubType.REUSE:
                        stringBuilder.append(context.getString(R.string.text_sub_type_reuse));
                        break;
                    case ConstantUtil.WorkSubType.NOTICE:
                        stringBuilder.append(context.getString(R.string.text_sub_type_notice_simple));
                        break;
                    case ConstantUtil.WorkSubType.VERIFY:
                        stringBuilder.append(context.getString(R.string.text_sub_type_verify_simple));
                        break;
                    default:
                        break;
                }
                return stringBuilder.toString();
            case ConstantUtil.WorkType.TASK_INSTALL_METER:
                return context.getString(R.string.title_meter_install_list) ;
            case ConstantUtil.WorkType.TASK_CALL_PAY:
                return context.getString(R.string.title_call_pay_list) ;
            case ConstantUtil.WorkType.TASK_REPORT:
                return context.getString(R.string.title_report) ;
            case ConstantUtil.WorkType.TASK_HOT:
                return context.getString(R.string.title_hot) ;
            case ConstantUtil.WorkType.TASK_INSIDE:
                return context.getString(R.string.title_inside) ;
            default:
                return TextUtil.EMPTY;
        }
    }

}
