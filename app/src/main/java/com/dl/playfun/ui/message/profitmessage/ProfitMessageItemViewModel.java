package com.dl.playfun.ui.message.profitmessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.ProfitMessageEntity;
import com.dl.playfun.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class ProfitMessageItemViewModel extends ItemViewModel<ProfitMessageViewModel> {

    public ObservableField<ProfitMessageEntity> itemEntity = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ProfitMessageItemViewModel.this);
            viewModel.onItemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ProfitMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public ProfitMessageItemViewModel(@NonNull ProfitMessageViewModel viewModel, ProfitMessageEntity messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}
