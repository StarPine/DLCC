package com.fine.friendlycc.event;

/**
 * Author: 彭石林
 * Time: 2021/10/18 11:05
 * Description: This is SelectMediaSourcesEvent
 */
public class SelectMediaSourcesEvent {
    private String path;
    private int type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SelectMediaSourcesEvent(String path, int type) {
        this.path = path;
        this.type = type;
    }
}
