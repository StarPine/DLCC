package com.dl.playfun.ui.task;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.entity.TaskAdEntity;
import com.dl.playfun.ui.task.fukubukuro.FukubukuroFragment;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Author: 彭石林
 * Time: 2021/9/8 15:57
 * Description: This is TaskCenterADItemViewModel
 */
public class TaskCenterADItemViewModel extends MultiItemViewModel<TaskCenterViewModel> {
    public ObservableField<TaskAdEntity> itemEntity = new ObservableField<>();

    public TaskCenterADItemViewModel(@NonNull @NotNull TaskCenterViewModel viewModel, TaskAdEntity taskAdEntity) {
        super(viewModel);
        itemEntity.set(taskAdEntity);
    }

    public BindingCommand toWebBindingCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putString("link", itemEntity.get().getLink());
            viewModel.start(FukubukuroFragment.class.getCanonicalName(), bundle);
        }
    });
}
