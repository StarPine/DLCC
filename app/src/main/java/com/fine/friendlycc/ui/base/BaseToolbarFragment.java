package com.fine.friendlycc.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.BasicToolbar;
import com.fine.friendlycc.R;

/**
 * @author wulei
 */
public abstract class BaseToolbarFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<V, VM> implements BasicToolbar.ToolbarListener {

    protected BasicToolbar basicToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        basicToolbar = view.findViewById(R.id.basic_toolbar);
        if (basicToolbar != null) {
            basicToolbar.setToolbarListener(this);
        }
        return view;
    }

    @Override
    public void onBackClick(BasicToolbar toolbar) {
        mActivity.onBackPressed();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitleBarTitle(String title) {
        if (basicToolbar != null) {
            basicToolbar.setTitle(title);
        }
    }

}
