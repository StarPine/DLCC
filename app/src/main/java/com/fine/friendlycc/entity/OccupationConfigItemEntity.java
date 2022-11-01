package com.fine.friendlycc.entity;

import java.util.List;

/**
 * 职业配置项
 *
 * @author wulei
 */
public class OccupationConfigItemEntity {
    private boolean isChoose = false;

    /**
     * id : 1
     * name : 信息技术
     * item : [{"id":11,"name":"互联网"},{"id":12,"name":"IT"},{"id":13,"name":"通讯"},{"id":13,"name":"电信运营"},{"id":14,"name":"网络游戏"}]
     */

    private int id;
    private String name;
    private List<ItemEntity> item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemEntity> getItem() {
        return item;
    }

    public void setItem(List<ItemEntity> item) {
        this.item = item;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public class ItemEntity {
        /**
         * id : 11
         * name : 互联网
         */

        private int id;
        private String name;
        private boolean isChoose = false;

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
