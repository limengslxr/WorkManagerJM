package com.sh3h.workmanagerjm.ui.slide;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.SlideShowPagerAdapter;
import com.sh3h.workmanagerjm.myinterface.OnHandlerInterface;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.util.ScreenUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideShowFragment extends ParentFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener {
    @Inject
    SlideShowPresenter presenter;
    @BindView(R.id.vp_slide_show)
    ViewPager viewPager;
    @BindView(R.id.ll_slide_show)
    LinearLayout llSlideShow;
    @BindView(R.id.ll_slide_show_container)
    LinearLayout llSlideShowContainer;
    private int position;
    private SlideShowPagerAdapter pagerAdapter;
    private int screenHeight, screenWidth;
    private Unbinder unbinder;
    private OnHandlerInterface mListener;
    private ArrayList<DUMedia> duMedias;

    public static SlideShowFragment newInstance(int position, ArrayList<DUMedia> duMedias) {
        SlideShowFragment fragment = new SlideShowFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantUtil.Parcel.PHOTO, duMedias);
        args.putInt(ConstantUtil.SELECT_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ((ParentActivity) getActivity()).getActivityComponent().inject(this);
        if (bundle == null) {
            if (getArguments() != null) {
                duMedias = getArguments().getParcelableArrayList(ConstantUtil.Parcel.PHOTO);
                position = getArguments().getInt(ConstantUtil.SELECT_POSITION);
            }
        } else {
            duMedias = bundle.getParcelableArrayList(ConstantUtil.Parcel.PHOTO);
            position = bundle.getInt(ConstantUtil.SELECT_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        pagerAdapter = new SlideShowPagerAdapter();
        pagerAdapter.setPictures(duMedias);
        pagerAdapter.setMediaFileFolder(presenter.getImageFolderPath());
        ParentActivity activity = (ParentActivity) getActivity();
        DUData data = activity.getDUData();
        pagerAdapter.setNativeOrNetwork(data.isNativeOrNetwork());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(this);

        screenHeight = ScreenUtils.getScreenHeight(getContext());
        screenWidth = ScreenUtils.getScreenWidth(getContext());
        int paddingWidth = (int) (0.1 * screenWidth);
        int paddingHeight = (int) (0.1 * screenHeight);
        llSlideShowContainer.setPadding(paddingWidth, paddingHeight, paddingWidth, paddingHeight);

        initSelectPoint();

        llSlideShowContainer.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ConstantUtil.Parcel.PHOTO, duMedias);
        outState.putInt(ConstantUtil.SELECT_POSITION, position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_slide_show_container:
                mListener.hideSlideShowFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHandlerInterface) {
            mListener = (OnHandlerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHandlerInterface");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        int size = llSlideShow.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = llSlideShow.getChildAt(i);
            if (i == position) {
                view.setBackgroundResource(R.mipmap.ic_select_picture_p);
            } else {
                view.setBackgroundResource(R.mipmap.ic_select_picture);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyDataChange(int position, ArrayList<DUMedia> pictures) {
        int pictureNumber = pictures == null ? 0 : pictures.size();
        if (pictureNumber <= 0) {
            return;
        }

        duMedias = pictures;
        this.position = position;
        pagerAdapter.setPictures(pictures);

        int pointNumber = llSlideShow.getChildCount();
        if (pointNumber != pictureNumber) {
            llSlideShow.removeAllViews();
            initSelectPoint();
        }

        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(position);
    }

    private void initSelectPoint() {
        if (duMedias == null || duMedias.size() == 0) {
            return;
        }

        int size = duMedias.size();
        int height = (int) (0.02 * screenWidth);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height, height);
            layoutParams.setMargins(height, height, height, height);
            imageView.setLayoutParams(layoutParams);
            if (i == position) {
                imageView.setBackgroundResource(R.mipmap.ic_select_picture_p);
            } else {
                imageView.setBackgroundResource(R.mipmap.ic_select_picture);
            }
            llSlideShow.addView(imageView);
        }
    }

}
