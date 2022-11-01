package com.fine.friendlycc.ui.mine.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.OccupationConfigItemEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.event.AvatarChangeEvent;
import com.fine.friendlycc.event.ProfileChangeEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;

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
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class EditProfileViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<UserDataEntity> userDataEntity = new ObservableField<>();
    public ObservableField<String> gender = new ObservableField<>("");
    //    身高
    public List<ConfigItemEntity> height = new ArrayList<>();
    //    体重
    public List<ConfigItemEntity> weight = new ArrayList<>();
    //    职业
    public List<OccupationConfigItemEntity> occupation = new ArrayList<>();

    UIChangeObservable uc = new UIChangeObservable();
    public BindingCommand uploadAvatarOnClickCommand = new BindingCommand(() -> {
        uc.clickAvatar.call();
    });
    //    选择生日
    public BindingCommand chooseBirthday = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            ToastUtils.showShort("选择生日");
            uc.clickBirthday.call();
        }
    });
    //    选择职业
    public BindingCommand chooseOccupation = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            ToastUtils.showShort("选择职业");
            uc.clickOccupation.call();
        }
    });
    //    选择身高
    public BindingCommand chooseHeight = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            ToastUtils.showShort("选择身高");
            uc.clickHeight.call();
        }
    });
    //    选择体重
    public BindingCommand chooseWeight = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            ToastUtils.showShort("选择体重");
            uc.clickWeight.call();
        }
    });
    private boolean showFlag = false;
    public BindingCommand clickSave = new BindingCommand(() -> {
        saveProfile();
    });

    public EditProfileViewModel(@NonNull Application application, AppRepository repository) {
        super(application, repository);
        height.addAll(model.readHeightConfig());
        weight.addAll(model.readWeightConfig());
        occupation.addAll(model.readOccupationConfig());

    }

    @Override
    public void onEnterAnimationEnd() {
        super.onEnterAnimationEnd();
        loadProfile();
    }

    //获取个人资料
    private void loadProfile() {
        //RaJava模拟登录
        model.getUserData()
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<UserDataEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<UserDataEntity> response) {
                        UserDataEntity data = response.getData();
                        showFlag = data.isPerfect();
                        model.saveUserData(data);
                        userDataEntity.set(data);
                        if(userDataEntity.get().getSex() != null){
                            gender.set(StringUtils.getString((userDataEntity.get().getSex() == 0 ? R.string.playfun_fragment_edit_profile_male : R.string.playfun_fragment_edit_profile_female)));
                        }
                    }
                });
    }

    public void saveAvatar(String filePath) {
        Observable.just(filePath)
                .doOnSubscribe(this)
                .compose(RxUtils.exceptionTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map((Function<String, String>) s -> FileUploadUtils.ossUploadFile("avatar", FileUploadUtils.FILE_TYPE_IMAGE, s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        userDataEntity.get().setAvatar(fileKey);
                        updataAvatar(fileKey);
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

    private void updataAvatar(String fileKey) {
        model.updateAvatar(fileKey)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(EditProfileViewModel.this)
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        dismissHUD();
                        ToastUtils.showShort(R.string.playfun_updata_head_success);
                        RxBus.getDefault().post(new AvatarChangeEvent(fileKey));
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    //保存修复
    public void saveProfile() {
        UserDataEntity userEntity = userDataEntity.get();
        if(userEntity==null){
            return;
        }
        if (userEntity.getAvatar() == null || userEntity.getAvatar().length() == 0) {
            ToastUtils.showShort(R.string.playfun_warn_avatar_not_null);
            return;
        }
        if (StringUtils.isEmpty(userEntity.getNickname())) {
            ToastUtils.showShort(R.string.playfun_name_nust);
            return;
        }
        if (userEntity.getBirthday() == null) {
            ToastUtils.showShort(R.string.playfun_brithday_must);
            return;
        }
        if (userEntity.getOccupationId() == null || userEntity.getOccupationId().intValue() == 0) {
            ToastUtils.showShort(R.string.playfun_occupation_must);
            return;
        }

        model.updateUserData(
                userEntity.getNickname(),
                userEntity.getPermanentCityIds(),
                userEntity.getBirthday(),
                String.valueOf(userEntity.getOccupationId()),
                userEntity.getProgramIds(),
                userEntity.getHopeObjectIds(),
                userEntity.getWeixin(),
                userEntity.getInsgram(),
                null,
                userEntity.isWeixinShow() ? 1 : 0,
                userEntity.getHeight(),
                userEntity.getWeight(),
                userEntity.getDesc()
        )
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dismissHUD();
                        model.saveUserData(userEntity);
                        RxBus.getDefault().post(new ProfileChangeEvent());
                        ToastUtils.showShort(R.string.playfun_alter_success);
                        pop();

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }

    public boolean getTipMoneyShowFlag() {
        return ConfigManager.getInstance().getTipMoneyShowFlag();
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> clickAvatar = new SingleLiveEvent<>();
        public SingleLiveEvent clickBirthday = new SingleLiveEvent<>();
        public SingleLiveEvent clickOccupation = new SingleLiveEvent<>();
        public SingleLiveEvent clickHeight = new SingleLiveEvent<>();
        public SingleLiveEvent clickWeight = new SingleLiveEvent<>();
        public SingleLiveEvent clickUploadingHead = new SingleLiveEvent<>();

        public SingleLiveEvent showFlagClick = new SingleLiveEvent();
    }

}