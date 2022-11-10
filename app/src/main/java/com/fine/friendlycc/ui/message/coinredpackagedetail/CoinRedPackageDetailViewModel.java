package com.fine.friendlycc.ui.message.coinredpackagedetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.ChatRedPackageEntity;
import com.fine.friendlycc.utils.ChatUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * @author wulei
 */
public class CoinRedPackageDetailViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<Integer> redPackageId = new ObservableField<>();
    public ObservableField<String> msgId = new ObservableField<>();
    public ObservableField<String> statusText = new ObservableField<>();
    public ObservableField<String> tipText = new ObservableField<>();
    public ObservableField<Boolean> isSender = new ObservableField<>();

    public ObservableField<ChatRedPackageEntity> redpackageDetail = new ObservableField<>();
    public BindingCommand receiveOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            receiveCoinRedPackageDetail();
        }
    });
    public BindingCommand linkServiceOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ChatUtils.chatUser(AppConfig.CHAT_SERVICE_USER_ID_SEND,0, StringUtils.getString(R.string.playcc_chat_service_name), CoinRedPackageDetailViewModel.this);
        }
    });

    public CoinRedPackageDetailViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onLazyInitView() {
        super.onLazyInitView();
        getCoinRedPackageDetail();
    }

    private void receiveCoinRedPackageDetail() {
        model.receiveCoinRedPackage(redPackageId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        model.saveChatCustomMessageStatus(msgId.get(), 2);
                        //C2CChatPresenter..updateMessageInfoStatusByMessageId(msgId.get());
                        redpackageDetail.get().setStatus(1);
                        statusText.set(StringUtils.getString(R.string.playcc_received));
                        refreshTipText();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    private void getCoinRedPackageDetail() {
        model.getCoinRedPackage(redPackageId.get())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseEmptyObserver<BaseDataResponse<ChatRedPackageEntity>>(this) {
                    @Override
                    public void onSuccess(BaseDataResponse<ChatRedPackageEntity> response) {
                        super.onSuccess(response);
                        redpackageDetail.set(response.getData());
                        if (response.getData().getStatus() == 0) {
                            if (isSender.get()) {
                                statusText.set(StringUtils.getString(R.string.playcc_to_be_received));
                            } else {
                                statusText.set(StringUtils.getString(R.string.playcc_chat_get_red_package_wait_rec));
                            }
                        } else if (response.getData().getStatus() == 1) {
                            model.saveChatCustomMessageStatus(msgId.get(), 2);
                            if (isSender.get()) {
                                statusText.set(StringUtils.getString(R.string.playcc_her_received));
                            } else {
                                statusText.set(StringUtils.getString(R.string.playcc_received));
                            }
                        } else if (response.getData().getStatus() == 2) {
                            model.saveChatCustomMessageStatus(msgId.get(), 3);
                            statusText.set(StringUtils.getString(R.string.playcc_returned));
                        }
                        refreshTipText();
                        //C2CChatManagerKit.getInstance().updateMessageInfoStatusByMessageId(msgId.get());
                    }

                });
    }

    private void refreshTipText() {
        if (redpackageDetail.get().getStatus() == 1) {
            if (isSender.get()) {
                tipText.set(StringUtils.getString(R.string.playcc_you_cheated_money_recover_loss_for_you));
            } else {
                tipText.set(StringUtils.getString(R.string.playcc_redpackage_detail_tip_receiver));
            }
        } else {
            if (isSender.get()) {
                tipText.set(StringUtils.getString(R.string.playcc_redpackage_detail_tip_sender));
            } else {
                tipText.set(StringUtils.getString(R.string.playcc_redpackage_detail_tip_receiver));
            }
        }
    }
}