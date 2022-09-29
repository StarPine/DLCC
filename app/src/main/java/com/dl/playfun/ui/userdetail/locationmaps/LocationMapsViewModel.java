package com.dl.playfun.ui.userdetail.locationmaps;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class LocationMapsViewModel extends BaseViewModel<AppRepository> {

    //完成按钮的点击事件
    public BindingCommand commitOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
        }
    });

    public LocationMapsViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

}