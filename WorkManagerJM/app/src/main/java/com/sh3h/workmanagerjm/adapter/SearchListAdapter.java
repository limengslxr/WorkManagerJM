package com.sh3h.workmanagerjm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.util.TransformUtil;

import java.io.File;

/**
 * Created by limeng on 2016/12/14.
 * 查询
 */

public class SearchListAdapter extends AbstractAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new SearchListAdapter.SearchHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SearchListAdapter.SearchHolder) {
            SearchListAdapter.SearchHolder holder = (SearchListAdapter.SearchHolder) viewHolder;
            DUData data = list.get(position);
            DUTask duTask = data.getDuTask();
            holder.tvCardId.setText(duTask.getTaskId() + File.separator + duTask.getCardId());
            holder.tvCardName.setText(duTask.getCardName());
            holder.tvAddress.setText(duTask.getAddress());
            holder.tvType.setText(TransformUtil.getTaskTypeResourceId(duTask.getType()));
            holder.llResolvePerson.setVisibility(data.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            holder.tvResolvePerson.setText(data.getHostPerson());

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
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ITEM, position);
                break;
            default:
                break;
        }
    }

    private class SearchHolder extends RecyclerView.ViewHolder {
        private TextView tvCardId, tvCardName, tvAddress, tvType, tvResolvePerson;
        private LinearLayout llResolvePerson;
        private Button btnProcess;
        private CardView cardView;

        SearchHolder(View view) {
            super(view);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_taskId);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_card_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            tvType = (TextView) view.findViewById(R.id.tv_item_type);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            btnProcess = (Button) view.findViewById(R.id.btn_item_process);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }

}
