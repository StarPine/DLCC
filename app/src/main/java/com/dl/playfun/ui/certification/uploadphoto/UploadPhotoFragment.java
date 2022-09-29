package com.dl.playfun.ui.certification.uploadphoto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppContext;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.app.AppsFlyerEvent;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.dl.playfun.widget.picchoose.PicChooseItemEntity;
import com.dl.playfun.widget.picchoose.PicChooseView;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentUploadPhotoBinding;

import java.util.List;

/**
 * @author wulei
 */
public class UploadPhotoFragment extends BaseToolbarFragment<FragmentUploadPhotoBinding, UploadPhotoViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_upload_photo;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public UploadPhotoViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(UploadPhotoViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.clickAddPic.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                addPic();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        binding.picChooseView.setMaxSelectNum(1);
        binding.picChooseView.setShowCamera(true);
        binding.picChooseView.setOnMediaOperateListener(new PicChooseView.OnMediaOperateListener() {
            @Override
            public void onMediaChooseCancel() {

            }

            @Override
            public void onMediaChoosed(List<PicChooseItemEntity> medias) {
                if (medias != null && !medias.isEmpty()) {
                    viewModel.selectedPhotoPath.set(medias.get(0));
                    AppContext.instance().logEvent(AppsFlyerEvent.Add_photo);
                }
            }

            @Override
            public void onMediaDelete(List<PicChooseItemEntity> medias, PicChooseItemEntity delMedia) {
                viewModel.selectedPhotoPath.set(null);
            }
        });

//        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            ImageInfo info = new ImageInfo();
//            info.setThumbnailUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1590665691430&di=1089f909852f2d3ab7839436c225c00b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F06%2F20170806115559_nSZym.jpeg");
//            info.setBigImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1590665691430&di=1089f909852f2d3ab7839436c225c00b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F06%2F20170806115559_nSZym.jpeg");
//            imageInfo.add(info);
//        }
//
//        ClickImageGridViewAdapter adapter = new ClickImageGridViewAdapter(getContext(), imageInfo);
//        binding.imageGridView.setAdapter(adapter);
    }

    private void addPic() {
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .forResult(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(List<LocalMedia> result) {
//                        System.out.println("");
//                        GlideEngine.createGlideEngine().loadImage(getContext(),result.get(0).getPath(),binding.ivPic);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        System.out.println("");
//                    }
//                });

    }

}
