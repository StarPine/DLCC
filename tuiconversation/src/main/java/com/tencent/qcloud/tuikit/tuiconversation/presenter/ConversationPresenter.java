package com.tencent.qcloud.tuikit.tuiconversation.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendshipListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.component.interfaces.IUIKitCallback;
import com.tencent.qcloud.tuicore.util.ThreadHelper;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuiconversation.TUIConversationService;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.interfaces.ConversationEventListener;
import com.tencent.qcloud.tuikit.tuiconversation.model.ConversationProvider;
import com.tencent.qcloud.tuikit.tuiconversation.ui.interfaces.IConversationListAdapter;
import com.tencent.qcloud.tuikit.tuiconversation.ui.view.ConversationListAdapter;
import com.tencent.qcloud.tuikit.tuiconversation.util.ConversationUtils;
import com.tencent.qcloud.tuikit.tuiconversation.util.TUIConversationLog;
import com.tencent.qcloud.tuikit.tuiconversation.util.TUIConversationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ConversationPresenter {
    private static final String TAG = ConversationPresenter.class.getSimpleName();

    private final static int GET_CONVERSATION_COUNT = 100;
    private int needDelCount = 0;//需要删除个数
    private boolean isDelBanCovversation = false;//删除封号会话


    private final ConversationProvider provider;

    private IConversationListAdapter adapter;
    //会话列表储存
    private final List<ConversationInfo> loadedConversationInfoList = new ArrayList<>();
    //好友列表存储
    private IConversationListAdapter friendshipAdapter;
    //好友列表储存
    private final List<ConversationInfo> loadedFriendshipInfoList = new ArrayList<>();
    //好友ID存储
    private final List<String> loadedFriendshipInfoIdList = new ArrayList<>();
    //当前数据源是否是好友列表
    private boolean isFriendConversation = false;
    //好友会话最大页码 --最大会话数量
    private int friendMaxPaging = 0;
    //好友会话当前页吗
    private int friendCurrentPaging = 1;
    //自定义加载消息回调、未读数统计
    private LoadConversationCallback loadConversationCallback;
    //监听好友列表变更
    //private ContactEventListener friendListListener;


    public LoadConversationCallback getLoadConversationCallback() {
        return loadConversationCallback;
    }

    public void setLoadConversationCallback(LoadConversationCallback loadConversationCallback) {
        this.loadConversationCallback = loadConversationCallback;
    }

    public boolean isFriendConversation() {
        return isFriendConversation;
    }

    public void setFriendConversation(boolean friendConversation) {
        isFriendConversation = friendConversation;
    }

    private long totalUnreadCount;

    public ConversationPresenter() {
        provider = new ConversationProvider();
    }

    public void initIMListener() {
        V2TIMManager.getConversationManager().addConversationListener(new V2TIMConversationListener() {

            @Override
            public void onNewConversation(List<V2TIMConversation> conversationList) {
                if (!conversationList.isEmpty()) {
                    List<ConversationInfo> conversationInfoList = ConversationUtils.convertV2TIMConversationList(conversationList);
                    isFriendConversationList(conversationInfoList);
                    ConversationPresenter.this.onNewConversation(conversationInfoList);
                }

            }

            @Override
            public void onConversationChanged(List<V2TIMConversation> conversationList) {
                if (!conversationList.isEmpty()) {
                    List<ConversationInfo> conversationInfoList = ConversationUtils.convertV2TIMConversationList(conversationList);
                    isFriendConversationList(conversationInfoList);
                    ConversationPresenter.this.onConversationChanged(conversationInfoList);
                }
            }

            @Override
            public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                ConversationPresenter.this.updateTotalUnreadMessageCount(totalUnreadCount);
                HashMap<String, Object> param = new HashMap<>();
                param.put(TUIConstants.TUIConversation.TOTAL_UNREAD_COUNT, totalUnreadCount);
                TUICore.notifyEvent(TUIConstants.TUIConversation.EVENT_UNREAD, TUIConstants.TUIConversation.EVENT_SUB_KEY_UNREAD_CHANGED, param);
            }
        });
        //添加关系链监听器
        V2TIMManager.getFriendshipManager().addFriendListener(new V2TIMFriendshipListener() {
            /**
             * @Desc TODO(好友新增通知)
             * @author 彭石林
             * @parame [users]
             * @Date 2022/8/15
             */
            @Override
            public void onFriendListAdded(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                if(!v2TIMFriendInfos.isEmpty()){
                    final List<String> urlList = new ArrayList<>();
                    for (V2TIMFriendInfo v2TIMFriendInfo : v2TIMFriendInfos){
                        String userId = TUIConstants.TUIConversation.CONVERSATION_C2C_PREFIX + v2TIMFriendInfo.getUserID() ;
                        urlList.add(userId);
                    }
                    ConversationPresenter.this.newConversationListEvent(urlList);
                }
            }

            /**
             * @Desc (
             * 好友删除通知 ， ， 两种情况会收到这个回调 ：
             *自己删除好友 （ 单向和双向删除都会收到回调 ）
             *好友把自己删除 （ 双向删除会收到 ）)
             * @author 彭石林
             * @parame [userList]
             * @Date 2022/8/15
             */
            @Override
            public void onFriendListDeleted(List<String> userList) {
                if(!userList.isEmpty()){
                    List<String> removeInfoList = new ArrayList<>();
                    for (String infoData : userList){
                        String userId = TUIConstants.TUIConversation.CONVERSATION_C2C_PREFIX + infoData ;
                        removeInfoList.add(userId);
                    }
                    ConversationPresenter.this.deleteConversationListEvent(removeInfoList);
                }
            }
            /**
             * @Desc(
             * 黑名单删除通知
             * )
             * @author 彭石林
             * @parame [userList]
             * @return void
             * @Date 2022/8/15
             */
            @Override
            public void onBlackListDeleted(List<String> userList) {

            }
        });
    }

    public void setConversationListener() {
        ConversationEventListener conversationEventListener = new ConversationEventListener() {
            @Override
            public void deleteConversation(String chatId, boolean isGroup) {
                if(isFriendConversation){
                    ConversationPresenter.this.deleteConversation(chatId, loadedFriendshipInfoList);
                }else{
                    ConversationPresenter.this.deleteConversation(chatId, loadedConversationInfoList);
                }
            }

            @Override
            public void clearConversationMessage(String chatId, boolean isGroup) {
                ConversationPresenter.this.clearConversationMessage(chatId, isGroup);
            }

            @Override
            public void deleteConversation(String conversationId) {
                if(isFriendConversation){
                    ConversationPresenter.this.deleteConversation(conversationId, loadedFriendshipInfoList);
                }else{
                    ConversationPresenter.this.deleteConversation(conversationId, loadedConversationInfoList);
                }
            }

            @Override
            public void setConversationTop(String chatId, boolean isChecked, IUIKitCallback<Void> iuiKitCallBack) {
                if(isFriendConversation){
                    ConversationPresenter.this.setConversationTop(chatId, isChecked, iuiKitCallBack,loadedFriendshipInfoList);
                }else{
                    ConversationPresenter.this.setConversationTop(chatId, isChecked, iuiKitCallBack, loadedConversationInfoList);
                }
            }

            @Override
            public boolean isTopConversation(String chatId) {
                if(isFriendConversation){
                    return ConversationPresenter.this.isTopConversation(chatId, loadedFriendshipInfoList);
                }else{
                    return ConversationPresenter.this.isTopConversation(chatId, loadedConversationInfoList);
                }
            }

            @Override
            public long getUnreadTotal() {
                return totalUnreadCount;
            }

            @Override
            public void updateTotalUnreadMessageCount(long count) {
                ConversationPresenter.this.updateTotalUnreadMessageCount(count);
            }

            @Override
            public void onNewConversation(List<ConversationInfo> conversationList) {
                isFriendConversationList(conversationList);
                ConversationPresenter.this.onNewConversation(conversationList);
            }

            @Override
            public void onConversationChanged(List<ConversationInfo> conversationList) {
                //部分会话更新
                isFriendConversationList(conversationList);
                ConversationPresenter.this.onConversationChanged(conversationList);
            }

            @Override
            public void onFriendRemarkChanged(String id, String remark) {
                ConversationPresenter.this.onFriendRemarkChanged(id, remark);
            }

            @Override
            public void deleteConversationListEvent(List<String> conversationId) {
                ConversationPresenter.this.deleteConversationListEvent(conversationId);
            }

            @Override
            public void newConversationListEvent(List<String> conversationId) {
                ConversationPresenter.this.newConversationListEvent(conversationId);
            }
        };
        TUIConversationService.getInstance().setConversationEventListener(conversationEventListener);
    }

    //移除好友列表的会话
    public void deleteConversationListEvent(List<String> conversationIdList) {
        if(isFriendConversation){
            ConversationPresenter.this.deleteConversationListChange(conversationIdList,friendshipAdapter,loadedFriendshipInfoList);
        }else{
            addNewConversation(conversationIdList,false);
        }

    }
    //新的好友添加通知
    public void newConversationListEvent(List<String> conversationIdList) {
        //如果不是再好友列表。那么原有会话列表应该执行删除
        if(!isFriendConversation){
            ConversationPresenter.this.deleteConversationListChange(conversationIdList,adapter,loadedConversationInfoList);
        }else{
            addNewConversation(conversationIdList,true);
        }
    }
    /**
    * @Desc TODO(删除好友会话列表)
    * @author 彭石林
    * @parame [dataConversationInfoList]
    * @return void
    * @Date 2022/8/15
    */
    public void deleteConversationListChange(List<String> dataConversationInfoList,IConversationListAdapter iAdapter, List<ConversationInfo> deleteConversationList){
        //列表执行删除
        if(!deleteConversationList.isEmpty()){
            int friendConversationSize = deleteConversationList.size();
            List<ConversationInfo> removeConversationInfoList = new ArrayList<>();
            for (String conversationLey : dataConversationInfoList) {
                for (int i = 0; i < friendConversationSize; i++) {
                    ConversationInfo removeData = deleteConversationList.get(i);
                    if(conversationLey.equals(removeData.getConversationId())){
                        removeConversationInfoList.add(removeData);
                        if(iAdapter!=null){
                            iAdapter.onItemRemoved(i);
                        }
                    }
                }
            }
            if(!removeConversationInfoList.isEmpty()){
                deleteConversationList.removeAll(removeConversationInfoList);
            }

        }
        if(isFriendConversation){
            if(!loadedFriendshipInfoIdList.isEmpty()){
                //删除好友列表数据
                loadedFriendshipInfoIdList.removeAll(dataConversationInfoList);
            }
        }else{
            loadedFriendshipInfoIdList.addAll(dataConversationInfoList);
        }


        if(deleteConversationList.isEmpty()){
            if(loadConversationCallback!=null){
                loadConversationCallback.isConversationEmpty(true);
            }
        }
        busConversationCount(deleteConversationList);
    }
    /**
    * @Desc TODO(根据c2cId进行新的会话查询)
    * @author 彭石林
    * @parame [dataConversationInfoList, isAddConversation]
    * @return void
    * @Date 2022/8/15
    */
    public void addNewConversation(List<String> dataConversationInfoList,boolean isAddConversation){
        if(isAddConversation){
            loadedFriendshipInfoIdList.addAll(dataConversationInfoList);
        }else{
            loadedFriendshipInfoIdList.removeAll(dataConversationInfoList);
        }
        provider.getFriendShipConversationList(dataConversationInfoList, new IUIKitCallback<List<ConversationInfo>>() {
            @Override
            public void onSuccess(List<ConversationInfo> dataConversationInfo) {
                isFriendConversationList(dataConversationInfo);
                ConversationPresenter.this.onNewConversation(dataConversationInfo);
            }
            @Override
            public void onError(int errCode, String errMsg, List<ConversationInfo> data) {
            }
        });
    }
    /**
    * @Desc TODO(效验新消息是否存在是好友列表数据)
    * @author 彭石林
    * @parame [conversationList]
    * @return void
    * @Date 2022/8/13
    */
    public void isFriendConversationList(List<ConversationInfo> conversationList){
        if(conversationList==null){
            return;
        }
        Iterator<ConversationInfo> iterator = conversationList.iterator();
        //去重后的数据 如果已经是好友关系了。那么讲不会存在会话列表里面
        while(iterator.hasNext()){
            ConversationInfo update = iterator.next();
            if (!ConversationUtils.isNeedUpdate(update)) {
                if(isFriendConversation){
                    if(!loadedFriendshipInfoIdList.isEmpty()){
                        //如果好友列表存在
                        if(!loadedFriendshipInfoIdList.contains(update.getConversationId())){
                            iterator.remove();
                        }
                    }else{
                        iterator.remove();
                    }
                }else{
                    if(!loadedFriendshipInfoIdList.isEmpty()){
                        if(loadedFriendshipInfoIdList.contains(update.getConversationId()) || update.getConversationId().contains("customer")){
                            iterator.remove();
                        }
                    }else{
                        if(update.getConversationId().contains("customer")){
                            iterator.remove();
                        }
                    }
                }
            }
        }

        //TODO 非好友列表，判断数据是否为空（逻辑判断还有问题）
//        if (conversationList.size() <= 0 && !isFriendConversation){
//            if(loadConversationCallback!=null){
//                loadConversationCallback.isConversationEmpty(true);
//            }
//        }
    }

    public void setAdapter(IConversationListAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 加载会话信息
     *
     * @param nextSeq 分页拉取的游标，第一次默认取传 0，后续分页拉传上一次分页拉取成功回调里的 nextSeq
     */
    public void loadConversation(long nextSeq) {
        TUIConversationLog.i(TAG, "loadConversation");
        provider.loadConversation(nextSeq, GET_CONVERSATION_COUNT, new IUIKitCallback<List<ConversationInfo>>() {
            @Override
            public void onSuccess(List<ConversationInfo> conversationInfoList) {
                isFriendConversationList(conversationInfoList);
                onLoadConversationCompleted(conversationInfoList);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if(loadConversationCallback!=null && nextSeq==0){
                    loadConversationCallback.isConversationEmpty(true);
                }
                adapterLoadingStateChanged(adapter);
            }
        });
    }

    public void adapterLoadingStateChanged(IConversationListAdapter iConversationListAdapter){
        if(iConversationListAdapter != null){
            iConversationListAdapter.onLoadingStateChanged(false);
        }
    }
    //分页拉取会话列表数据
    public void loadMoreConversation() {
        if(isFriendConversation){
            loadMoreFriendConversation();
        }else{
            provider.loadMoreConversation(GET_CONVERSATION_COUNT, new IUIKitCallback<List<ConversationInfo>>() {
                @Override
                public void onSuccess(List<ConversationInfo> data) {
                    isFriendConversationList(data);
                    onLoadConversationCompleted(data);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    adapterLoadingStateChanged(adapter);
                }
            });
        }
    }
    //分页拉取好友会话列表数据
    public void loadMoreFriendConversation(){
        //当前页码==最大页码的时候
        if (friendCurrentPaging > friendMaxPaging){
            adapterLoadingStateChanged(friendshipAdapter);
            return;
        }
        //上次分页最大拉取数量
        int oldFriendSize = friendCurrentPaging * GET_CONVERSATION_COUNT;
        friendCurrentPaging++;
        //当前需要拉取数量
        int newFriendSize = friendCurrentPaging * GET_CONVERSATION_COUNT;
        int getQryCount = GET_CONVERSATION_COUNT;
        int friendshipInfoIdSize = loadedFriendshipInfoIdList.size();
        //当前拉取数量超过 总好友数量
        if(newFriendSize > friendshipInfoIdSize){
            getQryCount = newFriendSize - (newFriendSize - friendshipInfoIdSize);
        }
        List<String> friendList = new ArrayList<>();
        for (int i = oldFriendSize; i < getQryCount; i++){
            friendList.add(loadedFriendshipInfoIdList.get(i));
        }
        provider.getFriendShipConversationList(friendList, new IUIKitCallback<List<ConversationInfo>>() {
            @Override
            public void onSuccess(List<ConversationInfo> dataConversationInfo) {
                if(dataConversationInfo!=null && !dataConversationInfo.isEmpty()){
                    if(friendshipAdapter!=null){
                        isFriendConversationList(dataConversationInfo);
                        ConversationPresenter.this.onNewConversation(dataConversationInfo);
                    }
                }
                adapterLoadingStateChanged(friendshipAdapter);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                adapterLoadingStateChanged(friendshipAdapter);
            }
        });
    }

    private void onLoadConversationCompleted(List<ConversationInfo> conversationInfoList) {
        onNewConversation(conversationInfoList);
        if(!isFriendConversation){
            adapterLoadingStateChanged(adapter);
        }
        provider.getTotalUnreadMessageCount(new IUIKitCallback<Long>() {
            @Override
            public void onSuccess(Long data) {
                totalUnreadCount = data.intValue();
                updateUnreadTotal(totalUnreadCount);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    public boolean isLoadFinished() {
        return provider.isLoadFinished();
    }

    public boolean isFriendLoadFinished() {
        return (friendCurrentPaging > friendMaxPaging);
    }

    /**
     * 有新的会话
     * @param conversationInfoList 新的会话列表
     */
    public void onNewConversation(List<ConversationInfo> conversationInfoList) {
        TUIConversationLog.i(TAG, "onNewConversation conversations:" + conversationInfoList.size());
        if (conversationInfoList.size() == 0) {
            return;
        }
        Collections.sort(conversationInfoList);
        //判断当前是否是好友列表页面
        if(isFriendConversation){
            CustomNewConversationList(conversationInfoList,friendshipAdapter,loadedFriendshipInfoList);
        }else{
            CustomNewConversationList(conversationInfoList,adapter,loadedConversationInfoList);
        }

    }
    /**
    * @Desc TODO(新消息进入会话列表回调处理)
    * @author 彭石林
    * @parame [inflows, iAdapter, loadedConversationInfo]
    * @return void
    * @Date 2022/8/13
    */
    public void CustomNewConversationList(List<ConversationInfo> inflows,IConversationListAdapter iAdapter,List<ConversationInfo> loadedConversationInfo){
        List<ConversationInfo> exists = new ArrayList<>();
        Iterator<ConversationInfo> iterator = inflows.iterator();
        while(iterator.hasNext()) {
            ConversationInfo update = iterator.next();
            for (int i = 0; i < loadedConversationInfo.size(); i++) {
                ConversationInfo cacheInfo = loadedConversationInfo.get(i);
                // 去重
                if (cacheInfo.getConversationId().equals(update.getConversationId())) {
                    loadedConversationInfo.set(i, update);
                    iterator.remove();
                    exists.add(update);
                    break;
                }
            }
        }

        // 对新增会话排序，避免插入 recyclerview 时错乱
        Collections.sort(inflows);
        loadedConversationInfo.addAll(inflows);
        if (iAdapter != null) {
            Collections.sort(loadedConversationInfo);
            iAdapter.onDataSourceChanged(loadedConversationInfo);
            for (ConversationInfo info : inflows) {
                int index = loadedConversationInfo.indexOf(info);
                if (index != -1) {
                    iAdapter.onItemInserted(index);
                }
            }

            for (ConversationInfo info : exists) {
                int index = loadedConversationInfo.indexOf(info);
                if (index != -1) {
                    iAdapter.onItemChanged(index);
                }
            }
        }
        if(loadConversationCallback!=null && !loadedConversationInfo.isEmpty()){
            loadConversationCallback.isConversationEmpty(false);
        }
        busConversationCount(loadedConversationInfo);
    }

    /**
     * 部分会话刷新（包括多终端已读上报同步）
     *
     * @param conversationInfoList 需要刷新的会话列表
     */
    public void onConversationChanged(List<ConversationInfo> conversationInfoList) {
        TUIConversationLog.i(TAG, "onConversationChanged conversations:" + conversationInfoList.size());
        if (conversationInfoList.size() == 0) {
            return;
        }
        Collections.sort(conversationInfoList);

        //判断当前是否是好友列表页面
        if(isFriendConversation){
            CustomConversationChangedList(conversationInfoList,friendshipAdapter,loadedFriendshipInfoList);
        }else{
            CustomConversationChangedList(conversationInfoList,adapter,loadedConversationInfoList);
        }
    }
    /**
    * @Desc TODO(自定义刷新普通列表 or 好友列表)
    * @author 彭石林
    * @parame [inflows, indexMap, iAdapter, loadedConversationInfo]
    * @return void
    * @Date 2022/8/13
    */
    private void  CustomConversationChangedList(List<ConversationInfo> inflows,IConversationListAdapter iAdapter,List<ConversationInfo> loadedConversationInfo) {
        HashMap<ConversationInfo, Integer> indexMap = new HashMap<>();
        for (int j = 0; j < inflows.size(); j++) {
            ConversationInfo update = inflows.get(j);
            for (int i = 0; i < loadedConversationInfo.size(); i++) {
                ConversationInfo cacheInfo = loadedConversationInfo.get(i);
                //单个会话刷新时找到老的会话数据，替换
                if (cacheInfo.getConversationId().equals(update.getConversationId())) {
                    loadedConversationInfo.set(i, update);
                    indexMap.put(update, i);
                    break;
                }
            }
        }
        if (iAdapter != null) {
            Collections.sort(loadedConversationInfo);
            iAdapter.onDataSourceChanged(loadedConversationInfo);
            int minRefreshIndex = Integer.MAX_VALUE;
            int maxRefreshIndex = Integer.MIN_VALUE;
            for (ConversationInfo info : inflows) {
                Integer oldIndexObj = indexMap.get(info);
                if (oldIndexObj == null) {
                    continue;
                }
                int oldIndex = oldIndexObj;
                int newIndex = loadedConversationInfo.indexOf(info);
                if (newIndex != -1) {
                    minRefreshIndex = Math.min(minRefreshIndex, Math.min(oldIndex, newIndex));
                    maxRefreshIndex = Math.max(maxRefreshIndex, Math.max(oldIndex, newIndex));
                }
            }
            int count;
            if (minRefreshIndex == maxRefreshIndex) {
                count = 1;
            } else {
                count = maxRefreshIndex - minRefreshIndex + 1;
            }
            if (count > 0 && maxRefreshIndex >= minRefreshIndex) {
                iAdapter.onItemRangeChanged(minRefreshIndex, count);
            }
            if(loadConversationCallback!=null && !loadedConversationInfo.isEmpty()){
                loadConversationCallback.isConversationEmpty(false);
            }
            busConversationCount(loadedConversationInfo);
        }
    }

    public void updateTotalUnreadMessageCount(long totalUnreadCount) {
        this.totalUnreadCount = (int) totalUnreadCount;
        updateUnreadTotal(this.totalUnreadCount);
    }

    /**
     * 更新会话未读计数
     *
     * @param unreadTotal
     */
    public void updateUnreadTotal(long unreadTotal) {
        TUIConversationLog.i(TAG, "updateUnreadTotal:" + unreadTotal);
        totalUnreadCount = unreadTotal;
        HashMap<String, Object> param = new HashMap<>();
        param.put(TUIConstants.TUIConversation.TOTAL_UNREAD_COUNT, totalUnreadCount);
        TUICore.notifyEvent(TUIConstants.TUIConversation.EVENT_UNREAD, TUIConstants.TUIConversation.EVENT_SUB_KEY_UNREAD_CHANGED, param);
        busConversationCount(isFriendConversation?loadedFriendshipInfoList:loadedConversationInfoList);
    }

    /**
     * 将某个会话置顶
     *
     * @param conversation
     */
    public void setConversationTop(final ConversationInfo conversation, final IUIKitCallback<Void> callBack) {
        TUIConversationLog.i(TAG, "setConversationTop" + "|conversation:" + conversation);
        final boolean setTop = !conversation.isTop();

        provider.setConversationTop(conversation.getConversationId(), setTop, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                conversation.setTop(setTop);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIConversationLog.e(TAG, "setConversationTop code:" + errCode + "|desc:" + errMsg);
                if (callBack != null) {
                    callBack.onError("setConversationTop", errCode, errMsg);
                }
            }
        });

    }

    /**
     * 会话置顶操作
     *
     * @param id    会话ID
     * @param isTop 是否置顶
     */
    public void setConversationTop(String id, final boolean isTop, final IUIKitCallback<Void> callBack,List<ConversationInfo> conversationInfoList) {
        TUIConversationLog.i(TAG, "setConversationTop id:" + id + "|isTop:" + isTop);
        ConversationInfo conversation = null;
        for (int i = 0; i < conversationInfoList.size(); i++) {
            ConversationInfo info = conversationInfoList.get(i);
            if (info.getId().equals(id)) {
                conversation = info;
                break;
            }
        }
        if (conversation == null) {
            return;
        }
        final ConversationInfo conversationInfo = conversation;
        final String conversationId = conversation.getConversationId();
        provider.setConversationTop(conversationId, isTop, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                conversationInfo.setTop(isTop);
                TUIConversationUtils.callbackOnSuccess(callBack, null);
            }

            @Override
            public void onError(String module, int code, String desc) {
                TUIConversationLog.e(TAG, "setConversationTop code:" + code + "|desc:" + desc);
                if (callBack != null) {
                    callBack.onError("setConversationTop", code, desc);
                }
            }
        });
    }

    public boolean isTopConversation(String conversationID,List<ConversationInfo> conversationInfoList) {
        for (int i = 0; i < conversationInfoList.size(); i++) {
            ConversationInfo info = conversationInfoList.get(i);
            if (info.getId().equals(conversationID)) {
                return info.isTop();
            }
        }
        return false;
    }

    /**
     * 删除会话，会将本地会话数据从imsdk中删除
     *
     * @param conversation 会话信息
     */
    public void deleteConversation(ConversationInfo conversation) {
        TUIConversationLog.i(TAG, "deleteConversation conversation:" + conversation);
        if (conversation == null) {
            return;
        }
        List<ConversationInfo> currentList = isFriendConversation ? loadedFriendshipInfoList : loadedConversationInfoList;
        IConversationListAdapter currentAdapter = isFriendConversation ? friendshipAdapter : adapter;
        provider.deleteConversation(conversation.getConversationId(), new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                int index = currentList.indexOf(conversation);
                boolean isRemove = currentList.remove(conversation);
                if (currentAdapter != null && isRemove && index != -1) {
                    if (isDelBanCovversation){
                        needDelCount--;
                    }
                    currentAdapter.onItemRemoved(index);
                }
                if (needDelCount == 0){
                    isDelBanCovversation = false;
                    ((ConversationListAdapter)currentAdapter).banConversationDel();
                }
                if(currentList.isEmpty()){
                    if(loadConversationCallback!=null){
                        loadConversationCallback.isConversationEmpty(true);
                    }
                }
                busConversationCount(isFriendConversation?loadedFriendshipInfoList:loadedConversationInfoList);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                needDelCount--;
                if (needDelCount == 0){
                    isDelBanCovversation = false;
                    ((ConversationListAdapter)currentAdapter).banConversationDel();
                }
            }
        });

    }

    /**
     * DL add lsf
     * 删除所有封号会话
     */
    public void deleteAllBannedConversation() {
        isDelBanCovversation = true;
        List<ConversationInfo> dataSource = null;
        if(isFriendConversation){
            dataSource = ((ConversationListAdapter) friendshipAdapter).getDataSource();
        }else{
            dataSource = ((ConversationListAdapter) adapter).getDataSource();
        }
        if(dataSource==null){
            return;
        }
        List<String> users = new ArrayList<>();

        for (ConversationInfo conversationInfo : dataSource) {
            users.add(conversationInfo.getId());
        }

        //获取用户资料
        V2TIMManager.getInstance().getUsersInfo(users,new V2TIMValueCallback<List<V2TIMUserFullInfo>>(){
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                    if(v2TIMUserFullInfos!=null && !v2TIMUserFullInfos.isEmpty()){
                        for (V2TIMUserFullInfo v2TIMUserFullInfo : v2TIMUserFullInfos) {
                            int level = v2TIMUserFullInfo.getLevel();
                            if (level == 6){
                                needDelCount++;
                                String conversational = TUIConstants.TUIConversation.CONVERSATION_C2C_PREFIX + v2TIMUserFullInfo.getUserID();
                                if(isFriendConversation){
                                    deleteConversation(conversational, loadedFriendshipInfoList);
                                }else{
                                    deleteConversation(conversational, loadedConversationInfoList);
                                }
                            }
                        }
                        if (needDelCount == 0){
                            isDelBanCovversation = false;
                            try{
                                if(isFriendConversation){
                                    ((ConversationListAdapter)friendshipAdapter).banConversationDel();
                                }else{
                                    ((ConversationListAdapter)adapter).banConversationDel();
                                }
                            }catch (Exception ignored){
                                //一键删除封号设备。异常检测
                            }

                        }
                    }
            }

            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                Log.e(TAG, "getUsersProfile failed: " + code + " desc");
                ToastUtil.toastLongMessage(code + " " + desc);
            }
        });

    }

    /**
     * 清空会话
     *
     * @param conversation 会话信息
     */
    public void clearConversationMessage(ConversationInfo conversation) {
        if (conversation == null || TextUtils.isEmpty(conversation.getConversationId())) {
            TUIConversationLog.e(TAG, "clearConversationMessage error: invalid conversation");
            return;
        }

        provider.clearHistoryMessage(conversation.getId(), conversation.isGroup(), new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    public void clearConversationMessage(String chatId, boolean isGroup) {
        provider.clearHistoryMessage(chatId, isGroup, new IUIKitCallback<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    /**
     * 删除会话，只删除数据源中的会话信息
     *
     * @param id C2C：对方的 userID；Group：群 ID
     */
    public void deleteConversation(String id, boolean isGroup,List<ConversationInfo> conversationInfoList) {
        ConversationInfo conversationInfo = null;
        for (int i = 0; i < conversationInfoList.size(); i++) {
            ConversationInfo info = conversationInfoList.get(i);
            if (isGroup == info.isGroup() && info.getId().equals(id)) {
                conversationInfo = info;
                break;
            }
        }
        deleteConversation(conversationInfo);
    }

    public void deleteConversation(String conversationId,List<ConversationInfo> conversationInfoList) {
        if (TextUtils.isEmpty(conversationId)) {
            return;
        }
        ConversationInfo conversationInfo = null;
        for (int i = 0; i < conversationInfoList.size(); i++) {
            ConversationInfo info = conversationInfoList.get(i);
            if (info.getConversationId().equals(conversationId)) {
                conversationInfo = info;
                break;
            }
        }
        deleteConversation(conversationInfo);
    }

    public void onFriendRemarkChanged(String id, String remark) {
        if(isFriendConversation){
            for (int i = 0; i < loadedFriendshipInfoList.size(); i++) {
                ConversationInfo info = loadedFriendshipInfoList.get(i);
                if (info.getId().equals(id) && !info.isGroup()) {
                    String title = info.getShowName();
                    if (!TextUtils.isEmpty(remark)) {
                        title = remark;
                    }
                    info.setTitle(title);
                    friendshipAdapter.onDataSourceChanged(loadedFriendshipInfoList);
                    if (friendshipAdapter != null) {
                        friendshipAdapter.onItemChanged(i);
                    }
                    break;
                }
            }
        }else{
            for (int i = 0; i < loadedConversationInfoList.size(); i++) {
                ConversationInfo info = loadedConversationInfoList.get(i);
                if (info.getId().equals(id) && !info.isGroup()) {
                    String title = info.getShowName();
                    if (!TextUtils.isEmpty(remark)) {
                        title = remark;
                    }
                    info.setTitle(title);
                    adapter.onDataSourceChanged(loadedConversationInfoList);
                    if (adapter != null) {
                        adapter.onItemChanged(i);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 会话会话列界面，在数据源更新的地方调用
     */
    public void updateAdapter() {
        if (adapter != null) {
            adapter.onViewNeedRefresh();
        }
    }

    /**
    * @Desc TODO(查询当前用户所有的好友列表)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/8/11
    */
    public void getFriendshipList(long loadSize,final boolean isFriend){
        synchronized (ConversationPresenter.class){
            //说明好友列表还没有数据
            if(isFriend){
                if(loadedFriendshipInfoList.size() > 1){
                    friendshipAdapter.onDataSourceChanged(loadedFriendshipInfoList);
                    friendshipAdapter.onItemRangeChanged(0,loadedFriendshipInfoList.size());
                }else{
                    provider.getFriendShipList(new IUIKitCallback<List<String>>() {
                        @Override
                        public void onSuccess(List<String> userIdData) {
                            if(!userIdData.isEmpty()){
                                loadedFriendshipInfoIdList.addAll(userIdData);
                                int dataSize = userIdData.size();
                                List<String> friendList = new ArrayList<>();
                                friendCurrentPaging = 1;
                                //如果当前查询好友列表总数量小于 100
                                if(dataSize < GET_CONVERSATION_COUNT){
                                    friendMaxPaging = 1;
                                    //不满足100条件直接添加过滤
                                    friendList.addAll(userIdData);
                                }else {
                                    double friendLimit = dataSize / GET_CONVERSATION_COUNT;
                                    //向上去整--无穷大到整数
                                    friendMaxPaging = (int) Math.ceil(friendLimit);
                                    //第一次默认查询100条用户会话列表
                                    for (int i = 0; i < GET_CONVERSATION_COUNT; i++){
                                        friendList.add(userIdData.get(i));
                                    }
                                }
                                provider.getFriendShipConversationList(friendList, new IUIKitCallback<List<ConversationInfo>>() {
                                    @Override
                                    public void onSuccess(List<ConversationInfo> dataConversationInfo) {
                                        if(!dataConversationInfo.isEmpty()){
                                            //loadedFriendshipInfoList.addAll(dataConversationInfo);
                                            isFriendConversationList(dataConversationInfo);
                                            ConversationPresenter.this.onNewConversation(dataConversationInfo);
                                        }

                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                        if(loadConversationCallback!=null){
                                            loadConversationCallback.isConversationEmpty(true);
                                        }
                                    }
                                });
                            }else{
                                if(loadConversationCallback!=null){
                                    loadConversationCallback.isConversationEmpty(true);
                                }
                            }
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            if(loadConversationCallback!=null){
                                loadConversationCallback.isConversationEmpty(true);
                            }
                        }
                    });
                }
            }else{
                provider.getFriendShipList(new IUIKitCallback<List<String>>() {
                    @Override
                    public void onSuccess(List<String> userIdData) {
                        if(!userIdData.isEmpty()){
                            loadedFriendshipInfoIdList.addAll(userIdData);
                        }
                        loadConversation(loadSize);
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        loadConversation(loadSize);
                    }
                });
            }
        }
    }
    /**
    * @Desc TODO(统计当前会话列表的未读数量)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/8/15
    */
    public void busConversationCount( List<ConversationInfo> dataInfoList){
        try {
            ThreadHelper.INST.execute(() -> {
                try{
                    int totalUnreadCounts = 0;
                    //这里用增强for
                    for (ConversationInfo conversationInfo : dataInfoList) {
                        totalUnreadCounts += conversationInfo.getUnRead();
                    }
                    if(loadConversationCallback!=null){
                        loadConversationCallback.totalUnreadCount(totalUnreadCounts);
                    }
                }catch (Exception ignored){

                }
            });
        }catch (Exception ignored){
            //修正统计会话消息数量。线程溢出
        }

    }
    //设置好友列表
    public void setFriendshipAdapter(IConversationListAdapter adapter) {
        this.friendshipAdapter = adapter;
    }
    /**
    * @Desc TODO(自定义接口。会话列表数量、好友列表是否为空)
    * @author 彭石林
    * @parame
    * @return
    * @Date 2022/8/15
    */
    public interface LoadConversationCallback{
        void totalUnreadCount(int count);

        default void isConversationEmpty(boolean empty) {

        }
    }

}