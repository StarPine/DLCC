package com.fine.friendlycc.data.source.http.exception;

public class ApiFailException extends Exception {

    private Integer code;

    public ApiFailException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
