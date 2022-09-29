package com.aliyun.svideo.beauty.queen.view.skin;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.aliyun.svideo.base.BaseChooser;
import com.aliyun.svideo.beauty.queen.R;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyParamsChangeListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBlankViewClickListener;


/**
 * 美型微调
 * @author xlx
 */
public class BeautyShapeDetailChooser extends BaseChooser implements OnBeautyParamsChangeListener , OnBlankViewClickListener {
    private QueenBeautyShapeParams mBeautyParams;
    private BeautyShapeDetailSettingView mBeautyShapeDetailSettingView;
    private OnBeautyChooserCallback mOnBeautyChooserCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return new BeautyShapeDetailSettingView(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBeautyShapeDetailSettingView = (BeautyShapeDetailSettingView) view;
        mBeautyShapeDetailSettingView.setOnBeautyParamsChangeListener(this);
        mBeautyShapeDetailSettingView.setOnBlankViewClickListener(this);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    if (mOnBeautyChooserCallback != null){
                        mOnBeautyChooserCallback.onChooserKeyBackClick();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBeautyShapeDetailSettingView != null) {
            mBeautyShapeDetailSettingView.setParams(mBeautyParams);
        }
    }
    public void setBeautyShapeParams(QueenBeautyShapeParams beautyParams) {
        this.mBeautyParams = beautyParams;
    }

    @Override
    public void onBlankClick() {
        if (mOnBeautyChooserCallback != null) {
            mOnBeautyChooserCallback.onChooserBlankClick();
        }
        dismiss();
    }

    @Override
    public void onBackClick() {
        if (mOnBeautyChooserCallback != null) {
            mOnBeautyChooserCallback.onChooserBackClick();
        }
    }

    @Override
    public void onBeautyFaceChange(QueenBeautyFaceParams param) {
        if (mOnBeautyChooserCallback != null) {
            mOnBeautyChooserCallback.onBeautyFaceChange(param);
        }
    }

    @Override
    public void onBeautyShapeChange(QueenBeautyShapeParams param) {
        if (mOnBeautyChooserCallback != null){
            mOnBeautyChooserCallback.onBeautyShapeChange(param);
        }
    }

    public void setOnBeautyChooserCallback(OnBeautyChooserCallback mOnBeautyChooserCallback) {
        this.mOnBeautyChooserCallback = mOnBeautyChooserCallback;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBeautyShapeDetailSettingView != null) {
            mBeautyShapeDetailSettingView.setOnBlankViewClickListener(null);
            mBeautyShapeDetailSettingView.setOnBeautyParamsChangeListener(null);
            mBeautyShapeDetailSettingView = null;
        }
    }
}
