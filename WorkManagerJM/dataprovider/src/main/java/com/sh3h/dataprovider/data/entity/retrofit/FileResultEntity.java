package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * Created by LiMeng on 2017/4/11.
 */

public class FileResultEntity {
    private int fileType;//number 0：图片；1：语音；
    private String fileUrl;//'地址'
    private String fileName;//'文件名'

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
