package com.fine.friendlycc.ui.vest.second;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.entity.AdUserItemEntity;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

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
