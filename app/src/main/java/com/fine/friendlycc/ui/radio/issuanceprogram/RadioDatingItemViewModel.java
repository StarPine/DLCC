package com.fine.friendlycc.ui.radio.issuanceprogram;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.DatingObjItemEntity;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @ClassName RadioDatingItemViewModel
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/6/26 14:29
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class RadioDatingItemViewModel extends ItemViewModel<IssuanceProgramViewModel> {
    public ObservableField<DatingObjItemEntity> itemEntity = new ObservableField<>();
    //条目的点击事件
    public BindingCommand itemDatingClick = new BindingCommand(() -> {
        try {
            int type = itemEntity.get().getType();
            if (type == 0) {
                for (int i = 0; i < viewModel.objItems.size(); i++) {
                    RadioDatingItemViewModel radioDatingObjItemEntity = viewModel.objItems.get(i);
                    int oldId = radioDatingObjItemEntity.itemEntity.get().getId().intValue();
                    int newId = itemEntity.get().getId().intValue();
                    if (oldId == newId) {
                        viewModel.objItems.get(i).itemEntity.get().setSelect(true);
                        viewModel.OnClickItem(itemEntity.get());
                    } else {
                        viewModel.objItems.get(i).itemEntity.get().setSelect(false);
                    }
                    viewModel.objAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            ExceptionReportUtils.report(e);
        }
    });

    public RadioDatingItemViewModel(@NonNull IssuanceProgramViewModel viewModel, DatingObjItemEntity itemEntity) {
        super(viewModel);
        this.itemEntity.set(itemEntity);
    }
}