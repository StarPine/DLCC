package com.fine.friendlycc.ui.message;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.MessageGroupEntity;
import com.fine.friendlycc.event.MainTabEvent;
import com.fine.friendlycc.event.MessageCountChangeContactEvent;
import com.fine.friendlycc.event.MessageCountChangeEvent;
import com.fine.friendlycc.event.MessageCountChangeTagEvent;
import com.fine.friendlycc.event.SystemMessageCountChangeEvent;
import com.fine.friendlycc.ui.message.pushsetting.PushSettingFragment;
import com.fine.friendlycc.ui.message.systemmessagegroup.SystemMessageGroupFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class MessageMainViewModel extends BaseViewModel<AppRepository> {
    public ObservableBoolean tabSelected = new ObservableBoolean(true);
    //顶部切换tab按键选中
    public SingleLiveEvent<Boolean> tabSelectEvent = new SingleLiveEvent<>();

    public ObservableField<Integer> chatMessageCount = new ObservableField<>(0);
    public ObservableField<Integer> chatMessageContactCount = new ObservableField<>(0);
    public ObservableField<Integer> systemMessageCount = new ObservableField<>(0);
    //推送设置按钮的点击事件
    public BindingCommand pushSettingOnClickCommand = new BindingCommand(() -> start(PushSettingFragment.class.getCanonicalName()));

    private Disposable mSubscription, MessageCountTagSubscription,mainTabEventReceive,MessageCountContactSubscription;


    public BindingCommand<Integer> onPageSelectedCommand = new BindingCommand<>(index -> {

        boolean flag = tabSelected.get();
        if (index == 0){
            if (!flag) {
                tabSelected.set(true);
                tabSelectEvent.postValue(true);
            }
        }else {
            if (flag) {
                tabSelected.set(false);
                tabSelectEvent.postValue(false);
            }
        }
    });
    //tab切换按键
    public BindingCommand toLeftTabClickCommand = new BindingCommand(() -> {
        boolean flag = tabSelected.get();
        if (!flag) {
            tabSelected.set(true);
            tabSelectEvent.postValue(true);
        }
    });
    //tab切换按键
    public BindingCommand toRightTabClickCommand = new BindingCommand(() -> {
        boolean flag = tabSelected.get();
        if (flag) {
            tabSelected.set(false);
            tabSelectEvent.postValue(false);
        }
    });
    //tab切换按键
    public BindingCommand toMessageTabClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            start(SystemMessageGroupFragment.class.getCanonicalName());
        }
    });

    public MessageMainViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(SystemMessageCountChangeEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(systemMessageCountChangeEvent -> {
                    SystemMessageCountChangeEvent systemMessageCountChangeEvent1 = (SystemMessageCountChangeEvent) systemMessageCountChangeEvent;
                    if(systemMessageCountChangeEvent1!=null){
                        systemMessageCount.set(systemMessageCountChangeEvent1.getCount());
                        notifyMessageCountChange();
                    }
                });
        MessageCountTagSubscription = RxBus.getDefault().toObservable(MessageCountChangeTagEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(messageCountChangeTagEvent -> {
                    MessageCountChangeTagEvent messageCountChangeTagEvent1 = (MessageCountChangeTagEvent)messageCountChangeTagEvent;
                    if (messageCountChangeTagEvent1!=null && messageCountChangeTagEvent1.getTextCount() != null) {
                        chatMessageCount.set(messageCountChangeTagEvent1.getTextCount());
                        notifyMessageCountChange();
                    }
                });
        mainTabEventReceive = RxBus.getDefault().toObservable(MainTabEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(event -> {
                    MainTabEvent mainTabEvent = (MainTabEvent) event;
                    if(mainTabEvent!=null){
                        if (mainTabEvent.getTabName().equals("message")){
                            boolean flag = tabSelected.get();
                            if (flag) {
                                tabSelected.set(false);
                                tabSelectEvent.postValue(false);
                            }else{
                                tabSelected.set(true);
                                tabSelectEvent.postValue(true);
                            }
                        }
                    }
        });
        MessageCountContactSubscription = RxBus.getDefault().toObservable(MessageCountChangeContactEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(event -> {
                    MessageCountChangeContactEvent messageCountChangeContactEvent = (MessageCountChangeContactEvent) event;
                    if(messageCountChangeContactEvent!=null){
                        chatMessageContactCount.set(messageCountChangeContactEvent.getTextContactCount());
                        notifyMessageCountChange();
                    }
                });
        RxSubscriptions.add(mSubscription);
        RxSubscriptions.add(MessageCountTagSubscription);
        RxSubscriptions.add(mainTabEventReceive);
        RxSubscriptions.add(MessageCountContactSubscription);

    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
        RxSubscriptions.remove(MessageCountTagSubscription);
        RxSubscriptions.remove(mainTabEventReceive);
        RxSubscriptions.remove(MessageCountContactSubscription);
    }

    public void loadDatas() {
        model.getMessageList()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<List<MessageGroupEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<MessageGroupEntity>> response) {
                        for (MessageGroupEntity datum : response.getData()) {
                            systemMessageCount.set(systemMessageCount.get() + datum.getUnreadNumber());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void notifyMessageCountChange() {
        Integer chatMessageNum = chatMessageCount.get();
        if(chatMessageNum==null){
            chatMessageNum = 0;
        }
//        Integer systemMessageNum = systemMessageCount.get();
//        if(systemMessageNum==null){
//            systemMessageNum = 0;
//        }
        Integer chatMessageContactNum = chatMessageContactCount.get();
        if(chatMessageContactNum==null){
            chatMessageContactNum = 0;
        }
        int sumCount = chatMessageNum + chatMessageContactNum;
        RxBus.getDefault().post(new MessageCountChangeEvent(sumCount));
    }

    public String addString(Integer integer) {
        String s = String.valueOf(integer);
        if (integer > 99) {
            s = "99+";
        }
        return s;
    }
}