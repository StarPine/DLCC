package com.fine.friendlycc.ui.vest.second;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.AdUserBannerEntity;
import com.fine.friendlycc.entity.AdUserItemEntity;
import com.fine.friendlycc.entity.BroadcastEntity;
import com.fine.friendlycc.entity.BroadcastListEntity;
import com.fine.friendlycc.entity.CallingInviteInfo;
import com.fine.friendlycc.kl.Utils;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.radio.issuanceprogram.IssuanceProgramFragment;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class VestSecondViewModel extends BaseRefreshViewModel<AppRepository> {


    public BindingRecyclerViewAdapter<VestSecondHeadItemViewModel> headAdapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VestSecondHeadItemViewModel> headItemList = new ObservableArrayList<>();
    public ItemBinding<VestSecondHeadItemViewModel> headItemLayout = ItemBinding.of(BR.viewModel, R.layout.item_vest_second_head);

    public BindingRecyclerViewAdapter<VestSecondTrendItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VestSecondTrendItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<VestSecondTrendItemViewModel> itemLayout = ItemBinding.of(BR.viewModel, R.layout.item_vest_second_trend);
    /**
     * 发布按钮的点击事件
     */
    public BindingCommand publishOnClickCommand = new BindingCommand(() -> {
        start(IssuanceProgramFragment.class.getCanonicalName());
    });


    public VestSecondViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
        loadDatas(1);
    }

    @Override
    public void loadDatas(int page) {
        if(page == 1) {
            headItemList.clear();
            itemList.clear();
            getAdUserBanner();
        }
        getBroadcast(page);
    }

    //获取用户广告列表
    public void getAdUserBanner(){
        model.getUserAdList(1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<AdUserBannerEntity>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<AdUserBannerEntity> listBaseDataResponse) {
                        AdUserBannerEntity adUserBanner = listBaseDataResponse.getData();
                        if(adUserBanner != null){
                            List<AdUserItemEntity> listData = adUserBanner.getDataList();
                            if(ObjectUtils.isNotEmpty(listData)){
                                for (AdUserItemEntity adUserItemEntity : listData) {
                                    VestSecondHeadItemViewModel itemViewModel = new VestSecondHeadItemViewModel(VestSecondViewModel.this,adUserItemEntity);
                                    headItemList.add(itemViewModel);
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        Log.e("获取用户广告列表接口","异常原因："+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    private void getBroadcast(int page) {
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
        model.getBroadcastHome(null, null, null, null, null, 1, page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<BroadcastListEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BroadcastListEntity> response) {

                        List<BroadcastEntity> listReal = response.getData().getRealData();
                        for (BroadcastEntity broadcastEntity : listReal) {
                            itemList.add(new VestSecondTrendItemViewModel(VestSecondViewModel.this, broadcastEntity));
                        }

                        //机器人集合
                        List<BroadcastEntity> listUntrue = response.getData().getUntrueData();

                        for (BroadcastEntity broadcastEntity : listUntrue) {

                            itemList.add(new VestSecondTrendItemViewModel(VestSecondViewModel.this, broadcastEntity));
                        }

                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    //拨打语音、视频
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId) {
        if(callingType==1){
            //男女点击拨打语音
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        }else{
            //男女点击拨打视频
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_video_male : AppsFlyerEvent.call_video_female);
        }
        model.callingInviteInfo(callingType, IMUserId, toIMUserId, 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//對方忙線中
                            ToastUtils.showShort(R.string.custom_message_other_busy);
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
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

}