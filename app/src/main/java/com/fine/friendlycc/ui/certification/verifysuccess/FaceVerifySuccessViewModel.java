package com.fine.friendlycc.ui.certification.verifysuccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * @author wulei
 */
public class FaceVerifySuccessViewModel extends BaseViewModel<AppRepository> {

    public SingleLiveEvent<Boolean> isMessageMan = new SingleLiveEvent<Boolean>();
    public BindingCommand finishOnClickCommand = new BindingCommand(() -> {
        popTo(MainFragment.class.getCanonicalName());
    });

    public FaceVerifySuccessViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        isMessageMan.postValue(ConfigManager.getInstance().isMale());

    }
}