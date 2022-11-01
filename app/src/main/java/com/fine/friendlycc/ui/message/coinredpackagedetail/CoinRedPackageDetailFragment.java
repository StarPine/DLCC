package com.fine.friendlycc.ui.message.coinredpackagedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentCoinRedPackageDetailBinding;

/**
 * @author wulei
 */
public class CoinRedPackageDetailFragment extends BaseToolbarFragment<FragmentCoinRedPackageDetailBinding, CoinRedPackageDetailViewModel> {
    public static final String ARG_RED_PACKAGE_ID = "arg_redpackage_id";
    public static final String ARG_MSG_ID = "arg_msg_id";
    public static final String ARG_IS_SENDER = "arg_is_sender";

    private int redPackageId;
    private String msgId;
    private boolean isSender;

    public static Bundle getStartBundle(int redPackageId, String msgId, boolean isSender) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_RED_PACKAGE_ID, redPackageId);
        bundle.putString(ARG_MSG_ID, msgId);
        bundle.putBoolean(ARG_IS_SENDER, isSender);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_coin_red_package_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        redPackageId = getArguments().getInt(ARG_RED_PACKAGE_ID, 0);
        msgId = getArguments().getString(ARG_MSG_ID);
        isSender = getArguments().getBoolean(ARG_IS_SENDER, false);
    }

    @Override
    public CoinRedPackageDetailViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        CoinRedPackageDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(CoinRedPackageDetailViewModel.class);
        viewModel.redPackageId.set(redPackageId);
        viewModel.msgId.set(msgId);
        viewModel.isSender.set(isSender);
        return viewModel;
    }

}
