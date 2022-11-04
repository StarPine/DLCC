package com.fine.friendlycc.ui.home;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fine.friendlycc.ui.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/3 17:11
 */
public class HomePagerAdapter extends FragmentStateAdapter {

    private BaseFragment[] fragmentList;

    public HomePagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    public void setFragmentList(BaseFragment[] fragmentList) {
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragmentList == null || fragmentList.length <= position) {
            return new Fragment();
        }
        return fragmentList[position];
    }

    @Override
    public int getItemCount() {
        return fragmentList == null ? 0 : fragmentList.length;
    }

}
