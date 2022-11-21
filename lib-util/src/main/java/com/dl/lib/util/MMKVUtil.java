package com.dl.lib.util;

import com.tencent.mmkv.MMKV;

/**
 * Author: 彭石林
 * Time: 2022/2/22 22:40
 * Description: This is MMKVUtil
 */
public class MMKVUtil {
    private static final String cryptKey = "playfun@2020";
    public static final String KEY_ELK_URL_DATA = "key_elk_url_data";
    private static final MMKV kv = MMKV.mmkvWithID("cache", MMKV.SINGLE_PROCESS_MODE, cryptKey);
    private volatile static MMKVUtil INSTANCE = null;

    public static MMKVUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (MMKVUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MMKVUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void putKeyValue(String key, String value) {
        kv.encode(key, value);
    }

    public String readKeyValue(String key) {
        return kv.decodeString(key);
    }

    public void removeKeyValue(String key) {
        kv.remove(key);
    }

}
