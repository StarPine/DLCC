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
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.AdUserBannerBean;
import com.fine.friendlycc.bean.AdUserItemBean;
import com.fine.friendlycc.bean.BroadcastBean;
import com.fine.friendlycc.bean.BroadcastListBean;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.calling.Utils;
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
     * ???????????????????????????
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

    //????????????????????????
    public void getAdUserBanner(){
        model.getUserAdList(1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<AdUserBannerBean>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<AdUserBannerBean> listBaseDataResponse) {
                        AdUserBannerBean adUserBanner = listBaseDataResponse.getData();
                        if(adUserBanner != null){
                            List<AdUserItemBean> listData = adUserBanner.getDataList();
                            if(ObjectUtils.isNotEmpty(listData)){
                                for (AdUserItemBean adUserItemEntity : listData) {
                                    VestSecondHeadItemViewModel itemViewModel = new VestSecondHeadItemViewModel(VestSecondViewModel.this,adUserItemEntity);
                                    headItemList.add(itemViewModel);
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        Log.e("??????????????????????????????","???????????????"+e.getMessage());
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
                .subscribe(new BaseObserver<BaseDataResponse<BroadcastListBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BroadcastListBean> response) {

                        List<BroadcastBean> listReal = response.getData().getRealData();
                        for (BroadcastBean broadcastEntity : listReal) {
                            itemList.add(new VestSecondTrendItemViewModel(VestSecondViewModel.this, broadcastEntity));
                        }

                        //???????????????
                        List<BroadcastBean> listUntrue = response.getData().getUntrueData();

                        for (BroadcastBean broadcastEntity : listUntrue) {

                            itemList.add(new VestSecondTrendItemViewModel(VestSecondViewModel.this, broadcastEntity));
                        }

                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    //?????????????????????
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId) {
        if(callingType==1){
            //????????????????????????
            CCApplication.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        }else{
            //????????????????????????
            CCApplication.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_video_male : AppsFlyerEvent.call_video_female);
        }
        model.callingInviteInfo(callingType, IMUserId, toIMUserId, 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<CallingInviteInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CallingInviteInfo> callingInviteInfoBaseDataResponse) {
                        if (callingInviteInfoBaseDataResponse.getCode() == 2) {//???????????????
                            ToastUtils.showShort(R.string.custom_message_other_busy);
                            return;
                        }
                        if (callingInviteInfoBaseDataResponse.getCode() == 22001) {//?????????
                            Toast.makeText(CCApplication.instance(), R.string.playcc_in_game, Toast.LENGTH_SHORT).show();
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