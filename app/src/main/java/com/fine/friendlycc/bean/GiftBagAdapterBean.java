package com.fine.friendlycc.bean;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/7 17:47
 * Description: 礼物列表适配器bean
 */
public class GiftBagAdapterBean {
    private  int idx;
    private List<GiftBagBean.giftEntity> giftBagEntity;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public List<GiftBagBean.giftEntity> getGiftBagEntity() {
        return giftBagEntity;
    }

    public void setGiftBagEntity(List<GiftBagBean.giftEntity> giftBagEntity) {
        this.giftBagEntity = giftBagEntity;
    }

    public GiftBagAdapterBean(int idx, List<GiftBagBean.giftEntity> giftBagEntity) {
        this.idx = idx;
        this.giftBagEntity = giftBagEntity;
    }

    @Override
    public String toString() {
        return "GiftBagAdapterBean{" +
                "idx=" + idx +
                ", giftBagEntity=" + giftBagEntity +
                '}';
    }
}