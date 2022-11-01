package com.fine.friendlycc.ui.mine.invitationcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentInvitationCodeBinding;

/**
 * 我的邀請碼
 *
 * @author wulei
 */
public class InvitationCodeFragment extends BaseToolbarFragment<FragmentInvitationCodeBinding, InvitationCodeViewModel> {

    public static final String ARG_INVITATION_CODE = "arg_invitation_code";

    private String invitationCode;

    public static Bundle getStartBundle(String invitationCode) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_INVITATION_CODE, invitationCode);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_invitation_code;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public InvitationCodeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(InvitationCodeViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        invitationCode = getArguments().getString(ARG_INVITATION_CODE);
    }

    @Override
    public void initData() {
        super.initData();
        binding.tvCode.setText(invitationCode);
    }
}
