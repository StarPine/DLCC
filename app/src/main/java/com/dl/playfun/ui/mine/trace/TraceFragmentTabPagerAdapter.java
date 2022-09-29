package com.dl.playfun.ui.mine.trace;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.ui.mine.trace.list.TraceListFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Author: 彭石林
 * Time: 2021/8/2 16:19
 * Description: This is TraceFragmentTabPagerAdapter
 */
public class TraceFragmentTabPagerAdapter extends FragmentStatePagerAdapter {


    @StringRes
    public static final int[] TAB_MALE_TITLES = new int[]{R.string.playfun_mine_my_likes, R.string.playfun_mine_my_visitors_many};

    private final Context mContext;

    private final int gender;

    private final TraceViewModel traceViewModel;

    public TraceFragmentTabPagerAdapter(Context context, FragmentManager fm, int gender, TraceViewModel traceViewModel) {
        super(fm);
        mContext = context;
        this.gender = gender;
        this.traceViewModel = traceViewModel;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        TraceListFragment traceListFragment = TraceListFragment.newInstance(position);
        traceListFragment.setTraceListViewModel(traceViewModel);
        return traceListFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return StringUtils.getString(TAB_MALE_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_MALE_TITLES.length;
    }
}
