package com.fine.friendlycc.ui.home.search;

import android.app.Application;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.entity.ParkItemEntity;
import com.fine.friendlycc.manager.LocationManager;
import com.fine.friendlycc.widget.emptyview.EmptyState;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.viewmodel.BaseParkItemViewModel;
import com.fine.friendlycc.ui.viewmodel.BaseParkViewModel;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class SearchViewModel extends BaseParkViewModel<AppRepository> {

    public ObservableField<String> searchBarHint = new ObservableField<>();

    public ObservableField<String> searchText = new ObservableField<>();

    public ObservableField<TextView.OnEditorActionListener> onEditorActionListener = new ObservableField<>();

    //搭讪失败。充值钻石
    public SingleLiveEvent<Void> sendAccostFirstError = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> loadLoteAnime = new SingleLiveEvent<>();

    private String keyword;
    private final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        }
    };

    public SearchViewModel(@NonNull Application application, AppRepository appRepository) {
        super(application, appRepository);
        searchBarHint.set(model.readUserData().getSex() == 1 ? StringUtils.getString(R.string.playfun_model_please_input_explain_female) : StringUtils.getString(R.string.playfun_model_please_input_explain_male));
        onEditorActionListener.set(editorActionListener);
    }

    @Override
    public void AccostFirstSuccess(ParkItemEntity itemEntity, int position) {
        if (itemEntity == null) {//提醒充值钻石
            sendAccostFirstError.call();
        } else {
            //loadLoteAnime.postValue(position);
            //跳转到聊天界面
//            ChatUtils.chatUser(itemEntity.getId(), itemEntity.getNickname(), SearchViewModel.this);
        }
    }

    @Override
    public void loadDatas(int page) {
        searchDatas(1);
    }

    private void search() {
        String s = searchText.get();
        if (TextUtils.isEmpty(searchText.get())) {
            ToastUtils.showShort(model.readUserData().getSex() == 1 ? StringUtils.getString(R.string.playfun_model_please_input_explain_female) : StringUtils.getString(R.string.playfun_model_please_input_explain_male));
            return;
        }
        keyword = s;
        hideKeyboard();
        currentPage = 1;
        loadDatas(currentPage);
    }

    public void searchDatas(int page) {
        if (StringUtils.isEmpty(keyword)) {
            stopRefreshOrLoadMore();
            return;
        }
        model.homeList(null, null, null, model.readUserData().getSex() == 1 ? 0 : 1, keyword, LocationManager.getInstance().getLng(), LocationManager.getInstance().getLat(), page)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseListDataResponse<ParkItemEntity>>() {

                    @Override
                    public void onSuccess(BaseListDataResponse<ParkItemEntity> response) {
                        if (currentPage == 1) {
                            observableList.clear();
                            if (response.getData().getData().isEmpty()) {
                                stateModel.setEmptyState(EmptyState.EMPTY);
                            } else {
                                stateModel.setEmptyState(EmptyState.NORMAL);
                            }
                        }
                        if (page == 1 && currentPage - page >= page) {
                            return;
                        }
                        int sex = model.readUserData().getSex();
                        for (ParkItemEntity itemEntity : response.getData().getData()) {
                            BaseParkItemViewModel item = new BaseParkItemViewModel(SearchViewModel.this, sex, itemEntity);
                            if (observableList.indexOf(item) == -1) {
                                observableList.add(item);
                            }
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        stateModel.setEmptyState(EmptyState.NOT_AVAILABLE);
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                        dismissHUD();
                    }
                });
    }
}