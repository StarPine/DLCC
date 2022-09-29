package com.dl.playfun.ui.dialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;

/**
 * 複製社交賬號dialog
 *
 * @author wulei
 */
public class CopyAccountDialog extends BaseDialogFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvAccount;
    private TextView tvCopy;
    private ViewGroup llAccount;
    private TextView tvChat;
    private Button btnChat;
    private ImageView ivClose;

    private String account;
    private String title;
    private String content;
    private String btnText;

    private CopyAccountDialogListener copyAccountDialogListener;

    public CopyAccountDialog() {
    }

    public CopyAccountDialog(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public CopyAccountDialogListener getCopyAccountDialogListener() {
        return copyAccountDialogListener;
    }

    public void setCopyAccountDialogListener(CopyAccountDialogListener copyAccountDialogListener) {
        this.copyAccountDialogListener = copyAccountDialogListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBtnText(String text) {
        this.btnText = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        tvAccount = view.findViewById(R.id.tv_account);
        tvCopy = view.findViewById(R.id.tv_copy);
        llAccount = view.findViewById(R.id.ll_account);
        tvChat = view.findViewById(R.id.tv_chat);
        ivClose = view.findViewById(R.id.iv_dialog_close);
        btnChat = view.findViewById(R.id.btn_chat);

        ivClose.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
        btnChat.setOnClickListener(this);

        tvTitle.setText(title);
        tvChat.setText(content);
        btnChat.setText(btnText);

        if (!StringUtils.isEmpty(account)) {
            tvAccount.setText(account);
            tvChat.setVisibility(View.GONE);
        } else {
            tvChat.setVisibility(View.VISIBLE);
            llAccount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(mWidthAndHeight[0] - ConvertUtils.dp2px(88), ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_copy_account;
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
        } else if (view.getId() == R.id.tv_copy) {
            if (copyAccountDialogListener != null) {
                copyAccountDialogListener.onCopyClick(this);
            }
        } else if (view.getId() == R.id.btn_chat) {
            if (copyAccountDialogListener != null) {
                copyAccountDialogListener.onChatClick(this);
            }
        }
    }

    public interface CopyAccountDialogListener {
        void onCopyClick(CopyAccountDialog dialog);

        void onChatClick(CopyAccountDialog dialog);
    }
}
