package com.fine.friendlycc.ui.certification.goddesscertification;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.event.GoddessCertificationEvent;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.picchoose.PicChooseItemBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class GoddessCertificationViewModel extends BaseViewModel<AppRepository> {

    private final List<String> photoSrcs = new ArrayList<>();
    public List<PicChooseItemBean> chooseMedias = new ArrayList<>();
    private int index = 0;
    public BindingCommand commitOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (chooseMedias == null || chooseMedias.size() == 0) {
                ToastUtils.showShort(R.string.playcc_model_goddesscertification_choose_photo_video);
                return;
            }
            index = 0;
            photoSrcs.clear();
            uploadPhoto();
        }
    });

    public GoddessCertificationViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    public void uploadPhoto() {
        if (index == chooseMedias.size()) {
            dismissHUD();
            commitPhotos(photoSrcs);
            return;
        }

        Observable.just(chooseMedias.get(index))
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<PicChooseItemBean, String>) entity -> FileUploadUtils.ossUploadFile("certification/", entity.getMediaType(), entity.getSrc()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        photoSrcs.add(fileKey);
                        index++;
                        uploadPhoto();
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

    /**
     * 插入验证照片到相册
     */
    private void commitPhotos(List<String> photoSrcs) {
        if (photoSrcs == null || photoSrcs.isEmpty()) {
            ToastUtils.showShort(R.string.playcc_model_goddesscertification_upload_photo);
            return;
        }

        model.applyGoddess(photoSrcs)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        RxBus.getDefault().post(new GoddessCertificationEvent());
                        pop();
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
}