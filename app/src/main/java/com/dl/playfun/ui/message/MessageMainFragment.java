package com.dl.playfun.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.databinding.FragmentMessageMainBinding;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.ui.message.chatmessage.ChatMessageFragment;
import com.dl.playfun.ui.message.contact.OftenContactFragment;
import com.dl.playfun.utils.AutoSizeUtils;

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
        AppContext.instance().logEvent(AppsFlyerEvent.Messages);
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

        binding.viewPager.setUserInputEnabled(false);
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
                AppContext.instance().logEvent(AppsFlyerEvent.System_Messages);
                binding.viewPager.setCurrentItem(0, false);
            } else {
                AppContext.instance().logEvent(AppsFlyerEvent.Chat);
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
        AppContext.isShowNotPaid = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        AppContext.isShowNotPaid = false;
    }
}
