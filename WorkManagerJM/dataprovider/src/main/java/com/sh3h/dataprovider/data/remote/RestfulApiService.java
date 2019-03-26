package com.sh3h.dataprovider.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sh3h.dataprovider.data.entity.base.DUResult;
import com.sh3h.dataprovider.data.entity.base.DUResults;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyAssistInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchResult;
import com.sh3h.dataprovider.data.entity.retrofit.DownloadTaskResult;
import com.sh3h.dataprovider.data.entity.retrofit.MaterialResult;
import com.sh3h.dataprovider.data.entity.retrofit.MeterCard;
import com.sh3h.dataprovider.data.entity.retrofit.PersonResult;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailEntity;
import com.sh3h.dataprovider.data.entity.retrofit.SearchHandleEntity;
import com.sh3h.dataprovider.data.entity.retrofit.StatisticsResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadFileResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportResult;
import com.sh3h.dataprovider.data.entity.retrofit.UploadWorkResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.VerifyResult;
import com.sh3h.dataprovider.data.entity.retrofit.WordResult;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

interface RestfulApiService {

    /**
     * 工单任务下载
     */
    @GET("/API/v1/mobile/tasks/list")
    Observable<DUResults<DownloadTaskResult>> downloadTasks(@Query("account") String account,
                                                            @Query("type") int type,
                                                            @Query("since") int since,
                                                            @Query("count") int count);

    /**
     * 自开单上传
     */
    @POST("/API/v1/mobile/tasks/report")
    Observable<DUResults<UploadReportResult>> reportLocalTask(@Query("account") String account,
                                                              @Body RequestBody requestBody);

    /**
     * 任务处理
     */
    @POST("/API/v1/mobile/tasks/reply")
    Observable<DUResults<UploadWorkResultEntity>> handleTask(@Query("account") String account,
                                                             @Body RequestBody requestBody);

    /**
     * 任务协助
     */
    @POST("/API/v1/mobile/tasks/assist")
    Observable<DUResults<UploadWorkResultEntity>> applyHelp(@Query("account") String account,
                                                            @Body List<ApplyAssistInfoEntity> requestBody);

    /**
     * 任务申请
     */
    @POST("/API/v1/mobile/tasks/apply")
    Observable<DUResults<UploadWorkResultEntity>> apply(@Query("account") String account,
                                                        @Body List<ApplyInfoEntity> requestBody);

    /**
     * 上传文件
     */
    @Multipart
    @POST("/API/v1/mobile/files/upload")
    Call<DUResults<UploadFileResultEntity>> uploadFiles(@PartMap Map<String, RequestBody> params);

    /**
     * 上传文件关联关系
     */
    @POST("/API/v1/mobile/task/{taskId}/fileInfos/upload")
    Observable<DUResult<String>> uploadFileRelations(@Path("taskId") String taskId,
                                             @Body RequestBody requestBody);

    /**
     * 任务查询
     */
    @POST("/API/v1/mobile/task/search")
    Observable<DUResults<SearchDetailEntity>> searchWork(@Body RequestBody requestBody);

    /**
     * 查询任务回复信息
     */
    @GET("/API/v1/mobile/task/search/reply")
    Observable<DUResults<SearchHandleEntity>> searchHandle(@Query("taskId") long taskId);

    /**
     * 获取词语信息 (测试有数据)
     */
    @GET("/API/v1/mobile/words/list")
    Observable<DUResults<WordResult>> downLoadWords(@Query("group") String group);

    /**
     * 获取材料信息
     */
    @GET("/API/v1/mobile/materials/list")
    Observable<DUResults<MaterialResult>> downLoadMaterials();

    /**
     * 获取所有人员
     */
    @GET("/API/v1/mobile/persons")
    Observable<DUResults<PersonResult>> downLoadPersons();

    /**
     * 表卡信息
     */
    @GET("/API/v1/pmreading/metercard/{cardId}")
    Observable<MeterCard> downloadMeterCard(@Path("cardId") String cardId);

    /**
     * 获取统计信息
     */
    @GET("/API/v1/mobile/tasks/statistics")
    Observable<DUResults<StatisticsResult>> downloadStatistics(@Query("account") String account,
                                                               @Query("beginTime") long beginTime,
                                                               @Query("endTime") long endTime);
    
    /**
     * 获取派遣工单
     */
    @GET("/API/v1/mobile/tasks/getDispatch")
    Observable<DUResults<DispatchResult>> downloadDispatch(@Query("type") int type,
                                                           @Query("subType") int subType,
                                                           @Query("station") String station,
                                                           @Query("volume") String volume,
                                                           @Query("beginTime") long beginTime,
                                                           @Query("endTime") long endTime);

    /**
     * 派单
     */
    @POST("/API/v1/mobile/tasks/dispatchTask")
    Observable<DUResult<String>> dispatch(@Body RequestBody requestBody);

    /**
     * 转站和撤销
     */
    @POST("/API/v1/mobile/tasks/transformCancelDispatch")
    Observable<DUResult<String>> transformCancel(@Body RequestBody requestBody);

    /**
     * 获取工单审核
     */
    @GET("/API/v1/mobile/tasks/getVerify")
    Observable<DUResults<VerifyResult>> downloadVerify(@Query("account") String account);

    /**
     * 审核
     */
    @POST("/API/v1/mobile/tasks/verifyTask")
    Observable<DUResult<String>> verifyTask(@Body RequestBody requestBody);
    
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /********
     * Helper class that sets up a new services
     *******/
    class Factory {
        public static RestfulApiService newInstance(Bus bus, String baseUrl) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // BuildConfig.DEBUG

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new UnauthorisedInterceptor(bus))
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RestfulApiService.class);
        }
    }
}
