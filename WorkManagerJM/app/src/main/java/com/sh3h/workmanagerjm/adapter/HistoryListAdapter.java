package com.sh3h.workmanagerjm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUApplyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.util.TransformUtil;

import java.io.File;
import java.util.List;

/**
 * Created by limeng on 2016/12/14.
 * 历史
 */

public class HistoryListAdapter extends AbstractAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list, parent, false);
        return new HistoryListAdapter.HistoryHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HistoryListAdapter.HistoryHolder) {
            HistoryListAdapter.HistoryHolder holder = (HistoryListAdapter.HistoryHolder) viewHolder;
            DUData duData = list.get(position);
            DUTask duTask = duData.getDuTask();
            holder.tvCardId.setText(duTask.getType() == ConstantUtil.WorkType.TASK_REPORT
                    ? String.valueOf(duTask.getTaskId())
                    : duTask.getTaskId() + File.separator + duTask.getCardId());
            holder.llResolvePerson.setVisibility(duData.isNeedHideHostPerson() ? View.GONE : View.VISIBLE);
            holder.tvResolvePerson.setText(duData.getHostPerson());

            String original = duTask.getTaskOrigin();
            holder.tvMark.setVisibility(duTask.getType() == ConstantUtil.WorkType.TASK_HOT
                    && !TextUtil.isNullOrEmpty(original) && original.contains("12345")
                    ? View.VISIBLE : View.INVISIBLE);

            holder.tvCardName.setText(duTask.getCardName());
            holder.tvAddress.setText(duTask.getAddress());
            holder.tvTaskType.setText(TransformUtil
                    .getTaskMultiType(((HistoryHolder) viewHolder).getContext(), duTask));
            DUHandle duHandle = duData.getHandle();
            if (duHandle != null) {
                holder.tvCompleteDate.setText(duHandle.getFormatTime());
                switch (duHandle.getReportType()) {
                    case ConstantUtil.ReportType.Handle:
                        switch (duHandle.getState()) {
                            case ConstantUtil.State.HANDLE:
                                holder.tvResult.setText(R.string.text_handler);
                                break;
                            case ConstantUtil.State.BACK:
                                holder.tvResult.setText(R.string.text_charge_back);
                                break;
                            case ConstantUtil.State.ONSPOT:
                                holder.tvResult.setText(R.string.text_on_spot);
                                break;
                            case ConstantUtil.State.RECEIVEORDER:
                                holder.tvResult.setText(R.string.text_receive_order);
                                break;
                            default:
                                holder.tvResult.setText(R.string.toast_unknown_error);
                                break;
                        }
                        break;
                    case ConstantUtil.ReportType.Report:
                        holder.tvResult.setText(R.string.title_report);
                        break;
                    case ConstantUtil.ReportType.Assist:
                        holder.tvResult.setText(R.string.menu_assist);
                        break;
                    case ConstantUtil.ReportType.Apply:
                        DUApplyHandle applyHandle = duHandle.toDUApplyHandle();
                        switch (applyHandle.getApplyType()) {
                            case ConstantUtil.ApplyType.DELAY:
                                holder.tvResult.setText(R.string.text_delay);
                                break;
                            case ConstantUtil.ApplyType.HANG_UP:
                                holder.tvResult.setText(R.string.text_hang_up);
                                break;
                            case ConstantUtil.ApplyType.RECOVERY:
                                holder.tvResult.setText(R.string.text_recovery);
                                break;
                            default:
                                holder.tvResult.setText(R.string.toast_unknown_error);
                                break;
                        }
                        break;
                    default:
                        holder.tvResult.setText(R.string.toast_unknown_error);
                        break;
                }
            }

            boolean upload = true;
            int notUploadPhoto = 0;
            int photoNumber = 0;
            int notUploadVoice = 0;
            int voiceNumber = 0;
            List<DUHandle> handles = duData.getHandles();
            if (handles != null && handles.size() > 0) {
                for (DUHandle handle : handles) {
                    if (handle.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                        upload = false;
                        break;
                    }
                }

                List<DUMedia> medias;
                for (DUHandle handle : handles) {
                    medias = handle.getMedias();
                    if (medias != null && medias.size() > 0) {
                        for (DUMedia media : medias) {
                            switch (media.getFileType()) {
                                case ConstantUtil.FileType.FILE_PICTURE:
                                    if (media.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED)
                                        notUploadPhoto++;
                                    photoNumber++;
                                    break;
                                case ConstantUtil.FileType.FILE_VOICE:
                                    if (media.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED)
                                        notUploadVoice++;
                                    voiceNumber++;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

            }

            if (upload) {
                holder.tvUploadFlag.setText(R.string.text_already_upload);
                holder.ivUploadFlag.setImageResource(R.mipmap.ic_cloud);
            } else {
                holder.tvUploadFlag.setText(R.string.text_not_upload);
                holder.ivUploadFlag.setImageResource(R.mipmap.ic_cloud_p);
            }

            holder.tvTemporary.setVisibility(duData.isHaveTemporary() ? View.VISIBLE : View.GONE);

            holder.tvPhotoNumber.setText(String.valueOf(photoNumber));
            holder.tvVoiceNumber.setText(String.valueOf(voiceNumber));
            holder.ivPhotoNumber.setImageResource(notUploadPhoto > 0 ?
                    R.mipmap.ic_picture_p : R.mipmap.ic_picture);
            holder.ivVoiceNumber.setImageResource(notUploadVoice > 0 ?
                    R.mipmap.ic_voice_p : R.mipmap.ic_voice);

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

    private class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView tvCardId, tvMark, tvCardName, tvAddress, tvTaskType, tvResolvePerson,
                tvCompleteDate, tvResult, tvUploadFlag, tvTemporary, tvPhotoNumber, tvVoiceNumber;
        private LinearLayout llResolvePerson;
        private Button btnProcess;
        private ImageView ivUploadFlag, ivPhotoNumber, ivVoiceNumber;
        private CardView cardView;

        HistoryHolder(View view) {
            super(view);
            tvCardId = (TextView) view.findViewById(R.id.tv_item_taskId);
            tvMark = (TextView) view.findViewById(R.id.tv_item_mark);
            tvCardName = (TextView) view.findViewById(R.id.tv_item_card_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_item_address);
            tvResult = (TextView) view.findViewById(R.id.tv_item_result);
            tvTaskType = (TextView) view.findViewById(R.id.tv_item_order_type);
            tvResolvePerson = (TextView) view.findViewById(R.id.tv_resolve_person);
            llResolvePerson = (LinearLayout) view.findViewById(R.id.ll_resolve_person);
            tvCompleteDate = (TextView) view.findViewById(R.id.tv_item_complete_date);
            tvUploadFlag = (TextView) view.findViewById(R.id.tv_item_upload_flag);
            tvTemporary = (TextView) view.findViewById(R.id.tv_item_temporary);
            tvPhotoNumber = (TextView) view.findViewById(R.id.tv_item_photo_number);
            tvVoiceNumber = (TextView) view.findViewById(R.id.tv_item_voice_number);

            ivUploadFlag = (ImageView) view.findViewById(R.id.iv_item_upload_flag);
            ivPhotoNumber = (ImageView) view.findViewById(R.id.iv_item_photo_number);
            ivVoiceNumber = (ImageView) view.findViewById(R.id.iv_item_voice_number);

            btnProcess = (Button) view.findViewById(R.id.btn_item_process);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }

}