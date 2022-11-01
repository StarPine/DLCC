package com.fine.friendlycc.ui.message.givemessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.GiveMessageEntity;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class GiveMessageItemViewModel extends ItemViewModel<GiveMessageViewModel> {

    public ObservableField<GiveMessageEntity> itemEntity = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(GiveMessageItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(GiveMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public GiveMessageItemViewModel(@NonNull GiveMessageViewModel viewModel, GiveMessageEntity messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}
