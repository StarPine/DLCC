package com.dl.playfun.ui.splash;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ColorUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentSplashBinding;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.utils.AutoSizeUtils;


/**
 * @author wulei
 */
public class SplashFragment extends BaseFragment<FragmentSplashBinding, SplashViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(getResources());
        return R.layout.fragment_splash;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SplashViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(SplashViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        String txt = getString(R.string.splash_init_tv);
        String txt2 = getString(R.string.splash_init_tv2);
        int whiteLength = txt.length() - txt2.length();
        SpannableString stringBuilder = new SpannableString(txt);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.white));
        ForegroundColorSpan redSpan = new ForegroundColorSpan(ColorUtils.getColor(R.color.red_9));
        stringBuilder.setSpan(whiteSpan, 0, whiteLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(redSpan, whiteLength, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new UnderlineSpan(), whiteLength, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvInit.setText(stringBuilder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //View销毁时会执行，同时取消所有异步任务
        AutoSizeUtils.closeAdapt(getResources());
    }
}
