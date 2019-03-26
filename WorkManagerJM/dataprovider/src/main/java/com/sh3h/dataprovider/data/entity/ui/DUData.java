package com.sh3h.dataprovider.data.entity.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by limeng on 2016/12/21.
 * 总的数据
 */
public class DUData implements Parcelable {
    private Gson gson = new Gson();
    private DUTask duTask;
    private ArrayList<DUHandle> handles;
    //那里面来的数据（和MainShell）
    private String taskEntrance;
    //项点击还是处理
    private int operateType;
    private int handlePosition;
    //是否选中
    private boolean isCheck;
    private boolean haveTemporary;
    //此字段仅仅只是针对审批
    private int applyType;//上报类型 number,-1：默认；0: 延期, 1: 挂起, 2: 恢复,3:协助, 4: 自开单 ...
    //推送新消息 是否需要下载任务
    private ArrayList<Integer> downloadTypes;

    public DUData() {
        handles = new ArrayList<>();
        downloadTypes = new ArrayList<>();
    }

    private DUData(Parcel in) {
        operateType = in.readInt();
        handlePosition = in.readInt();
        taskEntrance = in.readString();
        boolean[] checks = new boolean[2];
        in.readBooleanArray(checks);
        isCheck = checks[0];
        haveTemporary = checks[1];
        applyType = in.readInt();
        duTask = in.readParcelable(DUTask.class.getClassLoader());
        handles = new ArrayList<>();
        in.readTypedList(handles, DUHandle.CREATOR);
        in.readList(downloadTypes, Integer.class.getClassLoader());
    }

    public DUTask getDuTask() {
        return duTask;
    }

    public void setDuTask(DUTask duTask) {
        this.duTask = duTask;
    }

    public ArrayList<DUHandle> getHandles() {
        return handles;
    }

    public void setHandles(ArrayList<DUHandle> handles) {
        this.handles = handles;
    }

    public String getTaskEntrance() {
        return taskEntrance;
    }

