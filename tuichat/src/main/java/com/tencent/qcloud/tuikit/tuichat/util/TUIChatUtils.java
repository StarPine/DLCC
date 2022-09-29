package com.tencent.qcloud.tuikit.tuichat.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuicore.component.interfaces.IUIKitCallback;
import com.tencent.qcloud.tuicore.util.ErrorMessageConverter;
import com.tencent.qcloud.tuicore.util.ImageUtil;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

import java.io.File;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.StringUtils;

import static com.tencent.qcloud.tuicore.TUIConstants.TUIConversation.CONVERSATION_C2C_PREFIX;
import static com.tencent.qcloud.tuicore.TUIConstants.TUIConversation.CONVERSATION_GROUP_PREFIX;

public class TUIChatUtils {

    public static final String IMAGE_BASE_URL = "https://img.play-chat.net/";

    public static <T> void callbackOnError(IUIKitCallback<T> callBack, String module, int errCode, String desc) {
        if (callBack != null) {
            callBack.onError(module, errCode, ErrorMessageConverter.convertIMError(errCode, desc));
        }
    }

    public static <T> void callbackOnError(IUIKitCallback<T> callBack, int errCode, String desc, T data) {
        if (callBack != null) {
            callBack.onError(errCode, ErrorMessageConverter.convertIMError(errCode, desc), data);
        }
    }

    public static <T> void callbackOnError(IUIKitCallback<T> callBack, int errCode, String desc) {
        if (callBack != null) {
            callBack.onError(null, errCode, ErrorMessageConverter.convertIMError(errCode, desc));
        }
    }

    public static <T> void callbackOnSuccess(IUIKitCallback<T> callBack, T data) {
        if (callBack != null) {
            callBack.onSuccess(data);
        }
    }

    public static void callbackOnProgress(IUIKitCallback callBack, Object data) {
        if (callBack != null) {
            callBack.onProgress(data);
        }
    }

    public static String getConversationIdByUserId(String id, boolean isGroup) {
        String conversationIdPrefix = isGroup ? CONVERSATION_GROUP_PREFIX : CONVERSATION_C2C_PREFIX;
        return conversationIdPrefix + id;
    }

    public static boolean isC2CChat(int chatType) {
        return chatType == V2TIMConversation.V2TIM_C2C;
    }

    public static boolean isGroupChat(int chatType) {
        return chatType == V2TIMConversation.V2TIM_GROUP;
    }

    /**
     * 获取 MessageInfo 中原图的路径
     *
     * @param msg
     * @return
     */
    public static String getOriginImagePath(final TUIMessageBean msg) {
        if (msg == null) {
            return null;
        }
        V2TIMMessage v2TIMMessage = msg.getV2TIMMessage();
        if (v2TIMMessage == null) {
            return null;
        }
        V2TIMImageElem v2TIMImageElem = v2TIMMessage.getImageElem();
        if (v2TIMImageElem == null) {
            return null;
        }
        String localImgPath = ChatMessageParser.getLocalImagePath(msg);
        if (localImgPath == null) {
            String originUUID = null;
            for (V2TIMImageElem.V2TIMImage image : v2TIMImageElem.getImageList()) {
                if (image.getType() == V2TIMImageElem.V2TIM_IMAGE_TYPE_ORIGIN) {
                    originUUID = image.getUUID();
                    break;
                }
            }
            String originPath = ImageUtil.generateImagePath(originUUID, V2TIMImageElem.V2TIM_IMAGE_TYPE_ORIGIN);
            File file = new File(originPath);
            if (file.exists()) {
                localImgPath = originPath;
            }
        }
        return localImgPath;
    }

    /**
     * 匹配包含0开头的十位数字
     *
     * @param str
     * @return
     */
    public static boolean isLineNumber(String str) {
        if (str == null || str.length() < 10) return false;
        String all = str.replaceAll("\\s+", "").toLowerCase();
        String s = all.replaceAll("isLine", "");
        String replace = s.replaceAll("0[0-9]{9}", "isLine");
        return replace.contains("isLine");
    }

    /**
     * 判断是否包含相关字段，默认小写匹配
     */
    public static boolean isContains(String message, List<String> str) {
        if (str == null || str.size() == 0 || message == null || message.length() == 0)
            return false;
        String all = message.replaceAll("\\s+", "").toLowerCase();
        for (String words : str) {
            if (words.equals("")) {
                continue;
            }
            boolean contains = all.contains(words.toLowerCase());
            if (contains) return true;
        }
        return false;
    }

    public static boolean isJSON2(String str) {
        boolean result = false;
        if (StringUtils.isEmpty(str)){
            return false;
        }
        try {
            new Gson().fromJson(str, Map.class);
            result = true;
        } catch (Exception ignored) {
        }
        return result;

    }

    public static String json2Massage(String json, String key) {
        String msgType = null;
        try {
            Map<String, Object> map_data = new Gson().fromJson(json, Map.class);
            if (map_data != null && map_data.containsKey(key)) {
                Object o = map_data.get(key);
                if (o instanceof Integer){
                    msgType = (Integer) o + "";
                }else if(o instanceof String){
                    msgType = (String) o;
                }else if(o instanceof Double){
                    msgType = (Double) o + "";
                }
            }
        }catch (Exception e){

        }

        return msgType;
    }

    public static String getFullImageUrl(String imgPath) {
        if (imgPath == null) {
            return "";
        } else {
            return IMAGE_BASE_URL + imgPath;
        }
    }

}
