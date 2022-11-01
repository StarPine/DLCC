package com.fine.friendlycc.ui.mine.setredpackagevideo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseListEmptyObserver;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.event.MyPhotoAlbumChangeEvent;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 设置红包照片
 *
 * @author wulei
 */
public class SetRedPackageVideoViewModel extends BaseViewModel<AppRepository> {

    public BindingRecyclerViewAdapter<SetRedPackageVideoItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public ItemBinding<SetRedPackageVideoItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_set_red_package_video);
    public ObservableList<SetRedPackageVideoItemViewModel> items = new ObservableArrayList<>();

    public ObservableField<Integer> redPackagePrice = new ObservableField<>(0);

    private Integer id;
    private boolean state;
    public BindingCommand confirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            setRedPackageImage();
        }
    });

    public SetRedPackageVideoViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        redPackagePrice.set(model.readSystemConfig().getVideoRedPackageMoney());
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadAlbumVideo();
    }

    public void loadAlbumVideo() {
        model.albumImage(null, 2)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<AlbumPhotoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<AlbumPhotoEntity> response) {
                        super.onSuccess(response);
                        items.clear();
                        for (AlbumPhotoEntity datum : response.getData().getData()) {
                            items.add(new SetRedPackageVideoItemViewModel(SetRedPackageVideoViewModel.this, datum));
                        }
                    }
                });
    }

    public void itemClick(int position) {
        AlbumPhotoEntity entity = items.get(position).itemEntity.get();
        if (entity.getVerificationType() != 1) {
            return;
        }
        for (SetRedPackageVideoItemViewModel item : items) {
            if (item.itemEntity.get() == entity) {
                item.itemEntity.get().setIsRedPackage(entity.getIsRedPackage() == 1 ? 0 : 1);
            } else {
                item.itemEntity.get().setIsRedPackage(0);
            }
        }
        if (entity.getIsRedPackage() == 1) {
            id = entity.getId();
        } else {
            id = null;
        }
        state = entity.getIsRedPackage() == 1;
    }

    public void setRedPackageImage() {
        model.setRedPackageAlbumImage(id, state)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (state) {
                            RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genSetRedPackageEvent(id));
                        } else {
                            RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genCancelRedPackageEvent(id));
                        }
                        ToastUtils.showShort(R.string.playfun_hint_set_redpackage_video_success);
                        pop();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

}