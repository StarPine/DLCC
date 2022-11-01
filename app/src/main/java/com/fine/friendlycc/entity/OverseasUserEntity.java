package com.fine.friendlycc.entity;

/**
 * Author: 彭石林
 * Time: 2022/7/13 15:21
 * Description: 海外第三方登录存储登录信息
 */
public class OverseasUserEntity {
    private String name;
    private String email;
    private String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
