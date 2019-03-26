package com.sh3h.dataprovider.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sh3h.dataprovider.condition.HistoryCondition;
import com.sh3h.dataprovider.condition.MaterialCondition;
import com.sh3h.dataprovider.condition.MultiMediaCondition;
import com.sh3h.dataprovider.condition.TaskCondition;
import com.sh3h.dataprovider.condition.TemporaryCondition;
import com.sh3h.dataprovider.condition.WordCondition;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyAssistInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.ApplyInfoEntity;
import com.sh3h.dataprovider.data.entity.retrofit.DispatchResult;
import com.sh3h.dataprovider.data.entity.retrofit.DownloadTaskResult;
import com.sh3h.dataprovider.data.entity.retrofit.FileResultEntity;
import com.sh3h.dataprovider.data.entity.retrofit.MaterialInfo;
import com.sh3h.dataprovider.data.entity.retrofit.MaterialResult;
import com.sh3h.dataprovider.data.entity.retrofit.MessageUpdate;
import com.sh3h.dataprovider.data.entity.retrofit.PersonResult;
import com.sh3h.dataprovider.data.entity.retrofit.SearchDetailEntity;
import com.sh3h.dataprovider.data.entity.retrofit.SearchHandleEntity;
import com.sh3h.dataprovider.data.entity.retrofit.UploadReportInfo;
import com.sh3h.dataprovider.data.entity.retrofit.UploadTaskInfo;
import com.sh3h.dataprovider.data.entity.retrofit.VerifyResult;
import com.sh3h.dataprovider.data.entity.retrofit.WordResult;
import com.sh3h.dataprovider.data.entity.ui.DUApplyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUAssistHandle;
import com.sh3h.dataprovider.data.entity.ui.DUData;
import com.sh3h.dataprovider.data.entity.ui.DUDownloadVerifyHandle;
import com.sh3h.dataprovider.data.entity.ui.DUHandle;
import com.sh3h.dataprovider.data.entity.ui.DUMaterial;
import com.sh3h.dataprovider.data.entity.ui.DUMedia;
import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUReportHandle;
import com.sh3h.dataprovider.data.entity.ui.DUTask;
import com.sh3h.dataprovider.data.entity.ui.DUTaskHandle;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.localprovider.condition.HistoryConditionDb;
import com.sh3h.localprovider.condition.MaterialConditionDb;
import com.sh3h.localprovider.condition.MultiMediaConditionDb;
import com.sh3h.localprovider.condition.TaskConditionDb;
import com.sh3h.localprovider.condition.TemporaryConditionDb;
import com.sh3h.localprovider.condition.WordConditionDb;
import com.sh3h.localprovider.entity.History;
import com.sh3h.localprovider.entity.Material;
import com.sh3h.localprovider.entity.MultiMedia;
import com.sh3h.localprovider.entity.Person;
import com.sh3h.localprovider.entity.Task;
import com.sh3h.localprovider.entity.Temporary;
import com.sh3h.localprovider.entity.Word;
import com.sh3h.mobileutil.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limeng on 2016/12/15.
 * 转换
 */

public class TransformUtil {

    public static TaskConditionDb transform(TaskCondition condition) {
        TaskConditionDb conditionDb = new TaskConditionDb();
        conditionDb.setOperate(condition.getOperate());
        conditionDb.setType(condition.getType());
        conditionDb.setSubType(condition.getSubType());
        conditionDb.setOffset(condition.getOffset());
        conditionDb.setLimit(condition.getLimit());
        conditionDb.setKey(condition.getKey());
        conditionDb.setAccount(condition.getAccount());
        List<MessageUpdate> list = condition.getList();
        if (list != null && list.size() > 0) {
            List<Task> tasks = new ArrayList<>();
            for (MessageUpdate messageUpdate : list) {
                tasks.add(transform(messageUpdate));
            }
            conditionDb.setList(tasks);
        }

        return conditionDb;
    }

    private static Task transform(MessageUpdate update) {
        Task task = new Task();
        task.setTaskId(update.getTaskId());
        task.setState(update.getState());
        task.setEndDate(update.getProcessTime());
        task.setHangUpState(update.getHangUpState());
        task.setDelayState(update.getDelayState());
        task.setExtend(update.getExtend());
        return task;
    }

