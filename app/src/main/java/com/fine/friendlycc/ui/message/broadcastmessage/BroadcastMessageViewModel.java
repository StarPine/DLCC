package com.fine.friendlycc.ui.message.broadcastmessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.BoradCastMessageBean;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;

import java.util.List;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class BroadcastMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<BroadcastMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<BroadcastMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<BroadcastMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_broadcast_message);
    UIChangeObservable uc = new UIChangeObservable();

    public BroadcastMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageBoradcast(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<BoradCastMessageBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<BoradCastMessageBean> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<BoradCastMessageBean> list = response.getData().getData();
                        for (BoradCastMessageBean entity : list) {
                            BroadcastMessageItemViewModel item = new BroadcastMessageItemViewModel(BroadcastMessageViewModel.this, entity);
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
        model.deleteMessage("broadcast", observableList.get(position).itemEntity.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
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