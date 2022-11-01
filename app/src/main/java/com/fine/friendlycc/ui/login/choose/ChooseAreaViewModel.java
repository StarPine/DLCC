package com.fine.friendlycc.ui.login.choose;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.ChooseAreaEntity;
import com.fine.friendlycc.entity.ChooseAreaItemEntity;
import com.fine.friendlycc.event.ItemChooseAreaEvent;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Author: 彭石林
 * Time: 2022/7/5 17:35
 * Description: This is ChooseAreaViewModel
 */
public class ChooseAreaViewModel extends BaseViewModel<AppRepository> {

    //广告列表
    public BindingRecyclerViewAdapter<ItemChooseAreaViewModel> areaAdapter = new BindingRecyclerViewAdapter<>();
    public ObservableList<ItemChooseAreaViewModel> areaObservableList = new ObservableArrayList<>();
    public ItemBinding<ItemChooseAreaViewModel> areaItemBinding = ItemBinding.of(BR.viewModel, R.layout.item_area_check);

    public ChooseAreaViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public void chooseAreaClick(ChooseAreaItemEntity chooseAreaItemEntity) {
        RxBus.getDefault().post(new ItemChooseAreaEvent(chooseAreaItemEntity));
        pop();
    }

    public void getChooseAreaList() {
        model.getChooseAreaList()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<ChooseAreaEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<ChooseAreaEntity> response) {
                        ChooseAreaEntity chooseAreaEntity = response.getData();
                        if (chooseAreaEntity != null && ObjectUtils.isNotEmpty(chooseAreaEntity.getList())) {
                            for (ChooseAreaItemEntity chooseArea : chooseAreaEntity.getList()) {
                                ItemChooseAreaViewModel itemChooseAreaViewModel = new ItemChooseAreaViewModel(ChooseAreaViewModel.this, chooseArea);
                                areaObservableList.add(itemChooseAreaViewModel);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
}
