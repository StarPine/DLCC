package com.fine.friendlycc.ui.radio.radiohome;

import static com.blankj.utilcode.util.SnackbarUtils.dismiss;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.AdBannerBean;
import com.fine.friendlycc.bean.AdItemBean;
import com.fine.friendlycc.bean.AdUserBannerBean;
import com.fine.friendlycc.bean.AdUserItemBean;
import com.fine.friendlycc.bean.BroadcastBean;
import com.fine.friendlycc.bean.BroadcastListBean;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.BadioEvent;
import com.fine.friendlycc.event.LikeChangeEvent;
import com.fine.friendlycc.event.RadioadetailEvent;
import com.fine.friendlycc.event.TaskListEvent;
import com.fine.friendlycc.event.TaskMainTabEvent;
import com.fine.friendlycc.event.TaskTypeStatusEvent;
import com.fine.friendlycc.event.ZoomInPictureEvent;
import com.fine.friendlycc.calling.Utils;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.TrendItemViewModel;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DiamondRechargeActivity;
import com.fine.friendlycc.ui.radio.issuanceprogram.IssuanceProgramFragment;
import com.fine.friendlycc.ui.radio.radiohome.item.RadioItemBannerVideoViewModel;
import com.fine.friendlycc.ui.task.webview.FukuokaViewFragment;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * @author wulei
 */
public class RadioViewModel extends BaseRefreshViewModel<AppRepository> {
    public static final String RadioRecycleType_New = "new";
    public static final String RadioRecycleType_trace = "emptyTrace";
    public ObservableField<UserDataBean> userDataEntity = new ObservableField<>(new UserDataBean());
    public ObservableField<String> area = new ObservableField<>();
    public int userId;
    public String avatar;
    public UIChangeObservable radioUC = new UIChangeObservable();
    public Integer type = 1;
    public Integer cityId = null;
    public Integer gameId = null;
    public Integer sexId = null;
    public Integer IsCollect = 1;
    public boolean CollectFlag = false;
    public Integer certification = null;
    public boolean collectReLoad = false;
    public boolean isLoading = false;
    //??????????????????????????????item??????
    public Integer lastClickAudioPlayer = -1;

    //????????????????????????
    public ObservableField<List<AdItemBean>> itemBannerEntity = new ObservableField<>();
    public ObservableBoolean itemBannerShow = new ObservableBoolean(false);

    //??????????????????
    public ObservableField<String> regionTitle = new ObservableField<>(StringUtils.getString(R.string.playcc_tab_female_1));
    public ObservableField<String> tarckingTitle = new ObservableField<>(StringUtils.getString(R.string.playcc_radio_selected_zuiz));

    public BindingRecyclerViewAdapter<RadioItemBannerVideoViewModel> adapterAdUser = new BindingRecyclerViewAdapter<>();
    public ObservableList<RadioItemBannerVideoViewModel> radioItemsAdUser = new ObservableArrayList<>();
    public ItemBinding<RadioItemBannerVideoViewModel> radioItemAdUserBinding = ItemBinding.of(BR.viewModel, R.layout.item_radio_banner_video);

