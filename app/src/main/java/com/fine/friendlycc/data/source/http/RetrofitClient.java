package com.fine.friendlycc.data.source.http;

import android.content.Context;
import android.text.TextUtils;

import com.dl.lib.util.MPDeviceUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.source.http.interceptor.ApiTimeOutInterceptor;
import com.fine.friendlycc.data.source.http.interceptor.TokenInterceptor;
import com.fine.friendlycc.utils.AESUtil;
import com.fine.friendlycc.utils.ApiUitl;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import me.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import me.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import me.goldze.mvvmhabit.http.interceptor.CacheInterceptor;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.Utils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 15;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    private static final Context mContext = Utils.getContext();
    //服务端根路径
    public static String baseUrl = AppConfig.DEFAULT_API_URL;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "play_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }

        if (headers == null) {
            headers = new HashMap<>();
        }
//        - PlayChat source值 1648626888 user_
//                - 宠物世界-俄罗斯 source值 1642158125 ru_
//                - OKEY土耳其 source值 1648520220 troken_
//                - 宠物大富翁-台湾繁体 source值 1648699860 twpet_
        headers.put("client", "Android");
        headers.put("version", AppConfig.VERSION);
        headers.put("sdkVersion", AppConfig.SDK_VERSION_NAME_PUSH);
        headers.put("build", AppConfig.VERSION_CODE+"");
        headers.put("versionName", AppConfig.VERSION_NAME);
        //source 来源ID 1642158125=喵遊(俄语) 1648520220=杜拉克 //playchat 1648626888
        headers.put("appId",AppConfig.APPID);
        headers.put("deviceCode", ApiUitl.getAndroidId());
        headers.put("dev_id", AESUtil.encrypt_AES(AppConfig.KEY_DL_AES_ENCRY, MPDeviceUtils.getDevId(),AppConfig.KEY_DL_AES_ENCRY));
        headers.put("Accept-Language", mContext.getString(R.string.playcc_local_language));
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(mContext))
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(new ApiTimeOutInterceptor())
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(AppConfig.isDebug) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
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

    private static class SingletonHolder {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }
}
