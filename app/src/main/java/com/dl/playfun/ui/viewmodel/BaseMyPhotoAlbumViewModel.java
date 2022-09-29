package com.dl.playfun.ui.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseListEmptyObserver;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.entity.UserInfoEntity;
import com.dl.playfun.event.PhotoCallCoverEvent;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.mine.myphotoalbum.MyPhotoAlbumItemViewModel;
import com.dl.playfun.ui.mine.photosetting.PhotoSettingFragment;
import com.dl.playfun.viewmodel.BaseRefreshViewModel;
import com.dl.playfun.BR;
import com.dl.playfun.R;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public abstract class BaseMyPhotoAlbumViewModel<T extends AppRepository> extends BaseRefreshViewModel<T> {
    public BindingRecyclerViewAdapter<MyPhotoAlbumItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public ObservableField<Integer> totalPhoto = new ObservableField<>();
    public ObservableList<MyPhotoAlbumItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<MyPhotoAlbumItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_my_photo_album);
    protected ArrayList<AlbumPhotoEntity> photoEntityList = new ArrayList<>();

    private Disposable photoCallCoverEventSub;

    public BaseMyPhotoAlbumViewModel(@NonNull Application application, T model) {
        super(application, model);
    }

    public T getRepository() {
        return model;
    }

    public void itemClick(int position) {
        //自己点击自己相册
        Bundle bundle = new Bundle();
        bundle.putInt(PhotoSettingFragment.ARG_TYPE, PhotoSettingFragment.TYPE_PHOTO_SETTING);
        bundle.putInt(PhotoSettingFragment.ARG_PHOTOS_INDEX, position);
        bundle.putParcelableArrayList(PhotoSettingFragment.ARG_PHOTOS, photoEntityList);
        start(PhotoSettingFragment.class.getCanonicalName(), bundle);
    }

    public void loadAlbumDetail(Integer showMaxCount) {
        model.albumImage(null, null)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseListDataResponse<AlbumPhotoEntity>>() {
                    @Override
                    public void onSuccess(BaseListDataResponse<AlbumPhotoEntity> response) {
                        totalPhoto.set(response.getData().getTotal());
                        observableList.clear();
                        photoEntityList.clear();
                        if (showMaxCount != null && response.getData().getData().size() > showMaxCount) {
                            for (int i = 0; i < showMaxCount; i++) {
                                AlbumPhotoEntity datum = response.getData().getData().get(i);
                                photoEntityList.add(datum);
                                MyPhotoAlbumItemViewModel itemViewModel = new MyPhotoAlbumItemViewModel(BaseMyPhotoAlbumViewModel.this, datum);
                                observableList.add(itemViewModel);
                                if (i == showMaxCount - 1) {
                                    itemViewModel.moreCount.set(response.getData().getData().size() - showMaxCount);
                                }
                            }
                        } else {
                            for (AlbumPhotoEntity datum : response.getData().getData()) {
                                photoEntityList.add(datum);
                                MyPhotoAlbumItemViewModel itemViewModel = new MyPhotoAlbumItemViewModel(BaseMyPhotoAlbumViewModel.this, datum);
                                observableList.add(itemViewModel);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        stopRefreshOrLoadMore();
                    }
                });
    }

    public void loadAlbumDetailShowEmpty() {
        model.albumImage(null, null)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseListEmptyObserver<BaseListDataResponse<AlbumPhotoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseListDataResponse<AlbumPhotoEntity> response) {
                        super.onSuccess(response);
                        totalPhoto.set(response.getData().getTotal());
                        observableList.clear();
                        photoEntityList.clear();
                        for (AlbumPhotoEntity datum : response.getData().getData()) {
                            photoEntityList.add(datum);
                            MyPhotoAlbumItemViewModel itemViewModel = new MyPhotoAlbumItemViewModel(BaseMyPhotoAlbumViewModel.this, datum);
                            observableList.add(itemViewModel);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        stopRefreshOrLoadMore();
                    }
                });
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        photoCallCoverEventSub = RxBus.getDefault().toObservable(PhotoCallCoverEvent.class)
                .compose(RxUtils.schedulersTransformer())
                .subscribe(photoCallCoverEventSub -> {
                    PhotoCallCoverEvent itemEntity = ((PhotoCallCoverEvent)photoCallCoverEventSub);
                    if(observableList!=null && itemEntity!=null && itemEntity.getAlbumPhotoEntity()!=null){
                        int albumId = itemEntity.getAlbumPhotoEntity().getId();
                        for (int j = 0; j < observableList.size(); j++) {
                            AlbumPhotoEntity albumPhotoEntity = observableList.get(j).itemEntity.get();
                            if(albumPhotoEntity!=null){
                                //不为null  并且是本人
                                if(albumPhotoEntity.getVerificationType()==1 && !ConfigManager.getInstance().isMale()){
                                    if(albumId == albumPhotoEntity.getId()){
                                        observableList.get(j).itemEntity.get().setIsCallCover(1);
                                    }else{
                                        observableList.get(j).itemEntity.get().setIsCallCover(0);
                                    }
                                }else{
                                    observableList.get(j).itemEntity.get().setIsCallCover(0);
                                }
                            }
                        }
                    }
                });
        RxSubscriptions.add(photoCallCoverEventSub);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(photoCallCoverEventSub);
    }


}
