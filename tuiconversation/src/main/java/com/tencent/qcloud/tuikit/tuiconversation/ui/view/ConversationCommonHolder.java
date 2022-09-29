package com.tencent.qcloud.tuikit.tuiconversation.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.component.UnreadCountTextView;
import com.tencent.qcloud.tuicore.util.ConfigManagerUtil;
import com.tencent.qcloud.tuicore.util.DateTimeUtil;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;
import com.tencent.qcloud.tuikit.tuiconversation.R;
import com.tencent.qcloud.tuikit.tuiconversation.TUIConversationService;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.bean.DraftInfo;
import com.tencent.qcloud.tuikit.tuiconversation.util.StringUtil;
import com.tencent.qcloud.tuikit.tuiconversation.util.TUIConversationLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ConversationCommonHolder extends ConversationBaseHolder {

    public ConversationIconView conversationIconView;
    public LinearLayout leftItemLayout;
    public TextView titleText;
    public TextView messageText;
    public TextView timelineText;
    public UnreadCountTextView unreadText;
    public TextView atInfoText;
    public ImageView disturbView;
    public CheckBox multiSelectCheckBox;
    public RelativeLayout messageStatusLayout;
    public ImageView messageSending;
    public ImageView messagefailed;
    private boolean isForwardMode = false;
    public static boolean sexMale = false;

    //彭石林修改
    public final ImageView certification;
    public final ImageView iv_vip;
    public final ImageView iv_game_icon;
    public Context context;

    public ConversationCommonHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
        atInfoText = rootView.findViewById(R.id.conversation_at_msg);
        disturbView = rootView.findViewById(R.id.not_disturb);
        multiSelectCheckBox = rootView.findViewById(R.id.select_checkbox);
        messageStatusLayout = rootView.findViewById(R.id.message_status_layout);
        messagefailed = itemView.findViewById(R.id.message_status_failed);
        messageSending = itemView.findViewById(R.id.message_status_sending);
        certification = rootView.findViewById(R.id.certification);
        iv_vip = rootView.findViewById(R.id.iv_vip);
        iv_game_icon = rootView.findViewById(R.id.iv_game_icon);
    }

    public void setForwardMode(boolean forwardMode) {
        isForwardMode = forwardMode;
    }

    public void layoutViews(ConversationInfo conversation, int position) {
        if (conversation.isTop() && !isForwardMode) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.conversation_item_top_color));
        } else {
            leftItemLayout.setBackgroundColor(Color.WHITE);
        }

        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");
        DraftInfo draftInfo = conversation.getDraft();
        String draftText = "";
        if (draftInfo != null) {
            Gson gson = new Gson();
            HashMap draftJsonMap;
            draftText = draftInfo.getDraftText();
            try {
                draftJsonMap = gson.fromJson(draftInfo.getDraftText(), HashMap.class);
                if (draftJsonMap != null) {
                    draftText = (String) draftJsonMap.get("content");
                }
            } catch (JsonSyntaxException e) {
                TUIConversationLog.e("ConversationCommonHolder", " getDraftJsonMap error ");
            }
        }
        if (draftInfo != null) {
            messageText.setText(draftText);
            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(draftInfo.getDraftTime() * 1000)));
        } else {
            V2TIMMessage lastMessage = conversation.getLastMessage();
            String lastMsgDisplayString = "";

//            HashMap<String, Object> param = new HashMap<>();
//            param.put(TUIConstants.TUIChat.V2TIMMESSAGE, lastMessage);
//            String lastMsgDisplayString = (String) TUICore.callService(TUIConstants.TUIChat.SERVICE_NAME, TUIConstants.TUIChat.METHOD_GET_DISPLAY_STRING, param);
//            if (lastMsgDisplayString != null && TUIChatUtils.isJSON2(lastMsgDisplayString) && lastMsgDisplayString.contains("type")) {
//                lastMsgDisplayString = TUIChatUtils.json2ConversationMsg(lastMsgDisplayString);
//            }
            if (lastMessage != null){
                if (lastMessage.isSelf()) {
                    lastMsgDisplayString = TUIConversationService.getAppContext().getString(R.string.default_message_content3);
                } else {
                    lastMsgDisplayString = TUIConversationService.getAppContext().getString(R.string.default_message_content);
                }
            }

            // 如果最后一条消息是自定义消息, 获取要显示的字符
            if (lastMsgDisplayString != null) {
                messageText.setText(Html.fromHtml(lastMsgDisplayString));
                messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
            }
            if (conversation.getLastMessage() != null) {
                timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(conversation.getLastMessageTime() * 1000)));
            }
        }

        if (conversation.isShowDisturbIcon()) {
            if (conversation.getUnRead() > 0) {
                unreadText.setVisibility(View.VISIBLE);
                unreadText.setText("");

                if (messageText.getText() != null) {
                    String text = messageText.getText().toString();
                    messageText.setText("[" + conversation.getUnRead() + rootView.getContext().getString(R.string.message_num) + "] " + text);
                }
            } else {
                unreadText.setVisibility(View.GONE);
            }
        } else if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (draftInfo != null) {
            atInfoText.setVisibility(View.VISIBLE);
            atInfoText.setText(R.string.drafts);
            atInfoText.setTextColor(Color.RED);

            messageStatusLayout.setVisibility(View.GONE);
            messagefailed.setVisibility(View.GONE);
            messageSending.setVisibility(View.GONE);
        } else {
            if (conversation.getAtInfoText().isEmpty()) {
                atInfoText.setVisibility(View.GONE);
            } else {
                atInfoText.setVisibility(View.VISIBLE);
                atInfoText.setText(conversation.getAtInfoText());
                atInfoText.setTextColor(Color.RED);
            }

            V2TIMMessage lastMessage = conversation.getLastMessage();
            if (lastMessage != null) {
                int status = lastMessage.getStatus();
                if (status == V2TIMMessage.V2TIM_MSG_STATUS_SEND_FAIL) {
                    messageStatusLayout.setVisibility(View.VISIBLE);
                    messagefailed.setVisibility(View.VISIBLE);
                    messageSending.setVisibility(View.GONE);
                } else if (status == V2TIMMessage.V2TIM_MSG_STATUS_SENDING) {
                    messageStatusLayout.setVisibility(View.VISIBLE);
                    messagefailed.setVisibility(View.GONE);
                    messageSending.setVisibility(View.VISIBLE);
                } else {
                    messageStatusLayout.setVisibility(View.GONE);
                    messagefailed.setVisibility(View.GONE);
                    messageSending.setVisibility(View.GONE);
                }
            } else {
                messageStatusLayout.setVisibility(View.GONE);
                messagefailed.setVisibility(View.GONE);
                messageSending.setVisibility(View.GONE);
            }
        }

        conversationIconView.setRadius(mAdapter.getItemAvatarRadius());
        if (mAdapter.getItemDateTextSize() != 0) {
            timelineText.setTextSize(mAdapter.getItemDateTextSize());
        }
        if (mAdapter.getItemBottomTextSize() != 0) {
            messageText.setTextSize(mAdapter.getItemBottomTextSize());
        }
        if (mAdapter.getItemTopTextSize() != 0) {
            titleText.setTextSize(mAdapter.getItemTopTextSize());
        }
        if (!mAdapter.hasItemUnreadDot()) {
            unreadText.setVisibility(View.GONE);
        }

        conversationIconView.setConversation(conversation);
        conversationIconView.setTag(conversation);
        conversationIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.mOnItemAvatarClickListener != null) {
                    ConversationInfo conversation = (ConversationInfo) view.getTag();
                    mAdapter.mOnItemAvatarClickListener.onItemAvatarClick(view, position, conversation);
                }
            }
        });

        if (conversation.isShowDisturbIcon() && !isForwardMode) {
            disturbView.setVisibility(View.VISIBLE);
        } else {
            disturbView.setVisibility(View.GONE);
        }

        if (isForwardMode) {
            messageText.setVisibility(View.GONE);
            timelineText.setVisibility(View.GONE);
            unreadText.setVisibility(View.GONE);
            atInfoText.setVisibility(View.GONE);
            messageStatusLayout.setVisibility(View.GONE);
            messagefailed.setVisibility(View.GONE);
            messageSending.setVisibility(View.GONE);
        }


        //彭石林修改
        //待获取用户资料的用户列表
        List<String> users = new ArrayList<String>();

        users.add(conversation.getId());
        //获取用户资料
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                for (V2TIMUserFullInfo res : v2TIMUserFullInfos) {

                    int level = res.getLevel();
                    String gameUrl = "";
                    if (!ConfigManagerUtil.getInstance().getGameUrl(res.getRole() + "").isEmpty()) {
                        gameUrl = StringUtil.IMAGE_BASE_URL + ConfigManagerUtil.getInstance().getGameUrl(res.getRole() + "");
                        Glide.with(context).load(gameUrl).into(iv_game_icon);
                        iv_game_icon.setVisibility(View.VISIBLE);
                    }else{
                        iv_game_icon.setVisibility(View.GONE);
                    }

//                     certification;
                    // iv_vip;
                    if (level == 1) { //vip
                        iv_vip.setImageResource(R.drawable.ic_vip);
                        iv_vip.setVisibility(View.VISIBLE);
                        certification.setVisibility(View.GONE);
                    } else if (level == 2) { //真人
                        certification.setImageResource(R.drawable.ic_real_man);
                        iv_vip.setVisibility(View.GONE);
                        certification.setVisibility(View.VISIBLE);
                    } else if (level == 3 || level == 5) {
                        iv_vip.setImageResource(R.drawable.ic_goddess);
                        certification.setVisibility(View.GONE);
                        iv_vip.setVisibility(View.VISIBLE);
                    } else if (level == 4) {//真人加VIP只显示VIP
                        iv_vip.setImageResource(R.drawable.ic_vip);
                        iv_vip.setVisibility(View.VISIBLE);
                    } else {
                        iv_vip.setVisibility(View.GONE);
                        certification.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e("获取用户信息失败", "getUsersProfile failed: " + code + " desc");
            }
        });
    }
}
