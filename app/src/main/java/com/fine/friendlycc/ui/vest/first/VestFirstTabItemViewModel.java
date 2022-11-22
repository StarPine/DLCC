package com.fine.friendlycc.ui.vest.first;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.bean.ParkItemBean;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2022/7/22 18:09
 * Description: This is HomeMainItemViewModel
 */
public class VestFirstTabItemViewModel extends MultiItemViewModel<VestFirstViewModel> {

    public ObservableField<ParkItemBean> itemData = new ObservableField<>();

    public VestFirstTabItemViewModel(@NonNull VestFirstViewModel viewModel, ParkItemBean parkItemEntity) {
        super(viewModel);
        itemData .set(parkItemEntity);
    }

    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(VestFirstTabItemViewModel.this);
            Bundle bundle = UserDetailFragment.getStartBundle(itemData.get().getId());
            bundle.putInt(UserDetailFragment.ARG_USER_DETAIL_POSITION,position);
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public String getOnlineStatus() {
        String onlineStatus = null;
        if (itemData.get().getCallingStatus() == 0){
            if (itemData.get().getIsOnline() == -1) {
                onlineStatus = null;
            } else if (itemData.get().getIsOnline() == 1) {
                onlineStatus = StringUtils.getString(R.string.playcc_on_line);
            } else if (itemData.get().getIsOnline() == 0) {
                onlineStatus = null;
            }
        }else if (itemData.get().getCallingStatus() == 1){
            onlineStatus = StringUtils.getString(R.string.playcc_calling);
        }else if (itemData.get().getCallingStatus() == 2){
            onlineStatus = StringUtils.getString(R.string.playcc_in_video);
        }

        return onlineStatus;
    }

    public int onLineColor(ParkItemBean itemEntity){
        if (itemEntity == null)return -1;
        if (itemEntity.getCallingStatus() == 0){
            if (itemEntity.getIsOnline() == 1) {
                return CCApplication.instance().getResources().getColor(R.color.green2);
            }
        }else {
            return CCApplication.instance().getResources().getColor(R.color.red_9);
        }
        return CCApplication.instance().getResources().getColor(R.color.text_9EA1B0);
    }
}