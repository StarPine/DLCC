package com.dl.playfun.ui.mine.myphotoalbum;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.ui.base.BaseRefreshToolbarFragment;
import com.dl.playfun.ui.mine.photosetting.PhotoSettingFragment;
import com.dl.playfun.utils.PictureSelectorUtil;
import com.dl.playfun.widget.bottomsheet.BottomSheet;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentMyPhotoAlbumBinding;
import com.tencent.qcloud.tuicore.Status;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 相册
 *
 * @author wulei
 */
public class MyPhotoAlbumFragment extends BaseRefreshToolbarFragment<FragmentMyPhotoAlbumBinding, MyPhotoAlbumViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_my_photo_album;
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MyPhotoAlbumViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(MyPhotoAlbumViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        binding.refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickUploadPhoto.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer sex) {
                if (sex == 1) {
                    choosePhoto();
                } else {
                    showChoosePhotoSheet();
                }
            }
        });
    }

    private void showChoosePhotoSheet() {
        String[] items = new String[]{getString(R.string.playfun_chat_action_picture), getString(R.string.playfun_video)};
        new BottomSheet.Builder(mActivity)
                .setType(BottomSheet.BOTTOM_SHEET_TYPE_NORMAL)
                .setDatas(items)
                .setOnItemSelectedListener((bottomSheet, position) -> {
                    bottomSheet.dismiss();
                    if (position == 0) {
                        choosePhoto();
                    } else if (position == 1) {
                        if (Status.mIsShowFloatWindow){
                            ToastUtils.showShort(R.string.audio_in_call);
                            return;
                        }
                        chooseVideo();
                    }
                }).setCancelButton(getString(R.string.playfun_cancel), new BottomSheet.CancelClickListener() {
            @Override
            public void onCancelClick(BottomSheet bottomSheet) {
                bottomSheet.dismiss();
            }
        }).build().show();
    }

    //選擇圖片
    private void choosePhoto() {
        int max = 24 - viewModel.totalPhoto.get();
        if (max > 9) {
            max = 9;
        }
        PictureSelectorUtil.selectImage(mActivity, true, max, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                if (result.isEmpty()) {
                    return;
                }
                ArrayList<AlbumPhotoEntity> albumPhotoList = new ArrayList<>();
                for (LocalMedia localMedia : result) {
                    AlbumPhotoEntity albumPhotoEntity = new AlbumPhotoEntity();
                    albumPhotoEntity.setIsBurn(1);
                    if (PictureMimeType.MIME_TYPE_IMAGE.equals(localMedia.getMimeType()) || PictureMimeType.PNG_Q.equals(localMedia.getMimeType())) {
                        albumPhotoEntity.setType(1);
                        albumPhotoEntity.setSrc(localMedia.getCompressPath());
                    } else if (PictureMimeType.MIME_TYPE_VIDEO.equals(localMedia.getMimeType())) {
                        albumPhotoEntity.setType(2);
                        albumPhotoEntity.setSrc(localMedia.getCompressPath());
                    } else {
                        break;
                    }
                    albumPhotoEntity.setLocalUpdate(true);
                    albumPhotoList.add(albumPhotoEntity);
                }
                if (albumPhotoList.size() == 0) {
                    return;
                }
                Bundle bundle = PhotoSettingFragment.getStartBundle(PhotoSettingFragment.TYPE_PHOTO_REVIEW, 0, albumPhotoList);
                viewModel.start(PhotoSettingFragment.class.getCanonicalName(), bundle);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    //選擇視頻
    private void chooseVideo() {
        int max = 24 - viewModel.totalPhoto.get();
        if (max > 1) {
            max = 1;
        }
        PictureSelectorUtil.selectVideo(mActivity, false, max, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                if (result.isEmpty()) {
                    return;
                }
                ArrayList<AlbumPhotoEntity> albumPhotoList = new ArrayList<>();
                for (LocalMedia localMedia : result) {
                    AlbumPhotoEntity albumPhotoEntity = new AlbumPhotoEntity();
                    if (PictureMimeType.MIME_TYPE_IMAGE.equals(localMedia.getMimeType())) {
                        albumPhotoEntity.setSrc(localMedia.getRealPath());
                        albumPhotoEntity.setType(1);
                    } else if (PictureMimeType.MIME_TYPE_VIDEO.equals(localMedia.getMimeType())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            albumPhotoEntity.setSrc(localMedia.getAndroidQToPath());
                            if (localMedia.getAndroidQToPath() == null || localMedia.getAndroidQToPath().isEmpty()) {
                                albumPhotoEntity.setSrc(localMedia.getRealPath());
                            }
                        } else {
                            albumPhotoEntity.setSrc(localMedia.getRealPath());
                        }
                        albumPhotoEntity.setType(2);
                    } else {
                        break;
                    }
                    albumPhotoEntity.setLocalUpdate(true);
                    albumPhotoList.add(albumPhotoEntity);
                }
                if (albumPhotoList.size() == 0) {
                    return;
                }
                Bundle bundle = PhotoSettingFragment.getStartBundle(PhotoSettingFragment.TYPE_PHOTO_REVIEW, 0, albumPhotoList);
                viewModel.start(PhotoSettingFragment.class.getCanonicalName(), bundle);
            }

            @Override
            public void onCancel() {
            }
        });
    }
}
