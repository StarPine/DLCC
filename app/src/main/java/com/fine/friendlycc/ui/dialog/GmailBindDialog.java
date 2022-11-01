package com.fine.friendlycc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatImageView;

import com.fine.friendlycc.R;

/**
 * Author: 彭石林
 * Time: 2022/5/19 12:20
 * Description: This is GmailBindDialog
 */
public class GmailBindDialog {

    /**
     * 弹出邮箱绑定提示
     * @param context
     * @param touchOutside
     * @param onClickInterface
     * @return
     */
    public static Dialog getHintEmailDialog(Context context, boolean touchOutside,OnClickInterface onClickInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_account_email_hint, null);

        Button contentBtn = contentView.findViewById(R.id.btn_submit);
        AppCompatImageView imgClose = contentView.findViewById(R.id.img_close);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        contentBtn.setOnClickListener(v -> {
            onClickInterface.confirm();
//                ToastUtils.showShort("确定");
        });
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(contentView);
        return dialog;
    }

    public interface OnClickInterface{
        void confirm();
    }

}
