package com.fine.friendlycc.ui.certification.goddesscertification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentGoddessCertificationBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.widget.picchoose.PicChooseItemBean;
import com.fine.friendlycc.widget.picchoose.PicChooseView;

import java.util.List;

/**
 * 女神认证
 *
 * @author wulei
 */
public class GoddessCertificationFragment extends BaseToolbarFragment<FragmentGoddessCertificationBinding, GoddessCertificationViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_goddess_certification;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public GoddessCertificationViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(GoddessCertificationViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();

        binding.picChooseView.setOnMediaOperateListener(new PicChooseView.OnMediaOperateListener() {
            @Override
            public void onMediaChooseCancel() {

            }

            @Override
            public void onMediaChoosed(List<PicChooseItemBean> medias) {
                viewModel.chooseMedias = medias;
            }

            @Override
            public void onMediaDelete(List<PicChooseItemBean> medias, PicChooseItemBean delMedia) {
                viewModel.chooseMedias = medias;
            }
        });


    }

}