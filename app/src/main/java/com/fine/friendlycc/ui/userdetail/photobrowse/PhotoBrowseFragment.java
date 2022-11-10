package com.fine.friendlycc.ui.userdetail.photobrowse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentPhotoBrowseBinding;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.ui.base.BaseFragment;
import com.fine.friendlycc.widget.coinpaysheet.CoinPaySheet;
import com.fine.friendlycc.widget.coinrechargesheet.CoinRechargeSheetView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 照片浏览
 *
 * @author wulei
 */
public class PhotoBrowseFragment extends BaseFragment<FragmentPhotoBrowseBinding, PhotoBrowseViewModel> {
    public static final int TYPE_ALBUM = 1;
    public static final int TYPE_CHAT = 2;
    public static final int TYPE_APPLY_MESSAGE = 3;

    public static final String ARG_BROWSE_TYPE = "arg_browse_type";
    public static final String ARG_BROWSE_INDEX = "arg_browse_index";
    public static final String ARG_BROWSE_PHOTOS = "arg_browse_photos";

    private int type;
    private int index;
    private ArrayList<AlbumPhotoEntity> photos;

    public static Bundle getStartBundle(int index, ArrayList<AlbumPhotoEntity> photos) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_BROWSE_TYPE, TYPE_ALBUM);
        bundle.putInt(ARG_BROWSE_INDEX, index);
        bundle.putParcelableArrayList(ARG_BROWSE_PHOTOS, photos);
        return bundle;
    }

    public static Bundle getStartChatBundle(AlbumPhotoEntity albumPhotoEntity) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_BROWSE_TYPE, TYPE_CHAT);
        bundle.putInt(ARG_BROWSE_INDEX, 0);
        ArrayList<AlbumPhotoEntity> list = new ArrayList<>();
        list.add(albumPhotoEntity);
        bundle.putParcelableArrayList(ARG_BROWSE_PHOTOS, list);
        return bundle;
    }

    public static Bundle getStartApplyMessageBundle(AlbumPhotoEntity albumPhotoEntity) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_BROWSE_TYPE, TYPE_APPLY_MESSAGE);
        bundle.putInt(ARG_BROWSE_INDEX, 0);
        ArrayList<AlbumPhotoEntity> list = new ArrayList<>();
        list.add(albumPhotoEntity);
        bundle.putParcelableArrayList(ARG_BROWSE_PHOTOS, list);
        return bundle;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_photo_browse;
    }

    @Override
    public void initParam() {
        super.initParam();
        type = getArguments().getInt(ARG_BROWSE_TYPE, TYPE_ALBUM);
        index = getArguments().getInt(ARG_BROWSE_INDEX, 0);
        photos = getArguments().getParcelableArrayList(ARG_BROWSE_PHOTOS);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public PhotoBrowseViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        PhotoBrowseViewModel viewModel = ViewModelProviders.of(this, factory).get(PhotoBrowseViewModel.class);
        viewModel.type.set(type);
        viewModel.setPhotos(index, photos);
        return viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.clickPayRedPackage.observe(this, photoId -> payReaPackagePhoto(photoId));
        viewModel.uc.clickPayRedPackageVideo.observe(this, photoId -> payReaPackageVideo(photoId));
    }

    @Override
    public void initData() {
        super.initData();
        binding.vp.setOffscreenPageLimit(24);
    }

    private void payReaPackagePhoto(int photoId) {
        new CoinPaySheet.Builder(mActivity).setPayParams(4, photoId, getString(R.string.playcc_red_package_photo), false, new CoinPaySheet.CoinPayDialogListener() {
            @Override
            public void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice) {
                sheet.dismiss();
                ToastUtils.showShort(R.string.playcc_pay_success);
                viewModel.payRedPackageSuccess(photoId);
            }
            @Override
            public void toGooglePlayView() {
                toRecharge();
            }
        }).build().show();
    }

    private void payReaPackageVideo(int photoId) {
        new CoinPaySheet.Builder(mActivity).setPayParams(5, photoId, getString(R.string.playcc_red_package_video), false, new CoinPaySheet.CoinPayDialogListener() {
            @Override
            public void onPaySuccess(CoinPaySheet sheet, String orderNo, Integer payPrice) {
                sheet.dismiss();
                ToastUtils.showShort(R.string.playcc_pay_success);
                viewModel.payRedPackageSuccess(photoId);
            }
            @Override
            public void toGooglePlayView() {
                toRecharge();
            }
        }).build().show();
    }

    /**
     * 去充值
     */
    private void toRecharge() {
        CoinRechargeSheetView coinRechargeFragmentView = new CoinRechargeSheetView(mActivity);
        coinRechargeFragmentView.show();
    }

    @Override
    public void onDestroy() {
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onDestroy();
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception e) {

        }
    }
}
