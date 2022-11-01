package com.fine.friendlycc.entity;

import java.io.Serializable;

/**
 * 修改备注：专属搭讪信息
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/12 11:08
 */
public class ExclusiveAccostInfoEntity implements Serializable {
    /**
     * id : 2
     * type : 1
     * content : 你好13425435
     */
    private int len;
    private int type;
    private String content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
