package com.sh3h.dataprovider.data.entity.retrofit;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by LiMeng on 2017/6/5.
 */

public class MaterialInfo implements Serializable {
    @Expose
    private String materialNo;
    @Expose
    private String type;//材料类型
    @Expose
    private String name;//材料名称
    @Expose
    private String spec;//材料规格
    @Expose
    private String unit;//单位
    @Expose
    private double price;//单价
    @Expose
    private double count;//数量

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
}
