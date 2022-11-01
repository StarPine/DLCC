package com.fine.friendlycc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/13 17:57
 * Description: 通话中
 */
public class CallingInfoEntity implements Serializable {
    //钻石余额(仅男用户)
    private Integer coinBalance;
    //可通话时长(剩余分钟数)
    private Integer minutesRemaining;
    //通话拔打人个人资料
    private FromUserProfile fromUserProfile;
    //通话接收人个人资料
    private FromUserProfile toUserProfile;
    //是否已追踪0未追踪1已追踪
    private Integer collected;
    //是否已互相追踪
    private Integer collectedEachOther;
    //是否使用道具
    private Integer useProp;
    //破冰文案列表
    private SayHiList sayHiList;
    private List<CallingUnitPriceInfo> unitPriceList;
    //余额不足提示
    private String balanceNotEnoughTips;
    //通话收益提示
    private String profitTips;
    //通话挂断提示
    private String handUpTips;
    //余额不足提示分钟数
    private Integer balanceNotEnoughTipsMinutes;
    //通话收益提示间隔秒数
    private Integer profitTipsIntervalSeconds;
    private PaymentRelationBean paymentRelation;

    public Integer getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(Integer coinBalance) {
        this.coinBalance = coinBalance;
    }

    public Integer getMinutesRemaining() {
        return minutesRemaining;
    }

    public void setMinutesRemaining(Integer minutesRemaining) {
        this.minutesRemaining = minutesRemaining;
    }

    public FromUserProfile getFromUserProfile() {
        return fromUserProfile;
    }

    public void setFromUserProfile(FromUserProfile fromUserProfile) {
        this.fromUserProfile = fromUserProfile;
    }

    public FromUserProfile getToUserProfile() {
        return toUserProfile;
    }

    public void setToUserProfile(FromUserProfile toUserProfile) {
        this.toUserProfile = toUserProfile;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Integer getCollectedEachOther() {
        return collectedEachOther;
    }

    public void setCollectedEachOther(Integer collectedEachOther) {
        this.collectedEachOther = collectedEachOther;
    }

    public Integer getUseProp() {
        return useProp;
    }

    public void setUseProp(Integer useProp) {
        this.useProp = useProp;
    }

    public SayHiList getSayHiList() {
        return sayHiList;
    }

    public void setSayHiList(SayHiList sayHiList) {
        this.sayHiList = sayHiList;
    }

    public List<CallingUnitPriceInfo> getUnitPriceList() {
        return unitPriceList;
    }

    public void setUnitPriceList(List<CallingUnitPriceInfo> unitPriceList) {
        this.unitPriceList = unitPriceList;
    }

    public String getBalanceNotEnoughTips() {
        return balanceNotEnoughTips;
    }

    public void setBalanceNotEnoughTips(String balanceNotEnoughTips) {
        this.balanceNotEnoughTips = balanceNotEnoughTips;
    }

    public String getProfitTips() {
        return profitTips;
    }

    public void setProfitTips(String profitTips) {
        this.profitTips = profitTips;
    }

    public String getHandUpTips() {
        return handUpTips;
    }

    public void setHandUpTips(String handUpTips) {
        this.handUpTips = handUpTips;
    }

    public Integer getBalanceNotEnoughTipsMinutes() {
        return balanceNotEnoughTipsMinutes;
    }

    public void setBalanceNotEnoughTipsMinutes(Integer balanceNotEnoughTipsMinutes) {
        this.balanceNotEnoughTipsMinutes = balanceNotEnoughTipsMinutes;
    }

    public Integer getProfitTipsIntervalSeconds() {
        return profitTipsIntervalSeconds;
    }

    public void setProfitTipsIntervalSeconds(Integer profitTipsIntervalSeconds) {
        this.profitTipsIntervalSeconds = profitTipsIntervalSeconds;
    }

    public PaymentRelationBean getPaymentRelation() {
        return paymentRelation;
    }

    public void setPaymentRelation(PaymentRelationBean paymentRelation) {
        this.paymentRelation = paymentRelation;
    }

    public class PaymentRelationBean {
        private int payerUserId;
        private int payeeUserId;
        private String payerImId;
        private String payeeImId;

        public int getPayerUserId() {
            return payerUserId;
        }

        public void setPayerUserId(int payerUserId) {
            this.payerUserId = payerUserId;
        }

        public int getPayeeUserId() {
            return payeeUserId;
        }

        public void setPayeeUserId(int payeeUserId) {
            this.payeeUserId = payeeUserId;
        }

        public String getPayerImId() {
            return payerImId;
        }

        public void setPayerImId(String payerImId) {
            this.payerImId = payerImId;
        }

        public String getPayeeImId() {
            return payeeImId;
        }

        public void setPayeeImId(String payeeImId) {
            this.payeeImId = payeeImId;
        }
    }

    public class FromUserProfile {
        private Integer id;
        private String nickname;
        private String avatar;
        private Integer sex;
        private Integer isVip;
        private Integer certification;
        private Integer cityId;
        private String cityName;
        private Integer age;
        private String constellation;
        private String occupation;
        private Integer occupationId;
        private String gameChannel;
        //当前用户IM id
        private String imId;

        public String getImId() {
            return imId;
        }

        public void setImId(String imId) {
            this.imId = imId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public Integer getIsVip() {
            return isVip;
        }

        public void setIsVip(Integer isVip) {
            this.isVip = isVip;
        }

        public Integer getCertification() {
            return certification;
        }

        public void setCertification(Integer certification) {
            this.certification = certification;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public Integer getOccupationId() {
            return occupationId;
        }

        public void setOccupationId(Integer occupationId) {
            this.occupationId = occupationId;
        }

        public String getGameChannel() {
            return gameChannel;
        }

        public void setGameChannel(String gameChannel) {
            this.gameChannel = gameChannel;
        }
    }

    public class SayHiList {
        private Integer currentPage;
        private Integer perPage;
        private Integer total;
        private List<SayHiEntity> data;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getPerPage() {
            return perPage;
        }

        public void setPerPage(Integer perPage) {
            this.perPage = perPage;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<SayHiEntity> getData() {
            return data;
        }

        public void setData(List<SayHiEntity> data) {
            this.data = data;
        }
    }

    public class SayHiEntity {
        private Integer id;
        private String message;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class CallingUnitPriceInfo {
        private Integer fromMinute;
        private BigDecimal unitPrice;

        public Integer getFromMinute() {
            return fromMinute;
        }

        public void setFromMinute(Integer fromMinute) {
            this.fromMinute = fromMinute;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