    private static DUTask transform(Task task) {
        DUTask duTask = new DUTask();
        duTask.setId(task.getId());
        duTask.setAccount(task.getAccount());
        duTask.setTaskId(task.getTaskId());
        duTask.setHotTaskId(task.getHotTaskId());
        duTask.setType(task.getType());
        duTask.setSubType(task.getSubType());
        duTask.setDetailType(task.getDetailType());
        duTask.setState(task.getState());
        duTask.setHangUpState(task.getHangUpState());
        duTask.setDelayState(task.getDelayState());
        duTask.setIsAssist(task.getIsAssist());
        duTask.setCardId(task.getCardId());
        duTask.setCardName(task.getCardName());
        duTask.setContacts(task.getContacts());
        duTask.setProcessPerson(task.getProcessPerson());
        duTask.setAcceptName(task.getAcceptName());
        duTask.setAssistPerson(task.getAssistPerson());
        duTask.setDriver(task.getDriver());
        duTask.setTelephone(task.getTelephone());
        duTask.setAddress(task.getAddress());
        duTask.setLongitude(task.getLongitude());
        duTask.setLatitude(task.getLatitude());
        duTask.setBarCode(task.getBarCode());
        duTask.setStation(task.getStation());
        duTask.setCaliber(task.getCaliber());
        duTask.setLastReading(task.getLastReading());
        duTask.setPrepaidMoney(task.getPrepaidMoney());
        duTask.setMeterPosition(task.getMeterPosition());
        duTask.setMeterDegree(task.getMeterDegree());
        duTask.setMeterType(task.getMeterType());
        duTask.setAverageWaterQuantity(task.getAverageWaterQuantity());
        duTask.setProcessReason(task.getProcessReason());
        duTask.setArrearageRange(task.getArrearageRange());
        duTask.setArrearageMoney(task.getArrearageMoney());
        duTask.setTaskOrigin(task.getTaskOrigin());
        duTask.setProcessDate(task.getProcessDate());
        duTask.setDispatchTime(task.getDispatchTime());
        duTask.setEndDate(task.getEndDate());
        duTask.setResponseType(task.getResponseType());
        duTask.setResponseContent(task.getResponseContent());
        duTask.setVolume(task.getVolume());
        duTask.setVolumeIndex(task.getVolumeIndex());
        duTask.setSupplyWater(task.getSupplyWater());
        duTask.setAcceptGroup(task.getAcceptGroup());
        duTask.setResolveLevel(task.getResolveLevel());
        duTask.setResponseArea(task.getResponseArea());
        duTask.setRemark(task.getRemark());
        duTask.setExtend(task.getExtend());
        return duTask;
    }

    public static DUHandle transformDUHandle(History history) {
        DUHandle duHandle = new DUHandle();
        duHandle.setId(history.getId());
        duHandle.setAccount(history.getAccount());
        duHandle.setTaskId(history.getTaskId());
        duHandle.setType(history.getType());
        duHandle.setSubType(history.getSubType());
        duHandle.setState(history.getState());
        duHandle.setHangUpState(history.getHangUpState());
        duHandle.setDelayState(history.getDelayState());
        duHandle.setReportType(history.getReportType());
        duHandle.setAssist(history.getAssist());
        duHandle.setContent(history.getContent());
        duHandle.setReply(history.getReply());
        duHandle.setReplyTime(history.getReplyTime());
        duHandle.setUploadFlag(history.getUploadFlag());
        duHandle.setExtend(history.getExtend());
        return duHandle;
    }

    public static DUData transformDUData(Task task) {
        DUData data = new DUData();
        data.setDuTask(TransformUtil.transform(task));
        task.resetHistories();
        List<History> histories = task.getHistories();
        ArrayList<DUHandle> handles = new ArrayList<>();
        Temporary temporary = task.getTemporaries();
        DUHandle temporaryDUHandle = null;
        if (temporary != null) {
            temporaryDUHandle = transformDUHandle(temporary);
        }
        int position = 0;
        if (histories != null && histories.size() > 0) {
            for (History history : histories) {
                DUHandle handle = transformDUHandle(history);
                history.resetMultiMedias();
                List<MultiMedia> medias = history.getMultiMedias();
                if (medias != null && medias.size() > 0) {
                    ArrayList<DUMedia> duMedias = new ArrayList<>();
                    for (MultiMedia media : medias) {
                        duMedias.add(transformDUMedia(media));
                    }
                    handle.setMedias(duMedias);
                }
                handles.add(handle);

                if (temporaryDUHandle != null && history.getReplyTime() > temporaryDUHandle.getReplyTime()) {
                    position++;
                }

                if (temporaryDUHandle != null && history.getReplyTime() == temporaryDUHandle.getReplyTime()) {
                    temporaryDUHandle = null;
                }
            }
        }
        if (temporaryDUHandle != null) {
            handles.add(position, temporaryDUHandle);
        }

        data.setHandles(handles);
        return data;
    }

    public static ApplyAssistInfoEntity transformApplyHelpInfoEntity(DUHandle duHandle) {
        ApplyAssistInfoEntity entity = new ApplyAssistInfoEntity();
        entity.setTaskId(duHandle.getTaskId());
        entity.setType(duHandle.getType());
        entity.setSubType(duHandle.getSubType());
        entity.setState(duHandle.getState());
        entity.setAssistTime(duHandle.getReplyTime());
        DUAssistHandle duAssistHandle = duHandle.toDUAssistHandle();
        if (duAssistHandle != null) {
            entity.setAssistPersonCount(duAssistHandle.getAssistPersonCount());
            entity.setRemark(duAssistHandle.getRemark());
            entity.setExtend(duAssistHandle.getExtend());
        }
        return entity;
    }

    public static ApplyInfoEntity transformApplyInfoEntity(DUHandle handle) {
        ApplyInfoEntity entity = new ApplyInfoEntity();
        entity.setTaskId(handle.getTaskId());
        entity.setType(handle.getType());
        entity.setSubType(handle.getSubType());
        entity.setState(handle.getState());
        entity.setApplyTime(handle.getReplyTime());
        DUApplyHandle applyHandle = handle.toDUApplyHandle();
        if (applyHandle != null) {
            entity.setApplyType(applyHandle.getApplyType());
            entity.setApplyTimeEx(applyHandle.getApplyTimeEx());
            entity.setReason(applyHandle.getReason());
            entity.setRemark(applyHandle.getRemark());
            entity.setExtend(applyHandle.getExtend());
        }
        return entity;
    }

