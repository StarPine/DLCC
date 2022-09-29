package com.aliyun.svideo.beauty.queen.util;

import android.content.Context;
import android.text.TextUtils;

import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;
import com.aliyun.svideo.beauty.queen.constant.QueenBeautyConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class QueenParamsUtils {
    public static Map<Integer, QueenBeautyShapeParams> getShapeParams(Context context) {
        Map<Integer, QueenBeautyShapeParams> customParams = null;
        try {
            //获取用户微调参数
            String shapeBeautyCustomParams = SharedPreferenceUtils.getQueenShapeBeautyCustomParams(context);
            if (!TextUtils.isEmpty(shapeBeautyCustomParams)) {
                Gson gson = new GsonBuilder().create();
                customParams = gson.fromJson(shapeBeautyCustomParams, new TypeToken<HashMap<Integer, QueenBeautyShapeParams>>() {
                }.getType());
            }
        } catch (Exception e) {
            SharedPreferenceUtils.setQueenShapeBeautyCustomParams(context, "");
        }
        if (customParams == null) {
            customParams = new HashMap<>();
            for (Map.Entry<Integer, QueenBeautyShapeParams> key : QueenBeautyConstants.BEAUTY_SHAPE_DEFAULT_MAP.entrySet()) {
                QueenBeautyShapeParams value = key.getValue();
                QueenBeautyShapeParams cloneParams = value.clone();
                customParams.put(cloneParams.shapeType, cloneParams);

            }

        }
        return customParams;
    }

    public static Map<Integer, QueenBeautyFaceParams> getFaceParams(Context context) {
        Map<Integer, QueenBeautyFaceParams> customParams = null;
        try {
            //获取用户微调参数
            String raceFaceBeautyCustomParams = SharedPreferenceUtils.getQueenFaceBeautyCustomParams(context);
            if (!TextUtils.isEmpty(raceFaceBeautyCustomParams)) {
                Gson gson = new GsonBuilder().create();
                customParams = gson.fromJson(raceFaceBeautyCustomParams, new TypeToken<HashMap<Integer, QueenBeautyFaceParams>>() {
                }.getType());
            }
        } catch (Exception e) {
            SharedPreferenceUtils.setQueenFaceBeautyCustomParams(context, "");
        }
        if (customParams == null) {
            customParams = new HashMap<>();
            for (Map.Entry<Integer, QueenBeautyFaceParams> key : QueenBeautyConstants.BEAUTY_MAP.entrySet()) {
                QueenBeautyFaceParams value = key.getValue();
                QueenBeautyFaceParams cloneParams = value.clone();
                customParams.put(cloneParams.beautyLevel, cloneParams);
            }
        }
        return customParams;
    }
}
