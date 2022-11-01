package com.fine.friendlycc.ui.message.pushsetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentPushSettingBinding;

/**
 * @author wulei
 */
public class PushSettingFragment extends BaseToolbarFragment<FragmentPushSettingBinding, PushSettingViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_push_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public PushSettingViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(PushSettingViewModel.class);
    }


}
