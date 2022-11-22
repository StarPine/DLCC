package com.fine.friendlycc.ui.message.profitmessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.ProfitMessageBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class ProfitMessageItemViewModel extends ItemViewModel<ProfitMessageViewModel> {

    public ObservableField<ProfitMessageBean> itemEntity = new ObservableField<>();
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

    public ProfitMessageItemViewModel(@NonNull ProfitMessageViewModel viewModel, ProfitMessageBean messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}