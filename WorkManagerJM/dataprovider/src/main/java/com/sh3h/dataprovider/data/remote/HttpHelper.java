package com.sh3h.dataprovider.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sh3h.dataprovider.data.entity.base.DUResult;
import com.sh3h.dataprovider.data.entity.base.DUResults;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyAssistInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchInfo;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchResult;
import com.sh3h.dataprovider.data.entity.retrofit.DownLoadWorkInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DownloadTaskResult;
import com.sh3h.dataprovider.data.entity.retrofit.MaterialResult;
import com.sh3h.dataprovider.data.entity.retrofit.MeterCard;
import com.sh3h.dataprovider.data.entity.retrofit.PersonResult;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailEntity;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.SearchHandleEntity;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsInfo;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadFileResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportInfo;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadTaskInfo;
import com.sh3h.dataprovider.data.entity.retrofit.UploadWorkRelationInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.UploadWorkResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.VerifyResult;
import com.sh3h.dataprovider.data.entity.retrofit.WordInfo;
import com.sh3h.dataprovider.data.entity.retrofit.WordResult;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDispatchHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTransformCancelHandle;
import com.sh3h.dataprovider.data.entity.ui.DUVerifyHandle;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.config.SystemConfig;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.dataprovider.util.TransformUtil;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

import static android.content.ContentValues.TAG;

@Singleton
public class HttpHelper {
    private static final String BASE_URL = "http://128.1.3.102:8021";
    private static final String METER_BASE_URL = "http://113.107.139.145:54812";
    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    private final Bus mBus;
    private boolean isConnected;
    private boolean isMeterConnected;
    private RestfulApiService restfulApiService;
    private RestfulApiService meterRestfulApiService;

    @Inject
    HttpHelper(@ApplicationContext Context context, ConfigHelper configHelper, Bus bus) {
        mContext = context;
        mConfigHelper = configHelper;
        mBus = bus;
        isConnected = false;
        isMeterConnected = false;
        restfulApiService = null;
        meterRestfulApiService = null;
    }

    private void connect() {
        if (isConnected) {
            return;
        }
        SystemConfig systemConfig = mConfigHelper.getSystemConfig();
        String baseUrl = systemConfig.getBoolean(SystemConfig.PARAM_USING_RESERVED_ADDRESS, false) ?
                systemConfig.getString(SystemConfig.PARAM_SERVER_REVERSE_BASE_URI) :
                systemConfig.getString(SystemConfig.PARAM_SERVER_BASE_URI);
        baseUrl = TextUtil.isNullOrEmpty(baseUrl) ? BASE_URL : baseUrl;
        restfulApiService = RestfulApiService.Factory.newInstance(mBus, baseUrl);
        isConnected = true;
    }

    private void connectMeter() {
        if (isMeterConnected) {
            return;
        }
        SystemConfig systemConfig = mConfigHelper.getSystemConfig();
        String baseUrl = systemConfig.getString(SystemConfig.PARAM_SERVER_METER_BASE_URI);
        baseUrl = TextUtil.isNullOrEmpty(baseUrl) ? METER_BASE_URL : baseUrl;
        meterRestfulApiService = RestfulApiService.Factory.newInstance(mBus, baseUrl);
        isMeterConnected = true;
    }