    public static HistoryConditionDb transform(HistoryCondition condition) {
        HistoryConditionDb conditionDb = new HistoryConditionDb();
        conditionDb.setOperate(condition.getOperate());
        conditionDb.setTaskId(condition.getTaskId());
        conditionDb.setOffset(condition.getOffset());
        conditionDb.setLimit(condition.getLimit());
        conditionDb.setKey(condition.getKey());
        conditionDb.setType(condition.getType());
        conditionDb.setHandleType(condition.getHandleType());
        conditionDb.setAccount(condition.getAccount());
        conditionDb.setHistory(transformHistory(condition.getDuHandle()));
        return conditionDb;
    }

    public static TemporaryConditionDb transform(TemporaryCondition condition) {
        TemporaryConditionDb conditionDb = new TemporaryConditionDb();
        conditionDb.setOperate(condition.getOperate());
        conditionDb.setAccount(condition.getAccount());
        return conditionDb;
    }

    public static DUData transformDUData(History history) {
        DUData data = new DUData();
        Gson gson = new Gson();
        DUTask duTask = TransformUtil.transform(gson.fromJson(history.getContent(), Task.class));
        duTask.setState(history.getState());
        data.setDuTask(duTask);
        ArrayList<DUHandle> handles = new ArrayList<>();
        history.resetTemporary();
        List<Temporary> temporaries = history.getTemporary();
        data.setHaveTemporary(temporaries != null && temporaries.size() > 0);
        history.resetHistories();
        List<History> histories = history.getHistories();
        if (histories != null && histories.size() > 0) {
            for (History entity : histories) {
                DUHandle handle = transformDUHandle(entity);
                entity.resetMultiMedias();
                List<MultiMedia> medias = entity.getMultiMedias();
                if (medias != null && medias.size() > 0) {
                    ArrayList<DUMedia> duMedias = new ArrayList<>();
                    for (MultiMedia media : medias) {
                        duMedias.add(transformDUMedia(media));
                    }
                    handle.setMedias(duMedias);
                }
                handles.add(handle);
            }
        }
        data.setHandles(handles);
        return data;
    }

    private static History transformHistory(DUHandle data) {
        if (data == null) {
            return null;
        }
        History history = new History();
        if (!ConstantUtil.INVALID_ID.equals(data.getId())) {
            history.setId(data.getId());
        }

        history.setTaskId(data.getTaskId());
        history.setAccount(data.getAccount());
        history.setState(data.getState());
        history.setHangUpState(data.getHangUpState());
        history.setDelayState(data.getDelayState());
        history.setReportType(data.getReportType());
        history.setType(data.getType());
        history.setSubType(data.getSubType());
        history.setAssist(data.getAssist());
        history.setContent(data.getContent());
        history.setReply(data.getReply());
        history.setReplyTime(data.getReplyTime());
        history.setUploadFlag(data.getUploadFlag());
        history.setExtend(data.getExtend());
        return history;
    }

    public static Temporary transformTemporary(DUHandle data) {
        if (data == null) {
            return null;
        }

        Temporary temporary = new Temporary();
        temporary.setId(data.getTaskId());
        temporary.setAccount(data.getAccount());
        temporary.setState(data.getState());
        temporary.setHangUpState(data.getHangUpState());
        temporary.setDelayState(data.getDelayState());
        temporary.setReportType(data.getReportType());
        temporary.setType(data.getType());
        temporary.setSubType(data.getSubType());
        temporary.setAssist(data.getAssist());
        temporary.setContent(data.getContent());
        temporary.setReply(data.getReply());
        temporary.setReplyTime(data.getReplyTime());
        temporary.setUploadFlag(data.getUploadFlag());
        temporary.setExtend(data.getExtend());
        return temporary;
    }

    public static DUHandle transformDUHandle(Temporary temporary) {
        if (temporary == null) {
            return null;
        }

        DUHandle handle = new DUHandle();
        handle.setTaskId(temporary.getId());
        handle.setAccount(temporary.getAccount());
        handle.setState(temporary.getState());
        handle.setHangUpState(temporary.getHangUpState());
        handle.setDelayState(temporary.getDelayState());
        handle.setReportType(temporary.getReportType());
        handle.setType(temporary.getType());
        handle.setSubType(temporary.getSubType());
        handle.setAssist(temporary.getAssist());
        handle.setContent(temporary.getContent());
        handle.setReply(temporary.getReply());
        handle.setReplyTime(temporary.getReplyTime());
        handle.setUploadFlag(temporary.getUploadFlag());
        handle.setExtend(temporary.getExtend());

        temporary.resetMultiMedias();
        List<MultiMedia> medias = temporary.getMultiMedias();
        if (medias != null && medias.size() > 0) {
            ArrayList<DUMedia> duMedias = new ArrayList<>();
            for (MultiMedia media : medias) {
                duMedias.add(transformDUMedia(media));
            }
            handle.setMedias(duMedias);
        }

        return handle;
    }

