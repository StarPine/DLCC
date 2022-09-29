package com.dl.playfun.data.source.http.exception;

public class RequestException extends Exception {
    private Integer code;

    public RequestException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public RequestException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
