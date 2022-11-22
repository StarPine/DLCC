package com.fine.friendlycc.ui.certification.uploadphoto;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.ui.certification.facerecognition.FaceRecognitionFragment;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.picchoose.PicChooseItemBean;
import com.fine.friendlycc.R;

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

    public ObservableField<PicChooseItemBean> selectedPhotoPath = new ObservableField<>();
    public BindingCommand nextOnClickCommand = new BindingCommand(() -> uploadPhoto());
    UIChangeObservable uc = new UIChangeObservable();

    public UploadPhotoViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    public void uploadPhoto() {
        if (selectedPhotoPath.get() == null) {
            ToastUtils.showShort(R.string.playcc_model_goddesscertification_choose_photo);
            return;
        }

        Observable.just(selectedPhotoPath.get())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<PicChooseItemBean, String>) s -> FileUploadUtils.ossUploadFile("certification/", s.getMediaType(), s.getSrc()))
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
                                        CCApplication.instance().logEvent(AppsFlyerEvent.Next_step);
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
                        ToastUtils.showShort(R.string.playcc_upload_failed);
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