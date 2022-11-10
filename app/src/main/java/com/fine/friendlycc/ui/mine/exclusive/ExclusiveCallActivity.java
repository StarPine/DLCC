package com.fine.friendlycc.ui.mine.exclusive;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.ActivityExclusivecallBinding;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.ui.dialog.ExclusiveAccostDialog;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.widget.BasicToolbar;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

/**
 * 修改备注：我的专属招呼activity
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/11 20:01
 */
public class ExclusiveCallActivity extends BaseActivity<ActivityExclusivecallBinding, ExclusiveCallViewModel> implements BasicToolbar.ToolbarListener {

    @Override
    public void onBackClick(BasicToolbar toolbar) {
        finish();
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_exclusivecall;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ExclusiveCallViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(ExclusiveCallViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public void initData() {
        super.initData();
        binding.basicToolbar.setToolbarListener(this);
        viewModel.getExclusiveAccost();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.editText.observe(this, unused -> {
            ExclusiveAccostDialog.getInstance(ExclusiveCallActivity.this)
                    .setOnClickListener(new ExclusiveAccostDialog.DialogOnClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, String content) {
                            if (content.length() <= 0) {
                                ToastCenterUtils.showShort(R.string.playcc_text_accost_tips);
                                return;
                            }
                            if (TUIChatUtils.isContains(content, viewModel.sensitiveWords.get())) {
                                ToastCenterUtils.showShort(R.string.playcc_text_accost_tips2);
                                return;
                            }
                            dialog.dismiss();
                            viewModel.setExclusiveAccost(viewModel.TEXT_TYPE, content, -1);
                        }
                    })
                    .editAccostContentDialog(viewModel.textContent.get())
                    .show();
        });

        viewModel.editAudio.observe(this, unused -> {
            ExclusiveAccostDialog.getInstance(ExclusiveCallActivity.this)
                    .setOnClickListener(new ExclusiveAccostDialog.DialogOnClickListener() {
                        @Override
                        public void onConfirmAudio(Dialog dialog, String content, int second) {
                            dialog.dismiss();
                            viewModel.uploadUserSoundFile(viewModel.AUDIO_TYPE, content, second);
                        }
                    })
                    .editAccostAudioDialog(viewModel.audioContent.get())
                    .show();
        });

        //播放录音
        viewModel.playAudio.observe(this, unused -> {
            if (AudioPlayer.getInstance().isPlaying()) {
                AudioPlayer.getInstance().stopPlay();
                return;
            }
            Animation animation = new ScaleAnimation(0.8f,1.0f,0.8f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setRepeatMode(ValueAnimator.REVERSE);
            animation.setRepeatCount(ValueAnimator.INFINITE);
            animation.setDuration(300);
            binding.ivAnim.startAnimation(animation);
            AudioPlayer.getInstance().startPlay(StringUtil.getFullAudioUrl(viewModel.audioContent.get()), new AudioPlayer.Callback() {
                @Override
                public void onCompletion(Boolean success, Boolean isOutTime) {
                    binding.ivAnim.clearAnimation();
                }
            });
        });


    }
}
