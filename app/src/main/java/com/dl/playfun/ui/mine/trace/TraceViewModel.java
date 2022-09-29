package com.dl.playfun.ui.mine.trace;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.event.TraceEvent;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.ui.userdetail.detail.UserDetailFragment;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Author: 彭石林
 * Time: 2021/8/2 15:52
 * Description: This is TraceViewModel
 */
public class TraceViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<Boolean> gender = new ObservableField<Boolean>(false);
    public UIChangeObservable uc = new UIChangeObservable();
    private Disposable TraceEventSubscription;

    public TraceViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        TraceEventSubscription = RxBus.getDefault().toObservable(TraceEvent.class)
                .subscribe(event -> {
                    uc.refreshTag.setValue(event);
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(TraceEventSubscription);
    }

    /**
     * @return void
     * @Desc TODO(跳转前往用户详细界面)
     * @author 彭石林
     * @parame [userId]
     * @Date 2021/8/5
     */
    public void toUserDetails(Integer userId) {
        Bundle bundle = UserDetailFragment.getStartBundle(userId);
        start(UserDetailFragment.class.getCanonicalName(), bundle);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<TraceEvent> refreshTag = new SingleLiveEvent<>();

    }
}
