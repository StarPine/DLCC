package com.fine.friendlycc.ui.message.applymessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.ApplyMessageBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class ApplyMessageItemViewModel extends MultiItemViewModel<ApplyMessageViewModel> {

    public ObservableField<ApplyMessageBean> itemEntity = new ObservableField<>();
    public ObservableField<Integer> photoIsView = new ObservableField<>(0);
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ApplyMessageItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ApplyMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand photoClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ApplyMessageItemViewModel.this);
            viewModel.itemPhotoClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand allowClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ApplyMessageItemViewModel.this);
            viewModel.replyApply(position, true);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand rejectClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(ApplyMessageItemViewModel.this);
            viewModel.replyApply(position, false);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public ApplyMessageItemViewModel(@NonNull ApplyMessageViewModel viewModel, ApplyMessageBean applyMessageEntity) {
        super(viewModel);
        this.itemEntity.set(applyMessageEntity);
        if (applyMessageEntity.getApply() != null) {
            photoIsView.set(applyMessageEntity.getApply().getIsView());
        }
    }
}