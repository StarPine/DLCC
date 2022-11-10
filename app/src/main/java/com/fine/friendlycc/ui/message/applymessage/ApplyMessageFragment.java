package com.fine.friendlycc.ui.message.applymessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseRefreshToolbarFragment;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentApplyMessageBinding;

/**
 * @author wulei
 */
public class ApplyMessageFragment extends BaseRefreshToolbarFragment<FragmentApplyMessageBinding, ApplyMessageViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_apply_message;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ApplyMessageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(ApplyMessageViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickDelete.observe(this, integer -> MVDialog.getInstance(mActivity)
                .setContent(getString(R.string.playcc_comfirm_delete_message))
                .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    viewModel.deleteMessage(integer);
                })
                .show());
    }
}
