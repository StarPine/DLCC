package com.fine.friendlycc.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.R;

/**
 * @author wulei
 */
public class MessageNotification {

    private static final String TAG = MessageNotification.class.getSimpleName();

    private static final String NOTIFICATION_CHANNEL_COMMON = "tuikit_common_msg";
    private static final int NOTIFICATION_ID_COMMON = 520;

    private static final String NOTIFICATION_CHANNEL_CALL = "tuikit_call_msg";
    private static final int NOTIFICATION_ID_CALL = 521;

    private static final int DIALING_DURATION = 30 * 1000;

    private static final MessageNotification sNotification = new MessageNotification();

    private final NotificationManager mManager;
    private final Handler mHandler = new Handler();
    private final Context mContext = AppContext.instance();

    private MessageNotification() {
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mManager == null) {
            Log.e(TAG, "get NotificationManager failed");
            return;
        }
        createNotificationChannel(false);
        createNotificationChannel(true);
    }

    public static MessageNotification getInstance() {
        return sNotification;
    }

    private void createNotificationChannel(boolean isDialing) {
        if (mManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel;
            if (isDialing) {
                channel = new NotificationChannel(NOTIFICATION_CHANNEL_CALL, mContext.getString(R.string.playcc_audio_video_invitation_message_notification), NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), null);
                channel.setDescription(mContext.getString(R.string.playcc_incoming_call_message_notification_remind_user));
                channel.setVibrationPattern(new long[]{0, 1000, 1000, 1000, 1000});
            } else {
                channel = new NotificationChannel(NOTIFICATION_CHANNEL_COMMON, mContext.getString(R.string.playcc_new_message_notification), NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(mContext.getString(R.string.playcc_new_message_notification_remind_user));
            }
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            mManager.createNotificationChannel(channel);
        }
    }

    public void cancelTimeout() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
