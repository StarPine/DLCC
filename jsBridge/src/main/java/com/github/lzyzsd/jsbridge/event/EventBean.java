package com.github.lzyzsd.jsbridge.event;

/**
 * Created by linqiye on 11/4/16.
 */
public class EventBean {
    String name;
    String params;

    public EventBean() {
    }

    public EventBean(String name) {
        this.name = name;
    }

    public EventBean(String name, String params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        return params;
    }


}
