package com.fine.friendlycc.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.fine.friendlycc.R;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.tencent.custom.MvBlurTransformation;

/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
public class GlideEngine implements ImageEngine {


    private static GlideEngine instance;


    private GlideEngine() {
    }

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setPanEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setPanEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView,int placeholderImg,int errorImg) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholderImg)
                .error(errorImg)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setPanEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }


    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.picture_image_placeholder)
                        .override(180, 180)
                        .centerCrop()
                        .sizeMultiplier(0.5f))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载gif
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url,
                               @NonNull ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.picture_image_placeholder)
                        .override(200, 200)
                        .centerCrop())
                .into(imageView);
    }

    public void loadRoundeImage(@NonNull Context context, @NonNull Integer radius, @NonNull String url, @NonNull ImageView imageView) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(SizeUtils.dp2px(radius));
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions override = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(context).load(url)
                .apply(override)
                .into(imageView);
    }
    /**
    * @Desc TODO(加载长图处理)
    * @author 彭石林
    * @parame [context, url, imageView, longImageView, callback]
    * @Date 2022/9/16
    */
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          int placeholderImg,int errorImg,
                          SubsamplingScaleImageView longImageView,boolean stateSnapshot, LoadProgressCallback callback) {
        RequestOptions override;
        if(stateSnapshot){
            override = RequestOptions.bitmapTransform(new MvBlurTransformation(100));
        }else{
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            override = new RequestOptions();
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(override.error(errorImg).placeholder(placeholderImg))
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onLoadStarted(placeholder);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onLoadFailed(errorDrawable);
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setPanEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                            if (callback != null) {
                                callback.setResource(eqLongImage);
                            }
                        }
                    }
                });
    }
    /**
     * @Desc TODO(加载长图处理)
     * @author 彭石林
     * @parame [context, url, imageView, longImageView, callback]
     * @Date 2022/9/16
     */
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,int placeholderImg,int errorImg, LoadProgressCallback callback) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholderImg)
                .error(errorImg)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onLoadStarted(placeholder);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onLoadFailed(errorDrawable);
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            if (callback != null) {
                                callback.setResource(false);
                            }
                        }
                    }
                });
    }
    /**
     * @Desc TODO(加载进度回调)
     * @author 彭石林
     * @Date 2022/9/16
     */
    public interface LoadProgressCallback {
        void onLoadStarted(@Nullable Drawable placeholder);
        void onLoadFailed(@Nullable Drawable errorDrawable);
        void setResource(boolean eqLongImage);
    }
}