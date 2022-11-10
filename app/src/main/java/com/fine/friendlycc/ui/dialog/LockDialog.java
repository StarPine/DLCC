package com.fine.friendlycc.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.widget.NineGridLockView;
import com.fine.friendlycc.R;

/**
 * 平台使用规范对话框
 *
 * @author wulei
 */
public class LockDialog extends BaseDialogFragment {

    private NineGridLockView lockView;
    private ImageView ivContent;
    private TextView tvExpain;
    private TextView tvLogout;


    private LockDialogListener lockDialogListener;
    private String password;

    public LockDialogListener getLockDialogListener() {
        return lockDialogListener;
    }

    public void setLockDialogListener(LockDialogListener lockDialogListener) {
        this.lockDialogListener = lockDialogListener;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lockView = view.findViewById(R.id.lock);
        ivContent = view.findViewById(R.id.iv_content);
        tvExpain = view.findViewById(R.id.tv_expain);
        tvLogout = view.findViewById(R.id.tv_logout);

        tvLogout.setOnClickListener(view1 -> {
            if (lockDialogListener != null) {
                lockDialogListener.onLogoutClick(LockDialog.this);
            }
        });

        lockView.setBack(inputPassword -> {
            if (StringUtils.isEmpty(inputPassword)) {
                return;
            }
            if (password.equals(inputPassword)) {
                if (lockDialogListener != null) {
                    lockDialogListener.onVerifySuccess(LockDialog.this);
                } else {
                    dismiss();
                }
            } else {
                tvExpain.setVisibility(View.VISIBLE);
                tvExpain.setTextColor(ColorUtils.getColor(R.color.red_7c));
                tvExpain.setText(getString(R.string.playcc_draw_wrong_again_draw));
                CountDownTimer timer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        tvExpain.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }

        });
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
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_lock;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public interface LockDialogListener {
        void onLogoutClick(LockDialog dialog);

        void onVerifySuccess(LockDialog dialog);
    }
}
