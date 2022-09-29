package com.dl.playfun.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.dl.playfun.observable.MvUIChangeObservable;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.base.BaseModel;

/**
 * @author wulei
 */
public class BaseViewModel<M extends BaseModel> extends me.goldze.mvvmhabit.base.BaseViewModel<M> {

    public StateModel stateModel;

    private MvUIChangeObservable muc;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application, model);
        stateModel = new StateModel();
    }

    public MvUIChangeObservable getMuc() {
        if (muc == null) {
            muc = new MvUIChangeObservable();
        }
        return muc;
    }

    public void start(String targetFragmentClass) {
        start(targetFragmentClass, null);
    }

    public void start(String targetFragmentClass, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.FRAGMENT_NAME, targetFragmentClass);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        muc.startFragmentEvent.postValue(params);
    }

    public void startWithPop(String targetFragmentClass) {
        startWithPop(targetFragmentClass, null);
    }

    public void startWithPop(String targetFragmentClass, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.FRAGMENT_NAME, targetFragmentClass);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        muc.startWithPopFragmentEvent.postValue(params);
    }

    public void startWithPopTo(String toFragment, String targetFragmentClass) {
        startWithPopTo(toFragment, targetFragmentClass, false);
    }

    public void startWithPopTo(String toFragment, String targetFragmentClass, Boolean includeTargetFragment) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.TO_FRAGMENT_NAME, toFragment);
        params.put(ParameterField.FRAGMENT_NAME, targetFragmentClass);
        if (includeTargetFragment != null) {
            params.put(ParameterField.INCLUDE_TARGET_FRAGMENT, includeTargetFragment);
        }
        muc.startWithPopToFragmentEvent.postValue(params);
    }

    public void pop() {
        muc.popFragmentEvent.call();
    }

    public void popTo(String targetFragmentClass) {
        popTo(targetFragmentClass, false);
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(String targetFragmentClass, Boolean includeTargetFragment) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.FRAGMENT_NAME, targetFragmentClass);
        if (includeTargetFragment != null) {
            params.put(ParameterField.INCLUDE_TARGET_FRAGMENT, includeTargetFragment);
        }
        muc.popToFragmentEvent.postValue(params);
    }

    public void showHUD() {
        showHUD(null);
    }

    public void showHUD(String message) {
        getMuc().showHudEvent.postValue(message);
    }

    public void showProgressHUD(String title, int progress) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("progress", progress);
        getMuc().showProgressHudEvent.postValue(map);
    }

    public void dismissHUD() {
        getMuc().dismissHudEvent.call();
    }

    public void hideKeyboard() {
        getMuc().hideKeyboardEvent.call();
    }

    public void onViewCreated() {

    }

    public void onLazyInitView() {

    }

    public void onEnterAnimationEnd() {

    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String BUNDLE = "BUNDLE";

        public static String FRAGMENT_NAME = "FRAGMENT_NAME";

        public static String INCLUDE_TARGET_FRAGMENT = "INCLUDE_TARGET_FRAGMENT";

        public static String TO_FRAGMENT_NAME = "TO_FRAGMENT_NAME";
    }
}
