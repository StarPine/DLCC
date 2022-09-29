package com.dl.playfun.ui.mine.wallet;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dl.playfun.ui.mine.wallet.cash.CashFragment;
import com.dl.playfun.R;
import com.dl.playfun.ui.mine.wallet.coin.CoinFragment;

public class WalletTabPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.playfun_tab_wallet_1, R.string.playfun_tab_wallet_2};
    private final Context mContext;

    public WalletTabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CashFragment();
        } else {
            return new CoinFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}