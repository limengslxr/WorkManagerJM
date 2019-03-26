package com.sh3h.workmanagerjm.ui.multimedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.sh3h.dataprovider.condition.MultiMediaCondition;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.workmanagerjm.ui.base.ParentPresenter;
import com.sh3h.workmanagerjm.util.Compressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sh3h.workmanagerjm.CrashHandler.copyFile;

/**
 * 多媒体
 * Created by limeng on 2016/9/18.
 */
class MediaPresenter extends ParentPresenter<MediaMvpView> {

    @Inject
    MediaPresenter(DataManager dataManager) {
        super(dataManager);
    }

    File getImageFolderPath() {
        return mDataManager.getImageFolderPath();
    }

    File getSoundFolderPath() {
        return mDataManager.getSoundFolderPath();
    }

    void getMedias(ArrayList<DUMedia> list) {
        mSubscription.add(Observable.just(list)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list1 -> getMvpView().getMultiMedias(list1)));
    }

    void getMedias(long taskId, long replayTime) {
        MultiMediaCondition condition = new MultiMediaCondition();
        condition.setOperate(MultiMediaConditionDb.GET_BY_TASK_ID);
        condition.setTaskId(taskId);
        condition.setReplayTime(replayTime);
        mSubscription.add(mDataManager.getDUMedias(condition)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<DUMedia> duMedias) {
                        getMvpView().getMultiMedias(duMedias);
                    }
                }));
    }

    //压缩图片
    void saveAndCompressTakeImage(DUMedia media) {
        mSubscription.add(mDataManager.insertMedia(media)
                .concatMap(duMedia ->
                        Observable.create((Observable.OnSubscribe<DUMedia>) subscriber -> {
                            File mediaFile = new File(getImageFolderPath(), duMedia.getFileName());
                            try {
                                new Compressor()
                                        .setMaxWidth(540)
                                        .setMaxHeight(360)
                                        .setQuality(90)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(mediaFile.getPath(), mediaFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                                subscriber.onError(new Exception("compress bitmap error."));
                                return;
                            }

                            subscriber.onNext(duMedia);
                            subscriber.onCompleted();
                        })
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMedia>() {
                    @Override
                    public void onCompleted() {
                        Log.i("abc", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveMediaError();
                    }

                    @Override
                    public void onNext(DUMedia duMedia) {
                        getMvpView().onShowMedia(duMedia);
                        getMvpView().refreshSystemPhoto(duMedia);
                    }
                }));
    }

    //压缩图片
    void saveAndCompressImage(DUMedia media) {
        mSubscription.add(mDataManager.insertMedia(media)
                .concatMap(duMedia ->
                        Observable.create((Observable.OnSubscribe<DUMedia>) subscriber -> {
                            String mediaPath = new File(getImageFolderPath(), duMedia.getFileName()).getPath();

                            //将保存在本地的图片取出并缩小后显示在界面
                            //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数
                            opts.inSampleSize = 2;
                            Bitmap bitmap = BitmapFactory.decodeFile(mediaPath, opts);
                            if (bitmap == null) {
                                subscriber.onError(new Throwable("decodeFile is null"));
                                return;
                            }

                            // rotate the bitmap
                            int degree = getBitmapDegree(mediaPath);
                            if (degree != 0) {
                                bitmap = rotateBitmapByDegree(bitmap, degree);
                            }

                            //具体压缩
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                            int options = 100;
                            while (baos.toByteArray().length / 1024 > 150) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                                baos.reset();//重置baos即清空baos
                                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                                options /= 2;//每次减少一半
                            }

                            try {
                                FileOutputStream fos = new FileOutputStream(mediaPath);
                                baos.writeTo(fos);
                                fos.flush();
                                fos.close();
                                subscriber.onNext(duMedia);
                                subscriber.onCompleted();
                            } catch (IOException e) {
                                e.printStackTrace();
                                subscriber.onError(new Throwable("compress file fail"));
                            }
                        })
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMedia>() {
                    @Override
                    public void onCompleted() {
                        Log.i("abc", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveMediaError();
                    }

                    @Override
                    public void onNext(DUMedia duMedia) {
                        getMvpView().onShowMedia(duMedia);
                        getMvpView().refreshSystemPhoto(duMedia);
                    }
                }));
    }

    void saveAndCompressSelectImage(final String path, DUMedia media) {
        mSubscription.add(mDataManager.insertMedia(media)
                .concatMap((Func1<DUMedia, Observable<DUMedia>>) duMedia ->
                        Observable.create(subscriber -> {
                            try {
                                new Compressor()
                                        .setMaxWidth(540)
                                        .setMaxHeight(360)
                                        .setQuality(90)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(path,
                                                new File(getImageFolderPath(), duMedia.getFileName()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                subscriber.onError(new Exception("compress bitmap error."));
                                return;
                            }

                            subscriber.onNext(duMedia);
                            subscriber.onCompleted();
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMedia>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveMediaError();
                    }

                    @Override
                    public void onNext(DUMedia duMedia) {
                        getMvpView().onShowMedia(duMedia);
                    }
                }));
    }

    void saveSelectPhoto(final String path, DUMedia media) {
        mSubscription.add(mDataManager.insertMedia(media)
                .concatMap((Func1<DUMedia, Observable<DUMedia>>) duMedia ->
                        Observable.create(subscriber -> {
                            File file = new File(getImageFolderPath(), duMedia.getFileName());
                            if (!copyFile(path, file.getPath())) {
                                subscriber.onError(new Throwable("copy select photo error"));
                                return;
                            }

                            if (!calculateInSampleSize(file.getAbsolutePath())) {
                                subscriber.onError(new Throwable("select photo is error"));
                                return;
                            }

                            subscriber.onNext(duMedia);
                            subscriber.onCompleted();
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMedia>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveMediaError();
                    }

                    @Override
                    public void onNext(DUMedia duMedia) {
                        getMvpView().onShowMedia(duMedia);
                    }
                }));
    }

    void deleteMedia(DUMedia duMedia, int position) {
        mSubscription.add(mDataManager.deleteMedia(duMedia)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().deleteMedia(duMedia.getFileType(), false, position);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getMvpView().deleteMedia(duMedia.getFileType(), aBoolean, position);
                    }
                }));
    }

    void saveVoiceFile(DUMedia media) {
        mSubscription.add(mDataManager.insertMedia(media)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMedia>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().saveVoiceFile(false, null);
                    }

                    @Override
                    public void onNext(DUMedia media) {
                        getMvpView().saveVoiceFile(true, media);
                    }
                }));
    }

    /**
     * 读取图片旋转的角度
     *
     * @param path 路径
     * @return 旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        if (returnBm == null) {
            returnBm = bm;
        }

        if (bm != returnBm) {
            bm.recycle();
        }

        return returnBm;
    }

    private boolean calculateInSampleSize(String phoneFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = true;
        options.inTempStorage = new byte[16 * 1024];
        BitmapFactory.decodeFile(phoneFile, options);

        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            //表示图片已损毁
            // Toast.makeText(getActivity(), R.string.photo_is_damage, Toast.LENGTH_SHORT).show();
            return false;
        }

        options.inSampleSize = calculateInSampleSize(options, 160, 240);
        options.inJustDecodeBounds = false;

        Bitmap fBitmap = BitmapFactory.decodeFile(phoneFile, options);
//        Matrix scaleMatrix = new Matrix();
//        scaleMatrix.setScale(ratioX, ratioY, 0, 0);
//
//        Canvas canvas = new Canvas(fBitmap);
//        canvas.setMatrix(scaleMatrix);
//        canvas.drawBitmap(bmp, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        FileOutputStream out;
        try {
            out = new FileOutputStream(phoneFile);
            fBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            Log.i("FileNotFoundException", e.getMessage(), e);
        }
        return true;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
