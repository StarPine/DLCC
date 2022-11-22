package com.fine.friendlycc.ui.certification.updateface;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.bean.FaceVerifyResultBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.event.FaceCertificationEvent;
import com.fine.friendlycc.ui.certification.updatefacesuccess.UpdateFaceSuccessFragment;
import com.fine.friendlycc.ui.certification.verifysuccess.FaceVerifySuccessFragment;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;

import java.util.Map;

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

/**
 * @author wulei
 */
public class UpdateFaceViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<Boolean> verifyFaceFail = new ObservableField<>(false);
    /**
     * 重新上传照片
     */
    public BindingCommand reUploadPhotoOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pop();
        }
    });
    UIChangeObservable uc = new UIChangeObservable();
    public BindingCommand startFaceVerifyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startFaceVerify();
        }
    });
    private String bizId;


    public UpdateFaceViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    private void startFaceVerify() {
        uc.clickStart.postValue(null);
    }

    public void verifyFaceResult() {
        model.faceVerifyResult(bizId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<FaceVerifyResultBean>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<FaceVerifyResultBean> response) {
                        RxBus.getDefault().post(new FaceCertificationEvent());
                        if (response.getData().getVerifyStatus() != 1) {
                            verifyFaceFail.set(true);
                            UserDataBean userDataEntity = model.readUserData();
                            userDataEntity.setCertification(1);
                            model.saveUserData(userDataEntity);
                        } else {
                            start(UpdateFaceSuccessFragment.class.getCanonicalName());
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public void imagFaceUpload() {

        Observable.just(uc.imageUrlFace.getValue())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribeOn(Schedulers.io())
                .map((Function<String, String>) entity -> FileUploadUtils.ossUploadFile(String.format("album/%s/", model.readUserData().getId()), 1, uc.imageUrlFace.getValue(), new FileUploadUtils.FileUploadProgressListener() {
                    @Override
                    public void fileCompressProgress(int progress) {
                        showProgressHUD(String.format(StringUtils.getString(R.string.playcc_compressing), progress), progress);
                    }

                    @Override
                    public void fileUploadProgress(int progress) {
                        showProgressHUD(String.format(StringUtils.getString(R.string.playcc_uploading), progress), progress);
                    }
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        model.imagFaceUpload(fileKey)
                                .compose(RxUtils.schedulersTransformer())
                                .compose(RxUtils.exceptionTransformer())
                                .doOnSubscribe(disposable -> showHUD())
                                .doOnSubscribe(UpdateFaceViewModel.this)
                                .subscribe(new BaseObserver<BaseDataResponse<Map<String, String>>>() {
                                    @Override
                                    public void onSuccess(BaseDataResponse<Map<String, String>> mapBaseDataResponse) {
                                        Map<String, String> map = mapBaseDataResponse.getData();
                                        String status = map.get("status");
                                        if (status.equals("1")) {
                                            ToastUtils.showShort(R.string.playcc_face_success);
                                            UserDataBean userDataEntity = model.readUserData();
                                            userDataEntity.setCertification(1);
                                            model.saveUserData(userDataEntity);
                                            RxBus.getDefault().post(new FaceCertificationEvent());
                                            start(FaceVerifySuccessFragment.class.getCanonicalName());
                                        } else {
                                            String message = String.valueOf(map.get("message"));
                                            if (message != null) {
                                                if (message.indexOf("Error.InternalError") != -1) { //服务内部出现未知错误，请联系技术支持排查。
                                                    ToastUtils.showShort(R.string.playcc_error_InternalError);
                                                } else if (message.indexOf("InvalidParam.MaterialsNotValid") != -1) { //请确保提交的材料类型正确性。
                                                    ToastUtils.showShort(R.string.playcc_error_MaterialsNotValid);
                                                } else if (message.indexOf("Error.MaterialsInsufficient") != -1) { // requirements for verification.	提交认证的材料种类不满足要求，请检查上传的材料种类是否符合业务场景要求。
                                                    ToastUtils.showShort(R.string.playcc_error_MaterialsInsufficient);
                                                } else if (message.indexOf("Error.NoFaceDetected") != -1) { //指定图片中没有检测到人脸。
                                                    ToastUtils.showShort(R.string.playcc_error_NoFaceDetected);
                                                } else if (message.indexOf("InvalidParam.FacePicNotGiven") != -1) { //进行比对的两个图片中，至少其中一个图片的类型应该设置为人脸照FacePic。。 At least one of the two given images has a type of FacePic
                                                    ToastUtils.showShort(R.string.playcc_error_FacePicNotGiven);
                                                } else if (message.indexOf("Error.DuplicatedTicketId") != -1) { //请确保提交的材料类型正确性。
                                                    ToastUtils.showShort(R.string.playcc_error_DuplicatedTicketId);
                                                } else { //请确保提交的材料类型正确性。
                                                    ToastUtils.showShort(R.string.playcc_error_face_message);
                                                }
                                            } else {
                                                ToastUtils.showShort(R.string.playcc_error_face_message);
                                            }
                                            verifyFaceFail.set(true);
                                        }
                                    }

                                    @Override
                                    public void onError(RequestException e) {
                                        super.onError(e);
                                        ToastUtils.showShort(e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        dismissHUD();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dismissHUD();
                        ToastUtils.showShort(R.string.playcc_upload_failed);
                    }

                    @Override
                    public void onComplete() {
                        //                     dismissHUD();
                    }
                });
    }

    public class UIChangeObservable {
        public SingleLiveEvent<String> clickStart = new SingleLiveEvent<>();
        public SingleLiveEvent<String> imageUrlFace = new SingleLiveEvent<>();

    }
}