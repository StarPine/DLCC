package com.tencent.qcloud.tuikit.tuichat.ui.interfaces;

import android.view.View;
import android.widget.TextView;

import com.tencent.custom.CustomIMTextEntity;
import com.tencent.custom.EvaluateItemEntity;
import com.tencent.custom.PhotoAlbumItemEntity;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;
import com.tencent.qcloud.tuicore.custom.entity.SystemTipsEntity;
import com.tencent.qcloud.tuikit.tuichat.bean.message.QuoteMessageBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

public abstract class OnItemClickListener {
    public void onMessageLongClick(View view, int position, TUIMessageBean messageInfo) {};

    public void onMessageClick(View view, int position, TUIMessageBean messageInfo) {};

    public void onUserIconClick(View view, int position, TUIMessageBean messageInfo) {};

    public void onUserIconLongClick(View view, int position, TUIMessageBean messageInfo) {};

    public void onReEditRevokeMessage(View view, int position, TUIMessageBean messageInfo) {};

    public void onRecallClick(View view, int position, TUIMessageBean messageInfo) {};

    public  void onReplyMessageClick(View view, int position, QuoteMessageBean messageBean) {}

    public  void onReplyDetailClick(TUIMessageBean messageBean) {}

    public  void onReactOnClick(String emojiId, TUIMessageBean messageBean) {}

    public  void onSendFailBtnClick(View view, int position, TUIMessageBean messageInfo) {};

    public  void onTextSelected(View view, int position, TUIMessageBean messageInfo) {};


    //彭石林新增
    public abstract void onToastVipText(TUIMessageBean messageInfo);

    public abstract void onTextReadUnlock(TextView textView, View view, TUIMessageBean messageInfo);

    public abstract void onTextTOWebView(TUIMessageBean messageInfo);

    public abstract void toUserHome();

    public abstract void openUserImage(PhotoAlbumItemEntity itemEntity);

    public abstract void onClickEvaluate(int position, TUIMessageBean messageInfo, EvaluateItemEntity evaluateItemEntity, boolean more);

    public abstract void onClickCustomText(int position, TUIMessageBean messageInfo, CustomIMTextEntity customIMTextEntity) ;

    public abstract void onClickDialogRechargeShow();

    public abstract void clickToUserMain();

    public abstract void onClickCustomText();

    public void systemTipsOnClick(int position,TUIMessageBean messageInfo, SystemTipsEntity systemTipsEntity) {};

    //DL Add lsf -- 图片点击
    public void onImageClick(TUIMessageBean messageInfo){}

    //自定义 发送红包模块点击触发
    public void onMediaGalleryClick(MediaGalleryEditEntity mediaGalleryEditEntity) {}
}