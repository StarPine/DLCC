package com.dl.playfun.ui.vest.second;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.entity.AdUserItemEntity;
import com.dl.playfun.ui.radio.radiohome.RadioViewModel;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.utils.StringUtil;
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
public class VestSecondHeadItemViewModel extends MultiItemViewModel<VestSecondViewModel> {

    public ObservableField<AdUserItemEntity> adUserItemEntity = new ObservableField<>();

    public VestSecondHeadItemViewModel(@NonNull VestSecondViewModel viewModel, AdUserItemEntity adUserItemEntity) {
        super(viewModel);
        this.adUserItemEntity.set(adUserItemEntity);
    }
    //item点击
    public BindingCommand itemClick = new BindingCommand(()->{
        try {
            AppContext.instance().logEvent(AppsFlyerEvent.Nearby_Follow);
            Bundle bundle = UserDetailFragment.getStartBundle(adUserItemEntity.get().getUserId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

}
