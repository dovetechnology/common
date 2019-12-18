package com.appbaselib.network;

/**
 * ===============================
 * 描    述：服务器返回数据
 * 作    者：pjw
 * 创建日期：2019/4/15 17:31
 * ===============================
 */
public class ResponseBean<T> {

    private int code;
    private T data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return (message != null) ? message : "";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
