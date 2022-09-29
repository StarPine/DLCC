package com.dl.playfun.ui.userdetail.userdynamic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.NewsEntity;
import com.dl.playfun.event.RadioadetailEvent;
import com.dl.playfun.ui.mine.broadcast.mytrends.TrendItemViewModel;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;
import com.dl.playfun.BR;
import com.dl.playfun.R;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * 用户动态
 *
 * @author wulei
 */
public class UserDynamicViewModel extends BaseRefreshViewModel<AppRepository> {
    public ObservableField<Integer> id = new ObservableField<>();
    public boolean isVip;
    public int sex;
    public int userId;
    public ObservableField<String> titleText = new ObservableField<>();
    public UIChangeObservable uc = new UIChangeObservable();
    public ObservableList<TrendItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<TrendItemViewModel> itemBinding = ItemBinding.of(new OnItemBind<TrendItemViewModel>() {

        @Override
        public void onItemBind(ItemBinding itemBinding, int position, TrendItemViewModel item) {
            itemBinding.set(BR.viewModel, R.layout.item_trend);
        }
    });
    private Disposable radioadetailEvent;

    public UserDynamicViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        isVip = model.readUserData().getIsVip() == 1;
        sex = model.readUserData().getSex();
        userId = model.readUserData().getId();
    }

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
        RxSubscriptions.remove(radioadetailEvent);
    }

    //获动态列表
    private void getNewsList() {
        model.getNewsList(id.get(), currentPage)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseListDataResponse<NewsEntity>>() {
                    @Override
                    public void onSuccess(BaseListDataResponse<NewsEntity> response) {
                        if (response.getData().getData() != null) {
                            if (currentPage == 1) {
                                observableList.clear();
                            }
                            for (int i = 0; i < response.getData().getData().size(); i++) {
                                TrendItemViewModel item = new TrendItemViewModel(UserDynamicViewModel.this,
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

    //动态点赞
    public void newsGive(int posion) {
        model.newsGive(observableList.get(posion).newsEntityObservableField.get().getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
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
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
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