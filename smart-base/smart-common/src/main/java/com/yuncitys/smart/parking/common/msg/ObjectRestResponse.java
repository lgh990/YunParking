package com.yuncitys.smart.parking.common.msg;

/**
 * Created by Smart on 2017/6/11.
 */
public class ObjectRestResponse<T> extends BaseResponse {

    T data;

    public ObjectRestResponse data(T data) {
        this.setData(data);
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ObjectRestResponse ok(Object data) {
        return new ObjectRestResponse<Object>().data(data);
    }

    public static ObjectRestResponse ok() {
        return new ObjectRestResponse<Object>();
    }
    public ObjectRestResponse BaseResponse(int status, String message) {
        this.setMessage(message);
        this.setStatus(status);
        return this;
    }

}
