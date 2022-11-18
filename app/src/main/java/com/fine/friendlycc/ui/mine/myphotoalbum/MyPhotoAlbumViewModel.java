package com.fine.friendlycc.ui.mine.myphotoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.event.MyPhotoAlbumChangeEvent;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.viewmodel.BaseMyPhotoAlbumViewModel;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 相册
 *
 * @author wulei
 */
public class MyPhotoAlbumViewModel extends BaseMyPhotoAlbumViewModel<AppRepository> {

    public ObservableField<String> titleBtnText = new ObservableField<>();
    UIChangeObservable uc = new UIChangeObservable();
    //完成按钮的点击事件
    public BindingCommand uploadPhotoOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (totalPhoto.get() == null)return;
            if (totalPhoto.get() >= 24) {
                ToastUtils.showShort(R.string.playcc_warn_max_upload_photo);
                return;
            }
            uc.clickUploadPhoto.postValue(model.readUserData().getSex());
        }
    });
    private Disposable mPhotoUploadSubscription;

    public MyPhotoAlbumViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        if (model.readUserData().getSex() == 1) {
            titleBtnText.set(StringUtils.getString(R.string.playcc_photo_album_title_button_text_male));
            stateModel.setEmptyRetryCommand(StringUtils.getString(R.string.playcc_photo_album_title_button_text_male), uploadPhotoOnClickCommand);
        } else {
            titleBtnText.set(StringUtils.getString(R.string.playcc_photo_album_title_button_female));
            stateModel.setEmptyRetryCommand(StringUtils.getString(R.string.playcc_photo_album_title_button_female), uploadPhotoOnClickCommand);
        }

    }

    @Override
    public void loadDatas(int page) {
        loadAlbumDetailShowEmpty();
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        startRefresh();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mPhotoUploadSubscription = RxBus.getDefault().toObservable(MyPhotoAlbumChangeEvent.class)
                .subscribe(event -> {
                    if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_REFRESH) {
                        loadAlbumDetailShowEmpty();
                    } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_DATA) {
                        observableList.clear();
                        photoEntityList.clear();
                        for (AlbumPhotoEntity datum : event.getPhotos()) {
                            photoEntityList.add(datum);
                            MyPhotoAlbumItemViewModel itemViewModel = new MyPhotoAlbumItemViewModel(MyPhotoAlbumViewModel.this, datum);
                            observableList.add(itemViewModel);
                        }
                    } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_CANCEL_RED_PACKAGE) {
                        loadAlbumDetailShowEmpty();
//                        for (MyPhotoAlbumItemViewModel myPhotoAlbumItemViewModel : observableList) {
//                            if (myPhotoAlbumItemViewModel.itemEntity.get().getIsRedPackage() == 1) {
//                                myPhotoAlbumItemViewModel.itemEntity.get().setIsRedPackage(0);
//                            }
//                        }
                    } else {
                        loadAlbumDetailShowEmpty();
//                        for (MyPhotoAlbumItemViewModel myPhotoAlbumItemViewModel : observableList) {
//                            if (myPhotoAlbumItemViewModel.itemEntity.get().getId().intValue() == event.getPhotoId().intValue()) {
//                                if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_DELETE) {
//                                    loadAlbumDetail(null);
//                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_BURN) {
////                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsBurn() != 1) {
////                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsBurn(1);
////                                    }
//                                    loadAlbumDetail(null);
//                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_CANCEL_BURN) {
////                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsBurn() != 0) {
////                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsBurn(0);
////                                    }
//                                    loadAlbumDetail(null);
//                                } else if (event.getType() == MyPhotoAlbumChangeEvent.TYPE_SET_RED_PACKAGE) {
////                                    if (myPhotoAlbumItemViewModel.itemEntity.get().getIsRedPackage() != 1) {
////                                        myPhotoAlbumItemViewModel.itemEntity.get().setIsRedPackage(1);
////                                    }
//                                    loadAlbumDetail(null);
//                                }
//                                break;
//                            }
//                        }
                    }
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mPhotoUploadSubscription);
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Integer> clickUploadPhoto = new SingleLiveEvent<>();
    }

}