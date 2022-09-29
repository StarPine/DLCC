package com.dl.playfun.data;

/**
 * Author: 彭石林
 * Time: 2022/7/2 14:53
 * Description: This is RetrofitHeadersConfig
 */
public interface RetrofitHeadersConfig {
    //不需要添加token api
    String NO_TOKEN_CHECK = "NO_TOKEN_CHECK:NO_TOKEN_CHECK";
    String NO_TOKEN_CHECK_KEY = "NO_TOKEN_CHECK";
    //初始化api
    String DEFAULT_API_INIT_URL = "DEFAULT_API_INIT_URL:DEFAULT_API_INIT_URL";
    String DEFAULT_API_INIT_URL_KEY = "DEFAULT_API_INIT_URL";
    //任务中心模块
    String PlayChat_API_URL = "PlayChat_API_URL:PlayChat_API_URL";
    String PlayChat_API_URL_KEY = "PlayChat_API_URL";

    //推币机请求头配置
    interface CoinPUsherConfig{
        //超时时间
        int API_TIMEOUT = 5;
        String API_TIMEOUT_HEADER = "API_TIMEOUT_KEY:"+API_TIMEOUT;
        String API_TIMEOUT_KEY = "API_TIMEOUT_KEY";
    }

}
