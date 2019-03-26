package com.sh3h.workmanagerjm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.util.TransformUtil;

import java.io.File;

/**
 * Created by LiMeng on 2018/2/9.
 * 派单
 */

public class DispatchListAdapter extends AbstractAdapter implements CompoundButton.OnCheckedChangeListener{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DispatchListAdapter.DispatchHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_dispatch_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DispatchHolder dispatchHolder = (DispatchHolder) holder;
        DUData data = list.get(position);
        DUTask duTask = data.getDuTask();
        dispatchHolder.tvCardId.setText(duTask.getTaskId() + File.separator + duTask.getCardId());
        dispatchHolder.tvCardName.setText(duTask.getCardName());
        dispatchHolder.tvAddress.setText(duTask.getAddress());
        dispatchHolder.tvType.setText(TransformUtil.getTaskMultiType(dispatchHolder.getContext(), duTask));

        dispatchHolder.cbCheck.setTag(Integer.MAX_VALUE, position);
        dispatchHolder.cbCheck.setChecked(data.isCheck());
        dispatchHolder.cbCheck.setOnCheckedChangeListener(this);

        dispatchHolder.cardView.setOnClickListener(this);
        dispatchHolder.cardView.setTag(Integer.MAX_VALUE, position);
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener == null) {
            return;
        }

        int position = (int) view.getTag(Integer.MAX_VALUE);
        switch (view.getId()) {
            case R.id.card_view:
                onItemClickListener.onItemClick(ConstantUtil.ClickType.TYPE_ITEM, position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.btn_item_check:
                Integer position = (Integer) buttonView.getTag(Integer.MAX_VALUE);
                DUData data = list.get(position);
                data.setCheck(isChecked);
                break;
            default:
                break;
        }
    }

    class DispatchHolder extends RecyclerView.ViewHolder {
        private TextView tvCardId, tvCardName, tvAddress, tvType;
        private CheckBox cbCheck;
        private CardView cardView;

        DispatchHolder(View view) {
            super(view);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_taskId);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_card_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            tvType = (TextView) view.findViewById(R.id.tv_item_type);
            cbCheck = (CheckBox) view.findViewById(R.id.btn_item_check);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }

}
