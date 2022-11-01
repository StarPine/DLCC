package com.fine.friendlycc.ui.certification.certificationmale;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.StatusEntity;
import com.fine.friendlycc.event.FaceCertificationEvent;
import com.fine.friendlycc.ui.certification.updateface.UpdateFaceFragment;
import com.fine.friendlycc.ui.certification.uploadphoto.UploadPhotoFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * 男士真人认证
 *
 * @author wulei
 */
public class CertificationMaleViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<Boolean> faceCertification = new ObservableField<>(false);
    public BindingCommand startOnClickCommand = new BindingCommand(() -> {
        if (faceCertification.get()) {
            start(UpdateFaceFragment.class.getCanonicalName());
        } else {
            AppContext.instance().logEvent(AppsFlyerEvent.Identity_Verification);
            start(UploadPhotoFragment.class.getCanonicalName());
        }
    });
    private Disposable mFaceCertificationSubscription;

    public CertificationMaleViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadFaceCertification();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mFaceCertificationSubscription = RxBus.getDefault().toObservable(FaceCertificationEvent.class)
                .subscribe(event -> {
                    loadFaceCertification();
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mFaceCertificationSubscription);
    }

    public void loadFaceCertification() {
        model.faceIsCertification()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<StatusEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<StatusEntity> response) {
                        faceCertification.set(response.getData().getStatus() == 1);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

}