package com.dl.playfun.ui.message.mediagallery.video;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.http.observer.BaseObserver;
import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.viewmodel.BaseViewModel;

import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/19 14:28
 * Description: This is MediaGalleryVideoPayViewModel
 */
public class MediaGalleryVideoPayViewModel extends BaseViewModel<AppRepository> {

    //是否需要评价
    public ObservableBoolean evaluationState = new ObservableBoolean(false);
    //本地资源
    public ObservableBoolean isLocalSrc = new ObservableBoolean(false);
    //播放地址
    public ObservableField<String> srcPath = new ObservableField<>();

    //评价状态：评价，0未评价，1差评，2好评
    public SingleLiveEvent<Integer> evaluationLikeEvent = new SingleLiveEvent<>();

    //返回上一页
    public BindingCommand<Void> onBackViewClick = new BindingCommand<>(this::finish);

    public MediaGalleryVideoPayViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
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
                                int evaluation = Integer.parseInt(String.valueOf(mapData.get("evaluation")));
                                if(evaluation == 0 || evaluation == 1){
                                    evaluationState.set(true);
                                    evaluationLikeEvent.setValue(evaluation);
                                }
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
