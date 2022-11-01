package com.fine.friendlycc.viewadapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fine.friendlycc.R;
import com.fine.friendlycc.entity.AdItemEntity;
import com.fine.friendlycc.entity.TaskAdEntity;
import com.fine.friendlycc.utils.StringUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author 彭石林
 * @Desc TODO()
 * @parame
 * @return
 * @Date 2021/9/4
 */
public class BannetDataAdapter {

    @BindingAdapter(value = {"BannerTaskAdData", "onBannerClickCommand"}, requireAll = false)
    public static void setTaskAdImageUri(Banner banner, List<TaskAdEntity> adItemEntities, BindingCommand<Integer> OnBannerClickCommand) {
        try {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new TaskGlideImageLoader());
            //设置图片集合
            banner.setImages(adItemEntities);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.Default);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(2500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (OnBannerClickCommand != null) {
                        OnBannerClickCommand.execute(position);
                    }
//                    ToastUtils.showShort("位置：" + position + ".路劲：" + adItemEntities.get(position).getLink());
                }
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = {"BannerAdData", "onBannerClickCommand"}, requireAll = false)
    public static void setAdImageUri(Banner banner, List<AdItemEntity> adItemEntities, BindingCommand<Integer> OnBannerClickCommand) {
        if(adItemEntities==null || adItemEntities.isEmpty()){
            return;
        }
        try {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new AdBannerGlideImageLoader());
            //设置图片集合
            banner.setImages(adItemEntities);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.Default);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(5000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setOnBannerListener(position -> {
                if (OnBannerClickCommand != null) {
                    OnBannerClickCommand.execute(position);
                }
//                    ToastUtils.showShort("位置：" + position + ".路劲：" + adItemEntities.get(position).getLink());
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        } catch (Exception e) {
            Log.e("进入banner广告列表播放","触发异常：异常原因 "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static class TaskGlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            TaskAdEntity taskAdEntity = (TaskAdEntity) path;
            Glide.with(context)
                    .load(StringUtil.getFullImageUrl(taskAdEntity.getImg()))
                    .apply(new RequestOptions()
                            .placeholder(context.getResources().getDrawable(R.drawable.img_vip_sub_banner_default))
                            .error(context.getResources().getDrawable(R.drawable.img_vip_sub_banner_default)))
                    .into(imageView);
        }
    }
    //首页-广告列表banner展示
    public static class AdBannerGlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            AdItemEntity adItemEntity = (AdItemEntity) path;
            Glide.with(context)
                    .load(StringUtil.getFullImageUrl(adItemEntity.getImg()))
                    .apply(new RequestOptions()
                            .placeholder(context.getResources().getDrawable(R.drawable.img_banner_default))
                            .error(context.getResources().getDrawable(R.drawable.img_banner_default)))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"BannerTaskAdData", "onBannerClickCommand"}, requireAll = false)
    public static void setVIPSubscribeImageUri(Banner banner, List<TaskAdEntity> adItemEntities, BindingCommand<Integer> OnBannerClickCommand) {
        try {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new VIPSubscribeImageLoader());
            //设置图片集合
            banner.setImages(adItemEntities);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.Default);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(2500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (OnBannerClickCommand != null) {
                        OnBannerClickCommand.execute(position);
                    }
//                    ToastUtils.showShort("位置：" + position + ".路劲：" + adItemEntities.get(position).getLink());
                }
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class VIPSubscribeImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            TaskAdEntity taskAdEntity = (TaskAdEntity) path;
            Glide.with(context)
                    .load(StringUtil.getFullImageUrl(taskAdEntity.getImg()))
                    .apply(new RequestOptions()
                            .placeholder(context.getResources().getDrawable(R.drawable.img_vip_sub_banner_defaults))
                            .error(context.getResources().getDrawable(R.drawable.img_vip_sub_banner_defaults)))
                    .into(imageView);
        }
    }
}
