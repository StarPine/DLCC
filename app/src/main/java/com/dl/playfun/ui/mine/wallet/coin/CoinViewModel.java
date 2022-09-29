package com.dl.playfun.ui.mine.wallet.coin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.entity.CoinWalletEntity;
import com.dl.playfun.entity.UserCoinItemEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.main.MainFragment;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;
import com.dl.playfun.widget.emptyview.EmptyState;
import com.dl.playfun.BR;
import com.dl.playfun.R;

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

    public ObservableField<CoinWalletEntity> coinWalletEntity = new ObservableField<>();

    public ObservableList<CoinItemViewModel> observableList = new ObservableArrayList<>();

    public ObservableField<String> paypalAccount = new ObservableField<>(StringUtils.getString(R.string.playfun_fragment_cash_withdraw_account_bind));
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
            emptyText.set(StringUtils.getString(R.string.playfun_coin_fragment_empty_male));
        }else {
            emptyText.set(StringUtils.getString(R.string.playfun_coin_fragment_empty_female));
        }
        model.userCoinEarnings(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<UserCoinItemEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<UserCoinItemEntity> response) {
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
                        for (UserCoinItemEntity entity : response.getData().getData()) {
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
                .subscribe(new BaseObserver<BaseDataResponse<CoinWalletEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<CoinWalletEntity> response) {
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