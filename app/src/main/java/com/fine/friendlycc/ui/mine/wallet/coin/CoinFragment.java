package com.fine.friendlycc.ui.mine.wallet.coin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentCoinBinding;
import com.fine.friendlycc.ui.base.BaseRefreshToolbarFragment;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.SoftKeyBoardListener;
import com.fine.friendlycc.widget.dialog.MVDialog;

/**
 * @author wulei
 */
@SuppressLint("StringFormatMatches")
public class CoinFragment extends BaseRefreshToolbarFragment<FragmentCoinBinding, CoinViewModel>  {
    public static final String TAG = "CoinFragment";
    protected InputMethodManager inputMethodManager;

    private boolean SoftKeyboardShow = false;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_coin;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CoinViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CoinViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                SoftKeyboardShow = true;
            }

            @Override
            public void keyBoardHide(int height) {
                SoftKeyboardShow = false;
            }
        });
        viewModel.uc.withdrawComplete.observe(this, aVoid -> MVDialog.getInstance(mActivity)
                .setTitele(getString(R.string.playcc_dialog_title_withdraw_complete))
                .setContent(getString(R.string.playcc_dialog_content_withdraw_complete))
                .setConfirmOnlick(dialog -> dialog.dismiss())
                .chooseType(MVDialog.TypeEnum.CENTER)
                .show());
    }

    /**
     *
     */
    protected void hideSoftKeyboard() {
        if (SoftKeyboardShow) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }


}