    public static Task transform(DownloadTaskResult result, String account) {
        Task task = new Task();
        task.setAccount(account);
        task.setTaskId(result.getTaskId());
        task.setHotTaskId(result.getHotTaskId());
        task.setType(result.getType());
        task.setSubType(result.getSubType());
        task.setDetailType(result.getDetailType());
        task.setState(result.getState());
        task.setHangUpState(result.getHangUpState());
        task.setDelayState(result.getDelayState());
        task.setIsAssist(result.getIsAssist());
        task.setCardId(result.getCardId());
        task.setCardName(result.getCardName());
        task.setContacts(result.getContacts());
        task.setProcessPerson(result.getProcessPerson());
        task.setAcceptName(result.getAcceptName());
        task.setAssistPerson(result.getAssistPerson());
        task.setDriver(result.getDriver());
        task.setTelephone(result.getTelephone());
        task.setAddress(result.getAddress());
        task.setLongitude(result.getLongitude());
        task.setLatitude(result.getLatitude());
        task.setBarCode(result.getBarCode());
        task.setStation(result.getStation());
        task.setCaliber(result.getCaliber());
        task.setLastReading(result.getLastReading());
        task.setPrepaidMoney(result.getPrepaidMoney());
        task.setMeterPosition(result.getMeterPosition());
        task.setMeterDegree(result.getMeterDegree());
        task.setMeterType(result.getMeterType());
        task.setAverageWaterQuantity(result.getAverageWaterQuantity());
        task.setProcessReason(result.getProcessReason());
        task.setArrearageRange(result.getArrearageRange());
        task.setArrearageMoney(result.getArrearageMoney());
        task.setTaskOrigin(result.getTaskOrigin());
        task.setProcessDate(result.getProcessDate());
        task.setDispatchTime(result.getDispatchTime());
        task.setEndDate(result.getEndDate());
        task.setResponseType(result.getResponseType());
        task.setResponseContent(result.getResponseContent());
        task.setVolume(result.getVolume());
        task.setVolumeIndex(result.getVolumeIndex());
        task.setSupplyWater(result.getSupplyWater());
        task.setAcceptGroup(result.getAcceptGroup());
        task.setResolveLevel(result.getResolveLevel());
        task.setResponseArea(result.getResponseArea());
        task.setRemark(result.getRemark());
        task.setExtend(result.getExtend());
        return task;
    }

    public static List<Word> transformToWord(List<WordResult> results) {
        ArrayList<Word> words = new ArrayList<>();
        for (WordResult result : results) {
            Word word = new Word();
            word.setServerId(result.getId());
            word.setParentId(result.getParentId());
            word.setGroup(result.getGroup());
            word.setName(result.getName());
            word.setValue(result.getValue());
            word.setRemark(result.getRemark());
            words.add(word);
        }
        return words;
    }

    public static List<Person> transformToPerson(List<PersonResult> results) {
        ArrayList<Person> list = new ArrayList<>();
        for (PersonResult result : results) {
            Person person = new Person();
            person.setName(result.getName());
            person.setAccount(result.getAccount());
            person.setRoles(result.getRoles());
            person.setStation(result.getStation());
            person.setPlatformRoles(result.getPlatformRoles());
            list.add(person);
        }
        return list;
    }

    public static List<Material> transformToMaterial(List<MaterialResult> results) {
        ArrayList<Material> words = new ArrayList<>();
        for (MaterialResult result : results) {
            Material material = new Material();
            material.setMaterialNo(result.getMaterialNo());
            material.setType(result.getType());
            material.setName(result.getName());
            material.setSpec(result.getSpec());
            material.setUnit(result.getUnit());
            material.setPrice(result.getPrice());
            material.setStatus(result.getStatus());
            material.setExtend(result.getExtend());
            words.add(material);
        }
        return words;
    }

    public static MultiMediaConditionDb transformMultiMediaConditionDb(MultiMediaCondition condition) {
        MultiMediaConditionDb conditionDb = new MultiMediaConditionDb();
        conditionDb.setOperate(condition.getOperate());
        conditionDb.setTaskId(condition.getTaskId());
        conditionDb.setReplayTime(condition.getReplayTime());
        conditionDb.setAccount(condition.getAccount());
        return conditionDb;
    }

    public static MultiMedia transformMultiMedia(DUMedia duMedia) {
        MultiMedia multiMedia = new MultiMedia();
        if (!ConstantUtil.INVALID_ID.equals(duMedia.getId())) {
            multiMedia.setId(duMedia.getId());
        }
        multiMedia.setAccount(duMedia.getAccount());
        multiMedia.setTaskId(duMedia.getTaskId());
        multiMedia.setType(duMedia.getType());
        multiMedia.setSubType(duMedia.getSubType());
        multiMedia.setState(duMedia.getState());
        multiMedia.setFileType(duMedia.getFileType());
        multiMedia.setFileName(duMedia.getFileName());
        multiMedia.setFileHash(duMedia.getFileHash());
        multiMedia.setFileUrl(duMedia.getFileUrl());
        multiMedia.setUploadFlag(duMedia.getUploadFlag());
        multiMedia.setExtend(duMedia.getExtend());
        return multiMedia;
    }

    public static DUMedia transformDUMedia(MultiMedia media) {
        DUMedia duMedia = new DUMedia();
        duMedia.setId(media.getId());
        duMedia.setAccount(media.getAccount());
        duMedia.setTaskId(media.getTaskId());
        duMedia.setType(media.getType());
        duMedia.setSubType(media.getSubType());
        duMedia.setState(media.getState());
        duMedia.setFileType(media.getFileType());
        duMedia.setFileName(media.getFileName());
        duMedia.setFileHash(media.getFileHash());
        duMedia.setFileUrl(media.getFileUrl());
        duMedia.setUploadFlag(media.getUploadFlag());
        duMedia.setExtend(media.getExtend());
        return duMedia;
    }

