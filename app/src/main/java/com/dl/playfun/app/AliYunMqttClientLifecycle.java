package com.dl.playfun.app;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.GsonUtils;
import com.dl.playfun.entity.MqBroadcastGiftEntity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Author: 彭石林
 * Time: 2022/7/4 17:51
 * Description: This is AliYunMqttClient
 */
public class AliYunMqttClientLifecycle implements LifecycleObserver {
    public final String TAG = "MQTT";
    private MqttAndroidClient mqttAndroidClient;
    public int MQTT_ConnectionTimeout = 10;             // 设置超时时间，单位：秒
    public int KeepAliveIntervalTime = 20;              // 设置心跳包发送间隔，单位：秒
    public boolean CleanSession = true;                 // 设置是否清除缓存

    //页面创建第一次连接时间
    private long mqttConnectCreateLastTime = 0L;
    //页面处于可见状态最后依次连接时间
    private long mqttConnectResumeLastTime = 0L;
    //使用实列
    /*   在 act or fragment中注入声明周期管理
     *   AliYunMqttClientLifecycle aliYunMqttClientLifecycle = ((MyContext) getApplication()).getBillingClientLifecycle();
     *   getLifecycle().addObserver(aliYunMqttClientLifecycle);
    */
    //礼物广播回传
    public SingleLiveEvent<MqBroadcastGiftEntity> broadcastGiftEvent = new SingleLiveEvent<>();

    /* 自动Topic, 用于上报消息 */
    final private String PUB_TOPIC = "test/broadcast";
    /* 自动Topic, 用于接受消息 */
    final private String SUB_TOPIC = "test/broadcast";

//    mqtt公网接入点：mqtt-cn-i7m2rnxmn06.mqtt.aliyuncs.com
//    实例ID：mqtt-cn-i7m2rnxmn06
//    测试环境topic：test
//    生产环境topic：prod
//    测试环境Group：GID_TEST
//    生产环境Group：GID_PROD
//    测试环境送礼广播Topic：test/broadcast/sendGift
//    生产环境送礼广播Topic：prod/broadcast/sendGift

    private static volatile AliYunMqttClientLifecycle INSTANCE = null;

    private Application appContext;

    private AliYunMqttClientLifecycle(Application app) {
        this.appContext = app;
    }

    public static AliYunMqttClientLifecycle getInstance(Application context){
        if(INSTANCE==null){
            synchronized (AliYunMqttClientLifecycle.class){
                if(INSTANCE == null){
                    INSTANCE =  new AliYunMqttClientLifecycle(context);
                }
            }
        }
        return INSTANCE;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        mqttConnectCreateLastTime = System.currentTimeMillis();
        initClient();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        //页面再次可见时再次进入判断。防止弱引用持有回收
        mqttConnectResumeLastTime = System.currentTimeMillis();
        if(mqttConnectResumeLastTime!=0L && mqttConnectCreateLastTime!=0L){
            //页面可见时间 - 页面创建时间 < 10秒。不在进行连接。避免重复创建
            if((mqttConnectResumeLastTime / 1000) - (mqttConnectCreateLastTime / 1000) <= 10){
                return;
            }
        }else {
            return;
        }
        initClient();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        Log.d(TAG, "ON_DESTROY");
        clientClose();
    }

    public void clientClose(){
        if(mqttAndroidClient!=null){
            if(mqttAndroidClient.isConnected()){
                try {
                    mqttAndroidClient.unsubscribe(SUB_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                mqttAndroidClient.close();
                mqttAndroidClient.unregisterResources();
                mqttAndroidClient = null;
            }
        }
    }

    public void initClient() {
        if(mqttAndroidClient!=null){
            if(mqttAndroidClient.isConnected()){
                return;
            }
        }
        /* 创建MqttConnectOptions对象并配置username和password */
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        //断开后，是否自动连接
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setConnectionTimeout(MQTT_ConnectionTimeout);
        mqttConnectOptions.setKeepAliveInterval(KeepAliveIntervalTime);
        mqttConnectOptions.setUserName("Signature|LTAI4G25QqrdXwYKMi9aGLYK|mqtt-cn-i7m2rnxmn06");
        mqttConnectOptions.setPassword("gkd6A68riwrjWzmTBEVus0WH4Po=".toCharArray());
        mqttConnectOptions.setCleanSession(CleanSession);
        mqttConnectOptions.setMqttVersion(3);
        /* 创建MqttAndroidClient对象, 并设置回调接口 */
        mqttAndroidClient = new MqttAndroidClient(appContext, "tcp://mqtt-cn-i7m2rnxmn06.mqtt.aliyuncs.com:1883", "GID_TEST@@@TEST1381");
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //断开链接进入这里
                Log.i(TAG, "connection lost");
                long reconnectTimes = 1;
                while (true) {
                    try {
                        if (mqttAndroidClient.isConnected()) {
                            Log.i(TAG,"mqtt reconnect success end");
                            break;
                        }
                        Log.e(TAG,"mqtt reconnect times = {} try again..."+ reconnectTimes++);
                        mqttAndroidClient.connect();
                    } catch (MqttException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e1) {
//                            e1.printStackTrace();
                    }
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.i(TAG, message.getId()+"====="+"，topic: " + topic + ", msg: " + new String(message.getPayload()));
                try {
                    if(message.getPayload() != null){
                        String msgBody = new String(message.getPayload());
                        Map<String,Object> mqttMessageEntity = GsonUtils.fromJson(msgBody,Map.class);
                        if(mqttMessageEntity!=null && mqttMessageEntity.get("messageType")!=null){
                            String messageType = (String) mqttMessageEntity.get("messageType");
                            MqttEventEnum eventEnum = MqttEventEnum.valueOf(messageType);
                            if (eventEnum == MqttEventEnum.sendGift) {//送礼广播
                                MqBroadcastGiftEntity mqBroadcastGiftEntity = GsonUtils.fromJson(GsonUtils.toJson(mqttMessageEntity.get("content")), MqBroadcastGiftEntity.class);
                                if (mqBroadcastGiftEntity != null) {
                                    broadcastGiftEvent.setValue(mqBroadcastGiftEntity);
                                }
                            } else {
                                Log.e(TAG, "当前类型转换不对1：" + messageType + "==========" + MqttEventEnum.valueOf(messageType));
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e(TAG,"当前捕获异常信息："+e.getMessage());
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "msg delivered");
            }
        });

        /* Mqtt建连 */
        try {
            mqttAndroidClient.connect(mqttConnectOptions,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connect succeed");
                    subscribeTopic(SUB_TOPIC);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "connect failed");

                    Log.e(TAG,exception.toString()+"失败原因："+exception.getMessage());
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅特定的主题
     * @param topic mqtt主题
     */
    public void subscribeTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
                    //MqttWireMessage mqttWireMessage = asyncActionToken.getResponse();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "subscribed failed");
                    if(exception!=null && exception.getMessage()!=null){
                        Log.e(TAG,"订阅异常原因:"+exception.getMessage());
                    }
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向默认的主题/user/update发布消息
     * @param payload 消息载荷
     */
    public void publishMessage(String payload) {
        try {
            if (!mqttAndroidClient.isConnected()) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(PUB_TOPIC, message,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "publish failed!");
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

}
