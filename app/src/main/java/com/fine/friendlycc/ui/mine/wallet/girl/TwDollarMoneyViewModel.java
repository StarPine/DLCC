package com.fine.friendlycc.ui.mine.wallet.girl;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.UserProfitPageEntity;
import com.fine.friendlycc.entity.UserProfitPageInfoEntity;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.ui.mine.webview.WebViewFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.emptyview.EmptyState;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2021/12/3 14:51
 * Description: This is TwDollarMoneyViewModel
 */
public class TwDollarMoneyViewModel extends BaseViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<TwDollarMoneyItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TwDollarMoneyItemViewModel> observableList = new ObservableArrayList<>();
    //RecyclerView多布局添加ItemBinding
    public ItemBinding<TwDollarMoneyItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_wallet_dollar_money);

    public ObservableField<Integer> enableWithdraw = new ObservableField<>();
    public ObservableField<String> totalProfits = new ObservableField<>();

    public ObservableField<Boolean> isShowEmpty = new ObservableField<Boolean>(false);
    public ObservableField<Boolean> isShowProfitTips = new ObservableField<Boolean>(false);
    public ObservableField<String> withdrawString = new ObservableField<>(StringUtils.getString(R.string.playfun_withdraw));
    public ObservableField<String> currencyName = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();
    private int currentPage = 1;
    private Integer userId = null;
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        currentPage = 1;
        loadDatas(currentPage);
    });
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> nextPage());
    //提现点击
    public BindingCommand withdrawonClickCommand = new BindingCommand(() -> {

        if (isShowEmpty.get()) {//跳转到任务中心
            AppConfig.homePageName = "navigation_rank";
            popTo(MainFragment.class.getCanonicalName());
        } else {//提现
            //没有进行真人认证
            if (model.readUserData().getCertification() != null && model.readUserData().getCertification().intValue() == 1) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("link", AppConfig.WEB_BASE_URL + "reflect");
                    start(WebViewFragment.class.getCanonicalName(), bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {//提示去认证
                uc.certification.call();
            }
        }
    });

    public BindingCommand toWebViewProfitonClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("link", model.readApiConfigManagerEntity().getPlayFunWebUrl() + "/profit");
                start(WebViewFragment.class.getCanonicalName(), bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public TwDollarMoneyViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
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

    protected void nextPage() {
        currentPage++;
        loadDatas(currentPage);
    }

    //初始化加载数据
    public void loadDatas(int page) {
        if (userId == null) {
            userId = model.readUserData().getId();
        }
        if (page == 1) {
            observableList.clear();
        }
        model.getUserProfitPageInfo(userId.longValue(), page, 20)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<UserProfitPageEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserProfitPageEntity> response) {
                        UserProfitPageEntity data = response.getData();
                        totalProfits.set(String.format("%.2f", data.getTotalProfits()));
                        isShowProfitTips.set(data.getDisplayProfitTips()==1);
                        currencyName.set(data.getCurrencyName());
                        UserProfitPageEntity.CustomProfitList pageData = data.getUserProfitList();
                        List<UserProfitPageInfoEntity> listData = pageData.getData();
                        if (!ObjectUtils.isEmpty(listData) && listData.size() > 0) {
                            isShowEmpty.set(false);
                            withdrawString.set(StringUtils.getString(R.string.playfun_withdraw));
                            stateModel.setEmptyState(EmptyState.NORMAL);
                            for (UserProfitPageInfoEntity itemEntity : listData) {
                                TwDollarMoneyItemViewModel twDollarMoneyItemViewModel = new TwDollarMoneyItemViewModel(TwDollarMoneyViewModel.this, itemEntity);
                                observableList.add(twDollarMoneyItemViewModel);
                            }
                        } else {
                            if (observableList == null || observableList.size() < 1) {
                                isShowEmpty.set(true);
                                withdrawString.set(StringUtils.getString(R.string.playfun_dialong_coin_doing_task));
                                stateModel.emptyText.set(StringUtils.getString(R.string.playfun_tw_money_empty));
                                stateModel.setEmptyState(EmptyState.EMPTY);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                        stopRefreshOrLoadMore();
                    }

                });
    }

    public class UIChangeObservable {
        //完成刷新
        public SingleLiveEvent<Void> loadRefresh = new SingleLiveEvent<>();

        //下拉刷新开始
        public SingleLiveEvent<Void> startRefreshing = new SingleLiveEvent<>();
        //下拉刷新完成
        public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent<Void> finishLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> certification = new SingleLiveEvent<>();

    }
}
