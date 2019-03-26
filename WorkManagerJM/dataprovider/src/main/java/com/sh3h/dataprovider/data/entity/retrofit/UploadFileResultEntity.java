package com.sh3h.dataprovider.data.entity.retrofit;

/**
 * 上传文件返回结果
 */
public class UploadFileResultEntity {
    private String url;// '文件url'，
    private String fileType; //'文件类型',
    private int fileSize;// '文件大小',
    private String fileHash;// '文件hash',
    private String originName;// '原始文件名'

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

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
