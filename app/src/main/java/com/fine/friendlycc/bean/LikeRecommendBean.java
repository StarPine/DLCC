package com.fine.friendlycc.bean;

import java.io.Serializable;
import java.util.List;

public class LikeRecommendBean implements Serializable {
    private int price;
    private List<RecommendUserBean> user;

    public LikeRecommendBean(int price, List<RecommendUserBean> user) {
        this.price = price;
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<RecommendUserBean> getUser() {
        return user;
    }

    public void setUser(List<RecommendUserBean> user) {
        this.user = user;
    }
}