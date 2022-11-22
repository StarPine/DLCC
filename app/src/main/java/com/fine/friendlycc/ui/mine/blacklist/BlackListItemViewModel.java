package com.fine.friendlycc.ui.mine.blacklist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.BlackBean;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class BlackListItemViewModel extends MultiItemViewModel<BlacklistViewModel> {

    public ObservableField<BlackBean> itemEntity = new ObservableField<>();

    public ObservableField<Boolean> isCancel = new ObservableField<>(false);
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            Bundle bundle = UserDetailFragment.getStartBundle(itemEntity.get().getUser().getId());
            viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand delBlackClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(BlackListItemViewModel.this);
            if (!isCancel.get()) {
                viewModel.delBlackList(position);
            } else {
                viewModel.addBlackList(position);
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public BlackListItemViewModel(@NonNull BlacklistViewModel viewModel, BlackBean itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }

    public int isRealManVisible() {
        if (itemEntity.get().getUser().getIsVip() != 1) {
            if (itemEntity.get().getUser().getCertification() == 1) {
                return View.VISIBLE;
            } else {
                return View.GONE;
            }
        }else {
            return View.GONE;
        }
    }

    public int isVipVisible() {
        if (itemEntity.get().getUser().getSex() != null && itemEntity.get().getUser().getSex() == 1 && itemEntity.get().getUser().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int isGoddessVisible() {
        if (itemEntity.get().getUser().getSex() == 0 && itemEntity.get().getUser().getIsVip() == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }
}