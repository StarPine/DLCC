package com.tencent.custom.tmp;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/9/8 10:49
 * Description: 德力-自定义消息模板
 */
public class CustomDlTempMessage extends TUIMessageBean implements Serializable {
    //模板ID
    private final String businessID = CustomConstants.Message.CUSTOM_BUSINESS_ID_KEY;
    //当前业务的模块
    private MsgModuleInfo contentBody;
    //当前用户的语言
    private String language;

    public String getBusinessID() {
        return businessID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MsgModuleInfo getContentBody() {
        return contentBody;
    }

    public void setContentBody(MsgModuleInfo contentBody) {
        this.contentBody = contentBody;
    }

    public CustomDlTempMessage() {
    }

    public CustomDlTempMessage(MsgModuleInfo contentBody) {
        this.contentBody = contentBody;
    }

    @Override
    public String onGetDisplayString() {
        return getExtra();
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {
        setExtra(TUIChatService.getAppContext().getString(R.string.custom_msg));
    }

    /**
    * @Desc TODO(当前业务的模块)
    * @author 彭石林
    * @Date 2022/9/8
    */
    public static class MsgModuleInfo implements Serializable {
        //模块名
        private String msgModuleName;
        //模块内容体
        private MsgBodyInfo contentBody;

        public String getMsgModuleName() {
            return msgModuleName;
        }

        public void setMsgModuleName(String msgModuleName) {
            this.msgModuleName = msgModuleName;
        }

        public MsgBodyInfo getContentBody() {
            return contentBody;
        }
        public void setContentBody(MsgBodyInfo contentBody) {
            this.contentBody = contentBody;
        }

        @Override
        public String toString() {
            return "MsgModuleInfo{" +
                    "msgModuleName='" + msgModuleName + '\'' +
                    ", contentBody=" + contentBody +
                    '}';
        }
    }

    /**
    * @Desc TODO(消息的类型，可以自定义)
    * @author 彭石林
    * @Date 2022/9/8
    */
    public static class MsgBodyInfo implements Serializable{
        ////消息的类型，可以自定义
        private String customMsgType;
        private Object customMsgBody;

        public String getCustomMsgType() {
            return customMsgType;
        }

        public void setCustomMsgType(String customMsgType) {
            this.customMsgType = customMsgType;
        }

        public Object getCustomMsgBody() {
            return customMsgBody;
        }

        public void setCustomMsgBody(Object customMsgBody) {
            this.customMsgBody = customMsgBody;
        }

        @Override
        public String toString() {
            return "MsgBodyInfo{" +
                    "customMsgType='" + customMsgType + '\'' +
                    ", customMsgBody=" + customMsgBody +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CustomDlTempMessage{" +
                "businessID='" + businessID + '\'' +
                ", contentBody=" + contentBody +
                '}';
    }
}
