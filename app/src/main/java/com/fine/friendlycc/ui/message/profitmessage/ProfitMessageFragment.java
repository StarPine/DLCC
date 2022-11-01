package com.fine.friendlycc.ui.message.profitmessage;

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
import com.fine.friendlycc.databinding.FragmentProfitMessageBinding;

/**
 * @author wulei
 */
public class ProfitMessageFragment extends BaseRefreshToolbarFragment<FragmentProfitMessageBinding, ProfitMessageViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_profit_message;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ProfitMessageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(ProfitMessageViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickDelete.observe(this, integer -> MVDialog.getInstance(mActivity)
                .setContent(getString(R.string.playfun_comfirm_delete_message))
                .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                .setConfirmOnlick(dialog -> {
                    dialog.dismiss();
                    viewModel.deleteMessage(integer);
                })
                .show());
    }
}
