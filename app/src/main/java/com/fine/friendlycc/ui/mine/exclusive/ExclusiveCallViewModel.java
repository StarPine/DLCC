package com.fine.friendlycc.ui.mine.exclusive;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.ExclusiveAccostInfoEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.utils.ToastCenterUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.tencent.qcloud.tuicore.Status;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
 * 修改备注：我的专属招呼viewmodel
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/11 20:55
 */
public class ExclusiveCallViewModel extends BaseViewModel<AppRepository> {
    //回传事件
    public SingleLiveEvent<Void> editText = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> editAudio = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> playAudio = new SingleLiveEvent<>();

    public final int TEXT_TYPE = 1;
    public final int AUDIO_TYPE = 2;
    public ObservableField<List<ExclusiveAccostInfoEntity>> accostInfoEntity = new ObservableField<>();
    public ObservableField<String> textContent = new ObservableField<>();
    public ObservableField<String> audioContent = new ObservableField<>();
    public ObservableField<String> audioSecond = new ObservableField<>();
    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<List<String>> sensitiveWords = new ObservableField<>();
    public int textTypeId;
    public int audioTypeId;

    /**
     * 删除文本搭讪语
     */
    public BindingCommand delTextAccostOnClick = new BindingCommand(() -> delExclusiveAccost(TEXT_TYPE));

    /**
     * 删除语音搭讪语
     */
    public BindingCommand delAudioAccostOnClick = new BindingCommand(() -> delExclusiveAccost(AUDIO_TYPE));

    /**
     * 编辑文本搭讪语
     */
    public BindingCommand editTextAccostOnClick = new BindingCommand(() -> {
        editText.call();
    });

    /**
     * 编辑语音搭讪语
     */
    public BindingCommand editAudioAccostOnClick = new BindingCommand(() -> {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        editAudio.call();
    });

    /**
     * 播放录音
     */
    public BindingCommand audioPlayOnClick = new BindingCommand(() -> {
        if (Status.mIsShowFloatWindow){
            ToastUtils.showShort(R.string.audio_in_call);
            return;
        }
        playAudio.call();
    });


    public ExclusiveCallViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
        sensitiveWords.set(model.readSensitiveWords());
        avatar.set(ConfigManager.getInstance().getAvatar());
    }

    public void getExclusiveAccost() {
        model.getExclusiveAccost()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<List<ExclusiveAccostInfoEntity>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<List<ExclusiveAccostInfoEntity>> response) {
                        List<ExclusiveAccostInfoEntity> data = response.getData();

                        if (data != null) {
                            for (ExclusiveAccostInfoEntity accostInfo : data) {
                                int type = accostInfo.getType();
                                if (type == TEXT_TYPE) {
                                    textContent.set(accostInfo.getContent());
                                } else if (type == AUDIO_TYPE) {
                                    audioContent.set(accostInfo.getContent());
                                    audioSecond.set(accostInfo.getLen() + "");
                                }
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    public void delExclusiveAccost(int type) {
        model.delExclusiveAccost(type)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse>() {
                    @Override
                    public void onSuccess(BaseDataResponse response) {
                        if (type == TEXT_TYPE) {
                            textContent.set(null);
                        } else if (type == AUDIO_TYPE) {
                            audioContent.set(null);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

    //上传音频
    public void uploadUserSoundFile(Integer type, String filePath, Integer sound_time) {
        Observable.just(filePath)
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return FileUploadUtils.ossUploadFileAudio("sound/", FileUploadUtils.FILE_TYPE_AUDIO, s, null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String fileKey) {
                        dismissHUD();
                        setExclusiveAccost(type, fileKey, sound_time);
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

    public void setExclusiveAccost(Integer type, String content, int second) {
        model.setExclusiveAccost(type, content, second)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse>() {
                    @Override
                    public void onSuccess(BaseDataResponse response) {

                        if (type == TEXT_TYPE) {
                            textContent.set(content);
                            ToastCenterUtils.showShort(R.string.playfun_text_accost_tips3);
                        } else if (type == AUDIO_TYPE) {
                            audioContent.set(content);
                            audioSecond.set(second + "");
                            ToastCenterUtils.showShort(R.string.playfun_audio_accost_tips2);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }

}
