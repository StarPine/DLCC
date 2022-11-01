package com.fine.friendlycc.event;

import com.fine.friendlycc.entity.ConfigItemEntity;

/**
 * 城市切换
 *
 * @author wulei
 */
public class CityChangeEvent {
    private ConfigItemEntity cityEntity;

    public CityChangeEvent() {
    }

    public CityChangeEvent(ConfigItemEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    public ConfigItemEntity getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(ConfigItemEntity cityEntity) {
        this.cityEntity = cityEntity;
    }
}
