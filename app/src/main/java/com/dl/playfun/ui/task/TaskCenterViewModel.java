package com.dl.playfun.ui.task;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.ApiConfigManagerEntity;
import com.dl.playfun.entity.BonusGoodsEntity;
import com.dl.playfun.entity.EjectEntity;
import com.dl.playfun.entity.EjectSignInEntity;
import com.dl.playfun.entity.SystemConfigTaskEntity;
import com.dl.playfun.entity.TaskAdEntity;
import com.dl.playfun.entity.TaskConfigEntity;
import com.dl.playfun.entity.TaskConfigItemEntity;
import com.dl.playfun.event.ReceiveNewUserRewardEvent;
import com.dl.playfun.event.RewardRedDotEvent;
import com.dl.playfun.event.TaskListEvent;
import com.dl.playfun.event.TaskTypeStatusEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.invitewebdetail.InviteWebDetailFragment;
import com.dl.playfun.ui.task.webview.FukuokaViewFragment;
import com.dl.playfun.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2021/8/7 10:52
 * Description: This is TaskCenterViewModel
 */
public class TaskCenterViewModel extends BaseViewModel<AppRepository> {
    private static final String IS_SINGIN = "is_sign_in";
    public static Integer SignWinningDay = -1;
    private final String[] maleSigninDayNumber = new String[]{AppsFlyerEvent.sign_day1_male, AppsFlyerEvent.sign_day2_male, AppsFlyerEvent.sign_day3_male,
            AppsFlyerEvent.sign_day4_male, AppsFlyerEvent.sign_day5_male, AppsFlyerEvent.sign_day6_male, AppsFlyerEvent.sign_day6_male};
    private final String[] femaleSigninDayNumber = new String[]{AppsFlyerEvent.sign_day1_female, AppsFlyerEvent.sign_day2_female, AppsFlyerEvent.sign_day3_female,
            AppsFlyerEvent.sign_day4_female, AppsFlyerEvent.sign_day5_female, AppsFlyerEvent.sign_day6_female, AppsFlyerEvent.sign_day6_female};
    public boolean loadTaskAdFlag = false;

    public ObservableField<Boolean> isShowEmpty = new ObservableField<Boolean>(false);
    private static final String TAG = "任务中心签到领取会员";
    private final int consumeImmediately = 0;
    private final Integer pay_good_day = 7;
    public int currentPage = 1;
    //广告列表
    public BindingRecyclerViewAdapter<TaskCenterADItemViewModel> daily_task_ad_adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TaskCenterADItemViewModel> daily_task_ad_observableList = new ObservableArrayList<>();
    public ItemBinding<TaskCenterADItemViewModel> daily_task_ad_itemBinding = ItemBinding.of(BR.viewModel, R.layout.task_item_ad);
    //每日任务
    public BindingRecyclerViewAdapter<TaskCenterItemViewModel> daily_task_adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TaskCenterItemViewModel> daily_task_observableList = new ObservableArrayList<>();
    public ItemBinding<TaskCenterItemViewModel> daily_task_itemBinding = ItemBinding.of(BR.viewModel, R.layout.task_center_item_fragment);
    //积分商品列表
    public ObservableField<Boolean> isShowSign = new ObservableField<>(true);
    public ObservableField<Boolean> isUnfold = new ObservableField<>(false);
    public ObservableField<Integer> goldMoney = new ObservableField<>(0);
    public UIChangeObservable uc = new UIChangeObservable();
    //福袋
    public ObservableField<List<TaskAdEntity>> adItemEntityObservableField = new ObservableField<>(new ArrayList<>());
    //奖励状态-红点
    private final Map<String, Integer> rewardStausMap = new HashMap<>();
    //用户邀请码
    public ObservableField<String> userInvite = new ObservableField<>();
    public EjectEntity $ejectEntity;
    //连续已经签到N天
    public ObservableField<String> SignDayNumEd = new ObservableField<>("0天");
    //重新刷新页面
    public BindingCommand emptyRetryOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            loadDatas(1);
        }
    });
    //跳转帮助界面
    public BindingCommand helpOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //Bundle bundle = WebDetailFragment.getStartBundle(AppConfig.TASK_TITLE_HELP);
            ApiConfigManagerEntity apiConfigManager = model.readApiConfigManagerEntity();
            if(apiConfigManager!=null){
                if(!StringUtils.isEmpty(apiConfigManager.getPlayChatWebUrl())){
                    Bundle bundle = new Bundle();
                    bundle.putString("link", apiConfigManager.getPlayChatWebUrl()+"/Rule_renwu2");
                    start(FukuokaViewFragment.class.getCanonicalName(), bundle);
                }
            }
        }
    });
    //点击签到
    public BindingCommand onClickday = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if ($ejectEntity != null && $ejectEntity.getIsSignIn() == 0) {
                reportEjectSignIn();
            } else {
                ToastUtils.showShort(R.string.task_fragment_eject_error);
            }
        }
    });
    public BindingCommand onClickRepeat = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort(R.string.task_fragment_eject_error1);
        }
    });
    //点击加载全部
    public BindingCommand ocClickUnfold = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            boolean flag = !isUnfold.get();
            isUnfold.set(flag);
            uc.UnfoldEvent.setValue(flag);
        }
    });
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        currentPage = 1;
        loadDatas(currentPage);
    });
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> nextPage());
    private final String orderNumber = null;
    private final String google_goods_id = null;
    private Disposable mModCompletedsubscribe, newUserCompletedsubscribe;
    private Integer isSinginNum = -1;

    public TaskCenterViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
