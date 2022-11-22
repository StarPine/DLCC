package com.fine.friendlycc.event;


import com.fine.friendlycc.bean.ChooseAreaItemBean;

/**
 * Author: 彭石林
 * Time: 2022/7/7 18:03
 * Description: This is ItemChooseAreaEvent
 */
public class ItemChooseAreaEvent {
    private ChooseAreaItemBean chooseAreaItemEntity;

    public ItemChooseAreaEvent(ChooseAreaItemBean chooseAreaItemEntity) {
        this.chooseAreaItemEntity = chooseAreaItemEntity;
    }

    public ChooseAreaItemBean getChooseAreaItemEntity() {
        return chooseAreaItemEntity;
    }

    public void setChooseAreaItemEntity(ChooseAreaItemBean chooseAreaItemEntity) {
        this.chooseAreaItemEntity = chooseAreaItemEntity;
    }
}