package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class CreateOrderBean {
    @SerializedName("order_number")
    private String orderNumber;
    private Integer money;
    private Integer actual_value;

    public Integer getActual_value() {
        return actual_value;
    }

    public void setActual_value(Integer actual_value) {
        this.actual_value = actual_value;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}