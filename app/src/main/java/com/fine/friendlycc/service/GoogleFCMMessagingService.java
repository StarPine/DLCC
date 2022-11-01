package com.fine.friendlycc.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.event.PushMessageEvent;
import com.fine.friendlycc.ui.MainContainerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.fine.friendlycc.R;
import com.fine.friendlycc.manager.ThirdPushTokenMgr;

import java.util.Map;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * @author wulei
 */
public class GoogleFCMMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        Log.d("GoogleFCMMessaging", "Refreshed token: " + token);
        super.onNewToken(token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        AppContext.instance().pushDeviceToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("GoogleFCMMessaging", "onMessageReceived:" + remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        if (map != null) {
            String type = map.get("type");
            if (type != null) {
                RxBus.getDefault().post(new PushMessageEvent(type));
            }
        }
        //这个应该可以看懂-往通知栏添加消息
        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
            sendNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            sendNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void sendNotification(Context iContext, String messageTitle, String messageBody) {


        //跳转到你想要跳转到页面
        Intent intent = new Intent(AppContext.instance(), MainContainerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        //兼容安卓12判断通知栏
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.playfun_received_new_message);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setTicker(messageTitle)//标题
                        .setSmallIcon(R.mipmap.ic_launcher)//你的推送栏图标
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)//内容
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        //这里如果需要的话填写你自己项目的，可以在控制台找到，强转成int类型
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
