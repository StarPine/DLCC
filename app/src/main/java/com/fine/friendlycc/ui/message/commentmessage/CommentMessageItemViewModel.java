package com.fine.friendlycc.ui.message.commentmessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.CommentMessageEntity;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class CommentMessageItemViewModel extends ItemViewModel<CommentMessageViewModel> {

    public ObservableField<CommentMessageEntity> itemEntity = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(CommentMessageItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(CommentMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public CommentMessageItemViewModel(@NonNull CommentMessageViewModel viewModel, CommentMessageEntity messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}
