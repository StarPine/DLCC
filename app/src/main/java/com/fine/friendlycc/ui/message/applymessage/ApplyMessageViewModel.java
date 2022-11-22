package com.fine.friendlycc.ui.message.applymessage;

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
import com.fine.friendlycc.bean.AlbumPhotoBean;
import com.fine.friendlycc.bean.ApplyMessageBean;
import com.fine.friendlycc.event.ApplyMessagePhotoStatusChangeEvent;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;
import com.fine.friendlycc.ui.userdetail.photobrowse.PhotoBrowseFragment;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class ApplyMessageViewModel extends BaseRefreshViewModel<AppRepository> {
    public BindingRecyclerViewAdapter<ApplyMessageItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<ApplyMessageItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<ApplyMessageItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_apply_message);
    UIChangeObservable uc = new UIChangeObservable();
    private Disposable mSubscription;

    public ApplyMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    public void itemClick(int position) {
        ApplyMessageBean applyMessageEntity = observableList.get(position).itemEntity.get();
        Bundle bundle = UserDetailFragment.getStartBundle(applyMessageEntity.getUser().getId());
        start(UserDetailFragment.class.getCanonicalName(), bundle);
    }

    public void itemPhotoClick(int position) {
        ApplyMessageBean applyMessageEntity = observableList.get(position).itemEntity.get();
        AlbumPhotoBean albumPhotoEntity = new AlbumPhotoBean();
        albumPhotoEntity.setId(applyMessageEntity.getApplyId());
        albumPhotoEntity.setType(1);
        albumPhotoEntity.setSrc(applyMessageEntity.getApply().getImg());
        albumPhotoEntity.setBurnStatus(applyMessageEntity.getApply().getIsView());
        albumPhotoEntity.setIsBurn(1);
        Bundle bundle = PhotoBrowseFragment.getStartApplyMessageBundle(albumPhotoEntity);
        start(PhotoBrowseFragment.class.getCanonicalName(), bundle);
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(ApplyMessagePhotoStatusChangeEvent.class)
                .subscribe(event -> {
                    for (ApplyMessageItemViewModel applyMessageItemViewModel : observableList) {
                        if (applyMessageItemViewModel.itemEntity.get().getApplyId() == event.getApplyId()) {
                            applyMessageItemViewModel.photoIsView.set(1);
                            break;
                        }
                    }
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageApply(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<ApplyMessageBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<ApplyMessageBean> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<ApplyMessageBean> list = response.getData().getData();
                        for (ApplyMessageBean entity : list) {
                            ApplyMessageItemViewModel item = new ApplyMessageItemViewModel(ApplyMessageViewModel.this, entity);
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
        model.deleteMessage("apply", observableList.get(position).itemEntity.get().getId())
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

    public void replyApply(int position, boolean apply) {
        ApplyMessageBean applyMessageEntity = observableList.get(position).itemEntity.get();
        model.replyApplyAlubm(applyMessageEntity.getApplyId(), apply)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        applyMessageEntity.getApply().setStatus(apply ? 1 : 2);
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