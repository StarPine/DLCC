package com.fine.friendlycc.ui.dialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;

/**
 * 设置提现账号对话框
 *
 * @author wulei
 */
public class SetWithdrawAccountDialog extends BaseDialogFragment implements View.OnClickListener {

    private final String account;
    private final String name;
    private TextView tvTitle;
    private EditText edtAccount;
    private EditText edtName;
    private Button btnConfirm;
    private ImageView ivClose;
    private SetWithdrawAccountDialogListener setWithdrawAccountDialogListener;

    public SetWithdrawAccountDialog(String account, String name) {
        this.account = account;
        this.name = name;
    }

    public SetWithdrawAccountDialogListener getSetWithdrawAccountDialogListener() {
        return setWithdrawAccountDialogListener;
    }

    public void setSetWithdrawAccountDialogListener(SetWithdrawAccountDialogListener setWithdrawAccountDialogListener) {
        this.setWithdrawAccountDialogListener = setWithdrawAccountDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        edtAccount = view.findViewById(R.id.edt_account);
        edtName = view.findViewById(R.id.edt_name);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        ivClose = view.findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        if (!StringUtils.isEmpty(account)) {
            edtAccount.setText(account);
        }
        if (!StringUtils.isEmpty(name)) {
            edtName.setText(name);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(ConvertUtils.dp2px(300), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_set_withdraw_account;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_dialog_close) {
            dismiss();
        } else if (view.getId() == R.id.btn_confirm) {
            if (setWithdrawAccountDialogListener != null) {
                String account = edtAccount.getText().toString();
                String name = edtName.getText().toString();
                setWithdrawAccountDialogListener.onConfirmClick(this, account, name);
            }
        }
    }

    public interface SetWithdrawAccountDialogListener {
        void onConfirmClick(SetWithdrawAccountDialog dialog, String account, String name);
    }
}
