package com.fine.friendlycc.ui.mine.likelist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentLikeListBinding;

/**
 * 我喜欢的
 *
 * @author wulei
 */
public class LikeListFragment extends BaseToolbarFragment<FragmentLikeListBinding, LikeListViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_like_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LikeListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(LikeListViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.clickDelLike.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer unused) {
                TraceDialog.getInstance(LikeListFragment.this.getContext())
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
        viewModel.uc.clickAddLike.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });
        viewModel.uc.loadRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                binding.title.setText(String.format(StringUtils.getString(R.string.playfun_mine_trace_man_title2), viewModel.totalCount));
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
