package com.dl.playfun.entity;

import java.io.Serializable;

/**
 * @Description：
 * @Author： liaosf
 * @Date： 2022/1/14 18:57
 * 修改备注：
 */
public class GameConfigEntity implements Serializable {

    /**
     * id : 1
     * url : images/320220112-153030.png
     */

    private String id;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
