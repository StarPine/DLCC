package com.fine.friendlycc.ui.message.chatmessage;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.databinding.FragmentChatMessageBinding;
import com.fine.friendlycc.entity.BrowseNumberEntity;
import com.fine.friendlycc.entity.SystemConfigEntity;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.event.MessageCountChangeTagEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.ThirdPushTokenMgr;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ChatUtils;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.interfaces.TUICallback;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.model.CustomConfigSetting;
import com.tencent.qcloud.tuikit.tuiconversation.presenter.ConversationPresenter;
import com.tencent.qcloud.tuikit.tuiconversation.ui.view.ConversationListLayout;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * @author litchi
 */
public class ChatMessageFragment extends BaseFragment<FragmentChatMessageBinding, ChatMessageViewModel> {

    private ConversationInfo selectedConversationInfo;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_chat_message;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ChatMessageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        ChatMessageViewModel viewModel = ViewModelProviders.of(this, factory).get(ChatMessageViewModel.class);
        return viewModel;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        //会话列表展示最多消息数量
        AppRepository appRepository = ConfigManager.getInstance().getAppRepository();
        SystemConfigEntity systemConfigEntity = appRepository.readSystemConfig();
        if (systemConfigEntity != null) {
            Integer visibleCount = systemConfigEntity.getConversationAstrictCount();
            if (visibleCount != null) {
                int ast = visibleCount;
                if (ast >= 0) {
                    CustomConfigSetting.conversationAstrictCount = ast;
                }
            }
        }
        //腾讯IM登录
        TokenEntity tokenEntity = Injection.provideDemoRepository().readLoginInfo();
        if (tokenEntity != null) {
            if(TUILogin.isUserLogined()){
                initIM();
                ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
            }else{
                TUILogin.login(mActivity, appRepository.readApiConfigManagerEntity().getImAppId(), tokenEntity.getUserID(), tokenEntity.getUserSig(), new TUICallback() {
                    @Override
                    public void onSuccess() {
                        initIM();
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    }

                    @Override
                    public void onError(int code, String desc) {
                        KLog.e("tencent im login error  errCode = " + code + ", errInfo = " + desc);
                        viewModel.flushSign();
                    }
                });
            }

        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.loginSuccess.observe(this, Void -> {
            initIM();
            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
        });
    }

