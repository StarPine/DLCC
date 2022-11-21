package com.dl.lib.elk.Interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: 彭石林
 * Time: 2022/9/29 15:49
 * Description: This is BaseInterceptor
 */
public class BaseElkInterceptor implements Interceptor {
    private final Map<String, String> headers;

    public BaseElkInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, Objects.requireNonNull(headers.get(headerKey))).build();
            }
        }
        //请求信息
        return chain.proceed(builder.build());
    }
}