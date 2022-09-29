package com.aliyun.svideo.beauty.queen.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    private static final String SHAREDPRE_FILE = "svideo_queen";
    private static final String BEAUTY_FACE_LEVEL = "beauty_face_level";
    private static final String BEAUTY_SKIN_LEVEL = "beauty_skin_level";
    private static final String BEAUTY_FACE_PARAMS = "race_face_params";
    private static final String BEAUTY_SHAPE_PARAMS = "race_shape_params";


    public static void setQueenFaceBeautyCustomParams(Context context, String data) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(BEAUTY_FACE_PARAMS, data);
        editor.apply();
    }
    public static void setQueenShapeBeautyCustomParams(Context context, String data) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(BEAUTY_SHAPE_PARAMS, data);
        editor.apply();
    }

    public static String getQueenFaceBeautyCustomParams(Context context) {
        if (context == null){
            return "";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        return sharedPreferences.getString(BEAUTY_FACE_PARAMS, "");
    }

    public static String getQueenShapeBeautyCustomParams(Context context) {
        if (context == null){
            return "";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        return sharedPreferences.getString(BEAUTY_SHAPE_PARAMS, "");
    }


    public static int getQueenFaceBeautyLevel(Context context) {
        if (context == null){
            return 3;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(BEAUTY_FACE_LEVEL, 3);
    }

    public static int getQueenSkinType(Context context) {
        if (context == null){
            return 3;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
                Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(BEAUTY_SKIN_LEVEL, 3);
    }


    public static void setQueenFaceBeautyLevel(Context context, int level) {
        if (context != null){
            SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.remove(BEAUTY_FACE_LEVEL);
            editor.putInt(BEAUTY_FACE_LEVEL, level);
            editor.apply();
        }
    }


    public static void setQueenSkinType(Context context, int level) {
        if (context != null) {
            SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.remove(BEAUTY_SKIN_LEVEL);
            editor.putInt(BEAUTY_SKIN_LEVEL, level);
            editor.apply();
        }
    }


}

