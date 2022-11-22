package com.fine.friendlycc.event;

import com.fine.friendlycc.bean.ConfigItemBean;

/**
 * 城市切换
 *
 * @author wulei
 */
public class CityChangeEvent {
    private ConfigItemBean cityEntity;
    private int index;

    public CityChangeEvent() {
    }

    public CityChangeEvent(ConfigItemBean cityEntity, int index) {
        this.cityEntity = cityEntity;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ConfigItemBean getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(ConfigItemBean cityEntity) {
        this.cityEntity = cityEntity;
    }
}