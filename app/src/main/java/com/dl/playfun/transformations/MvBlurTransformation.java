package com.dl.playfun.transformations;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * @author litchi
 */
public class MvBlurTransformation implements Transformation<Bitmap> {

    private static final int VERSION = 1;
    private static final String ID =
            "com.dl.playfun.transformations.MvBlurTransformation." + VERSION;


    private final int radius;

    public MvBlurTransformation(int radius) {
        this.radius = radius;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        BlurFactor blurFactor = new BlurFactor();
//        int targetWidth = outWidth == Target.SIZE_ORIGINAL ? resource.get().getWidth() : outWidth;
//        int targetHeight = outHeight == Target.SIZE_ORIGINAL ? resource.get().getHeight() : outHeight;
        blurFactor.width = resource.get().getWidth();
        blurFactor.height = resource.get().getHeight();
        blurFactor.radius = radius;
        blurFactor.sampling = 1;
//        blurFactor.color = Color.argb(100, 255, 255, 255);
        Bitmap of = Blur.of(context, resource.get(), blurFactor);
        Resource<Bitmap> result = BitmapResource.obtain(of, bitmapPool);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID).getBytes(CHARSET));
    }

}
