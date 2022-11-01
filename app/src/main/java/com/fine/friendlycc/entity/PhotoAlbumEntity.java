package com.fine.friendlycc.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/10/22 16:40
 * Description: This is PhotoAlbumEntity
 */
public class PhotoAlbumEntity {
    private String nickname;
    private Integer sex;
    private Integer is_vip;
    private Integer certification;
    private List<PhotoAlbumItemEntity> img;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(Integer is_vip) {
        this.is_vip = is_vip;
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
    }

    public List<PhotoAlbumItemEntity> getImg() {
        return img;
    }

    public void setImg(List<PhotoAlbumItemEntity> img) {
        this.img = img;
    }
}
