package com.dl.playfun.ui.webview;

import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncWebviewResopnse extends WebResourceResponse {
    private final static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    static String TAG = "AsyncWebviewResopnse";
    static InputStream EMPTY_STREAM = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    protected CountDownLatch latch = new CountDownLatch(1);
    protected WebResourceResponse responseReal = null;

    public AsyncWebviewResopnse() {
        super("jpg", "utf-8", EMPTY_STREAM);
    }

    public boolean request(OkHttpClient client, WebResourceRequest webRequest) {
        final Request okRequest;
        try {
            Request.Builder builder = new Request.Builder()
                    .url(new URL(webRequest.getUrl().toString()))
                    .method("GET", null);
            Map<String, String> headers = webRequest.getRequestHeaders();
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
            okRequest = builder.build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            latch.countDown();
            return false;
        }
        EXECUTOR.execute(() -> {
            client.newCall(okRequest)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            responseReal = new WebResourceResponse(
                                    "text/html",
                                    "utf-8",
                                    451,
                                    "java exception:" + e.toString(),
                                    Collections.emptyMap(),
                                    EMPTY_STREAM
                            );
                            latch.countDown();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                Map<String, String> headers = new HashMap<>();
                                for (String key : response.headers().names()) {
                                    headers.put(key, response.header(key));
                                }
                                String message = response.message();
                                if (TextUtils.isEmpty(message)) {
                                    message = response.isSuccessful() ? "OK(kl set)" : "None(kl set)";
                                }
                                responseReal = new WebResourceResponse(
                                        response.header("content-type", "text/plain"), // You can set something other as default content-type
                                        response.header("content-encoding", "utf-8"),  // Again, you can set another encoding as default
                                        response.code(),
                                        message,
                                        headers,
                                        response.body().byteStream()
                                );
                            } catch (Throwable e) {
                                e.printStackTrace();
                                responseReal = new WebResourceResponse(
                                        "text/html",
                                        "utf-8",
                                        451,
                                        "parse response failed,kl set:" + e.toString(),
                                        Collections.emptyMap(),
                                        EMPTY_STREAM
                                );
                            }
                            latch.countDown();
                        }
                    });
        });
        return true;
    }

    protected void await() {
        try {
            latch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMimeType() {
        // TODO: webview有机会很早就读取这个值，可以试试根据url就提前判断这个，不wait了
        await();
        return responseReal.getMimeType();
    }

    @Override
    public int getStatusCode() {
        await();
        return responseReal.getStatusCode();
    }

    @Override
    public String getReasonPhrase() {
        await();
        return responseReal.getReasonPhrase();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        await();
        return responseReal.getResponseHeaders();
    }

    @Override
    public String getEncoding() {
        await();
        return responseReal.getEncoding();
    }

    @Override
    public InputStream getData() {
        await();
        return responseReal.getData();
    }
}
