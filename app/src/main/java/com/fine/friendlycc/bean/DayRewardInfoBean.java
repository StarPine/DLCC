package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 修改备注：每日奖励数据
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/17 15:29
 */
public class DayRewardInfoBean implements Serializable {
    /**
     * now : [{"type":"video_card","num":1},{"type":"coin","num":200},{"type":"vip_coin","num":200}]
     * next : 500
     */

    @SerializedName("next")
    private int next;
    @SerializedName("nextCard")
    private int nextCard;
    @SerializedName("now")
    private List<NowBean> now;

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<NowBean> getNow() {
        return now;
    }

    public int getNextCard() {
        return nextCard;
    }

    public void setNextCard(int nextCard) {
        this.nextCard = nextCard;
    }

    public void setNow(List<NowBean> now) {
        this.now = now;
    }

    public static class NowBean implements Serializable {
        /**
         * type : video_card
         * num : 1
         */

        @SerializedName("type")
        private String type;
        @SerializedName("num")
        private int num;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}