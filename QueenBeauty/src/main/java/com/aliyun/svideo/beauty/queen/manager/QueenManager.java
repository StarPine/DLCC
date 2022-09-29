package com.aliyun.svideo.beauty.queen.manager;

import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.aliyun.svideo.base.BaseChooser;
import com.aliyun.svideo.base.beauty.api.BeautyInterface;
import com.aliyun.svideo.base.beauty.api.IAliyunBeautyInitCallback;
import com.aliyun.svideo.base.beauty.api.OnBeautyLayoutChangeListener;
import com.aliyun.svideo.base.beauty.api.OnDefaultBeautyLevelChangeListener;
import com.aliyun.svideo.base.beauty.api.constant.BeautyConstant;
import com.aliyun.svideo.base.beauty.api.constant.BeautySDKType;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;
import com.aliyun.svideo.beauty.queen.inteface.OnBeautyChooserCallback;
import com.aliyun.svideo.beauty.queen.util.QueenParamHolder;
import com.aliyun.svideo.beauty.queen.util.QueenParamsUtils;
import com.aliyun.svideo.beauty.queen.util.SharedPreferenceUtils;
import com.aliyun.svideo.beauty.queen.util.SimpleBytesBufPool;
import com.aliyun.svideo.beauty.queen.view.QueenBeautyChooser;
import com.aliyun.svideo.beauty.queen.view.face.BeautyFaceDetailSettingChooser;
import com.aliyun.svideo.beauty.queen.view.skin.BeautyShapeDetailChooser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taobao.android.libqueen.ImageFormat;
import com.taobao.android.libqueen.QueenEngine;
import com.taobao.android.libqueen.Texture2D;
import com.taobao.android.libqueen.exception.InitializationException;
import com.taobao.android.libqueen.models.Flip;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
@Keep
public class QueenManager implements BeautyInterface, OnBeautyChooserCallback {
    private static final String TAG = "QueenManager";
    private final Queue<Integer> mQueenParamUpdateQueue = new LinkedList<Integer>();
    private boolean isInit;//是否初始化
    private QueenEngine mQueenEngine;
    private SimpleBytesBufPool mBytesBufPool;
    private int mDeviceOrientation;
    private int mDisplayOrientation;
    private WeakReference<Context> mContextWeakReference;
    private QueenBeautyChooser mQueenBeautyChooser;
    private BeautyFaceDetailSettingChooser beautyFaceDetailSettingChooser;
    private BeautyShapeDetailChooser mBeautyShapeDetailChooser;
    private OnBeautyLayoutChangeListener mOnBeautyLayoutChangeListener;
    private FragmentManager mFragmentManager;
    //用户美颜微调后的参数
    private Map<Integer, QueenBeautyFaceParams> mCustomFaceParams = null;
    //用户美型微调后的参数
    private Map<Integer, QueenBeautyShapeParams> mCustomShapeParams = null;
    /**
     * 美肌美颜微调dialog是否正在显示
     */
    private boolean isBeautyDetailShowing = false;
    private int mBeautyFaceLevel;
    private int mBeautySkinType;
    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    /**
     * 相机的原始NV21数据
     */
    private byte[] frameBytes;

    private Texture2D mTexture2D;

    public QueenManager() {
    }


    @Override
    public void init(final Context context, final IAliyunBeautyInitCallback iAliyunBeautyInitCallback) {
        mContextWeakReference = new WeakReference<>(context);
        if (iAliyunBeautyInitCallback != null) {
            iAliyunBeautyInitCallback.onInit(BeautyConstant.BEAUTY_INIT_SUCCEED);
        }
    }


