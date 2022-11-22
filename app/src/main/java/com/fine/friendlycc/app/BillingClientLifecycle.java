package com.fine.friendlycc.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.StringUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/7/21 17:34
 * Description: 谷歌支付工具类 建议在AppLocation中进行初始 跟随 声明周期
 */
public class BillingClientLifecycle implements LifecycleObserver, BillingClientStateListener,PurchasesUpdatedListener {
    private static final String TAG = "BillingClientLifecycle";
    private static volatile BillingClientLifecycle INSTANCE;
    private final Application app;

    private BillingClient billingClient;

    //重新连接间隔时间 10 秒
    private long Connection_retry_time = 10;

    //页面创建第一次连接时间
    private long mqttConnectCreateLastTime = 0L;

    //判断当前谷歌商店服务是否处于连接成功
    private boolean connectionSuccessful = false;

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }
    //连接成功回调
    public SingleLiveEvent<Boolean> CONNECTION_SUCCESS = new SingleLiveEvent<>();
    //支付购买成功流程回调
    public SingleLiveEvent<BillingPurchasesState> PAYMENT_SUCCESS = new SingleLiveEvent<>();
    //支付购买异常流程回调
    public SingleLiveEvent<BillingPurchasesState> PAYMENT_FAIL = new SingleLiveEvent<>();
    //查询本地订单消耗
    public SingleLiveEvent<BillingPurchasesState> PurchaseHistory = new SingleLiveEvent<>();

    private BillingClientLifecycle(Application app) {
        this.app = app;
    }

    public static BillingClientLifecycle getInstance(Application app) {
        if (INSTANCE == null) {
            synchronized (BillingClientLifecycle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BillingClientLifecycle(app);
                }
            }
        }
        return INSTANCE;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.d(TAG, "ON_CREATE");
        mqttConnectCreateLastTime = System.currentTimeMillis() / 1000;
        //Lifecycle声明周期监听 页面创建 初始化建立谷歌连接
        initBillingClient();
    }

    public void initBillingClient() {
        billingClient = BillingClient.newBuilder(app)
                .setListener(this)
                .enablePendingPurchases() // Not used for subscriptions.
                .build();
        //判断是否连接
        if (!billingClient.isReady()) {
            billingClient.startConnection(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        //页面再次可见时再次进入判断。防止弱引用持有回收
        //页面处于可见状态最后依次连接时间
        long mqttConnectResumeLastTime = System.currentTimeMillis() / 1000;
        if(mqttConnectResumeLastTime != 0L && mqttConnectCreateLastTime != 0L){
            //页面可见时间 - 页面创建时间 < 10秒。不在进行连接。避免重复创建
            if(mqttConnectResumeLastTime - mqttConnectCreateLastTime  <= 10){
                return;
            }
        }
        //页面再次可见时再次进入判断。防止弱引用持有回收
        if(billingClient == null){
            initBillingClient();
        }else {
            //判断是否连接
            if(!billingClient.isReady()){
                billingClient.startConnection(this);
            }
        }
    }

    /*******************************谷歌连接回调************************************/
    @SuppressLint("CheckResult")
    @Override
    public void onBillingServiceDisconnected() {
        //断开连接
        connectionSuccessful = false;
        //间隔 N 秒再次发送连接
        Observable.timer(Connection_retry_time, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    billingClient.startConnection(this);
                });
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        //连接成功
        if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
            // The BillingClient is ready. You can query purchases here.
            connectionSuccessful = true;
            CONNECTION_SUCCESS.postValue(true);
        }else{
            CONNECTION_SUCCESS.postValue(false);
        }
    }
    /**
    * @Desc TODO(查询本地谷歌商店购买订单记录并且上报)
    * @author 彭石林
    * @parame [SkuType]
    * @return void
    * @Date 2022/7/29
    */
    public void queryPurchaseHistoryAsync(String SkuType){
        billingClient.queryPurchaseHistoryAsync(SkuType, (billingResult, purchaseHistoryRecordList) -> {
            //开始连接进入以下：
            if (purchaseHistoryRecordList != null) {
                List<Map> purchaseList = new ArrayList<>();
                Date endTime = new Date();
                Date beginTime = ApiUitl.toDayMinTwo(endTime);
                for (PurchaseHistoryRecord purchaseHistoryRecord : purchaseHistoryRecordList) {
                    try {
                        Purchase purchase = new Purchase(purchaseHistoryRecord.getOriginalJson(), purchaseHistoryRecord.getSignature());
                        Date date = new Date();
                        date.setTime(purchase.getPurchaseTime());
                        if (purchase.isAcknowledged()) {
                            if (ApiUitl.belongCalendar(date, beginTime, endTime)) {
                                String pack = purchase.getPackageName();
                                if (StringUtil.isEmpty(pack)) {
                                    pack = BuildConfig.APPLICATION_ID;
                                }
                                Map<String, Object> maps = new HashMap<>();
                                maps.put("orderId", purchase.getOrderId());
                                maps.put("token", purchase.getPurchaseToken());
                                maps.put("sku", purchase.getSkus().toString());
                                maps.put("package", pack);
                                purchaseList.add(maps);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                UserDataBean userDataEntity = ConfigManager.getInstance().getAppRepository().readUserData();
                if (userDataEntity == null || userDataEntity.getId() == null) {
                    return;
                }
                if (!ObjectUtils.isEmpty(purchaseList) && purchaseList.size() > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("data", purchaseList);
                    ConfigManager.getInstance().getAppRepository().repoetLocalGoogleOrder(map)
                            .compose(RxUtils.exceptionTransformer())
                            .compose(RxUtils.schedulersTransformer())
                            .subscribe(new BaseObserver<BaseResponse>() {
                                @Override
                                public void onSuccess(BaseResponse baseResponse) {
                                }
                                @Override
                                public void onComplete() {
                                }
                            });
                }
            }
        });
    }

    /**
    * @Desc TODO(Google Play 会调用 onPurchasesUpdated()，以将购买操作的结果传送给实现 PurchasesUpdatedListener 接口的)
    * @author 彭石林
    * @parame [billingResult, list]
    * @return void
    * @Date 2022/7/21
    */
    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> listPurchase) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && ObjectUtils.isNotEmpty(listPurchase) ) {
            for (final Purchase purchase : listPurchase) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    // Acknowledge purchase and grant the item to the user
                    Log.i(TAG, "Purchase success");
                    //确认购买交易，不然三天后会退款给用户 而且此时也没有支付成功
                    if (!purchase.isAcknowledged()) {
                        try {
                            CCApplication.instance().logEvent(AppsFlyerEvent.pay_success);
                        }catch (Exception ignored) {

                        }
                        PAYMENT_SUCCESS.postValue(getBillingPurchasesState(0,BillingPurchasesState.BillingFlowNode.purchasesUpdated,purchase));
                        acknowledgePurchase(purchase);
                    }
                } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                    //需要用户确认 可能是非正常操作处理购买
                    Log.i(TAG, "Purchase pending,need to check");
                }
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            //用户取消
            Log.i(TAG, "Purchase cancel");
            PAYMENT_FAIL.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.purchasesUpdated,null));
        } else {
            //支付错误
            Log.i(TAG, "Pay result error,code=" + billingResult.getResponseCode() + "\nerrorMsg=" + billingResult.getDebugMessage());
            PAYMENT_FAIL.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.purchasesUpdated,null));
        }
    }

    /*******************************外部购买商品掉用************************************/
    /**
    * @Desc TODO(根据类型查询可供购买的商品--这里改成同步返回)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/7/21
    */
    public List<SkuDetails> querySkuDetailsAsync(String type,List<String> SkuIdList) throws InterruptedException {
        AtomicReference<List<SkuDetails>> skuDetailsList = new AtomicReference<>();
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setType(type)
                .setSkusList(SkuIdList)
                .build();
        //定义线程等待。改成同步回调
        final CountDownLatch parserCtl = new CountDownLatch(1);
        billingClient.querySkuDetailsAsync(params, (billingResult, skuDetails) -> {
            //执行api成功
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                skuDetailsList.set(skuDetails);
            }
            parserCtl.countDown();
        });
        parserCtl.await();
        return skuDetailsList.get();
    }
    /**
     * @Desc TODO(根据类型查询可供购买的商品--这里改成内部回调)
     * @author 彭石林
     * @parame []
     * @return void
     * @Date 2022/7/21
     */
    public void querySkuDetailsAsync(SkuDetailsParams.Builder params, SkuDetailsResponseListener skuDetailsResponseListener) {
        billingClient.querySkuDetailsAsync(params.build(), skuDetailsResponseListener);
    }
    /**
    * @Desc TODO(查询商品是否存在并开始购买)
    * @author 彭石林
    * @parame [params, activity]
    * @Date 2022/7/21
    */
    public void  querySkuDetailsLaunchBillingFlow(SkuDetailsParams.Builder params , Activity activity,String orderNumber){
        Log.e(TAG,"查询商品是否存在并开始购买："+params.toString());
        long startTime = System.currentTimeMillis() /1000;
        //查询商品是否存在
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, skuDetailsList) -> {
            long endTime = System.currentTimeMillis() /1000;
            Log.e(TAG,"查询商品时间消耗："+(endTime-startTime));
            Log.e(TAG,"查询商品："+billingResult.getResponseCode()+"===="+ (skuDetailsList != null ? skuDetailsList.size() : 0));
            //执行api成功
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //成功找到商品
                if (ObjectUtils.isNotEmpty(skuDetailsList)) {
                    PAYMENT_SUCCESS.postValue(getBillingPurchasesState(0,BillingPurchasesState.BillingFlowNode.querySkuDetails,null));
                    for (SkuDetails skuDetails : skuDetailsList) {
                        String sku = skuDetails.getSku();
                        String price = skuDetails.getPrice();
                        Log.i(TAG, "Sku=" + sku + ",price=" + price);
                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .setObfuscatedAccountId(ConfigManager.getInstance().getUserId())
                                .setObfuscatedProfileId(orderNumber)
                                .build();
                        int responseCode = billingClient.launchBillingFlow(activity, flowParams).getResponseCode();
                        if (responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.i(TAG, "成功啟動google支付");
                            PAYMENT_SUCCESS.postValue(getBillingPurchasesState(responseCode,BillingPurchasesState.BillingFlowNode.launchBilling,null));
                        } else {
                            PAYMENT_FAIL.postValue(getBillingPurchasesState(responseCode,BillingPurchasesState.BillingFlowNode.launchBilling,null));
                            Log.i(TAG, "LaunchBillingFlow Fail,code=" + responseCode);
                        }
                    }
                }else {
                    PAYMENT_FAIL.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.querySkuDetails,null));
                }
            } else {
                //谷歌api状态
                PAYMENT_FAIL.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.querySkuDetails,null));
                Log.i(TAG, "Get SkuDetails Failed,Msg=" + billingResult.getDebugMessage());
            }
        });
    }

    //商家确认消耗订单
    private void acknowledgePurchase(final Purchase purchase) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {
            //Log.e(TAG,"確認訂單成功回調："+billingResult.getResponseCode());
            //确认购买成功
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Acknowledge purchase success");
                PAYMENT_SUCCESS.postValue(getBillingPurchasesState(0,BillingPurchasesState.BillingFlowNode.acknowledgePurchase,purchase));
            } else {
                //上架确认购买消耗失败 原因多种：掉线、超时、无网络、用户主动关闭支付处理窗体
                PAYMENT_FAIL.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.acknowledgePurchase,purchase));
            }
        };
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
    }

    //查询本地谷歌缓存、消耗历史订单
    public void queryAndConsumePurchase(String SkuType){
        //queryPurchases() 方法会使用 Google Play 商店应用的缓存，而不会发起网络请求
        billingClient.queryPurchaseHistoryAsync(SkuType,
                (billingResult, purchaseHistoryRecordList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchaseHistoryRecordList != null) {
                        for (PurchaseHistoryRecord purchaseHistoryRecord : purchaseHistoryRecordList) {
                            // Process the result.
                            //确认购买交易，不然三天后会退款给用户
                            try {
                                Purchase purchase = new Purchase(purchaseHistoryRecord.getOriginalJson(), purchaseHistoryRecord.getSignature());
                                Log.e(TAG,SkuType+"===="+purchaseHistoryRecord.getSkus()+"=查询当前订单状态："+purchase.toString());
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                    //消耗品 开始消耗
                                    consumePurchaseHistory(purchase);
                                    //确认购买交易
                                    if (!purchase.isAcknowledged()) {
                                        acknowledgeHistoryPurchase(purchase);
                                    }
                                    //TODO：这里可以添加订单找回功能，防止变态用户付完钱就杀死App的这种
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    //消耗商品
    private void consumePurchaseHistory(final Purchase purchase) {
        ConsumeParams.Builder consumeParams = ConsumeParams.newBuilder();
        consumeParams.setPurchaseToken(purchase.getPurchaseToken());
        billingClient.consumeAsync(consumeParams.build(), (billingResult, purchaseToken) -> {
            Log.i(TAG, "onConsumeResponse, code=" + billingResult.getResponseCode());
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "onConsumeResponse,code=BillingResponseCode.OK");
                PurchaseHistory.postValue(getBillingPurchasesState(0,BillingPurchasesState.BillingFlowNode.queryPurchaseHistory,purchase));
            } else {
                //如果消耗不成功，那就再消耗一次
                Log.i(TAG, "onConsumeResponse=getDebugMessage==" + billingResult.getDebugMessage());
            }
        });
    }
    //商家确认消耗订单
    private void acknowledgeHistoryPurchase(final Purchase purchase) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {
            //Log.e(TAG,"確認訂單成功回調："+billingResult.getResponseCode());
            //确认购买成功
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Acknowledge purchase success");
                PurchaseHistory.postValue(getBillingPurchasesState(0,BillingPurchasesState.BillingFlowNode.acknowledgePurchase,purchase));
            } else {
                //上架确认购买消耗失败 原因多种：掉线、超时、无网络、用户主动关闭支付处理窗体
                PurchaseHistory.postValue(getBillingPurchasesState(billingResult.getResponseCode(),BillingPurchasesState.BillingFlowNode.acknowledgePurchase,purchase));
            }
        };
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
    }

    //返回支付流程节点
    private BillingPurchasesState getBillingPurchasesState(int billingResponseCode, BillingPurchasesState.BillingFlowNode billingFlowNode, Purchase purchase){
        return new BillingPurchasesState(billingResponseCode,billingFlowNode,purchase);
    }

    /**
     * 补单操作 查询已支付的商品，并通知服务器后消费（google的支付里面，没有消费的商品，不能再次购买）
     */
    public void queryPurchasesAsync(String SkuType){
        PurchasesResponseListener mPurchasesResponseListener = (billingResult, purchasesResult) -> {
            if(billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) return;
            if(purchasesResult!=null && !purchasesResult.isEmpty()){
                for (Purchase purchase : purchasesResult) {
                    if(purchase!=null){
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            //消耗品 开始消耗
                            consumePurchaseHistory(purchase);
                            //确认购买交易
                            if (!purchase.isAcknowledged()) {
                                acknowledgeHistoryPurchase(purchase);
                            }
                        }
                    }
                }
            }
        };
        billingClient.queryPurchasesAsync(SkuType,mPurchasesResponseListener);
    }

}