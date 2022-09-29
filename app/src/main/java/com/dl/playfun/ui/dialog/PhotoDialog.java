package com.dl.playfun.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.playfun.R;
import com.dl.playfun.databinding.AlertPhoneCoverBinding;
import com.dl.playfun.entity.EvaluateItemEntity;
import com.dl.playfun.ui.message.chatdetail.ChatDetailEvaluateAdapter;

/**
 * Author: 彭石林
 * Time: 2022/8/2 15:44
 * Description: 视频、图片设置封面提示
 */
public class PhotoDialog {
    //视频、图片设置封面提示
    public static void alertPhotoCover(Context mContext){
        Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        AlertPhoneCoverBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_phone_cover, null, false);
        binding.btnConfirm.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }
}