    /**
     * SDK初始化,
     */
    public Texture2D initEngine(boolean toScreen, int textureId, int textureWidth, int textureHeight, boolean isEOS) {
        Texture2D texture2D = null;
        try {
           Context context  = getContext();
            if (context == null){
                Log.e(TAG, "initEngine: context is null" );
                return null;
            }
            if (mQueenEngine == null) {
                mQueenEngine = new QueenEngine(context, toScreen);
                mQueenEngine.setPowerSaving(true);
                mQueenEngine.setInputTexture(textureId, textureHeight, textureWidth, isEOS);
                texture2D = updateOutTexture(textureHeight, textureWidth);
                mQueenEngine.setScreenViewport(0, 0, textureHeight, textureWidth);
                writeParamToEngine();
            }
        } catch (InitializationException e) {
            e.printStackTrace();
        }

        return texture2D;
    }

    @Nullable
    private Context getContext() {
        return mContextWeakReference != null?mContextWeakReference.get():null;
    }

    @Override
    public void initParams() {
        Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        mBeautyFaceLevel = SharedPreferenceUtils.getQueenFaceBeautyLevel(context);
        mBeautySkinType = SharedPreferenceUtils.getQueenSkinType(context);
        mCustomFaceParams = QueenParamsUtils.getFaceParams(context);
        mCustomShapeParams = QueenParamsUtils.getShapeParams(context);
        changeFaceBeautyLevel(mBeautyFaceLevel);
        changeSkinLevel(mBeautySkinType);
    }

    private void changeSkinLevel(int beautySkinValue) {
        if (mCustomShapeParams == null) {
            return;
        }
        QueenBeautyShapeParams params = mCustomShapeParams.get(beautySkinValue);
        changeSkinParams(params);
    }

    private void changeFaceBeautyLevel(int beautyFaceLevel) {
        if (mCustomFaceParams == null) {
            return;
        }
        QueenBeautyFaceParams params = mCustomFaceParams.get(beautyFaceLevel);
        changeFaceParams(params);
    }

    /**
     * 美肌参数设置
     */
    private void changeSkinParams(QueenBeautyShapeParams shapeParam) {
        if (shapeParam != null) {
            QueenParamHolder.getQueenParam().faceShapeRecord.enableFaceShape = true;
            //削脸
            QueenParamHolder.getQueenParam().faceShapeRecord.cutFaceParam = shapeParam.beautyCutFace / 100 * 3;
            //瘦脸
            QueenParamHolder.getQueenParam().faceShapeRecord.thinFaceParam = shapeParam.beautyThinFace / 100 * 1.5f * 3;
            //脸长
            QueenParamHolder.getQueenParam().faceShapeRecord.longFaceParam = shapeParam.beautyLongFace / 100 * 3;
            //下巴缩短
            QueenParamHolder.getQueenParam().faceShapeRecord.lowerJawParam = shapeParam.beautyLowerJaw / 100 * -1f * 3;
            //大眼
            QueenParamHolder.getQueenParam().faceShapeRecord.bigEyeParam = shapeParam.beautyBigEye / 100 * 3;
            //瘦鼻
            QueenParamHolder.getQueenParam().faceShapeRecord.thinNoseParam = shapeParam.beautyThinNose / 100 * 3;
            //唇宽
            QueenParamHolder.getQueenParam().faceShapeRecord.mouthWidthParam = shapeParam.beautyMouthWidth / 100 * -1f * 3;
            //下颌
            QueenParamHolder.getQueenParam().faceShapeRecord.thinMandibleParam = shapeParam.beautyThinMandible / 100 * 3;
            //颧骨
            QueenParamHolder.getQueenParam().faceShapeRecord.cutCheekParam = shapeParam.beautyCutCheek / 100 * 3;
            addParamsTask();
        }
    }

    /**
     * 美颜参数设置
     */
    private void changeFaceParams(QueenBeautyFaceParams params) {
        if (params == null) {
            return;
        }
        QueenParamHolder.getQueenParam().basicBeautyRecord.enableSkinWhiting = true;
        QueenParamHolder.getQueenParam().basicBeautyRecord.skinWhitingParam = params.beautyWhite / 100;
        QueenParamHolder.getQueenParam().basicBeautyRecord.enableSkinBuffing = true;
        QueenParamHolder.getQueenParam().basicBeautyRecord.skinSharpenParam = params.beautySharpen / 100;
        QueenParamHolder.getQueenParam().basicBeautyRecord.skinBuffingParam = params.beautyBuffing / 100;
        addParamsTask();
    }


