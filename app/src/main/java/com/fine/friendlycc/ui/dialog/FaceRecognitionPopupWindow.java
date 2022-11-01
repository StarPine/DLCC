package com.fine.friendlycc.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.fine.friendlycc.R;

/**
 * @author wulei
 */
public class FaceRecognitionPopupWindow extends PopupWindow implements View.OnClickListener {
    private final Activity mActivity;

    private View mPopView;
    private Button btnStart, btnLogout;
    private TextView tvContactService;

    private FaceRecognitionDialogListener faceRecognitionDialogListener;

    public FaceRecognitionPopupWindow(Activity activity) {
        super(activity);
        this.mActivity = activity;
        init(activity);
        setPopupWindow();
    }

    public FaceRecognitionDialogListener getFaceRecognitionDialogListener() {
        return faceRecognitionDialogListener;
    }

    public void setFaceRecognitionDialogListener(FaceRecognitionDialogListener faceRecognitionDialogListener) {
        this.faceRecognitionDialogListener = faceRecognitionDialogListener;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);        //绑定布局
        mPopView = inflater.inflate(R.layout.dialog_face_recognition, null);

        btnStart = mPopView.findViewById(R.id.btn_start);
        btnLogout = mPopView.findViewById(R.id.btn_logout);
        tvContactService = mPopView.findViewById(R.id.tv_contact_service);

        btnStart.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tvContactService.setOnClickListener(this);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
    }

    public void show() {
        if (mActivity != null && !mActivity.isFinishing()) {
            Window dialogWindow = mActivity.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT - SizeUtils.dp2px(60);
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.alpha = 0.5f;
            dialogWindow.setAttributes(lp);
        }
        this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

//
//    @Override
//    public void showAsDropDown(View anchor) {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            Window dialogWindow = mActivity.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.alpha = 0.7f;
//            dialogWindow.setAttributes(lp);
//        }
//        super.showAsDropDown(anchor);
//    }
//
//    @Override
//    public void dismiss() {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            Window dialogWindow = mActivity.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.alpha = 1.0f;
//            dialogWindow.setAttributes(lp);
//        }
//
//        super.dismiss();
//    }

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

        void onStartFaceRecognitionClick(FaceRecognitionPopupWindow dialog);

        void onLogoutClick(FaceRecognitionPopupWindow dialog);

        void onContactServiceClick(FaceRecognitionPopupWindow dialog);
    }
}
