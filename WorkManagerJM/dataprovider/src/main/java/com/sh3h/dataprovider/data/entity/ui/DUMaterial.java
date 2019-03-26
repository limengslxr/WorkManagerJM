package com.sh3h.dataprovider.data.entity.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiMeng on 2017/6/5.
 */

public class DUMaterial implements Parcelable{
    private String materialNo;//材料编号
    private String type;//材料类型
    private String name;//材料名称
    private String spec;//材料规格
    private String unit;//单位
    private double price;//单价
    private double count;//数量

    public DUMaterial() {
    }

    protected DUMaterial(Parcel in) {
        materialNo = in.readString();
        type = in.readString();
        name = in.readString();
        spec = in.readString();
        unit = in.readString();
        price = in.readDouble();
        count = in.readDouble();
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public static final Creator<DUMaterial> CREATOR = new Creator<DUMaterial>() {
        @Override
        public DUMaterial createFromParcel(Parcel in) {
            return new DUMaterial(in);
        }

        @Override
        public DUMaterial[] newArray(int size) {
            return new DUMaterial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(materialNo);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(spec);
        dest.writeString(unit);
        dest.writeDouble(price);
        dest.writeDouble(count);
    }
}
