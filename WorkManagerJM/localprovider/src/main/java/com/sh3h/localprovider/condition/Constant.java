package com.sh3h.localprovider.condition;

/**
 * Created by limeng on 2016/12/22.
 */

public class Constant {

    public static class WorkState {

        public static final String WORKSTATE = "workState";

        public static final int BACK = -2;//退单

        public static final int INVALID = -1;//无效

        public static final int REGISTER = 1;//登记

        public static final int DISPACHORDER = 2;//派单

        public static final int RECEIVEORDER = 3;//接单

        public static final int DEPART = 4;//出发

        public static final int ONSPOT = 5;//到场

        public static final int HADLER = 6;//回填

        public static final int AUDIT = 10;//审核
    }

    public static class UploadFlag {
        //无效值
        public static final int INVAILD = -1;
        //未上传
        public static final int NOT_UPLOAD = 0;
        //正在上传
        public static final int UPLOADING = 1;
        //已上传
        public static final int UPLOADED = 2;
        //暂存
        public static final int TEMPORARY_STORAGE = -2;
    }

    public static class WorkType {
        //表务工单
        public static final int TASK_BIAOWU = 2;
        //催缴
        public static final int TASK_CALL_PAY = 3;
        //报装工单
        public static final int TASK_INSTALL_METER = 5;
        //内部工单
        public static final int TASK_INSIDE = 8;
        //热线
        public static final int TASK_HOT = 9;
        //上报
        public static final int TASK_REPORT = 90;
    }

    public class HandleType {
        public static final int ALL = 0;
        public static final int HANDLE = 1;
        public static final int REJECT = 2;
        public static final int DELAY = 3;
        public static final int HANG_UP = 4;
        public static final int RESTORE = 5;
        public static final int ASSIST = 6;
        public static final int SPOT = 7;
        public static final int ACCEPT = 8;
    }

    public static class DelayState {
        public static final int NORMAL = 0;
        public static final int REPORT_DELAY = 1;
        public static final int DELAY_SUCCESS = 2;
        public static final int DELAY_FAIL = 3;
    }

    public static class HangUpState {
        public static final int VALID = -1;
        public static final int NORMAL = 0;
        public static final int WAIT_HANG_UP = 1;
        public static final int HANG_UP = 2;
        public static final int WAIT_RECOVERY = 3;
    }

    //是否协助
    public static class Assist {
        public static final int OK = 1; //是   ，是协助
        public static final int NO = 0; //否   ，不是协助
    }

    public static class ReportType {
        public static final int Handle = 0;
        public static final int Report = 1;
        public static final int Assist = 2;
        public static final int Apply = 3;
        public static final int SAVE = 4;
        public static final int VERIFY = 5;
        public static final int DISPATCH = 6;
    }

}
