package com.dl.playfun.ui.certification.updatefacesuccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.ui.main.MainFragment;
import com.dl.playfun.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author wulei
 */
public class UpdateFaceSuccessViewModel extends BaseViewModel<AppRepository> {

    public BindingCommand finishOnClickCommand = new BindingCommand(() -> {
        popTo(MainFragment.class.getCanonicalName(), false);
    });

    public UpdateFaceSuccessViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

}