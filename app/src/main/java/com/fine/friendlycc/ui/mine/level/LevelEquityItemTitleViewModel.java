package com.fine.friendlycc.ui.mine.level;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.entity.LevelSelectInfoEntity;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2022/6/20 18:38
 * Description: This is LevelEquityItemTitleViewModel
 */
public class LevelEquityItemTitleViewModel extends MultiItemViewModel<LevelEquityViewModel> {

    public ObservableBoolean checkCurrent = new ObservableBoolean(false);
    public ObservableField<LevelSelectInfoEntity.LevelInfo> levelInfoData = new ObservableField<>();

    public BindingCommand itemClick = new BindingCommand(() -> {
        int idx = viewModel.observableListTitle.indexOf(LevelEquityItemTitleViewModel.this);
        viewModel.titleRcvItemClick(idx, true);
    });

    public LevelEquityItemTitleViewModel(@NonNull LevelEquityViewModel viewModel, boolean check, LevelSelectInfoEntity.LevelInfo levelInfo) {
        super(viewModel);
        checkCurrent.set(check);
        levelInfoData.set(levelInfo);
    }

}