    public static UploadTaskInfo transformToUploadWorkInfo(History history) {
        UploadTaskInfo entity = new UploadTaskInfo();
        if (TextUtil.isNullOrEmpty(history.getReply())) {
            return null;
        }

        DUTaskHandle handler = new Gson().fromJson(history.getReply(), new TypeToken<DUTaskHandle>() {
        }.getType());

        entity.setTaskId(history.getTaskId());
        entity.setType(history.getType());
        entity.setSubType(history.getSubType());
        entity.setState(history.getState());
        entity.setProcessTime(history.getReplyTime());
        entity.setProcessReason(handler.getProcessReason());
        entity.setBarCode(handler.getBarCode());
        entity.setMeterType(handler.getMeterType());
        entity.setMeterProducer(handler.getMeterProducer());
        entity.setMeterReading(handler.getMeterReading());
        entity.setCaliber(handler.getCaliber());
        entity.setMeterRange(handler.getMeterRange());
        entity.setOldMeterReading(handler.getOldMeterReading());
        entity.setResolverResult(handler.getResolverResult());
        entity.setAddress(handler.getAddress());
        entity.setUrgingPaymentMethod(handler.getUrgingPaymentMethod());
        entity.setStopWater(handler.getStopWater());
        entity.setReplaceMeter(handler.getReplaceMeter());
        entity.setLatitude(handler.getLatitude());
        entity.setLongitude(handler.getLongitude());
        entity.setMeterPosition(handler.getMeterPosition());
        entity.setResolveType(handler.getResolveType());
        entity.setResolveContent(handler.getResolveContent());
        entity.setResolveDepartment(handler.getResolveDepartment());
        entity.setValveType(handler.getValveType());
        entity.setStopWaterMethod(handler.getStopWaterMethod());
        entity.setStopWaterType(handler.getStopWaterType());
        entity.setResolveMethod(handler.getResolveMethod());
        entity.setRemark(handler.getRemark());
        entity.setExtend(handler.getExtend());
        ArrayList<DUMaterial> materials = handler.getMaterials();
        if (materials != null && materials.size() > 0) {
            List<MaterialInfo> infos = new ArrayList<>();
            for (DUMaterial material : materials) {
                infos.add(transformMaterialInfo(material));
            }
            entity.setMaterials(infos);
        }
        return entity;
    }

    public static DUData transformDUData(SearchDetailEntity result) {
        DUData data = new DUData();
        data.setTaskEntrance(ConstantUtil.TaskEntrance.SEARCH);
        DUTask task = new DUTask();
        task.setTaskId(result.getTaskId());
        task.setHotTaskId(result.getHotTaskId());
        task.setType(result.getType());
        task.setSubType(result.getSubType());
        task.setDetailType(result.getDetailType());
        task.setState(result.getState());
        task.setHangUpState(result.getHangUpState());
        task.setDelayState(result.getDelayState());
        task.setIsAssist(result.getIsAssist());
        task.setCardId(result.getCardId());
        task.setCardName(result.getCardName());
        task.setContacts(result.getContacts());
        task.setProcessPerson(result.getProcessPerson());
        task.setAcceptName(result.getAcceptName());
        task.setAssistPerson(result.getAssistPerson());
        task.setDriver(result.getDriver());
        task.setTelephone(result.getTelephone());
        task.setAddress(result.getAddress());
        task.setLongitude(result.getLongitude());
        task.setLatitude(result.getLatitude());
        task.setBarCode(result.getBarCode());
        task.setStation(result.getStation());
        task.setCaliber(result.getCaliber());
        task.setLastReading(result.getLastReading());
        task.setPrepaidMoney(result.getPrepaidMoney());
        task.setMeterPosition(result.getMeterPosition());
        task.setMeterDegree(result.getMeterDegree());
        task.setMeterType(result.getMeterType());
        task.setAverageWaterQuantity(result.getAverageWaterQuantity());
        task.setProcessReason(result.getProcessReason());
        task.setArrearageRange(result.getArrearageRange());
        task.setArrearageMoney(result.getArrearageMoney());
        task.setTaskOrigin(result.getTaskOrigin());
        task.setProcessDate(result.getProcessDate());
        task.setDispatchTime(result.getDispatchTime());
        task.setEndDate(result.getEndDate());
        task.setResponseType(result.getResponseType());
        task.setResponseContent(result.getResponseContent());
        task.setVolume(result.getVolume());
        task.setVolumeIndex(result.getVolumeIndex());
        task.setSupplyWater(result.getSupplyWater());
        task.setAcceptGroup(result.getAcceptGroup());
        task.setResolveLevel(result.getResolveLevel());
        task.setResponseArea(result.getResponseArea());
        task.setRemark(result.getRemark());
        task.setExtend(result.getExtend());
        data.setDuTask(task);
        data.setHandles(new ArrayList<>());
        return data;
    }

