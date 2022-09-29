package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/7 15:57
 * Description: 礼物背包
 */
public class GiftBagEntity {
    @SerializedName("is_first")
    private Integer isFirst;
    @SerializedName("total_coin")
    private Integer totalCoin;
    private List<giftEntity> gift;
    private List<propEntity> prop;

    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }

    public Integer getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(Integer totalCoin) {
        this.totalCoin = totalCoin;
    }

    public List<giftEntity> getGift() {
        return gift;
    }

    public void setGift(List<giftEntity> gift) {
        this.gift = gift;
    }

    public List<propEntity> getProp() {
        return prop;
    }

    public void setProp(List<propEntity> prop) {
        this.prop = prop;
    }

    public class giftEntity{
        private Integer id;
        private String name;
        private Integer money;
        private String img;
        @SerializedName("icon_id")
        private Integer iconId;
        private String link;
        private iconEntity icon;

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

        public Integer getMoney() {
            return money;
        }

        public void setMoney(Integer money) {
            this.money = money;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Integer getIconId() {
            return iconId;
        }

        public void setIconId(Integer iconId) {
            this.iconId = iconId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public iconEntity getIcon() {
            return icon;
        }

        public void setIcon(iconEntity icon) {
            this.icon = icon;
        }


        public class iconEntity {
            private Integer id;
            private String img;
            private String name;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @Override
        public String toString() {
            return "giftEntity{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", money=" + money +
                    ", img='" + img + '\'' +
                    ", iconId=" + iconId +
                    ", link='" + link + '\'' +
                    ", icon=" + icon +
                    '}';
        }
    }

    public class propEntity {
        private Integer id;
        private Integer total = 0;
        private String name;
        private String icon;
        @SerializedName("prop_type")
        private Integer propType;
        private String desc;

        public Integer getPropType() {
            return propType;
        }

        public void setPropType(Integer propType) {
            this.propType = propType;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
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

    @Override
    public String toString() {
        return "GiftBagEntity{" +
                "isFirst=" + isFirst +
                ", totalCoin=" + totalCoin +
                ", gift=" + gift +
                ", prop=" + prop +
                '}';
    }
}
