package com.fine.friendlycc.bean;

/**
 * Author: 彭石林
 * Time: 2021/12/2 18:15
 * Description: This is BubbleBean
 */
public class BubbleBean {
    private Integer status;
    private String message;

    public Integer getStatus() {
        if (status == null) {
            return 0;
        }
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}