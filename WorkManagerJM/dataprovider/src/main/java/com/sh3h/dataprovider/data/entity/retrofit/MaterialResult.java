package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2017/6/6.
 */
public class MaterialResult {
    private String materialNo;//材料编号
    private String type;//材料类型
    private String name;//材料名称
    private String spec;//材料规格
    private String unit;//单位
    private double price;//单价
    private int status;//状态 -1.作废；0.正常
    private String extend;//

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
