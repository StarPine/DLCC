package com.fine.friendlycc.data.source.http.interceptor;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.RetrofitHeadersConfig;
import com.fine.friendlycc.data.source.LocalDataSource;
import com.fine.friendlycc.data.source.local.LocalDataSourceImpl;
import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.utils.LogUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 彭石林
 */
public class TokenInterceptor implements Interceptor {

    private final LocalDataSource localDataSource;

    public TokenInterceptor() {
        localDataSource = LocalDataSourceImpl.getInstance();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        Request request = builder.build();
        int upUrlFlag = -1;
        ApiConfigManagerBean apiServerUrl = localDataSource.readApiConfigManagerEntity();
        if(request!=null){
            Headers headers = request.headers();
            if(headers!=null){
                //不需要登录token效验
                if(!ObjectUtils.isEmpty(headers.get(RetrofitHeadersConfig.DEFAULT_API_INIT_URL_KEY))&& upUrlFlag == -1){
                    //初始化API
                    upUrlFlag = 0;
                    builder.removeHeader(RetrofitHeadersConfig.DEFAULT_API_INIT_URL_KEY);
                    builder.removeHeader("Authorization");
                    //builder.url(apiServerUrl+path);
                }
                if(!ObjectUtils.isEmpty(headers.get(RetrofitHeadersConfig.NO_TOKEN_CHECK_KEY)) && upUrlFlag == -1){
                    //登录接口api 不加token
                    upUrlFlag = 3;
                }
                if(!ObjectUtils.isEmpty(headers.get(RetrofitHeadersConfig.PlayChat_API_URL_KEY))){
                    upUrlFlag = 2;
                }
                if(headers.get(RetrofitHeadersConfig.DEFAULT_API_INIT_URL_KEY)==null && headers.get(RetrofitHeadersConfig.NO_TOKEN_CHECK_KEY)==null && headers.get(RetrofitHeadersConfig.PlayChat_API_URL_KEY)==null){
                    upUrlFlag = 1;
                }
                if(upUrlFlag == 1 || upUrlFlag == 2){
                    if (localDataSource != null && localDataSource.readLoginInfo() != null && !StringUtils.isEmpty(localDataSource.readLoginInfo().getToken())) {
                        String token = localDataSource.readLoginInfo().getToken();
                        builder.removeHeader(RetrofitHeadersConfig.NO_TOKEN_CHECK_KEY);
                        builder.addHeader("Authorization", "Bearer " + token);
                    }
                }

                //设置环境切换
                setDebugToggle(builder, request);
            }
        }
        if(apiServerUrl!=null){
            try {
                URI customUrl = null;
                switch (upUrlFlag){
                    case -1:
                    case 1:
                    case 3:
                        //
                        customUrl = new URI(apiServerUrl.getPlayFunApiUrl());
                        break;
                    case 0:
                        //不做任何处理
                        break;
                    case 2:
                        //任务中心+福袋页面
                        customUrl = new URI(apiServerUrl.getPlayChatApiUrl());
                        break;
                }
                if(customUrl!=null){
                    HttpUrl newUrl = request.url().newBuilder()
                            .host(customUrl.getHost())
                            .scheme(customUrl.getScheme())
                            .build();
                    builder.url(newUrl);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
//        Response response = chain.proceed(builder.build());
//        // 输出返回结果
//        try {
//            Charset charset;
//            charset = Charset.forName("UTF-8");
//            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
//            Reader jsonReader = new InputStreamReader(responseBody.byteStream(), charset);
//            BufferedReader reader = new BufferedReader(jsonReader);
//            StringBuilder sbJson = new StringBuilder();
//            String line = reader.readLine();
//            do {
//                sbJson.append(line);
//                line = reader.readLine();
//            } while (line != null);
//            Log.e("请求地址拦截: " ,path+"==\t"+ sbJson.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return chain.proceed(builder.build());
    }

    private void setDebugToggle(Request.Builder builder, Request request){
        try {
            if (BuildConfig.DEBUG){
                URI openUrl = null;
                if (AppConfig.isInit){
                    LogUtils.i("setDebugToggle: 调试");
                    if (!AppConfig.isTest){
                        openUrl = new URI("http://api.playcc.net/");
                    }else {
                        openUrl = new URI("http://t-api.playcc.net/");
                    }

                    HttpUrl newUrl = request.url().newBuilder()
                            .host(openUrl.getHost())
                            .scheme(openUrl.getScheme())
                            .build();
                    builder.url(newUrl);
                    AppConfig.isInit = false;
                }
            }
        }catch (Exception e){

        }

    }
}