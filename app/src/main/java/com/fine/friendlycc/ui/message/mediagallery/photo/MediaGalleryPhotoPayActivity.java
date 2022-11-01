package com.fine.friendlycc.ui.message.mediagallery.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.databinding.ActivityMediaGalleryPhotoBinding;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.glide.GlideProgressListener;
import com.fine.friendlycc.widget.glide.GlideProgressManager;
import com.gyf.immersionbar.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.qcloud.tuicore.custom.CustomDrawableUtils;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Author: 彭石林
 * Time: 2022/9/14 11:20
 * Description: This is MediaGalleryPhotoPayActivity
 */
public class MediaGalleryPhotoPayActivity extends BaseActivity<ActivityMediaGalleryPhotoBinding,MediaGalleryPhotoPayViewModel> {
    private static final String TAG = "MediaGalleryPhotoPay";
    private MediaGalleryEditEntity mediaGalleryEditEntity;
    //倒计时
    private CountDownTimer downTimer;
    //文件地址
    private String srcPath;
    //快照可看时间
    private long millisInFuture = 2;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(getResources());
        return R.layout.activity_media_gallery_photo;
    }

    @Override
    public int initVariableId() {
        return BR.photoViewModel;
    }

    /**
     * @Desc TODO()
     * @author 彭石林
     * @parame [isPayState 是否付费, isVideoSetting 是否 视频, srcPath 文件信息]
     * @return android.content.Intent
     * @Date 2022/9/14
     */
    public static Intent createIntent(Context mContext, MediaGalleryEditEntity mediaGalleryEditEntity){
        Intent snapshotIntent = new Intent(mContext, MediaGalleryPhotoPayActivity.class);
        snapshotIntent.putExtra("mediaGalleryEditEntity",mediaGalleryEditEntity);
        return snapshotIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, false, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, false);
    }

    @Override
    public void initParam() {
        super.initParam();
        //防窥屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        mediaGalleryEditEntity = (MediaGalleryEditEntity) getIntent().getSerializableExtra("mediaGalleryEditEntity");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        AutoSizeUtils.closeAdapt(getResources());
        if (downTimer != null) {
            downTimer.cancel();
            downTimer = null;
        }
        GlideProgressManager.getInstance().removeGlideProgressListener(glideProgressListener);
    }

    GlideProgressListener glideProgressListener = (url, progress, isFinish) -> {
        if(srcPath!=null && url!=null && srcPath.equals(url)){
            binding.getRoot().post(()->processLoading(progress));
        }
    };

    private void processLoading(int progress){
        if(progress>=100){
            binding.processLayout.setVisibility(View.GONE);
        }else{
            binding.mpProgress.SetCurrent(progress);
            if (binding.processLayout.getVisibility() == View.GONE) {
                binding.processLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        //快照预览时间
        millisInFuture = ConfigManager.getInstance().mediaGallerySnapshotUnLockTime();
        int barHeight = ImmersionBar.getStatusBarHeight(this);
        ViewGroup.LayoutParams gulp = binding.statusBarViews.getLayoutParams();
        gulp.height = barHeight;
        binding.statusBarViews.setLayoutParams(gulp);
        viewModel.mediaGalleryEditEntity = mediaGalleryEditEntity;
        GlideProgressManager.getInstance().setGlideProgressListener(glideProgressListener);
        if(mediaGalleryEditEntity!=null){
            Log.e(TAG,"当前照片详细信息为："+mediaGalleryEditEntity.toString());
            srcPath = StringUtil.getFullImageUrl(mediaGalleryEditEntity.getSrcPath());
            //本地资源存在
            if(!TextUtils.isEmpty(mediaGalleryEditEntity.getAndroidLocalSrcPath())){
                //判断本地资源是否存在
                File imageFile = new File(mediaGalleryEditEntity.getAndroidLocalSrcPath());
                if(imageFile.exists()){
                    srcPath = mediaGalleryEditEntity.getAndroidLocalSrcPath();
                }
            }
            //快照 并且不是自己查看 加蒙版
            if(mediaGalleryEditEntity.isStateSnapshot() && !mediaGalleryEditEntity.isSelfSend()){
                GlideEngine.createGlideEngine().loadImage(this, srcPath, binding.imgContent,R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error, binding.imgLong,true, new GlideEngine.LoadProgressCallback() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    }

                    @Override
                    public void setResource(boolean imgLong) {
                        if(!mediaGalleryEditEntity.isReadLook()){
                            viewModel.snapshotLockState.set(true);
                        }
                        //不是自己发送
                        if(!mediaGalleryEditEntity.isSelfSend()){
                            //付费照片才查看评价
                            if(mediaGalleryEditEntity.isStatePay() && mediaGalleryEditEntity.isReadLook()){
                                viewModel.mediaGalleryEvaluationQry(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
                            }
                        }
                    }
                });
            }else{
                GlideEngine.createGlideEngine().loadImage(this, srcPath, binding.imgContent,R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error, binding.imgLong,false, new GlideEngine.LoadProgressCallback() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    }

                    @Override
                    public void setResource(boolean imgLong) {
                        //不是自己发送
                        if(!mediaGalleryEditEntity.isSelfSend()){
                            //付费照片才查看评价
                            if(mediaGalleryEditEntity.isStatePay()){
                                viewModel.mediaGalleryEvaluationQry(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
                            }
                        }
                    }
                });
            }
            //进入页面逻辑不是自己发送
            if(!mediaGalleryEditEntity.isSelfSend()){
                //付费照片才查看评价
                if(mediaGalleryEditEntity.isStatePay()){
                    //已经解锁
                    if(mediaGalleryEditEntity.isStateUnlockPhoto()){
                        //不是快照 并且已读 查询评价
                        if(!mediaGalleryEditEntity.isStateSnapshot() && mediaGalleryEditEntity.isReadLook()){
                            viewModel.mediaGalleryEvaluationQry(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
                        }
                    }
                }
                viewModel.isReadLook.set(mediaGalleryEditEntity.isReadLook());
            }
        }
        doubleClick(binding.llLike);
        doubleClick(binding.llNoLike);
    }
    @SuppressLint("CheckResult")
    public void doubleClick(View view){
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)//1秒钟内只允许点击1次
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (view.getId() == binding.llLike.getId()) {
                            Integer evaluationType = viewModel.evaluationLikeEvent.getValue();
                            if(evaluationType !=null){
                                if(evaluationType == 0 || evaluationType == 1){
                                    viewModel.mediaGalleryEvaluationPut(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId(),2);
                                }
                            }
                        }else if(view.getId() == binding.llNoLike.getId()){
                            Integer evaluationType = viewModel.evaluationLikeEvent.getValue();
                            if(evaluationType !=null && evaluationType == 0){
                                //差评
                                viewModel.mediaGalleryEvaluationPut(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId(),1);
                            }
                        }
                    }
                });
    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();

        //解锁事件
        viewModel.snapshotLockEvent.observe(this, unused -> {
            GlideEngine.createGlideEngine().loadImage(this, srcPath, binding.imgContent, R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error,binding.imgLong,false, new GlideEngine.LoadProgressCallback() {
                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                }

                @Override
                public void setResource(boolean imgLong) {
                    viewModel.snapshotLockState.set(false);
                    //图片加载成功开始倒计时
                    startTimer();
                }
            });
        });
        //当前评价状态
        viewModel.evaluationLikeEvent.observe(this, state -> {
            //评价，0未评价，1差评，2好评
            if(state == 1){
                viewModel.evaluationState.set(true);
                generateDrawable(binding.llNoLike,null,22,null,null,R.color.playfun_shape_radius_start_color,R.color.playfun_shape_radius_end_color,GradientDrawable.Orientation.LEFT_RIGHT);
                generateDrawable(binding.llLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }else if(state == 2){
                generateDrawable(binding.llLike,null,22,null,null,R.color.playfun_shape_radius_start_color,R.color.playfun_shape_radius_end_color,GradientDrawable.Orientation.LEFT_RIGHT);
                generateDrawable(binding.llNoLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }else if(state == 0){
                //没有评价、并且是已经解锁状态
                if(mediaGalleryEditEntity.isStateUnlockPhoto()){
                    viewModel.evaluationState.set(true);
                }
                generateDrawable(binding.llLike,R.color.black,22,R.color.purple_text,1,null,null,null);
                generateDrawable(binding.llNoLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }
        });
    }

    void generateDrawable(View view,Integer drawableColor,Integer drawableCornersRadius,Integer drawableStrokeColor,
                          Integer drawableStrokeWidth,Integer drawableStartColor, Integer drawableEndColor,GradientDrawable.Orientation orientation){
        CustomDrawableUtils.generateDrawable(view, getColorFromResource(drawableColor),
                drawableCornersRadius,null,null,null,null,
                getColorFromResource(drawableStartColor),getColorFromResource(drawableEndColor),drawableStrokeWidth,getColorFromResource(drawableStrokeColor),null, orientation);
    }

    Integer getColorFromResource(Integer resourceId) {
        if (resourceId==null) {
            return null;
        } else {
            return getContext().getResources().getColor(resourceId);
        }
    }
    @Override
    public MediaGalleryPhotoPayViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MediaGalleryPhotoPayViewModel.class);
    }

    /**
     * 开始计时
     */
    public void startTimer() {
        viewModel.snapshotTimeState.set(true);
        //倒计时15秒，一次1秒
        downTimer = new CountDownTimer(millisInFuture * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                    viewModel.snapshotTimeText.set((millisUntilFinished / 1000)+"s");
            }
            @Override
            public void onFinish() {
                stopTimer();
                //再次模糊图片
                GlideEngine.createGlideEngine().loadImage(getContext(), srcPath, binding.imgContent,R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error, binding.imgLong,true, new GlideEngine.LoadProgressCallback() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    }

                    @Override
                    public void setResource(boolean imgLong) {

                    }
                });
            }
        };
        downTimer.start();
    }

    /**
     * 停止计时
     */
    public void stopTimer() {
        if (downTimer != null) {
            downTimer.cancel();
            downTimer = null;
        }
        //倒计时不可见
        viewModel.snapshotTimeState.set(false);
        //是否已经看过
        viewModel.isReadLook.set(true);
        //查询评价接口失败。不让继续评价
//        if(viewModel.evaluationLikeEvent.getValue()!=null){
//            //评价弹出
//            viewModel.evaluationState.set(false);
//        }
        //付费照片才查看评价
        if(mediaGalleryEditEntity.isStatePay()){
            viewModel.mediaGalleryEvaluationQry(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
        }
    }


}
