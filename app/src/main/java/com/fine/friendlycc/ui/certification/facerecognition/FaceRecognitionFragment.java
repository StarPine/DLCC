package com.fine.friendlycc.ui.certification.facerecognition;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.widget.dialog.MMAlertDialog;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentFaceRecognitionBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tuicore.util.FileUtil;

/**
 * @author wulei
 */
public class FaceRecognitionFragment extends BaseToolbarFragment<FragmentFaceRecognitionBinding, FaceRecognitionViewModel> {

    private static final int REQUEST_CAMERA_1 = 1;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_face_recognition;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FaceRecognitionViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(FaceRecognitionViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.clickStart.observe(this, s -> startVerify());
        viewModel.uc.imageUrlFace.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.imagFaceUpload();
            }
        });
    }

    private void startVerify() {
        //showHud();
        if (ApiUitl.isCameraCanUse()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// ??????????????????
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);//??????????????????
            intent.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1);
            startActivityForResult(intent, REQUEST_CAMERA_1);
        } else {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.setLogging(true);
            rxPermissions.requestEachCombined(Manifest.permission.CAMERA)
                    .subscribe(permission -> { // will emit 1 Permission object
                        if (permission.granted) {
                            // All permissions are granted !
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// ??????????????????
                            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);//??????????????????
                            intent.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1);
                            startActivityForResult(intent, REQUEST_CAMERA_1);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // At least one denied permission without ask never again
                            System.out.println();
                        } else {
                            // At least one denied permission with ask never again
                            // Need to go to the settings
                            System.out.println();
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // ??????????????????
            if (requestCode == REQUEST_CAMERA_1) { // ????????????????????????REQUEST_CAMERA,???????????????????????????????????????????????????????????????
                Bundle bundle = data.getExtras(); // ???data?????????????????????????????????????????????????????????????????????????????????
                Bitmap bitmap = (Bitmap) bundle.get("data"); // ???data????????????????????????Bitmap??????
                try {
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, null, null));
                    MMAlertDialog.RegisterFaceDialog(getContext(), true, uri, new MMAlertDialog.DilodAlertInterface() {
                        @Override
                        public void confirm(DialogInterface dialog, int which, int sel_Index) {
                            dialog.dismiss();
                            String path = FileUtil.saveBitmap("face_image", bitmap);
                            viewModel.uc.imageUrlFace.postValue(path);
                            CCApplication.instance().logEvent(AppsFlyerEvent.Confirm);
                        }

                        @Override
                        public void cancel(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } catch (Exception e) {

                }
            }
        }
    }
}
