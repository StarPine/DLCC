package com.dl.playfun.entity;

import java.io.Serializable;
import java.util.List;

public class LikeRecommendEntity implements Serializable {
    private int price;
    private List<RecommendUserEntity> user;

    public LikeRecommendEntity(int price, List<RecommendUserEntity> user) {
        this.price = price;
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<RecommendUserEntity> getUser() {
        return user;
    }

    public void setUser(List<RecommendUserEntity> user) {
        this.user = user;
    }
}
