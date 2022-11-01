package com.fine.friendlycc.entity;

/**
 * @ClassName DatingObjItemEntity
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/6/26 14:25
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class DatingObjItemEntity {
    private Integer type = -1;
    private Integer id;
    private String name;
    private String iconChecked;
    private String iconCheckno;
    private boolean isSelect = false;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconChecked() {
        return iconChecked;
    }

    public void setIconChecked(String iconChecked) {
        this.iconChecked = iconChecked;
    }

    public String getIconCheckno() {
        return iconCheckno;
    }

    public void setIconCheckno(String iconCheckno) {
        this.iconCheckno = iconCheckno;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

}