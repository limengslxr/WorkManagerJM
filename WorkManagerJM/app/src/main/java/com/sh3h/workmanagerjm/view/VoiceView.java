package com.sh3h.workmanagerjm.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.workmanagerjm.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 自定义录音控件
 * Created by limeng on 2016/8/26.
 */
public class VoiceView extends AppCompatRadioButton implements View.OnTouchListener {
    //最快每隔0.2秒刷新一下界面
    private static final int MIN_REFRESH_TIME = 200;
    //控制录音最长时间
    private static final int MAX_RECORD_TIME = 60 * 1000;
    //把声音分为六个等级的跨度值
    private static final int STEP_RECORD_VALUE = 1600;
    //录音的保存格式
    private static final String RECORD_FORMAT = ".mp4";
    //录音类
    private MediaRecorder mMediaRecorder;
    //录音文件名
    private String mMultiMediaName;
    //录音时长
    private long mRecordLong;
    //开始录音的时间
    private long mActionDownTime;
    //保存路径
    private File mPathFile;
    //回调
    private VoiceListener mVoiceListener;
    //上下文环境
    private Context mContext;

    private Subscription mSubscription;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    //显示声音大小的图片
    private ImageView mImageView;
    //刷新时间
    private int mRefreshTime;

    /**
     * 录音回调接口
     */
    public interface VoiceListener {
        /**
         * 录音结束的回调
         *
         * @param recordName 保存的文件名
         * @param time       录音时长
         */
        void voiceOver(String recordName, long time);
    }

    public VoiceView(Context context) {
        super(context);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOnTouchListener(this);
        initParams();
    }

    /**
     * @param mRefreshTime 刷新的时间（毫秒）
     */
    public void setmRefreshTime(int mRefreshTime) {
        this.mRefreshTime = mRefreshTime;
    }

    /**
     * @param path 设置录音的保存路径
     */
    public void setOutputPath(File path) {
        this.mPathFile = path;
    }

    /**
     * @param voiceListener 设置监听
     */
    public void setOnRecordListener(VoiceListener voiceListener) {
        this.mVoiceListener = voiceListener;
    }

    /**
     * onInterceptTouchEvent()基本的规则是：
     * down事件首先会传递到onInterceptTouchEvent()方法
     * 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return false，
     * 那么后续的move, up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
     * 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return true，
     * 那么后续的move, up等事件将不再传递给onInterceptTouchEvent()，而是和down事件一样传递给该ViewGroup的onTouchEvent()处理，
     * 注意，目标view将接收不到任何事件。
     * 如果最终需要处理事件的view的onTouchEvent()返回了false，那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
     * 如果最终需要处理事件的view 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理。
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mPathFile != null) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    actionDown();
                    break;
                case MotionEvent.ACTION_UP:
                    actionUp();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    actionCancel();
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    /**
     * 按下录音
     */
    public void actionDown() {
        if (!mPathFile.exists()) {
            return;
        }

        showImageView();

        mActionDownTime = System.currentTimeMillis();
        mMultiMediaName = mActionDownTime + RECORD_FORMAT;

        mRefreshTime = mRefreshTime < MIN_REFRESH_TIME ? MIN_REFRESH_TIME : mRefreshTime;
        mSubscription = Observable.interval(mRefreshTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        recordOver();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recordOver();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //初始化录音对象
                        if (mMediaRecorder == null) {
                            mMediaRecorder = new MediaRecorder();
                            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
//                            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 设置输出文件格式
//                            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 设置编码格式
                            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 设置输出文件格式
                            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 设置编码格式
                            mMediaRecorder.setOutputFile(mPathFile.getAbsolutePath() + File.separator + mMultiMediaName);// 使用绝对路径进行保存文件
                            try {
                                mMediaRecorder.prepare();
                                mMediaRecorder.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //控制最大时长
                        if (System.currentTimeMillis() - mActionDownTime > MAX_RECORD_TIME) {
                            ApplicationsUtil.showMessage(mContext, R.string.toast_max_record_time);
                            actionUp();
                            onCompleted();
                            return;
                        }

                        if (mMediaRecorder != null && mVoiceListener != null) {
                            refreshImageView(mMediaRecorder.getMaxAmplitude() / STEP_RECORD_VALUE);
                        } else {
                            onError(new Exception());
                        }
                    }
                });
        mRecordLong = 0L;
    }

    /**
     * 取消录音
     */
    public void actionCancel() {
        hideImageView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 松开录音
     */
    public void actionUp() {
        hideImageView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        if (mMediaRecorder == null) {
            return;
        }

        File file = new File(mPathFile, mMultiMediaName);
        mRecordLong = System.currentTimeMillis() - mActionDownTime;
        if (mRecordLong < 500) {
            ApplicationsUtil.showMessage(mContext, R.string.toast_hold_voice_short);
            if (file.exists()) {
                file.delete();
            }
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            return;
        }

        try {
            mMediaRecorder.stop();
        } catch (Exception e) {
            ApplicationsUtil.showMessage(mContext, R.string.toast_check_forbid_record);
        } finally {
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }

        if (getFileSizes(file) > 0) {
            if (mVoiceListener != null) {
                mVoiceListener.voiceOver(mMultiMediaName, mRecordLong);
            }
        } else {
            ApplicationsUtil.showMessage(mContext, R.string.toast_check_forbid_record);
        }
    }

    /**
     * @param f 文件
     * @return 文件大小
     */
    private long getFileSizes(File f) {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                s = fis.available();
                if (s == 0) {
                    f.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 录音结束，取消订阅和隐藏UI
     */
    private void recordOver() {
        hideImageView();

        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * 获取windowManager，并设置相关参数
     */
    private void initParams() {
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.gravity = Gravity.CENTER;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 显示图片
     */
    private void showImageView() {
        if (mImageView == null) {
            mImageView = new ImageView(mContext);
            mImageView.setImageResource(R.mipmap.bg_recording1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!mImageView.isAttachedToWindow()) {
                mWindowManager.addView(mImageView, mParams);
            }
        } else {
            mWindowManager.addView(mImageView, mParams);
        }
    }

    /**
     * 隐藏图片
     */
    private void hideImageView() {
        if (mImageView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (mImageView.isAttachedToWindow()) {
                    mWindowManager.removeView(mImageView);
                }
            } else {
                mWindowManager.removeView(mImageView);
            }
        }
    }

    /**
     * 刷新声音大小
     *
     * @param value 声音大小
     */
    private void refreshImageView(int value) {
        if (mImageView == null) {
            return;
        }
        int id;
        switch (value) {
            case 0:
                id = R.mipmap.bg_recording1;
                break;
            case 1:
                id = R.mipmap.bg_recording2;
                break;
            case 2:
                id = R.mipmap.bg_recording3;
                break;
            case 3:
                id = R.mipmap.bg_recording4;
                break;
            case 4:
                id = R.mipmap.bg_recording5;
                break;
            default:
                id = R.mipmap.bg_recording6;
                break;
        }

        mImageView.setImageResource(id);
    }

}
