package com.dl.playfun.ui.certification.certificationmale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentCertificationMaleBinding;

/**
 * 男士真人认证
 *
 * @author wulei
 */
public class CertificationMaleFragment extends BaseToolbarFragment<FragmentCertificationMaleBinding, CertificationMaleViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_certification_male;
    }

    @Override
    public int initVariableId() {
        showHud();
        return BR.viewModel;
    }

    @Override
    public CertificationMaleViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CertificationMaleViewModel.class);
    }

//    @Override
//    public boolean onBackPressed() {
//        return true;
//    }
}
