package com.dl.playfun.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/2/10 12:02
 * Description: This is GamePayEnity
 */
public class GamePayEntity {
    private String packageName;
    private String orderNumber;
    private List<String> productId;
    private String token;
    private Integer event;
    private String serverId;
    private String roleId;

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

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public GamePayEntity() {

    }

    public GamePayEntity(String packageName, String orderNumber, List<String> productId, String token, Integer event, String serverId, String roleId) {
        this.packageName = packageName;
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.token = token;
        this.event = event;
        this.serverId = serverId;
        this.roleId = roleId;
    }
}
