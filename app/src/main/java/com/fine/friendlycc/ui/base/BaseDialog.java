package com.fine.friendlycc.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.fine.friendlycc.widget.dialog.loading.DialogLoading;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Author: 彭石林
 * Time: 2021/9/29 14:41
 * Description: This is BaseDialog
 */
public class BaseDialog  extends Dialog implements Consumer<Disposable>, LifecycleOwner {

    private DialogLoading dialogLoading = null;

    private CompositeDisposable mCompositeDisposable;

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        mCompositeDisposable = new CompositeDisposable();
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

    @Override
    public void dismiss() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.dismiss();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    /**
     * {@link DialogInterface.OnDismissListener}
     */
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    private void showHud(String title) {
        if (dialogLoading == null) {
            dialogLoading = new DialogLoading(this.getContext());
        }
        dialogLoading.show();
    }

    public void showHud() {
        showHud("");
    }

    public void dismissHud() {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
    }
}
