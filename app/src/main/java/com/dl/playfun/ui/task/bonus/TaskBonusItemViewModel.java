package com.dl.playfun.ui.task.bonus;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.R;
import com.dl.playfun.entity.BonusGoodsEntity;
import com.dl.playfun.ui.task.TaskCenterViewModel;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: 彭石林
 * Time: 2021/8/11 15:04
 * Description: 积分商品兑换列表明细
 */
public class TaskBonusItemViewModel extends MultiItemViewModel<TaskCenterViewModel> {

    public ObservableField<BonusGoodsEntity> itemEntity = new ObservableField<>();
    public BindingCommand subGoodsCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.uc.AlertBonuClick.setValue(itemEntity.get());
        }
    });

    public TaskBonusItemViewModel(@NonNull @NotNull TaskCenterViewModel viewModel, BonusGoodsEntity entity) {
        super(viewModel);
        itemEntity.set(entity);
    }

    public String getMoneyText() {
        return String.format(StringUtils.getString(R.string.task_fragment_bonus_detail_fen), itemEntity.get().getMoney());
    }
}
