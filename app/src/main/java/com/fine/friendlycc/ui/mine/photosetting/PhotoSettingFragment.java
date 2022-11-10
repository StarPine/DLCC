package com.fine.friendlycc.ui.mine.photosetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.dialog.PhotoDialog;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentPhotoSettingBinding;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;

/**
 * 照片属性设置
 *
 * @author wulei
 */
public class PhotoSettingFragment extends BaseToolbarFragment<FragmentPhotoSettingBinding, PhotoSettingViewModel> {
    public static final int TYPE_PHOTO_REVIEW = 10010;
    public static final int TYPE_PHOTO_SETTING = 10011;

    public static final String ARG_TYPE = "arg_type";
    public static final String ARG_PHOTOS_INDEX = "arg_photos_index";
    public static final String ARG_PHOTOS = "arg_photos";

    private int type;
    private int index;
    private ArrayList<AlbumPhotoEntity> photos;

    public static Bundle getStartBundle(int type, int index, ArrayList<AlbumPhotoEntity> albumPhotoList) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TYPE, type);
        bundle.putInt(ARG_PHOTOS_INDEX, index);
        bundle.putParcelableArrayList(ARG_PHOTOS, albumPhotoList);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.fragment_photo_setting;
    }

    @Override
    public void initParam() {
        super.initParam();
        type = getArguments().getInt(ARG_TYPE, 0);
        index = getArguments().getInt(ARG_PHOTOS_INDEX, 0);
        photos = getArguments().getParcelableArrayList(ARG_PHOTOS);
        if (type == 0) {
            pop();
        }
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public PhotoSettingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        PhotoSettingViewModel viewModel = ViewModelProviders.of(this, factory).get(PhotoSettingViewModel.class);
        viewModel.setPhotos(type, index, photos);
        return viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickDelete.observe(this, index -> {
            String src = viewModel.items.get(index).itemEntity.get().getSrc();
            String title = null;
            if (src.toLowerCase().endsWith(".jpg") || src.toLowerCase().endsWith(".jpeg") || src.toLowerCase().endsWith(".png")) {
                title = getString(R.string.playcc_dialog_delete_photo_content);
            } else {
                title = getString(R.string.playcc_dialog_delete_video_content);
            }
            MVDialog.getInstance(mActivity)
                    .setTitele(getString(R.string.playcc_dialog_title_prompt))
                    .setContent(title)
                    .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
                        @Override
                        public void confirm(MVDialog dialog) {
                            dialog.dismiss();
                            viewModel.deleteAlbumPhoto(index);
                        }
                    })
                    .chooseType(MVDialog.TypeEnum.CENTER)
                    .show();
        });
        viewModel.uc.clickPhotoCoverAlert.observe(this, unused -> {
            PhotoDialog.alertPhotoCover(getContext());
        });
    }

    @Override
    public void initData() {
        super.initData();
        binding.viewPager.setOffscreenPageLimit(5);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }
}
