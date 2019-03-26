package com.sh3h.dataprovider.util;


/**
 * 常量类
 */
public class ConstantUtil {
    //无效的id
    public static final Long INVALID_ID = -1L;
    //照片的最多数量
    public static final int MAX_PHOTO_NUMBER = 6;
    //向服务器上传的值如果是空值，默认取-1
    public static final int UPLOAD_NULL = -1;
    //滚动时，控制置顶按钮显示与隐藏
    public static final int FIRST_VISIBLE_ITEM_POSITION = 4;
    //swipeRefreshLayout 下拉刷新圈的开始位置
    public static final int SRL_START_PROGRESS_OFFSET = 0;
    //swipeRefreshLayout 下拉刷新圈的结束位置
    public static final int SRL_END_PROGRESS_OFFSET = 100;
    //一次分页查找的数量
    public static final int GET_PAGE_DATA_LIMIT = 10;

    public static final int SPAN_COUNT = 3;
    //选择的下标
    public static final String SELECT_POSITION = "selectPosition";
    //当前activity显示的fragment的名字
    public static final String CURRENT_FRAGMENT = "currentFragment";
    public static final int SUCCESS_CODE = 0;
    public static final int EMPTY_DATA = 0;
    public static final int DELETE_DATA_INTERVAL = 1000 * 3600 * 24 * 60;//两个月
    public static final double SMALL_NUMBER = 1E-10;
    public static final String CONNECT_SIGN = "@";
    public static final String PICTURE_SUFFIX = "MW";

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

    public static class WorkSubType {

        /*表务工单*/
        public static final int SPLIT_METER = 1; //拆表

        public static final int REPLACE_METER = 2;//换表

        public static final int INSTALL_METER = 3;//复装

        public static final int STOP_METER = 4;//停水

        public static final int MOVE_METER = 5;//迁表

        public static final int CHECK_METER = 6;//验表

        public static final int REUSE = 7;//复用

        public static final int NOTICE = 8;//派发停水通知单

        public static final int VERIFY = 9;//新装水表核对工单
        /*表务工单*/

        /*内部工单*/
        public static final int REPAIR = 11;//维修
        public static final int ELECTRICIAN = 12;//电工
        public static final int OTHER = 13;//其他
        public static final int SITE_METER = 14;//工地表续期

        public static final int FLOOR_ACCEPTANCE = 15;//楼层验收
        /*内部工单*/
    }

    public static class DetailType {
        /*迁表工单*/
        public static final int PAID = 1;//有偿迁表
        public static final int FREE = 2;//无偿迁表
        /*迁表工单*/

        /*换表工单*/
        public static final int APPLY = 1;//申请换表
        public static final int REGULAR = 2;//定期换表
        public static final int FAULT = 3;//故障换表
        /*换表工单*/

        /*拆表类型*/
        public static final int ARREARAGE = 1;//欠费拆表
        public static final int OTHER = 2;//其他拆表
        /*拆表类型*/
    }

    public static class State {

        public static final int BACK = -2;//退单

        public static final int INVALID = -1;//无效

        public static final int REGISTER = 1;//登记

        public static final int DISPACHORDER = 2;//派单

        public static final int RECEIVEORDER = 3;//接单

        public static final int DEPART = 4;//出发

        public static final int ONSPOT = 5;//到场

        public static final int HANDLE = 6;//回填

        public static final int AUDIT = 10;//审核
    }

    public static class SelfReportType {
        public static final String WORK_TASK = "表务工单";
        public static final String OFFICIAL_TASK = "管网工单";
        public static final String LEAKAGE = "漏水";
        public static final String ILLEGAL_WATER = "违章用水";
        public static final String CHANGE_METER = "换表";
        public static final String ADJUST_INFO = "基本信息调整";
        public static final String OTHER = "其他";

        public static final int WORK_TASK_NUMBER = 1;
        public static final int OFFICIAL_TASK_NUMBER = 2;
        public static final int LEAKAGE_NUMBER = 3;
        public static final int ILLEGAL_WATER_NUMBER = 4;
        public static final int CHANGE_METER_NUMBER = 5;
        public static final int ADJUST_INFO_NUMBER = 6;
        public static final int OTHER_NUMBER = 7;
    }


