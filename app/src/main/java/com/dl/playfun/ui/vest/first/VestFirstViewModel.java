package com.dl.playfun.ui.vest.first;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.AdBannerEntity;
import com.dl.playfun.entity.AdItemEntity;
import com.dl.playfun.entity.ConfigItemEntity;
import com.dl.playfun.entity.ParkItemEntity;
import com.dl.playfun.entity.SystemConfigEntity;
import com.dl.playfun.entity.UserDataEntity;
import com.dl.playfun.event.AddBlackListEvent;
import com.dl.playfun.event.CityChangeEvent;
import com.dl.playfun.event.DailyAccostEvent;
import com.dl.playfun.event.LoadEvent;
import com.dl.playfun.event.LocationChangeEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.home.HomeMainBannerItemViewModel;
import com.dl.playfun.ui.home.HomeMainTabItemViewModel;
import com.dl.playfun.ui.home.HomeMainViewModel;
import com.dl.playfun.ui.home.search.SearchFragment;
import com.dl.playfun.ui.viewmodel.BaseParkItemViewModel;
import com.dl.playfun.ui.viewmodel.BaseParkViewModel;
import com.dl.playfun.utils.LogUtils;
import com.dl.playfun.utils.StringUtil;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
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
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<ParkItemEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<ParkItemEntity> response) {
                        super.onSuccess(response);
                        LogUtils.i("onSuccess: "+response.getData().getData().toString());
                        for (ParkItemEntity itemEntity : response.getData().getData()) {
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