    public BindingRecyclerViewAdapter<MultiItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<MultiItemViewModel> radioItems = new ObservableArrayList<>();
    public ItemBinding<MultiItemViewModel> radioItemBinding = ItemBinding.of(new OnItemBind<MultiItemViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, MultiItemViewModel item) {
            //??????item?????????, ????????????Item???????????????
            String itemType = (String) item.getItemType();
            if (RadioRecycleType_New.equals(itemType)) {
                //??????new
                itemBinding.set(BR.viewModel, R.layout.item_trend_new);
            } else if (RadioRecycleType_trace.equals(itemType)) {
                //???????????????????????????
                itemBinding.set(BR.viewModel, R.layout.item_radio_trace_empty);
            }
        }
    });
    /**
     * ???????????????????????????
     */
    public BindingCommand publishOnClickCommand = new BindingCommand(() -> {
        start(IssuanceProgramFragment.class.getCanonicalName());
    });
    //????????????
    public BindingCommand regionOnClickCommand = new BindingCommand(() -> radioUC.clickRegion.call());
    //????????????????????????
    public BindingCommand clickTackingClickCommand = new BindingCommand(() -> radioUC.clickTacking.call());

    //banner??????
    public BindingCommand<Integer> onBannerClickCommand = new BindingCommand<>(index -> {
        try {
            AdItemBean adItemEntity = itemBannerEntity.get().get(index);
            int typeAct = adItemEntity.getType();
            if (typeAct != 0) {
                switch (typeAct) {
                    case 5:
                        startActivity(DiamondRechargeActivity.class);
                        break;
                }
            } else if (adItemEntity != null && adItemEntity.getLink() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("link", adItemEntity.getLink());
                start(FukuokaViewFragment.class.getCanonicalName(), bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    //item????????????
    public void itemClickChangeIdx(int position) {
        radioUC.clickBannerIdx.postValue(position);
    }

    //item??????????????????
    public void itemClickCallVideo(AdUserItemBean adUserItemEntity) {
        //???????????????????????????????????? ???????????????????????????????????????
        UserDataBean userDataEntity = model.readUserData();
        if (userDataEntity != null && adUserItemEntity != null && adUserItemEntity.getToImId() != null) {
            //????????????
            getCallingInvitedInfo(2, model.readUserData().getImUserId(), adUserItemEntity.getToImId());
        }

    }

    //????????????
    public void itemClickPlayAudio(int position) {
        if (lastClickAudioPlayer == -1) {
            lastClickAudioPlayer = position;
        } else {
            if (lastClickAudioPlayer != position) {
                radioItemsAdUser.get(lastClickAudioPlayer).isPlaying.set(false);
                lastClickAudioPlayer = position;
            }
        }
    }

    private Integer default_sex = null;
    private Disposable badioEvent;
    private Disposable radioadetailEvent;
    private Disposable taskTypeStatusEvent;
    private Disposable likeChangeEventDisposable, zoomInPictureEvent;
    private boolean isFirstComment = false;
    private boolean isFirstLike = false;
    private boolean accostThree = false;
    private boolean dayAccost = false;

    public RadioViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
        initUserDate();
    }

    public void initUserDate() {
        userId = model.readUserData().getId();
        avatar = model.readUserData().getAvatar();
        certification = model.readUserData().getCertification();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        //????????????
        likeChangeEventDisposable = RxBus.getDefault().toObservable(LikeChangeEvent.class)
                .subscribe(event -> {
                    if (event != null) {
                        collectReLoad = true;
                    }
                });
        zoomInPictureEvent = RxBus.getDefault().toObservable(ZoomInPictureEvent.class)
                .subscribe(event -> {
                    radioUC.zoomInp.setValue(event.getDrawable());
                });
        badioEvent = RxBus.getDefault().toObservable(BadioEvent.class)
                .subscribe(event -> {
                    currentPage = 1;
                    getBroadcast(1);
                });
        radioadetailEvent = RxBus.getDefault().toObservable(RadioadetailEvent.class)
                .subscribe(event -> {
                    for (int i = 0; i < radioItems.size(); i++) {
                        if (radioItems.get(i) instanceof TrendItemViewModel) {
                            if (((TrendItemViewModel) radioItems.get(i)).newsEntityObservableField.get().getId() == event.getId()) {
                                switch (event.getType()) {//1:?????? 2????????????????????? 3????????? 4????????????????????? 5?????????  6?????????
                                    case 1:
                                        radioItems.remove(i);
                                        break;
                                    case 2:
                                        ((TrendItemViewModel) radioItems.get(i)).newsEntityObservableField.get().getBroadcast().setIsComment(event.isComment);
                                        break;
                                    case 5:
                                        ((TrendItemViewModel) radioItems.get(i)).addComment(event.getId(), event.content, event.toUserId, event.toUserName, model.readUserData().getNickname());
                                        break;
                                    case 6:
                                        ((TrendItemViewModel) radioItems.get(i)).addGiveUser();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                });
        taskTypeStatusEvent = RxBus.getDefault().toObservable(TaskTypeStatusEvent.class)
                .subscribe(taskTypeStatusEvent -> {
                    isFirstComment = taskTypeStatusEvent.getDayCommentNews() == 0;
                    isFirstLike = taskTypeStatusEvent.getDayGiveNews() == 0;
                    accostThree = taskTypeStatusEvent.getAccostThree() == 0;
                    dayAccost = taskTypeStatusEvent.getDayAccost() == 0;
                });
    }

    /**
     * @return void
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/9/30
     */
    public void toTaskCenter() {
        RxBus.getDefault().post(new TaskMainTabEvent(false, true));
        //start(TaskCenterFragment.class.getCanonicalName());
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(likeChangeEventDisposable);
        RxSubscriptions.remove(badioEvent);
        RxSubscriptions.remove(radioadetailEvent);
        RxSubscriptions.remove(taskTypeStatusEvent);
        RxSubscriptions.remove(zoomInPictureEvent);
    }

    public void setType(Integer type) {
        this.type = type;
        CollectFlag = false;
        getBroadcast(1);
    }

    public void setCityId(Integer gameId, Integer cityId) {
        this.cityId = cityId;
        this.gameId = gameId;
        CollectFlag = false;
        getBroadcast(1);
    }

    public void setSexId(Integer sexId) {
        this.sexId = sexId;
        this.IsCollect = 0;
        CollectFlag = false;
        getBroadcast(1);
    }

    public void setIsCollect(Integer collect) {
        this.IsCollect = collect;
        this.sexId = null;
        CollectFlag = false;
        getBroadcast(1);
    }

    @Override
    public void loadDatas(int page) {
        if (page == 1) {
            getAdUserBanner();
            getAdListBanner();
        }
        try {
            if (IsCollect == null) {
                if (default_sex == null) {
                    default_sex = 2;
                    IsCollect = 1;
                    sexId = null;
                }
            }
        } catch (Exception e) {

        }
        //????????????????????????????????????????????????????????????????????????????????????????????????m
        if (collectReLoad && CollectFlag) {
            setIsCollect(1);
        } else {
            if (page == 1 && CollectFlag && IsCollect == 0) {
                sexId = null;
                CollectFlag = false;
                IsCollect = 1;
            }
            getBroadcast(page);
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

    }

    @Override
    public void onResume() {
        super.onResume();
        userDataEntity.set(model.readUserData());
        getArea();
    }

    public void getArea() {//??????
        List<ConfigItemBean> cityConfig = model.readCityConfig();
        if (ObjectUtils.isEmpty(userDataEntity) || ObjectUtils.isEmpty(cityConfig) || ObjectUtils.isEmpty(userDataEntity.get()) || ObjectUtils.isEmpty(userDataEntity.get().getCityId())) {
            return;
        }
        for (int i = 0; i < cityConfig.size(); i++) {
            if (userDataEntity.get().getCityId() == cityConfig.get(i).getId()) {
                area.set(cityConfig.get(i).getName());
            }
        }
    }

    /**
     * ????????????
     */
    private void getBroadcast(int page) {
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
        model.getBroadcastHome(sexId, cityId, gameId, null, IsCollect, type, page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<BroadcastListBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<BroadcastListBean> response) {
                        isLoading = true;
                        if (!CollectFlag) {
                            if (page == 1) {
                                radioItems.clear();
                            }
                        }
                        if (response.getData() != null) {
                            //????????????
                            int realIndex = 0;
                            List<BroadcastBean> listReal = response.getData().getRealData();
                            //???????????????
                            List<BroadcastBean> listUntrue = response.getData().getUntrueData();
                            //?????????????????????
                            Integer collectListEmpty = response.getData().getIsCollect();
                            if (listReal.size() <= 0 && listUntrue.size() <= 0 && IsCollect == 0 && radioItems.size() <= 1){
                                isLoading = false;
                                radioItems.clear();
                            }
                            //??????????????????
                            int position = 0;
                            if (IsCollect == 1 && page == 1 && CollectFlag) {
                                radioItems.clear();
                                CollectFlag = true;
                                IsCollect = 0;
                                if (ConfigManager.getInstance() != null && ConfigManager.getInstance().isMale()) {
                                    sexId = 0;
                                } else {
                                    sexId = 1;
                                }
                                String emptyText = null;
                                if (radioItems == null || radioItems.size() == 0) {
                                    if (collectListEmpty != null && collectListEmpty >= 1) {//???????????????
                                        emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                    } else {//??????????????????
                                        emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty);
                                    }
                                } else {
                                    emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                }
                                // ??????
                                RadioTraceEmptyItemViewModel trendItemViewModel = new RadioTraceEmptyItemViewModel(RadioViewModel.this, emptyText);
                                trendItemViewModel.multiItemType(RadioRecycleType_trace);
                                radioItems.add(trendItemViewModel);
                            }

                            //??????????????????
                            if (!ObjectUtils.isEmpty(listUntrue) && listUntrue.size() > 0) {
                                for (BroadcastBean broadcastEntity : listUntrue) {
                                    position++;
                                    if (broadcastEntity.getNews() != null) {
//                                ??????
                                        TrendItemViewModel trendItemViewModel = new TrendItemViewModel(RadioViewModel.this, broadcastEntity);
                                        trendItemViewModel.multiItemType(RadioRecycleType_New);
                                        radioItems.add(trendItemViewModel);
                                    }
                                    if (position % 2 == 0) {
                                        if (listReal.size() > realIndex + 1) {
                                            BroadcastBean broadcastEntityReal = listReal.get(realIndex);
                                            if (broadcastEntityReal.getNews() != null) {
                                                //??????
                                                TrendItemViewModel trendItemViewModelReal = new TrendItemViewModel(RadioViewModel.this, broadcastEntityReal);
                                                trendItemViewModelReal.multiItemType(RadioRecycleType_New);
                                                radioItems.add(trendItemViewModelReal);
                                            }
                                            realIndex++;
                                        }
                                    }
                                }
                                if (realIndex == 0 && listReal.size() > 1) {
                                    for (BroadcastBean broadcastEntity : listReal) {
                                        if (broadcastEntity.getNews() != null) {
                                            // ??????
                                            TrendItemViewModel trendItemViewModel = new TrendItemViewModel(RadioViewModel.this, broadcastEntity);
                                            trendItemViewModel.multiItemType(RadioRecycleType_New);
                                            radioItems.add(trendItemViewModel);
                                        }
                                    }
                                }
                            } else {
                                //?????????????????????
                                if (!ObjectUtils.isEmpty(listReal) && listReal.size() > 0) {
                                    for (BroadcastBean broadcastEntity : listReal) {
                                        if (broadcastEntity.getNews() != null) {
                                            // ??????
                                            TrendItemViewModel trendItemViewModel = new TrendItemViewModel(RadioViewModel.this, broadcastEntity);
                                            trendItemViewModel.multiItemType(RadioRecycleType_New);
                                            radioItems.add(trendItemViewModel);
                                        }
                                    }
                                } else {
                                    if (IsCollect == 1 && !CollectFlag) {
                                        CollectFlag = true;
                                        IsCollect = 0;
                                        if (ConfigManager.getInstance() != null && ConfigManager.getInstance().isMale()) {
                                            sexId = 0;
                                        } else {
                                            sexId = 1;
                                        }
                                        String emptyText = null;
                                        if (radioItems == null || radioItems.size() == 0) {
                                            if (collectListEmpty != null && collectListEmpty >= 1) {//???????????????
                                                emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                            } else {//??????????????????
                                                emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty);
                                            }
                                        } else {
                                            emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                        }
                                        // ??????
                                        RadioTraceEmptyItemViewModel trendItemViewModel = new RadioTraceEmptyItemViewModel(RadioViewModel.this, emptyText);
                                        trendItemViewModel.multiItemType(RadioRecycleType_trace);
                                        radioItems.add(trendItemViewModel);
                                        getBroadcast(1);
                                    }
                                }
                            }
                            if (IsCollect == 1 && !CollectFlag) {
                                int listRealSize = ObjectUtils.isEmpty(listReal) ? 0 : listReal.size();
                                int listUntrueSize = ObjectUtils.isEmpty(listUntrue) ? 0 : listUntrue.size();

                                if ((listRealSize + listUntrueSize) < 2) {
                                    CollectFlag = true;
                                    IsCollect = 0;
                                    if (ConfigManager.getInstance() != null && ConfigManager.getInstance().isMale()) {
                                        sexId = 0;
                                    } else {
                                        sexId = 1;
                                    }
                                    String emptyText = null;
                                    if (radioItems == null || radioItems.size() == 0) {
                                        if (collectListEmpty != null && collectListEmpty >= 1) {//???????????????
                                            emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                        } else {//??????????????????
                                            emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty);
                                        }
                                    } else {
                                        emptyText = StringUtils.getString(R.string.playcc_radio_list_trace_empty2);
                                    }
                                    // ??????
                                    RadioTraceEmptyItemViewModel trendItemViewModel = new RadioTraceEmptyItemViewModel(RadioViewModel.this, emptyText);
                                    trendItemViewModel.multiItemType(RadioRecycleType_trace);
                                    radioItems.add(trendItemViewModel);
                                    getBroadcast(1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                        collectReLoad = false;
                    }
                });
    }

    public boolean isShowEmpty(ObservableList<MultiItemViewModel> itemViewModels){
        if (isLoading){
            return false;
        }
        if (itemViewModels.size() > 0){
            if (itemViewModels.size() == 1){
                MultiItemViewModel multiItemViewModel = itemViewModels.get(0);
                String itemType = (String) multiItemViewModel.getItemType();
                if (itemType.equals(RadioRecycleType_trace)){
                    return true;
                }
            }
        }else {
            return true;
        }
        return false;
    }

    //????????????????????????
    public void getAdUserBanner() {
        model.getUserAdList(1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<AdUserBannerBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<AdUserBannerBean> listBaseDataResponse) {
                        AdUserBannerBean adUserBanner = listBaseDataResponse.getData();
                        if (adUserBanner != null) {
                            List<AdUserItemBean> listData = adUserBanner.getDataList();
                            if (ObjectUtils.isNotEmpty(listData)) {
                                ObservableList<RadioItemBannerVideoViewModel> listReal = new ObservableArrayList<>();
                                for (AdUserItemBean adUserItemEntity : listData) {
                                    RadioItemBannerVideoViewModel radioItemBannerVideoViewModel = new RadioItemBannerVideoViewModel(RadioViewModel.this, adUserItemEntity);
                                    listReal.add(radioItemBannerVideoViewModel);
                                }
                                if (!listReal.isEmpty()) {
                                    radioUC.startBannerEvent.call();
                                    if (radioItemsAdUser.size() > 0) {
                                        adapterAdUser.setItems(listReal);
                                        adapterAdUser.notifyItemRangeChanged(0, adapterAdUser.getItemCount() - 1);
                                    } else {
                                        radioItemsAdUser.addAll(listReal);
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        Log.e("??????????????????????????????", "???????????????" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //??????????????????
    public void getAdListBanner() {
        model.getRadioAdBannerList(2)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<AdBannerBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<AdBannerBean> adBannerEntityDataResponse) {
                        AdBannerBean adBannerEntity = adBannerEntityDataResponse.getData();
                        if (adBannerEntity != null) {
                            List<AdItemBean> listData = adBannerEntity.getDataList();
                            if (listData != null && listData.size() > 0) {
                                itemBannerEntity.set(listData);
                                itemBannerShow.set(true);
                            } else {
                                itemBannerShow.set(false);
                            }
                        } else {
                            itemBannerShow.set(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }

                });
    }

    //????????????
    public void newsGive(int posion) {
        model.newsGive(((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_give_success);
                        if (isFirstLike) {
                            RxBus.getDefault().post(new TaskListEvent());
                        }
                        ((TrendItemViewModel) radioItems.get(posion)).addGiveUser();
                        CCApplication.instance().logEvent(AppsFlyerEvent.Like);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //????????????
    public void newsComment(Integer id, String content, Integer toUserId, String toUserName) {
        model.newsComment(id, content, toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_comment_success);
                        if (isFirstComment) {//?????????????????????
                            RxBus.getDefault().post(new TaskListEvent());
                        }
                        for (int i = 0; i < radioItems.size(); i++) {
                            if (radioItems.get(i) instanceof TrendItemViewModel) {
                                if (id == ((TrendItemViewModel) radioItems.get(i)).newsEntityObservableField.get().getId()) {
                                    CCApplication.instance().logEvent(AppsFlyerEvent.Message);
                                    ((TrendItemViewModel) radioItems.get(i)).addComment(id, content, toUserId, toUserName, model.readUserData().getNickname());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 10016) {
                            ToastUtils.showShort(StringUtils.getString(R.string.playcc_comment_close));
                            for (int i = 0; i < radioItems.size(); i++) {
                                if (radioItems.get(i) instanceof TrendItemViewModel) {
                                    if (id == ((TrendItemViewModel) radioItems.get(i)).newsEntityObservableField.get().getId()) {
                                        CCApplication.instance().logEvent(AppsFlyerEvent.Message);
                                        ((TrendItemViewModel) radioItems.get(i)).newsEntityObservableField.get().getBroadcast().setIsComment(1);
                                    }
                                }
                            }
                        } else {
                            if (e.getMessage() != null) {
                                ToastUtils.showShort(e.getMessage());
                            } else {
                                ToastUtils.showShort(R.string.error_http_internal_server_error);
                            }

                            super.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //??????/????????????
    public void setComment(int posion, String type) {
        int broadcastId = ((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getBroadcast().getId();
        int isComment = ((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getBroadcast().getIsComment();
        model.setComment(broadcastId,
                isComment == 0 ? 1 : 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (type.equals(RadioRecycleType_New)) {
                            ToastUtils.showShort(((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getBroadcast().getIsComment() == 1 ? StringUtils.getString(R.string.playcc_open_comment_success) : StringUtils.getString(R.string.playcc_close_success));
                            ((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getBroadcast().setIsComment(
                                    ((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //????????????
    public void deleteNews(int posion) {
        model.deleteNews(((TrendItemViewModel) radioItems.get(posion)).newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        radioItems.remove(posion);
                        try {
                            GSYVideoManager.releaseAllVideos();
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //?????????????????????
    public void getCallingInvitedInfo(int callingType, String IMUserId, String toIMUserId) {
        if (callingType == 1) {
            //????????????????????????
            CCApplication.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.call_voice_male : AppsFlyerEvent.call_voice_female);
        } else {
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
                    public void onError(RequestException e) {
                        super.onError(e);
                        if (e != null) {
                            if (e.getCode() == 21001) {
                                radioUC.sendDialogViewEvent.call();
                            }
                        }
                    }


                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //??????
    public void putAccostFirst(int position) {
        int id = ((TrendItemViewModel)radioItems.get(position)).newsEntityObservableField.get().getUser().getId();
        model.putAccostFirst(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_text_accost_success1);
                        Objects.requireNonNull(((TrendItemViewModel)adapter.getAdapterItem(position)).newsEntityObservableField.get()).getUser().setIsAccost(1);
                        adapter.notifyItemChanged(position);
                        //????????????????????????
                        if (accostThree || dayAccost)RxBus.getDefault().post(new TaskListEvent());
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        if(e.getCode()!=null && e.getCode().intValue()==21001 ){//??????????????????
                            ToastCenterUtils.showToast(R.string.playcc_dialog_exchange_integral_total_text3);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }


    public class UIChangeObservable {
        public SingleLiveEvent clickMore = new SingleLiveEvent<>();
        public SingleLiveEvent clickLike = new SingleLiveEvent<>();
        public SingleLiveEvent clickComment = new SingleLiveEvent<>();
        public SingleLiveEvent clickImage = new SingleLiveEvent<>();
        //??????????????????????????????
        public SingleLiveEvent<Boolean> emptyLayoutShow = new SingleLiveEvent<>();
        public SingleLiveEvent<String> zoomInp = new SingleLiveEvent<>();
        //????????????
        public SingleLiveEvent<Void> clickRegion = new SingleLiveEvent<>();
        //?????????????????????????????????????????????
        public SingleLiveEvent<Void> clickTacking = new SingleLiveEvent<>();
        //????????????
        public SingleLiveEvent otherBusy = new SingleLiveEvent<>();
        //???????????????????????????
        public SingleLiveEvent<Void> sendDialogViewEvent = new SingleLiveEvent<>();
        //????????????banner
        public SingleLiveEvent<Integer> clickBannerIdx = new SingleLiveEvent<>();
        //????????????banner
        public SingleLiveEvent<Void> startBannerEvent = new SingleLiveEvent<>();
    }

}