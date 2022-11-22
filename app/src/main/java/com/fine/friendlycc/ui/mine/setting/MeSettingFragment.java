package com.fine.friendlycc.ui.mine.setting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentMeSettingBinding;
import com.fine.friendlycc.bean.VersionBean;
import com.fine.friendlycc.calling.view.VideoPresetActivity;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.widget.dialog.version.view.UpdateDialogView;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.tencent.qcloud.tuicore.util.BackgroundTasks;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2021/10/25 14:38
 * Description: This is MeSettingFragment
 */
public class MeSettingFragment extends BaseToolbarFragment<FragmentMeSettingBinding, MeSettingViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return R.layout.fragment_me_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public MeSettingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MeSettingViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
    }

    ActivityResultLauncher<String> toPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            Intent intent = new Intent(mActivity, VideoPresetActivity.class);
            mActivity.startActivity(intent);
        } else {
            Toast.makeText(_mActivity, R.string.picture_camera, Toast.LENGTH_SHORT).show();
            if (!ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 只要有一个权限没有被授予, 则直接返回 false
                PermissionChecker.launchAppDetailsSettings(getContext());
            }
        }
    });

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //美颜点击
        viewModel.uc.starFacebeautyActivity.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aBoolean) {
                toPermissionIntent.launch(Manifest.permission.CAMERA);
            }
        });
        viewModel.uc.versionEntitySingl.observe(this, new Observer<VersionBean>() {
            @Override
            public void onChanged(VersionBean versionEntity) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (versionEntity.getVersion_code().intValue() <= AppConfig.VERSION_CODE.intValue()) {
                            ToastUtils.showShort(R.string.playcc_version_latest);
                        } else {
                            boolean isUpdate = versionEntity.getIs_update().intValue() == 1;
                            UpdateDialogView.getInstance(mActivity)
                                    .getUpdateDialogView(versionEntity.getVersion_name(),
                                            versionEntity.getContent(),
                                            versionEntity.getUrl(),
                                            isUpdate, "playchat", versionEntity.getLinkUrl()).show();
                        }
                    }
                });
            }
        });
    }
}