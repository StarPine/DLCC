package com.aliyun.svideo.beauty.queen.adapter.holder;

import android.content.Context;
import android.view.View;

import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyDetailClickListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyShapeTypeChangeListener;
import com.aliyun.svideo.beauty.queen.view.skin.AlivcBeautyShapeSettingView;

public class QueenSkinViewHolder extends BaseCaptionViewHolder {

    private final OnBeautyChooserCallback mOnBeautyChooserCallback;
    private AlivcBeautyShapeSettingView mAlivcBeautyShapeSettingView;
    private int mCurrentLevel = 0;

    public QueenSkinViewHolder(Context context, int currentLevel, OnBeautyChooserCallback mOnBeautyChooserCallback) {
        super(context);
        mCurrentLevel = currentLevel;
        this.mOnBeautyChooserCallback = mOnBeautyChooserCallback;
    }

    @Override
    public View onCreateView(Context context) {
        mAlivcBeautyShapeSettingView = new AlivcBeautyShapeSettingView(context);
        return mAlivcBeautyShapeSettingView;
    }

    @Override
    public void onBindViewHolder() {
        mAlivcBeautyShapeSettingView.setDefaultSelect(mCurrentLevel);
        mAlivcBeautyShapeSettingView.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick(int level) {
                if (mOnBeautyChooserCallback != null) {
                    mOnBeautyChooserCallback.onShowSkinDetailView(level);
                }

            }
        });

        mAlivcBeautyShapeSettingView.setOnBeautyShapeLevelChangeListener(new OnBeautyShapeTypeChangeListener() {
            @Override
            public void onShapeTypeChange(int type) {
                if (mOnBeautyChooserCallback != null) {
                    mOnBeautyChooserCallback.onShapeTypeChange(type);
                }
            }
        });
    }
}
