package com.dl.playfun.data.source.http.response;

/**
 * @author wulei
 */
public class BaseResponse {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_INFO = "info";

    private Integer code;
    private String status;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return STATUS_SUCCESS.equals(status);
    }

    public boolean isFail() {
        return STATUS_FAIL.equals(status);
    }

    public boolean isError() {
        return STATUS_ERROR.equals(status);
    }

    public boolean isInfo() {
        return STATUS_INFO.equals(status);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}