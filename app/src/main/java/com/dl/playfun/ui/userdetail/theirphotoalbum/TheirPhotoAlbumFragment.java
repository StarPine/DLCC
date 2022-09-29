package com.dl.playfun.ui.userdetail.theirphotoalbum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentTheirPhotoAlbumBinding;

/**
 * 相册
 *
 * @author wulei
 */
public class TheirPhotoAlbumFragment extends BaseToolbarFragment<FragmentTheirPhotoAlbumBinding, TheirPhotoAlbumViewModel> {

    public static final String ARG_USER_ID = "arg_user_id";

    private Integer userId;

    public static Bundle getStartBundle(Integer userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_their_photo_album;
    }

    @Override
    public void initParam() {
        super.initParam();
        userId = getArguments().getInt(ARG_USER_ID);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TheirPhotoAlbumViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        TheirPhotoAlbumViewModel viewModel = ViewModelProviders.of(this, factory).get(TheirPhotoAlbumViewModel.class);
        viewModel.userId.set(userId);
        return viewModel;
    }

}
