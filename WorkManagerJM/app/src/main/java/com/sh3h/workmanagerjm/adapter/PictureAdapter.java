package com.sh3h.workmanagerjm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.myinterface.OnItemLongClickListener;
import com.sh3h.workmanagerjm.myinterface.OnPicItemClickListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 图片适配器
 * Created by limeng on 2016/9/18.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MediaHolder> implements
        View.OnClickListener, View.OnLongClickListener {
    private List<DUMedia> list;
    private File mediaFileFolder;
    private int height;
    private OnItemLongClickListener onItemLongClickListener;
    private OnPicItemClickListener onItemClickListener;
    private boolean nativeOrNetwork;
    private boolean isShowUploadStatus;

    public PictureAdapter() {
        nativeOrNetwork = true;
    }

    public void setShowUploadStatus(boolean showUploadStatus) {
        isShowUploadStatus = showUploadStatus;
    }

    public void setMediaFileFolder(File mediaFileFolder) {
        this.mediaFileFolder = mediaFileFolder;
    }

    public void setList(List<DUMedia> list) {
        this.list = list;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setNativeOrNetwork(boolean nativeOrNetwork) {
        this.nativeOrNetwork = nativeOrNetwork;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnPicItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_fragment, parent, false);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaHolder holder, int position) {
        DUMedia duMedia = list.get(position);
        String fileName = duMedia.getFileName();
        if (fileName != null && fileName.endsWith(".jpg")) {
            try {
                Glide.with(holder.getContext())
                        .load(nativeOrNetwork ? new File(mediaFileFolder, fileName) : new URL(duMedia.getFileUrl()))
                        .placeholder(R.mipmap.arrow)
                        .error(R.mipmap.ic_back)
                        .centerCrop()
                        .into(holder.ivPhoto);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            holder.ivPhoto.setImageResource(R.mipmap.ic_camera);
        }
        holder.ivPhoto.setAdjustViewBounds(true);

        switch (duMedia.getUploadFlag()) {
            case ConstantUtil.UploadFlag.INVAILD:
                holder.ivUploadStatus.setVisibility(View.GONE);
                break;
            case ConstantUtil.UploadFlag.NOT_UPLOAD:
                holder.ivUploadStatus.setVisibility(View.VISIBLE);
                holder.ivUploadStatus.setImageResource(R.mipmap.ic_cloud_p);
                break;
            case ConstantUtil.UploadFlag.UPLOADING:
                holder.ivUploadStatus.setVisibility(View.VISIBLE);
                holder.ivUploadStatus.setImageResource(R.mipmap.ic_cloud_p);
                break;
            case ConstantUtil.UploadFlag.UPLOADED:
                holder.ivUploadStatus.setVisibility(View.VISIBLE);
                holder.ivUploadStatus.setImageResource(R.mipmap.ic_cloud);
                break;
            default:
                break;
        }
        holder.ivPhoto.setTag(Integer.MIN_VALUE, position);
        holder.ivPhoto.setOnClickListener(this);
        holder.ivPhoto.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_media_fragment_photo:
                int position = (int) v.getTag(Integer.MIN_VALUE);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_media_fragment_photo:
                if (onItemLongClickListener != null) {
                    int position = (int) v.getTag(Integer.MIN_VALUE);
                    onItemLongClickListener.onItemLongClick(ConstantUtil.FileType.FILE_PICTURE,
                            position);
                }
                break;
            default:
                break;
        }
        return false;
    }

    class MediaHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layout;
        private ImageView ivPhoto, ivUploadStatus;

        MediaHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.rl_pic_item_view);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_item_media_fragment_photo);
            ivUploadStatus = (ImageView) itemView.findViewById(R.id.iv_item_media_fragment_upload_status);
            ivPhoto.setMinimumHeight(height);
        }

        public Context getContext() {
            return this.itemView.getContext();
        }
    }
}
