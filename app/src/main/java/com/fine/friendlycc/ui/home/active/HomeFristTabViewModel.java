package com.fine.friendlycc.ui.home.active;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.ParkItemBean;
import com.fine.friendlycc.event.CityChangeEvent;
import com.fine.friendlycc.event.GenderToggleEvent;
import com.fine.friendlycc.ui.viewmodel.BaseParkItemViewModel;
import com.fine.friendlycc.ui.viewmodel.BaseParkViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;


/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/4 11:05
 */
public class HomeFristTabViewModel extends BaseParkViewModel<AppRepository> {

    public ObservableField<Integer> cityId = new ObservableField<>();
    public ObservableField<Boolean> gender = new ObservableField<>();//false:女 ，true: 男
    public ObservableField<Boolean> online = new ObservableField<>(true);
    public ObservableField<Double> lat = new ObservableField<>();//纬度
    public ObservableField<Double> lng = new ObservableField<>();//经度
    public List<ConfigItemBean> list_chooseCityItem = new ArrayList<>();
    public ObservableField<Integer> type = new ObservableField<>(1);//男女-选项卡内容
    public int index;

    private Disposable mCitySubscription;
    private Disposable mGenderSubscription;

    public HomeFristTabViewModel(@NonNull @NotNull Application application, AppRepository repository) {
        super(application, repository);
        try {
            gender.set(repository.readUserData().getSex() != 1);
        } catch (Exception e) {
            gender.set(true);
        }
    }

    @Override
    public void AccostFirstSuccess(ParkItemBean itemEntity, int position) {

    }

    @Override
    public void loadDatas(int page) {

        setType();
        if (page == 1 && observableList.size() > 0) {
            observableList.clear();
        }
        model.homeList(cityId.get(),
                type.get(),
                online.get() ? 1 : 0,
                gender.get() ? 1 : 0,
                null,
                lng.get(),
                lat.get(),
                page
        )
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<ParkItemBean>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<ParkItemBean> response) {
                        super.onSuccess(response);
                        int sex = model.readUserData().getSex();
                        for (ParkItemBean itemEntity : response.getData().getData()) {
                            Integer itemType = itemEntity.getType();
                            if (itemType != null) {
                                BaseParkItemViewModel item;
                                if (itemType == 1) {
                                    item = new BaseParkItemViewModel(HomeFristTabViewModel.this, sex, itemEntity);
                                    item.multiItemType(ItemPark);
                                } else {
                                    item = new BaseParkItemViewModel(HomeFristTabViewModel.this, itemEntity.getBannerList());
                                    item.multiItemType(ItemParkBanner);
                                }
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

    private void setType() {
        if (index == 0){
            if (gender.get()){
                type.set(5);
            }else {
                type.set(3);
            }
        }else if (index == 1){
            if (gender.get()){
                type.set(4);
            }else {
                type.set(2);
            }
        }
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mCitySubscription = RxBus.getDefault().toObservable(CityChangeEvent.class)
                .subscribe(cityChangeEvent -> {
                    ConfigItemBean cityEntity = cityChangeEvent.getCityEntity();
                    if (cityEntity != null) {
                        if (cityEntity.getId() != null && cityEntity.getId() == -1) {
                            cityId.set(null);
                        } else {
                            cityId.set(cityEntity.getId());
                        }
                    }
                    startRefresh();
                });
        mGenderSubscription = RxBus.getDefault().toObservable(GenderToggleEvent.class)
                .subscribe(event -> {
                    if (event.isMale() != null){
                        gender.set(event.isMale());
                    }
                    startRefresh();
                });

        RxSubscriptions.add(mCitySubscription);
        RxSubscriptions.add(mGenderSubscription);

    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mCitySubscription);
        RxSubscriptions.remove(mGenderSubscription);
    }
}