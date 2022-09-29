package com.dl.playfun.utils;

import android.util.Log;

/**
 * @Name： JoyMask_Google
 * @Description：日志工具类
 * @Author： liaosf
 * @Date： 2021/12/18 12:29
 * 修改备注：
 */
public class LogUtils {

    private static final String TAG = "starpine";

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void v(String message) {
        Log.v(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }


}
