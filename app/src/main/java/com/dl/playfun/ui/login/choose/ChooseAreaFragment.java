package com.dl.playfun.ui.login.choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentChooseAreaBinding;
import com.dl.playfun.ui.base.BaseToolbarFragment;

/**
 * Author: 彭石林
 * Time: 2022/7/5 17:35
 * Description: This is ChooseAreaFragment
 */
public class ChooseAreaFragment extends BaseToolbarFragment<FragmentChooseAreaBinding,ChooseAreaViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_choose_area;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ChooseAreaViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(ChooseAreaViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.getChooseAreaList();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }
}
