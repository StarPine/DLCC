package com.fine.friendlycc.viewadapter;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author wulei
 */
public class ViewPagerAdapter {

    @BindingAdapter({"current_item"})
    public static void setCurrentItem(ViewPager viewPager, Integer pageIndex) {
        if (viewPager == null || pageIndex == null) {
            return;
        }
        if (viewPager.getCurrentItem() == pageIndex) {
            return;
        }
        viewPager.setCurrentItem(pageIndex);
    }
}