    public static DUData transformDUData(DispatchResult result, String account) {
        DUData data = new DUData();
        data.setTaskEntrance(ConstantUtil.TaskEntrance.DISPATCH);
        DUTask task = new DUTask();
        task.setTaskId(result.getId());
        task.setHotTaskId(result.getHotTaskId());
        task.setAccount(account);
        task.setType(result.getType());
        task.setSubType(result.getSubType());
        task.setDetailType(result.getDetailType());
        task.setState(result.getState());
        task.setHangUpState(result.getHangUpState());
        task.setDelayState(result.getDelayState());
        task.setIsAssist(result.getIsAssist());
        task.setCardId(result.getCardId());
        task.setCardName(result.getCardName());
        task.setContacts(result.getContacts());
        task.setProcessPerson(result.getProcessPerson());
        task.setAcceptName(result.getAcceptName());
        task.setAssistPerson(result.getAssistPerson());
        task.setDriver(result.getDriver());
        task.setTelephone(result.getTelephone());
        task.setAddress(result.getAddress());
        task.setLongitude(result.getLongitude());
        task.setLatitude(result.getLatitude());
        task.setBarCode(result.getBarCode());
        task.setStation(result.getStation());
        task.setCaliber(result.getCaliber());
        task.setLastReading(result.getLastReading());
        task.setPrepaidMoney(result.getPrepaidMoney());
        task.setMeterPosition(result.getMeterPosition());
        task.setMeterDegree(result.getMeterDegree());
        task.setMeterType(result.getMeterType());
        task.setAverageWaterQuantity(result.getAverageWaterQuantity());
        task.setProcessReason(result.getProcessReason());
        task.setArrearageRange(result.getArrearageRange());
        task.setArrearageMoney(result.getArrearageMoney());
        task.setTaskOrigin(result.getTaskOrigin());
        task.setProcessDate(result.getProcessDate());
        task.setDispatchTime(result.getDispatchTime());
        task.setEndDate(result.getEndDate());
        task.setResponseType(result.getResponseType());
        task.setResponseContent(result.getResponseContent());
        task.setVolume(result.getVolume());
        task.setVolumeIndex(result.getVolumeIndex());
        task.setSupplyWater(result.getSupplyWater());
        task.setAcceptGroup(result.getAcceptGroup());
        task.setResolveLevel(result.getResolveLevel());
        task.setResponseArea(result.getResponseArea());
        task.setRemark(result.getRemark());
        task.setExtend(result.getExtend());
        data.setDuTask(task);
        data.setHandles(new ArrayList<>());
        return data;
    }

    public static DUData transformDUData(VerifyResult result, String account) {
        DUData data = new DUData();
        data.setTaskEntrance(ConstantUtil.TaskEntrance.VERIFY);
        data.setApplyType(result.getApplyType());

        DUTask task = new DUTask();
        task.setTaskId(result.getTaskId());
        task.setHotTaskId(result.getHotTaskId());
        task.setAccount(account);
        task.setType(result.getType());
        task.setSubType(result.getSubType());
        task.setDetailType(result.getDetailType());
        task.setState(result.getState());
        task.setHangUpState(result.getHangUpState());
        task.setDelayState(result.getDelayState());
        task.setIsAssist(result.getIsAssist());
        task.setCardId(result.getCardId());
        task.setCardName(result.getCardName());
        task.setContacts(result.getContacts());
        task.setProcessPerson(result.getProcessPerson());
        task.setAcceptName(result.getAcceptName());
        task.setAssistPerson(result.getAssistPerson());
        task.setDriver(result.getDriver());
        task.setTelephone(result.getTelephone());
        task.setAddress(result.getAddress());
        task.setLongitude(result.getLongitude());
        task.setLatitude(result.getLatitude());
        task.setBarCode(result.getBarCode());
        task.setStation(result.getStation());
        task.setCaliber(result.getCaliber());
        task.setLastReading(result.getLastReading());
        task.setPrepaidMoney(result.getPrepaidMoney());
        task.setMeterPosition(result.getMeterPosition());
        task.setMeterDegree(result.getMeterDegree());
        task.setMeterType(result.getMeterType());
        task.setAverageWaterQuantity(result.getAverageWaterQuantity());
        task.setProcessReason(result.getProcessReason());
        task.setArrearageRange(result.getArrearageRange());
        task.setArrearageMoney(result.getArrearageMoney());
        task.setTaskOrigin(result.getTaskOrigin());
        task.setProcessDate(result.getProcessDate());
        task.setDispatchTime(result.getDispatchTime());
        task.setEndDate(result.getEndDate());
        task.setResponseType(result.getResponseType());
        task.setResponseContent(result.getResponseContent());
        task.setVolume(result.getVolume());
        task.setVolumeIndex(result.getVolumeIndex());
        task.setSupplyWater(result.getSupplyWater());
        task.setAcceptGroup(result.getAcceptGroup());
        task.setResolveLevel(result.getResolveLevel());
        task.setResponseArea(result.getResponseArea());
        task.setRemark(result.getRemark());
        task.setExtend(result.getExtend());
        data.setDuTask(task);

        ArrayList<DUHandle> list = new ArrayList<>();
        DUHandle handle = data.initDUHandle();
        if (result.getApplyType() == ConstantUtil.ApplyType.REPORT) {
            DUReportHandle reportHandle = new DUReportHandle();
            reportHandle.setLocalTaskId(result.getTaskId());
            reportHandle.setType(result.getType());
            reportHandle.setCardId(result.getCardId());
            reportHandle.setCardName(result.getCardName());
            reportHandle.setAddress(result.getAddress());
            reportHandle.setTelephone(result.getTelephone());
            reportHandle.setBarCode(result.getBarCode());
            reportHandle.setMeterReading(result.getLastReading());
            reportHandle.setReportTime(result.getApplyTime());
            reportHandle.setLongitude(result.getLongitude());
            reportHandle.setLatitude(result.getLatitude());
            reportHandle.setRemark(result.getRemark());
            reportHandle.setExtend(result.getExtend());
            handle.setReply(new Gson().toJson(reportHandle));

            ArrayList<DUMedia> medias = new ArrayList<>();
            List<FileResultEntity> files = result.getFile();
            if (files != null && files.size() > 0) {
                for (FileResultEntity file : files) {
                    DUMedia media = new DUMedia();
                    media.setTaskId(result.getTaskId());
                    media.setType(result.getType());
                    media.setSubType(result.getSubType());
                    media.setState(result.getState());
                    media.setFileType(file.getFileType());
                    media.setFileName(file.getFileName());
                    media.setFileUrl(file.getFileUrl());
                    media.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                    medias.add(media);
                }
            }
            handle.setMedias(medias);
        } else {
            DUDownloadVerifyHandle verifyHandle = new DUDownloadVerifyHandle();
            verifyHandle.setApplyType(result.getApplyType());
            verifyHandle.setApplyPerson(result.getApplyPerson());
            verifyHandle.setApplyPersonCount(result.getApplyPersonCount());
            verifyHandle.setApplyTime(result.getApplyTime());
            verifyHandle.setDelayTime(result.getDelayTime());
            verifyHandle.setApplyReason(result.getApplyReason());
            verifyHandle.setApplyRemark(result.getApplyRemark());
            handle.setReply(new Gson().toJson(verifyHandle));
        }

        ArrayList<DUMedia> medias = new ArrayList<>();
        List<FileResultEntity> files = result.getFile();
        if (files != null && files.size() > 0) {
            for (FileResultEntity file : files) {
                DUMedia media = new DUMedia();
                media.setTaskId(result.getTaskId());
                media.setType(result.getType());
                media.setSubType(result.getSubType());
                media.setState(result.getState());
                media.setFileType(file.getFileType());
                media.setFileName(file.getFileName());
                media.setFileUrl(file.getFileUrl());
                media.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                medias.add(media);
            }
        }
        handle.setMedias(medias);

        list.add(handle);
        data.setHandles(list);
        return data;
    }

