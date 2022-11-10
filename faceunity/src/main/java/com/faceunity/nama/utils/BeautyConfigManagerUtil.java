package com.faceunity.nama.utils;

import com.blankj.utilcode.util.GsonUtils;
import com.faceunity.nama.entity.FaceBeautyFilterBean;
import com.faceunity.nama.entity.FaceBeautyLocalBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import java.util.Collection;
import java.util.List;

/**
 * @Description：
 * @Author： liaosf
 * @Date： 2022/1/15 15:55
 * 修改备注：
 */
public class BeautyConfigManagerUtil {
    private static BeautyConfigManagerUtil mCacheManager;
    private final String cryptKey = "playcc@2020";
    private static final String KEY_FACE_BEAUTY = "key_face_beauty";
    private static final String KEY_FACE_BEAUTY_FILTERS = "key_face_beauty_filters";
    private static final String KEY_FACE_BEAUTY_FILTERS_INDEX = "key_face_beauty_filters_index";
    private final MMKV kv = MMKV.mmkvWithID("cache", MMKV.SINGLE_PROCESS_MODE, cryptKey);
    private Gson gson;

    public static BeautyConfigManagerUtil getInstance() {
        if (mCacheManager == null) {
            synchronized (BeautyConfigManagerUtil.class) {
                if (mCacheManager == null) {
                    mCacheManager = new BeautyConfigManagerUtil();
                }
            }
        }
        return mCacheManager;
    }

    /**
     * 保存滤镜上次下标
     */
    public void putFaceBeautyFiltersIndex(int index) {
        kv.encode(KEY_FACE_BEAUTY_FILTERS_INDEX, index);
    }

    /**
     * 获取滤镜上次下标
     */
    public int getFaceBeautyFiltersIndex() {
        return kv.decodeInt(KEY_FACE_BEAUTY_FILTERS_INDEX, 0);
    }

    /**
     * 保存滤镜数据
     * @param param
     */
    public void putFaceBeautyFilters(List<FaceBeautyFilterBean> param) {
        if (param == null) {
            return;
        }
        String json = GsonUtils.toJson(param);
        kv.encode(KEY_FACE_BEAUTY_FILTERS, json);
    }

    /**
     * 获取滤镜数据
     * @return
     */
    public List<FaceBeautyFilterBean> getFaceBeautyFilters() {
        String json = kv.decodeString(KEY_FACE_BEAUTY_FILTERS);
        if (json == null) {
            return null;
        } else if (json.isEmpty()) {
            return null;
        }
        return GsonUtils.fromJson(json, new TypeToken<List<FaceBeautyFilterBean>>(){}.getType());
    }

    /**
     * 保存美颜相关数值
     * @param param
     */
    public void putFaceBeautyParam(List< FaceBeautyLocalBean> param) {
        if (param == null) {
            return;
        }
        String json = GsonUtils.toJson(param);
        kv.encode(KEY_FACE_BEAUTY, json);
    }

    /**
     * 获取美颜本地数值
     * @return
     */
    public List<FaceBeautyLocalBean> getFaceBeautyParam() {
        String json = kv.decodeString(KEY_FACE_BEAUTY);
        if (json == null) {
            return null;
        } else if (json.isEmpty()) {
            return null;
        }
        return GsonUtils.fromJson(json, new TypeToken<List<FaceBeautyLocalBean>>(){}.getType());
    }



    public static boolean isEmpty(final CharSequence obj) {
        return obj == null || obj.toString().length() == 0;
    }

    public static boolean isEmpty(final Collection obj) {
        return obj == null || obj.isEmpty();
    }
}
