package com.fine.friendlycc.ui.message.evaluatemessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.EvaluateBean;
import com.fine.friendlycc.bean.EvaluateMessageBean;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * @author wulei
 */
public class EvaluateMessageViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<MultiItemViewModel<EvaluateMessageViewModel>> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<MultiItemViewModel<EvaluateMessageViewModel>> observableList = new ObservableArrayList<>();
    public ItemBinding<MultiItemViewModel<EvaluateMessageViewModel>> itemBinding = ItemBinding.of(new OnItemBind<MultiItemViewModel<EvaluateMessageViewModel>>() {

        @Override
        public void onItemBind(ItemBinding itemBinding, int position, MultiItemViewModel<EvaluateMessageViewModel> item) {
            Integer itemType = (Integer) item.getItemType();
            if (itemType == 1) {
                //评价别人
                itemBinding.set(BR.viewModel, R.layout.item_evaluate_theme_message);
            } else if (itemType == 2) {
                //别人评价我
                itemBinding.set(BR.viewModel, R.layout.item_evaluate_message);
            }
        }
    });
    UIChangeObservable uc = new UIChangeObservable();

    public EvaluateMessageViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    public void itemClick(int position) {
        MultiItemViewModel<EvaluateMessageViewModel> itemViewModel = observableList.get(position);
        if ((Integer) itemViewModel.getItemType() == 1) {
            EvaluateThemMessageItemViewModel evaluateThemMessageItemViewModel = (EvaluateThemMessageItemViewModel) itemViewModel;
            getUserEvaluate(evaluateThemMessageItemViewModel.itemEntity.get().getUser().getId(), evaluateThemMessageItemViewModel.itemEntity.get().getUser().getSex());
        } else if ((Integer) itemViewModel.getItemType() == 2) {

        }
    }

    @Override
    public void loadDatas(int page) {
        model.getMessageEvaluate(page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<EvaluateMessageBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<EvaluateMessageBean> response) {
                        super.onSuccess(response);
                        if (page == 1) {
                            observableList.clear();
                        }
                        List<EvaluateMessageBean> list = response.getData().getData();
                        for (EvaluateMessageBean entity : list) {
                            if (entity.getRelationType() == 1) {
                                MultiItemViewModel item = new EvaluateThemMessageItemViewModel(EvaluateMessageViewModel.this, entity);
                                item.multiItemType(1);
                                observableList.add(item);
                            } else {
                                MultiItemViewModel item = new EvaluateMessageItemViewModel(EvaluateMessageViewModel.this, entity);
                                item.multiItemType(2);
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

    public void deleteMessage(int position) {
        MultiItemViewModel<EvaluateMessageViewModel> itemViewModel = observableList.get(position);
        int id = 0;
        if ((Integer) itemViewModel.getItemType() == 1) {
            EvaluateThemMessageItemViewModel evaluateThemMessageItemViewModel = (EvaluateThemMessageItemViewModel) itemViewModel;
            id = evaluateThemMessageItemViewModel.itemEntity.get().getId();
        } else if ((Integer) itemViewModel.getItemType() == 2) {
            EvaluateMessageItemViewModel evaluateMessageItemViewModel = (EvaluateMessageItemViewModel) itemViewModel;
            id = evaluateMessageItemViewModel.itemEntity.get().getId();
        }
        if (id == 0) {
            return;
        }
        model.deleteMessage("evaluate", id)
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

    public void getUserEvaluate(int userId, int sex) {
        model.evaluate(userId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<EvaluateBean>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<EvaluateBean>> response) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("sex", sex);
                        params.put("userId", userId);
                        params.put("evaluates", response.getData());
                        uc.clickEvaluate.postValue(params);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 提交评价
     *
     * @param tagId 评价标签ID
     * @param img   当tag_id值为 5/6时 为必填
     */
    public void commitUserEvaluate(int userId, int tagId, String img) {
        model.evaluateCreate(userId, tagId, img)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        startRefresh();
                        ToastUtils.showShort(R.string.playcc_submittd);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    /**
     * 提交负面评价
     *
     * @param tagId
     * @param imageSrc
     */
    public void commitNegativeEvaluate(int userId, int tagId, String imageSrc) {
        Observable.just(imageSrc)
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return FileUploadUtils.ossUploadFile("evaluate/", FileUploadUtils.FILE_TYPE_IMAGE, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        commitUserEvaluate(userId, tagId, fileKey);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void clickEvaluateAppeal(int position) {
        uc.clickAppealEvaluate.postValue(position);
    }

    public void commitEvaluateAppeal(int position) {
        EvaluateMessageItemViewModel evaluateMessageItemViewModel = (EvaluateMessageItemViewModel) observableList.get(position);
        model.evaluateAppeal(evaluateMessageItemViewModel.itemEntity.get().getId(), evaluateMessageItemViewModel.itemEntity.get().getEvaluate().getTagId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        evaluateMessageItemViewModel.itemEntity.get().setStatus(1);
                        String statusText = getStatusText(evaluateMessageItemViewModel.itemEntity.get().getStatus());
                        evaluateMessageItemViewModel.statusText.set(statusText);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public String getStatusText(int s) {
        String status = StringUtils.getString(R.string.playcc_unknown);
        //0未上诉 1已上诉等待处理 2上诉成功 3上诉失败
        if (s == 0) {
            status = "";
        } else if (s == 1) {
            status = StringUtils.getString(R.string.playcc_applealed);
        } else if (s == 2) {
            status = StringUtils.getString(R.string.playcc_appleal_success);
        } else if (s == 3) {
            status = StringUtils.getString(R.string.playcc_appleal_defail);
        }
        return status;
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Map<String, Object>> clickEvaluate = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> clickAppealEvaluate = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> clickDelete = new SingleLiveEvent<>();
    }
}