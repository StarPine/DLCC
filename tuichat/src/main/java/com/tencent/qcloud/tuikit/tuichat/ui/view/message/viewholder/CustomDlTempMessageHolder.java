package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.custom.IMGsonUtils;
import com.tencent.custom.tmp.CustomDlTempMessage;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.dltmpapply.CallingMessageModuleView;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.dltmpapply.MediaGalleryModuleView;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.dltmpapply.SystemTipsMessageModuleView;

/**
 * Author: 彭石林
 * Time: 2022/9/8 11:49
 * Description: 自定义消息模板view渲染层
 */
public class CustomDlTempMessageHolder extends MessageContentHolder{
    private static final String TAG = "DlTempMessageHolder";
    private final FrameLayout flTmpLayout;

    public CustomDlTempMessageHolder(View itemView) {
        super(itemView);
        flTmpLayout = itemView.findViewById(R.id.fl_tmp_layout);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.custom_dl_tmp_message_layout;
    }

    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        if(flTmpLayout!=null){
            flTmpLayout.removeAllViews();
        }
        CustomDlTempMessage customDlTempMessage = IMGsonUtils.fromJson(new String(msg.getCustomElemData()), CustomDlTempMessage.class);
        if (customDlTempMessage == null) {
            defaultLayout(flTmpLayout, msg.isSelf(),position,msg);
            return;
        }
        //判断模块
        if (customDlTempMessage.getContentBody() != null && !TextUtils.isEmpty(customDlTempMessage.getContentBody().getMsgModuleName())) {
            String moduleName = customDlTempMessage.getContentBody().getMsgModuleName();
            if (TextUtils.isEmpty(moduleName)) {
                defaultLayout(flTmpLayout, msg.isSelf(),position,msg);
                return;
            }
            CustomDlTempMessage.MsgBodyInfo msgModuleInfo = customDlTempMessage.getContentBody().getContentBody();
            if (CustomConstants.MediaGallery.MODULE_NAME.equals(moduleName)) { //IM照片、视频模块
                if (msgArea != null) {
                    msgArea.setBackground(null);
                }
                flTmpLayout.setOnLongClickListener(v -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onMessageLongClick(flTmpLayout, position, msg);
                    }
                    return true;
                });
                new MediaGalleryModuleView(this).layoutVariableViews(msg, flTmpLayout, position, msgModuleInfo);
            }else if (CustomConstants.CallingMessage.MODULE_NAME.equals(moduleName)) {
                //禁止通话模块
                new CallingMessageModuleView(this).layoutVariableViews(msg, flTmpLayout, position,msgModuleInfo);
            } else if (CustomConstants.SystemTipsMessage.MODULE_NAME.equals(moduleName)) {
                //系统提示模块
                new SystemTipsMessageModuleView(this).layoutVariableViews(msg,position,flTmpLayout,msgModuleInfo);
            } else {
                //默认展示解析不出的模板提示
                defaultLayout(flTmpLayout, msg.isSelf(),position,msg);
            }
        }else{
            defaultLayout(flTmpLayout,msg.isSelf(),position,msg);
        }

    }

    //默认消息模板
    public void defaultLayout(FrameLayout rootView, boolean isSelf,int position,TUIMessageBean msg){
        if (msgArea != null) {
            msgArea.setBackground(null);
        }
        View defaultView = View.inflate(getContext(), R.layout.tmp_message_default_layout, null);
        FrameLayout frameLayout = defaultView.findViewById(R.id.container);
        TextView textContent = defaultView.findViewById(R.id.tv_content);
        if(frameLayout!=null){
            if (properties.getLeftBubble() != null && properties.getLeftBubble().getConstantState() != null) {
                frameLayout.setBackground(properties.getLeftBubble().getConstantState().newDrawable());
            } else {
                frameLayout.setBackgroundResource(TUIThemeManager.getAttrResId(itemView.getContext(), R.attr.chat_bubble_other_bg));
            }
        }
        if (!isSelf) {
            textContent.setTextColor(textContent.getResources().getColor(TUIThemeManager.getAttrResId(textContent.getContext(), R.attr.chat_other_custom_msg_text_color)));
        } else {
            textContent.setTextColor(textContent.getResources().getColor(TUIThemeManager.getAttrResId(textContent.getContext(), R.attr.chat_self_custom_msg_text_color)));
        }

        String txt = getContext().getString(R.string.dl_tmp_default_text);
        String txt2 = getContext().getString(R.string.dl_tmp_default_text2);
        int whiteLength = txt.length() - txt2.length();
        SpannableString stringBuilder = new SpannableString(txt);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.black));
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getContext().getResources().getColor(R.color.purple_be63));
        stringBuilder.setSpan(whiteSpan, 0, whiteLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(redSpan, whiteLength, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new UnderlineSpan(), whiteLength, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textContent.setText(stringBuilder);
        rootView.setOnLongClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onMessageLongClick(rootView, position, msg);
            }
            return true;
        });
        rootView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Log.e("当前跳转的链接：","https://play.google.com/store/apps/details?id="+TUIChatService.getAppContext().getPackageName());
            Uri content_url = Uri.parse("https://play.google.com/store/apps/details?id="+TUIChatService.getAppContext().getPackageName());
            intent.setData(content_url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            TUIChatService.getAppContext().startActivity(intent);
        });
        rootView.addView(defaultView);

    }



    /**
     * 设置内容字体和颜色
     *
     * @param msg
     */
    public void setBackColor(TUIMessageBean msg, TextView msgBodyText) {
        if (properties.getChatContextFontSize() != 0) {
            msgBodyText.setTextSize(properties.getChatContextFontSize());
        }
        if (msg.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
        }
    }

    public Context getContext(){
        return itemView.getContext();
    }
    public int dp2px(Context context, float dpValue) {
        final float densityScale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * densityScale + 0.5f);
    }
}
