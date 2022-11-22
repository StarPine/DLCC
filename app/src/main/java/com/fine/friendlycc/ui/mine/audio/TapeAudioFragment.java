package com.fine.friendlycc.ui.mine.audio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragentTapeAudioBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

import java.io.File;
import java.math.BigDecimal;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/10/21 10:21
 * Description: This is TapeAudioFragment
 */
public class TapeAudioFragment extends BaseFragment<FragentTapeAudioBinding,TapeAudioViewModel> {


    private final String default_time_text = "00:00/00:20";
    private final int MaxTime = 20;
    private int startTime = 0;
    private CountDownTimer downTimer;
    private boolean deleteFlag = false;
    private String playPath = null;
    private int playgDuration = 0;

    public  String getTimeText(int time) {
        String val = "";
        if(time<10){
            val = "0"+time;
        }else{
            if(time>MaxTime){
                time  =MaxTime;
            }
            val = time+"";
        }
        return val;
    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragent_tape_audio;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public TapeAudioViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(TapeAudioViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        try {
            new RxPermissions(this)
                    .request(Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {

                        } else {
                            MMAlertDialog.AlertAudioPermissions(getContext(), false, new MMAlertDialog.DilodAlertInterface() {
                                @Override
                                public void confirm(DialogInterface dialog, int which, int sel_Index) {
                                    dialog.dismiss();
                                    viewModel.pop();
                                }
                                @Override
                                public void cancel(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    });
        } catch (Exception e) {

        }

        binding.circleRecordSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Status.mIsShowFloatWindow){
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        ToastUtils.showShort(R.string.audio_in_call);
                    }
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = 0;
                        startAudioCall();
                        binding.myTime.setText(default_time_text);
                        binding.myTime.setVisibility(View.VISIBLE);
                        startAudio();
                        deleteFlag = false;
                        CCApplication.instance().logEvent(AppsFlyerEvent.voice_record);
                        break;
                    case MotionEvent.ACTION_UP:
                        stopAudioCall();
                        if (startTime < 5) {
                            ToastUtil.toastShortMessage(StringUtils.getString(R.string.playcc_tape_audio_error_text));
                            //ToastCenterUtils.showShort(R.string.tape_audio_error_text);
                            //ToastUtils.showShort(R.string.tape_audio_error_text);
                            startTime = 0;
                            deleteFlag = true;
                        } else {
                            binding.circleRecordSurfaceView.resetView();
                            binding.startAudio.setImageResource(R.drawable.audio_backdrop_img_start);
                            stopAudio();
                            binding.audioLayoutTouch.setVisibility(View.GONE);
                            binding.audioStartLayout.setVisibility(View.GONE);
                            binding.audioSuccessLayout.setVisibility(View.VISIBLE);
                        }
                        binding.myTime.setVisibility(View.GONE);
                        binding.circleRecordSurfaceView.resetView();
                        binding.startAudio.setImageResource(R.drawable.audio_backdrop_img_start);
                        stopAudio();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //重录
        binding.audioRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AudioPlayer.getInstance().isPlaying()) {
                    binding.startPlay.setImageResource(R.drawable.audio_backdrop_img_start_play);
                    AudioPlayer.getInstance().stopPlay();
                }
                binding.circleRecordSurfaceView.setVisibility(View.VISIBLE);
                binding.audioSuccessLayout.setVisibility(View.GONE);
                binding.audioStartLayout.setVisibility(View.VISIBLE);
                binding.audioLayoutTouch.setVisibility(View.VISIBLE);
            }
        });
        //播放按钮
        binding.startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AudioPlayer.getInstance().isPlaying()) {
                    binding.startPlay.setImageResource(R.drawable.audio_backdrop_img_start_play);
                    AudioPlayer.getInstance().stopPlay();
                    return;
                } else {
                    binding.startPlay.setImageResource(R.drawable.audio_backdrop_img_start_stop);
                }
                AudioPlayer.getInstance().startPlay(playPath, new AudioPlayer.Callback() {
                    @Override
                    public void onCompletion(Boolean success, Boolean isOutTime) {
                        binding.startPlay.setImageResource(R.drawable.audio_backdrop_img_start_play);
                    }
                });
            }
        });
        //提交按钮
        binding.audioSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCApplication.instance().logEvent(AppsFlyerEvent.voice_submit);
                viewModel.uploadUserSoundFile(playPath,startTime);
            }
        });
    }

    public void startAudioCall(){
        AudioPlayer.getInstance().startRecord(new AudioPlayer.Callback() {
            @Override
            public void onCompletion(Boolean success, Boolean isOutTime) {
                if(deleteFlag){
                    try {
                        File deleteFile = new File(AudioPlayer.getInstance().getPath());
                        deleteFile.delete();
                    }catch(Exception e){

                    }
                }else{
                    if(success){
                        playPath = AudioPlayer.getInstance().getPath();
                        playgDuration = AudioPlayer.getInstance().getDuration();
                        binding.myTime.setText("00:"+getTimeText(playgDuration>=MaxTime?MaxTime:playgDuration)+"/00:20");
                    }
                }
            }
        });
    }
    public void stopAudioCall(){
        AudioPlayer.getInstance().stopRecord();
    }

    @Override
    public void initViewObservable() {
        viewModel.getUserSound();
        super.initViewObservable();
        viewModel.showDialog.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer money) {
                TraceDialog.getInstance(getContext())
                        .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(Dialog dialog) {//前往充值VIP
                                dialog.dismiss();
                                RxBus.getDefault().post(new TaskMainTabEvent(false,true));
                                pop();
                                //viewModel.startWithPop(TaskCenterFragment.class.getCanonicalName());
                            }
                        })
                        .setCannelOnclick(new TraceDialog.CannelOnclick() {
                            @Override
                            public void cannel(Dialog dialog) {
                                dialog.dismiss();
                                pop();
                            }
                        })
                        .AlertAudioPlayer(money).show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    public void startAudio(){
        /**
         * 倒计时15秒，一次1秒
         */
        downTimer = new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime ++;
                BigDecimal result1 = new BigDecimal(millisUntilFinished);
                BigDecimal divideBigDecimal = result1.divide(new BigDecimal(20000),2,BigDecimal.ROUND_FLOOR);
                binding.circleRecordSurfaceView.setProgress(divideBigDecimal.floatValue());
                int val =(int)(20000-millisUntilFinished)/1000;
                binding.myTime.setText("00:"+getTimeText(val>=MaxTime?MaxTime:val)+"/00:20");
                Log.e("当前倒计时",millisUntilFinished+"========="+val+"======"+startTime);
            }

            @Override
            public void onFinish() {
                stopAudioCall();
                if (startTime < 5) {
                    ToastUtil.toastShortMessage(StringUtils.getString(R.string.playcc_tape_audio_error_text));
                    //ToastCenterUtils.showShort(R.string.tape_audio_error_text);
                    //ToastUtils.showShort(R.string.tape_audio_error_text);
                    startTime = 0;
                    deleteFlag = true;
                } else {
                    binding.circleRecordSurfaceView.resetView();
                    binding.circleRecordSurfaceView.setVisibility(View.GONE);
                    binding.audioStartLayout.setVisibility(View.GONE);
                    binding.audioSuccessLayout.setVisibility(View.VISIBLE);
                }
                binding.circleRecordSurfaceView.resetView();
                binding.startAudio.setImageResource(R.drawable.audio_backdrop_img_start);
                stopAudio();
            }
        };
        downTimer.start();
    }
    public void stopAudio(){
        if(downTimer!=null){
            downTimer.cancel();
        }
    }

}
