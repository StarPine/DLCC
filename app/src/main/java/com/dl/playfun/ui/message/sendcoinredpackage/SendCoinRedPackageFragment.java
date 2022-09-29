package com.dl.playfun.ui.message.sendcoinredpackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.dl.playfun.widget.coinpaysheet.CoinPaySheet;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentSendCoinRedPackageBinding;

import me.goldze.mvvmhabit.utils.ToastUtils;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * @author wulei
 */
public class SendCoinRedPackageFragment extends BaseToolbarFragment<FragmentSendCoinRedPackageBinding, SendCoinRedPackageViewModel> {
    public static final String ARG_USER_ID = "arg_user_id";

    public static final String ARG_RED_PACKAGE_ID = "arg_red_package_id";
    public static final String ARG_NUMBER = "arg_number";
    public static final String ARG_DESC = "arg_desc";

    private int userId;

    public static Bundle getStartBundle(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBarUtils.setupStatusBar(this, true, false);
        return view;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_send_coin_red_package;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        userId = getArguments().getInt(ARG_USER_ID, 0);
    }

    @Override
    public SendCoinRedPackageViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        SendCoinRedPackageViewModel viewModel = ViewModelProviders.of(this, factory).get(SendCoinRedPackageViewModel.class);
        viewModel.userId.set(userId);
        return viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickSend.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void v) {
                if (StringUtils.isEmpty(viewModel.number.get()) || Integer.valueOf(viewModel.number.get()) < 1) {
                    ToastUtils.showShort(R.string.playfun_please_diamond_number);
                    return;
                }
                hideSoftInput();
                String desc = viewModel.desc.get();
                if (desc == null) {
                    desc = binding.edtDesc.getHint().toString();
                }
                payCoinRedPackage(userId, Integer.valueOf(viewModel.number.get()), desc);
            }
        });
    }

    public void payCoinRedPackage(int userId, int coinNumber, String desc) {
        new CoinPaySheet.Builder(mActivity).setPayRedPackageParams(userId, coinNumber, desc, getString(R.string.playfun_diamond_red_package), (sheet, redPackageId) -> {
            sheet.dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_RED_PACKAGE_ID, redPackageId);
            bundle.putInt(ARG_NUMBER, Integer.valueOf(viewModel.number.get()));
            if (StringUtils.isEmpty(viewModel.desc.get())) {
                bundle.putString(ARG_DESC, binding.edtDesc.getHint().toString());
            } else {
                bundle.putString(ARG_DESC, viewModel.desc.get());
            }
            setFragmentResult(ISupportFragment.RESULT_OK, bundle);
            hideSoftInput();
            pop();
        }).build().show();
    }

}
