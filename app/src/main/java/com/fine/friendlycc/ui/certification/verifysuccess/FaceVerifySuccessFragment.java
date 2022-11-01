package com.fine.friendlycc.ui.certification.verifysuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentFaceVerifySuccessBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;

/**
 * @author wulei
 */
public class FaceVerifySuccessFragment extends BaseToolbarFragment<FragmentFaceVerifySuccessBinding, FaceVerifySuccessViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_face_verify_success;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FaceVerifySuccessViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(FaceVerifySuccessViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        if (basicToolbar != null) {
            basicToolbar.hiddenBack(true);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }

    @Override
    public boolean onBackPressedSupport() {
        return true;
    }

}
