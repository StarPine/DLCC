package com.dl.playfun.event;


import com.dl.playfun.entity.ChooseAreaItemEntity;

/**
 * Author: 彭石林
 * Time: 2022/7/7 18:03
 * Description: This is ItemChooseAreaEvent
 */
public class ItemChooseAreaEvent {
    private ChooseAreaItemEntity chooseAreaItemEntity;

    public ItemChooseAreaEvent(ChooseAreaItemEntity chooseAreaItemEntity) {
        this.chooseAreaItemEntity = chooseAreaItemEntity;
    }

    public ChooseAreaItemEntity getChooseAreaItemEntity() {
        return chooseAreaItemEntity;
    }

    public void setChooseAreaItemEntity(ChooseAreaItemEntity chooseAreaItemEntity) {
        this.chooseAreaItemEntity = chooseAreaItemEntity;
    }
}