    public void setTaskEntrance(String taskEntrance) {
        this.taskEntrance = taskEntrance;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getHandlePosition() {
        return handlePosition;
    }

    public void setHandlePosition(int handlePosition) {
        this.handlePosition = handlePosition;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isHaveTemporary() {
        return haveTemporary;
    }

    public void setHaveTemporary(boolean haveTemporary) {
        this.haveTemporary = haveTemporary;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public ArrayList<Integer> getDownloadTypes() {
        return downloadTypes;
    }

    public void setDownloadTypes(ArrayList<Integer> downloadTypes) {
        this.downloadTypes = downloadTypes;
    }

    public DUHandle getHandle() {
        if (handles == null || handles.size() == 0
                || handles.size() < handlePosition - 1 || handlePosition < 0) {
            return null;
        }
        return handles.get(handlePosition);
    }

    public DUHandle getActualHandle() {
        if (handles == null || handles.size() == 0
                || handles.size() < handlePosition - 1 || handlePosition < 0) {
            return null;
        }

        DUHandle temporaryHandle = handles.get(handlePosition);
        if (ConstantUtil.TaskEntrance.VERIFY.equals(taskEntrance)) {
            return temporaryHandle;
        }

        int state = temporaryHandle.getState();
        if (state != ConstantUtil.State.HANDLE &&
                state != ConstantUtil.State.BACK) {
            for (DUHandle handle : handles) {
                if (handle.getState() == ConstantUtil.State.HANDLE
                        || handle.getState() == ConstantUtil.State.BACK) {
                    temporaryHandle = handle;
                    break;
                }
            }

            state = temporaryHandle.getState();
            if (state != ConstantUtil.State.HANDLE &&
                    state != ConstantUtil.State.BACK) {
                return null;
            }
        }

        if (TextUtil.isNullOrEmpty(taskEntrance)) {
            return null;
        }

        switch (taskEntrance) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.REPORT:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.HOT:
            case ConstantUtil.TaskEntrance.HISTORY:
            case ConstantUtil.TaskEntrance.SEARCH:
                return temporaryHandle;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                return temporaryHandle.getReportType() == ConstantUtil.ReportType.SAVE
                        || ConstantUtil.ClickType.TYPE_PROCESS == operateType
                        ? temporaryHandle : null;
            default:
                return null;
        }
    }

    public boolean updateAllDate() {
        boolean result = false;
        if (handles != null && handles.size() > 0) {
            result = true;
            out:
            for (DUHandle handle : handles) {
                if (handle.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                    result = false;
                    break;
                }
                List<DUMedia> medias = handle.getMedias();
                if (medias != null && medias.size() > 0) {
                    for (DUMedia media : medias) {
                        if (media.getUploadFlag() != ConstantUtil.UploadFlag.UPLOADED) {
                            result = false;
                            break out;
                        }
                    }
                }
            }
        }
        return result;
    }

    public DUHandle initDUHandle() {
        DUHandle handle = getHandle();
        /* 暂存的数据不需要创新建立对象 */
        if (handle == null || (handle.getState() != ConstantUtil.State.HANDLE
                && handle.getUploadFlag() != ConstantUtil.UploadFlag.TEMPORARY_STORAGE)
                || handle.getReportType() == ConstantUtil.ReportType.Assist
                || handle.getReportType() == ConstantUtil.ReportType.Apply
                || (handle.getType() == ConstantUtil.WorkType.TASK_CALL_PAY
                && handle.getReportType() == ConstantUtil.ReportType.Handle)) {
            handle = new DUHandle();
            handle.setReplyTime(System.currentTimeMillis());
        }
        handle.setAccount(duTask.getAccount());
        handle.setTaskId(duTask.getTaskId());
        handle.setType(duTask.getType());
        handle.setSubType(duTask.getSubType());
        int state = duTask.getState();
        handle.setState(state == ConstantUtil.State.BACK ? state : Math.max(state, handle.getState()));
        handle.setDelayState(duTask.getDelayState());
        handle.setHangUpState(duTask.getHangUpState());
        if (handles != null && handles.size() > 0) {
            handle.setAssist(getHandles().get(0).getAssist());
        }
        handle.setContent(gson.toJson(duTask));
        handle.setUploadFlag(ConstantUtil.UploadFlag.NOT_UPLOAD);
        return handle;
    }

    public boolean isPermitWrite() {
        if (TextUtil.isNullOrEmpty(taskEntrance)) {
            return false;
        }

        switch (taskEntrance) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
            case ConstantUtil.TaskEntrance.REPORT:
            case ConstantUtil.TaskEntrance.METER_INSTALL:
            case ConstantUtil.TaskEntrance.INSIDE:
            case ConstantUtil.TaskEntrance.HOT:
                return true;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                return isUploadHandleData();
            case ConstantUtil.TaskEntrance.HISTORY:
                return false;
            case ConstantUtil.TaskEntrance.SEARCH:
            case ConstantUtil.TaskEntrance.VERIFY:
                return false;
            default:
                return false;
        }
    }

    //批量下载任务
    public boolean isNeedDownloadTask() {
        if (TextUtil.isNullOrEmpty(taskEntrance)) {
            return false;
        }

        if (downloadTypes == null || downloadTypes.size() == 0) {
            return false;
        }

        int type;
        switch (taskEntrance) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                type = ConstantUtil.WorkType.TASK_BIAOWU;
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                type = ConstantUtil.WorkType.TASK_INSTALL_METER;
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                type = ConstantUtil.WorkType.TASK_INSIDE;
                break;
            case ConstantUtil.TaskEntrance.HOT:
                type = ConstantUtil.WorkType.TASK_HOT;
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                type = ConstantUtil.WorkType.TASK_CALL_PAY;
                break;
            default:
                return false;
        }

        return downloadTypes.contains(type);
    }

    //删除某种需要下载类型工单
    public void addDownloadTask(List<Integer> types) {
        if (downloadTypes == null || types == null || types.size() == 0) {
            return;
        }

        for (Integer type : types) {
            if (!downloadTypes.contains(type)) {
                downloadTypes.add(type);
            }
        }
    }

