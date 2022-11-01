package com.fine.friendlycc.ui.mine.trace.list;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.mine.trace.TraceViewModel;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentTraceListBinding;

/**
 * Author: 彭石林
 * Time: 2021/8/3 11:32
 * Description: This is TraceListFragment
 */
public class TraceListFragment extends BaseToolbarFragment<FragmentTraceListBinding, TraceListViewModel> {
    public static final String ARG_HOME_LIST_GENDER = "arg_trace_list_gender";

    private int grends;
    private TraceViewModel traceViewModel;

    public static TraceListFragment newInstance(int grend) {
        TraceListFragment fragment = new TraceListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_HOME_LIST_GENDER, grend);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TraceViewModel getTraceViewModel() {
        return traceViewModel;
    }

    public void setTraceListViewModel(TraceViewModel traceViewModel) {
        this.traceViewModel = traceViewModel;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_trace_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        grends = getArguments().getInt(ARG_HOME_LIST_GENDER, 0);
    }

    @Override
    public TraceListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        TraceListViewModel traceListViewModel = ViewModelProviders.of(this, factory).get(TraceListViewModel.class);
        traceListViewModel.grend = grends;
        return traceListViewModel;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            Log.e("从新进入绑定刷新框架","-----");
//            if (binding.refreshLayout!=null){//重新设置监听解决有时候上下拉回调不走 原因监听被覆盖
//                binding.refreshLayout.setEnableLoadMore(true);//启用上拉加载功能
//                binding.refreshLayout.setEnableRefresh(true);
//            }
//        }
//    }

    @Override
    public void initViewObservable() {
        viewModel.traceViewModel = this.traceViewModel;
        viewModel.uc.clickDelLike.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer unused) {
                TraceDialog.getInstance(TraceListFragment.this.getContext())
                        .setTitle(getString(R.string.playfun_mine_trace_delike))
                        .setCannelText(getString(R.string.playfun_mine_trace_delike_cannel))
                        .setConfirmText(getString(R.string.playfun_mine_trace_delike_confirm))
                        .chooseType(TraceDialog.TypeEnum.CENTER)
                        .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                            @Override
                            public void confirm(Dialog dialog) {
                                dialog.dismiss();
                                viewModel.delLike(unused);
                            }
                        }).show();
            }
        });
        viewModel.uc.loadRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                //binding.refreshLayout.finishRefreshWithNoMoreData();
            }
        });

        viewModel.uc.startRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.autoRefresh();
            }
        });

        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishRefresh(100);
            }
        });

        //监听上拉加载完成
        viewModel.uc.finishLoadmore.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                //结束刷新
                binding.refreshLayout.finishLoadMore(100);
            }
        });

    }

}
