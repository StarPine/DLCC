package com.tencent.qcloud.tuikit.tuiconversation.model;

import com.tencent.qcloud.tuicore.component.interfaces.IUIKitCallback;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import java.util.List;

public class CustomFriendEvent {
    private ConversationEventEnum event;
    private String id;
    private String remark;
    private List<String> conversationId;
    private boolean isGroup;
    private String chatId;
    boolean isChecked;
    private IUIKitCallback<Void> iuiKitCallBack;
    private List<ConversationInfo> conversationList;

    public List<ConversationInfo> getConversationList() {
        return conversationList;
    }

    public void setConversationList(List<ConversationInfo> conversationList) {
        this.conversationList = conversationList;
    }

    public ConversationEventEnum getEvent() {
        return event;
    }

    public void setEvent(ConversationEventEnum event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getConversationId() {
        return conversationId;
    }

    public void setConversationId(List<String> conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public IUIKitCallback<Void> getIuiKitCallBack() {
        return iuiKitCallBack;
    }

    public void setIuiKitCallBack(IUIKitCallback<Void> iuiKitCallBack) {
        this.iuiKitCallBack = iuiKitCallBack;
    }

    public enum ConversationEventEnum{
        deleteConversation,
        clearConversationMessage,
        setConversationTop,
        isTopConversation,
        getUnreadTotal,
        updateTotalUnreadMessageCount,
        onNewConversation,
        onConversationChanged,
        onFriendRemarkChanged,
        deleteConversationListEvent,
        newConversationListEvent
    }
}
