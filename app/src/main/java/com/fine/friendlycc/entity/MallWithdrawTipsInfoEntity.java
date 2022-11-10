package com.fine.friendlycc.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/7/20 11:38
 * 修改备注：
 */
public class MallWithdrawTipsInfoEntity implements Serializable {

    /**
     * title : 你可通過水晶兌換
     * goodsList : [{"quantity":200,"profits":9999},{"quantity":2000,"profits":19999}]
     */

    private String title;//标题
    private List<GoodsListBean> goodsList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public static class GoodsListBean implements Serializable {
        /**
         * quantity : 200
         * profits : 9999
         */

        private int quantity;//数量
        private int profits;//所需水晶

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getProfits() {
            return profits;
        }

        public void setProfits(int profits) {
            this.profits = profits;
        }

    }
}