    public static UploadReportInfo transFormToReportInfo(History history) {
        UploadReportInfo entity = new UploadReportInfo();
        String str = history.getReply();
        DUReportHandle reportHandle = new Gson().fromJson(str, new TypeToken<DUReportHandle>() {
        }.getType());
        entity.setLocalTaskId(history.getTaskId());
        entity.setType(reportHandle.getType());
        entity.setCardId(reportHandle.getCardId());
        entity.setCardName(reportHandle.getCardName());
        entity.setAddress(reportHandle.getAddress());
        entity.setTelephone(reportHandle.getTelephone());
        entity.setMeterReading(reportHandle.getMeterReading());
        entity.setBarCode(reportHandle.getBarCode());
        entity.setReportTime(reportHandle.getReportTime());
        entity.setLongitude(reportHandle.getLongitude());
        entity.setLatitude(reportHandle.getLatitude());
        entity.setRemark(reportHandle.getRemark());
        entity.setExtend(reportHandle.getExtend());
        return entity;
    }

    public static DUHandle transformDUHandle(DUTask task, SearchHandleEntity entity) {
        DUHandle handle = new DUHandle();
        handle.setAccount(task.getAccount());
        handle.setTaskId(entity.getTaskId());
        handle.setType(entity.getType());
        handle.setSubType(entity.getSubType());
        handle.setState(entity.getState());
        handle.setContent(handle.toJson(task));
        handle.setReplyTime(entity.getProcessTime());
        handle.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
        handle.setExtend(entity.getExtend());

        DUApplyHandle applyHandle;
        DUAssistHandle assistHandle;
        DUTaskHandle taskHandle;
        switch (entity.getApplyType()) {
            case SearchHandleEntity.DELAY:
                handle.setDelayState(ConstantUtil.DelayState.REPORT_DELAY);
                handle.setHangUpState(task.getHangUpState());
                handle.setReportType(ConstantUtil.ReportType.Apply);
                handle.setAssist(ConstantUtil.Assist.NO);
                applyHandle = new DUApplyHandle(entity.getTaskId(), entity.getType(), entity.getSubType(), entity.getState(),
                        ConstantUtil.ApplyType.DELAY, entity.getProcessTime(), entity.getDelayTime());
                handle.setReply(applyHandle);
                break;
            case SearchHandleEntity.HANG_UP:
                handle.setDelayState(task.getDelayState());
                handle.setHangUpState(ConstantUtil.HangUpState.HANG_UP);
                handle.setReportType(ConstantUtil.ReportType.Apply);
                handle.setAssist(ConstantUtil.Assist.NO);
                applyHandle = new DUApplyHandle(entity.getTaskId(), entity.getType(), entity.getSubType(), entity.getState(),
                        ConstantUtil.ApplyType.HANG_UP, entity.getProcessTime(), entity.getProcessReason());
                handle.setReply(applyHandle);
                break;
            case SearchHandleEntity.RECOVERY:
                handle.setDelayState(task.getDelayState());
                handle.setHangUpState(ConstantUtil.HangUpState.NORMAL);
                handle.setReportType(ConstantUtil.ReportType.Apply);
                handle.setAssist(ConstantUtil.Assist.NO);
                applyHandle = new DUApplyHandle(entity.getTaskId(), entity.getType(), entity.getSubType(), entity.getState(),
                        ConstantUtil.ApplyType.RECOVERY, entity.getProcessTime(), entity.getProcessReason());
                handle.setReply(applyHandle);
                break;
            case SearchHandleEntity.ASSIST:
                handle.setDelayState(task.getDelayState());
                handle.setHangUpState(task.getHangUpState());
                handle.setReportType(ConstantUtil.ReportType.Assist);
                handle.setAssist(ConstantUtil.Assist.OK);
                assistHandle = new DUAssistHandle(entity.getTaskId(), entity.getType(), entity.getSubType(), entity.getState(),
                        entity.getProcessTime(), entity.getAssistPersonCount(), entity.getRemark());
                handle.setReply(assistHandle);
                break;
            default:
                handle.setDelayState(task.getDelayState());
                handle.setHangUpState(task.getHangUpState());
                handle.setReportType(ConstantUtil.ReportType.Handle);
                handle.setAssist(ConstantUtil.Assist.NO);
                handle.setReply(transformDUTaskHandle(entity));
                break;
        }

        ArrayList<DUMedia> medias = new ArrayList<>();
        List<FileResultEntity> files = entity.getFile();
        if (files != null && files.size() > 0) {
            for (FileResultEntity file : files) {
                DUMedia media = new DUMedia();
                media.setTaskId(entity.getTaskId());
                media.setType(entity.getType());
                media.setSubType(entity.getSubType());
                media.setState(entity.getState());
                media.setFileType(file.getFileType());
                media.setFileName(file.getFileName());
                media.setFileUrl(file.getFileUrl());
                media.setUploadFlag(ConstantUtil.UploadFlag.UPLOADED);
                medias.add(media);
            }
        }
        handle.setMedias(medias);

        return handle;
    }

