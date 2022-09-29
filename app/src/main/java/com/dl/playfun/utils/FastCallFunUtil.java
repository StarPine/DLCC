package com.dl.playfun.utils;

import androidx.annotation.NonNull;

import com.dl.playfun.manager.CacheManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 彭石林
 * Time: 2022/4/3 15:49
 * Description: 限制快速多次触发执行方法的工具类
 */
public class FastCallFunUtil {

    private static FastCallFunUtil fastCallFunUtil;

    private Map<String,Long> lastClickFunName = new HashMap<>();

    private FastCallFunUtil() {
    }

    public static FastCallFunUtil getInstance() {
        if (fastCallFunUtil == null) {
            synchronized (FastCallFunUtil.class) {
                if (fastCallFunUtil == null) {
                    fastCallFunUtil = new FastCallFunUtil();
                }
            }
        }
        return fastCallFunUtil;
    }

    /**
    * @Desc TODO(连续执行多次方法 方法参数 标识方法名，间隔时间)
    * @author 彭石林
    * @parame [callFunSimpleName, delayTime] 标识方法名，间隔时间
    * @return boolean
    * @Date 2022/4/3
    */
    public  boolean isFastCallFun(@NonNull String callFunSimpleName,int delayTime) {
        long currentClickTime = System.currentTimeMillis();
        if(lastClickFunName.containsKey(callFunSimpleName)){
            long oldClickFUnTime = lastClickFunName.get(callFunSimpleName);
            boolean isFastClick = (currentClickTime - oldClickFUnTime) <= delayTime;
            if(!isFastClick){//这里采用了点击一次间隔 N 秒后可以再次触发执行。如有需要可改成强制防抖。没执行一次调用。时间无限往后延迟
                lastClickFunName.put(callFunSimpleName,currentClickTime);
            }
            return isFastClick;
        }
        lastClickFunName.put(callFunSimpleName,currentClickTime);
        return false;
    }
}
