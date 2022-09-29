package com.dl.playfun.ui.message.mediagallery.photo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.viewmodel.BaseViewModel;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;

import java.util.Map;

import me.goldze.mvvmhabit.base.BaseModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/14 14:23
 * Description: This is MediaGalleryPhotoPayViewModel
 */
public class MediaGalleryPhotoPayViewModel extends BaseViewModel<AppRepository> {

    public MediaGalleryEditEntity mediaGalleryEditEntity;

    //快照专属是否已读
    public ObservableBoolean isReadLook = new ObservableBoolean(false);

    //是否需要评价
    public ObservableBoolean evaluationState = new ObservableBoolean(false);

    //评价状态：评价，0未评价，1差评，2好评
    public SingleLiveEvent<Integer> evaluationLikeEvent = new SingleLiveEvent<>();
    //快照可见倒计时状态
    public ObservableBoolean snapshotTimeState = new ObservableBoolean(false);
    //快照可见倒计时时间
    public ObservableField<String> snapshotTimeText = new ObservableField<>("10s");
    //提示解锁
    public ObservableBoolean snapshotLockState = new ObservableBoolean(false);
    //解锁事件
    public SingleLiveEvent<Void> snapshotLockEvent = new SingleLiveEvent<>();
    //查看解锁按钮
    public BindingCommand<Void> clickUnLock = new BindingCommand<>(() -> {
        if(mediaGalleryEditEntity!=null){
            mediaGallerySnapshotUnLock(mediaGalleryEditEntity.getMsgKeyId(),mediaGalleryEditEntity.getToUserId());
        }
    });


    //返回上一页
    public BindingCommand<Void> onBackViewClick = new BindingCommand<>(this::finish);

    public MediaGalleryPhotoPayViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }
    //快照查看
    public void mediaGallerySnapshotUnLock(String msgKey,Integer toUserId){
        model.mediaGallerySnapshotUnLock(msgKey,toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        snapshotLockEvent.call();
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
    //查询当前评价
    public void mediaGalleryEvaluationQry(String msgKey,Integer toUserId){
        model.mediaGalleryEvaluationQry(msgKey,toUserId)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<Map<String, Integer>>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<Map<String, Integer>> mapBaseDataResponse) {
                        if(!mapBaseDataResponse.isDataEmpty()){
                            Map<String, Integer> mapData = mapBaseDataResponse.getData();
                            if(mapData.containsKey("evaluation")){
                                evaluationLikeEvent.setValue(mapData.get("evaluation"));
                            }
                        }
                    }
                });
    }
    //调整当前评价
    public void mediaGalleryEvaluationPut(String msgKey,Integer toUserId,Integer type) {
        model.mediaGalleryEvaluationPut(msgKey, toUserId, type)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        evaluationLikeEvent.setValue(type);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissHUD();
                    }
                });
    }
}
