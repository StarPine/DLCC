package com.fine.friendlycc.ui.mine.wallet.cash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentCashBinding;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class CashFragment extends BaseRefreshFragment<FragmentCashBinding, CashViewModel> implements View.OnClickListener {

    public static final String TAG = "CashFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_cash;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CashViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CashViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.btnWithdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (viewModel.cashWalletEntity.get() != null && viewModel.cashWalletEntity.get().getCanAccount() < 10) {
            ToastUtils.showShort(R.string.playcc_withdrawal_warn);
            return;
        }
        viewModel.cashWithdraw(10);
    }


}
