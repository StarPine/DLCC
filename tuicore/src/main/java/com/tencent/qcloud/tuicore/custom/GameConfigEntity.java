package com.tencent.qcloud.tuicore.custom;

import java.io.Serializable;

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
