package com.dl.playfun.ui.message.profitmessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.ProfitMessageEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.wallet.WalletFragment;
import com.dl.playfun.ui.mine.wallet.coin.CoinFragment;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;
import com.dl.playfun.BR;
import com.dl.playfun.R;

import java.util.List;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class ProfitMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<ProfitMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<ProfitMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<ProfitMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_profit_message);
    UIChangeObservable uc = new UIChangeObservable();

    public ProfitMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    public void onItemClick(int position) {
        //男生跳转钻石页面。女生跳转体现页面
        if (ConfigManager.getInstance().isMale()) {
            start(CoinFragment.class.getCanonicalName());
        } else {
            start(WalletFragment.class.getCanonicalName());
        }
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageProfit(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<ProfitMessageEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<ProfitMessageEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<ProfitMessageEntity> list = response.getData().getData();
                        for (ProfitMessageEntity entity : list) {
                            ProfitMessageItemViewModel item = new ProfitMessageItemViewModel(ProfitMessageViewModel.this, entity);
                            observableList.add(item);
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    public void deleteMessage(int position) {
        model.deleteMessage("profit", observableList.get(position).itemEntity.get().getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        observableList.remove(position);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Integer> clickDelete = new SingleLiveEvent<>();
    }
}
