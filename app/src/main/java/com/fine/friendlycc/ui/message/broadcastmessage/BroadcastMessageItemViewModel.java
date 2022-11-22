package com.fine.friendlycc.ui.message.broadcastmessage;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.BoradCastMessageBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class BroadcastMessageItemViewModel extends ItemViewModel<BroadcastMessageViewModel> {

    public ObservableField<BoradCastMessageBean> itemEntity = new ObservableField<>();
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(BroadcastMessageItemViewModel.this);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });
    public BindingCommand itemLongClick = new BindingCommand(() -> {
        try {
            int position = viewModel.observableList.indexOf(BroadcastMessageItemViewModel.this);
            viewModel.uc.clickDelete.postValue(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public BroadcastMessageItemViewModel(@NonNull BroadcastMessageViewModel viewModel, BoradCastMessageBean messageEntity) {
        super(viewModel);
        this.itemEntity.set(messageEntity);
    }

}