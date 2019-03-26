package com.sh3h.ipc.module;


import android.os.Parcel;
import android.os.Parcelable;

public class MyModule implements Parcelable {
    public static final String PACKAGE_NAME = "packageName";
    public static final String ACTIVITY_NAME = "activityName";
    public static final String DATA = "data";
    public static final String SEPARATOR = "#";
    public static final String COUNT = "count";
    public static final String MQTT = "mqtt"; // @deprecated
    public static final String CLEAR_CACHE = "clearCache";
    public static final String RESTORE_FACTORY = "restoreFactory";
    public static final String PHOTO_QUALITY = "photoQuality";
    public static final String OUTER_NETWORK = "outerNetwork";
    public static final String GPS_NOT_OPENED = "gpsNotOpened";
    public static final String LOGOUT_SUB_SYSTEM = "logoutSubSystem";
    public static final String LOGOUT_MAIN_SYSTEM = "logoutMainSystem";
    public static final String SUB_BOUND = "subBound";
    public static final String PUSH_MESSAGE = "pushMessage";
    public static final String POPUP_MESSAGE = "popupMessage";

    /**
     * for example
     * {
     * "packageName": "com.sh3h.meterreading",
     * "activityName": "com.sh3h.meterreading.ui.xxx.xxx",
     * "data" : [
     * "count#0",
     * "mqtt#xxxxxxx",
     * "clearCache",
     * "restoreFactory",
     * "photoQuality#normal", // normal/high
     * "outerNetwork#true", // true: outer, false: inner
     * "gpsNotOpened",
     * "logoutSubSystem",
     * "logoutMainSystem"
     * "subBound"
     * "pushMessage#xxx",
     * "popupMessage#xxx"
     * ]
     * }
     */

    private String info; // json

    public MyModule() {
    }

    public MyModule(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
    }

    //添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
    public static final Parcelable.Creator<MyModule> CREATOR = new Parcelable.Creator<MyModule>() {
        @Override
        public MyModule createFromParcel(Parcel source) {//从Parcel中读取数据，返回person对象
            return new MyModule(source.readString());
        }

        @Override
        public MyModule[] newArray(int size) {
            return new MyModule[size];
        }
    };
}
