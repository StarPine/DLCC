package com.fine.friendlycc.ui.message.commentmessage;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CommentMessageEntity;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail.TrendDetailFragment;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import java.util.List;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class CommentMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<CommentMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<CommentMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<CommentMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_comment_message);
    UIChangeObservable uc = new UIChangeObservable();

    public CommentMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    public void itemClick(int position) {
        CommentMessageEntity itemEntity = observableList.get(position).itemEntity.get();
        if (itemEntity.getRelationType() == 2) {
            //动态
            Bundle bundle = TrendDetailFragment.getStartBundle(itemEntity.getNewsId());
            start(TrendDetailFragment.class.getCanonicalName(), bundle);
        }
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageComment(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<CommentMessageEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<CommentMessageEntity> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<CommentMessageEntity> list = response.getData().getData();
                        for (CommentMessageEntity entity : list) {
                            CommentMessageItemViewModel item = new CommentMessageItemViewModel(CommentMessageViewModel.this, entity);
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
        model.deleteMessage("comment", observableList.get(position).itemEntity.get().getId())
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
