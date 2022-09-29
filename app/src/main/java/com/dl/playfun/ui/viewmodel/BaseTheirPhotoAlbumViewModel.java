package com.dl.playfun.ui.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.ui.userdetail.photobrowse.PhotoBrowseFragment;
import com.dl.playfun.ui.userdetail.theirphotoalbum.TheirPhotoAlbumItemViewModel;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author wulei
 */
public class BaseTheirPhotoAlbumViewModel<T extends AppRepository> extends BaseViewModel<T> {
    public ObservableField<Integer> userId = new ObservableField<>();

    public BindingRecyclerViewAdapter<TheirPhotoAlbumItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public ObservableList<TheirPhotoAlbumItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<TheirPhotoAlbumItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_their_photo_album);
    protected ArrayList<AlbumPhotoEntity> photoEntityList = new ArrayList<>();

    public BaseTheirPhotoAlbumViewModel(@NonNull Application application, T model) {
        super(application, model);
    }

    public void itemClick(int position) {
        Bundle bundle = PhotoBrowseFragment.getStartBundle(position, photoEntityList);
        start(PhotoBrowseFragment.class.getCanonicalName(), bundle);
    }

    public void showAlbum2(List<AlbumPhotoEntity> photos, int maxCount) {
        if (photos == null) {
            return;
        }
        if (photos.isEmpty()) {
            return;
        }
        observableList.clear();
        photoEntityList = (ArrayList<AlbumPhotoEntity>) photos;
        for (int i = 0; i < photos.size(); i++) {
            AlbumPhotoEntity datum = photos.get(i);
            TheirPhotoAlbumItemViewModel itemViewModel = new TheirPhotoAlbumItemViewModel(this, datum);
            observableList.add(itemViewModel);
            if (i == 7) {
                itemViewModel.moreCount.set(maxCount - 8);
            }
        }
    }

    public void showAlbum(List<AlbumPhotoEntity> photos, Integer showMaxCount) {
        if (photos == null) {
            return;
        }
        if (photos.isEmpty()) {
            return;
        }
        observableList.clear();
        photoEntityList = (ArrayList<AlbumPhotoEntity>) photos;
        if (showMaxCount != null) {
            if (photos.size() < showMaxCount) {
                showMaxCount = photos.size();
            }
            for (int i = 0; i < showMaxCount; i++) {
                AlbumPhotoEntity datum = photos.get(i);
                TheirPhotoAlbumItemViewModel itemViewModel = new TheirPhotoAlbumItemViewModel(this, datum);
                observableList.add(itemViewModel);
                if (i == showMaxCount - 1) {
                    itemViewModel.moreCount.set(photos.size() - showMaxCount);
                }
            }
        }
    }

    public void loadAlbumDetail(Integer userId, Integer showMaxCount) {
        model.albumImage(userId, null)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseListDataResponse<AlbumPhotoEntity>>() {
                    @Override
                    public void onSuccess(BaseListDataResponse<AlbumPhotoEntity> response) {
                        observableList.clear();
                        photoEntityList.clear();
                        if (showMaxCount != null && response.getData().getData().size() > showMaxCount) {
                            for (int i = 0; i < showMaxCount; i++) {
                                AlbumPhotoEntity datum = response.getData().getData().get(i);
                                photoEntityList.add(datum);
                                TheirPhotoAlbumItemViewModel itemViewModel = new TheirPhotoAlbumItemViewModel(BaseTheirPhotoAlbumViewModel.this, datum);
                                observableList.add(itemViewModel);
                                if (i == showMaxCount - 1) {
                                    itemViewModel.moreCount.set(response.getData().getData().size() - showMaxCount);
                                }
                            }
                        } else {
                            for (AlbumPhotoEntity datum : response.getData().getData()) {
                                photoEntityList.add(datum);
                                TheirPhotoAlbumItemViewModel itemViewModel = new TheirPhotoAlbumItemViewModel(BaseTheirPhotoAlbumViewModel.this, datum);
                                observableList.add(itemViewModel);
                            }
                        }
                    }
                });
    }
}
