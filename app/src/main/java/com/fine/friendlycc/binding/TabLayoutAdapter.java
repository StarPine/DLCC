package com.fine.friendlycc.binding;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ColorUtils;
import com.fine.friendlycc.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/3 16:14
 */
public class TabLayoutAdapter {

    @BindingAdapter(value = {"tabNameList", "layoutRes"}, requireAll = false)
    public static void setTablayout(TabLayout tabLayout, List<String> tabNameList, @LayoutRes int layoutRes) {
        tabLayout.removeAllTabs();
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null)
                    tabSelectedSetting(new ViewHolder(customView));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null)
                    tabUnSelectedSetting(new ViewHolder(customView));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener);

        for (int i = 0; i < tabNameList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(layoutRes);
            ViewHolder holder = new ViewHolder(tab.getCustomView());
            holder.mTabItemName.setText(tabNameList.get(i));
            if (i == 0) {
                tabSelectedSetting(holder);
            } else {
                tabUnSelectedSetting(holder);
            }
        }

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private static void tabUnSelectedSetting(ViewHolder holder) {
        holder.mTabItemName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        holder.mTabItemName.setSelected(false);
        holder.mTabItemName.setTextSize(13);
        holder.mTabItemName.setTextColor(ColorUtils.getColor(R.color.play_chat_gray_3));
        holder.mTabItemNBg.setVisibility(View.GONE);
    }

    private static void tabSelectedSetting(ViewHolder holder) {
        holder.mTabItemName.setSelected(true);
        holder.mTabItemName.setTextSize(17);
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
