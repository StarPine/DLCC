package com.fine.friendlycc.utils;

import android.app.Activity;
import android.content.Context;

import com.fine.friendlycc.app.GlideEngine;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.lxj.xpopup.XPopup;
import com.fine.friendlycc.R;
import com.fine.friendlycc.widget.dialog.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author litchi
 */
public class PictureSelectorUtil {

    public static final int IMAGE_SPAN_COUNT = 3;

    public static void selectImage(Activity activity, boolean showCamera, int maxSelectNum, OnResultCallbackListener<LocalMedia> callbackListener) {
        if (maxSelectNum < 1 || maxSelectNum > 9) {
            maxSelectNum = 1;
        }
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(showCamera)
                .setLanguage(-1)
                .setPictureWindowAnimationStyle(getPictureWindowAnimationStyle())// 自定义相册启动退出动画
                .theme(R.style.picture_white_style_custom)
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .compressQuality(85)// 图片压缩后输出质量 0~ 100
                .synOrAsy(true)
                .imageSpanCount(IMAGE_SPAN_COUNT)
                .compressFocusAlpha(true)
                .isPreviewImage(false)
                .minimumCompressSize(200)
                .queryMaxFileSize(6)
                .maxSelectNum(maxSelectNum);
        if (maxSelectNum == 1) {
            pictureSelectionModel.selectionMode(PictureConfig.SINGLE);
        }
        pictureSelectionModel.forResult(callbackListener);
    }

    public static void selectImage(Activity activity, boolean showCamera, int maxSelectNum, int outQuality, OnResultCallbackListener<LocalMedia> callbackListener) {
        if (maxSelectNum < 1 || maxSelectNum > 9) {
            maxSelectNum = 1;
        }
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(showCamera)
                .setLanguage(-1)
                .setPictureWindowAnimationStyle(getPictureWindowAnimationStyle())// 自定义相册启动退出动画
                .theme(R.style.picture_white_style_custom)
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .compressQuality(85)// 图片压缩后输出质量 0~ 100
                .isPreviewImage(false)
                .imageSpanCount(IMAGE_SPAN_COUNT)
                .compressFocusAlpha(true)
                .minimumCompressSize(200)
                .queryMaxFileSize(6)
                .maxSelectNum(maxSelectNum);
        if (maxSelectNum == 1) {
            pictureSelectionModel.selectionMode(PictureConfig.SINGLE);
        }
        pictureSelectionModel.forResult(callbackListener);
    }

    public static void selectVideo(Activity activity, boolean showCamera, int maxSelectNum, OnResultCallbackListener<LocalMedia> callbackListener) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .isCamera(showCamera)
                .setLanguage(-1)
                .setPictureWindowAnimationStyle(getPictureWindowAnimationStyle())// 自定义相册启动退出动画
                .imageSpanCount(IMAGE_SPAN_COUNT)
                .theme(R.style.picture_white_style_custom)
                .videoMaxSecond(20)//视频选最最大20秒
                .videoMinSecond(3)//最小三秒
                .recordVideoSecond(20)
                .videoQuality(0)
                .queryMaxFileSize(60)
                .isPreviewVideo(true)
                .maxSelectNum(maxSelectNum)
                .isZoomAnim(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .minimumCompressSize(200)
                .forResult(callbackListener);
    }

    public static void selectImageAndCrop(Activity activity, boolean showCamera, int aspectRatioX, int aspectRatioY, OnResultCallbackListener<LocalMedia> callbackListener) {
        PictureSelectionModel pictureSelectionModel = PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .setLanguage(-1)
                .theme(R.style.picture_white_style_custom)
                .isCamera(showCamera)
                .setPictureWindowAnimationStyle(getPictureWindowAnimationStyle())// 自定义相册启动退出动画
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(IMAGE_SPAN_COUNT)
                .compressFocusAlpha(true)
                .isEnableCrop(true)// 是否裁剪
                .withAspectRatio(aspectRatioX, aspectRatioY)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .minimumCompressSize(200)
                .compressQuality(85)// 图片压缩后输出质量 0~ 100
                .maxSelectNum(1)
                .queryMaxFileSize(6)
                .isPreviewImage(false)
                .isSingleDirectReturn(true)
                .isGif(false)
                .freeStyleCropEnabled(false)
                .isAutomaticTitleRecyclerTop(true)
                .selectionMode(PictureConfig.SINGLE);
        pictureSelectionModel.forResult(callbackListener);
    }

    public static void previewImage(Activity activity, String img) {
        List<String> imgs = new ArrayList<>();
        imgs.add(img);
        previewImage(activity, imgs, 0);
    }

    public static void previewImage(Activity activity, List<String> imgs, int index) {
        List<LocalMedia> list = new ArrayList<>();
        for (String img : imgs) {
            LocalMedia avatarMedia = new LocalMedia();
            avatarMedia.setPath(StringUtil.getFullImageUrl(img));
            list.add(avatarMedia);
        }
        previewLocalImages(activity, list, index);
    }

    public static void previewImage(Context context, List<String> imgs, int index) {
        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < imgs.size(); i++) {
            result.add(imgs.get(i));
        }
        new XPopup.Builder(context).asImageViewer(null, index, result, false, false, -1, -1, -1, false, null, new ImageLoader())
                .show();
    }

    public static void previewLocalImages(Activity activity, List<LocalMedia> localMedias, int index) {
        PictureSelector.create(activity)
                .themeStyle(R.style.picture_white_style_custom)
                .isNotPreviewDownload(true)
                .setLanguage(-1)
                .setLanguage(Utils.isManilaApp(activity) ? LanguageConfig.ENGLISH : LanguageConfig.TRADITIONAL_CHINESE)
                .imageEngine(GlideEngine.createGlideEngine())
                .openExternalPreview(index, localMedias);
    }

    private static PictureWindowAnimationStyle getPictureWindowAnimationStyle() {
        PictureWindowAnimationStyle mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        return mWindowAnimationStyle;
    }

}
