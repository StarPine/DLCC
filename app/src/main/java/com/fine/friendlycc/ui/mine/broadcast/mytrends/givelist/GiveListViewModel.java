package com.fine.friendlycc.ui.mine.broadcast.mytrends.givelist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.entity.BaseUserBeanEntity;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.HeadItemViewModel;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class GiveListViewModel extends BaseRefreshViewModel<AppRepository> {
    public ObservableList<GiveListItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<GiveListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_give);
    public BindingRecyclerViewAdapter<GiveListItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    private String type;
    private Integer id;

    public GiveListViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    @Override
    public void loadDatas(int page) {
        if (type.equals(HeadItemViewModel.Type_New)) {
            getNewsGiveList(page);
        } else {
            getTopicalGiveList(page);
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //动态点赞
    public void getNewsGiveList(int page) {
        model.getNewsGiveList(id, page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<BaseUserBeanEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<BaseUserBeanEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        if (!ListUtils.isEmpty(response.getData().getData())) {
                            for (BaseUserBeanEntity baseUser : response.getData().getData()) {
                                GiveListItemViewModel giveListItemViewModel = new GiveListItemViewModel(GiveListViewModel.this, baseUser);
                                observableList.add(giveListItemViewModel);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    //节目点赞
    public void getTopicalGiveList(int page) {
        model.getTopicalGiveList(id, page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<BaseUserBeanEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<BaseUserBeanEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        if (!ListUtils.isEmpty(response.getData().getData())) {
                            for (BaseUserBeanEntity baseUser : response.getData().getData()) {
                                GiveListItemViewModel giveListItemViewModel = new GiveListItemViewModel(GiveListViewModel.this, baseUser);
                                observableList.add(giveListItemViewModel);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }
}