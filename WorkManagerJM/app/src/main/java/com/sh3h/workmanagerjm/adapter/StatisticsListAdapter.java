package com.sh3h.workmanagerjm.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.retrofit.Statistics;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;

import java.io.File;
import java.util.List;

/**
 * Created by LiMeng on 2018/2/8.
 * 統計
 */
public class StatisticsListAdapter extends RecyclerView.Adapter<StatisticsListAdapter.StatisticsHolder> {

    private List<StatisticsResult> list;

    public void setList(List<StatisticsResult> list) {
        this.list = list;
    }

    public List<StatisticsResult> getList() {
        return list;
    }

    @Override
    public StatisticsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistics_list,
                parent, false);
        return new StatisticsListAdapter.StatisticsHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(StatisticsHolder holder, int position) {
        StatisticsResult statistics = list.get(position);
        holder.tvName.setText(statistics.getName());
        List<Statistics> data = statistics.getInfo();
        if (data != null){
            for (Statistics info : data){
                switch (info.getType()){
                    case ConstantUtil.WorkType.TASK_BIAOWU:
                        switch (info.getSubType()){
                            case ConstantUtil.WorkSubType.SPLIT_METER:
                                holder.tvSplitDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvSplitHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvSplitRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvSplitReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvSplitAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.REPLACE_METER:
                                holder.tvReplaceDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvReplaceHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvReplaceRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvReplaceReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvReplaceAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.INSTALL_METER:
                                holder.tvReinstallDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvReinstallHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvReinstallRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvReinstallReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvReinstallAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.STOP_METER:
                                holder.tvStopDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvStopHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvStopRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvStopReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvStopAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.MOVE_METER:
                                holder.tvMoveDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvMoveHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvMoveRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvMoveReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvMoveAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.CHECK_METER:
                                holder.tvCheckDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvCheckHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvCheckRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvCheckReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvCheckAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.REUSE:
                                holder.tvReuseDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvReuseHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvReuseRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvReuseReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvReuseAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.NOTICE:
                                holder.tvNoticeDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvNoticeHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvNoticeRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvNoticeReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvNoticeAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            case ConstantUtil.WorkSubType.VERIFY:
                                holder.tvVerifyDelay.setText(info.getApproveDelay()
                                        + File.separator + info.getNoneDelay());
                                holder.tvVerifyHangUp.setText(info.getApproveHangUp()
                                        + File.separator + info.getNoneHangUp());
                                holder.tvVerifyRecovery.setText(info.getApproveRecover()
                                        + File.separator + info.getNoneRecover());
                                holder.tvVerifyReject.setText(info.getApproveReject()
                                        + File.separator + info.getNoneReject());
                                holder.tvVerifyAssist.setText(info.getApproveAssist()
                                        + File.separator + info.getNoneAssist());
                                break;
                            default:
                                break;
                        }
                        break;
                    case ConstantUtil.WorkType.TASK_CALL_PAY:
                        holder.tvCallPayDelay.setText(info.getApproveDelay()
                                + File.separator + info.getNoneDelay());
                        holder.tvCallPayHangUp.setText(info.getApproveHangUp()
                                + File.separator + info.getNoneHangUp());
                        holder.tvCallPayRecovery.setText(info.getApproveRecover()
                                + File.separator + info.getNoneRecover());
                        holder.tvCallPayReject.setText(info.getApproveReject()
                                + File.separator + info.getNoneReject());
                        holder.tvCallPayAssist.setText(info.getApproveAssist()
                                + File.separator + info.getNoneAssist());
                        break;
                    case ConstantUtil.WorkType.TASK_INSTALL_METER:
                        holder.tvInstallDelay.setText(info.getApproveDelay()
                                + File.separator + info.getNoneDelay());
                        holder.tvInstallHangUp.setText(info.getApproveHangUp()
                                + File.separator + info.getNoneHangUp());
                        holder.tvInstallRecovery.setText(info.getApproveRecover()
                                + File.separator + info.getNoneRecover());
                        holder.tvInstallReject.setText(info.getApproveReject()
                                + File.separator + info.getNoneReject());
                        holder.tvInstallAssist.setText(info.getApproveAssist()
                                + File.separator + info.getNoneAssist());
                        break;
                    case ConstantUtil.WorkType.TASK_INSIDE:
                        holder.tvInsideDelay.setText(info.getApproveDelay()
                                + File.separator + info.getNoneDelay());
                        holder.tvInsideHangUp.setText(info.getApproveHangUp()
                                + File.separator + info.getNoneHangUp());
                        holder.tvInsideRecovery.setText(info.getApproveRecover()
                                + File.separator + info.getNoneRecover());
                        holder.tvInsideReject.setText(info.getApproveReject()
                                + File.separator + info.getNoneReject());
                        holder.tvInsideAssist.setText(info.getApproveAssist()
                                + File.separator + info.getNoneAssist());
                        break;
                    case ConstantUtil.WorkType.TASK_HOT:
                        holder.tvHotDelay.setText(info.getApproveDelay()
                                + File.separator + info.getNoneDelay());
                        holder.tvHotHangUp.setText(info.getApproveHangUp()
                                + File.separator + info.getNoneHangUp());
                        holder.tvHotRecovery.setText(info.getApproveRecover()
                                + File.separator + info.getNoneRecover());
                        holder.tvHotReject.setText(info.getApproveReject()
                                + File.separator + info.getNoneReject());
                        holder.tvHotAssist.setText(info.getApproveAssist()
                                + File.separator + info.getNoneAssist());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class StatisticsHolder extends RecyclerView.ViewHolder {
        private TextView tvSplitDelay, tvSplitHangUp, tvSplitRecovery, tvSplitReject, tvSplitAssist;
        private TextView tvReplaceDelay, tvReplaceHangUp, tvReplaceRecovery, tvReplaceReject, tvReplaceAssist;
        private TextView tvReinstallDelay, tvReinstallHangUp, tvReinstallRecovery, tvReinstallReject, tvReinstallAssist;
        private TextView tvStopDelay, tvStopHangUp, tvStopRecovery, tvStopReject, tvStopAssist;
        private TextView tvMoveDelay, tvMoveHangUp, tvMoveRecovery, tvMoveReject, tvMoveAssist;
        private TextView tvCheckDelay, tvCheckHangUp, tvCheckRecovery, tvCheckReject, tvCheckAssist;
        private TextView tvReuseDelay, tvReuseHangUp, tvReuseRecovery, tvReuseReject, tvReuseAssist;
        private TextView tvNoticeDelay, tvNoticeHangUp, tvNoticeRecovery, tvNoticeReject, tvNoticeAssist;
        private TextView tvVerifyDelay, tvVerifyHangUp, tvVerifyRecovery, tvVerifyReject, tvVerifyAssist;
        private TextView tvCallPayDelay, tvCallPayHangUp, tvCallPayRecovery, tvCallPayReject, tvCallPayAssist;
        private TextView tvInstallDelay, tvInstallHangUp, tvInstallRecovery, tvInstallReject, tvInstallAssist;
        private TextView tvInsideDelay, tvInsideHangUp, tvInsideRecovery, tvInsideReject, tvInsideAssist;
        private TextView tvHotDelay, tvHotHangUp, tvHotRecovery, tvHotReject, tvHotAssist;
        private TextView tvName;

        StatisticsHolder(View view) {
            super(view);
            tvSplitDelay = (TextView) view.findViewById(R.id.tv_item_split_delay);
            tvSplitHangUp = (TextView) view.findViewById(R.id.tv_item_split_hang_up);
            tvSplitRecovery = (TextView) view.findViewById(R.id.tv_item_split_recovery);
            tvSplitReject = (TextView) view.findViewById(R.id.tv_item_split_reject);
            tvSplitAssist = (TextView) view.findViewById(R.id.tv_item_split_assist);

            tvReplaceDelay = (TextView) view.findViewById(R.id.tv_item_replace_delay);
            tvReplaceHangUp = (TextView) view.findViewById(R.id.tv_item_replace_hang_up);
            tvReplaceRecovery = (TextView) view.findViewById(R.id.tv_item_replace_recovery);
            tvReplaceReject = (TextView) view.findViewById(R.id.tv_item_replace_reject);
            tvReplaceAssist = (TextView) view.findViewById(R.id.tv_item_replace_assist);

            tvReinstallDelay = (TextView) view.findViewById(R.id.tv_item_reinstall_delay);
            tvReinstallHangUp = (TextView) view.findViewById(R.id.tv_item_reinstall_hang_up);
            tvReinstallRecovery = (TextView) view.findViewById(R.id.tv_item_reinstall_recovery);
            tvReinstallReject = (TextView) view.findViewById(R.id.tv_item_reinstall_reject);
            tvReinstallAssist = (TextView) view.findViewById(R.id.tv_item_reinstall_assist);

            tvStopDelay = (TextView) view.findViewById(R.id.tv_item_stop_delay);
            tvStopHangUp = (TextView) view.findViewById(R.id.tv_item_stop_hang_up);
            tvStopRecovery = (TextView) view.findViewById(R.id.tv_item_stop_recovery);
            tvStopReject = (TextView) view.findViewById(R.id.tv_item_stop_reject);
            tvStopAssist = (TextView) view.findViewById(R.id.tv_item_stop_assist);

            tvMoveDelay = (TextView) view.findViewById(R.id.tv_item_move_delay);
            tvMoveHangUp = (TextView) view.findViewById(R.id.tv_item_move_hang_up);
            tvMoveRecovery = (TextView) view.findViewById(R.id.tv_item_move_recovery);
            tvMoveReject = (TextView) view.findViewById(R.id.tv_item_move_reject);
            tvMoveAssist = (TextView) view.findViewById(R.id.tv_item_move_assist);

            tvCheckDelay = (TextView) view.findViewById(R.id.tv_item_check_delay);
            tvCheckHangUp = (TextView) view.findViewById(R.id.tv_item_check_hang_up);
            tvCheckRecovery = (TextView) view.findViewById(R.id.tv_item_check_recovery);
            tvCheckReject = (TextView) view.findViewById(R.id.tv_item_check_reject);
            tvCheckAssist = (TextView) view.findViewById(R.id.tv_item_check_assist);

            tvReuseDelay = (TextView) view.findViewById(R.id.tv_item_reuse_delay);
            tvReuseHangUp = (TextView) view.findViewById(R.id.tv_item_reuse_hang_up);
            tvReuseRecovery = (TextView) view.findViewById(R.id.tv_item_reuse_recovery);
            tvReuseReject = (TextView) view.findViewById(R.id.tv_item_reuse_reject);
            tvReuseAssist = (TextView) view.findViewById(R.id.tv_item_reuse_assist);

            tvNoticeDelay = (TextView) view.findViewById(R.id.tv_item_notice_delay);
            tvNoticeHangUp = (TextView) view.findViewById(R.id.tv_item_notice_hang_up);
            tvNoticeRecovery = (TextView) view.findViewById(R.id.tv_item_notice_recovery);
            tvNoticeReject = (TextView) view.findViewById(R.id.tv_item_notice_reject);
            tvNoticeAssist = (TextView) view.findViewById(R.id.tv_item_notice_assist);

            tvVerifyDelay = (TextView) view.findViewById(R.id.tv_item_verify_delay);
            tvVerifyHangUp = (TextView) view.findViewById(R.id.tv_item_verify_hang_up);
            tvVerifyRecovery = (TextView) view.findViewById(R.id.tv_item_verify_recovery);
            tvVerifyReject = (TextView) view.findViewById(R.id.tv_item_verify_reject);
            tvVerifyAssist = (TextView) view.findViewById(R.id.tv_item_verify_assist);

            tvCallPayDelay = (TextView) view.findViewById(R.id.tv_item_call_pay_delay);
            tvCallPayHangUp = (TextView) view.findViewById(R.id.tv_item_call_pay_hang_up);
            tvCallPayRecovery = (TextView) view.findViewById(R.id.tv_item_call_pay_recovery);
            tvCallPayReject = (TextView) view.findViewById(R.id.tv_item_call_pay_reject);
            tvCallPayAssist = (TextView) view.findViewById(R.id.tv_item_call_pay_assist);

            tvInstallDelay = (TextView) view.findViewById(R.id.tv_item_install_delay);
            tvInstallHangUp = (TextView) view.findViewById(R.id.tv_item_install_hang_up);
            tvInstallRecovery = (TextView) view.findViewById(R.id.tv_item_install_recovery);
            tvInstallReject = (TextView) view.findViewById(R.id.tv_item_install_reject);
            tvInstallAssist = (TextView) view.findViewById(R.id.tv_item_install_assist);

            tvInsideDelay = (TextView) view.findViewById(R.id.tv_item_inside_delay);
            tvInsideHangUp = (TextView) view.findViewById(R.id.tv_item_inside_hang_up);
            tvInsideRecovery = (TextView) view.findViewById(R.id.tv_item_inside_recovery);
            tvInsideReject = (TextView) view.findViewById(R.id.tv_item_inside_reject);
            tvInsideAssist = (TextView) view.findViewById(R.id.tv_item_inside_assist);

            tvHotDelay = (TextView) view.findViewById(R.id.tv_item_hot_delay);
            tvHotHangUp = (TextView) view.findViewById(R.id.tv_item_hot_hang_up);
            tvHotRecovery = (TextView) view.findViewById(R.id.tv_item_hot_recovery);
            tvHotReject = (TextView) view.findViewById(R.id.tv_item_hot_reject);
            tvHotAssist = (TextView) view.findViewById(R.id.tv_item_hot_assist);

            tvName = (TextView) view.findViewById(R.id.tv_item_name);
        }
    }

}
