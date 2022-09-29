package com.tencent.qcloud.tuikit.tuichat;

import android.os.Environment;

import com.tencent.imsdk.BaseConstants;

public class TUIChatConstants {
    public static final String CAMERA_IMAGE_PATH = "camera_image_path";
    public static final String IMAGE_WIDTH = "image_width";
    public static final String IMAGE_HEIGHT = "image_height";
    public static final String VIDEO_TIME = "video_time";
    public static final String CAMERA_VIDEO_PATH = "camera_video_path";
    public static final String IMAGE_PREVIEW_PATH = "image_preview_path";
    public static final String IS_ORIGIN_IMAGE = "is_origin_image";
    public static final String CAMERA_TYPE = "camera_type";

    public static final String BUSINESS_ID_CUSTOM_HELLO = "text_link";
    public static final String BUSINESS_ID_CUSTOM_IMAGE = "custom_image";

    public static final String FORWARD_SELECT_CONVERSATION_KEY = "forward_select_conversation_key";
    public static final int FORWARD_SELECT_ACTIVTY_CODE = 101;
    public static final String FORWARD_MERGE_MESSAGE_KEY = "forward_merge_message_key";


    public static final int GET_MESSAGE_FORWARD = 0;
    public static final int GET_MESSAGE_BACKWARD = 1;
    public static final int GET_MESSAGE_TWO_WAY = 2;
    public static final int GET_MESSAGE_LOCATE = 3;

    public static final String CHAT_INFO = "chatInfo";

    public static final String MESSAGE_BEAN = "messageBean";

    public static final String OPEN_MESSAGE_SCAN = "open_message_scan";
    public static final String OPEN_MESSAGES_SCAN_FORWARD = "open_messages_scan_forward";

    public static final String FORWARD_MODE = "forward_mode";//0,onebyone;  1,merge;
    public static final int FORWARD_MODE_ONE_BY_ONE = 0;
    public static final int FORWARD_MODE_MERGE = 1;

    public static final String SELECT_FRIENDS = "select_friends";
    public static final String GROUP_ID = "group_id";
    public static final String SELECT_FOR_CALL = "isSelectForCall";

    public static final String MESSAGE_REPLY_KEY = "messageReply";
    public static final String MESSAGE_REPLIES_KEY = "messageReplies";
    public static final String MESSAGE_REACT_KEY = "messageReact";

    public static final int ERR_SDK_INTERFACE_NOT_SUPPORT = BaseConstants.ERR_SDK_INTERFACE_NOT_SUPPORT;
    public static final String BUYING_GUIDELINES_EN = "https://intl.cloud.tencent.com/document/product/1047/36021?lang=en&pg=#changing-configuration";
    public static final String BUYING_GUIDELINES = "https://cloud.tencent.com/document/product/269/32458";
    /**
     * 1: 仅仅是一个带链接的文本消息
     * 2: iOS支持的视频通话版本，后续已经不兼容
     * 3: 未发布版本
     * 4: Android/iOS/Web互通的视频通话版本
     */
    public static final int JSON_VERSION_UNKNOWN = 0;
    public static final int JSON_VERSION_1       = 1;
    public static final int JSON_VERSION_4       = 4;
    public static int version = JSON_VERSION_4;

    public static String covert2HTMLString(String original) {
        return "\"<font color=\"#5B6B92\">" + original + "</font>\"";
    }

    public static final class Group {

        public static final String GROUP_ID = "group_id";
        public static final String GROUP_INFO = "groupInfo";
        public static final String MEMBER_APPLY = "apply";
    }

    public static class Selection {
        public static final String SELECT_ALL = "select_all";
        public static final String LIMIT = "limit";

        public static final String USER_ID_SELECT = "user_id_select";
        public static final String USER_NAMECARD_SELECT = "user_namecard_select";
    }

    //自定义消息类型
    public static class CoustomMassageType {
        public static final String TOAST_LOCAL = "toast_local";
        public static final String MESSAGE_TAG = "message_tag";//搭讪文字
        public static final String MESSAGE_COUNTDOWN = "message_countdown";//语音视频余额不足推送
        public static final String MESSAGE_PHOTO = "message_photo";//相册
        public static final String MESSAGE_EVALUATE = "message_evaluate";//评价
        public static final String MESSAGE_GIFT = "message_gift";//礼物消息
        public static final String MESSAGE_TRACKING = "message_tracking";
        public static final String MESSAGE_CUSTOM = "message_custom";//推送提示语
        public static final String SEND_VIOLATION_MESSAGE = "send_violation_message";//本地发送违规消息
        public static final String MESSAGE_CALLINGBUSY = "message_callingbusy";//忙线
        public static final String SEND_MALE_ERROR = "send_male_error";//发送异常
        public static final String CHAT_EARNINGS = "chat_earnings";//收益提示(旧的)

    }

}
