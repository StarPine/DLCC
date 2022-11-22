package com.fine.friendlycc.ui.message.systemmessagegroup;

import android.app.Application;
import android.app.Service;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.MessageGroupBean;
import com.fine.friendlycc.event.PushMessageEvent;
import com.fine.friendlycc.event.SystemMessageCountChangeEvent;
import com.fine.friendlycc.ui.message.applymessage.ApplyMessageFragment;
import com.fine.friendlycc.ui.message.broadcastmessage.BroadcastMessageFragment;
import com.fine.friendlycc.ui.message.commentmessage.CommentMessageFragment;
import com.fine.friendlycc.ui.message.evaluatemessage.EvaluateMessageFragment;
import com.fine.friendlycc.ui.message.givemessage.GiveMessageFragment;
import com.fine.friendlycc.ui.message.profitmessage.ProfitMessageFragment;
import com.fine.friendlycc.ui.message.systemmessage.SystemMessageFragment;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class SystemMessageGroupViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<SystemMessageGroupItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<SystemMessageGroupItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<SystemMessageGroupItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_message_group);
    private Disposable mSubscription;
    private Vibrator mVibrator;  //声明一个振动器对象

    public SystemMessageGroupViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void initData() {
        startRefresh();
    }

    @Override
    public void onLazyInitView() {
        super.onLazyInitView();
    }

    public void onItemClick(int position) {
        try {
            MessageGroupBean messageGroupEntity = observableList.get(position).itemEntity.get();
            String mold = messageGroupEntity.getMold();
            notifySystemMessageCount();
            if ("system".equals(mold)) {
                start(SystemMessageFragment.class.getCanonicalName());
            } else if ("apply".equals(mold)) {
                start(ApplyMessageFragment.class.getCanonicalName());
            } else if ("broadcast".equals(mold)) {
                start(BroadcastMessageFragment.class.getCanonicalName());
            } else if ("comment".equals(mold)) {
                start(CommentMessageFragment.class.getCanonicalName());
            }  else if ("give".equals(mold)) {
                start(GiveMessageFragment.class.getCanonicalName());
            } else if ("evaluate".equals(mold)) {
                start(EvaluateMessageFragment.class.getCanonicalName());
            } else if ("profit".equals(mold)) {
                start(ProfitMessageFragment.class.getCanonicalName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageList()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<List<MessageGroupBean>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<MessageGroupBean>> response) {
                        observableList.clear();
                        for (MessageGroupBean datum : response.getData()) {
                            SystemMessageGroupItemViewModel item = new SystemMessageGroupItemViewModel(SystemMessageGroupViewModel.this, datum);
                            observableList.add(item);
                        }
                        notifySystemMessageCount();
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(PushMessageEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    startRefresh();
                    if (model.readChatMessageIsShake() && model.readChatMessageIsSound()) {
                        Remind(1);
                    } else if (model.readChatMessageIsSound()) {
                        Remind(2);
                    } else if (model.readChatMessageIsShake()) {
                        Remind(0);
                    }
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
    }

    private void notifySystemMessageCount() {
        int count = 0;
        for (SystemMessageGroupItemViewModel systemMessageGroupItemViewModel : observableList) {
            count = count + systemMessageGroupItemViewModel.unreadCount.get();
        }
        RxBus.getDefault().post(new SystemMessageCountChangeEvent(count));
    }

    public void Remind(int action) {
        try {
            /**
             * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
             */
            if (mVibrator == null) {
                mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
            }
            //用于获取手机默认提示音的Uri
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone rt = RingtoneManager.getRingtone(getApplication(), uri);

            switch (action) {
                case 0:
                    //震动
                    mVibrator.vibrate(new long[]{500, 300, 500, 300}, -1);
                    //停止500毫秒，开启震动300毫秒，然后又停止500毫秒，又开启震动300毫秒，不重复.
                    break;
                case 1:
                    //震动,声音
                    mVibrator.vibrate(new long[]{500, 300, 500, 300}, -1);
                    //停止500毫秒，开启震动300毫秒，然后又停止500毫秒，又开启震动300毫秒，不重复.
                    rt.play();
                    break;
                case 2:
//                    声音
                    rt.play();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.i("debug", e.toString());
        }

    }
}