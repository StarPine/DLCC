package com.fine.friendlycc.ui.mine.creenlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.widget.NineGridLockView;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentScreenLockBinding;

public class ScreenLockFragment extends BaseFragment<FragmentScreenLockBinding, ScreenLockViewModel> {

    private ScreenLockViewModel mViewModel;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_screen_lock;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ScreenLockViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(ScreenLockViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.lock.setBack(new NineGridLockView.Back() {
            @Override
            public void backPassword(String password) {
                viewModel.setPassword(password);
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.bindupdata.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                String[] items = new String[]{getString(R.string.playcc_setting_new_password), getString(R.string.playcc_cancel_lock_design)};
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            viewModel.setNewPassword();
                        } else {
                            viewModel.cancelPassword();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle(getString(R.string.playcc_want_set_up));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }
}