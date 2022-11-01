package com.fine.friendlycc.ui.userdetail.theirphotoalbum;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.event.TheirPhotoAlbumChangeEvent;
import com.fine.friendlycc.ui.viewmodel.BaseTheirPhotoAlbumViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;

/**
 * 她的相册
 *
 * @author wulei
 */
public class TheirPhotoAlbumViewModel extends BaseTheirPhotoAlbumViewModel<AppRepository> {

    private Disposable mPhotoStateChangeSubscription;

    public TheirPhotoAlbumViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadAlbumDetail(userId.get(), null);
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mPhotoStateChangeSubscription = RxBus.getDefault().toObservable(TheirPhotoAlbumChangeEvent.class)
                .subscribe(new Consumer<TheirPhotoAlbumChangeEvent>() {
                    @Override
                    public void accept(TheirPhotoAlbumChangeEvent event) throws Exception {
                        for (TheirPhotoAlbumItemViewModel theirPhotoAlbumItemViewModel : observableList) {
                            if (event.getType() == TheirPhotoAlbumChangeEvent.TYPE_BURN) {
                                if (theirPhotoAlbumItemViewModel.itemEntity.get().getId().intValue() == event.getPhotoId().intValue()) {
                                    if (theirPhotoAlbumItemViewModel.itemEntity.get().getBurnStatus() != 1) {
                                        theirPhotoAlbumItemViewModel.itemEntity.get().setBurnStatus(1);
                                    }
                                }
                            } else if (event.getType() == TheirPhotoAlbumChangeEvent.TYPE_PAY_RED_PACKAGE) {

                            }
                        }
                    }
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mPhotoStateChangeSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mPhotoStateChangeSubscription);
    }
}