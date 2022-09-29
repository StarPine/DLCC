package com.dl.playfun.ui.certification.certificationfemale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentCertificationFemaleBinding;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.widget.dialog.TraceDialog;

/**
 * 女士真人认证
 *
 * @author wulei
 */
public class CertificationFemaleFragment extends BaseRefreshToolbarFragment<FragmentCertificationFemaleBinding, CertificationFemaleViewModel> {
    private boolean dialog_tw_money = false;
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_certification_female;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CertificationFemaleViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CertificationFemaleViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        dialog_tw_money = getArguments().getBoolean("dialog_tw_money", false);
    }

    @Override
    public void initData() {
        super.initData();
        if (dialog_tw_money) {
            TraceDialog.getInstance(getContext())
                    .alertCertificationGirl().show();
        }
        binding.refreshLayout.setEnableLoadMore(false);
    }
}
