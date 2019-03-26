package com.sh3h.workmanagerjm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUApplyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUAssistHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;

import java.util.List;

/**
 * 过程
 * Created by BJB147 on 2017/3/30.
 */
public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessHolder>
        implements View.OnClickListener {
    private List<DUHandle> list;
    private OnItemClickListener onItemClickListener;

    public List<DUHandle> getList() {
        return list;
    }

    public void setList(List<DUHandle> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_process, parent, false);
        return new ProcessHolder(view);
    }

    @Override
    public void onBindViewHolder(ProcessHolder holder, int position) {
        DUHandle handle = list.get(position);
        holder.tvTime.setText(handle.getFormatTime());
        switch (handle.getReportType()) {
            case ConstantUtil.ReportType.Handle:
                switch (handle.getState()) {
                    case ConstantUtil.State.DISPACHORDER:
                        holder.tvTitle.setText(R.string.text_dispatch_order);
                        goneContent(holder);
                        break;
                    case ConstantUtil.State.RECEIVEORDER:
                        holder.tvTitle.setText(R.string.text_receive_order);
                        goneContent(holder);
                        break;
                    case ConstantUtil.State.ONSPOT:
                        holder.tvTitle.setText(R.string.text_on_spot);
                        goneContent(holder);
                        break;
                    case ConstantUtil.State.HANDLE:
                        holder.tvTitle.setText(R.string.text_handler);
                        goneContent(holder);
                        break;
                    case ConstantUtil.State.BACK:
                        reject(handle, holder);
                        break;
                    default:
                        break;
                }
                break;
            case ConstantUtil.ReportType.Assist:
                assist(handle, holder);
                break;
            case ConstantUtil.ReportType.Apply:
                apply(handle, holder);
                break;
            case ConstantUtil.ReportType.Report:
                holder.tvTitle.setText(R.string.title_report);
                goneContent(holder);
                break;
            case ConstantUtil.ReportType.SAVE:
                holder.tvTitle.setText(R.string.title_temporary);
                goneContent(holder);
                break;
            default:
                break;
        }

        holder.llItem.setOnClickListener(this);
        holder.llItem.setTag(Integer.MAX_VALUE, position);
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0) {
            return 0;
        }

        return list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item_process:
                int position = (int) v.getTag(Integer.MAX_VALUE);
                DUHandle handle = list.get(position);
                if (onItemClickListener == null) {
                    return;
                }

                if (handle.getState() == ConstantUtil.State.HANDLE
                        && (handle.getReportType() == ConstantUtil.ReportType.Handle
                        || handle.getReportType() == ConstantUtil.ReportType.Report)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_HANDLE, position);
                } else if (handle.getState() == ConstantUtil.State.BACK
                        && handle.getReportType() == ConstantUtil.ReportType.Handle) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_BACK, position);
                }
                break;
            default:
                break;
        }
    }

    private void goneContent(ProcessHolder holder) {
        holder.tvMainTitle.setVisibility(View.GONE);
        holder.tvMainContent.setVisibility(View.GONE);
        holder.tvLessTitle.setVisibility(View.GONE);
        holder.tvLessContent.setVisibility(View.GONE);
    }

    private void assist(DUHandle handle, ProcessHolder holder) {
        DUAssistHandle assistHandle = handle.toDUAssistHandle();
        holder.tvMainTitle.setVisibility(View.VISIBLE);
        holder.tvMainContent.setVisibility(View.VISIBLE);
        holder.tvLessTitle.setVisibility(View.VISIBLE);
        holder.tvLessContent.setVisibility(View.VISIBLE);
        holder.tvMainTitle.setText(R.string.text_assist_number);
        holder.tvMainContent.setText(String.valueOf(assistHandle.getAssistPersonCount()));
        holder.tvLessTitle.setText(R.string.text_remark);
        holder.tvLessContent.setText(assistHandle.getRemark());
        holder.tvTitle.setText(R.string.text_apply_help);
    }

    private void reject(DUHandle handle, ProcessHolder holder) {
        DUTaskHandle taskHandle = handle.toDUTaskHandle();
        holder.tvMainTitle.setVisibility(View.VISIBLE);
        holder.tvMainContent.setVisibility(View.VISIBLE);
        holder.tvLessTitle.setVisibility(View.GONE);
        holder.tvLessContent.setVisibility(View.GONE);
        holder.tvMainTitle.setText(R.string.text_reject_reason);
        holder.tvMainContent.setText(taskHandle.getProcessReason());
        holder.tvTitle.setText(R.string.text_charge_back);
    }

    private void apply(DUHandle handle, ProcessHolder holder) {
        DUApplyHandle applyHandle = handle.toDUApplyHandle();
        holder.tvMainTitle.setVisibility(View.VISIBLE);
        holder.tvMainContent.setVisibility(View.VISIBLE);

        switch (applyHandle.getApplyType()) {
            case ConstantUtil.ApplyType.DELAY:
                holder.tvTitle.setText(R.string.text_delay);
                holder.tvMainTitle.setText(R.string.text_delay_time);
                holder.tvMainContent.setText(applyHandle.getDelayTime());
                holder.tvLessTitle.setVisibility(View.VISIBLE);
                holder.tvLessContent.setVisibility(View.VISIBLE);
                holder.tvLessTitle.setText(R.string.text_delay_reason);
                holder.tvLessContent.setText(applyHandle.getReason());
                break;
            case ConstantUtil.ApplyType.HANG_UP:
                holder.tvTitle.setText(R.string.text_hang_up);
                holder.tvMainTitle.setText(R.string.text_hang_up_reason);
                holder.tvMainContent.setText(applyHandle.getReason());
                holder.tvLessTitle.setVisibility(View.GONE);
                holder.tvLessContent.setVisibility(View.GONE);
                break;
            case ConstantUtil.ApplyType.RECOVERY:
                holder.tvTitle.setText(R.string.text_recovery);
                holder.tvMainTitle.setText(R.string.text_recovery_reason);
                holder.tvMainContent.setText(applyHandle.getReason());
                holder.tvLessTitle.setVisibility(View.GONE);
                holder.tvLessContent.setVisibility(View.GONE);
                break;
            default:
                holder.tvLessTitle.setVisibility(View.GONE);
                holder.tvLessContent.setVisibility(View.GONE);
                break;
        }
    }

    class ProcessHolder extends RecyclerView.ViewHolder {
        private LinearLayout llItem;
        private TextView tvTitle, tvTime, tvMainTitle, tvMainContent, tvLessTitle, tvLessContent;

        ProcessHolder(View view) {
            super(view);
            llItem = (LinearLayout) view.findViewById(R.id.ll_item_process);
            tvTitle = (TextView) view.findViewById(R.id.tv_item_process_title);
            tvTime = (TextView) view.findViewById(R.id.tv_item_process_time);
            tvMainTitle = (TextView) view.findViewById(R.id.tv_item_process_main_title);
            tvMainContent = (TextView) view.findViewById(R.id.tv_item_process_main_content);
            tvLessTitle = (TextView) view.findViewById(R.id.tv_item_process_less_title);
            tvLessContent = (TextView) view.findViewById(R.id.tv_item_process_less_content);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
