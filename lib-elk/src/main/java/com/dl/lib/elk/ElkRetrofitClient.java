package com.dl.lib.elk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.dl.lib.elk.Interceptor.BaseElkInterceptor;
import com.dl.lib.util.MMKVUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: 彭石林
 * Time: 2022/9/29 15:40
 * Description: This is ElkRetrofitClient
 */
public class ElkRetrofitClient {

    //超时时间
    private static final int DEFAULT_TIMEOUT = 15;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = "https://log.play-chat.net";
    private static Retrofit retrofit;

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        public static final ElkRetrofitClient INSTANCE = new ElkRetrofitClient();
    }

    public static ElkRetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ElkRetrofitClient() {

        this(null);
    }

    private ElkRetrofitClient( Map<String, String> headers) {
        String keyElkUrlPath = MMKVUtil.getInstance().readKeyValue(MMKVUtil.KEY_ELK_URL_DATA);
        if(StringUtils.isEmpty(keyElkUrlPath)){
            keyElkUrlPath = baseUrl;
        }
        if(keyElkUrlPath.equals("null")){
            keyElkUrlPath = baseUrl;
        }

        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("client", "Android");
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new BaseElkInterceptor(headers))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(keyElkUrlPath)
                .build();

    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
}
