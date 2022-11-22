package com.fine.friendlycc.ui.vest.first;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.bean.ParkItemBean;
import com.fine.friendlycc.utils.LogUtils;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class VestFirstViewModel extends BaseRefreshViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<VestFirstTabItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<VestFirstTabItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<VestFirstTabItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_vest_first_tab);
    public int type = 1;
    public int sex = 1;

    public VestFirstViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);

    }

    @Override
    public void loadDatas(int page) {
        if (page == 1 && observableList.size()>0) {
            observableList.clear();
        }
        model.homeList(null,
                type,
                1,
                sex,
                null,
                0d,
                0d,
                page
        )
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<ParkItemBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<ParkItemBean> response) {
                        super.onSuccess(response);
                        LogUtils.i("onSuccess: "+response.getData().getData().toString());
                        for (ParkItemBean itemEntity : response.getData().getData()) {
                            observableList.add(new VestFirstTabItemViewModel(VestFirstViewModel.this,itemEntity));
                        }
                    }

                    @Override
                    public void onComplete() {
                        stopRefreshOrLoadMore();
                    }
                });
    }

}