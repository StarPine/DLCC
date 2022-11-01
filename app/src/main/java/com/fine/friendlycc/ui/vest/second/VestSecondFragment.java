package com.fine.friendlycc.ui.vest.second;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentRadioBinding;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;

/**
 * @author wulei
 */
public class VestSecondFragment extends BaseRefreshFragment<FragmentRadioBinding, VestSecondViewModel> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_vest_second;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VestSecondViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(VestSecondViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }


}
