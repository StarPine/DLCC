package com.dl.playfun.entity;

/**
 * @author wulei
 */
public class AdItemEntity {

    /**
     * id : 1
     * title : 新上线
     * link : https://www.baidu.com
     * img : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588854485881&di=16338c184071456d0127e10927d14d0d&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg
     */

    private int id;
    private String title;
    private String link;
    private String img;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
