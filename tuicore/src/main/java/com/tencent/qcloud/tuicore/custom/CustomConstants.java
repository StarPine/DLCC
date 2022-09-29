package com.tencent.qcloud.tuicore.custom;

/**
 * Author: 彭石林
 * Time: 2022/9/2 17:01
 * Description: This is CustomConstants
 */
public class CustomConstants {
    public static final class Message {
        //BUSINESS_ID 模板标识
        public static final String CUSTOM_BUSINESS_ID_KEY = "dl_custom_tmp";
        //内容体
        public static final String CUSTOM_CONTENT_BODY= "contentBody";
        public static final String CUSTOM_MSG_KEY = "customMsgType";
        public static final String CUSTOM_MSG_BODY = "customMsgBody";
        //模块名
        public static final String MODULE_NAME_KEY = "msgModuleName";
    }
    //推币机模块
    public static final class CoinPusher {
        //当前推币机模块名
        public static final String MODULE_NAME = "pushCoinGame";
        //落币开始
        public static final String START_WINNING = "startWinning";
        //落币结束
        public static final String END_WINNING = "endWinning";
        //小游戏： 小玛利、叠叠乐
        public static final String LITTLE_GAME_WINNING = "littleGameWinning";
        //落币数量
        public static final String DROP_COINS = "dropCoins";
    }
    //照片、视频模块
    public static final class MediaGallery {
        //当前模块名
        public static final String MODULE_NAME = "mediaGalleryPay";
        //照片
        public static final String PHOTO_GALLERY = "photoGalleryPay";
        //视频
        public static final String  VIDEO_GALLERY = "videoGalleryPay";
    }

    //视讯推送
    public static final class PushMessage {
        //模块名
        public static final String MODULE_NAME = "pushMessage";
        public static final String VIDEO_CALL_PUSH = "videoCallPush";
        public static final String VIDEO_CALL_FEEDBACK = "videoCallFeedback";
    }

    //通话模块
    public static final class CallingMessage {
        //模块名
        public static final String MODULE_NAME = "calling";
        public static final String TYPE_CALLING_FAILED = "callingFailed";
    }

    //系统提示模块
    public static final class SystemTipsMessage {
        //模块名
        public static final String MODULE_NAME = "systemTips";
        public static final String TYPE_DISABLE_CALLS = "disableCalls";
        public static final String TYPE_JUMP_WEB = "openUrl";

        /**
         * 给接听方的禁用语音系统消息
         */
        public static final int TYPE_DISABLECALLS_CALLING_AUDIO = 1003;

        /**
         * 给接听方的禁用视讯系统消息
         */
        public static final int TYPE_DISABLECALLS_CALLING_VIDEO = 1004;

        /**
         * 外部链接跳转
         */
        public static final int TYPE_OUTSIDE_URL = 10001;

        /**
         * 内部链接跳转
         */
        public static final int TYPE_INSIDE_URL = 10002;
    }

}
