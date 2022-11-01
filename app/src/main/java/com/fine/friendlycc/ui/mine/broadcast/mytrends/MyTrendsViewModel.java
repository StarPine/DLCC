package com.fine.friendlycc.ui.mine.broadcast.mytrends;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseDisposableObserver;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.NewsEntity;
import com.fine.friendlycc.event.BadioEvent;
import com.fine.friendlycc.event.RadioadetailEvent;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.radio.issuanceprogram.IssuanceProgramFragment;
import com.fine.friendlycc.widget.emptyview.EmptyState;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MyTrendsViewModel extends BaseRefreshViewModel<AppRepository> {
    public boolean isVip;
    public int sex;
    public int userId;
    public String avatar;
    public BindingRecyclerViewAdapter<TrendItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<TrendItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<TrendItemViewModel> itemBinding = ItemBinding.of((itemBinding, position, item) -> itemBinding.set(BR.viewModel, R.layout.item_trend));
    UIChangeObservable uc = new UIChangeObservable();
    private Disposable badioEvent;
    private Disposable radioadetailEvent;

    public MyTrendsViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
        isVip = model.readUserData().getIsVip() == 1;
        sex = model.readUserData().getSex();
        userId = model.readUserData().getId();
        avatar = model.readUserData().getAvatar();

        //stateModel.setEmptyRetryCommand(StringUtils.getString(R.string.my_trends_no_issuance), StringUtils.getString(R.string.my_trends_issuance), issuanceCommand);
        stateModel.setEmptyRetryCommand(StringUtils.getString(R.string.playfun_my_trends_no_issuance), null, null);
    }

    //跳转发布界面
    public BindingCommand toProgramVIew = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            start(IssuanceProgramFragment.class.getCanonicalName());
        }
    });

    @Override
    public void loadDatas(int page) {
        getNewsList();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        startRefresh();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        badioEvent = RxBus.getDefault().toObservable(BadioEvent.class)
                .subscribe(event -> {
                    currentPage = 1;
                    getNewsList();
                });
        radioadetailEvent = RxBus.getDefault().toObservable(RadioadetailEvent.class)
                .subscribe(event -> {
                    for (int i = 0; i < observableList.size(); i++) {
                        if (observableList.get(i).newsEntityObservableField.get().getId() == event.getId()) {
                            switch (event.getType()) {//1:删除 2：评论关闭开启 3：报名成功 4：节目结束报名 5：评论  6：点赞
                                case 1:
                                    observableList.remove(i);
                                    break;
                                case 2:
                                    observableList.get(i).newsEntityObservableField.get().getBroadcast().setIsComment(event.isComment);
                                    break;
                                case 5:
                                    observableList.get(i).addComment(event.getId(), event.content, event.toUserId, event.toUserName, model.readUserData().getNickname());
                                    break;
                                case 6:
                                    observableList.get(i).addGiveUser();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(badioEvent);
        RxSubscriptions.remove(radioadetailEvent);
    }

    //获动态列表
    private void getNewsList() {
        model.getNewsList(null, currentPage)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<NewsEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<NewsEntity> response) {
                        super.onSuccess(response);
                        stateModel.setEmptyBroadcastCommand(StringUtils.getString(R.string.playfun_my_all_broadcast_empty), R.drawable.my_all_broadcast_empty_img, R.color.all_broadcast_empty,StringUtils.getString(R.string.playfun_task_fragment_task_new10),toProgramVIew);
                        if (response.getData().getData() != null) {
                            if (currentPage == 1) {
                                observableList.clear();
                            }
                            for (int i = 0; i < response.getData().getData().size(); i++) {
                                TrendItemViewModel item = new TrendItemViewModel(MyTrendsViewModel.this,
                                        response.getData().getData().get(i));
                                observableList.add(item);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

    //删除动态
    public void deleteNews(int posion) {
        model.deleteNews(observableList.get(posion).newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        observableList.remove(posion);
                        if(observableList.size()<=0){
                            stateModel.setEmptyBroadcastCommand(StringUtils.getString(R.string.playfun_my_all_broadcast_empty), R.drawable.my_all_broadcast_empty_img, R.color.all_broadcast_empty,StringUtils.getString(R.string.playfun_task_fragment_task_new10),toProgramVIew);
                            stateModel.setEmptyState(EmptyState.EMPTY);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //开启/关闭评论
    public void setComment(int posion) {
        model.setComment(observableList.get(posion).newsEntityObservableField.get().getBroadcast().getId(),
                observableList.get(posion).newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(observableList.get(posion).newsEntityObservableField.get().getBroadcast().getIsComment() == 1 ? StringUtils.getString(R.string.playfun_open_comment_success) : StringUtils.getString(R.string.playfun_close_success));
                        observableList.get(posion).newsEntityObservableField.get().getBroadcast().setIsComment(
                                observableList.get(posion).newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //动态点赞
    public void newsGive(int posion) {
        model.newsGive(observableList.get(posion).newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playfun_give_success);
                        observableList.get(posion).addGiveUser();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //动态评论
    public void newsComment(Integer id, String content, Integer toUserId, String toUserName) {
        model.newsComment(id, content, toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseDisposableObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ToastUtils.showShort(R.string.playfun_comment_success);
                        for (int i = 0; i < observableList.size(); i++) {
                            if (id == observableList.get(i).newsEntityObservableField.get().getId()) {
                                observableList.get(i).addComment(id, content, toUserId, toUserName, model.readUserData().getNickname());
                            }
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 10016) {
                            ToastUtils.showShort(StringUtils.getString(R.string.playfun_comment_close));
                            for (int i = 0; i < observableList.size(); i++) {
                                if (id == observableList.get(i).newsEntityObservableField.get().getId()) {
                                    observableList.get(i).newsEntityObservableField.get().getBroadcast().setIsComment(1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent clickMore = new SingleLiveEvent<>();
        public SingleLiveEvent clickLike = new SingleLiveEvent<>();
        public SingleLiveEvent clickComment = new SingleLiveEvent<>();
        public SingleLiveEvent clickImage = new SingleLiveEvent<>();
    }
}