    /**
     * 操作类型
     */
    public static class ClickType {
        //接收
        public static final int TYPE_RECEIVE = 1;
        //到场
        public static final int TYPE_ARRIVE = 2;
        //处理
        public static final int TYPE_HANDLE = 3;
        //协助
        public static final int TYPE_ASSIST = 4;
        //退单
        public static final int TYPE_BACK = 5;
        //延期
        public static final int TYPE_DELAY = 6;
        //挂起、恢复
        public static final int TYPE_HANG_UP = 7;
        //流程
        public static final int TYPE_PROCESS = 8;
        //item点击事件
        public static final int TYPE_ITEM = 9;
        //协助审批派遣
        public static final int TYPE_ASSIST_VERIFY_DISPATCH = 10;
    }

    /**
     * 工单的接入口
     */
    public static class TaskEntrance {

        //主程序向子程序发送的账号
        public static final String ACCOUNT = "account";
        //向子程序发送用户id
        public static final String USER_ID = "userId";
        //向子程序发送用户名
        public static final String USER_NAME = "userName";

        public static final String PARAMES = "params";

        public static final String ORDERTASKBW = "orderTask";    //表务工单

        public static final String REPORT = "report";         //工单上报

        public static final String METER_INSTALL = "meterInstall";    //报装工单

        public static final String HISTORY = "history";        //历史工单

        public static final String CALL_PAY = "callPay";        //催缴工单

        public static final String SEARCH = "search";         //查询

        public static final String INSIDE = "inside";       //内部工单

        public static final String HOT = "hot";         //热线

        public static final String STATISTICS = "statistics";         //统计

        public static final String VERIFY = "verify";         //审核

        public static final String DISPATCH = "dispatch";         //派单
    }

    //文件类型
    public static class FileType {
        //图片
        public static final int FILE_PICTURE = 0;
        //语音
        public static final int FILE_VOICE = 1;
    }

    //初始化步骤
    public static class InitStep {
        public static final String INIT_STEP = "initStep";

        //检查权限
        public static final int CHECK_PERMISSION = 1;
        //初始化目录
        public static final int INIT_CONFIG = 2;
        //清理超过两个月的历史数据
        public static final int CLEAN_DATA = 3;
        //防止数据出现上传中
        public static final int UPDATE_UPLOAD_FLAG = 4;
        //下载词语
        public static final int DOWNLOAD_WORD = 5;
        //下载材料
        public static final int DOWNLOAD_MATERIAL = 6;
        //下载人员
        public static final int DOWNLOAD_PERSON = 7;
        //初始化结束
        public static final int INIT_OVER = 8;
    }

    //请求码
    public static class RequestCode {
        //检查权限
        public static final int CHECK_PERMISSION = 1000;
        //拍照
        public static final int TAKE_PHOTO = 1001;
        //相册
        public static final int SELECT_PHOTO = 1002;
        //定位
        public static final int LOCATE_MAP = 1003;
        //扫描
        public static final int SCAN = 1004;
    }

    //是否协助
    public static class Assist {
        public static final int OK = 1; //是   ，是协助
        public static final int NO = 0; //否   ，不是协助
    }

    public static class Key {
        public static final String POSITION = "position";
        public static final String PRESS_BACK = "pressBack";
        public static final String IS_REPORT = "isReport";
    }

    //传递的Parcelable
    public static class Parcel {
        public static final String DUDATA = "duData";
        public static final String DUTASK = "duTask";
        public static final String PHOTO = "photo";
        public static final String VOICE = "voice";
        public static final String DUHANDLE = "deHandle";
        public static final String DUWORD = "duWord";
        public static final String DUMATERIAL = "duMaterial";
    }

    //上传标志
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

    public static class StopWater {
        public static final int NO = 0;//不停
        public static final int OK = 1;//停水
    }

    public static class CallPayMethod {
        public static final int PHONE = 1;
        public static final int ARRIVE = 2;
    }

    public static class Map {
        public static final String MAP_STATUS = "mapStatus";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String ADDRESS = "address";

        public static final int LOCATE_MAP = 1;
        public static final int MARK_MAP = 2;
        public static final int NAVIGATION_MAP = 4;

        public static final double ERROR_VALUE = 1e-10;
    }

    public static class HangUpState {
        public static final int VALID = -1;
        public static final int NORMAL = 0;
        public static final int WAIT_HANG_UP = 1;
        public static final int HANG_UP = 2;
        public static final int WAIT_RECOVERY = 3;
    }

