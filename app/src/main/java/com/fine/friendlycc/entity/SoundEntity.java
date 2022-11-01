package com.fine.friendlycc.entity;

/**
 * Author: 彭石林
 * Time: 2021/10/28 15:39
 * Description: This is SoundEntity
 */
public class SoundEntity {
    private Integer id;
    private String type;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
