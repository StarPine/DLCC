package com.fine.friendlycc.ui.ranklisk.ranklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.RankListItemEntity;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class RankListItemViewModel extends MultiItemViewModel<RankListViewModel> {

    public ObservableField<RankListItemEntity> itemEntity = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            Bundle bundle = UserDetailFragment.getStartBundle(itemEntity.get().getUserId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public RankListItemViewModel(@NonNull RankListViewModel viewModel, RankListItemEntity entity) {
        super(viewModel);
        itemEntity.set(entity);
    }

}
