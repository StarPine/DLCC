package com.fine.friendlycc.data.source.http.exception;

public class ApiErrorException extends RequestException {

    private Integer code;

    public ApiErrorException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

}
