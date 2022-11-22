package com.fine.friendlycc.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentMessageMainBinding;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.ui.message.chatmessage.ChatMessageFragment;
import com.fine.friendlycc.ui.message.contact.OftenContactFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;

/**
 * @author wulei
 */
public class MessageMainFragment extends BaseFragment<FragmentMessageMainBinding, MessageMainViewModel> {

    private final BaseFragment[] mFragments = new BaseFragment[2];

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_message_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MessageMainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MessageMainViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void initData() {
        super.initData();
        CCApplication.instance().logEvent(AppsFlyerEvent.Messages);
        viewModel.onViewCreated();
        viewModel.loadDatas();

        BaseFragment firstFragment = findChildFragment(ChatMessageFragment.class);
        mFragments[0] = new ChatMessageFragment();
        if (firstFragment == null) {
            mFragments[1] = new OftenContactFragment();
        } else {
            if(findChildFragment(OftenContactFragment.class)!=null){
                mFragments[1] = findChildFragment(OftenContactFragment.class);
            }else{
                mFragments[1] = new OftenContactFragment();
            }

        }
        MessagePagerAdapter fragmentAdapter = new MessagePagerAdapter(this);
        fragmentAdapter.setFragmentList(mFragments);

        binding.viewPager.setUserInputEnabled(true);
        binding.viewPager.setOffscreenPageLimit(1);
        binding.viewPager.setAdapter(fragmentAdapter);
        //取消保存页面--未知BUG
        binding.viewPager.setSaveEnabled(false);
        binding.viewPager.setCurrentItem(0, false);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.tabSelectEvent.observe(this, flag -> {
            if (flag) {
                CCApplication.instance().logEvent(AppsFlyerEvent.System_Messages);
                binding.viewPager.setCurrentItem(0, false);
            } else {
                CCApplication.instance().logEvent(AppsFlyerEvent.Chat);
                binding.viewPager.setCurrentItem(1, false);
            }

        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        // ImmersionBarUtils.setupStatusBar(this, false, true);
        if(mFragments.length>0){
            ChatMessageFragment chatmessage = (ChatMessageFragment)mFragments[0];
            chatmessage.loadBrowseNumberCall();
        }
    }


    @Override
    protected boolean isUmengReportPage() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        CCApplication.isShowNotPaid = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        CCApplication.isShowNotPaid = false;
    }
}
