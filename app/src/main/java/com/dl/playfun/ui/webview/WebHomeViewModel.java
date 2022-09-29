package com.dl.playfun.ui.webview;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.entity.CallingInviteInfo;
import com.dl.playfun.kl.Utils;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.radio.radiohome.RadioViewModel;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.google.gson.Gson;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/7/29 17:29
 * Description: This is WebHomeViewModel
 */
public class WebHomeViewModel extends BaseViewModel<AppRepository> {

    public UIChangeObservable webUC = new UIChangeObservable();

    public WebHomeViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public String getToken() {
        return model.readLoginInfo().getToken();
    }

    //获取当前用户本地缓存信息
    public String getUserData() {
        return GsonUtils.toJson(model.readUserData());
    }

    //拨打语音、视频
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId, int callingSource) {
        if(callingType==1){
            //男女点击拨打语音
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        }else{
            //男女点击拨打视频
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_video_male : AppsFlyerEvent.call_video_female);
        }
        model.callingInviteInfo(callingType, IMUserId, toIMUserId, callingSource)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//對方忙線中
                            webUC.otherBusy.call();
                            return;
                        }
                        if (callingInviteInfoBaseDataResponse.getCode() == 22001) {//游戏中
                            Toast.makeText(AppContext.instance(), R.string.playfun_in_game, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CallingInviteInfo callingInviteInfo = callingInviteInfoBaseDataResponse.getData();
                        if (callingInviteInfo != null) {
                            Utils.tryStartCallSomeone(callingType, toIMUserId, callingInviteInfo.getRoomId(), new Gson().toJson(callingInviteInfo));
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if (e != null) {
                            if (e.getCode() == 21001) {
                                webUC.sendDialogViewEvent.call();
                            }
                        }
                    }


                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        //对方忙线
        public SingleLiveEvent<Void> otherBusy = new SingleLiveEvent<>();
        //钻石不足。唤起充值
        public SingleLiveEvent<Void> sendDialogViewEvent = new SingleLiveEvent<>();
    }
}
