package com.dl.playfun.ui.mine.profile;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentPerfectProfileBinding;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.ui.login.register.RegisterSexFragment;
import com.dl.playfun.utils.ApiUitl;
import com.dl.playfun.utils.PictureSelectorUtil;
import com.dl.playfun.widget.dialog.TraceDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;


/**
 * Author: 彭石林
 * Time: 2022/4/4 11:19
 * Description: This is PerfectProfileFragment
 */
public class PerfectProfileFragment extends BaseFragment<FragmentPerfectProfileBinding, PerfectProfileViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_perfect_profile;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.getNickName();
    }

    @Override
    public PerfectProfileViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(PerfectProfileViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        if (AppConfig.overseasUserEntity != null) {
            viewModel.UserName.set(AppConfig.overseasUserEntity.getName());
            viewModel.UserAvatar.set(AppConfig.overseasUserEntity.getPhoto());
        }
        viewModel.uc.clickAvatar.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                clearNicknameFocus();
                chooseAvatar();
            }
        });

        viewModel.uc.verifyAvatar.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (AppConfig.overseasUserEntity != null) {
                    saveOverseas();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("avatar", viewModel.UserAvatar.get());
                    bundle.putString("name", viewModel.UserName.get());
                    viewModel.start(RegisterSexFragment.class.getCanonicalName(), bundle);
                }
            }
        });
        viewModel.uc.nicknameDuplicate.observe(this, name -> {
            TraceDialog.getInstance(mActivity)
                    .setTitle(String.format(getString(R.string.playfun_duplicate_nickname_tips), name))
                    .setCannelText(getString(R.string.cancel))
                    .setConfirmText(getString(R.string.playfun_mine_trace_delike_confirm))
                    .chooseType(TraceDialog.TypeEnum.CENTER)
                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(Dialog dialog) {
                            viewModel.UserName.set(name);
                            dialog.dismiss();
                        }
                    }).show();
        });


    }


    /**
     * @return void
     * @Desc TODO(讲第三方头像转成本地头像)
     * @author 彭石林
     * @parame []
     * @Date 2022/7/15
     */
    public void saveOverseas() {
        binding.imgAvatar.buildDrawingCache(true);
        binding.imgAvatar.buildDrawingCache();
        Bitmap bitmap = binding.imgAvatar.getDrawingCache();
        String filename = ApiUitl.getDiskCacheDir(getContext()) + "/Overseas" + ApiUitl.getDateTimeFileName() + ".jpg";
        ApiUitl.saveBitmap(bitmap, filename, flag -> {
            if (flag) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", filename);
                bundle.putString("name", viewModel.UserName.get());
                viewModel.start(RegisterSexFragment.class.getCanonicalName(), bundle);
            } else {
                ToastUtils.showShort(R.string.playfun_fragment_perfect_avatar1);
            }
        });
    }

    //选择头像
    private void chooseAvatar() {
        PictureSelectorUtil.selectImageAndCrop(mActivity, true, 1, 1, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                clearNicknameFocus();
                viewModel.UserAvatar.set(result.get(0).getCutPath());
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void clearNicknameFocus() {
        if (binding.editNickname.isFocused()) {
            binding.editNickname.clearFocus();
        }
    }
}
