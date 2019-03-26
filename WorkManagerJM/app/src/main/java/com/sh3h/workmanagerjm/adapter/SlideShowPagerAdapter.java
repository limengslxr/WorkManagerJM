package com.sh3h.workmanagerjm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.workmanagerjm.R;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 幻灯片播放的适配器
 * Created by limeng on 2016/10/1.
 */
public class SlideShowPagerAdapter extends PagerAdapter {
    private List<DUMedia> pictures;
    private File mediaFileFolder;
    private boolean nativeOrNetwork;

    public SlideShowPagerAdapter() {
        pictures = new ArrayList<>();
        nativeOrNetwork = true;
    }

    public void setPictures(List<DUMedia> pictures) {
        this.pictures.clear();
        if (pictures == null || pictures.size() <= 0) {
            return;
        }

        this.pictures.addAll(pictures);
    }

    public void setMediaFileFolder(File mediaFileFolder) {
        this.mediaFileFolder = mediaFileFolder;
    }

    public void setNativeOrNetwork(boolean nativeOrNetwork) {
        this.nativeOrNetwork = nativeOrNetwork;
    }

    @Override
    public int getCount() {
        return pictures == null ? 0 : pictures.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        DUMedia media = pictures.get(position);
        try {
            Glide.with(container.getContext())
                    .load(nativeOrNetwork ? new File(mediaFileFolder, media.getFileName()) :
                                    new URL(media.getFileUrl()))
                    .placeholder(R.mipmap.arrow)
                    .error(R.mipmap.ic_back)
                    .centerCrop()
                    .into(imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
