package com.fine.friendlycc.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName LocalGooglePayCache
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/13 14:45
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class LocalGooglePayCache implements Serializable {
    private String packageName;
    private String orderNumber;
    private List<String> productId;
    private String token;
    private int type;
    private Integer userId;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}