package com.dl.playfun.ui.message.systemmessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.SystemMessageEntity;
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
public class SystemMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<SystemMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<SystemMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<SystemMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_system_message);
    UIChangeObservable uc = new UIChangeObservable();

    public SystemMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);

    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageSystem(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<SystemMessageEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<SystemMessageEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<SystemMessageEntity> list = response.getData().getData();
                        for (SystemMessageEntity entity : list) {
                            SystemMessageItemViewModel item = new SystemMessageItemViewModel(SystemMessageViewModel.this, entity);
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
        model.deleteMessage("system", observableList.get(position).itemEntity.get().getId())
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
