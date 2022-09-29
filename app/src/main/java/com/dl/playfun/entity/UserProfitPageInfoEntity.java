package com.dl.playfun.entity;

import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2021/12/6 15:21
 * Description: This is UserProfitPageInfoEntity
 */
public class UserProfitPageInfoEntity {
    //主键
    private Integer id;
    //用户id
    private Integer userId;
    //收益来源用户ID
    private Integer fromUserId;
    //收益来源用户信息
    private FromUserInfoDTO fromUserInfo;
    //展示信息
    private String message;
    //金额
    private BigDecimal totalAmount;
   //是否使用道具
    private Integer useProp;
    //收益时间
    private String createAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public FromUserInfoDTO getFromUserInfo() {
        return fromUserInfo;
    }

    public void setFromUserInfo(FromUserInfoDTO fromUserInfo) {
        this.fromUserInfo = fromUserInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getUseProp() {
        return useProp;
    }

    public void setUseProp(Integer useProp) {
        this.useProp = useProp;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }



    public class FromUserInfoDTO {
        private Integer id;
        private String nickname;
        private String avatar;

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
    }
}
