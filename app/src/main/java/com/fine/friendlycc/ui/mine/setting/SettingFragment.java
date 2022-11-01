package com.fine.friendlycc.ui.mine.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentSettingBinding;
import com.fine.friendlycc.tim.TUIUtils;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.mine.trtctest.TrtcTestFragment;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.tencent.imsdk.v2.V2TIMCallback;

/**
 * @author wulei
 */
public class SettingFragment extends BaseToolbarFragment<FragmentSettingBinding, SettingViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SettingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(SettingViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.bindMobile.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
//                MVDialog.getInstance(SettingFragment.this.getContext())
//                        .setContent(getString(R.string.are_you_updata_phone))
//                        .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
//                            @Override
//                            public void confirm(MVDialog dialog) {
//                                startContainerActivity(BindMobileFragment.class.getCanonicalName());
//                            }
//                        })
//                        .setConfirmText(getString(R.string.conflirm_modification))
//                        .chooseType(MVDialog.TypeEnum.CENTER)
//                        .show();
            }
        });

        viewModel.uc.clickLogout.observe(this, aVoid -> MVDialog.getInstance(SettingFragment.this.getContext())
                .setContent(getString(R.string.playfun_conflirm_log_out))
                .setConfirmOnlick(dialog -> {
                    TUIUtils.logout(new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            viewModel.logout();
                        }

                        @Override
                        public void onError(int i, String s) {
                            viewModel.logout();
                        }
                    });
                })
                .chooseType(MVDialog.TypeEnum.CENTERWARNED)
                .show());

        viewModel.uc.clickClearCache.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });
        viewModel.loadUserInfo();
    }

    @Override
    public void initData() {
        super.initData();
        binding.trtcSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getTestDeviceInfo(mActivity);
                start(new TrtcTestFragment());
            }
        });

        if (!BuildConfig.DEBUG) {
            binding.trtcSpeed.setVisibility(View.GONE);
        }
    }

//    public static String[] getTestDeviceInfo(Context context){
//        String[] deviceInfo = new String[2];
//        try {
//            if(context != null){
//                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
//                deviceInfo[1] = DeviceConfig.getMac(context);
//            }
//        } catch (Exception e){
//        }
//        return deviceInfo;
//    }
}
