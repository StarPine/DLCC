package com.aliyun.svideo.beauty.queen.view.face;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.aliyun.svideo.base.BaseChooser;
import com.aliyun.svideo.beauty.queen.R;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyParamsChangeListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBlankViewClickListener;

/**
 * 美颜微调
 *
 */
public class BeautyFaceDetailSettingChooser extends BaseChooser implements OnBlankViewClickListener, OnBeautyParamsChangeListener , DialogInterface.OnKeyListener {

    private AlivcBeautyDetailSettingView beautyDetailSettingView;
    private QueenBeautyFaceParams mBeautyParams;
    private OnBeautyChooserCallback mOnBeautyChooserCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return new AlivcBeautyDetailSettingView(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        beautyDetailSettingView = (AlivcBeautyDetailSettingView) view;
        beautyDetailSettingView.setBeautyParamsChangeListener(this);
        beautyDetailSettingView.setOnBlankViewClickListener(this);
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
        beautyDetailSettingView.setParams(mBeautyParams);
    }

    /**
     * dialog空白区域点击事件
     */
    @Override
    public void onBlankClick() {
        if (mOnBeautyChooserCallback != null){
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




    public void setBeautyParams(QueenBeautyFaceParams mBeautyParams) {
        this.mBeautyParams = mBeautyParams;
    }

    @Override
    public void onBeautyFaceChange(QueenBeautyFaceParams param) {
        if (mOnBeautyChooserCallback != null){
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
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }
}
