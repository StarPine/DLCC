package com.fine.friendlycc.viewadapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.widget.videoview.SampleCoverVideo;
import com.fine.friendlycc.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class VideoViewAdapter {

    private static StandardGSYVideoPlayer curPlayer;
    private static StandardGSYVideoPlayer itemPlayer;

    protected static OrientationUtils orientationUtils;

    protected static boolean isPlay;

    protected static boolean isFull;

    @BindingAdapter(value = {"videoUri", "videoCompletionCommand", "position","isLocalFile"}, requireAll = false)
    public static void setVideoUri(SampleCoverVideo videoView, String uri, BindingCommand videoCompletionCommand, int position,Boolean isLocalFile) {
        if (videoView == null || uri == null) {
            return;
        }
        String url = null;
        if(isLocalFile==null){
                url = StringUtil.getFullImageUrl(uri);
        }else{
            if(!isLocalFile){
                url = StringUtil.getFullImageUrl(uri);
            }else{
                url = uri;
            }
        }
        //videoView.setUrl(url);
        // url = StringUtil.getFullAudioUrl(url);
        videoView.loadCoverImage(url, R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error);
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
//        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resolveFullBtn(gsyVideoPlayer);
//            }
//        });
        videoView.setRotateViewAuto(false);
        videoView.setLockLand(false);
        videoView.setPlayTag("SampleCoverVideoPlayer");
        //gsyVideoPlayer.c(true);
        videoView.setReleaseWhenLossAudio(true);
        videoView.setAutoFullWithSize(true);
        videoView.setShowFullAnimation(true);
        videoView.setIsTouchWiget(false);
        //循环
        //gsyVideoPlayer.setLooping(true);
        videoView.setNeedLockFull(true);

        //gsyVideoPlayer.setSpeed(2);

        videoView.setPlayPosition(position);

        videoView.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                boolean full = videoView.getCurrentPlayer().isIfCurrentIsFullscreen();
                if (!videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    //是否静音
                    GSYVideoManager.instance().setNeedMute(false);
                }
                if (videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setLastListener(videoView);
                }
                curPlayer = (StandardGSYVideoPlayer) objects[1];
                itemPlayer = videoView;
                isPlay = true;
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                isFull = false;
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                isFull = true;
                videoView.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                if (videoCompletionCommand != null) {
                    videoCompletionCommand.execute();
                }
                curPlayer = null;
                itemPlayer = null;
                isPlay = false;
                isFull = false;
            }
        });
    }

    @BindingAdapter({"playStatus"})
    public static void setVideoPlayStatus(SampleCoverVideo videoView, int playStatus) {
        if (videoView == null) {
            return;
        }
        GSYVideoManager.onPause();
    }

    //首页列表播放视频
    @BindingAdapter(value = {"videoUrl","position","imageRes","isVideoPlayer"})
    public static void setSampleCoverVideo(SampleCoverVideo gsyVideoPlayer,String url,int position,Integer imageRes,boolean isVideoPlayer){
        //不是视频播放。关闭继续执行绑定
        if(!isVideoPlayer){
            return;
        }
        url = StringUtil.getFullAudioUrl(url);
        gsyVideoPlayer.loadCoverImage(url, imageRes);
        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();

        //默认缓存路径
        //使用lazy的set可以避免滑动卡的情况存在
        gsyVideoPlayer.setUpLazy(url, true, null, null, "这是title");
        try {
            //增加title
            gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            gsyVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
        }catch(Exception e){

        }

        //设置全屏按键功能
//        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resolveFullBtn(gsyVideoPlayer);
//            }
//        });
        gsyVideoPlayer.setRotateViewAuto(false);
        gsyVideoPlayer.setLockLand(false);
        gsyVideoPlayer.setPlayTag("SampleCoverVideoPlayer");
        //gsyVideoPlayer.c(true);
        gsyVideoPlayer.setReleaseWhenLossAudio(true);
        gsyVideoPlayer.setAutoFullWithSize(true);
        gsyVideoPlayer.setShowFullAnimation(true);
        gsyVideoPlayer.setIsTouchWiget(false);
        //循环
        //gsyVideoPlayer.setLooping(true);
        gsyVideoPlayer.setNeedLockFull(true);

        //gsyVideoPlayer.setSpeed(2);

        gsyVideoPlayer.setPlayPosition(position);

        gsyVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                boolean full = gsyVideoPlayer.getCurrentPlayer().isIfCurrentIsFullscreen();
                if (!gsyVideoPlayer.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    //是否静音
                    GSYVideoManager.instance().setNeedMute(false);
                }
                if (gsyVideoPlayer.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setLastListener(gsyVideoPlayer);
                }
                curPlayer = (StandardGSYVideoPlayer) objects[1];
                itemPlayer = gsyVideoPlayer;
                isPlay = true;
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                isFull = false;
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                isFull = true;
                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                curPlayer = null;
                itemPlayer = null;
                isPlay = false;
                isFull = false;
            }
        });
    }

}
