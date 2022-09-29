package com.dl.playfun.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.dl.playfun.R;

/**
 * 人脸识别对话框
 *
 * @author wulei
 */
public class FaceRecognitionDialog extends BaseDialogFragment implements View.OnClickListener {

    private Button btnStart, btnLogout;
    private TextView tvContactService;

    private FaceRecognitionDialogListener faceRecognitionDialogListener;

    public FaceRecognitionDialogListener getFaceRecognitionDialogListener() {
        return faceRecognitionDialogListener;
    }

    public void setFaceRecognitionDialogListener(FaceRecognitionDialogListener faceRecognitionDialogListener) {
        this.faceRecognitionDialogListener = faceRecognitionDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(mWidthAndHeight[0] - ConvertUtils.dp2px(60), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_face_recognition;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = view.findViewById(R.id.btn_start);
        btnLogout = view.findViewById(R.id.btn_logout);
        tvContactService = view.findViewById(R.id.tv_contact_service);

        btnStart.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tvContactService.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start) {
            if (faceRecognitionDialogListener != null) {
                faceRecognitionDialogListener.onStartFaceRecognitionClick(this);
            }
        } else if (view.getId() == R.id.btn_logout) {
            if (faceRecognitionDialogListener != null) {
                faceRecognitionDialogListener.onLogoutClick(this);
            }
        } else if (view.getId() == R.id.tv_contact_service) {
            if (faceRecognitionDialogListener != null) {
                faceRecognitionDialogListener.onContactServiceClick(this);
            }
        }
    }

    public interface FaceRecognitionDialogListener {

        void onStartFaceRecognitionClick(FaceRecognitionDialog dialog);

        void onLogoutClick(FaceRecognitionDialog dialog);

        void onContactServiceClick(FaceRecognitionDialog dialog);
    }
}
