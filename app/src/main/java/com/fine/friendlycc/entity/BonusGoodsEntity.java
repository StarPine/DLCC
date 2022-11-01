package com.fine.friendlycc.entity;

/**
 * Author: 彭石林
 * Time: 2021/8/10 14:42
 * Description: This is BonusGoodsEntity
 */
public class BonusGoodsEntity {
    private Integer id;
    private String title;
    private String img;
    private Integer money;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
