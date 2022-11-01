package com.fine.friendlycc.ui.mine.audio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.entity.SoundEntity;
import com.fine.friendlycc.event.RefreshUserDataEvent;
import com.fine.friendlycc.utils.FileUploadUtils;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
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
 * Author: 彭石林
 * Time: 2021/10/21 10:21
 * Description: This is TapeAudioViewModel
 */
public class TapeAudioViewModel extends BaseViewModel<AppRepository> {
    //当前录音文案提示语
    public ObservableField<String> audioTextHint = new ObservableField<>();
    //当前录音文案
    public ObservableField<String> audioText = new ObservableField<String>();
    //录音文案集合
    public List<SoundEntity>  audioDataList = null;
    //录音文案数组坐标
    public int audioPostion = -1;

    public  int page = 1;
    //切换录音文案
    public BindingCommand changeAudioChick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getUserSound();
        }
    });
    //返回上一页
    public BindingCommand backFinish = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pop();
        }
    });

    public TapeAudioViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }
    SingleLiveEvent<Integer> showDialog = new SingleLiveEvent<Integer>();

    /*
    * @Desc TODO(录音文案)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2021/10/21
    */
    public void getUserSound(){
        if(!ObjectUtils.isEmpty(audioDataList) && audioPostion+1<audioDataList.size()-1){
            SoundEntity soundEntity = audioDataList.get(audioPostion++);
            audioTextHint.set(soundEntity.getType());
            audioText.set(soundEntity.getContent());
            return;
        }
        if(page!=1){
            page++;
        }
        model.getUserSound(page)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseListDataResponse<SoundEntity>>(){

                    @Override
                    public void onSuccess(BaseListDataResponse<SoundEntity> baseDataResponse) {
                        try{
                            List<SoundEntity> dataList =  baseDataResponse.getData().getData();
                            if(!ObjectUtils.isEmpty(dataList)){
                                audioDataList = dataList;
                                if(audioDataList.size()>1){
                                    SoundEntity soundEntity = audioDataList.get(0);
                                    audioTextHint.set(soundEntity.getType());
                                    audioText.set(soundEntity.getContent());
                                    audioPostion = 0;
                                }
                            }
                        }catch (Exception e) {

                        }
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                        super.onComplete();
                    }
                });
    }
    //上传音频
    public void uploadUserSoundFile(String filePath,Integer sound_time){
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
                        putUserSound(fileKey,sound_time);
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
    //上传音频到后台
    public void putUserSound(String path,Integer sound_time){
        model.putUserSound(path,sound_time)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse>(){
                    @Override
                    public void onSuccess(BaseDataResponse baseDataResponse) {
                        RxBus.getDefault().post(new RefreshUserDataEvent(path, sound_time));
                        Map<String,Object> mapData = (Map<String, Object>) baseDataResponse.getData();
                        if(!ObjectUtils.isEmpty(mapData) &&  !ObjectUtils.isEmpty(mapData.get("score"))){
                            String scoreF = String.valueOf(mapData.get("score"));
                            if(scoreF.indexOf(".")!=-1){
                                scoreF = scoreF.substring(0,scoreF.indexOf("."));
                            }
                            Integer score = Integer.parseInt(scoreF);
                            if(score != null && score > 0){
                                //showDialog.setValue(score);
                                return;
                            }
                        }
                        ToastUtils.showShort(R.string.playfun_mine_audio_success);
                        pop();
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }

                });
    }
}
