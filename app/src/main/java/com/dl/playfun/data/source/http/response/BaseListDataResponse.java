package com.dl.playfun.data.source.http.response;


/**
 * @author wulei
 */
public class BaseListDataResponse<T> extends BaseResponse {

    private PageData<T> data;

    public PageData<T> getData() {
        return data;
    }

    public void setData(PageData<T> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data == null || data.getData() == null || data.getData().isEmpty();
    }
}