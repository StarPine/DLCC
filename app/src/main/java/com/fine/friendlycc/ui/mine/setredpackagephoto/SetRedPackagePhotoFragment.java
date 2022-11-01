package com.fine.friendlycc.ui.mine.setredpackagephoto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentSetRedPackagePhotoBinding;

/**
 * 相册
 *
 * @author wulei
 */
public class SetRedPackagePhotoFragment extends BaseToolbarFragment<FragmentSetRedPackagePhotoBinding, SetRedPackagePhotoViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_set_red_package_photo;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SetRedPackagePhotoViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(SetRedPackagePhotoViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }

}
