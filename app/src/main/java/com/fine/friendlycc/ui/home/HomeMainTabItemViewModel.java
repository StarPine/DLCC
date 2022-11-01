package com.fine.friendlycc.ui.home;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import java.util.Map;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2022/7/22 18:09
 * Description: This is HomeMainItemViewModel
 */
public class HomeMainTabItemViewModel extends MultiItemViewModel<HomeMainViewModel> {

    public ObservableField<Map<String,Object>> itemMap = new ObservableField<>();
    public ObservableBoolean checked = new ObservableBoolean(false);

    public HomeMainTabItemViewModel(@NonNull HomeMainViewModel viewModel, Map map, boolean checked) {
        super(viewModel);
        this.itemMap.set(map);
        this.checked.set(checked);
    }

    public BindingCommand itemClick = new BindingCommand(() -> {
        int index = viewModel.observableListTab.indexOf(this);
        viewModel.titleRcvItemClick(index,(int)itemMap.get().get("type"));
    });
}
