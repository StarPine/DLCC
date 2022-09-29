package com.dl.playfun.ui.mine.wallet.coin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.app.AppContext;
import com.dl.playfun.entity.UserCoinItemEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class CoinItemViewModel extends MultiItemViewModel<CoinViewModel> {

    public ObservableField<UserCoinItemEntity> itemEntity = new ObservableField<>();

    public ObservableField<Boolean> isCancel = new ObservableField<>(false);
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            if (itemEntity.get().getUser() == null || itemEntity.get().getUser().getId() == 0) {
                return;
            }
            if (itemEntity.get().getUser().getId() == ConfigManager.getInstance().getAppRepository().readUserData().getId()) {
                return;
            }
            Bundle bundle = UserDetailFragment.getStartBundle(itemEntity.get().getUser().getId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public CoinItemViewModel(@NonNull CoinViewModel viewModel, UserCoinItemEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

}
