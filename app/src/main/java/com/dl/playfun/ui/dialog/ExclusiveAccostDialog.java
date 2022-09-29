package com.dl.playfun.ui.dialog;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.widget.dialog.MMAlertDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;
import com.tencent.qcloud.tuikit.tuichat.util.PermissionHelper;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

import java.io.File;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/12 11:26
 */
public class ExclusiveAccostDialog {
    private static volatile ExclusiveAccostDialog INSTANCE;
    private Context mContext;
    private ExclusiveAccostDialog.DialogOnClickListener onClickListener;
    private int startTime = 0;
    private CountDownTimer downTimer;
    private String playPath = null;
    private boolean deleteFlag = false;

    public static ExclusiveAccostDialog getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ExclusiveAccostDialog.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExclusiveAccostDialog(context);
                }
            }
        } else {
            init(context);
        }
        return INSTANCE;
    }

    private static void init(Context context) {
        INSTANCE.mContext = context;
    }

    private ExclusiveAccostDialog(Context context) {
        this.mContext = context;
    }

    public ExclusiveAccostDialog setOnClickListener(ExclusiveAccostDialog.DialogOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return INSTANCE;
    }

    /**
     * 编辑搭讪文本内容dialog
     *
     * @return
     */
    public Dialog editAccostContentDialog(String content) {
        Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_text_accost, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);
        TextView wordCount = contentView.findViewById(R.id.tv_word_count);
        EditText edAccostText = contentView.findViewById(R.id.et_accost);
        ImageView close = contentView.findViewById(R.id.iv_close);
        if (!TextUtils.isEmpty(content)) {
            edAccostText.setText(content);
            wordCount.setText(String.format(mContext.getString(R.string.playfun_text_accost_number_of_fonts), edAccostText.getText().length() + ""));
        } else {
            wordCount.setText(String.format(mContext.getString(R.string.playfun_text_accost_number_of_fonts), "0"));

        }
        close.setOnClickListener(v -> dialog.dismiss());
        edAccostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordCount.setText(String.format(mContext.getString(R.string.playfun_text_accost_number_of_fonts), edAccostText.getText().length() + ""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmBtn.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onConfirm(dialog, edAccostText.getText().toString());
            }
        });
        return dialog;
    }

    /**
     * 编辑录音搭讪内容dialog
     *
     * @param content
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    public Dialog editAccostAudioDialog(String content) {
        Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_audio_accost, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        //view初始化
        Button recording = contentView.findViewById(R.id.btn_recording);
        TextView timing = contentView.findViewById(R.id.tv_timing);
        ImageView close = contentView.findViewById(R.id.iv_close);
        ImageView audioNomal = contentView.findViewById(R.id.iv_audio_nomal);
        ImageView audioPlayable = contentView.findViewById(R.id.iv_audio_playable);
        ImageView ivReset = contentView.findViewById(R.id.iv_reset);
        ImageView ivOk = contentView.findViewById(R.id.iv_ok);
        LinearLayout llCompletiion = contentView.findViewById(R.id.ll_completion);

        //事件监听
        close.setOnClickListener(v -> dialog.dismiss());
        recording.setOnTouchListener((v, event) -> {
            try {
                new RxPermissions((FragmentActivity) mContext)
                        .request(Manifest.permission.RECORD_AUDIO)
                        .subscribe(granted -> {
                            if (granted) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        audioNomal.setImageDrawable(mContext.getDrawable(R.drawable.icon_anim_audio_recording));
                                        startAnimal(audioNomal);
                                        recording.setBackgroundResource(R.drawable.button_purple_background2);
                                        recording.setText(mContext.getString(R.string.playfun_audio_accost_recording));
                                        startTime = 0;
                                        startRecord(timing);
                                        setTimeText(timing);
                                        startTimer(timing, recording, audioPlayable, llCompletiion, audioNomal);
                                        deleteFlag = false;
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        audioNomal.clearAnimation();
                                        stopRecord();
                                        if (startTime < 1) {
                                            ToastUtil.toastShortMessage(StringUtils.getString(R.string.playfun_audio_tips_text_one));
                                            startTime = 0;
                                            deleteFlag = true;
                                            resetStatus(recording, timing, audioNomal, llCompletiion, audioPlayable);
                                        } else {
                                            recordCompletion(recording, audioPlayable, llCompletiion);
                                        }
                                        stopTimer();
                                        break;
                                }
                            } else {
                            }
                        });
            } catch (Exception e) {

            }

            return true;
        });
        ivOk.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onConfirmAudio(dialog, playPath, startTime);
            }
        });
        audioPlayable.setOnClickListener(v -> {
            playAudio(audioPlayable);
        });
        ivReset.setOnClickListener(v -> {
            resetStatus(recording, timing, audioNomal, llCompletiion, audioPlayable);
        });

        return dialog;
    }

    /**
     * 播放录音
     *
     * @param audioPlayable
     */
    private void playAudio(ImageView audioPlayable) {
        if (AudioPlayer.getInstance().isPlaying()) {
            audioPlayable.setImageResource(R.drawable.icon_stop_audio);
            AudioPlayer.getInstance().stopPlay();
            return;
        } else {
            audioPlayable.setImageResource(R.drawable.icon_playing_audio);
        }
        AudioPlayer.getInstance().startPlay(playPath, new AudioPlayer.Callback() {
            @Override
            public void onCompletion(Boolean success, Boolean isOutTime) {
                audioPlayable.setImageResource(R.drawable.icon_stop_audio);
            }
        });
    }

    private void recordCompletion(Button recording, ImageView audioPlayable, LinearLayout llCompletiion) {
        audioPlayable.setImageDrawable(mContext.getDrawable(R.drawable.icon_stop_audio));
        llCompletiion.setVisibility(View.VISIBLE);
        recording.setVisibility(View.GONE);
        audioPlayable.setVisibility(View.VISIBLE);
    }

    /**
     * 启动录制动画
     *
     * @param audioNomal
     */
    private void startAnimal(ImageView audioNomal) {
        Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        audioNomal.setAnimation(animation);
    }

    private void resetStatus(Button recording, TextView timing, ImageView audioNomal, LinearLayout llCompletiion, ImageView audioPlayable) {
        audioNomal.setImageDrawable(mContext.getDrawable(R.drawable.icon_audio_nomal));
        timing.setText(mContext.getString(R.string.playfun_audio_accost_tip));
        recording.setBackgroundResource(R.drawable.button_purple_background);
        recording.setText(mContext.getString(R.string.playfun_audio_accost_long_click));
        llCompletiion.setVisibility(View.GONE);
        audioPlayable.setVisibility(View.GONE);
        recording.setVisibility(View.VISIBLE);
        audioNomal.setVisibility(View.VISIBLE);
    }

    /**
     * 设置计时时间
     *
     * @param timing
     */
    private void setTimeText(TextView timing) {
        timing.setText(String.format("00:%02d", startTime));
    }

    /**
     * 开始计时
     */
    public void startTimer(TextView timing, Button recording, ImageView audioPlayable, LinearLayout llCompletiion, ImageView audioNomal) {
        //倒计时15秒，一次1秒
        downTimer = new CountDownTimer(15 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime++;
                setTimeText(timing);
            }

            @Override
            public void onFinish() {
                audioNomal.clearAnimation();
                recordCompletion(recording, audioPlayable, llCompletiion);
                stopRecord();
                stopTimer();
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
        }
    }

    /**
     * 开始录音
     *
     * @param timing
     */
    public void startRecord(TextView timing) {
        AudioPlayer.getInstance().startRecord(new AudioPlayer.Callback() {
            @Override
            public void onCompletion(Boolean success, Boolean isOutTime) {
                if (deleteFlag) {
                    try {
                        File deleteFile = new File(AudioPlayer.getInstance().getPath());
                        deleteFile.delete();
                    } catch (Exception e) {

                    }
                } else {
                    if (success) {
                        playPath = AudioPlayer.getInstance().getPath();
                        setTimeText(timing);
                    }
                }
            }
        });
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        AudioPlayer.getInstance().stopRecord();
    }

    public interface DialogOnClickListener {

        default void close() {
        }

        default void onConfirm(Dialog dialog, String content) {

        }

        default void onConfirmAudio(Dialog dialog, String content, int second) {

        }

    }
}
