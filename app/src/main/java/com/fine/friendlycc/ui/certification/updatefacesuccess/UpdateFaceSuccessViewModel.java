package com.fine.friendlycc.ui.certification.updatefacesuccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

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