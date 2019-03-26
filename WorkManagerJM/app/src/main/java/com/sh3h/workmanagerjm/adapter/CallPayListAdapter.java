package com.sh3h.workmanagerjm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;

import java.util.ArrayList;

/**
 * Created by limeng on 2016/12/14.
 * 催缴
 */
public class CallPayListAdapter extends AbstractAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_pay_list, parent, false);
        return new CallPayListAdapter.CallPayHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CallPayListAdapter.CallPayHolder) {
            CallPayListAdapter.CallPayHolder holder = (CallPayListAdapter.CallPayHolder) viewHolder;
            DUData duData = list.get(position);
            DUTask duTask = duData.getDuTask();
            holder.tvCardId.setText(duTask.getCardId());
            holder.tvCardName.setText(duTask.getCardName());
            holder.tvAddress.setText(duTask.getAddress());
            holder.tvVolumeId.setText(duTask.getVolume());
            holder.tvVolumeIndex.setText(String.valueOf(duTask.getVolumeIndex()));
            holder.tvRegisterTime.setText(duTask.getStrDispatchTime());
            holder.tvEndDate.setText(duTask.getStrEndDate());

            holder.tvCardId.setTextColor(duTask.getEndDate() > System.currentTimeMillis()
                    ? Color.parseColor("#666e81") : Color.RED);

            ArrayList<DUHandle> handles = duData.getHandles();
            boolean isCallPay = handles != null && (handles.size() > 1
                    || (handles.size() == 1
                    && handles.get(0).getReportType() != ConstantUtil.ReportType.SAVE));
            holder.tvMark.setVisibility(isCallPay ? View.VISIBLE : View.INVISIBLE );
            holder.btnProcess.setOnClickListener(this);
            holder.cardView.setOnClickListener(this);
            holder.btnProcess.setTag(Integer.MAX_VALUE, position);
            holder.cardView.setTag(Integer.MAX_VALUE, position);
        } else {
            LoadingHolder loadingHolder = (LoadingHolder) viewHolder;
            loadingHolder.pbLoading.setIndeterminate(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener == null) {
            return;
        }

        int position = (int) view.getTag(Integer.MAX_VALUE);
        switch (view.getId()) {
            case R.id.btn_item_process:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_PROCESS, position);
                break;
            case R.id.card_view:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_HANDLE, position);
                break;
            default:
                break;
        }
    }

    private class CallPayHolder extends RecyclerView.ViewHolder {
        private TextView tvCardId, tvMark, tvCardName, tvAddress, tvVolumeId, tvVolumeIndex,
                tvEndDate, tvRegisterTime;
        private Button btnProcess;
        private CardView cardView;

        CallPayHolder(View view) {
            super(view);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_taskId);
            tvMark = (TextView) view.findViewById(R.id.tv_item_mark);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_card_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            tvVolumeId = (TextView) view.findViewById(R.id.tv_item_volume_id);
            tvVolumeIndex = (TextView) view.findViewById(R.id.tv_item_volume_index);
            tvEndDate = (TextView) view.findViewById(R.id.tv_item_end_date);
            tvRegisterTime = (TextView) view.findViewById(R.id.tv_item_register_time);
            btnProcess = (Button) view.findViewById(R.id.btn_item_process);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }

}
