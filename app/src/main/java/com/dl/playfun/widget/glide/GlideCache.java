package com.dl.playfun.widget.glide;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.dl.playfun.data.source.http.RetrofitClient;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public final class GlideCache extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient.Builder builder = GlideUnsafeOkHttpClient.getUnsafeOkHttpClient();
        builder.addInterceptor(new GlideProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        registry.replace(GlideUrl.class, InputStream.class, new GlideOkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}