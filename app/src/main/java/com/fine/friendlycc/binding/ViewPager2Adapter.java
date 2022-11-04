package com.fine.friendlycc.binding;

import androidx.databinding.BindingAdapter;
import androidx.viewpager2.widget.ViewPager2;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/4 21:51
 */
public class ViewPager2Adapter {
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
}
