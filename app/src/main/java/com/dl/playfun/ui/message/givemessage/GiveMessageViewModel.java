package com.dl.playfun.ui.message.givemessage;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.GiveMessageEntity;
import com.dl.playfun.ui.mine.broadcast.mytrends.trenddetail.TrendDetailFragment;
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
public class GiveMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<GiveMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<GiveMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<GiveMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_give_message);
    UIChangeObservable uc = new UIChangeObservable();

    public GiveMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    public void itemClick(int position) {
        GiveMessageEntity itemEntity = observableList.get(position).itemEntity.get();
        if (itemEntity.getRelationType() == 2) {
            //动态
            Bundle bundle = TrendDetailFragment.getStartBundle(itemEntity.getRelationId());
            start(TrendDetailFragment.class.getCanonicalName(), bundle);
        }
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageGive(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<GiveMessageEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<GiveMessageEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<GiveMessageEntity> list = response.getData().getData();
                        for (GiveMessageEntity entity : list) {
                            GiveMessageItemViewModel item = new GiveMessageItemViewModel(GiveMessageViewModel.this, entity);
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
        GiveMessageItemViewModel itemViewModel = observableList.get(position);
        model.deleteMessage("give", itemViewModel.itemEntity.get().getId())
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
