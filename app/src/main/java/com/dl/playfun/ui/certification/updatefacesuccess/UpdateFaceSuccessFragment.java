package com.dl.playfun.ui.certification.updatefacesuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentUpdateFaceSuccessBinding;
import com.dl.playfun.ui.base.BaseToolbarFragment;

/**
 * @author wulei
 */
public class UpdateFaceSuccessFragment extends BaseToolbarFragment<FragmentUpdateFaceSuccessBinding, UpdateFaceSuccessViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_update_face_success;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public UpdateFaceSuccessViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(UpdateFaceSuccessViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        if (basicToolbar != null) {
            basicToolbar.hiddenBack(true);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return true;
    }
}
