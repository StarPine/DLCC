package com.fine.friendlycc.ui.certification.certificationfemale;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.ui.certification.goddesscertification.GoddessCertificationFragment;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.StatusEntity;
import com.fine.friendlycc.event.FaceCertificationEvent;
import com.fine.friendlycc.event.GoddessCertificationEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.certification.updateface.UpdateFaceFragment;
import com.fine.friendlycc.ui.certification.uploadphoto.UploadPhotoFragment;
import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 女士真人认证
 *
 * @author wulei
 */
public class CertificationFemaleViewModel extends BaseRefreshViewModel<AppRepository> {

    public ObservableField<Integer> applyGoddessStatus = new ObservableField<>(-2);
    public ObservableField<String> applyGoddessStatusStr = new ObservableField<>();
    public ObservableField<Boolean> faceCertification = new ObservableField<>(false);
    public BindingCommand faceVerifyOnClickCommand = new BindingCommand(() ->
    {
        if (faceCertification.get()) {
            start(UpdateFaceFragment.class.getCanonicalName());
        } else {
            AppContext.instance().logEvent(AppsFlyerEvent.Identity_Verification);
            start(UploadPhotoFragment.class.getCanonicalName());
        }
    });
    public BindingCommand goddessVerifyOnClickCommand = new BindingCommand(() -> {
        if (model.readUserData().getCertification() != 1) {
            ToastUtils.showShort(R.string.playfun_warn_no_certification);
            return;
        }
        if (applyGoddessStatus.get() == -1 || applyGoddessStatus.get() == 2) {
            start(GoddessCertificationFragment.class.getCanonicalName());
        } else {
            if (applyGoddessStatus.get() == 0) {
                ToastUtils.showShort(R.string.playfun_model_certification_again);
            } else if (applyGoddessStatus.get() == 1) {
                ToastUtils.showShort(R.string.playfun_model_certifition_pass_application);
            }
        }
    });
    private Disposable mSubscription;
    private Disposable mFaceCertificationSubscription;

    public CertificationFemaleViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(GoddessCertificationEvent.class)
                .subscribe(event -> {
                    loadApplyGoddessResult();
                });

        mFaceCertificationSubscription = RxBus.getDefault().toObservable(FaceCertificationEvent.class)
                .subscribe(event -> {
                    loadFaceCertification();
                });
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
        RxSubscriptions.remove(mFaceCertificationSubscription);
    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadFaceCertification();
        loadApplyGoddessResult();
    }

    @Override
    public void loadDatas(int page) {
        loadFaceCertification();
        loadApplyGoddessResult();
    }

    public void loadFaceCertification() {
        model.faceIsCertification()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<StatusEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<StatusEntity> response) {
                        faceCertification.set(response.getData().getStatus() == 1);
                    }
                });
    }

    public void loadApplyGoddessResult() {
        model.applyGoddessResult()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new BaseObserver<BaseDataResponse<StatusEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<StatusEntity> response) {
                        stopRefreshOrLoadMore();
                        //状态 -1未申请 0等待审核 1审核通过 2未通过
                        int status = response.getData().getStatus();
                        applyGoddessStatus.set(status);
                        switch (status) {
                            case -1:
                                applyGoddessStatusStr.set(StringUtils.getString(R.string.playfun_model_certification_not_applied));
                                break;
                            case 0:
                                applyGoddessStatusStr.set(StringUtils.getString(R.string.playfun_model_certification_wait_applied));
                                break;
                            case 1:
                                applyGoddessStatusStr.set(StringUtils.getString(R.string.playfun_model_certifition_pass));
                                break;
                            case 2:
                                applyGoddessStatusStr.set(StringUtils.getString(R.string.playfun_model_certifition_no_pass));
                                break;
                            default:
                                applyGoddessStatusStr.set("");
                        }
                    }
                });
    }

    public Boolean getTipMoneyShowFlag(boolean certification) {
        return certification && ConfigManager.getInstance().getTipMoneyShowFlag();
    }

}