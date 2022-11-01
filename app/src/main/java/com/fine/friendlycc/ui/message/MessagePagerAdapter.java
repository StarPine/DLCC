package com.fine.friendlycc.ui.message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.widget.pageview.FragmentAdapter;

/**
 * Author: 彭石林
 * Time: 2022/3/3 18:52
 * Description: This is MessagePagerAdapter
 */
public class MessagePagerAdapter extends FragmentStateAdapter {
    private static final String TAG = FragmentAdapter.class.getSimpleName();

    private BaseFragment[] fragmentList;

    public MessagePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MessagePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MessagePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
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
