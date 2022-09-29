package com.dl.playfun.ui.mine.broadcast.mytrends.givelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.BaseUserBeanEntity;
import com.dl.playfun.utils.ExceptionReportUtils;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * @author litchi
 */
public class GiveListItemViewModel extends MultiItemViewModel<BaseViewModel> {
    public ObservableField<BaseUserBeanEntity> baseUserBeanEntityObservableField = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
                Bundle bundle = UserDetailFragment.getStartBundle(baseUserBeanEntityObservableField.get().getId());
                viewModel.start(UserDetailFragment.class.getCanonicalName(), bundle);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public GiveListItemViewModel(@NonNull BaseViewModel viewModel, BaseUserBeanEntity baseUserBeanEntity) {
        super(viewModel);
        this.baseUserBeanEntityObservableField.set(baseUserBeanEntity);
    }
}