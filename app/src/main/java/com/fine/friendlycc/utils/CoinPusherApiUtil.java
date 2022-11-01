package com.fine.friendlycc.utils;

import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.manager.ConfigManager;

import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/23 18:08
 * Description: This is CoinPusherApiUtil
 */
public class CoinPusherApiUtil {
    public static void endGamePaying(Integer roomId){
        //结束游戏
        ConfigManager.getInstance().getAppRepository().playingCoinPusherClose(roomId)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {

                    }
                });
    }
}
