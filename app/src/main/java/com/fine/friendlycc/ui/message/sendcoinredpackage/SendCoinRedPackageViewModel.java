package com.fine.friendlycc.ui.message.sendcoinredpackage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * @author wulei
 */
public class SendCoinRedPackageViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<Integer> userId = new ObservableField<>();


    public ObservableField<String> number = new ObservableField<>("");
    public ObservableField<String> desc = new ObservableField<>();

    UIChangeObservable uc = new UIChangeObservable();
    public BindingCommand sendOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clickSend.call();
        }
    });

    public SendCoinRedPackageViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> clickSend = new SingleLiveEvent<>();
    }

}