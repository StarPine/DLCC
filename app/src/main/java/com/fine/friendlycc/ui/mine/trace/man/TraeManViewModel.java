package com.fine.friendlycc.ui.mine.trace.man;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.entity.TraceEntity;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2021/8/4 12:25
 * Description: This is TraeManViewModel
 */
public class TraeManViewModel extends BaseViewModel<AppRepository> {

    public int currentPage = 1;

    public BindingRecyclerViewAdapter<TraceManItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TraceManItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<TraceManItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.trace_item_park_man);
    public ObservableField<Integer> totalCount = new ObservableField<>(0);

    public UIChangeObservable uc = new UIChangeObservable();

    public long expireTime = 0;
    public Integer isPlay = -1;
    public BindingCommand AlertVipOnClickCommand = new BindingCommand(() -> {
        uc.clickVip.call();
    });
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        currentPage = 1;
        loadDatas(currentPage);
    });
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> nextPage());

    public TraeManViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadDatas(1);
    }

    public void loadDatas(int page) {
        if (isPlay == 0) {
            return;
        }
        toBrowse(page);
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

    public void toBrowse(Integer page) {
        model.toBrowse(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<TraceEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<TraceEntity> response) {
                        super.onSuccess(response);
                        if (currentPage == 1) {
                            observableList.clear();
                        }
                        totalCount.set(response.getData().getTotal());;
                        expireTime = response.getData().getExpireTime();
                        try {
                            long before = System.currentTimeMillis();
                            if (expireTime - (before / 1000) > 0) {
                                isPlay = 1;
                            } else {
                                isPlay = 0;
                            }
                        } catch (Exception e) {
                            isPlay = 0;
                        }
                        synchronized (observableList) {
                            for (TraceEntity itemEntity : response.getData().getData()) {
                                TraceManItemViewModel item = new TraceManItemViewModel(TraeManViewModel.this, itemEntity, isPlay);
                                observableList.add(item);
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                        uc.loadRefresh.call();
                    }
                });
    }

    /**
     * @return void
     * @Desc TODO(跳转前往用户详细界面)
     * @author 彭石林
     * @parame [userId]
     * @Date 2021/8/5
     */
    public void toUserDetails(Integer userId) {
        Bundle bundle = UserDetailFragment.getStartBundle(userId);
        start(UserDetailFragment.class.getCanonicalName(), bundle);
    }

    public class UIChangeObservable {
        //完成刷新
        public SingleLiveEvent<Void> loadRefresh = new SingleLiveEvent<>();
        //弹出充值VIP
        public SingleLiveEvent<Void> clickVip = new SingleLiveEvent<>();

        //下拉刷新开始
        public SingleLiveEvent<Void> startRefreshing = new SingleLiveEvent<>();
        //下拉刷新完成
        public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent<Void> finishLoadmore = new SingleLiveEvent<>();
    }
}
