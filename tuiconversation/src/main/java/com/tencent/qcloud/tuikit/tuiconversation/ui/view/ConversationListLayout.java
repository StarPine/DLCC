package com.tencent.qcloud.tuikit.tuiconversation.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tencent.qcloud.tuicore.component.CustomLinearLayoutManager;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.model.CustomConfigSetting;
import com.tencent.qcloud.tuikit.tuiconversation.ui.interfaces.IConversationListAdapter;
import com.tencent.qcloud.tuikit.tuiconversation.ui.interfaces.IConversationListLayout;
import com.tencent.qcloud.tuikit.tuiconversation.presenter.ConversationPresenter;

/**
* @Desc TODO(聊天页面RecyclerView)
* @author 彭石林
* @parame
* @return
* @Date 2022/8/13
*/
public class ConversationListLayout extends RecyclerView implements IConversationListLayout {

    private ConversationListAdapter mAdapter;
    private ConversationPresenter presenter;
    //好友列表
    private boolean isFriend;

    public ConversationListLayout(Context context) {
        this(context,null);
    }

    public ConversationListLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConversationListLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setPresenter(ConversationPresenter presenter) {
        this.presenter = presenter;
    }

    public void init() {
        setLayoutFrozen(false);
        setItemViewCacheSize(500);
        setHasFixedSize(true);
        setFocusableInTouchMode(false);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);
        SimpleItemAnimator animator = (SimpleItemAnimator) getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }
    }

    @Override
    public void setBackground(int resId) {
        setBackgroundColor(resId);
    }

    @Override
    public void disableItemUnreadDot(boolean flag) {
        mAdapter.disableItemUnreadDot(flag);
    }

    @Override
    public void setItemAvatarRadius(int radius) {
        mAdapter.setItemAvatarRadius(radius);
    }

    @Override
    public void setItemTopTextSize(int size) {
        mAdapter.setItemTopTextSize(size);
    }

    @Override
    public void setItemBottomTextSize(int size) {
        mAdapter.setItemBottomTextSize(size);
    }

    @Override
    public void setItemDateTextSize(int size) {
        mAdapter.setItemDateTextSize(size);
    }

    @Override
    public ConversationListLayout getListLayout() {
        return this;
    }

    @Override
    public ConversationListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(IConversationListAdapter adapter) {
        if (adapter instanceof ConversationListAdapter) {
            super.setAdapter((ConversationListAdapter) adapter);
            mAdapter = (ConversationListAdapter) adapter;
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemAvatarClickListener(OnItemAvatarClickListener listener) {
        mAdapter.setOnItemAvatarClickListener(listener);
    }

    @Override
    public void setBanConversationDelListener(BanConversationDelListener listener) {
        mAdapter.setBanConversationDelListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mAdapter.setOnItemLongClickListener(listener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ConversationInfo messageInfo);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position, ConversationInfo messageInfo);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            if (layoutManager == null) {
                return;
            }
            int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();

            if (mAdapter != null) {
                if (CustomConfigSetting.conversationAstrictCount > 0 && mAdapter.getItemCount() >= CustomConfigSetting.conversationAstrictCount) {
                    return;
                }
                boolean loadFlag = isFriend ? isFriendCompleted() : isLoadCompleted();
                //上拉刷新
                if (lastPosition == mAdapter.getItemCount() - 1 && !loadFlag) {
                    mAdapter.onLoadingStateChanged(true);
                    if (presenter != null) {
                        presenter.loadMoreConversation();
                    }else{
                        mAdapter.onLoadingStateChanged(false);
                    }
                }

            }
        }
    }

    public void loadConversation(long nextSeq,boolean isFriend) {
        if (presenter != null) {
            this.isFriend = isFriend;
            //presenter.loadConversation(nextSeq);
            //查询当前用户所有好友列表--附带查询所有会话列表数据
            presenter.getFriendshipList(nextSeq,isFriend);
        }
    }

    public void scrollToTop() {
        if (getAdapter() != null) {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            int itemCount = getAdapter().getItemCount();
            if (layoutManager instanceof LinearLayoutManager && itemCount > 0) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, 0);
            }
        }
    }

    boolean isLoadCompleted(){
        if (presenter != null) {
            return presenter.isLoadFinished();
        }
        return false;
    }

    boolean isFriendCompleted(){
        if (presenter != null) {
            return presenter.isFriendLoadFinished();
        }
        return false;
    }



    //彭石林新增。会话列表头像点击
    public interface OnItemAvatarClickListener{
        void onItemAvatarClick(View view, int position, ConversationInfo messageInfo);
    }

    public interface BanConversationDelListener{
        void banConversationDel();
    }


}