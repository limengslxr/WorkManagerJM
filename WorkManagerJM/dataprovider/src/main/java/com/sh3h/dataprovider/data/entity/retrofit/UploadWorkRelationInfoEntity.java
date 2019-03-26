package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * 上传文件关联关系
 */

public class UploadWorkRelationInfoEntity {
    private String url;// '文件url'，
    private String fileType;// '文件类型',
    private int fileSize;// '文件大小',
    private String fileHash;// '文件hash'

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
