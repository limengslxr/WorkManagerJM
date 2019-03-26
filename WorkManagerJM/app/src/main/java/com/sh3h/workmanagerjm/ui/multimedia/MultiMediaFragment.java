package com.sh3h.workmanagerjm.ui.multimedia;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.workmanagerjm.BuildConfig;
import com.sh3h.workmanagerjm.R;
import com.sh3h.workmanagerjm.adapter.PictureAdapter;
import com.sh3h.workmanagerjm.adapter.VoiceAdapter;
import com.sh3h.workmanagerjm.myinterface.OnHandlerInterface;
import com.sh3h.workmanagerjm.myinterface.OnItemLongClickListener;
import com.sh3h.workmanagerjm.myinterface.OnPicItemClickListener;
import com.sh3h.workmanagerjm.ui.base.ParentActivity;
import com.sh3h.workmanagerjm.ui.base.ParentFragment;
import com.sh3h.workmanagerjm.util.PermissionUtil;
import com.sh3h.workmanagerjm.util.ScreenUtils;
import com.sh3h.workmanagerjm.view.VoiceView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MultiMediaFragment extends ParentFragment implements View.OnClickListener,
        MediaMvpView, OnItemLongClickListener, OnPicItemClickListener, VoiceView.VoiceListener {

    @Inject
    MediaPresenter presenter;

    @BindView(R.id.rb_take_photo)
    RadioButton rbTakePhoto;

    @BindView(R.id.rb_select_photo)
    RadioButton rbSelectPhoto;

    @BindView(R.id.rv_recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_construction)
    LinearLayout ll;

    @BindView(R.id.vv_voice)
    VoiceView vvVoice;

    @BindView(R.id.rv_voice)
    RecyclerView rvVoice;

    private String mediaFileName;
    private OnHandlerInterface mListener;
    private ArrayList<DUMedia> pictures;
    private ArrayList<DUMedia> voices;
    private DUData data;
    private PictureAdapter pictureAdapter;
    private VoiceAdapter voiceAdapter;
    private Unbinder unBind;
    private int widthPixels;

    public static MultiMediaFragment newInstance() {
        return new MultiMediaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_multi_media, container, false);
        ParentActivity activity = (ParentActivity) getActivity();
        activity.getActivityComponent().inject(this);
        presenter.attachView(this);
        data = activity.getDUData();
        if (bundle == null) {
            pictures = new ArrayList<>();
            voices = new ArrayList<>();
            DUHandle handle = data.getActualHandle();
            if (!data.isNativeOrNetwork()) {
                if (handle != null && handle.getMedias() != null) {
                    presenter.getMedias(handle.getMedias());
                }
            } else {
                presenter.getMedias(data.getDuTask().getTaskId(),
                        handle == null ? System.currentTimeMillis() : handle.getReplyTime());
            }
        } else {
            pictures = bundle.getParcelableArrayList(ConstantUtil.Parcel.PHOTO);
            voices = bundle.getParcelableArrayList(ConstantUtil.Parcel.VOICE);
        }
        unBind = ButterKnife.bind(this, view);
        presenter.attachView(this);

        widthPixels = ScreenUtils.getScreenWidth(getContext());

        initPicture();

        initVoice();

        setOnListener();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantUtil.Parcel.DUDATA, data);
        outState.putParcelableArrayList(ConstantUtil.Parcel.PHOTO, pictures);
        outState.putParcelableArrayList(ConstantUtil.Parcel.VOICE, voices);
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
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        unBind.unbind();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_take_photo:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_CAMERA, permissionGrant);
                break;
            case R.id.rb_select_photo:
                if (pictures != null && pictures.size() >= ConstantUtil.MAX_PHOTO_NUMBER) {
                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_max_photo_number);
                    return;
                }

                File folder = presenter.getImageFolderPath();
                if (!folder.exists()) {
                    boolean result = folder.mkdirs();
                    if (!result) {
                        ApplicationsUtil.showMessage(getContext(),
                                R.string.create_media_folder_fail);
                        return;
                    }
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        ConstantUtil.RequestCode.SELECT_PHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void checkCamera() {
        super.checkCamera();
        takePhoto(ConstantUtil.RequestCode.TAKE_PHOTO);
    }

    @Override
    public void getMultiMedias(List<DUMedia> duMedias) {
        for (DUMedia media : duMedias) {
            switch (media.getFileType()) {
                case ConstantUtil.FileType.FILE_PICTURE:
                    pictures.add(media);
                    break;
                case ConstantUtil.FileType.FILE_VOICE:
                    voices.add(media);
                    break;
                default:
                    break;
            }
        }

        pictureAdapter.notifyDataSetChanged();
        voiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShowMedia(DUMedia duMedia) {
        pictures.add(duMedia);
        pictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshSystemPhoto(DUMedia duMedia) {
        File file = new File(presenter.getImageFolderPath(), duMedia.getFileName());
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getActivity().sendBroadcast(intent);
        }
    }

    @Override
    public void saveMediaError() {
        ApplicationsUtil.showMessage(getActivity(), R.string.toast_save_photo_error);
    }

    @Override
    public void deleteMedia(int fileType, boolean result, int position) {
        switch (fileType) {
            case ConstantUtil.FileType.FILE_PICTURE:
                if (result) {
                    pictures.remove(position);
                    pictureAdapter.notifyDataSetChanged();
                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_delete_photo_success);
                } else {
                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_delete_photo_error);
                }
                break;
            case ConstantUtil.FileType.FILE_VOICE:
                if (result) {
                    voices.remove(position);
                    voiceAdapter.notifyDataSetChanged();
                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_delete_voice_success);
                } else {
                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_delete_voice_error);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void saveVoiceFile(boolean result, DUMedia duMedia) {
        if (result) {
            voices.add(duMedia);
            voiceAdapter.notifyDataSetChanged();
        } else {
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_save_voice_fail);
        }
    }

    @Override
    public void onItemLongClick(int fileType, int position) {
        if (ConstantUtil.TaskEntrance.HISTORY.equals(data.getTaskEntrance())
                || (ConstantUtil.TaskEntrance.CALL_PAY.equals(data.getTaskEntrance())
                && data.getOperateType() == ConstantUtil.ClickType.TYPE_PROCESS)
                || ConstantUtil.TaskEntrance.SEARCH.equals(data.getTaskEntrance())) {
            return;
        }

        switch (fileType) {
            case ConstantUtil.FileType.FILE_PICTURE:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.text_prompt)
                        .content(R.string.dialog_if_delete_photo)
                        .onPositive((dialog, which) -> presenter.deleteMedia(pictures.get(position), position))
                        .positiveText(R.string.text_ok)
                        .negativeText(R.string.text_cancel)
                        .build().show();
                break;
            case ConstantUtil.FileType.FILE_VOICE:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.text_prompt)
                        .content(R.string.dialog_if_delete_voice)
                        .onPositive((dialog, which) -> presenter.deleteMedia(voices.get(position), position))
                        .positiveText(R.string.text_ok)
                        .negativeText(R.string.text_cancel)
                        .build().show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int i) {
        mListener.OnShowPicDetail(i, pictures);
    }

    @Override
    public void voiceOver(String recordName, long time) {
        DUMedia media = new DUMedia();
        DUTask duTask = data.getDuTask();
        media.setAccount(duTask.getAccount());
        media.setTaskId(duTask.getTaskId());
        media.setType(duTask.getType());
        media.setSubType(duTask.getSubType());
        media.setState(ConstantUtil.State.HANDLE);
        media.setFileType(ConstantUtil.FileType.FILE_VOICE);
        media.setFileName(recordName);
        media.setUploadFlag(ConstantUtil.UploadFlag.INVAILD);
        presenter.saveVoiceFile(media);
    }

    public boolean havePhoto() {
        return pictures != null && pictures.size() > 0;
    }

    private void setOnListener() {
        switch (data.getTaskEntrance()) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.REPORT:
                rbTakePhoto.setOnClickListener(this);
                rbSelectPhoto.setOnClickListener(this);
                rvVoice.setOnClickListener(this);
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                if (data.getOperateType() == ConstantUtil.ClickType.TYPE_PROCESS) {
                    rbTakePhoto.setEnabled(false);
                    rbSelectPhoto.setEnabled(false);
                    vvVoice.setEnabled(false);
                } else {
                    rbTakePhoto.setOnClickListener(this);
                    rbSelectPhoto.setOnClickListener(this);
                    rvVoice.setOnClickListener(this);
                }
                break;
            case ConstantUtil.TaskEntrance.HISTORY:
            case ConstantUtil.TaskEntrance.SEARCH:
            case ConstantUtil.TaskEntrance.VERIFY:
                rbTakePhoto.setEnabled(false);
                rbSelectPhoto.setEnabled(false);
                vvVoice.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void takePhoto(int requestCode) {
        if (pictures != null && pictures.size() >= ConstantUtil.MAX_PHOTO_NUMBER) {
            ApplicationsUtil.showMessage(getActivity(), R.string.toast_max_photo_number);
            return;
        }

        File folder = presenter.getImageFolderPath();
        if (!folder.exists()) {
            boolean result = folder.mkdirs();
            if (!result) {
                ApplicationsUtil.showMessage(getContext(),
                        R.string.create_media_folder_fail);
                return;
            }
        }
        mediaFileName = String.format("%s.jpg", TextUtil.format(System.currentTimeMillis(), TextUtil.FORMAT_DATE_SECOND));
        Uri uri = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider", new File(folder, mediaFileName));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        getActivity().startActivityForResult(intent, requestCode);
    }

    public void takePhotoResult() {
        if (mediaFileName != null) {
            DUMedia media = new DUMedia();
            DUTask duTask = data.getDuTask();
            media.setAccount(duTask.getAccount());
            media.setTaskId(duTask.getTaskId());
            media.setType(duTask.getType());
            media.setSubType(duTask.getSubType());
            media.setState(ConstantUtil.State.HANDLE);
            media.setFileType(ConstantUtil.FileType.FILE_PICTURE);
            media.setFileName(mediaFileName);
            media.setUploadFlag(ConstantUtil.UploadFlag.INVAILD);
            presenter.saveAndCompressTakeImage(media);
        }
    }

    public void selectPhotoResult(Intent data) {
        if (data == null) {
            return;
        }

        Uri uri = data.getData();
        if (uri == null) {
            return;
        }

        // the path of source file
        String path = getPath(getActivity(), uri);
        if (path == null) {
            return;
        }

        String[] result = path.split(File.separator);
        if (result.length <= 0) {
            return;
        }

        DUMedia media = new DUMedia();
        DUTask duTask = this.data.getDuTask();
        media.setAccount(duTask.getAccount());
        media.setTaskId(duTask.getTaskId());
        media.setType(duTask.getType());
        media.setSubType(duTask.getSubType());
        media.setState(ConstantUtil.State.HANDLE);
        media.setFileType(ConstantUtil.FileType.FILE_PICTURE);
        media.setFileName(String.format("%s.jpg", TextUtil.format(System.currentTimeMillis(),
                TextUtil.FORMAT_DATE_SECOND)));
        media.setUploadFlag(ConstantUtil.UploadFlag.INVAILD);
        //presenter.saveSelectPhoto(path, media);
        presenter.saveAndCompressSelectImage(path, media);
    }

    private void initPicture() {
        pictureAdapter = new PictureAdapter();
        RecyclerView.LayoutManager layout = new GridLayoutManager(getContext(), ConstantUtil.SPAN_COUNT);
        ll.setMinimumHeight(widthPixels / 4);
        pictureAdapter.setMediaFileFolder(presenter.getImageFolderPath());
        pictureAdapter.setShowUploadStatus(false);
        pictureAdapter.setList(pictures);
        pictureAdapter.setHeight(widthPixels / 4);
        pictureAdapter.setNativeOrNetwork(data.isNativeOrNetwork());
        pictureAdapter.setOnItemClickListener(this);
        pictureAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(pictureAdapter);
        pictureAdapter.notifyDataSetChanged();
    }

    private void initVoice() {
        vvVoice.setOnRecordListener(this);
        vvVoice.setOutputPath(presenter.getSoundFolderPath());

        voiceAdapter = new VoiceAdapter();
        voiceAdapter.setList(voices);
        voiceAdapter.setVoiceFile(presenter.getSoundFolderPath());
        voiceAdapter.setOnItemLongClickListener(this);
        voiceAdapter.setNativeOrNetwork(data.isNativeOrNetwork());
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        rvVoice.setLayoutManager(layout);
        rvVoice.setAdapter(voiceAdapter);
        voiceAdapter.notifyDataSetChanged();
    }

    /**
     * 根据uri获取绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(File.pathSeparator);
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(File.pathSeparator);
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
