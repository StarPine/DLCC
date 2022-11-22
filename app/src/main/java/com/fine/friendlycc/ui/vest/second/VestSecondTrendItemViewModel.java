package com.fine.friendlycc.ui.vest.second;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.BroadcastBean;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

/**
 * @author wulei
 */
public class VestSecondTrendItemViewModel extends MultiItemViewModel<BaseViewModel> {

    public ObservableField<BroadcastBean> broadcastEntity = new ObservableField<>();

    public VestSecondTrendItemViewModel(@NonNull VestSecondViewModel viewModel, BroadcastBean broadcastEntity) {
        super(viewModel);
        this.broadcastEntity.set(broadcastEntity);
    }

}