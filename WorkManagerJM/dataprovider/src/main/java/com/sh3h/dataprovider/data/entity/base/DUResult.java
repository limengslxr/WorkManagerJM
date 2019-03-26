package com.sh3h.dataprovider.data.entity.base;

/**
 * Created by dengzhimin on 2016/5/31.
 */
public class DUResult<T> {
    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = -1;

    private int code;
    private int statusCode;
    private String message;
    private T data;

    public DUResult(T data) {
        this.data = data;
    }

    public DUResult(int code, int statusCode, String message, T data) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int Code) {
        this.code = Code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.statusCode = StatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String Message) {
        this.message = Message;
    }

    public T getData() {
        return data;
    }

    public void setData(T Data) {
        this.data = Data;
    }
}
