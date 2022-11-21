package com.dl.lib.elk.entity;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/9/30 14:33
 * Description: This is ElkReportEntity
 */
public class ElkReportEntity implements Serializable {
    //上传状态码
    private int retcode;
    //


    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }
}
