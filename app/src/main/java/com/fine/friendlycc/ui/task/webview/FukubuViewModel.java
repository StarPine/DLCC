package com.fine.friendlycc.ui.task.webview;

import android.app.Application;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.bean.ExchangeIntegraOuterBean;
import com.fine.friendlycc.viewmodel.BaseViewModel;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/11/9 1:25
 * Description: This is FukubukuroViewModel
 */
public class FukubuViewModel extends BaseViewModel<AppRepository> {

    //弹出钻石兑换弹窗
    public SingleLiveEvent<ExchangeIntegraOuterBean> DialogExchangeIntegral = new SingleLiveEvent<>();

    public String getToken() {
        return model.readLoginInfo().getToken();
    }

    public FukubuViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
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
                .subscribe(new BaseObserver<BaseDataResponse<ExchangeIntegraOuterBean>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<ExchangeIntegraOuterBean> exchangeIntegraEntityBaseListDataResponse) {
                        dismissHUD();
                        ExchangeIntegraOuterBean listData = exchangeIntegraEntityBaseListDataResponse.getData();
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