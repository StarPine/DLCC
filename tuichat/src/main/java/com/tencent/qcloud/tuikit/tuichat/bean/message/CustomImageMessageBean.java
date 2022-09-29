package com.tencent.qcloud.tuikit.tuichat.bean.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.CustomImageMessage;
import com.tencent.qcloud.tuikit.tuichat.bean.message.reply.ImageReplyQuoteBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.reply.TUIReplyQuoteBean;

/**
 * 自定义图片消息
 */
public class CustomImageMessageBean extends TUIMessageBean {

    private String imgPath;
    private int imgWidth;
    private int imgHeight;

    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    @Override
    public String onGetDisplayString() {
        return getExtra();
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {
        imgPath = "";
        String data = new String(v2TIMMessage.getCustomElem().getData());
        try {
            CustomImageMessage customImageMessage = new Gson().fromJson(data, CustomImageMessage.class);
            imgPath = customImageMessage.getImgPath();
            imgWidth = customImageMessage.getImgWidth();
            imgHeight = customImageMessage.getImgHeight();

        } catch (JsonSyntaxException e) {

        }
        setExtra(TUIChatService.getAppContext().getString(R.string.picture_extra));
    }

    public String getImgPath() {
        return imgPath;
    }

    @Override
    public Class<? extends TUIReplyQuoteBean> getReplyQuoteBeanClass() {
        return ImageReplyQuoteBean.class;
    }
}
