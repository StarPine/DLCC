package com.fine.friendlycc.event;

import com.fine.friendlycc.entity.ConfigItemEntity;

/**
 * 城市切换
 *
 * @author wulei
 */
public class CityChangeEvent {
    private ConfigItemEntity cityEntity;
    private int index;

    public CityChangeEvent() {
    }

    public CityChangeEvent(ConfigItemEntity cityEntity, int index) {
        this.cityEntity = cityEntity;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ConfigItemEntity getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(ConfigItemEntity cityEntity) {
        this.cityEntity = cityEntity;
    }
}
