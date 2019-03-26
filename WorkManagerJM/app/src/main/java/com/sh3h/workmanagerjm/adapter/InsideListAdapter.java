package com.sh3h.workmanagerjm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;

import java.util.List;

/**
 * Created by LiMeng on 2017/4/27.
 */

public class InsideListAdapter extends AbstractAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inside_list, parent, false);
        return new InsideHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof InsideHolder) {
            InsideHolder holder = (InsideHolder) viewHolder;
            DUData duData = list.get(position);
            DUTask duTask = duData.getDuTask();
            holder.tvCardId.setText(String.valueOf(duTask.getTaskId()));
            holder.tvCardName.setText(duTask.getCardName());
            holder.tvAddress.setText(duTask.getAddress());
            holder.tvRegisterTime.setText(duTask.getStrDispatchTime());
            holder.tvEndDate.setText(duTask.getStrEndDate());

            switch (duData.getDuTask().getState()) {
                case ConstantUtil.State.RECEIVEORDER:
                    holder.btnReceive.setSelected(true);
                    holder.btnArrive.setSelected(false);
                    holder.btnBack.setSelected(false);
                    holder.btnHandle.setSelected(false);
                    break;
                case ConstantUtil.State.ONSPOT:
                    holder.btnReceive.setSelected(true);
                    holder.btnArrive.setSelected(true);
                    holder.btnBack.setSelected(false);
                    holder.btnHandle.setSelected(false);
                    break;
                default:
                    holder.btnReceive.setSelected(false);
                    holder.btnArrive.setSelected(false);
                    holder.btnBack.setSelected(false);
                    holder.btnHandle.setSelected(false);
                    break;
            }

            List<DUHandle> handles = duData.getHandles();
            holder.btnAssist.setSelected(handles != null && handles.size() > 0
                    && handles.get(0).getAssist() == ConstantUtil.Assist.OK);

            int delayState = duTask.getDelayState();
            holder.btnDelay.setSelected(delayState == ConstantUtil.DelayState.REPORT_DELAY
                    || delayState == ConstantUtil.DelayState.DELAY_SUCCESS);

            switch (duTask.getHangUpState()) {
                case ConstantUtil.HangUpState.HANG_UP:
                    holder.btnHangUp.setText(R.string.text_recovery);
                    holder.btnHangUp.setSelected(false);
                    break;
                case ConstantUtil.HangUpState.WAIT_RECOVERY:
                    holder.btnHangUp.setText(R.string.text_recovery);
                    holder.btnHangUp.setSelected(true);
                    break;
                case ConstantUtil.HangUpState.NORMAL:
                    holder.btnHangUp.setText(R.string.text_hang_up);
                    holder.btnHangUp.setSelected(false);
                    break;
                case ConstantUtil.HangUpState.WAIT_HANG_UP:
                    holder.btnHangUp.setText(R.string.text_hang_up);
                    holder.btnHangUp.setSelected(true);
                    break;
                default:
                    break;
            }

            holder.tvCardId.setTextColor(duTask.getEndDate() > System.currentTimeMillis()
                    ? Color.parseColor("#666e81") : Color.RED);

            if (duTask.getIsAssist() == ConstantUtil.Assist.OK) {
                holder.linearLayout.setVisibility(View.GONE);
                holder.lineView.setVisibility(View.GONE);
                holder.btnProcess.setVisibility(View.INVISIBLE);
            } else {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.btnProcess.setVisibility(View.VISIBLE);
            }

            holder.btnReceive.setOnClickListener(this);
            holder.btnArrive.setOnClickListener(this);
            holder.btnHandle.setOnClickListener(this);
            holder.btnAssist.setOnClickListener(this);
            holder.btnBack.setOnClickListener(this);
            holder.btnDelay.setOnClickListener(this);
            holder.btnHangUp.setOnClickListener(this);
            holder.btnProcess.setOnClickListener(this);
            holder.cardView.setOnClickListener(this);

            holder.btnReceive.setTag(Integer.MAX_VALUE, position);
            holder.btnArrive.setTag(Integer.MAX_VALUE, position);
            holder.btnHandle.setTag(Integer.MAX_VALUE, position);
            holder.btnAssist.setTag(Integer.MAX_VALUE, position);
            holder.btnBack.setTag(Integer.MAX_VALUE, position);
            holder.btnDelay.setTag(Integer.MAX_VALUE, position);
            holder.btnHangUp.setTag(Integer.MAX_VALUE, position);
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
        DUData duData = list.get(position);
        switch (view.getId()) {
            case R.id.btn_item_receive:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_RECEIVE)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_RECEIVE, position);
                }
                break;
            case R.id.btn_item_arrive:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_ARRIVE)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ARRIVE, position);
                }
                break;
            case R.id.btn_item_handle:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_HANDLE)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_HANDLE, position);
                }
                break;
            case R.id.btn_item_assist:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_ASSIST)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ASSIST, position);
                }
                break;
            case R.id.btn_item_back:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_BACK)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_BACK, position);
                }
                break;
            case R.id.btn_item_delay:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_DELAY)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_DELAY, position);
                }
                break;
            case R.id.btn_item_hang_up:
                if (checkState(view.getContext(), duData, ConstantUtil.ClickType.TYPE_HANG_UP)) {
                    onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_HANG_UP, position);
                }
                break;
            case R.id.btn_item_process:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_PROCESS, position);
                break;
            case R.id.card_view:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ITEM, position);
                break;
            default:
                break;
        }
    }

    private class InsideHolder extends RecyclerView.ViewHolder {
        private TextView tvCardId, tvCardName, tvAddress, tvEndDate, tvRegisterTime;
        private Button btnReceive, btnArrive, btnHandle, btnAssist, btnBack, btnDelay,
                btnHangUp, btnProcess;
        private LinearLayout linearLayout;
        private CardView cardView;
        private View lineView;

        InsideHolder(View view) {
            super(view);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_taskId);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_card_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            tvEndDate = (TextView) view.findViewById(R.id.tv_item_end_date);
            tvRegisterTime = (TextView) view.findViewById(R.id.tv_item_register_time);

            btnReceive = (Button) view.findViewById(R.id.btn_item_receive);
            btnArrive = (Button) view.findViewById(R.id.btn_item_arrive);
            btnHandle = (Button) view.findViewById(R.id.btn_item_handle);
            btnAssist = (Button) view.findViewById(R.id.btn_item_assist);
            btnBack = (Button) view.findViewById(R.id.btn_item_back);
            btnDelay = (Button) view.findViewById(R.id.btn_item_delay);
            btnHangUp = (Button) view.findViewById(R.id.btn_item_hang_up);
            btnProcess = (Button) view.findViewById(R.id.btn_item_process);

            lineView = view.findViewById(R.id.view_divide_line);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll_module_handler);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }

}