    private void initIM() {
        ConversationPresenter presenter = new ConversationPresenter();
        presenter.setFriendConversation(false);
        presenter.setLoadConversationCallback(new ConversationPresenter.LoadConversationCallback() {
            @Override
            public void totalUnreadCount(int count) {
                binding.conversationLayout.post(() -> RxBus.getDefault().post(new MessageCountChangeTagEvent(count)));

            }

            @Override
            public void isConversationEmpty(boolean empty) {
                //好友会话列表为空  这里切换成主线程进行改变页面状态
//                if(empty) {
//                    if(binding.conversationLayout.getVisibility()!=View.GONE){
//                        binding.conversationLayout.post(()->{
//                            binding.conversationLayout.setVisibility(View.GONE);
//                            binding.rlEmptyLayout.setVisibility(View.VISIBLE);
//                        });
//                    }
//                }else{
//                    if(binding.conversationLayout.getVisibility()!=View.VISIBLE){
//                        binding.conversationLayout.post(()->{
//                            binding.conversationLayout.setVisibility(View.VISIBLE);
//                            binding.rlEmptyLayout.setVisibility(View.GONE);
//                        });
//                    }
//                }
            }
        });
        presenter.initIMListener();
        binding.conversationLayout.setPresenter(presenter);
        binding.conversationLayout.initDefault(false);
        ConversationListLayout listLayout = binding.conversationLayout.getConversationList();

        // 设置adapter item中top文字大小
        listLayout.setItemTopTextSize(16);
        // 设置adapter item中bottom文字大小
        listLayout.setItemBottomTextSize(12);
        // 设置adapter item中timeline文字大小
        listLayout.setItemDateTextSize(10);
        // 设置adapter item头像圆角大小
        listLayout.setItemAvatarRadius(SizeUtils.dp2px(50));
        // 设置adapter item是否不显示未读红点，默认显示
        listLayout.disableItemUnreadDot(false);

        binding.conversationLayout.getConversationList().setOnItemAvatarClickListener(new ConversationListLayout.OnItemAvatarClickListener() {
            @Override
            public void onItemAvatarClick(View view, int position, ConversationInfo messageInfo) {
                //点击用户头像
                String id = messageInfo.getId();
                if(id==null){
                    return;
                }
                if (id.trim().contains(AppConfig.CHAT_SERVICE_USER_ID)) {
                    return;
                }
                if(TUILogin.getLoginUser()!=null && id.trim().equals(TUILogin.getLoginUser())){
                    return;
                }
                viewModel.transUserIM(id,true);
            }
        });

        binding.conversationLayout.getConversationList().setBanConversationDelListener(new ConversationListLayout.BanConversationDelListener() {
            @Override
            public void banConversationDel() {
                viewModel.dismissHUD();
            }
        });

        binding.conversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo messageInfo) {
                String id = messageInfo.getId();
                if(TUILogin.getLoginUser()!=null && id.trim().equals(TUILogin.getLoginUser())){
                    return;
                }
                if (id.trim().contains(AppConfig.CHAT_SERVICE_USER_ID)) {
                    ChatUtils.startChatActivity(messageInfo,0,viewModel);
                }else{
                    selectedConversationInfo = messageInfo;
                    viewModel.transUserIM(id,false);
                }
            }
        });

        binding.conversationLayout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo messageInfo) {

                TraceDialog.getInstance(ChatMessageFragment.this.getContext())
                        .setConfirmOnlick(dialog -> {
                            //置顶会话
                            binding.conversationLayout.setConversationTop(messageInfo,null);

                        })
                        .setConfirmTwoOnlick(dialog -> {
                            //删除会话
                            binding.conversationLayout.deleteConversation(messageInfo);
                            binding.conversationLayout.clearConversationMessage(messageInfo);
                        })
                        .setConfirmThreeOnlick(dialog -> {
                            TraceDialog.getInstance(ChatMessageFragment.this.getContext())
                                    .setTitle(getString(R.string.playcc_del_banned_account_content))
                                    .setTitleSize(18)
                                    .setCannelText(getString(R.string.playcc_cancel))
                                    .setConfirmText(getString(R.string.playcc_mine_trace_delike_confirm))
                                    .chooseType(TraceDialog.TypeEnum.CENTER)
                                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                        @Override
                                        public void confirm(Dialog dialog) {
                                            dialog.dismiss();
                                            //删除所有封号会话
                                            viewModel.showHUD();
                                            binding.conversationLayout.deleteAllBannedConversation();
                                        }
                                    }).show();
                        })
                        .convasationItemMenuDialog(messageInfo)
                        .show();
            }
        });
        viewModel.uc.startChatUserView.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer toUserId) {
                if(selectedConversationInfo!=null){
                    ChatUtils.startChatActivity(selectedConversationInfo,toUserId,viewModel);
                }
            }
        });
        viewModel.uc.loadBrowseNumber.observe(this, new Observer<BrowseNumberEntity>() {
            @Override
            public void onChanged(BrowseNumberEntity browseNumberEntity) {
                if (!ObjectUtils.isEmpty(browseNumberEntity)) {
                    if (ConfigManager.getInstance().isMale()) {
                        if (ObjectUtils.isEmpty(browseNumberEntity.getBrowseNumber()) || browseNumberEntity.getBrowseNumber().intValue() < 1) {
                            viewModel.NewNumberText.set(null);
                            binding.conversationLastMsg.setText(R.string.playcc_char_message_text2);
                        } else {
                            Integer number = browseNumberEntity.getBrowseNumber();
                            String text = String.format(StringUtils.getString(R.string.playcc_char_message_text1),number) ;
                            binding.conversationLastMsg.setText(text);
                            if (number > 99) {
                                viewModel.NewNumberText.set("99+");
                            } else {
                                viewModel.NewNumberText.set(String.valueOf(number));
                            }

                        }
                        binding.itemLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.itemLeft.setVisibility(View.GONE);
                }

            }
        });
    }

    public void loadBrowseNumberCall(){
        try {
            if (viewModel != null) {
                viewModel.newsBrowseNumber();
            }
        } catch (Exception ignored) {

        }
    }

}
