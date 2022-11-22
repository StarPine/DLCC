package com.fine.friendlycc.ui.mine.wallet.coin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.bean.CoinWalletBean;
import com.fine.friendlycc.bean.UserCoinItemBean;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.main.MainFragment;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.widget.emptyview.EmptyState;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class CoinViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<CoinItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public ObservableField<CoinWalletBean> coinWalletEntity = new ObservableField<>();

    public ObservableList<CoinItemViewModel> observableList = new ObservableArrayList<>();

    public ObservableField<String> paypalAccount = new ObservableField<>(StringUtils.getString(R.string.playcc_fragment_cash_withdraw_account_bind));
    public ObservableField<String> emptyText = new ObservableField<>();
    public ObservableField<Boolean> isShowEmpty = new ObservableField<Boolean>(false);
    //RecyclerView多布局添加ItemBinding
    public ItemBinding<CoinItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_coin_earnings);
    UIChangeObservable uc = new UIChangeObservable();
    //跳轉到任務中心
    public BindingCommand doingTasknClickCommand = new BindingCommand(() -> {
        AppConfig.homePageName = "navigation_rank";
        popTo(MainFragment.class.getCanonicalName());
    });

    public boolean getIsMale(){
        return ConfigManager.getInstance().isMale();
    }

    public CoinViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        startRefresh();

    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
    }

    @Override
    public void loadDatas(int page) {
        if (page == 1) {
            loadCoinWallet();
        }
        if(ConfigManager.getInstance().isMale()){
            emptyText.set(StringUtils.getString(R.string.playcc_coin_fragment_empty_male));
        }else {
            emptyText.set(StringUtils.getString(R.string.playcc_coin_fragment_empty_female));
        }
        model.userCoinEarnings(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<UserCoinItemBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<UserCoinItemBean> response) {
                        if (response.getData() == null || response.getData().getTotal() == 0) {
                            isShowEmpty.set(true);
                            stateModel.setEmptyState(EmptyState.EMPTY);
                        } else {
                            isShowEmpty.set(false);
                            stateModel.setEmptyState(EmptyState.NORMAL);
                        }

                        if (page == 1) {
                            observableList.clear();
                        }
                        for (UserCoinItemBean entity : response.getData().getData()) {
                            CoinItemViewModel item = new CoinItemViewModel(CoinViewModel.this, entity);
                            observableList.add(item);
                        }

                    }

                    @Override
                    public void onError(RequestException e) {
                        isShowEmpty.set(true);
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    public void loadCoinWallet() {
        model.coinWallet()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<CoinWalletBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinWalletBean> response) {
                        coinWalletEntity.set(response.getData());
                        if (!StringUtils.isEmpty(response.getData().getAccountNumber())) {
                            paypalAccount.set(String.format("%s(%s)", response.getData().getAccountNumber(), response.getData().getRealName()));
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> withdrawComplete = new SingleLiveEvent<>();
    }

}