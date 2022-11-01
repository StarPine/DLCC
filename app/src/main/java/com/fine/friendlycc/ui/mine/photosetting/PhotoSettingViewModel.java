package com.fine.friendlycc.ui.mine.photosetting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.event.MyPhotoAlbumChangeEvent;
import com.fine.friendlycc.event.PhotoCallCoverEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.utils.ImageUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 相片设置
 *
 * @author wulei
 */
public class PhotoSettingViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<String> titleBtnText = new ObservableField<>();
    public ObservableField<String> titleText = new ObservableField<>();
    public ObservableField<Boolean> isBurn = new ObservableField<>(false);
    public ObservableField<Integer> currentItem = new ObservableField<>(0);

    public BindingViewPagerAdapter<PhotoSettingItemViewModel> adAdapter = new BindingViewPagerAdapter<>();

    public ItemBinding<PhotoSettingItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_photo_setting);
    public ObservableList<PhotoSettingItemViewModel> items = new ObservableArrayList<>();
    UIChangeObservable uc = new UIChangeObservable();
    private int mType;
    private int mIndex = 0;
    public BindingCommand burnOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            AlbumPhotoEntity entity = items.get(mIndex).itemEntity.get();
            if (mType == PhotoSettingFragment.TYPE_PHOTO_SETTING) {
                setBurnImage(entity.getId(), isBurn.get());
                entity.setIsBurn(isBurn.get() ? 1 : 0);
            } else {
                entity.setIsBurn(isBurn.get() ? 1 : 0);
            }
        }
    });
    private int viewPagerPreviousPage = 0;
    //ViewPager切换监听
    public BindingCommand<Integer> onPageSelectedCommand = new BindingCommand<>(index -> {
        mIndex = index;
        titleText.set(String.format("%s/%s", mIndex + 1, items.size()));
        Integer burnStatus = items.get(mIndex).itemEntity.get().getIsBurn();
        isBurn.set(burnStatus != null && burnStatus == 1);

        if (items.size() > viewPagerPreviousPage) {
//            if (items.get(viewPagerPreviousPage).itemEntity.get().getType() == 2) {
            items.get(viewPagerPreviousPage).playStatus.set(items.get(viewPagerPreviousPage).playStatus.get() + 1);
//            }
        }
        viewPagerPreviousPage = index;
    });
    private int i = 0;
    //完成按钮的点击事件
    public BindingCommand deleteOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (mType == PhotoSettingFragment.TYPE_PHOTO_SETTING) {
                uc.clickDelete.postValue(mIndex);
            } else if (mType == PhotoSettingFragment.TYPE_PHOTO_REVIEW) {
                i = 0;
//                showHUD();
                uploadPhoto();
            }
        }
    });

    public PhotoSettingViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentItem.set(mIndex);
    }

    public void setPhotos(int type, int index, List<AlbumPhotoEntity> photos) {
        this.mIndex = index;
        this.mType = type;
        for (AlbumPhotoEntity photo : photos) {
            PhotoSettingItemViewModel photoSettingItemViewModel = new PhotoSettingItemViewModel(PhotoSettingViewModel.this, photo);
            //不为null  并且是本人
            if(photo.getVerificationType()==1 && !ConfigManager.getInstance().isMale()){
                Integer callCoverState = photo.getIsCallCover();
                if(callCoverState!=null){
                    if(callCoverState==0){
                        photoSettingItemViewModel.photoCoverShow.set(0);
                    }else if(callCoverState==1){
                        photoSettingItemViewModel.photoCoverShow.set(1);
                    }else{
                        photoSettingItemViewModel.photoCoverShow.set(-1);
                    }
                }else{
                    photoSettingItemViewModel.photoCoverShow.set(-1);
                }
            }
            items.add(photoSettingItemViewModel);
        }
        titleText.set(String.format("%s/%s", mIndex + 1, items.size()));
        if (type == PhotoSettingFragment.TYPE_PHOTO_REVIEW) {
            titleBtnText.set(StringUtils.getString(R.string.playfun_finish));
        } else if (type == PhotoSettingFragment.TYPE_PHOTO_SETTING) {
            titleBtnText.set(StringUtils.getString(R.string.playfun_delete));
        }
        if (items != null && items.size() > mIndex) {
            Integer burnStatus = items.get(mIndex).itemEntity.get().getIsBurn();
            isBurn.set(burnStatus != null && burnStatus == 1);
        }
    }

    public void uploadPhoto() {
        if (i >= items.size()) {
            dismissHUD();
            RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genRefreshEvent());
            pop();
            return;
        }
        AlbumPhotoEntity albumPhotoEntity = items.get(i).itemEntity.get();
        if (albumPhotoEntity == null) {
            return;
        }
        AppContext.instance().logEvent(AppsFlyerEvent.me_Upload_Photo);
        Observable.just(albumPhotoEntity)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribeOn(Schedulers.io())
                .map((Function<AlbumPhotoEntity, String>) entity -> FileUploadUtils.ossUploadFile(String.format("album/%s/", model.readUserData().getId()), entity.getType(), entity.getSrc(), new FileUploadUtils.FileUploadProgressListener() {
                    @Override
                    public void fileCompressProgress(int progress) {
                        showProgressHUD(String.format(StringUtils.getString(R.string.playfun_compressing), progress), progress);
                    }

                    @Override
                    public void fileUploadProgress(int progress) {
                        showProgressHUD(String.format(StringUtils.getString(R.string.playfun_uploading), progress), progress);
                    }
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        String videoImage = null;
                        if (albumPhotoEntity.getType() == 2) {
                            videoImage = ImageUtils.getVideoThumbnailBase64(albumPhotoEntity.getSrc());
                        }
                        model.albumInsert(albumPhotoEntity.getType(), fileKey, albumPhotoEntity.getIsBurn(), videoImage)
                                .compose(RxUtils.schedulersTransformer())
                                .compose(RxUtils.exceptionTransformer())
                                .doOnSubscribe(PhotoSettingViewModel.this)
                                .subscribe(new BaseObserver<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
//                                        dismissHUD();
                                        i++;
                                        AppContext.instance().logEvent(AppsFlyerEvent.me_Upload_Photo_succeed);
                                        uploadPhoto();
                                    }

                                    @Override
                                    public void onError(RequestException e) {
                                        super.onError(e);
                                        ToastUtils.showShort(e.getMessage());
                                        i++;
                                        uploadPhoto();
                                    }

                                    @Override
                                    public void onComplete() {
//                                        dismissHUD();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dismissHUD();
                        ToastUtils.showShort(R.string.playfun_upload_failed);
                        i++;
                        uploadPhoto();
                    }

                    @Override
                    public void onComplete() {
//                        dismissHUD();
                    }
                });
    }

    public void deleteAlbumPhoto(int index) {
        PhotoSettingItemViewModel photoSettingItemViewModel = items.get(index);
        int id = photoSettingItemViewModel.itemEntity.get().getId();
        model.delAlbumImage(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        items.remove(index);
                        if (items.size() == 0) {
                            pop();
                        }
                        titleText.set(String.format("%s/%s", mIndex + 1, items.size()));
                        RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genDeleteEvent(id));
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });

    }

    public void setBurnImage(int id, boolean state) {
        model.setBurnAlbumImage(id, state)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (state) {
                            RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genSetBurnEvent(id));
                        } else {
                            RxBus.getDefault().post(MyPhotoAlbumChangeEvent.genCancelBurnEvent(id));
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    //上传为封面图片、视频
    public void photoCallCover(int albumId){
        model.photoCallCover(albumId,1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>(){
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if(items!=null){
                            for (int j = 0; j < items.size(); j++) {
                                AlbumPhotoEntity albumPhotoEntity = items.get(j).itemEntity.get();
                                if(albumPhotoEntity!=null){
                                    //不为null  并且是本人
                                    if(albumPhotoEntity.getVerificationType()==1 && !ConfigManager.getInstance().isMale()){
                                        if(albumId == albumPhotoEntity.getId()){
                                            items.get(j).photoCoverShow.set(1);
                                            RxBus.getDefault().post(new PhotoCallCoverEvent(albumPhotoEntity));
                                        }else{
                                            items.get(j).photoCoverShow.set(0);
                                        }
                                    }else{
                                        items.get(j).photoCoverShow.set(-1);
                                    }
                                }
                            }
                            adAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onComplete(){
                        dismissHUD();
                    }
                });
    }


    public class UIChangeObservable {
        public SingleLiveEvent<Integer> clickDelete = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> clickPhotoCoverAlert = new SingleLiveEvent<>();
    }

}