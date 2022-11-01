package com.fine.friendlycc.ui.message.mediagallery.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.ActivityMediaGalleryVideoBinding;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tencent.qcloud.tuicore.custom.CustomDrawableUtils;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Author: 彭石林
 * Time: 2022/9/19 14:12
 * Description: This is MediaGalleryVideoPayActivity
 */
public class MediaGalleryVideoPayActivity extends BaseActivity<ActivityMediaGalleryVideoBinding,MediaGalleryVideoPayViewModel> {

    private MediaGalleryEditEntity mediaGalleryEditEntity;

    //文件地址
    private String srcPath;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.activity_media_gallery_video;
    }

    @Override
    public int initVariableId() {
        return BR.videoViewModel;
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
        GSYVideoManager.releaseAllVideos();
    }

    /**
     * @Desc TODO()
     * @author 彭石林
     * @parame [srcPath 文件信息]
     * @return android.content.Intent
     * @Date 2022/9/14
     */
    public static Intent createIntent(Context mContext, MediaGalleryEditEntity mediaGalleryEditEntity){
        Intent snapshotIntent = new Intent(mContext, MediaGalleryVideoPayActivity.class);
        snapshotIntent.putExtra("mediaGalleryEditEntity",mediaGalleryEditEntity);
        return snapshotIntent;
    }

    @Override
    public void initParam() {
        super.initParam();
        //防窥屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        mediaGalleryEditEntity = (MediaGalleryEditEntity) getIntent().getSerializableExtra("mediaGalleryEditEntity");
    }

    @Override
    public MediaGalleryVideoPayViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MediaGalleryVideoPayViewModel.class);
    }
    @Override
    public void initData() {
        super.initData();
        srcPath = mediaGalleryEditEntity.getSrcPath();
        //本地资源存在
        if(!TextUtils.isEmpty(mediaGalleryEditEntity.getAndroidLocalSrcPath())){
            //判断本地资源是否存在
            File videoFile = new File(mediaGalleryEditEntity.getAndroidLocalSrcPath());
            if(videoFile.exists()){
                srcPath = mediaGalleryEditEntity.getAndroidLocalSrcPath();
                viewModel.srcPath.set(srcPath);
                viewModel.isLocalSrc.set(true);
            }else{
                viewModel.srcPath.set(srcPath);
            }
        }else{
            viewModel.srcPath.set(srcPath);
        }
       // setVideoUri(binding.videoPlayer,srcPath);
        //是付费影片
        if(!mediaGalleryEditEntity.isSelfSend() && mediaGalleryEditEntity.isStatePay()){
            viewModel.mediaGalleryEvaluationQry(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
        }

        doubleClick(binding.llLike);
        doubleClick(binding.llNoLike);

        //当前评价状态
        viewModel.evaluationLikeEvent.observe(this, state -> {
            //评价，0未评价，1差评，2好评
            if(state == 1){
                generateDrawable(binding.llNoLike,null,22,null,null,R.color.playfun_shape_radius_start_color,R.color.playfun_shape_radius_end_color,GradientDrawable.Orientation.LEFT_RIGHT);
                generateDrawable(binding.llLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }else if(state == 2){
                generateDrawable(binding.llLike,null,22,null,null,R.color.playfun_shape_radius_start_color,R.color.playfun_shape_radius_end_color,GradientDrawable.Orientation.LEFT_RIGHT);
                generateDrawable(binding.llNoLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }else{
                generateDrawable(binding.llLike,R.color.black,22,R.color.purple_text,1,null,null,null);
                generateDrawable(binding.llNoLike,R.color.black,22,R.color.purple_text,1,null,null,null);
            }
        });

    }

    @SuppressLint("CheckResult")
    public void doubleClick(View view){
        RxView.clicks(view)
                .throttleFirst(2, TimeUnit.SECONDS)//2秒钟内只允许点击1次
                .subscribe(o -> {
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
                });
    }


    void generateDrawable(View view, Integer drawableColor, Integer drawableCornersRadius, Integer drawableStrokeColor,
                          Integer drawableStrokeWidth, Integer drawableStartColor, Integer drawableEndColor, GradientDrawable.Orientation orientation){
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
    public void initViewObservable() {
        super.initViewObservable();
    }

    private void setVideoUri(StandardGSYVideoPlayer videoView, String url){
        //videoView.setUrl(url);
        // url = StringUtil.getFullAudioUrl(url);
        //videoView.loadCoverImage(url, R.drawable.default_placeholder_img);
        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();

        //默认缓存路径
        //使用lazy的set可以避免滑动卡的情况存在
        videoView.setUpLazy(url, true, null, null, "VideoPlay");

        //增加title
        videoView.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        videoView.getBackButton().setVisibility(View.GONE);
        videoView.getFullscreenButton().setVisibility(View.GONE);
        //设置全屏按键功能
        videoView.setRotateViewAuto(false);
        videoView.setLockLand(false);
        videoView.setPlayTag("SampleCoverVideoPlayer");
        //gsyVideoPlayer.c(true);
        videoView.setReleaseWhenLossAudio(true);
        videoView.setAutoFullWithSize(true);
        videoView.setShowFullAnimation(true);
        videoView.setIsTouchWiget(true);
        //循环
        //gsyVideoPlayer.setLooping(true);
        videoView.setNeedLockFull(true);
        //gsyVideoPlayer.setSpeed(2);

        videoView.setPlayPosition(0);

        videoView.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                if (!videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    //是否静音
                    GSYVideoManager.instance().setNeedMute(false);
                }
                if (videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setLastListener(videoView);
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                videoView.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        AutoSizeUtils.closeAdapt(getResources());
        GSYVideoManager.releaseAllVideos();
    }
}
