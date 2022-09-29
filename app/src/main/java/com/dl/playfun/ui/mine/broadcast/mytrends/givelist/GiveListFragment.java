package com.dl.playfun.ui.mine.broadcast.mytrends.givelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentGiveListBinding;

/**
 * 点赞列表
 *
 * @author litchi
 */
public class GiveListFragment extends BaseRefreshToolbarFragment<FragmentGiveListBinding, GiveListViewModel> {
    private String type;
    private Integer id;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_give_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public GiveListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        GiveListViewModel giveListViewModel = ViewModelProviders.of(this, factory).get(GiveListViewModel.class);
        giveListViewModel.setId(id);
        giveListViewModel.setType(type);
        return giveListViewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        id = getArguments().getInt("id", 0);
        type = getArguments().getString("type");
    }
}