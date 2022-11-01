
package com.fine.friendlycc.widget.glide;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GlideProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody responseBody = response.body();

        Response newResponse = response.newBuilder().body(
                new GlideProgressResponseBody(url, responseBody)).build();

        return newResponse;
    }
}