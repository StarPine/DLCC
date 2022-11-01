package com.fine.friendlycc.manager;

import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.event.CoinPusherGamePlayingEvent;
import com.google.gson.reflect.TypeToken;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuicore.custom.CustomConvertUtils;
import com.tencent.qcloud.tuicore.custom.entity.CustomBaseEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoEvaluationEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoPushEntity;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * Author: 彭石林
 * Time: 2022/9/7 17:57
 * Description: IM自定义消息体处理
 */
public class V2TIMCustomManagerUtil {
    private static final String TAG = "V2TIMCustomManagerUtil";
    /**
    * @Desc TODO(推币机模块处理)
    * @author 彭石林
    * @parame [contentBody]
    * @return void
    * @Date 2022/9/7
    */
    public static void CoinPusherManager(Map<String,Object> contentBody){
        //获取moudle-pushCoinGame 推币机
        if(CustomConvertUtils.ContainsMessageModuleKey(contentBody, CustomConstants.Message.MODULE_NAME_KEY,CustomConstants.CoinPusher.MODULE_NAME)){
            Map<String,Object> pushCoinGame = CustomConvertUtils.ConvertMassageModule(contentBody,CustomConstants.Message.MODULE_NAME_KEY,CustomConstants.CoinPusher.MODULE_NAME,CustomConstants.Message.CUSTOM_CONTENT_BODY);
            if(ObjectUtils.isNotEmpty(pushCoinGame)){
                //消息类型--判断
                if(pushCoinGame.containsKey(CustomConstants.Message.CUSTOM_MSG_KEY)){
                    if (CustomConvertUtils.ContainsMessageModuleKey(pushCoinGame,CustomConstants.Message.CUSTOM_MSG_KEY,CustomConstants.CoinPusher.LITTLE_GAME_WINNING)){
                        //中奖 小游戏（叠叠乐、小玛利）
                        RxBus.getDefault().post(new CoinPusherGamePlayingEvent(CustomConstants.CoinPusher.LITTLE_GAME_WINNING));
                    }else if(CustomConvertUtils.ContainsMessageModuleKey(pushCoinGame,CustomConstants.Message.CUSTOM_MSG_KEY,CustomConstants.CoinPusher.START_WINNING)){
                        //开始游戏
                        RxBus.getDefault().post(new CoinPusherGamePlayingEvent(CustomConstants.CoinPusher.START_WINNING));
                    }else if (CustomConvertUtils.ContainsMessageModuleKey(pushCoinGame,CustomConstants.Message.CUSTOM_MSG_KEY,CustomConstants.CoinPusher.END_WINNING)){
                        //落币结束
                        RxBus.getDefault().post(new CoinPusherGamePlayingEvent(CustomConstants.CoinPusher.END_WINNING));
                    }else if(CustomConvertUtils.ContainsMessageModuleKey(pushCoinGame,CustomConstants.Message.CUSTOM_MSG_KEY,CustomConstants.CoinPusher.DROP_COINS)){
                        //落币奖励
                        Map<String,Object> startWinning = CustomConvertUtils.ConvertMassageModule(pushCoinGame,CustomConstants.Message.CUSTOM_MSG_KEY,CustomConstants.CoinPusher.DROP_COINS,CustomConstants.Message.CUSTOM_MSG_BODY);
                        if(ObjectUtils.isNotEmpty(startWinning)){
                            BigDecimal goldNumberDecimal = new BigDecimal(String.valueOf(ObjectUtils.getOrDefault(startWinning.get("goldNumber"),0)));
                            BigDecimal totalGoldDecimal = new BigDecimal(String.valueOf(ObjectUtils.getOrDefault(startWinning.get("totalGold"),0)));
                            RxBus.getDefault().post(new CoinPusherGamePlayingEvent(CustomConstants.CoinPusher.DROP_COINS,goldNumberDecimal.intValue(),totalGoldDecimal.intValue()));
                        }
                    }
                }
            }
        }
    }

    public static VideoPushEntity videoPushManager(String customData){
        try {
            Type videoPushType = new TypeToken<CustomBaseEntity<VideoPushEntity>>() {}.getType();
            return (VideoPushEntity) CustomConvertUtils.customMassageAnalyzeModule(customData,
                            CustomConstants.PushMessage.MODULE_NAME,
                            CustomConstants.PushMessage.VIDEO_CALL_PUSH,
                            videoPushType);
        }catch (Exception e){
            Log.i("V2TIMCustomManagerUtil", "自定数据解析异常");
        }
        return null;
    }

    public static VideoEvaluationEntity videoEvaluationManager(String customData){
        try {
            Type videoEvaluationType = new TypeToken<CustomBaseEntity<VideoEvaluationEntity>>() {}.getType();
            return  (VideoEvaluationEntity) CustomConvertUtils.customMassageAnalyzeModule(customData,
                            CustomConstants.PushMessage.MODULE_NAME,
                            CustomConstants.PushMessage.VIDEO_CALL_FEEDBACK,
                            videoEvaluationType);
        }catch (Exception e){
            Log.i("V2TIMCustomManagerUtil", "自定数据解析异常");
        }
        return null;
    }


}
