package com.fine.friendlycc.ui.message.chatdetail;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.ThemeItemEntity;
import com.fine.friendlycc.ui.radio.radiohome.RadioViewModel;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @ClassName SwiftMessageItemViewModel
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/30 23:14
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class SwiftMessageItemViewModel extends ItemViewModel {

    public ObservableField<ThemeItemEntity> itemEntity = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(() -> {
        try {

        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public SwiftMessageItemViewModel(@NonNull RadioViewModel viewModel, ThemeItemEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }
}
