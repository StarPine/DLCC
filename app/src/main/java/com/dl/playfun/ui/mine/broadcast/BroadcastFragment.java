package com.dl.playfun.ui.mine.broadcast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.google.android.material.tabs.TabLayout;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentBroadcastBinding;

/**
 * 我的動態fragment
 */
public class BroadcastFragment extends BaseToolbarFragment<FragmentBroadcastBinding, BroadcastViewModel> {
    private BroadcastPagerAdapter adapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_broadcast;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BroadcastViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(BroadcastViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.tabs.setSelectedTabIndicatorHeight(0);
        binding.tabs.setupWithViewPager(binding.viewPager);
        adapter = new BroadcastPagerAdapter(mActivity, this.getChildFragmentManager());
        binding.viewPager.setAdapter(adapter);

        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabs.getTabAt(i);
            tab.setCustomView(R.layout.tab_item);
            if (i == 0) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
            }
            Button button = tab.getCustomView().findViewById(R.id.tab_text);
            button.setOnClickListener(v -> tab.select());
            button.setText(BroadcastPagerAdapter.TAB_TITLES[i]);
        }
        binding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                //tab被选的时候回调
                //binding.viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab未被选择的时候回调
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab重新选择的时候回调
            }
        });
    }

    //TODO: KL：这部分代码做什么的？
    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.switchPosion.observe(this, o -> {
            if (o != null) {
                binding.tabs.setScrollPosition(0, (int) o, true);
                binding.viewPager.setCurrentItem((int) o, true);
            }
        });
    }

    @Override
    protected boolean isUmengReportPage() {
        return false;
    }
}
