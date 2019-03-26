package com.sh3h.localprovider.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by LiMeng on 2017/6/5.
 */
@Entity
public class Material {
    @Id(autoincrement = true)
    private Long id;//主键
    private String materialNo;//材料编号
    private String type;//材料类型
    private String name;//材料名称
    private String spec;//材料规格
    private String unit;//单位
    private double price;//单价
    private int status;//状态 -1.作废；0.正常
    private String extend;//
    @Generated(hash = 2135497800)
    public Material(Long id, String materialNo, String type, String name,
            String spec, String unit, double price, int status, String extend) {
        this.id = id;
        this.materialNo = materialNo;
        this.type = type;
        this.name = name;
        this.spec = spec;
        this.unit = unit;
        this.price = price;
        this.status = status;
        this.extend = extend;
    }
    @Generated(hash = 1176792654)
    public Material() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMaterialNo() {
        return this.materialNo;
    }
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpec() {
        return this.spec;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getExtend() {
        return this.extend;
    }
    public void setExtend(String extend) {
        this.extend = extend;
    }
}
