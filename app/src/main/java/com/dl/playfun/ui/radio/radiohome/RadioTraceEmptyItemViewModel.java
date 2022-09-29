package com.dl.playfun.ui.radio.radiohome;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

/**
 * Author: 彭石林
 * Time: 2021/11/3 14:44
 * Description: This is RadioTraceEmptyItemViewModel
 */
public class RadioTraceEmptyItemViewModel extends MultiItemViewModel<BaseViewModel> {
    public ObservableField<String> emptyTextField = new ObservableField<String>();
    public RadioTraceEmptyItemViewModel(@NonNull BaseViewModel viewModel,String emptyText) {
        super(viewModel);
        this.emptyTextField.set(emptyText);
    }
}
