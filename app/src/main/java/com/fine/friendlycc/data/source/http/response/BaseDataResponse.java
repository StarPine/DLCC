package com.fine.friendlycc.data.source.http.response;

/**
 * @author wulei
 */
public class BaseDataResponse<T> extends BaseResponse {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isDataEmpty() {
        return data == null;
    }
}