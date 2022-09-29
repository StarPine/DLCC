package com.dl.playfun.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.KeyboardUtils;
import com.dl.playfun.R;
import com.dl.playfun.utils.Utils;
import com.dl.playfun.widget.dialog.loading.DialogLoading;
import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author wulei
 */
public abstract class BaseDialogFragment extends DialogFragment implements Consumer<Disposable> {
    public Integer[] mWidthAndHeight;
    protected Activity mActivity;
    protected View mRootView;
    protected Window mWindow;
    private CompositeDisposable mCompositeDisposable;
    //加载进度条
    private DialogLoading dialogLoading = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
        initDisposable();
    }

    private void initDisposable() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);
        mWindow = dialog.getWindow();
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initData();
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardUtils.hideSoftInput(mActivity);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWidthAndHeight = Utils.getWidthAndHeight(mWindow);
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
//                .navigationBarDarkIcon(false)
                .autoNavigationBarDarkModeEnable(true)
//                .navigationBarColor(R.color.black)
                .keyboardEnable(false)
                .init();

//        ImmersionBar.with(this)
////                .titleBar(basic_toolbar)
////                .navigationBarColor(R.color.btn4)
//                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                .init();
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        if (disposable != null) {
            addSubscribe(disposable);
        }
    }

    public void showHUD(){
        if (dialogLoading == null) {
            dialogLoading = new DialogLoading(this.getContext());
        }
        dialogLoading.show();
    }

    public void dismissHud() {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
    }

}
