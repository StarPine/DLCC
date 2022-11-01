package com.fine.friendlycc.entity;

/**
 * 主题配置项
 *
 * @author wulei
 */
public class ThemeConfigItemEntity {
    private boolean isChoose = false;
    private Integer id;
    private String name;
    private String icon;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
