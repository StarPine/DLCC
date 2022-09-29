package com.dl.playfun.widget.videoview;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.dl.playfun.R;
import com.dl.playfun.manager.VideoProxyCacheManager;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author litchi
 */
public class JMVideoView extends FrameLayout implements View.OnClickListener, OnPreparedListener, OnCompletionListener {

    boolean isCompletion = false;
    private VideoView videoView;
    private ImageView ivPlay;
    private ProgressBar progressBar;
    private String url;
    private BindingCommand videoCompletionCommand;

    public JMVideoView(Context context) {
        super(context);
        initWithContext(context);
    }

    public JMVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public JMVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithContext(context);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if (url != null && videoView != null) {
            progressBar.setVisibility(View.VISIBLE);

            if (url.startsWith("http")) {
                String proxyUrl = VideoProxyCacheManager.getInstance().getProxy().getProxyUrl(url);
                videoView.setVideoPath(proxyUrl);
            } else {
                videoView.setVideoURI(Uri.parse(url));
            }
        }
    }

    public BindingCommand getVideoCompletionCommand() {
        return videoCompletionCommand;
    }

    public void setVideoCompletionCommand(BindingCommand videoCompletionCommand) {
        this.videoCompletionCommand = videoCompletionCommand;
    }

    private void initWithContext(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_jm_video_view, this);
        videoView = findViewById(R.id.video_view);
        ivPlay = findViewById(R.id.iv_play);
        progressBar = findViewById(R.id.progress_bar);

        ivPlay.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        ivPlay.setOnClickListener(this);
        videoView.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setControls(null);
    }

    public void playPause() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            ivPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrepared() {
//        videoView.start();
        progressBar.setVisibility(View.GONE);
        if (!videoView.isPlaying()) {
            ivPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCompletion() {
        isCompletion = true;
        ivPlay.setVisibility(View.VISIBLE);
        if (videoCompletionCommand != null) {
            videoCompletionCommand.execute();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_play) {
            if (isCompletion) {
                videoView.restart();
            } else {
                videoView.start();
            }
            isCompletion = false;
            ivPlay.setVisibility(View.GONE);
        } else if (view.getId() == R.id.video_view) {
            if (videoView.isPlaying()) {
                videoView.pause();
                ivPlay.setVisibility(View.VISIBLE);
            }
        }
    }

}
