package com.fine.friendlycc.ui.message.photoreview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentPhotoReviewBinding;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * @author wulei
 */
public class PhotoReviewFragment extends BaseToolbarFragment<FragmentPhotoReviewBinding, PhotoReviewViewModel> implements View.OnClickListener {
    public static final String ARG_IMAGE_PATH = "arg_image_path";
    public static final String ARG_IMAGE_SRC_KEY = "arg_image_src_key";

    private String imagePath;

    public static Bundle getStartBundle(String imgPath) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_IMAGE_PATH, imgPath);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_photo_review;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public PhotoReviewViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(PhotoReviewViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        imagePath = getArguments().getString(ARG_IMAGE_PATH);
    }

    @Override
    public void initData() {
        super.initData();
        binding.tvSend.setOnClickListener(this);
        if (imagePath == null) {
            return;
        }
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        GlideEngine.createGlideEngine().loadImage(mActivity, imagePath, binding.photoView);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.uploadPhotoSuccess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String imagePathKey) {
                if (!StringUtils.isEmpty(imagePathKey)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ARG_IMAGE_SRC_KEY, imagePathKey);
                    setFragmentResult(ISupportFragment.RESULT_OK, bundle);
                    pop();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_send) {
            if (imagePath == null) {
                return;
            }
            viewModel.uploadPhoto(imagePath);
        }
    }
}
