package com.dl.playfun.ui.message.commentmessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.widget.dialog.MVDialog;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentCommentMessageBinding;

/**
 * @author wulei
 */
public class CommentMessageFragment extends BaseRefreshToolbarFragment<FragmentCommentMessageBinding, CommentMessageViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_comment_message;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CommentMessageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CommentMessageViewModel.class);
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
