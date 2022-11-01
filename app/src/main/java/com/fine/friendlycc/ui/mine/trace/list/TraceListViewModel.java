package com.fine.friendlycc.ui.mine.trace.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.TraceEntity;
import com.fine.friendlycc.event.LikeChangeEvent;
import com.fine.friendlycc.event.TraceEvent;
import com.fine.friendlycc.ui.mine.trace.TraceItemViewModel;
import com.fine.friendlycc.ui.mine.trace.TraceViewModel;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2021/8/3 11:32
 * Description: This is TraceListViewModel
 */
public class TraceListViewModel extends BaseViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<TraceItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TraceItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<TraceItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.trace_item_park);
    public Integer grend = 0;
    public UIChangeObservable uc = new UIChangeObservable();
    public TraceViewModel traceViewModel;
    private int currentPage = 1;
    private int total = 1;
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        currentPage = 1;
        loadDatas(currentPage);
    });
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> nextPage());

    public TraceListViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    protected void nextPage() {
        currentPage++;
        loadDatas(currentPage);
    }

    @Override
    public void onLazyInitView() {
        super.onLazyInitView();
        loadDatas(1);
    }

    public void loadDatas(int page) {
        if (grend == 0) {
            collect(page);
        } else {
            collectFans(page);
        }
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

    public void collect(Integer page) {
        model.collect(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<TraceEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<TraceEntity> response) {
                        super.onSuccess(response);
                        if (currentPage == 1) {
                            observableList.clear();
                        }
                        total = response.getData().getTotal();
                        RxBus.getDefault().post(new TraceEvent(response.getData().getTotal(), grend));
                        for (TraceEntity itemEntity : response.getData().getData()) {
                            TraceItemViewModel item = new TraceItemViewModel(TraceListViewModel.this, itemEntity, grend);
                            observableList.add(item);
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
//                        if (currentPage == 1) {
//                            uc.loadRefresh.call();
//                        }
                    }
                });
    }

    public void collectFans(Integer page) {
        model.collectFans(page)
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
                        for (TraceEntity itemEntity : response.getData().getData()) {
                            TraceItemViewModel item = new TraceItemViewModel(TraceListViewModel.this, itemEntity, grend);
                            observableList.add(item);
                        }
                        RxBus.getDefault().post(new TraceEvent(response.getData().getTotal(), grend));
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                        // uc.loadRefresh.call();
                    }
                });
    }

    public void delLike(int position) {
        TraceEntity traceEntity = observableList.get(position).itemEntity.get();
        model.deleteCollect(traceEntity.getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        RxBus.getDefault().post(new LikeChangeEvent(TraceListViewModel.this, traceEntity.getId(), false));
                        if (grend == 0) {
                            observableList.remove(position);
                        } else {
                            observableList.get(position).itemEntity.get().setFollow(false);
                            adapter.notifyDataSetChanged();
                        }
                        collect(1);
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public void addLike(int position) {
        TraceEntity traceEntity = observableList.get(position).itemEntity.get();
        model.addCollect(traceEntity.getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        LikeChangeEvent likeChangeEvent = new LikeChangeEvent(TraceListViewModel.this, traceEntity.getId(), true);
                        RxBus.getDefault().post(likeChangeEvent);
                        observableList.get(position).itemEntity.get().setFollow(true);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        traceEntity.setFollow(false);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        //取消关注
        public SingleLiveEvent<Integer> clickDelLike = new SingleLiveEvent<>();
        //完成刷新
        public SingleLiveEvent<Void> loadRefresh = new SingleLiveEvent<>();

        //下拉刷新开始
        public SingleLiveEvent<Void> startRefreshing = new SingleLiveEvent<>();
        //下拉刷新完成
        public SingleLiveEvent<Void> finishRefreshing = new SingleLiveEvent<>();
        //上拉加载完成
        public SingleLiveEvent<Void> finishLoadmore = new SingleLiveEvent<>();

    }

}
