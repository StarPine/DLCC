package com.dl.playfun.ui.login.choose;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.ChooseAreaItemEntity;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2022/7/5 17:54
 * Description: This is ItemChooseAreaViewModel
 */
public class ItemChooseAreaViewModel extends MultiItemViewModel<ChooseAreaViewModel> {
    public ObservableField<ChooseAreaItemEntity> itemEntity = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(() -> {
        viewModel.chooseAreaClick(itemEntity.get());
    });

    public ItemChooseAreaViewModel(@NonNull ChooseAreaViewModel viewModel, ChooseAreaItemEntity chooseAreaEntity) {
        super(viewModel);
        itemEntity.set(chooseAreaEntity);
    }

    public String getAreaPhoneCode(ChooseAreaItemEntity chooseAreaItem) {
        if (chooseAreaItem != null) {
            if (chooseAreaItem.getCode() != null) {
                return "+" + chooseAreaItem.getCode();
            }
        }
        return null;
    }
}