//        SystemConfigTaskEntity systemConfigTaskEntity = ConfigManager.getInstance().getTaskConfig();
//        if (!ObjectUtils.isEmpty(systemConfigTaskEntity) && !StringUtils.isTrimEmpty(systemConfigTaskEntity.getEntryLabel())) {
//            uc.loadSysConfigTask.setValue(systemConfigTaskEntity);
//        }
        loadDatas(1);
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mModCompletedsubscribe = RxBus.getDefault().toObservable(TaskListEvent.class).subscribe(event -> {
            getTaskListConfig();
        });
        newUserCompletedsubscribe = RxBus.getDefault().toObservable(ReceiveNewUserRewardEvent.class).subscribe(event -> {
            ToaskSubBonus("register", -1, 2);
        });
        RxSubscriptions.add(newUserCompletedsubscribe);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mModCompletedsubscribe);
        RxSubscriptions.remove(newUserCompletedsubscribe);

    }

    protected void nextPage() {
        currentPage++;
        loadDatas(currentPage);
    }

    /**
     * 停止下拉刷新或加载更多动画
     */
    protected void stopRefreshOrLoadMore() {
        if (currentPage == 1) {
            uc.finishRefreshing.call();
        } else {
            uc.finishLoadmore.call();
        }
    }

    public void loadDatas(int page) {
        getTaskConfig();
        getTaskListConfig();
    }


    //领取积分
    public void ToaskSubBonus(String key, int type, int idx) {
        model.TaskRewardReceive(key)    //1.3.0新接口
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (key.equals("register")) {
                            AppContext.instance().logEvent(AppsFlyerEvent.task_register);
                        } else if (key.equals("firstIm")) {
                            AppContext.instance().logEvent(AppsFlyerEvent.task_first_profit);
                        }
                        if (daily_task_observableList != null && daily_task_observableList.size() > 0) {
                            TaskCenterItemViewModel taskModel = daily_task_observableList.get(idx);
                            int score = Double.valueOf(taskModel.itemEntity.get().getRewardValue()).intValue();
                            int taskType = taskModel.itemEntity.get().getTaskType();
                            taskModel.itemEntity.get().setRefresh(true, 2);

                            daily_task_observableList.remove(idx);
                            if (taskType != 1) {
                                daily_task_observableList.add(taskModel);
                            }
                            daily_task_adapter.notifyDataSetChanged();
                            goldMoney.set(goldMoney.get().intValue() + score);
                        }


                        rewardStausMap.put(key, 0);
                        if (!rewardStausMap.containsValue(1)) {
                            //隐藏小红点
                            RxBus.getDefault().post(new RewardRedDotEvent(false));
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //获取任务列表
    public void getTaskListConfig() {
        model.getTaskListConfig()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<TaskConfigItemEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<TaskConfigItemEntity>> response) {
                        List<TaskConfigItemEntity> taskConfigListEntity = response.getData();
                        daily_task_observableList.clear();
                        if (rewardStausMap.containsKey(IS_SINGIN)) {
                            isSinginNum = rewardStausMap.get(IS_SINGIN);
                        }
                        rewardStausMap.clear();
                        TaskTypeStatusEvent taskTypeStatusEvent = new TaskTypeStatusEvent();
                        for (TaskConfigItemEntity newbieTask : taskConfigListEntity) {
                            if (newbieTask.getSulg().equals("dayAccost")) {
                                taskTypeStatusEvent.setDayAccost(newbieTask.getStatus());
                            } else if (newbieTask.getSulg().equals("accostThree")) {
                                taskTypeStatusEvent.setAccostThree(newbieTask.getStatus());
                            } else if (newbieTask.getSulg().equals("dayCommentNews")) {
                                taskTypeStatusEvent.setDayCommentNews(newbieTask.getStatus());
                            } else if (newbieTask.getSulg().equals("dayGiveNews")) {
                                taskTypeStatusEvent.setDayGiveNews(newbieTask.getStatus());
                            }

                            rewardStausMap.put(newbieTask.getSulg(), newbieTask.getStatus());
                            TaskCenterItemViewModel item = new TaskCenterItemViewModel(TaskCenterViewModel.this, newbieTask, 0);
                            daily_task_observableList.add(item);
                        }
                        //添加奖励状态到list
                        if (isSinginNum != -1) {
                            rewardStausMap.put(IS_SINGIN, isSinginNum);
                        }
                        RxBus.getDefault().post(taskTypeStatusEvent);
                        if (rewardStausMap.containsValue(1)) {
                            //显示小红点
                            RxBus.getDefault().post(new RewardRedDotEvent(true));
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //加载任务中心配置
    public void getTaskConfig() {
        model.getTaskConfig()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<TaskConfigEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<TaskConfigEntity> taskConfigEntityBaseDataResponse) {
                        isShowEmpty.set(false);
                        TaskConfigEntity taskConfigEntity = taskConfigEntityBaseDataResponse.getData();
                        EjectEntity ejectEntity = new EjectEntity(taskConfigEntity.getIsSignIn(), taskConfigEntity.getDayNumber(), taskConfigEntity.getMaleConfig(), taskConfigEntity.getFemaleConfig());
                        rewardStausMap.put(IS_SINGIN, taskConfigEntity.getIsSignIn() == 1 ? 0 : 1);
                        if (rewardStausMap.containsValue(1)) {//因为请求是异步的，所以要在这里调下小红点
                            //显示小红点
                            RxBus.getDefault().post(new RewardRedDotEvent(true));
                        }
                        //加载任务中心背景图
                        SystemConfigTaskEntity systemConfigTaskEntity = new SystemConfigTaskEntity();
                        systemConfigTaskEntity.setBackgroundImg(taskConfigEntity.getBackgroundImg());
                        systemConfigTaskEntity.setHeadImg(systemConfigTaskEntity.getHeadImg());
                        uc.loadSysConfigTask.setValue(systemConfigTaskEntity);

                        ejectEntity.setFirstSign(taskConfigEntity.getFirstSign());
                        $ejectEntity = ejectEntity;
                        uc.EjectDay.setValue(ejectEntity);
                        SignDayNumEd.set(String.format(StringUtils.getString(R.string.task_sign_day), ejectEntity.getDayNumber()));
                        isShowSign.set(ejectEntity.getIsSignIn() == 1);
                        goldMoney.set(taskConfigEntity.getTotalMoney() == null ? 0 : taskConfigEntity.getTotalMoney());
                        daily_task_observableList.clear();
                        userInvite.set(taskConfigEntity.getCode());
                        List<TaskConfigItemEntity> taskConfigListEntity = taskConfigEntity.getTask();
                        for (TaskConfigItemEntity newbieTask : taskConfigListEntity) {
                            TaskCenterItemViewModel item = new TaskCenterItemViewModel(TaskCenterViewModel.this, newbieTask, 0);
                            daily_task_observableList.add(item);
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        isShowEmpty.set(true);
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        stopRefreshOrLoadMore();
                    }
                });
    }

    /**
     * @return void
     * @Desc TODO(签到成功)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/6
     */
    public void reportEjectSignIn() {
        model.reportEjectSignIn()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<EjectSignInEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<EjectSignInEntity> ejectSignInEntity) {
                        isShowSign.set(true);
                        if (isSinginNum != -1) {
                            rewardStausMap.put(IS_SINGIN, 0);
                        }
                        if (!rewardStausMap.containsValue(1)) {
                            //隐藏小红点
                            RxBus.getDefault().post(new RewardRedDotEvent(false));
                        }
                        if (!ObjectUtils.isEmpty(ejectSignInEntity)) {
                            Integer dayNumer = ejectSignInEntity.getData().getDayNumber();
                            //签到成功埋点
                            if (ConfigManager.getInstance().isMale()) {
                                AppContext.instance().logEvent(maleSigninDayNumber[dayNumer - 1]);
                            } else {
                                AppContext.instance().logEvent(femaleSigninDayNumber[dayNumer - 1]);
                            }
                            SignDayNumEd.set(String.format(StringUtils.getString(R.string.task_sign_day), dayNumer));
                            EjectSignInEntity signInEntity = ejectSignInEntity.getData();
                            uc.reporSignInSuccess.setValue(signInEntity);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //跳转邀请好友界面
    public void touserInviteWeb() {
        String code = userInvite.get();
        if (StringUtils.isEmpty(code)) {
            ToastUtils.showShort(R.string.task_fragment_error);
            return;
        }
        Bundle bundle = InviteWebDetailFragment.getStartBundle(model.readApiConfigManagerEntity().getPlayFunApiUrl() + model.readUserData().getInviteUrl(), userInvite.get());
        start(InviteWebDetailFragment.class.getCanonicalName(), bundle);
    }

    /**
     * @return void
     * @Desc TODO(兑换商品)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/11
     */
    public void exchange(String key, BonusGoodsEntity bonusGoodsEntity) {
        model.exchange(key)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        uc.exchangeSuccess.setValue(bonusGoodsEntity);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }


    public class UIChangeObservable {
        //加载任务中心系统配置
        public SingleLiveEvent<SystemConfigTaskEntity> loadSysConfigTask = new SingleLiveEvent<>();
        //下拉刷新开始
        public SingleLiveEvent<Void> startRefreshing = new SingleLiveEvent<>();
        //下拉刷新完成
        public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent<Void> finishLoadmore = new SingleLiveEvent<>();
        //兑换弹窗显示
        public SingleLiveEvent<BonusGoodsEntity> AlertBonuClick = new SingleLiveEvent<>();
        //兑换成功
        public SingleLiveEvent<BonusGoodsEntity> exchangeSuccess = new SingleLiveEvent<>();
        //加载全部-收起
        public SingleLiveEvent<Boolean> UnfoldEvent = new SingleLiveEvent<>();
        SingleLiveEvent<EjectSignInEntity> reporSignInSuccess = new SingleLiveEvent<>();
        SingleLiveEvent<EjectEntity> EjectDay = new SingleLiveEvent<>();
        //弹出充值钻石弹窗
        public SingleLiveEvent<Void> DialogCoinExchangeIntegral = new SingleLiveEvent<>();
    }
}
