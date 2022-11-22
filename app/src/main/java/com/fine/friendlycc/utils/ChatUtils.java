package com.fine.friendlycc.utils;

import static com.fine.friendlycc.ui.message.chatdetail.ChatDetailFragment.CHAT_INFO;

import android.os.Bundle;

import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.ui.message.chatdetail.ChatDetailFragment;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author litchi
 */
public class ChatUtils {
    public static final String TAG = "ChatUtils";


    public static void chatUserView(String serviceUserId, Integer userId, String name, BaseViewModel baseViewModel) {
        //待获取用户资料的用户列表
        List<String> users = new ArrayList<String>();
        users.add(serviceUserId);
        //获取用户资料
        V2TIMManager.getInstance().getUsersInfo(users,new V2TIMValueCallback<List<V2TIMUserFullInfo>>(){
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if(v2TIMUserFullInfos!=null && !v2TIMUserFullInfos.isEmpty()){
                    toChatUser(serviceUserId, userId, name, baseViewModel);
                }
            }

            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //Log.e("获取用户信息失败", "getUsersProfile failed: " + code + " desc");
                ToastUtil.toastLongMessage(code + " " + desc);
            }
        });
    }

    private static void toChatUser(String serviceUserId, Integer userId,String name, BaseViewModel baseViewModel) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(serviceUserId);
        chatInfo.setChatName(name);
        CCApplication.instance().logEvent(AppsFlyerEvent.IM);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHAT_INFO, chatInfo);
        bundle.putInt("toUserId", userId);
        baseViewModel.start(ChatDetailFragment.class.getCanonicalName(), bundle);
    }

    /**
    * @Desc TODO(进入1v1单聊页面)
    * @author 彭石林
    * @parame [ImUserId, name, baseViewModel]
    * @return void
    * @Date 2022/4/2
    */
    public static void chatUser(String ImUserId,Integer userId, String name, BaseViewModel baseViewModel) {
        chatUserView(ImUserId,userId, name, baseViewModel);
    }

    /**
    * @Desc TODO(跳转1v1文字聊天页面)
    * @author 彭石林
    * @parame [conversationInfo, toUserId, baseViewModel]
    * @return void
    * @Date 2022/8/13
    */
    public static void startChatActivity(ConversationInfo conversationInfo, Integer toUserId,BaseViewModel baseViewModel) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
        CCApplication.instance().logEvent(AppsFlyerEvent.IM);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatDetailFragment.CHAT_INFO, chatInfo);
        bundle.putSerializable("toUserId", toUserId);
        baseViewModel.start(ChatDetailFragment.class.getCanonicalName(), bundle);
    }

}
