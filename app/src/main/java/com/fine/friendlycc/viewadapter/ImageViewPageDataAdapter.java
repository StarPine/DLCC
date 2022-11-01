package com.fine.friendlycc.viewadapter;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fine.friendlycc.utils.ListUtils;

import java.util.List;

/**
 * @author wulei
 */
public class ImageViewPageDataAdapter {
    @BindingAdapter(value = {"ImageViewPageData"}, requireAll = false)
    public static void setImageUri(ViewPager viewPager, List<String> imagePath) {
        try {
            if (!ListUtils.isEmpty(imagePath)) {
                ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(viewPager.getContext(), imagePath);
                viewPager.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
