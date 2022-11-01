package com.fine.friendlycc.ui.mine.vipprivilege;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentVipPrivilegeBinding;

/**
 * @author wulei
 */
public class VipPrivilegeFragment extends BaseToolbarFragment<FragmentVipPrivilegeBinding, VipPrivilegeViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_vip_privilege;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VipPrivilegeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(VipPrivilegeViewModel.class);
    }

}
