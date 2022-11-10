package com.fine.friendlycc.ui.mine.trace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.event.TraceEmptyEvent;
import com.fine.friendlycc.event.TraceEvent;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.google.android.material.tabs.TabLayout;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentMineTraceGirlBinding;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * Author: 彭石林
 * Time: 2021/8/2 15:50
 * Description: This is TraceFragment
 */
public class TraceFragment extends BaseToolbarFragment<FragmentMineTraceGirlBinding, TraceViewModel> {

    String titleToobal = "";
    int sel_idx = -1;
    private TraceFragmentTabPagerAdapter adapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine_trace_girl;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBarUtils.setupStatusBar(this, true, true);
        return view;
    }

    @Override
    public TraceViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(TraceViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        titleToobal = getArguments().getString("title");
        sel_idx = getArguments().getInt("sel_idx");
    }

    @Override
    public void initData() {
        super.initData();
        if (!StringUtils.isEmpty(titleToobal)) {
            setTitleBarTitle(titleToobal);
        }
        adapter = new TraceFragmentTabPagerAdapter(mActivity, this.getChildFragmentManager(), 0, viewModel);
        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.setAdapter(adapter);
        binding.tabs.setSelectedTabIndicatorHeight(0);
        binding.tabs.setupWithViewPager(binding.viewPager);
        createTabs();
        binding.tabs.getTabAt(sel_idx).select();
        binding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    setTitleBarTitle(String.valueOf(tab.getText()));
                    tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                try {
                    //tab未被选择的时候回调
                    tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab重新选择的时候回调
            }
        });
    }

    private void createTabs() {
        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_item);
                View customView = tab.getCustomView();
                if (customView != null) {
                    if (i == 0) {
                        customView.findViewById(R.id.tab_text).setSelected(true);
                    }
                    Button button = customView.findViewById(R.id.tab_text);
                    button.setOnClickListener(v -> tab.select());
                    button.setText(TraceFragmentTabPagerAdapter.TAB_MALE_TITLES[i]);
                }
            }
        }
    }

    @Override
    public void initViewObservable() {
        RxBus.getDefault().post(new TraceEmptyEvent());
        viewModel.uc.refreshTag.observe(this, new Observer<TraceEvent>() {
            @Override
            public void onChanged(TraceEvent traceEvent) {
                int size = traceEvent.getSize();
                if (traceEvent.getSelIdx() == 0) {
                    String val = StringUtils.getString(R.string.playcc_mine_my_likes);
                    Button button = binding.tabs.getTabAt(0).getCustomView().findViewById(R.id.tab_text);
                    button.setText(val + " " + size);
                } else if (traceEvent.getSelIdx() == 1) {
                    String val = StringUtils.getString(R.string.playcc_mine_my_visitors_many);
                    Button button = binding.tabs.getTabAt(1).getCustomView().findViewById(R.id.tab_text);
                    button.setText(val + " " + size);
                }

            }
        });
    }
}
