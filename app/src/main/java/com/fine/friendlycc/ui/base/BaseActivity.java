package com.fine.friendlycc.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.fine.friendlycc.R;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.widget.dialog.loading.DialogLoading;
import com.fine.friendlycc.widget.dialog.loading.DialogProgress;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Map;

/**
 * Author: 彭石林
 * Time: 2022/7/29 17:05
 * Description: This is BaseActivity
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends me.goldze.mvvmhabit.base.BaseActivity<V, VM> {
    private DialogLoading dialogLoading;
    private DialogProgress dialogProgress;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig!=null){
            LocaleManager.setLocal(this);
        }
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocal(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LocaleManager.setLocal(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View statusView = findViewById(R.id.status_bar_view);
        if (statusView != null) {
            ImmersionBar.setStatusBarView(this, statusView);
        }
        if (viewModel != null) {
            viewModel.onViewCreated();
        }
    }

    @Override
    protected void registorUIChangeLiveDataCallBack() {
        super.registorUIChangeLiveDataCallBack();
        viewModel.getMuc().showHudEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showHud(s);
            }
        });
        viewModel.getMuc().showProgressHudEvent.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> map) {
                String title = (String) map.get("title");
                int progress = (int) map.get("progress");
                showProgressHud(title, progress);
            }
        });
        viewModel.getMuc().dismissHudEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void v) {
                dismissHud();
            }
        });

        viewModel.getMuc().startFragmentEvent.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.FRAGMENT_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startOtherActivity(canonicalName, bundle);
            }
        });
    }

    protected void startOtherActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(this, OtherFragmentActivity.class);
        intent.putExtra(BaseViewModel.ParameterField.FRAGMENT_NAME,canonicalName);
        intent.putExtra(BaseViewModel.ParameterField.BUNDLE,canonicalName);
        startActivity(intent);
    }

    public void showHud() {
        showHud("");
    }

    private void showHud(String title) {
        if (dialogProgress != null && dialogProgress.isShowing()) {
            dialogProgress.dismiss();
        }

        if (dialogLoading == null) {
            dialogLoading = new DialogLoading(this.getContext());
        }
        dialogLoading.show();
    }

    public void showProgressHud(String title, int progress) {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(this.getContext());
        }
        dialogProgress.setProgress(progress);
    }

    public void dismissHud() {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
        if (dialogProgress != null && dialogProgress.isShowing()) {
            dialogProgress.dismiss();
        }
    }

    public void dismissDestroyHud() {
        if (dialogLoading != null) {
            if(dialogLoading.isShowing()){
                dialogLoading.dismiss();
            }
            dialogLoading = null;
        }
        if (dialogProgress != null) {
            if(dialogProgress.isShowing()){
                dialogProgress.dismiss();
            }
            dialogProgress = null;
        }
    }

    @Override
    public void onDestroy() {
        dismissDestroyHud();
        super.onDestroy();
    }

    public Context getContext(){
        return this;
    }
}
