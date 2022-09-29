package com.dl.playfun.ui.radio.radiohome;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

/**
 * Author: 彭石林
 * Time: 2022/7/29 15:01
 * Description: This is RadioFragmentLifecycle
 */
public class RadioFragmentLifecycle implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
        }
        GSYVideoManager.onPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        GSYVideoManager.onResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
        }
        GSYVideoManager.releaseAllVideos();
    }

}
