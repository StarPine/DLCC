package com.dl.playfun.data.source.http.interceptor;

import com.blankj.utilcode.util.ObjectUtils;
import com.dl.playfun.data.RetrofitHeadersConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: 彭石林
 * Time: 2022/9/6 18:04
 * Description: 拦截器设置API接口超时时间
 */
public class ApiTimeOutInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        int connectTimeout = chain.connectTimeoutMillis();
        int readTimeout = chain.readTimeoutMillis();
        int writeTimeout = chain.writeTimeoutMillis();

        Request request = chain.request();
        Headers headers = request.headers();
        if(ObjectUtils.isNotEmpty(headers)){
            //推币机相关模块
            String apiTimeout = headers.get(RetrofitHeadersConfig.CoinPUsherConfig.API_TIMEOUT_KEY);
            if(ObjectUtils.isNotEmpty(apiTimeout)){
                int apiTimeOutNum = Integer.parseInt(apiTimeout);
                connectTimeout = apiTimeOutNum;
                readTimeout = apiTimeOutNum;
                writeTimeout = apiTimeOutNum;
            }
        }
        return chain
                .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                .proceed(request);
    }
}
