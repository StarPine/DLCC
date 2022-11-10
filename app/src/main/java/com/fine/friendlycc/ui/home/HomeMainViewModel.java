package com.fine.friendlycc.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AdBannerEntity;
import com.fine.friendlycc.entity.AdItemEntity;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.ParkItemEntity;
import com.fine.friendlycc.entity.SystemConfigEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.event.AddBlackListEvent;
import com.fine.friendlycc.event.CityChangeEvent;
import com.fine.friendlycc.event.DailyAccostEvent;
import com.fine.friendlycc.event.GenderToggleEvent;
import com.fine.friendlycc.event.LoadEvent;
import com.fine.friendlycc.event.LocationChangeEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.home.search.SearchFragment;
import com.fine.friendlycc.ui.viewmodel.BaseParkItemViewModel;
import com.fine.friendlycc.ui.viewmodel.BaseParkViewModel;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class HomeMainViewModel extends BaseViewModel<AppRepository> {
    //控制banner是否显示
    public ObservableBoolean rcvBannerDisplay = new ObservableBoolean(true);
    public ObservableList<String> tabList = new ObservableArrayList<>();


    public BindingRecyclerViewAdapter<HomeMainBannerItemViewModel> adapterBanner = new BindingRecyclerViewAdapter<>();
    public ObservableList<HomeMainBannerItemViewModel> observableBanner = new ObservableArrayList<>();
    public ItemBinding<HomeMainBannerItemViewModel> itemBannerBinding = ItemBinding.of(BR.viewModel, R.layout.item_main_banner);

    //位置选择文字
    public ObservableField<String> regionTitle = new ObservableField<>(StringUtils.getString(R.string.playcc_tab_female_1));

    //推荐用户弹窗
    public ObservableField<Integer> cityId = new ObservableField<>();
    public ObservableField<Boolean> gender = new ObservableField<>();//false:女 ，true: 男
    public ObservableField<Boolean> online = new ObservableField<>(true);
    public ObservableField<Boolean> locationService = new ObservableField<>(true);
    public ObservableField<Double> lat = new ObservableField<>();//纬度
    public ObservableField<Double> lng = new ObservableField<>();//经度
    public List<ConfigItemEntity> list_chooseCityItem = new ArrayList<>();
    //男女-选项卡内容
    public ObservableField<Integer> type = new ObservableField<>(1);


    public UIChangeObservable uc = new UIChangeObservable();
    public int currIndex;

    //订阅者
    private Disposable dailyAccostSubscription;
    private Disposable mAddBlackListSubscription;

    public int lastTabClickIdx = -1;
    public String accostKey = "";

    //搜索按钮的点击事件
    public BindingCommand searchOnClickCommand = new BindingCommand(() -> {
        AppContext.instance().logEvent(AppsFlyerEvent.Nearby_Search);
        start(SearchFragment.class.getCanonicalName());
    });


    /**
     * 点击性别
     */
    public BindingCommand genderOnClickCommand = new BindingCommand(() -> {
        AppContext.instance().logEvent(AppsFlyerEvent.Nearby_Change_gender);
        gender.set(!gender.get());
        RxBus.getDefault().post(new GenderToggleEvent(gender.get(), currIndex));
        setTabList();
    });

    private void setTabList() {
        if (tabList.size() > 0)
            tabList.clear();
        if (gender.get()){
            tabList.add(StringUtils.getString(R.string.playcc_tab_male_3_audit));
            tabList.add(StringUtils.getString(R.string.playcc_tab_male_2));
        }else {
            tabList.add(StringUtils.getString(R.string.playcc_tab_male_3));
            tabList.add(StringUtils.getString(R.string.playcc_tab_female_2));
        }
    }

    public BindingCommand toTaskClickCommand = new BindingCommand(() -> {
        uc.clickAccountDialog.setValue("0");
        AppContext.instance().logEvent(AppsFlyerEvent.homepage_batch_accost);
    });

    public BindingCommand regionOnClickCommand = new BindingCommand(() -> uc.clickRegion.call());

    //消费者
    private Disposable loadReceive;

    public HomeMainViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        try {
            gender.set(repository.readUserData().getSex() != 1);
        } catch (Exception e) {
            gender.set(true);
        }
        setTabList();
        list_chooseCityItem.addAll(model.readCityConfig());
        //一键搭讪
        accostKey =  StringUtil.getDailyFlag("dailyAccost");

    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        loadReceive = RxBus.getDefault().toObservable(LoadEvent.class)
                .subscribe(countDownTimerEvent -> {
                    uc.isLoad.postValue(countDownTimerEvent.isLoad());
                });
        dailyAccostSubscription = RxBus.getDefault().toObservable(DailyAccostEvent.class)
                .subscribe(cityChangeEvent -> {
                    boolean isMale = ConfigManager.getInstance().isMale();
                    SystemConfigEntity systemConfig = model.readSystemConfig();
                    if (AppConfig.isRegisterAccost) {
                        AppConfig.isRegisterAccost = false;
                        if (isMale){
                            if (systemConfig.getRegisterMaleAccost() == 1){
                                showDailyAccost();
                            }
                        }else {
                            if (systemConfig.getRegisterFemaleAccost() == 1){
                                showDailyAccost();
                            }
                        }
                    }else {
                        if (isMale){
                            if (systemConfig.getMaleAccost() == 1){
                                showDailyAccost();
                            }
                        }else {
                            if (systemConfig.getFemaleAccost() == 1){
                                showDailyAccost();
                            }
                        }
                    }

                });

        //将订阅者加入管理站
        RxSubscriptions.add(mAddBlackListSubscription);
        RxSubscriptions.add(dailyAccostSubscription);

    }

    private void showDailyAccost() {
        String value = model.readKeyValue(accostKey);
        if (value == null) {
            model.putKeyValue(accostKey, "true");
            uc.clickAccountDialog.setValue("0");
        }
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(loadReceive);
        RxSubscriptions.remove(mAddBlackListSubscription);
        RxSubscriptions.remove(dailyAccostSubscription);
    }

    //批量搭讪
    public void putAccostList(List<Integer> userIds) {
        model.putAccostList(userIds)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * @Desc TODO(获取首页广告位)
     * @author 彭石林
     * @parame []
     * @return void
     * @Date 2022/7/25
     */
    public void getAdListBannber(){
        model.getMainAdBannerList(1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<AdBannerEntity>>(){

                    @Override
                    public void onSuccess(BaseDataResponse<AdBannerEntity> listBaseDataResponse) {
                        if( listBaseDataResponse.getData()!=null){
                            List<AdItemEntity> listData = listBaseDataResponse.getData().getDataList();
                            if(!ObjectUtils.isEmpty(listData)){
                                for (AdItemEntity adItemEntity : listData){
                                    HomeMainBannerItemViewModel homeItemBanner = new HomeMainBannerItemViewModel(HomeMainViewModel.this,adItemEntity);
                                    observableBanner.add(homeItemBanner);
                                }
                            }
                            rcvBannerDisplay.set(ObjectUtils.isNotEmpty(listData));
                        }else{
                            rcvBannerDisplay.set(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        //选择位置
        public SingleLiveEvent<Void> clickRegion = new SingleLiveEvent<>();
        //打开批量搭讪接口
        public SingleLiveEvent<String> clickAccountDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> isLoad = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> starActivity = new SingleLiveEvent<>();
        //搭讪失败。充值钻石
        public SingleLiveEvent<Void> sendAccostFirstError = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> loadLoteAnime = new SingleLiveEvent<>();
        //推币机弹窗
        public SingleLiveEvent<Void> coinPusherRoomEvent = new SingleLiveEvent<>();
    }

}