package com.fine.friendlycc.ui.mine.wallet.girl;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.UserProfitPageInfoEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import java.math.BigDecimal;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/12/6 16:23
 * Description: This is TwDollarMoneyItemViewModel
 */
public class TwDollarMoneyItemViewModel extends MultiItemViewModel<TwDollarMoneyViewModel> {
    public ObservableField<UserProfitPageInfoEntity> itemEntity = new ObservableField<>();

    public TwDollarMoneyItemViewModel(@NonNull TwDollarMoneyViewModel viewModel,UserProfitPageInfoEntity userProfitPageInfoEntity) {
        super(viewModel);
        this.itemEntity.set(userProfitPageInfoEntity);
    }

    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            if (itemEntity.get().getUserId() == null || itemEntity.get().getUserId() == 0) {
                return;
            }
            if (itemEntity.get().getUserId() == ConfigManager.getInstance().getAppRepository().readUserData().getId()) {
                return;
            }
            //新需求，取消流水点击跳转
//            Bundle bundle = UserDetailFragment.getStartBundle(itemEntity.get().getUserId());
//            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public String getMoneyItemText() {
        double amount = itemEntity.get().getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (amount > 0.0f) {
            return "+" + String.format("%.2f", amount);
        }
        return String.format("%.2f", amount) + "";
    }

}
