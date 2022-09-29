package com.dl.playfun.ui.mine.blacklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentBlacklistBinding;

/**
 * @author wulei
 */
public class BlacklistFragment extends BaseRefreshToolbarFragment<FragmentBlacklistBinding, BlacklistViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_blacklist;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BlacklistViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(BlacklistViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }
}
