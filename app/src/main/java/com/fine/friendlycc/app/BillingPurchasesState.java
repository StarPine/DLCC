package com.fine.friendlycc.app;

import com.android.billingclient.api.Purchase;

/**
 * Author: 彭石林
 * Time: 2022/7/29 13:48
 * Description: 支付购买流程返回表
 */
public class BillingPurchasesState {

    //api回调状态
    private int billingResponseCode;
    //流程节点枚举类
    private BillingFlowNode billingFlowNode;
    //商品购买资料
    private Purchase purchase;


    public BillingPurchasesState(int billingResponseCode, BillingFlowNode billingFlowNode, Purchase purchase) {
        this.billingResponseCode = billingResponseCode;
        this.billingFlowNode = billingFlowNode;
        this.purchase = purchase;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getBillingResponseCode() {
        return billingResponseCode;
    }

    public void setBillingResponseCode(int billingResponseCode) {
        this.billingResponseCode = billingResponseCode;
    }

    public BillingFlowNode getBillingFlowNode() {
        return billingFlowNode;
    }

    public void setBillingFlowNode(BillingFlowNode billingFlowNode) {
        this.billingFlowNode = billingFlowNode;
    }

    public @interface BillingResponseCode {
        int SERVICE_TIMEOUT = -3;
        int FEATURE_NOT_SUPPORTED = -2;
        int SERVICE_DISCONNECTED = -1;
        int OK = 0;
        int USER_CANCELED = 1;
        int SERVICE_UNAVAILABLE = 2;
        int BILLING_UNAVAILABLE = 3;
        int ITEM_UNAVAILABLE = 4;
        int DEVELOPER_ERROR = 5;
        int ERROR = 6;
        int ITEM_ALREADY_OWNED = 7;
        int ITEM_NOT_OWNED = 8;
    }
    /**
    * @Desc TODO(流程节点枚举类)
    * @author 彭石林
    * @parame
    * @Date 2022/7/29
    */
    public enum BillingFlowNode{
        //查询商品阶段
        querySkuDetails,
        //启动购买
        launchBilling,
        //用户购买操作 可在此购买成功 or 取消支付
        purchasesUpdated,
        // 用户操作购买成功 --> 商家确认操作 需要手动确定收货（消耗这笔订单并且发货（给与用户购买奖励）） 否则 到达一定时间 自动退款
        acknowledgePurchase,
        //查询本地历史订单。未消耗的
        queryPurchaseHistory
    }
}
