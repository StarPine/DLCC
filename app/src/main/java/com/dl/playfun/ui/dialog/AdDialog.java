package com.dl.playfun.ui.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dl.playfun.app.GlideEngine;
import com.dl.playfun.entity.BannerItemEntity;
import com.dl.playfun.utils.StringUtil;
import com.dl.playfun.R;

/**
 * 平台使用规范对话框
 *
 * @author wulei
 */
public class AdDialog extends  BaseDialogFragment implements View.OnClickListener {

    private ImageView ivImage;
    private ImageView ivClose;

    private BannerItemEntity bannerItemEntity;

    private AdDialogListener adDialogListener;

    public static AdDialog newInstance(BannerItemEntity bannerItemEntity) {
        AdDialog dialog = new AdDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("banner", bannerItemEntity);
        dialog.setArguments(bundle);
        return dialog;
    }

    public AdDialogListener getAdDialogListener() {
        return adDialogListener;
    }

    public void setAdDialogListener(AdDialogListener adDialogListener) {
        this.adDialogListener = adDialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bannerItemEntity = (BannerItemEntity) getArguments().getSerializable("banner");
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
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_ad;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivImage = view.findViewById(R.id.iv_image);
        ivClose = view.findViewById(R.id.iv_dialog_close);

        ivClose.setOnClickListener(this);
        ivImage.setOnClickListener(this);

        if (bannerItemEntity != null) {
            GlideEngine.createGlideEngine().loadImage(getContext(), StringUtil.getFullImageUrl(bannerItemEntity.getImg()), ivImage);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_dialog_close) {
            if (adDialogListener != null) {
                adDialogListener.onCloseClick(this, bannerItemEntity);
            } else {
                dismiss();
            }
        } else if (view.getId() == R.id.iv_image) {
            if (adDialogListener != null) {
                adDialogListener.onImageClick(this, bannerItemEntity);
            } else {
                dismiss();
            }
        }
    }

    public interface AdDialogListener {

        void onImageClick(AdDialog dialog, BannerItemEntity entity);

        void onCloseClick(AdDialog dialog, BannerItemEntity entity);

    }
}
