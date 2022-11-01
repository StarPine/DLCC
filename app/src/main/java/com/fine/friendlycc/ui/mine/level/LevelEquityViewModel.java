package com.fine.friendlycc.ui.mine.level;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.LevelApiEntity;
import com.fine.friendlycc.entity.LevelCoinOptionInfo;
import com.fine.friendlycc.entity.LevelCoinSelectInfo;
import com.fine.friendlycc.entity.LevelPageInfoEntity;
import com.fine.friendlycc.entity.LevelSelectInfoEntity;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2022/6/18 18:17
 * Description: This is LevelEquityViewModel
 */
public class LevelEquityViewModel extends BaseViewModel<AppRepository> {

    public UIChangeObservable uc = new UIChangeObservable();

    public BindingRecyclerViewAdapter<LevelEquityItemTitleViewModel> adapterTitle = new BindingRecyclerViewAdapter<>();
    public ObservableList<LevelEquityItemTitleViewModel> observableListTitle = new ObservableArrayList<>();
    public ObservableList<LevelEquityItemTitleViewModel> observableListBanner = new ObservableArrayList<>();
    public ItemBinding<LevelEquityItemTitleViewModel> itemBindingTitle = ItemBinding.of(BR.viewModel, R.layout.item_level_equity_title);
    public ItemBinding<LevelEquityItemTitleViewModel> itemBindingBanner = ItemBinding.of(BR.viewModel, R.layout.item_level_equity_banner);

