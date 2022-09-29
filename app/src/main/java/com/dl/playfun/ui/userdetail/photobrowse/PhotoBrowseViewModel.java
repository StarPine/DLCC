package com.dl.playfun.ui.userdetail.photobrowse;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.exception.RequestException;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.entity.SystemConfigEntity;
import com.dl.playfun.event.ApplyMessagePhotoStatusChangeEvent;
import com.dl.playfun.event.TheirPhotoAlbumChangeEvent;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.BR;
import com.dl.playfun.R;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * 相片设置
 *
 * @author wulei
 */
public class PhotoBrowseViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<Integer> type = new ObservableField<>();
    public ObservableField<String> pageText = new ObservableField<>();
    public ObservableField<Integer> currentItem = new ObservableField<>(0);

    public BindingViewPagerAdapter<PhotoBrowseItemViewModel> adAdapter = new BindingViewPagerAdapter<>();

    public ItemBinding<PhotoBrowseItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_photo_browse);
    public ObservableList<PhotoBrowseItemViewModel> items = new ObservableArrayList<>();

    public UIChangeObservable uc = new UIChangeObservable();
    private int mIndex = 0;
    private int viewPagerPreviousPage = 0;
    //ViewPager切换监听
    public BindingCommand<Integer> onPageSelectedCommand = new BindingCommand<>(index -> {
        mIndex = index;
        pageText.set(String.format("%s/%s", mIndex + 1, items.size()));

        if (items.size() > viewPagerPreviousPage) {
//                if (items.get(viewPagerPreviousPage).itemEntity.get().getType() == 2) {
            items.get(viewPagerPreviousPage).playStatus.set(items.get(viewPagerPreviousPage).playStatus.get() + 1);
//                }
        }
        viewPagerPreviousPage = index;
    });
    private AlbumPhotoEntity albumPhotoEntity;

    public PhotoBrowseViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentItem.set(mIndex);
    }

    public void onItemClick() {
        pop();
    }

    public void setPhotos(int index, List<AlbumPhotoEntity> photos) {
        if (photos == null || photos.isEmpty()) {
            return;
        }
        this.mIndex = index;
        for (AlbumPhotoEntity photo : photos) {
            int price = 999;
            SystemConfigEntity s = model.readSystemConfig();
            if (photo.getType() == 1) {
                //照片
                price = model.readSystemConfig().getImageRedPackageMoney();
            } else if (photo.getType() == 2) {
                //视频
                price = model.readSystemConfig().getVideoRedPackageMoney();
            }
            PhotoBrowseItemViewModel photoBrowseItemViewModel = new PhotoBrowseItemViewModel(PhotoBrowseViewModel.this, photo, price);
            items.add(photoBrowseItemViewModel);
        }
        pageText.set(String.format("%s/%s", mIndex + 1, items.size()));
    }

    public void itemVideoPlayCompletion(int position) {
        albumPhotoEntity = items.get(position).itemEntity.get();
        if (albumPhotoEntity.getIsBurn() == 1) {
            albumPhotoEntity.setBurnStatus(1);
            setVideoBurned(albumPhotoEntity.getId());
            RxBus.getDefault().post(TheirPhotoAlbumChangeEvent.genBurnPhotoEvent(albumPhotoEntity.getId()));
        }
    }

    public void itemPhotoBurned(int position) {
        try {
//            if (!touch) {
//                countDownDisposable.dispose();
//                albumPhotoEntity.setIsBurn(1);
//                albumPhotoEntity.setBurnStatus(1);
//                countDown.set(0);
//                if (type.get() == PhotoBrowseFragment.TYPE_ALBUM) {
//                    RxBus.getDefault().post(TheirPhotoAlbumChangeEvent.genBurnPhotoEvent(albumPhotoEntity.getId()));
//                } else if (type.get() == PhotoBrowseFragment.TYPE_APPLY_MESSAGE) {
//                    RxBus.getDefault().post(new ApplyMessagePhotoStatusChangeEvent(albumPhotoEntity.getId()));
//                }
//            } else {
            albumPhotoEntity = items.get(position).itemEntity.get();
            if (albumPhotoEntity != null) {
                if (type.get() == PhotoBrowseFragment.TYPE_ALBUM) {
                    albumPhotoEntity.setIsBurn(1);
                    albumPhotoEntity.setBurnStatus(1);
                    if (type.get() == PhotoBrowseFragment.TYPE_ALBUM) {
                        RxBus.getDefault().post(TheirPhotoAlbumChangeEvent.genBurnPhotoEvent(albumPhotoEntity.getId()));
                    } else if (type.get() == PhotoBrowseFragment.TYPE_APPLY_MESSAGE) {
                        RxBus.getDefault().post(new ApplyMessagePhotoStatusChangeEvent(albumPhotoEntity.getId()));
                    }
                } else if (type.get() == PhotoBrowseFragment.TYPE_CHAT) {
                    albumPhotoEntity.setIsBurn(1);
                    if (!StringUtils.isEmpty(albumPhotoEntity.getMsgId())) {
                        model.saveChatCustomMessageStatus(albumPhotoEntity.getMsgId(), 1);
                        //C2CChatManagerKit.getInstance().updateMessageInfoStatusByMessageId(albumPhotoEntity.getMsgId());
                    }
                    if (type.get() == PhotoBrowseFragment.TYPE_ALBUM) {
                        RxBus.getDefault().post(TheirPhotoAlbumChangeEvent.genBurnPhotoEvent(albumPhotoEntity.getId()));
                    } else if (type.get() == PhotoBrowseFragment.TYPE_APPLY_MESSAGE) {
                        RxBus.getDefault().post(new ApplyMessagePhotoStatusChangeEvent(albumPhotoEntity.getId()));
                    }
                } else if (type.get() == PhotoBrowseFragment.TYPE_APPLY_MESSAGE) {
                    setApplyMessageBurned(albumPhotoEntity.getId());
                }
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payRedPackage(int position) {
        albumPhotoEntity = items.get(position).itemEntity.get();
        if (albumPhotoEntity.getType() == 1) {
            uc.clickPayRedPackage.postValue(albumPhotoEntity.getId());
        } else if (albumPhotoEntity.getType() == 2) {
            uc.clickPayRedPackageVideo.postValue(albumPhotoEntity.getId());
        }
    }

    public void payRedPackageSuccess(int photoId) {
        for (PhotoBrowseItemViewModel item : items) {
            if (item.itemEntity.get().getId().intValue() == photoId) {
                item.itemEntity.get().setIsPay(1);
            }
        }
    }

    public void setApplyMessageBurned(int id) {
        model.checkApplyAlbumPhoto(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        if (albumPhotoEntity != null) {
                            albumPhotoEntity.setIsBurn(0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void setVideoBurned(int id) {
        model.imgeReadLog(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Integer> clickPayRedPackage = new SingleLiveEvent<>();

        public SingleLiveEvent<Integer> clickPayRedPackageVideo = new SingleLiveEvent<>();
    }

}