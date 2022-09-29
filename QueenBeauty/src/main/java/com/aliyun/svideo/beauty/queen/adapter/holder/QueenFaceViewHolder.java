package com.aliyun.svideo.beauty.queen.adapter.holder;

import android.content.Context;
import android.view.View;

import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyDetailClickListener;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyFaceLevelChangeListener;
import com.aliyun.svideo.beauty.queen.view.face.AlivcBeautyFaceSettingView;

public class QueenFaceViewHolder extends BaseCaptionViewHolder {
    private final OnBeautyChooserCallback mOnBeautyChooserCallback;
    private int mCurrentLevel = 3;

    public QueenFaceViewHolder(Context context, int currentLevel, OnBeautyChooserCallback onBeautyChooserCallback) {
        super(context);
        mCurrentLevel = currentLevel;
        mOnBeautyChooserCallback = onBeautyChooserCallback;
    }


    @Override
    public View onCreateView(Context context) {
        return new AlivcBeautyFaceSettingView(context);
    }

    @Override
    public void onBindViewHolder() {
        AlivcBeautyFaceSettingView itemView = (AlivcBeautyFaceSettingView) getItemView();
        itemView.setDefaultSelect(mCurrentLevel);
        itemView.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick(int level) {
                if (mOnBeautyChooserCallback != null) {
                    mOnBeautyChooserCallback.onShowFaceDetailView(level);
                }
            }
        });

        itemView.setOnBeautyFaceLevelChangeListener(new OnBeautyFaceLevelChangeListener() {
            @Override
            public void onLevelChanged(int level) {
                if (mOnBeautyChooserCallback != null) {
                    mOnBeautyChooserCallback.onFaceLevelChanged(level);
                }
            }
        });

    }
}
