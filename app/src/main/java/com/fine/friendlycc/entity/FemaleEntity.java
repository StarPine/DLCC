package com.fine.friendlycc.entity;

import java.util.List;

/**
 * @Description：
 * @Author： liaosf
 * @Date： 2021/12/18 18:23
 * 修改备注：
 */
public class FemaleEntity {
    /**
     * is_sign_in : 1
     * day_number : 1
     * female_config : [10,20,30,10,10,10,30]
     * task : [{"id":3,"task_type":1,"name":"語音聊天1次","icon":"images/語音聊天@3x.png","link":"home","slug":"voice","reward_type":1,"reward_value":"5.00","status":0},{"id":17,"task_type":2,"name":"主動搭訕1次","icon":"images/語音聊天@3x.png","link":"home","slug":"dayAccost","reward_type":1,"reward_value":"0.50","status":0}]
     * code : O4MOG
     * total_money : 41866.00
     * good_bag_url : https://m.joy-mask.com
     * is_first_sign : 0
     */

    private int is_sign_in;
    private int day_number;
    private String code;
    private String total_money;
    private String good_bag_url;
    private int is_first_sign;
    private List<Integer> female_config;
    private List<TaskBean> task;

    public int getIs_sign_in() {
        return is_sign_in;
    }

    public void setIs_sign_in(int is_sign_in) {
        this.is_sign_in = is_sign_in;
    }

    public int getDay_number() {
        return day_number;
    }

    public void setDay_number(int day_number) {
        this.day_number = day_number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getGood_bag_url() {
        return good_bag_url;
    }

    public void setGood_bag_url(String good_bag_url) {
        this.good_bag_url = good_bag_url;
    }

    public int getIs_first_sign() {
        return is_first_sign;
    }

    public void setIs_first_sign(int is_first_sign) {
        this.is_first_sign = is_first_sign;
    }

    public List<Integer> getFemale_config() {
        return female_config;
    }

    public void setFemale_config(List<Integer> female_config) {
        this.female_config = female_config;
    }

    public List<TaskBean> getTask() {
        return task;
    }

    public void setTask(List<TaskBean> task) {
        this.task = task;
    }

    public static class TaskBean {
        /**
         * id : 3
         * task_type : 1
         * name : 語音聊天1次
         * icon : images/語音聊天@3x.png
         * link : home
         * slug : voice
         * reward_type : 1
         * reward_value : 5.00
         * status : 0
         */

        private int id;
        private int task_type;
        private String name;
        private String icon;
        private String link;
        private String slug;
        private int reward_type;
        private String reward_value;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTask_type() {
            return task_type;
        }

        public void setTask_type(int task_type) {
            this.task_type = task_type;
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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public int getReward_type() {
            return reward_type;
        }

        public void setReward_type(int reward_type) {
            this.reward_type = reward_type;
        }

        public String getReward_value() {
            return reward_value;
        }

        public void setReward_value(String reward_value) {
            this.reward_value = reward_value;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
