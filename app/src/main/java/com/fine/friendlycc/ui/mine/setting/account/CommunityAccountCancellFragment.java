package com.fine.friendlycc.ui.mine.setting.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentSettingAccountCancellBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.widget.coinrechargesheet.CoinRechargeSheetView;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;

public class CommunityAccountCancellFragment extends BaseToolbarFragment<FragmentSettingAccountCancellBinding, CommunityAccountCancellViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_setting_account_cancell;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CommunityAccountCancellViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CommunityAccountCancellViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.btnCancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.cancellation();
            }
        });
        viewModel.cancellType.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean type) {
                if (type) {
                    MMAlertDialog.AlertAccountCancell(mActivity, (dialog, which) -> {
                        //跳转到登录界面
                        startWithPopTo(new LoginFragment(), CommunityAccountCancellFragment.class, true);
                    }).show();
                } else {
                    //调用充值钻石弹窗
                    toRecharge();
                }
            }
        });
    }

    /**
     * 去充值
     */
    private void toRecharge() {
        CoinRechargeSheetView coinRechargeFragmentView = new CoinRechargeSheetView(mActivity);
        coinRechargeFragmentView.show();
    }
}
