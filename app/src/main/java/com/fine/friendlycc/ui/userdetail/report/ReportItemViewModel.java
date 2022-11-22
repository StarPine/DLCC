package com.fine.friendlycc.ui.userdetail.report;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


/**
 * @author wulei
 */
public class ReportItemViewModel extends MultiItemViewModel<ReportUserViewModel> {

    public ObservableField<ConfigItemBean> configItemEntityObservableField = new ObservableField<>();
    // TODO: Implement the ViewModel
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {
            int position = viewModel.reportItemViewModels.indexOf(ReportItemViewModel.this);
            viewModel.itemClick(position);
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public ReportItemViewModel(@NonNull ReportUserViewModel viewModel, ConfigItemBean commentBean) {
        super(viewModel);
        this.configItemEntityObservableField.set(commentBean);
    }

}