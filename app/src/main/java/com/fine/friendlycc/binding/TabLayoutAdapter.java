package com.fine.friendlycc.binding;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ColorUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.ui.home.HomeMainFragment;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/3 16:14
 */
public class TabLayoutAdapter {

    private static ViewHolder holder;

    @BindingAdapter(value = {"onPageSelectedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final ViewPager2 viewPager2,
                                             final BindingCommand<Integer> onPageSelectedCommand) {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedCommand != null) {
                    onPageSelectedCommand.execute(position);
                }
            }
        });
    }


    @BindingAdapter(value = {"tabNameList", "layoutRes"}, requireAll = false)
    public static void setTablayout(TabLayout tabLayout, List<String> tabNameList, @LayoutRes int layoutRes) {
        for (int i = 0; i < tabNameList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(layoutRes);
            holder = new ViewHolder(tab.getCustomView());
            holder.mTabItemName.setText(tabNameList.get(i));
            if (i == 0){
                tabSelectedSetting(tab);
            }else {
                tabUnSelectedSetting(tab);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabSelectedSetting(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabUnSelectedSetting(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private static void tabUnSelectedSetting(TabLayout.Tab tab) {
        holder = new ViewHolder(tab.getCustomView());
        holder.mTabItemName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        holder.mTabItemName.setSelected(false);
        holder.mTabItemName.setTextSize(14);
        holder.mTabItemName.setTextColor(ColorUtils.getColor(R.color.play_chat_gray_3));
        holder.mTabItemNBg.setVisibility(View.GONE);
    }

    private static void tabSelectedSetting(TabLayout.Tab tab) {
        holder = new ViewHolder(tab.getCustomView());
        holder.mTabItemName.setSelected(true);
        holder.mTabItemName.setTextSize(18);
        holder.mTabItemName.setTextColor(ColorUtils.getColor(R.color.toolbar_title_color));
        holder.mTabItemName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        holder.mTabItemNBg.setVisibility(View.VISIBLE);
    }

    static class ViewHolder {
        TextView mTabItemName;
        ImageView mTabItemNBg;

        ViewHolder(View tabView) {
            mTabItemName = (TextView) tabView.findViewById(R.id.tab_item_name);
            mTabItemNBg = (ImageView) tabView.findViewById(R.id.iv_tab_item_bg);
        }
    }

}
