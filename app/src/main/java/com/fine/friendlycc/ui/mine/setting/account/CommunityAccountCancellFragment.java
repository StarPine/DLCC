package com.fine.friendlycc.ui.mine.setting.account;

import android.content.Intent;
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
import com.fine.friendlycc.tim.TUIUtils;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.login.LoginFragment;
import com.fine.friendlycc.ui.mine.wallet.diamond.recharge.DialogDiamondRechargeActivity;
import com.fine.friendlycc.widget.coinrechargesheet.CoinRechargeSheetView;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.tencent.imsdk.v2.V2TIMCallback;

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
                        TUIUtils.logout(new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                viewModel.logout();
                            }

                            @Override
                            public void onError(int i, String s) {
                                viewModel.logout();
                            }
                        });
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
        Intent intent = new Intent(mActivity, DialogDiamondRechargeActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.pop_enter_anim, 0);
    }
}
