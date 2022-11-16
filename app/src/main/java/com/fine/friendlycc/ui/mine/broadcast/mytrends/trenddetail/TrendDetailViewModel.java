package com.fine.friendlycc.ui.mine.broadcast.mytrends.trenddetail;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseDisposableObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.CommentEntity;
import com.fine.friendlycc.entity.GiveUserBeanEntity;
import com.fine.friendlycc.entity.NewsEntity;
import com.fine.friendlycc.event.RadioadetailEvent;
import com.fine.friendlycc.event.ZoomInPictureEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.CommentItemViewModel;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.HeadItemViewModel;
import com.fine.friendlycc.ui.mine.broadcast.mytrends.ImageItemViewModel;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.userdetail.detail.UserDetailFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.fine.friendlycc.ui.radio.radiohome.RadioViewModel.RadioRecycleType_New;

public class TrendDetailViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<NewsEntity> newsEntityObservableField = new ObservableField<>();
    public int userId;
    public String avatar;
    public int sex;
    public ObservableField<Boolean> isDetele = new ObservableField<>(false);
    public ObservableField<Boolean> isSelf = new ObservableField<>(false);
    public ObservableField<Boolean> isShowComment = new ObservableField<>(false);
    public ObservableField<Boolean> isRealManVisible = new ObservableField<>(false);
    public ObservableField<String> gameUrl = new ObservableField<>("");
    //    public ObservableField<String> positonStr = new ObservableField<>();
    public ObservableField<Integer> pointPositon = new ObservableField<>(0);
    public UIChangeObservable uc = new UIChangeObservable();
    public ObservableList<HeadItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<HeadItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_head);
    public ObservableList<ImageItemViewModel> imageItemList = new ObservableArrayList<>();
    public ItemBinding<ImageItemViewModel> imageItemBinding = ItemBinding.of(BR.viewModel, R.layout.item_image);
    public ObservableList<CommentItemViewModel> commentItemList = new ObservableArrayList<>();
    public ItemBinding<CommentItemViewModel> commentItemBinding = ItemBinding.of(BR.viewModel, R.layout.item_comment);

    //点击图片
    public BindingCommand imageClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            try {
                //放大图片
                if (!ListUtils.isEmpty(newsEntityObservableField.get().getImages())) {
                    RxBus.getDefault().post(new ZoomInPictureEvent(newsEntityObservableField.get().getImages().get(0)));
                } else {
                    RxBus.getDefault().post(new ZoomInPictureEvent(newsEntityObservableField.get().getUser().getAvatar()));
                }
            }catch (Exception ignored) {

            }
        }
    });
    //更多的点击事件
    public BindingCommand moreClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clickMore.call();
        }
    });
    //点赞点击事件
    public BindingCommand likeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (newsEntityObservableField.get().getIsGive() == 0) {
                AppContext.instance().logEvent(AppsFlyerEvent.Like_2);
                newsGive();
            } else {
                ToastUtils.showShort(R.string.playcc_already);
            }
        }
    });
    //评论点击事件
    public BindingCommand commentClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (newsEntityObservableField.get() == null || newsEntityObservableField.get().getBroadcast() == null) {
                return;
            }
            if (newsEntityObservableField.get().getBroadcast().getIsComment() == 1) {
                ToastUtils.showShort(R.string.playcc_comment_close);
                return;
            }
            if (userId == newsEntityObservableField.get().getUser().getId()) {
                ToastUtils.showShort(R.string.playcc_self_ont_comment_broadcast);
            } else {
                AppContext.instance().logEvent(AppsFlyerEvent.Message_2);
                Map<String, String> data = new HashMap<>();
                data.put("id", String.valueOf(newsEntityObservableField.get().getId()));
                data.put("toUseriD", null);
                uc.clickComment.setValue(data);
            }
        }
    });
    //头像的点击事件
    public BindingCommand avatarClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (String.valueOf(newsEntityObservableField.get().getUser().getId()).equals(ConfigManager.getInstance().getUserId())) {
                return;
            }
            AppContext.instance().logEvent(AppsFlyerEvent.User_Page_1);
            Bundle bundle = UserDetailFragment.getStartBundle(newsEntityObservableField.get().getUser().getId());
            start(UserDetailFragment.class.getCanonicalName(), bundle);
        }
    });

    public TrendDetailViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
        userId = model.readUserData().getId();
        isDetele.set(false);
        avatar = model.readUserData().getAvatar();
        sex = model.readUserData().getSex();
    }

    public void setGameUrl(){
        if (ObjectUtils.isEmpty(newsEntityObservableField.get().getGamechannel())){
            return ;
        }
        gameUrl.set(ConfigManager.getInstance().getGameUrl(newsEntityObservableField.get().getGamechannel()));
    }

    public void isRealMan() {
        if (ObjectUtils.isEmpty(newsEntityObservableField.get().getUser())){
            isRealManVisible.set(false);
            return;
        }
        if (newsEntityObservableField.get().getUser().getIsVip() != 1) {
            if (newsEntityObservableField.get().getUser().getSex() == 1 && newsEntityObservableField.get().getUser().getCertification() == 1) {
                isRealManVisible.set(true);
            } else {
                isRealManVisible.set(false);
            }
        }else {
            isRealManVisible.set(false);
        }
    }

    private void init() {
        commentItemList.clear();
        itemList.clear();
        if (newsEntityObservableField.get().getGive_user() != null) {
            for (int i = 0; i < newsEntityObservableField.get().getGive_user().size(); i++) {
                if (i < 13) {
                    HeadItemViewModel item = new HeadItemViewModel(this, newsEntityObservableField.get().getGive_user().get(i).getAvatar(),
                            newsEntityObservableField.get().getGive_user().get(i).getId(),
                            newsEntityObservableField.get().getGive_user().get(i).getSex(),
                            0,
                            HeadItemViewModel.Type_New, newsEntityObservableField.get().getId()
                    );
                    itemList.add(item);
                } else if (i == 13) {
                    HeadItemViewModel item = new HeadItemViewModel(this, newsEntityObservableField.get().getGive_user().get(i).getAvatar(),
                            newsEntityObservableField.get().getGive_user().get(i).getId(),
                            newsEntityObservableField.get().getGive_user().get(i).getSex(),
                            newsEntityObservableField.get().getGiveCount() - 14,
                            HeadItemViewModel.Type_New, newsEntityObservableField.get().getId()
                    );
                    itemList.add(item);
                }
            }
        }

        if (newsEntityObservableField.get().getComment() != null) {
            for (int i = 0; i < newsEntityObservableField.get().getComment().size(); i++) {
                if (i < 5 || ApiUitl.isShow) {
                    CommentItemViewModel commentItemViewModel = new CommentItemViewModel(this, newsEntityObservableField.get().getComment().get(i),
                            newsEntityObservableField.get().getId(), RadioRecycleType_New, newsEntityObservableField.get().getUser().getId() == userId, false);
                    commentItemList.add(commentItemViewModel);
                } else {
                    CommentItemViewModel commentItemViewModel = new CommentItemViewModel(this, newsEntityObservableField.get().getComment().get(i),
                            newsEntityObservableField.get().getId(), RadioRecycleType_New, newsEntityObservableField.get().getUser().getId() == userId, true);
                    commentItemList.add(commentItemViewModel);
                    break;
                }

            }
        }

        if (ListUtils.isEmpty(newsEntityObservableField.get().getComment())) {
            isShowComment.set(false);
        } else {
            isShowComment.set(true);
        }

    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        newsDetail();
    }

    public void newsDetail() {
        model.newsDetail(newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseDisposableObserver<BaseDataResponse<NewsEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<NewsEntity> response) {
                        newsEntityObservableField.set(response.getData());
                        isDetele.set(false);
                        if (response.isSuccess() && response.getData() != null && response.getData().getUser().getId() == userId) {
                            isSelf.set(true);
                        }
                        init();
                        isRealMan();
                        setGameUrl();
//                        uc.clickIisDelete.call();
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 10013) {
                            isDetele.set(true);
//                            uc.clickIisDelete.call();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });

    }

    //删除动态
    public void deleteNews() {
        model.deleteNews(newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        RadioadetailEvent radioadetailEvent = new RadioadetailEvent();
                        radioadetailEvent.setId(newsEntityObservableField.get().getId());
                        radioadetailEvent.setRadioaType(RadioRecycleType_New);
                        radioadetailEvent.setType(1);
                        RxBus.getDefault().post(radioadetailEvent);
                        pop();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //开启/关闭评论
    public void setComment() {
        model.setComment(newsEntityObservableField.get().getBroadcast().getId(),
                newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(newsEntityObservableField.get().getBroadcast().getIsComment() == 1 ? StringUtils.getString(R.string.playcc_open_comment_success) : StringUtils.getString(R.string.playcc_close_success));
                        newsEntityObservableField.get().getBroadcast().setIsComment(
                                newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0);
                        RadioadetailEvent radioadetailEvent = new RadioadetailEvent();
                        radioadetailEvent.setId(newsEntityObservableField.get().getId());
                        radioadetailEvent.setRadioaType(RadioRecycleType_New);
                        radioadetailEvent.setType(2);
                        radioadetailEvent.setIsComment(newsEntityObservableField.get().getBroadcast().getIsComment() == 0 ? 1 : 0);
                        RxBus.getDefault().post(radioadetailEvent);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //动态点赞
    public void newsGive() {
        model.newsGive(newsEntityObservableField.get().getId())
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_give_success);
                        if (newsEntityObservableField.get().getGive_user() == null) {
                            newsEntityObservableField.get().setGive_user(new ArrayList<>());
                        }
                        GiveUserBeanEntity giveUserBeanEntity = new GiveUserBeanEntity(userId, avatar);
                        newsEntityObservableField.get().getGive_user().add(giveUserBeanEntity);
                        newsEntityObservableField.get().setGiveSize(newsEntityObservableField.get().getGive_user().size());
                        newsEntityObservableField.get().setIsGive(1);
                        newsEntityObservableField.get().getBroadcast().setGiveCount(newsEntityObservableField.get().getBroadcast().getGiveCount() + 1);
                        if (newsEntityObservableField.get().getGiveCount() < 13) {
                            HeadItemViewModel item = new HeadItemViewModel(TrendDetailViewModel.this, avatar, userId,
                                    sex,
                                    0,
                                    HeadItemViewModel.Type_New, newsEntityObservableField.get().getId()
                            );
                            itemList.add(item);
                        }
                        RadioadetailEvent radioadetailEvent = new RadioadetailEvent();
                        radioadetailEvent.setId(newsEntityObservableField.get().getId());
                        radioadetailEvent.setRadioaType(RadioRecycleType_New);
                        radioadetailEvent.setType(6);
                        RxBus.getDefault().post(radioadetailEvent);
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
                        ToastUtils.showShort(R.string.playcc_comment_success);
//                        newsDetail();
                        if (newsEntityObservableField.get().getComment() == null) {
                            newsEntityObservableField.get().setComment(new ArrayList<>());
                        }
                        CommentEntity commentEntity = new CommentEntity();
                        commentEntity.setContent(content);
                        commentEntity.setId(id);
                        commentEntity.setUserId(userId);
                        CommentEntity.UserBean userBean = new CommentEntity.UserBean();
                        userBean.setId(userId);
                        userBean.setNickname(model.readUserData().getNickname());
                        commentEntity.setUser(userBean);
                        if (toUserName != null) {
                            CommentEntity.TouserBean touserBean = new CommentEntity.TouserBean();
                            touserBean.setId(toUserId);
                            touserBean.setNickname(toUserName);
                            commentEntity.setTouser(touserBean);
                        }
                        newsEntityObservableField.get().getComment().add(commentEntity);
                        CommentItemViewModel commentItemViewModel = new CommentItemViewModel(TrendDetailViewModel.this, commentEntity, newsEntityObservableField.get().getId(),RadioRecycleType_New, newsEntityObservableField.get().getUser().getId() == userId, false);
                        commentItemList.add(commentItemViewModel);

                        if (ListUtils.isEmpty(newsEntityObservableField.get().getComment())) {
                            isShowComment.set(false);
                        } else {
                            isShowComment.set(true);
                        }

                        RadioadetailEvent radioadetailEvent = new RadioadetailEvent();
                        radioadetailEvent.setId(id);
                        radioadetailEvent.setRadioaType(RadioRecycleType_New);
                        radioadetailEvent.setType(5);
                        radioadetailEvent.setContent(content);
                        radioadetailEvent.setToUserId(toUserId);
                        radioadetailEvent.setToUserName(toUserName);
                        RxBus.getDefault().post(radioadetailEvent);
                    }

                    @Override
                    public void onError(RequestException e) {
                        if (e.getCode() == 10016) {
                            ToastUtils.showShort(StringUtils.getString(R.string.playcc_comment_close));
                            newsEntityObservableField.get().getBroadcast().setIsComment(1);
                            RadioadetailEvent radioadetailEvent = new RadioadetailEvent();
                            radioadetailEvent.setId(id);
                            radioadetailEvent.setRadioaType(RadioRecycleType_New);
                            radioadetailEvent.setType(2);
                            radioadetailEvent.setIsComment(1);
                            RxBus.getDefault().post(radioadetailEvent);
                        } else {
                            ToastUtils.showShort(e.getMessage() == null ? "" : e.getMessage());
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
        public SingleLiveEvent clickComment = new SingleLiveEvent<>();
        public SingleLiveEvent clickImage = new SingleLiveEvent<>();
        //        public SingleLiveEvent clickIisDelete = new SingleLiveEvent<>();
    }
}