    //删除某种需要下载类型工单
    public void removeDownloadTask() {
        if (TextUtil.isNullOrEmpty(taskEntrance)) {
            return;
        }

        if (downloadTypes == null || downloadTypes.size() == 0) {
            return;
        }

        Integer type;
        switch (taskEntrance) {
            case ConstantUtil.TaskEntrance.ORDERTASKBW:
                type = ConstantUtil.WorkType.TASK_BIAOWU;
                break;
            case ConstantUtil.TaskEntrance.METER_INSTALL:
                type = ConstantUtil.WorkType.TASK_INSTALL_METER;
                break;
            case ConstantUtil.TaskEntrance.INSIDE:
                type = ConstantUtil.WorkType.TASK_INSIDE;
                break;
            case ConstantUtil.TaskEntrance.HOT:
                type = ConstantUtil.WorkType.TASK_HOT;
                break;
            case ConstantUtil.TaskEntrance.CALL_PAY:
                type = ConstantUtil.WorkType.TASK_CALL_PAY;
                break;
            default:
                return;
        }

        downloadTypes.remove(type);
    }

    public boolean isNativeOrNetwork() {
        return !ConstantUtil.TaskEntrance.SEARCH.equals(taskEntrance)
                && !ConstantUtil.TaskEntrance.VERIFY.equals(taskEntrance);
    }

    //工单派遣和未派遣工单不需要显示派单时间和处理时限
    public boolean isNeedHideDispatchDate() {
        return ConstantUtil.TaskEntrance.DISPATCH.equals(taskEntrance)
                || (duTask != null && duTask.getTaskId() == 0);
    }

    //表务、装表、热线 需要显示主本人（接单人、协助人、司机）
    public boolean isNeedHideHostPerson() {
        if (duTask == null || TextUtil.isNullOrEmpty(taskEntrance)) {
            return true;
        }

        if (ConstantUtil.TaskEntrance.ORDERTASKBW.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.METER_INSTALL.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.HOT.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.SEARCH.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.HISTORY.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.VERIFY.equals(taskEntrance)) {
            int type = duTask.getType();
            return type != ConstantUtil.WorkType.TASK_BIAOWU
                    && type != ConstantUtil.WorkType.TASK_INSTALL_METER
                    && type != ConstantUtil.WorkType.TASK_HOT;
        }

        return true;
    }

    public String getHostPerson() {
        if (duTask == null) {
            return TextUtil.EMPTY;
        }

        String content;
        StringBuilder stringBuilder = new StringBuilder();
        content = duTask.getAcceptName();
        if (!TextUtil.isNullOrEmpty(content)) {
            stringBuilder.append(content);
        }

        stringBuilder.append(File.separator);
        content = duTask.getAssistPerson();
        if (!TextUtil.isNullOrEmpty(content)) {
            stringBuilder.append(content);
        }

        stringBuilder.append(File.separator);
        content = duTask.getDriver();
        if (!TextUtil.isNullOrEmpty(content)) {
            stringBuilder.append(content);
        }

        return stringBuilder.toString();
    }

    //按back键，提示保存或者直接退出
    public boolean isDirectFinish() {
        return operateType == ConstantUtil.ClickType.TYPE_ITEM
                || (ConstantUtil.TaskEntrance.CALL_PAY.equals(taskEntrance)
                && ConstantUtil.ClickType.TYPE_PROCESS == operateType)
                || ConstantUtil.TaskEntrance.HISTORY.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.SEARCH.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.VERIFY.equals(taskEntrance)
                || ConstantUtil.TaskEntrance.DISPATCH.equals(taskEntrance);
    }

    public static final Parcelable.Creator<DUData> CREATOR = new Parcelable.Creator<DUData>() {
        public DUData createFromParcel(Parcel in) {
            return new DUData(in);
        }

        public DUData[] newArray(int size) {
            return new DUData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(operateType);
        parcel.writeInt(handlePosition);
        parcel.writeString(taskEntrance);
        boolean[] checks = new boolean[2];
        checks[0] = isCheck;
        checks[1] = haveTemporary;
        parcel.writeBooleanArray(checks);
        parcel.writeInt(applyType);
        parcel.writeParcelable(duTask, i);
        parcel.writeTypedList(handles);
        parcel.writeList(downloadTypes);
    }

    private boolean isUploadHandleData() {
        boolean result = true;
        DUHandle handle = getHandle();
        if (handle != null) {
            result = handle.getUploadFlag() == ConstantUtil.UploadFlag.UPLOADED;
        }
        return result;
    }

}
