package com.dl.playfun.ui.certification.verifysuccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.entity.TaskRewardReceiveEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.main.MainFragment;
import com.dl.playfun.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

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