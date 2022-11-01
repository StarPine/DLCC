package com.fine.friendlycc.ui.task.fukubukuro;

import android.app.Application;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.ExchangeIntegraOuterEntity;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/9/4 16:52
 * Description: This is FukubukuroViewModel
 */
public class FukubukuroViewModel extends BaseViewModel<AppRepository> {

    //弹出钻石兑换弹窗
    public SingleLiveEvent<ExchangeIntegraOuterEntity> DialogExchangeIntegral = new SingleLiveEvent<>();

    public FukubukuroViewModel(@NonNull @NotNull Application application, AppRepository model) {
        super(application, model);
    }

    public String getToken() {
        return model.readLoginInfo().getToken();
    }

    /*
     * @Desc TODO(查询钻石兑换积分列表数据)
     * @author 彭石林
     * @parame []
     * @return void
     * @Date 2021/9/23
     */
    public void getExchangeIntegraListData(){
        model.getExchangeIntegraListData()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showHUD();
                })
                .subscribe(new BaseObserver<BaseDataResponse<ExchangeIntegraOuterEntity>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<ExchangeIntegraOuterEntity> exchangeIntegraEntityBaseListDataResponse) {
                        dismissHUD();
                        ExchangeIntegraOuterEntity listData = exchangeIntegraEntityBaseListDataResponse.getData();
                        if(!ObjectUtils.isEmpty(listData)){
                            if(!ObjectUtils.isEmpty(listData.getData())){
                                DialogExchangeIntegral.postValue(listData);
                            }
                        }
                    }
                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });
    }
    /**
     * @Desc TODO(钻石购买积分)
     * @author 彭石林
     * @parame [id]
     * @return void
     * @Date 2021/9/23
     */
    public void ExchangeIntegraBuy(Integer id){
        model.ExchangeIntegraBuy(id)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>(){
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.showShort(R.string.dialog_exchange_integral_success);
                    }
                    @Override
                    public void onError(RequestException e) {
                        if(e.getMessage()!=null){
                            ToastUtils.showShort(e.getMessage());
                        }

                    }

                    @Override
                    public void onComplete() {
                        dismissHUD();
                    }
                });

    }
}