    public static MaterialConditionDb transformMaterialConditionDb(MaterialCondition condition) {
        MaterialConditionDb conditionDb = new MaterialConditionDb();
        DUMaterial duMaterial = condition.getMaterial();
        Material material = new Material();
        material.setMaterialNo(duMaterial.getMaterialNo());
        material.setType(duMaterial.getType());
        material.setName(duMaterial.getName());
        material.setSpec(duMaterial.getSpec());
        material.setUnit(duMaterial.getUnit());
        material.setPrice(duMaterial.getPrice());
        conditionDb.setMaterial(material);
        conditionDb.setOperate(condition.getOperate());
        return conditionDb;
    }

    public static WordConditionDb transformWordConditionDb(WordCondition condition) {
        WordConditionDb conditionDb = new WordConditionDb();
        conditionDb.setOperate(condition.getOperate());
        conditionDb.setParentId(condition.getParentId());
        return conditionDb;
    }

    public static DUMaterial transformDUMaterial(Material material) {
        DUMaterial duMaterial = new DUMaterial();
        duMaterial.setMaterialNo(material.getMaterialNo());
        duMaterial.setType(material.getType());
        duMaterial.setName(material.getName());
        duMaterial.setSpec(material.getSpec());
        duMaterial.setUnit(material.getUnit());
        duMaterial.setPrice(material.getPrice());
        return duMaterial;
    }

    public static DUWord transformDUWord(Word word) {
        DUWord duWord = new DUWord();
        duWord.setId(word.getServerId());
        duWord.setParentId(word.getParentId());
        duWord.setGroup(word.getGroup());
        duWord.setName(word.getName());
        duWord.setValue(word.getValue());
        duWord.setRemark(word.getRemark());
        return duWord;
    }

    public static DUPerson transformDUPerson(Person person) {
        DUPerson duPerson = new DUPerson();
        duPerson.setName(person.getName());
        duPerson.setAccount(person.getAccount());
        duPerson.setRoles(person.getRoles());
        duPerson.setStation(person.getStation());
        duPerson.setPlatformRoles(person.getPlatformRoles());
        return duPerson;
    }

    private static MaterialInfo transformMaterialInfo(DUMaterial material) {
        MaterialInfo info = new MaterialInfo();
        info.setMaterialNo(material.getMaterialNo());
        info.setType(material.getType());
        info.setName(material.getName());
        info.setSpec(material.getSpec());
        info.setUnit(material.getUnit());
        info.setPrice(material.getPrice());
        info.setCount(material.getCount());
        return info;
    }

    private static DUTaskHandle transformDUTaskHandle(SearchHandleEntity entity) {
        DUTaskHandle handle = new DUTaskHandle();
        handle.setTaskId(entity.getTaskId());
        handle.setType(entity.getType());
        handle.setSubType(entity.getSubType());
        handle.setState(entity.getState());
        handle.setProcessTime(entity.getProcessTime());
        handle.setProcessReason(entity.getProcessReason());
        handle.setBarCode(entity.getBarCode());
        handle.setMeterType(entity.getMeterType());
        handle.setMeterProducer(entity.getMeterProducer());
        handle.setMeterReading(entity.getMeterReading());
        handle.setCaliber(entity.getCaliber());
        handle.setMeterRange(entity.getMeterRange());
        handle.setOldMeterReading(entity.getOldMeterReading());
        handle.setResolverResult(entity.getResolverResult());
        handle.setAddress(entity.getAddress());
        handle.setUrgingPaymentMethod(entity.getUrgingPaymentMethod());
        handle.setStopWater(entity.getStopWater());
        handle.setReplaceMeter(entity.getReplaceMeter());
        handle.setLongitude(entity.getLongitude());
        handle.setLatitude(entity.getLatitude());
        handle.setMeterPosition(entity.getMeterPosition());
        handle.setResolveContent(entity.getResolveContent());
        handle.setResolveType(entity.getResolveType());
        handle.setResolveDepartment(entity.getResolveDepartment());
        handle.setValveType(entity.getValveType());
        handle.setStopWaterMethod(entity.getStopWaterMethod());
        handle.setStopWaterType(entity.getStopWaterType());
        handle.setResolveMethod(entity.getResolveMethod());
        handle.setRemark(entity.getRemark());
        handle.setExtend(entity.getExtend());
        List<DUMaterial> list = entity.getMaterials();
        if (list != null && list.size() > 0) {
            ArrayList<DUMaterial> materials = new ArrayList<>();
            for (DUMaterial material : list) {
                materials.add(material);
            }
            handle.setMaterials(materials);
        }
        return handle;
    }

}
