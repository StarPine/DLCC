package com.dl.playfun.ui.mine.broadcast;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.event.BadioEvent;
import com.dl.playfun.viewmodel.BaseViewModel;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

public class BroadcastViewModel extends BaseViewModel<AppRepository> {
    UIChangeObservable uc = new UIChangeObservable();
    private Disposable badioEvent;
    public BroadcastViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        badioEvent = RxBus.getDefault().toObservable(BadioEvent.class)
                .subscribe(event -> {
                    uc.switchPosion.setValue(event.getType());
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(badioEvent);
    }

    public class UIChangeObservable {
        public SingleLiveEvent switchPosion = new SingleLiveEvent<>();
    }
}