    public BindingRecyclerViewAdapter<LevelEquityItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<LevelEquityItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<LevelEquityItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_level_equity_detail);

    public ObservableField<String> currentTextHint = new ObservableField<>();
    public ObservableField<String> hintTv = new ObservableField<>();
    public ObservableField<LevelPageInfoEntity.UserLevelInfo> userLevelInfo = new ObservableField<>();

    public ObservableField<String> Tv_ChatCoins = new ObservableField<>();
    public ObservableField<String> Tv_ChatProfits = new ObservableField<>();
    public ObservableField<String> Tv_ChatInterval = new ObservableField<>();
    public ObservableField<String> Tv_ChatMoney = new ObservableField<>();

    public ObservableField<String> Tv_VoiceCoins = new ObservableField<>();
    public ObservableField<String> Tv_VoiceProfits = new ObservableField<>();
    public ObservableField<String> Tv_VoiceInterval = new ObservableField<>();
    public ObservableField<String> Tv_VoiceMoney = new ObservableField<>();

    public ObservableField<String> Tv_VideoCoins = new ObservableField<>();
    public ObservableField<String> Tv_VideoProfits = new ObservableField<>();
    public ObservableField<String> Tv_VideoInterval = new ObservableField<>();
    public ObservableField<String> Tv_VideoMoney = new ObservableField<>();

    public ObservableField<String> imgUrlLevel = new ObservableField<>();

    public LevelCoinSelectInfo levelChatSelectInfo;
    public LevelCoinSelectInfo levelVideoSelectInfo;
    public LevelCoinSelectInfo levelAudioSelectInfo;

    public LevelSelectInfoEntity levelSelectInfoEntity;
    public List<LevelSelectInfoEntity.LevelTips> listLevelTips;

    public int lastTitleClickIdx = -1;

    //tab选择状态
    public ObservableBoolean tabLayoutState = new ObservableBoolean(true);

    //点击切换tab
    public BindingCommand<Boolean> backView = new BindingCommand(this::pop);
    public ObservableBoolean clickChatSubDisplay = new ObservableBoolean(false);
    public ObservableBoolean clickAudioSubDisplay = new ObservableBoolean(false);
    public ObservableBoolean clickVideoSubDisplay = new ObservableBoolean(false);

    public int $clickChatIndex = -1;
    public int $clickAudioIndex = -1;
    public int $clickVideoIndex = -1;

    public int clickChatIndex = -1;
    public int clickAudioIndex = -1;
    public int clickVideoIndex = -1;
    //文字调整按钮
    public BindingCommand clickChatSub = new BindingCommand(() -> {
        adjustPrice(1, levelChatSelectInfo.getOptions().get(clickChatIndex));
    });
    //语音调整按钮
    public BindingCommand clickAudiotSub = new BindingCommand(() -> {
        adjustPrice(2, levelAudioSelectInfo.getOptions().get(clickAudioIndex));
    });
    //视频调整按钮
    public BindingCommand clickVideoSub = new BindingCommand(() -> {
        adjustPrice(3, levelVideoSelectInfo.getOptions().get(clickVideoIndex));
    });

    public void titleRcvItemClick(int idx, boolean flag) {
        if (observableListTitle == null) {
            return;
        }
        if (lastTitleClickIdx == -1) {
            observableListTitle.get(idx).checkCurrent.set(true);
            lastTitleClickIdx = idx;
        } else {
            if (idx != lastTitleClickIdx) {
                observableListTitle.get(lastTitleClickIdx).checkCurrent.set(false);
                observableListTitle.get(idx).checkCurrent.set(true);
                lastTitleClickIdx = idx;
                if (flag) {
                    uc.scrollBannerIndex.setValue(idx);
                }
            }
        }

    }

    //点击切换tab
    public BindingCommand<Boolean> leftTabLayoutOnClickCommand = new BindingCommand(() -> {
        if (!tabLayoutState.get()) {
            tabLayoutState.set(true);
        }
    });

    //点击切换tab
    public BindingCommand<Boolean> rightTabLayoutOnClickCommand = new BindingCommand(() -> {
        if (tabLayoutState.get()) {
            tabLayoutState.set(false);
        }
    });

    public LevelEquityViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public void loadDataInfo() {
        model.getUserLevelPageInfo()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<LevelPageInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<LevelPageInfoEntity> responseData) {
                        LevelPageInfoEntity LevelPageInfoEntity = responseData.getData();
                        hintTv.set(LevelPageInfoEntity.getModifyRestrictionTips());
                        levelSelectInfoEntity = LevelPageInfoEntity.getLevelSelectInfo();
                        //权益列表
                        List<LevelSelectInfoEntity.LevelInfo> levelInfoList = levelSelectInfoEntity.getLevelList();
                        listLevelTips = levelSelectInfoEntity.getLevelTipsList();
                        LevelPageInfoEntity.UserLevelInfo $UserLevelInfo = LevelPageInfoEntity.getUserLevelInfo();
                        userLevelInfo.set(LevelPageInfoEntity.getUserLevelInfo());
                        //讯息每条收益
                        Tv_ChatCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_chat1), $UserLevelInfo.getChatCoins()));
                        //讯息水晶奖励
                        Tv_ChatProfits.set(StringUtils.getString(R.string.fragment_level_text8) + $UserLevelInfo.getChatProfits());
                        //语音每条收益
                        Tv_VoiceCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_audio1), $UserLevelInfo.getVoiceCoins()));
                        //语音水晶奖励
                        Tv_VoiceProfits.set(StringUtils.getString(R.string.fragment_level_text8) + $UserLevelInfo.getVoiceProfits());
                        //视频每条收益
                        Tv_VideoCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_video2), $UserLevelInfo.getVideoCoins()));
                        //视频水晶奖励
                        Tv_VideoProfits.set(StringUtils.getString(R.string.fragment_level_text8) + $UserLevelInfo.getVideoProfits());
                        Tv_ChatMoney.set(String.valueOf($UserLevelInfo.getChatProfits()));
                        Tv_VoiceMoney.set(String.valueOf($UserLevelInfo.getVoiceProfits()));
                        Tv_VideoMoney.set(String.valueOf($UserLevelInfo.getVideoProfits()));
                        for (LevelSelectInfoEntity.LevelTips levelTips : listLevelTips) {
                            if (levelTips.level == levelSelectInfoEntity.getUserLevel()) {
                                currentTextHint.set(levelTips.getTips());
                                break;
                            }
                        }
                        List<LevelEquityItemTitleViewModel> levelEquityItemTitleViewModelList = new ArrayList<>();
                        for (int i = 0; i < levelInfoList.size(); i++) {
                            boolean check = levelSelectInfoEntity.getUserLevel() == levelSelectInfoEntity.getLevelTipsList().get(i).getLevel();
                            if (check) {
                                lastTitleClickIdx = i;
                                imgUrlLevel.set(levelInfoList.get(i).getLevelImage());
                            }
                            LevelEquityItemTitleViewModel levelEquityItemTitleViewModel = new LevelEquityItemTitleViewModel(LevelEquityViewModel.this, check, levelInfoList.get(i));
                            levelEquityItemTitleViewModelList.add(levelEquityItemTitleViewModel);
                            LevelEquityItemViewModel levelEquityItemViewModel = new LevelEquityItemViewModel(LevelEquityViewModel.this, check, levelInfoList.get(i));
                            observableList.add(levelEquityItemViewModel);
                        }
                        observableListTitle.addAll(levelEquityItemTitleViewModelList);
                        observableListBanner.addAll(levelEquityItemTitleViewModelList);
                        uc.levelInfoPageInfoEvent.setValue(LevelPageInfoEntity);
                        uc.scrollBannerIndex.setValue(lastTitleClickIdx);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //主播调价
    public void adjustPrice(int state, LevelCoinOptionInfo optionInfo) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("adjustPriceType", state);
        dataMap.put("coins", optionInfo.getCoins());
        model.adjustLevelPrice(ApiUitl.getBody(GsonUtils.toJson(dataMap)))
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<LevelApiEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<LevelApiEntity> response) {
                        LevelApiEntity levelApiEntity = response.getData();
                        if (levelApiEntity != null && levelApiEntity.getEnableChangeAgain() == 1) {
                            //可以继续修改
                            switch (state) {
                                case 1:
                                    //讯息每条收益
                                    Tv_ChatCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_chat1), optionInfo.getCoins()));
                                    //讯息水晶奖励
                                    Tv_ChatProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    clickChatSubDisplay.set(false);
                                    levelChatSelectInfo.setSelectedCoins(optionInfo.getCoins());
                                    break;
                                case 2:
                                    //语音每条收益
                                    Tv_VoiceCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_audio1), optionInfo.getCoins()));
                                    //语音水晶奖励
                                    Tv_VoiceProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    clickAudioSubDisplay.set(false);
                                    levelAudioSelectInfo.setSelectedCoins(optionInfo.getCoins());
                                    break;
                                case 3:
                                    //视频每条收益
                                    Tv_VideoCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_video2), optionInfo.getCoins()));
                                    //视频水晶奖励
                                    Tv_VideoProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    clickVideoSubDisplay.set(false);
                                    levelVideoSelectInfo.setSelectedCoins(optionInfo.getCoins());
                                    break;
                            }
                        } else {//不可以修改
                            switch (state) {
                                case 1:
                                    //讯息每条收益
                                    Tv_ChatCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_chat1), optionInfo.getCoins()));
                                    //讯息水晶奖励
                                    Tv_ChatProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    levelChatSelectInfo.setSelectedCoins(optionInfo.getCoins());

                                    levelChatSelectInfo.setEnableChange(0);
                                    clickChatSubDisplay.set(false);
                                    break;
                                case 2:
                                    //语音每条收益
                                    Tv_VoiceCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_audio1), optionInfo.getCoins()));
                                    //语音水晶奖励
                                    Tv_VoiceProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    levelAudioSelectInfo.setSelectedCoins(optionInfo.getCoins());

                                    levelAudioSelectInfo.setEnableChange(0);
                                    clickAudioSubDisplay.set(false);
                                    break;
                                case 3:
                                    //视频每条收益
                                    Tv_VideoCoins.set(String.format(StringUtils.getString(R.string.fragment_level_text_video2), optionInfo.getCoins()));
                                    //视频水晶奖励
                                    Tv_VideoProfits.set(StringUtils.getString(R.string.fragment_level_text8) + optionInfo.getProfits());
                                    levelVideoSelectInfo.setSelectedCoins(optionInfo.getCoins());

                                    levelVideoSelectInfo.setEnableChange(0);
                                    clickVideoSubDisplay.set(false);
                                    break;
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
        public SingleLiveEvent<LevelPageInfoEntity> levelInfoPageInfoEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> scrollBannerIndex = new SingleLiveEvent<>();
    }
}