    public Observable<List<DownloadTaskResult>> downLoadWork(DownLoadWorkInfoEntity entity) {
        connect();
        return restfulApiService.downloadTasks(entity.getAccount(), entity.getType() , entity.getSince(), entity.getCount())
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null) {
                        return Observable.error(new Exception("results is error"));
                    }
                    List<DownloadTaskResult> list = results.getData();
                    return Observable.just(list);
                });
    }

    public Observable<DUResults<WordResult>> downLoadWords(WordInfo entity) {
        connect();
        return restfulApiService.downLoadWords(entity.getGroup());
    }

    public Observable<DUResults<MaterialResult>> downLoadMaterials() {
        connect();
        return restfulApiService.downLoadMaterials();
    }

    public Observable<DUResults<PersonResult>> downLoadPersons() {
        connect();
        return restfulApiService.downLoadPersons();
    }

    public Observable<DUResults<UploadWorkResultEntity>> applyAssist(String account, List<ApplyAssistInfoEntity> infoEntity) {
        connect();
        return restfulApiService.applyHelp(account, infoEntity);
    }

    public Observable<DUResults<UploadWorkResultEntity>> apply(String account, List<ApplyInfoEntity> infoEntity) {
        connect();
        return restfulApiService.apply(account, infoEntity);
    }

    public Observable<DUResults<UploadWorkResultEntity>> uploadTasks(String account,
                                                                     List<UploadTaskInfo> entity) {
        connect();
        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        String gsonStr = gson.toJson(entity);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gsonStr);
        return restfulApiService.handleTask(account, requestBody);
    }

    public Observable<List<MultiMedia>> uploadMediaList(List<MultiMedia> medias) {
        connect();

        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            try {
                if (medias == null) {
                    throw new NullPointerException("multimediaList is null");
                }

                List<MultiMedia> destMultimediaList = new ArrayList<>();
                Map<String, RequestBody> params = new HashMap<>();
                for (MultiMedia multimedia : medias) {
                    if (TextUtil.isNullOrEmpty(multimedia.getFileName())
                            || (!TextUtil.isNullOrEmpty(multimedia.getFileHash())
                            && !TextUtil.isNullOrEmpty(multimedia.getFileUrl()))) {
                        destMultimediaList.add(multimedia);
                        continue;
                    }
                    File folder;
                    switch (multimedia.getFileType()) {
                        case ConstantUtil.FileType.FILE_PICTURE:
                            folder = mConfigHelper.getImageFolderPath();
                            break;
                        case ConstantUtil.FileType.FILE_VOICE:
                            folder = mConfigHelper.getSoundFolderPath();
                            break;
                        default:
                            folder = null;
                            break;
                    }
                    if (folder == null) {
                        continue;
                    }
                    File file = new File(folder, multimedia.getFileName());
                    if (file.exists()) {
                        // create RequestBody instance from file
                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        params.put("file\"; filename=\"" + file.getName(), requestBody);
                        destMultimediaList.add(multimedia);
                    }
                }

                if ((params.size() > 0) && (params.size() == destMultimediaList.size())) {
                    Call<DUResults<UploadFileResultEntity>> call = restfulApiService.uploadFiles(params);

                    Response<DUResults<UploadFileResultEntity>> response = call.execute();

                    DUResults<UploadFileResultEntity> duResults = response.body();
                    List<UploadFileResultEntity> uploadFileResultEntityList = duResults.getData();
                    if ((uploadFileResultEntityList != null)
                            && (uploadFileResultEntityList.size() == destMultimediaList.size())) {

                        for (MultiMedia multimedia : destMultimediaList) {
                            for (UploadFileResultEntity uploadFileResultEntity : uploadFileResultEntityList) {
                                String srcFileName = multimedia.getFileName();
                                String destFileName = uploadFileResultEntity.getOriginName();
                                if (TextUtil.isNullOrEmpty(srcFileName)
                                        || TextUtil.isNullOrEmpty(destFileName)
                                        || (!srcFileName.equals(destFileName))) {
                                    LogUtil.i(TAG, "not match");
                                    continue;
                                }
                                multimedia.setFileUrl(uploadFileResultEntity.getUrl());
                                multimedia.setFileHash(uploadFileResultEntity.getFileHash());
                            }
                        }
                    }
                }
                subscriber.onNext(destMultimediaList);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                e.printStackTrace();
                subscriber.onError(new Throwable(e.getMessage()));
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> uploadMediasRelations(String taskId, List<UploadWorkRelationInfoEntity> infoEntities) {
        connect();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new Gson().toJson(infoEntities));
        return restfulApiService.uploadFileRelations(taskId, requestBody)
                .map((duResult -> duResult.getCode() == ConstantUtil.SUCCESS_CODE
                        && duResult.getStatusCode() == HttpURLConnection.HTTP_OK
                        && TextUtil.isNullOrEmpty(duResult.getMessage())));
    }

    public Observable<ArrayList<DUData>> searchWork(SearchDetailInfoEntity infoEntity) {
        connect();
        return restfulApiService.searchWork(RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(infoEntity)))
                .concatMap(results -> {
                    if (results.getCode() != ConstantUtil.SUCCESS_CODE ||
                            results.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            results.getData() == null) {
                        return Observable.error(new Exception("results is error"));
                    }

                    List<SearchDetailEntity> entities = results.getData();
                    ArrayList<DUData> list = new ArrayList<>();
                    for (SearchDetailEntity entity : entities) {
                        list.add(TransformUtil.transformDUData(entity));
                    }
                    return Observable.just(list);
                });
    }

    public Observable<DUData> searchHandle(DUData duData) {
        connect();

        return restfulApiService.searchHandle(duData.getDuTask().getTaskId())
                .concatMap(result -> {
                    if (result.getCode() != ConstantUtil.SUCCESS_CODE ||
                            result.getStatusCode() != HttpURLConnection.HTTP_OK ||
                            result.getData() == null) {
                        return Observable.error(new Exception("results is error"));
                    }

                    List<SearchHandleEntity> list = result.getData();
                    Collections.sort(list, (o1, o2) -> (int) (o2.getProcessTime() - o1.getProcessTime()));
                    List<DUHandle> handles = duData.getHandles();
                    DUTask task = duData.getDuTask();
                    handles = handles == null ? new ArrayList<>() : handles;
                    for (SearchHandleEntity entity : list) {
                        handles.add(TransformUtil.transformDUHandle(task, entity));
                    }

                    int position = 0;
                    for (DUHandle handle : handles) {
                        if (handle.getReportType() == ConstantUtil.ReportType.Handle
                                && (handle.getState() == ConstantUtil.State.BACK
                                || handle.getState() == ConstantUtil.State.HANDLE)) {
                            duData.setHandlePosition(position);
                            break;
                        }
                        position++;
                    }

                    return Observable.just(duData);
                });
    }

    public Observable<DUResults<UploadReportResult>> reportLocalTask(String account, List<UploadReportInfo> entity) {
        connect();

        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        String gsonStr = gson.toJson(entity);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gsonStr);
        return restfulApiService.reportLocalTask(account, requestBody);
    }

    public Observable<MeterCard> downloadMeterCard(String cardId){
        connectMeter();
        return meterRestfulApiService.downloadMeterCard(cardId);
    }

    public Observable<DUResults<StatisticsResult>> downloadStatistics(StatisticsInfo info){
        connect();
        return restfulApiService.downloadStatistics(info.getAccount(), info.getBeginTime(),
                info.getEndTime());
    }

    public Observable<DUResults<DispatchResult>> downloadDispatch(DispatchInfo dispatchInfo){
        connect();
        return restfulApiService.downloadDispatch(dispatchInfo.getType(),
                dispatchInfo.getSubType(), dispatchInfo.getStation(), dispatchInfo.getVolume(),
                dispatchInfo.getBeginTime(), dispatchInfo.getEndTime());
    }

    public Observable<DUResult<String>> dispatch(DUDispatchHandle handle){
        connect();
        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        String gsonStr = gson.toJson(handle);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gsonStr);
        return restfulApiService.dispatch(requestBody);
    }

    public Observable<DUResult<String>> transformCancel(DUTransformCancelHandle handle){
        connect();
        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        String gsonStr = gson.toJson(handle);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gsonStr);
        return restfulApiService.transformCancel(requestBody);
    }

    public Observable<DUResults<VerifyResult>> downloadVerify(String account){
        connect();
        return restfulApiService.downloadVerify(account);
    }

    public Observable<DUResult<String>> verifyTask(DUVerifyHandle handle){
        connect();
        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        String gsonStr = gson.toJson(handle);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gsonStr);
        return restfulApiService.verifyTask(requestBody);
    }

    public Observable<Boolean> downloadMedia(String url, String fileName, int fileType) {
        connect();
        return Observable.create(subscriber -> {
            try {
                restfulApiService.downloadFile(url).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody body = response.body();
                        if (body == null){
                            subscriber.onError(new Throwable("downloadMedia body is null."));
                            return;
                        }

                        boolean writtenToDisk = writeResponseBodyToDisk(body, fileName, fileType);
                        subscriber.onNext(writtenToDisk);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable e) {
                        subscriber.onError(e);
                    }
                });
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName, int fileType) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = fileType == ConstantUtil.FileType.FILE_VOICE ?
                    new File(mConfigHelper.getSoundFolderPath(), fileName) :
                    new File(mConfigHelper.getImageFolderPath(), fileName);
            byte[] fileReader = new byte[4096];
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
