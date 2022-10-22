package com.dl.playfun.ui.vest.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentVestFirstBinding;
import com.dl.playfun.ui.base.BaseRefreshFragment;
import com.dl.playfun.utils.AutoSizeUtils;
import com.google.android.material.tabs.TabLayout;

/**
 * @author wulei
 */
public class VestFirstFragment extends BaseRefreshFragment<FragmentVestFirstBinding, VestFirstViewModel> {

    private String[] tabs = {"在线", "推荐", "新人"};
    private ViewHolder holder;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_vest_first;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VestFirstViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(VestFirstViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        initTab();
    }

    private void initTab() {
        TabLayout tabLayout = binding.tabTitle;
        for (int i = 0; i < tabs.length; i++) {
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.frist_tab_item);
            holder = new ViewHolder(tab.getCustomView());
            holder.mTabItemName.setText(tabs[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                holder = new ViewHolder(tab.getCustomView());
                holder.mTabItemName.setSelected(true);
                holder.mTabItemName.setTextSize(18);
                CharSequence text = holder.mTabItemName.getText();
                if (text.equals(tabs[0])){
                    viewModel.type = 1;
                    viewModel.sex = 1;
                }else if (text.equals(tabs[1])){
                    viewModel.type = 2;
                    viewModel.sex = 1;
                }else if (text.equals(tabs[2])){
                    viewModel.sex = 0;
                    viewModel.type = 5;
                }
                viewModel.loadDatas(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                holder = new ViewHolder(tab.getCustomView());
                holder.mTabItemName.setSelected(false);
                holder.mTabItemName.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(1));
    }

    class ViewHolder {
        TextView mTabItemName;

        ViewHolder(View tabView) {
            mTabItemName = (TextView) tabView.findViewById(R.id.tab_item_name);
        }
    }
}
