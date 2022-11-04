package com.fine.friendlycc.ui.home.active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentActiveBinding;
import com.fine.friendlycc.ui.base.BaseRefreshFragment;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/4 11:00
 */
public class HomeFristTabFragment extends BaseRefreshFragment<FragmentActiveBinding, HomeFristTabViewModel> {

    private int index;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_active;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle arguments = getArguments();
        index = arguments.getInt("index");
    }

    @Override
    public HomeFristTabViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(HomeFristTabViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.index = index;
        viewModel.loadDatas(1);
    }
}