    public static class DelayState {
        public static final int NORMAL = 0;
        public static final int REPORT_DELAY = 1;
        public static final int DELAY_SUCCESS = 2;
        public static final int DELAY_FAIL = 3;
    }

    public static class ApplyType {
        public static final int DEFAULT = -1;
        public static final int DELAY = 0;
        public static final int HANG_UP = 1;
        public static final int RECOVERY = 2;
        public static final int ASSIST = 3;
        public static final int REPORT = 4;
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

    public static final class StopMethod {
        public static final int STOP_USE = 1;//停用
        public static final int SPLIT = 2;//拆表
    }

    public static final class StopType {
        public static final int REPORT_STOP = 1;//报停
        public static final int ARREARAGE = 2;//欠费

        public static final int CLOSE_METER_TAIL = 1;//封表尾
        public static final int LOCKING = 2;//锁制
        public static final int OTHER = 3;//其他
    }

    public static final class MeterKind {
        public static final String NORMAL = "AAA";// 普通表
        public static final String REMOTE = "BBB";// 远传表
    }

    public static final class ReplaceMeter {
        public static final int NO = 0;//报停
        public static final int YES = 1;//欠费
    }

    public static final class ResolveMethod {
        public static final int ARRIVE = 1; //现场处理
        public static final int PHONE = 2; //电话处理
    }

    public static class ParentId {
        public static final int RESOLVE_RESULT = 1;//处理结果
        public static final int METER_CALIBER = 2;//水表口径
        public static final int METER_TYPE = 3;//水表型号
        public static final int METER_PRODUCER = 4;//水表厂商
        public static final int RESOLVE_TYPE = 5;//处理类别
        public static final int RESOLVE_DEPARTMENT = 6;//处理部门
        public static final int VALVE_TYPE = 7;//表阀门分类
        public static final int STATION = 8;//站点
    }

    public static class Group {
        public static final String RESOLVE_RESULT = "resolve_result";//处理结果
        public static final String METER_CALIBER = "meter_caliber";//水表口径
        public static final String METER_TYPE = "meter_type";//水表型号
        public static final String METER_PRODUCER = "meter_producer";//水表厂商
        public static final String RESOLVE_TYPE = "resolve_type";//处理类别
        public static final String RESOLVE_CONTENT = "resolve_content";//处理內容
        public static final String RESOLVE_DEPARTMENT = "resolve_department";//处理部门
        public static final String VALVE_TYPE = "valve_type";//表阀门分类
        public static final String HAPPEN_REASON = "happen_reason";//发生原因
        public static final String STATION = "station";//站点
    }

    public static class Message {
        public static final int SINGLE_NEW_TASK = 201;
        public static final int UPDATE_STATE = 202;
        public static final int APPLY_PUSH_LEADER = 203;
        public static final int DELETE_TASK = 204;
        public static final int MULTIPLE_NEW_TASK = 205;
        public static final int TRANSFORM_STATION = 206;
        public static final int DELETE_BACKGROUND_CANCEL = 207;
        public static final int DELETE_REJECT_TASK = 208;
        public static final int DELETE_HANDLER_TASK = 209;
        public static final int DELETE_ALREADY_VERIFY = 210;
        public static final int DELETE_ALREADY_DISPATCH = 211;
        public static final int DELETE_ALREADY_TRANSFORM = 212;

        public static final String TYPE = "type";
        public static final String CONTENT = "content";
    }

    public static class PersonRole {
        //表务员
        public static final String METER_TASK = "@3@";
        //司机
        public static final String DRIVER = "@6@";
    }

    public static class DispatchOperate {
        public static final int TRANSFORM = 1;
        public static final int CANCEL = 2;
    }

    public static class SelectTimeType {
        public static final int NORMAL = 0;
        public static final int BEGIN = 1;
        public static final int END = 2;
        public static final int COMPLETE = 3;
    }

    public static class VerifyResult {
        public static final int PASS = 1;
        public static final int REJECT = 2;
    }

    public static class Default {
        public static final String MATERIAL_NAME = "水表";
        public static final int METER_TYPE = 30; //6.旋翼式小口径表
        public static final int METER_PRODUCER = 1026; //宁波东海
        public static final String CALIBER = "DN15-5";
    }

    public static class NoCheckResolveResult {
        public static final int DUPLICATION_REPAIR = 2;//重复报修
        public static final int NO_NEED_RESOLVE = 4;//无需处理
        public static final int TRANSFORM_RESOLVE = 5;//转外处理
    }

}
