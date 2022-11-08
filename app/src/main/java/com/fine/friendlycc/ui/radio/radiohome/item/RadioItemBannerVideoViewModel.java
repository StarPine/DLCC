package com.fine.friendlycc.ui.radio.radiohome.item;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.entity.AdUserItemEntity;
import com.fine.friendlycc.ui.radio.radiohome.RadioViewModel;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.tencent.qcloud.tuicore.Status;
import com.tencent.qcloud.tuikit.tuichat.component.AudioPlayer;

import java.util.Objects;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.goldze.mvvmhabit.utils.Utils;

/**
 * Author: 彭石林
 * Time: 2022/7/26 19:17
 * Description: This is RadioItemBannerVideoViewModel
 */
public class RadioItemBannerVideoViewModel extends MultiItemViewModel<RadioViewModel> {

    public ObservableField<AdUserItemEntity> adUserItemEntity = new ObservableField<>();
    public ObservableBoolean isPlaying = new ObservableBoolean(false);

    public RadioItemBannerVideoViewModel(@NonNull RadioViewModel viewModel,AdUserItemEntity adUserItemEntity) {
        super(viewModel);
        this.adUserItemEntity.set(adUserItemEntity);
    }
    //item点击
    public BindingCommand itemClick = new BindingCommand(()->{
        int position = viewModel.radioItemsAdUser.indexOf(this);
        if(position!=-1){
            viewModel.itemClickChangeIdx(position);
        }
        try {
            AppContext.instance().logEvent(AppsFlyerEvent.Nearby_Follow);
            Bundle bundle = UserDetailFragment.getStartBundle(adUserItemEntity.get().getUserId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    //获取播放状态图片
    public Drawable gerDrawablePlay(boolean play){
        return play ? Utils.getContext().getDrawable(R.drawable.radio_item_stop_audio) :Utils.getContext().getDrawable(R.drawable.radio_item_play_audio) ;
    }

    //播放语音
    public BindingCommand  audioPlayClickCommand = new BindingCommand(() -> {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(com.tencent.qcloud.tuikit.tuichat.R.string.audio_in_call);
            return;
        }
        if (AudioPlayer.getInstance().isPlaying()) {
            AudioPlayer.getInstance().stopPlay();
            isPlaying.set(false);
            return;
        }
        isPlaying.set(true);
        //全局音频播放单列
        AudioPlayer.getInstance().startPlay(StringUtil.getFullAudioUrl(Objects.requireNonNull(adUserItemEntity.get()).getSound()), (success, isOutTime) ->{
            isPlaying.set(false);
        });
        int position = viewModel.radioItemsAdUser.indexOf(this);
        if (position != - 1){
            viewModel.itemClickPlayAudio(position);
        }
    });

    //拨打视频
    public BindingCommand callVideoClickCommand = new BindingCommand(() ->
    {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(com.tencent.qcloud.tuikit.tuichat.R.string.audio_in_call);
            return;
        }
        viewModel.itemClickCallVideo(adUserItemEntity.get());
    });

    public Integer getSoundShow() {
        if (adUserItemEntity.get() != null && StringUtils.isEmpty(adUserItemEntity.get().getSound())) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }
    }
}
