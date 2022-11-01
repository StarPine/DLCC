package com.fine.friendlycc.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.fine.friendlycc.viewmodel.BaseRefreshViewModel;
import com.fine.friendlycc.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
 * @author wulei
 */
public abstract class BaseRefreshFragment<V extends ViewDataBinding, VM extends BaseRefreshViewModel> extends BaseFragment<V, VM> {

    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        return view;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //监听下拉刷新完成
        viewModel.uc.startRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                refreshLayout.autoRefresh();
            }
        });

        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                refreshLayout.finishRefresh(true);
            }
        });

        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                refreshLayout.finishLoadMore(true);
            }
        });
    }
}
