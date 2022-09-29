package com.aliyun.svideo.beauty.queen.inteface;

import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;

public interface OnBeautyChooserCallback {
    void onShowFaceDetailView(int level);
    void onShowSkinDetailView(int type);
    void onChooserBlankClick();
    void onChooserBackClick();
    void onChooserKeyBackClick();
    void onFaceLevelChanged(int level);
    void onBeautyFaceChange(QueenBeautyFaceParams param);
    void onBeautyShapeChange(QueenBeautyShapeParams param);
    void onShapeTypeChange(int type);

}
