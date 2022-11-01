package com.fine.friendlycc.ui.mine.blacklist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.BlackEntity;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import java.util.List;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class BlacklistViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<BlackListItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<BlackListItemViewModel> observableList = new ObservableArrayList<>();
    //RecyclerView多布局添加ItemBinding
    public ItemBinding<BlackListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_black_list);

    public BlacklistViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onLazyInitView() {
        super.onLazyInitView();
        loadDatas(1);
    }

    @Override
    public void loadDatas(int page) {
        model.getBlackList(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<BlackEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<BlackEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<BlackEntity> list = response.getData().getData();
                        for (BlackEntity blackEntity : list) {
                            BlackListItemViewModel item = new BlackListItemViewModel(BlacklistViewModel.this, blackEntity);
                            observableList.add(item);
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    public void addBlackList(int position) {
        int id = observableList.get(position).itemEntity.get().getUser().getId();
        model.addBlack(id)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        observableList.get(position).isCancel.set(false);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void delBlackList(int position) {
        int id = observableList.get(position).itemEntity.get().getUser().getId();
        model.deleteBlack(id)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        observableList.get(position).isCancel.set(true);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

}