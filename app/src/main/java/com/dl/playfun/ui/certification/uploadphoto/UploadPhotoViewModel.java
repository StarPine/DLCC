package com.dl.playfun.ui.certification.uploadphoto;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.ui.certification.facerecognition.FaceRecognitionFragment;
import com.dl.playfun.utils.FileUploadUtils;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.dl.playfun.widget.picchoose.PicChooseItemEntity;
import com.dl.playfun.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class UploadPhotoViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<PicChooseItemEntity> selectedPhotoPath = new ObservableField<>();
    public BindingCommand nextOnClickCommand = new BindingCommand(() -> uploadPhoto());
    UIChangeObservable uc = new UIChangeObservable();

    public UploadPhotoViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    public void uploadPhoto() {
        if (selectedPhotoPath.get() == null) {
            ToastUtils.showShort(R.string.playfun_model_goddesscertification_choose_photo);
            return;
        }

        Observable.just(selectedPhotoPath.get())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<PicChooseItemEntity, String>) s -> FileUploadUtils.ossUploadFile("certification/", s.getMediaType(), s.getSrc()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        model.saveVerifyFaceImage(fileKey)
                                .compose(RxUtils.schedulersTransformer())
                                .compose(RxUtils.exceptionTransformer())
                                .doOnSubscribe(UploadPhotoViewModel.this)
                                .subscribe(new BaseObserver<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        dismissHUD();
                                        AppContext.instance().logEvent(AppsFlyerEvent.Next_step);
                                        start(FaceRecognitionFragment.class.getCanonicalName());
                                    }

                                    @Override
                                    public void onComplete() {
                                        super.onComplete();
                                        dismissHUD();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playfun_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });

    }

    public class UIChangeObservable {
        public SingleLiveEvent clickAddPic = new SingleLiveEvent<>();

    }

}