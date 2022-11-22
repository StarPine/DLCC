package com.fine.friendlycc.ui.message.evaluatemessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.EvaluateMessageBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class EvaluateThemMessageItemViewModel extends MultiItemViewModel<EvaluateMessageViewModel> {

    public ObservableField<EvaluateMessageBean> itemEntity = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                int position = viewModel.observableList.indexOf(EvaluateThemMessageItemViewModel.this);
                viewModel.itemClick(position);
            } catch (Exception e) {
                ExceptionReportUtils.report(e);
            }

        }
    });
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                int position = viewModel.observableList.indexOf(EvaluateThemMessageItemViewModel.this);
                viewModel.uc.clickDelete.postValue(position);
            } catch (Exception e) {
                ExceptionReportUtils.report(e);
            }
        }
    });

    public EvaluateThemMessageItemViewModel(@NonNull EvaluateMessageViewModel viewModel, EvaluateMessageBean messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}