package com.dl.playfun.ui.ranklisk.ranklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentRankListBinding;

/**
 * 排行榜
 *
 * @author wulei
 */
public class RankListFragment extends BaseRefreshToolbarFragment<FragmentRankListBinding, RankListViewModel> {

    public static Bundle getStartBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_rank_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RankListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        RankListViewModel viewModel = ViewModelProviders.of(this, factory).get(RankListViewModel.class);
        return viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }

    @Override
    public void initData() {
        super.initData();
    }

}
