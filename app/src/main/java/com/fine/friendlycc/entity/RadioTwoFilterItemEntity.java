package com.fine.friendlycc.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description：游戏和地区二级筛选bean
 * @Author： liaosf
 * @Date： 2022/1/13 18:08
 * 修改备注：
 */
public class RadioTwoFilterItemEntity implements Serializable {

    /**
     * id : 1
     * name : 喵遊
     * city : [{"id":1,"name":"台北市"},{"id":2,"name":"基隆市"}]
     */

    private int id;
    private String name;
    private List<CityBean> city;

    public RadioTwoFilterItemEntity(int id, String name, List<CityBean> city) {
        this.id = id;
        this.name = name;
        this.city = city;
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

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    public static class CityBean {
        /**
         * id : 1
         * name : 台北市
         */

        private int id;
        private String name;

        public CityBean(int id, String name) {
            this.id = id;
            this.name = name;
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
