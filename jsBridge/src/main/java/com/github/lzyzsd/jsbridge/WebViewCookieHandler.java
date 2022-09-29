package com.github.lzyzsd.jsbridge;

import android.webkit.CookieManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

// 替换okhttp 的cookieJar，打通webview和okHttp的cookie
// https://www.jianshu.com/p/eebf9b270b6a
public class WebViewCookieHandler implements CookieJar {
    private final CookieManager mCookieManager = CookieManager.getInstance();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        String urlString = url.toString();

        for (Cookie cookie : cookies) {
            mCookieManager.setCookie(urlString, cookie.toString());
        }
        // 非必要，只是RAM to ROM，只要RAM有cookie就可以了
//        mCookieManager.flush();
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        String urlString = url.toString();
        String cookiesString = mCookieManager.getCookie(urlString);

        if (cookiesString != null && !cookiesString.isEmpty()) {
            String[] cookieHeaders = cookiesString.split(";");
            List<Cookie> cookies = new ArrayList<>(cookieHeaders.length);

            for (String header : cookieHeaders) {
                cookies.add(Cookie.parse(url, header));
            }

            return cookies;
        }

        return Collections.emptyList();
    }
}