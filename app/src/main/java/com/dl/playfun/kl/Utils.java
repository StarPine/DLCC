package com.dl.playfun.kl;

import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.ToastUtils;
import com.dl.playfun.app.AppContext;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.TUICallingImpl;
import com.tencent.liteav.trtccalling.model.TRTCCalling;

public class Utils {
    protected static final Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * 开始呼叫某人
     */
    public static void startCallSomeone(int type, String toUserId, int roomId, String data) {
        if (type == TRTCCalling.TYPE_VIDEO_CALL) {
            String[] userIDs = {toUserId};
            TUICallingImpl.sharedInstance(AppContext.instance()).call(userIDs, TUICalling.Type.VIDEO, roomId, data);
        } else if (type == TRTCCalling.TYPE_AUDIO_CALL) {
            String[] userIDs = {toUserId};
            TUICallingImpl.sharedInstance(AppContext.instance()).call(userIDs, TUICalling.Type.AUDIO, roomId, data);
        }
    }

    public static void tryStartCallSomeone(int type, String userId, int roomId, String data) {
        startCallSomeone(type, userId, roomId, data);
    }

    public static void show(String message) {
        ToastUtils.showLong(message);
    }

    public static void runOnUiThread(Runnable task) {
        if (null != task) {
            mMainHandler.post(task);
        }
    }

    public static void runOnUiThread(Runnable task, long delay) {
        if (null != task) {
            mMainHandler.postDelayed(task, delay);
        }
    }

}
