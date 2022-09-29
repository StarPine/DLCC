package com.tencent.qcloud.tuikit.tuiconversation.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.component.TitleBarLayout;
import com.tencent.qcloud.tuicore.component.interfaces.IUIKitCallback;
import com.tencent.qcloud.tuikit.tuiconversation.R;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.model.CustomConfigSetting;
import com.tencent.qcloud.tuikit.tuiconversation.presenter.ConversationPresenter;
import com.tencent.qcloud.tuikit.tuiconversation.ui.interfaces.IConversationLayout;
import com.tencent.qcloud.tuikit.tuiconversation.ui.interfaces.IConversationListAdapter;

import java.util.HashMap;
import java.util.Map;

public class ConversationLayout extends RelativeLayout implements IConversationLayout {

    private ConversationListLayout mConversationList;
    private ConversationPresenter presenter;
    public ConversationLayout(Context context) {
        this(context,null);
    }

    public ConversationLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConversationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPresenter(ConversationPresenter presenter) {
        this.presenter = presenter;
        if (mConversationList != null) {
            mConversationList.setPresenter(presenter);
        }
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.conversation_layout, this);
        mConversationList = findViewById(R.id.conversation_list);
    }

    public void initDefault(boolean isFriend) {
        final ConversationListAdapter adapter = new ConversationListAdapter();
        initSearchView(adapter);
        mConversationList.setAdapter((IConversationListAdapter) adapter);
        if (presenter != null) {
            if(isFriend){
                presenter.setFriendshipAdapter(adapter);
            }else{
                presenter.setAdapter(adapter);
            }
        }
        //可见数量大于0 才会进行拉取消息
        if (CustomConfigSetting.conversationAstrictCount > 0) {
            mConversationList.loadConversation(0,isFriend);
        }
    }

    public void initSearchView(ConversationListAdapter adapter) {
        Map<String, Object> param = new HashMap<>();
        param.put(TUIConstants.TUIConversation.CONTEXT, getContext());
        Map<String, Object> searchExtension = TUICore.getExtensionInfo(TUIConstants.TUIConversation.EXTENSION_SEARCH, param);
        if (searchExtension != null) {
            View searchView = (View) searchExtension.get(TUIConstants.TUIConversation.SEARCH_VIEW);
            if (searchView != null) {
                adapter.setSearchView(searchView);
            }
        }
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    @Override
    public void setConversationTop(ConversationInfo conversation, IUIKitCallback callBack) {
        if (presenter != null) {
            presenter.setConversationTop(conversation, callBack);
        }
    }

    @Override
    public void deleteConversation(ConversationInfo conversation) {
        if (presenter != null) {
            presenter.deleteConversation(conversation);
        }
    }

    /**
     * 删除所有已封号账号
     */
    public void deleteAllBannedConversation() {
        if (presenter != null) {
            presenter.deleteAllBannedConversation();
        }
    }

    @Override
    public void clearConversationMessage(ConversationInfo conversation) {
        if (presenter != null) {
            presenter.clearConversationMessage(conversation);
        }
    }

    @Override
    public TitleBarLayout getTitleBar() {
        return null;
    }
}