    @Override
    public void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) {
        frameBytes = bytes;
        mCameraInfo = info;
        updateBytesBufPool(width, height, bytes);
    }


    @Override
    public int onTextureIdBack(int textureId, int textureWidth, int textureHeight, float[] matrix,int currentCameraType) {
        if(mTexture2D == null){
            mTexture2D = initEngine(false, textureId, textureWidth, textureHeight, true);
        }

        byte[] lastBuffer = null;
        if (mBytesBufPool != null) {
            updateBytesBufPool(frameBytes);
            lastBuffer = mBytesBufPool.getLastBuffer();
        }
        if (lastBuffer != null) {
            mQueenEngine.updateInputDataAndRunAlg(lastBuffer, ImageFormat.NV21,textureWidth,textureHeight,0,getInputAngle(mCameraInfo),getOutputAngle(mCameraInfo),
                   getFlipAxis(mCameraInfo));
            mBytesBufPool.releaseBuffer(lastBuffer);
        }
        updateParamToEngine();
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        int retCode = mQueenEngine.renderTexture(matrix);
        Log.d(TAG, "onTextureIdBack: retCode:" + retCode);
        // QUEEN_INVALID_LICENSE(-9)，表示证书校验失败
       // QUEEN_NO_EFFECT(-10)，表示全部特效功能关闭
        if (retCode != -9 && retCode != -10) {
            if (mTexture2D != null) {
                textureId = mTexture2D.getTextureId();
            }
        }

        return textureId;
    }

    private Texture2D updateOutTexture(int textureWidth, int textureHeight) {
        Texture2D texture2D = null;
        // 非必要步骤：获得美颜输出纹理，可以在用于其他扩展业务
        if (mQueenEngine != null) {
            texture2D = mQueenEngine.autoGenOutTexture(true);
            if (texture2D != null) {
                mQueenEngine.updateOutTexture(texture2D.getTextureId(), textureWidth, textureHeight, true);
            }
        }
        return texture2D;
    }

    private void updateBytesBufPool(int width, int height, byte[] bytes) {
        if (mBytesBufPool == null) {
            int byteSize = width * height * android.graphics.ImageFormat.getBitsPerPixel(android.graphics.ImageFormat.NV21) / 8;
            mBytesBufPool = new SimpleBytesBufPool(3, byteSize);
        }
        updateBytesBufPool(bytes);
    }

    private void updateBytesBufPool(byte[] bytes) {
        mBytesBufPool.updateBuffer(bytes);
        mBytesBufPool.reusedBuffer();
    }

    private void writeParamToEngine() {
        QueenParamHolder.writeParamToEngine(mQueenEngine);
    }

    @Override
    public void setDeviceOrientation(int deviceOrientation, int displayOrientation) {
        if (deviceOrientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }
        Camera.getCameraInfo(mCameraInfo.facing, mCameraInfo);
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        this.mDeviceOrientation = deviceOrientation;
        this.mDisplayOrientation = displayOrientation;
    }

    @Override
    public void showControllerView(FragmentManager fragmentManager, OnBeautyLayoutChangeListener onBeautyLayoutChangeListener) {
        final Context context = mContextWeakReference.get();
        mFragmentManager = fragmentManager;
        if (context == null || fragmentManager == null) {
            return;
        }
        mOnBeautyLayoutChangeListener = onBeautyLayoutChangeListener;
        if (mQueenBeautyChooser == null) {
            mQueenBeautyChooser = new QueenBeautyChooser();
        }
        mQueenBeautyChooser.setOnBeautyChooserCallback(this);
        mQueenBeautyChooser.setDismissListener(new BaseChooser.DialogVisibleListener() {
            @Override
            public void onDialogDismiss() {
                if (mOnBeautyLayoutChangeListener != null && !isBeautyDetailShowing) {
                    mOnBeautyLayoutChangeListener.onLayoutChange(View.GONE);
                    saveBeautyParams(context);
                }
            }

            @Override
            public void onDialogShow() {
                if (mOnBeautyLayoutChangeListener != null) {
                    mOnBeautyLayoutChangeListener.onLayoutChange(View.VISIBLE);
                }
            }
        });
        mQueenBeautyChooser.show(fragmentManager, "beauty", mBeautyFaceLevel, mBeautySkinType);
    }

    @Override
    public void addDefaultBeautyLevelChangeListener(OnDefaultBeautyLevelChangeListener onDefaultBeautyLevelChangeListener) {

    }


    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public BeautySDKType getSdkType() {
        return BeautySDKType.QUEEN;
    }

    public void addParamsTask() {
        mQueenParamUpdateQueue.offer(1);
    }

    public void updateParamToEngine() {
        Integer poll = mQueenParamUpdateQueue.poll();
        if (poll != null) {
            writeParamToEngine();
        }
    }

    private int getOutputAngle(Camera.CameraInfo cameraInfo) {
        boolean isFont = cameraInfo.facing != Camera.CameraInfo.CAMERA_FACING_BACK;
        int angle = isFont ? (360 - mDeviceOrientation) % 360 : mDeviceOrientation % 360;
        return (angle - mDisplayOrientation + 360) % 360;
    }


    private int getInputAngle(Camera.CameraInfo cameraInfo) {
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (360 + cameraInfo.orientation - mDeviceOrientation) % 360;
        } else {
            return (cameraInfo.orientation + mDeviceOrientation) % 360;
        }
    }

    private int getFlipAxis(Camera.CameraInfo cameraInfo) {
        int mFlipAxis;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mFlipAxis = Flip.kNone;
        } else {
            mFlipAxis = Flip.kFlipY;
        }
        return mFlipAxis;
    }

    @Override
    public void setDebug(boolean isDebug) {
        if (mQueenEngine != null) {
            mQueenEngine.enableDebugLog();
        }
    }

    @Override
    public void onShowFaceDetailView(int level) {
        if (mCustomFaceParams == null) {
            return;
        }
        isBeautyDetailShowing = true;
        if (mQueenBeautyChooser != null && mQueenBeautyChooser.isVisible()) {
            mQueenBeautyChooser.dismiss();
        }
        if (beautyFaceDetailSettingChooser == null) {
            beautyFaceDetailSettingChooser = new BeautyFaceDetailSettingChooser();
        }
        beautyFaceDetailSettingChooser.setOnBeautyChooserCallback(this);
        beautyFaceDetailSettingChooser.setBeautyParams(mCustomFaceParams.get(level));
        beautyFaceDetailSettingChooser.show(mFragmentManager, "beautyFaceDetailSettingChooser");
    }

    @Override
    public void onShowSkinDetailView(int type) {
        isBeautyDetailShowing = true;
        if (mQueenBeautyChooser != null && mQueenBeautyChooser.isVisible()) {
            mQueenBeautyChooser.dismiss();
        }
        if (mBeautyShapeDetailChooser == null) {
            mBeautyShapeDetailChooser = new BeautyShapeDetailChooser();
        }
        mBeautyShapeDetailChooser.setOnBeautyChooserCallback(this);
        mBeautyShapeDetailChooser.setBeautyShapeParams(mCustomShapeParams.get(type));
        mBeautyShapeDetailChooser.show(mFragmentManager, "beautyFaceDetailSettingChooser");
    }

    @Override
    public void onChooserBlankClick() {
        //点击空白部分，隐藏美颜编辑界面
        isBeautyDetailShowing = false;
        if (mBeautyShapeDetailChooser != null && mBeautyShapeDetailChooser.isVisible()) {
            mBeautyShapeDetailChooser.dismiss();
        }
        if (beautyFaceDetailSettingChooser != null && beautyFaceDetailSettingChooser.isVisible()) {
            beautyFaceDetailSettingChooser.dismiss();
        }
        if (mQueenBeautyChooser != null && mQueenBeautyChooser.isVisible()) {
            mQueenBeautyChooser.dismiss();
        } else {
            if (mOnBeautyLayoutChangeListener != null) {
                mOnBeautyLayoutChangeListener.onLayoutChange(View.GONE);
            }
        }
    }

    @Override
    public void onChooserBackClick() {
        if (mBeautyShapeDetailChooser != null && mBeautyShapeDetailChooser.isVisible()) {
            mBeautyShapeDetailChooser.dismiss();
        }
        if (beautyFaceDetailSettingChooser != null && beautyFaceDetailSettingChooser.isVisible()) {
            beautyFaceDetailSettingChooser.dismiss();
        }
        if (mQueenBeautyChooser != null && !mQueenBeautyChooser.isVisible()) {
            mQueenBeautyChooser.show(mFragmentManager, "faceUnity", mBeautyFaceLevel, mBeautySkinType);
        }
    }

    @Override
    public void onChooserKeyBackClick() {
        isBeautyDetailShowing = false;
        if (mBeautyShapeDetailChooser != null && mBeautyShapeDetailChooser.isVisible()) {
            mBeautyShapeDetailChooser.dismiss();
        }
        if (mBeautyShapeDetailChooser != null && mBeautyShapeDetailChooser.isVisible()) {
            mBeautyShapeDetailChooser.dismiss();
        }
        if (mQueenBeautyChooser != null && mQueenBeautyChooser.isVisible()) {
            mQueenBeautyChooser.dismiss();
        }
        if (mOnBeautyLayoutChangeListener != null) {
            mOnBeautyLayoutChangeListener.onLayoutChange(View.GONE);
        }
    }

    @Override
    public void onFaceLevelChanged(int level) {
        mBeautyFaceLevel = level;
        changeFaceBeautyLevel(level);
    }

    @Override
    public void onBeautyFaceChange(QueenBeautyFaceParams param) {
        if (param != null) {
            changeFaceParams(param);
            if (mCustomFaceParams != null) {
                mCustomFaceParams.put(param.beautyLevel, param);
            }
        }
    }

    @Override
    public void onBeautyShapeChange(QueenBeautyShapeParams param) {
        if (param != null) {
            changeSkinParams(param);
            if (mCustomShapeParams != null) {
                mCustomShapeParams.put(param.shapeType, param);
            }
        }
    }

    @Override
    public void onShapeTypeChange(int type) {
        mBeautySkinType = type;
        changeSkinLevel(type);
    }

    private void saveBeautyParams(Context context) {
        String faceParams = "";
        String shapeParams = "";
        if (context != null) {
            try {
                Gson gson = new GsonBuilder().create();
                Type gsonType = new TypeToken<HashMap<Integer, QueenBeautyFaceParams>>() {
                }.getType();
                faceParams = gson.toJson(mCustomFaceParams, gsonType);
            } catch (Exception e) {
                Log.e(TAG, "saveBeautyParams: ", e);
            }

            try {
                Gson gson = new GsonBuilder().create();
                Type gsonType = new TypeToken<HashMap<Integer, QueenBeautyShapeParams>>() {
                }.getType();
                shapeParams = gson.toJson(mCustomShapeParams, gsonType);
            } catch (Exception e) {
                Log.e(TAG, "saveBeautyParams: ", e);
            }
            SharedPreferenceUtils.setQueenFaceBeautyCustomParams(context, faceParams);
            SharedPreferenceUtils.setQueenShapeBeautyCustomParams(context, shapeParams);
            SharedPreferenceUtils.setQueenFaceBeautyLevel(context, mBeautyFaceLevel);
            SharedPreferenceUtils.setQueenSkinType(context, mBeautySkinType);
        }
    }

    @Override
    public void release() {
        if (mQueenEngine != null) {
            mQueenEngine.release();
            mQueenEngine = null;
            mTexture2D = null;
        }
    }
}
