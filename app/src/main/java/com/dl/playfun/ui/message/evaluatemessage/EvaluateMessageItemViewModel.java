package com.dl.playfun.ui.message.evaluatemessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.EvaluateMessageEntity;
import com.dl.playfun.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class EvaluateMessageItemViewModel extends MultiItemViewModel<EvaluateMessageViewModel> {

    public ObservableField<EvaluateMessageEntity> itemEntity = new ObservableField<>();
    public ObservableField<String> statusText = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                int position = viewModel.observableList.indexOf(EvaluateMessageItemViewModel.this);
                viewModel.itemClick(position);
            } catch (Exception e) {
                ExceptionReportUtils.report(e);
            }
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(EvaluateMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand appealOnClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(EvaluateMessageItemViewModel.this);
            viewModel.clickEvaluateAppeal(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public EvaluateMessageItemViewModel(@NonNull EvaluateMessageViewModel viewModel, EvaluateMessageEntity messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
        this.statusText.set(viewModel.getStatusText(messageEntity.getStatus()));
    }

}
