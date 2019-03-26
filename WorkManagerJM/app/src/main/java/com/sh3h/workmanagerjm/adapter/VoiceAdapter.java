package com.sh3h.workmanagerjm.adapter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.myinterface.OnItemLongClickListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 录音适配器
 * Created by limeng on 2017/3/15.
 */
public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.VoiceHolder> implements
        View.OnClickListener, View.OnLongClickListener {
    private List<DUMedia> list;
    private File voiceFile;
    private OnItemLongClickListener onItemLongClickListener;
    private MediaPlayer player;
    private boolean nativeOrNetwork;

    public VoiceAdapter(){
        nativeOrNetwork = true;
    }

    public void setList(List<DUMedia> list) {
        this.list = list;
    }

    public void setVoiceFile(File voiceFile) {
        this.voiceFile = voiceFile;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setNativeOrNetwork(boolean nativeOrNetwork) {
        this.nativeOrNetwork = nativeOrNetwork;
    }

    @Override
    public VoiceAdapter.VoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice, parent, false);
        return new VoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(VoiceAdapter.VoiceHolder holder, int position) {
        DUMedia media = list.get(position);
        holder.ivUploadFlag.setBackgroundResource(media.getUploadFlag() == ConstantUtil.UploadFlag.UPLOADED
                ? R.mipmap.ic_cloud : R.mipmap.ic_cloud_p);
        int time = getTime(media);
        holder.tvVoiceWidth.setWidth(time * 6 + 10);
        holder.tvVoiceTime.setText(String.format(Locale.CHINA, "%ds", time));
        holder.llVoice.setTag(Integer.MAX_VALUE, position);
        holder.llVoice.setOnClickListener(this);
        holder.llVoice.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list == null || list.size() <= 0 ? 0 : list.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item_voice:
                int position = (int) view.getTag(Integer.MAX_VALUE);
                playVoice(position);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item_voice:
                int position = (int) view.getTag(Integer.MAX_VALUE);
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(ConstantUtil.FileType.FILE_VOICE,
                            position);
                }
                break;
        }
        return false;
    }

    public void release() {
        if (player == null) {
            return;
        }
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        player = null;
    }

    private int getTime(DUMedia media) {
        String name = media.getFileName();
        int time = 0;
        try {
            File file = new File(voiceFile, name);
            if (nativeOrNetwork && !file.exists()) {
                return time;
            }
            if (player == null) {
                player = new MediaPlayer();
            } else {
                player.reset();
            }
            player.setDataSource(nativeOrNetwork ? file.getPath() : media.getFileUrl());
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            time = player.getDuration() + 500;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time / 1000;
    }

    private void playVoice(int position) {
        try {
            DUMedia media = list.get(position);
            if (nativeOrNetwork && voiceFile == null) {
                return;
            }

            if (player == null) {
                player = new MediaPlayer();
            } else {
                player.reset();
            }
            player.setDataSource(nativeOrNetwork
                    ? voiceFile.getAbsoluteFile() + File.separator + media.getFileName()
                    : media.getFileUrl());
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class VoiceHolder extends RecyclerView.ViewHolder {
        private ImageView ivUploadFlag;
        private TextView tvVoiceWidth, tvVoiceTime;
        private LinearLayout llVoice;

        VoiceHolder(View itemView) {
            super(itemView);
            ivUploadFlag = (ImageView) itemView.findViewById(R.id.iv_item_upload_flag);
            tvVoiceWidth = (TextView) itemView.findViewById(R.id.tv_item_voice_width);
            tvVoiceTime = (TextView) itemView.findViewById(R.id.tv_item_voice_time);
            llVoice = (LinearLayout) itemView.findViewById(R.id.ll_item_voice);
        }
    }

}
