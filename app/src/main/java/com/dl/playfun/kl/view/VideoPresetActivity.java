package com.dl.playfun.kl.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dl.playfun.R;
import com.dl.playfun.manager.LocaleManager;
import com.faceunity.nama.FURenderer;
import com.faceunity.nama.data.FaceUnityDataFactory;
import com.faceunity.nama.ui.FaceUnityView;
import com.tencent.liteav.trtccalling.model.TRTCCalling;
import com.tencent.liteav.trtccalling.ui.base.VideoLayoutFactory;
import com.tencent.liteav.trtccalling.ui.videocall.videolayout.TRTCVideoLayout;
import com.tencent.liteav.trtccalling.ui.videocall.videolayout.TRTCVideoLayoutManager;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/3/29 17:13
 * 修改备注：
 */
public class VideoPresetActivity extends AppCompatActivity {

    private FaceUnityView faceUnityView;
    protected TRTCCalling mTRTCCalling;
    private FaceUnityDataFactory mFaceUnityDataFactory;
    private FURenderer mFURenderer;
    private boolean isFuEffect = true;
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private String userId = "userInfo";
    private ImageView mCameraChange,mBack;
    private boolean isFrontCamera = true; //是否为前置

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig!=null){
            LocaleManager.setLocal(this);
        }
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocal(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LocaleManager.setLocal(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_perset);
        faceUnityView = findViewById(R.id.fu_view);
        mLayoutManagerTrtc = findViewById(R.id.trtc_layout_manager);
        mCameraChange = findViewById(R.id.iv_camera_change);
        mBack = findViewById(R.id.iv_back);

        initListener();

        //1.先打开渲染器
        mTRTCCalling = TRTCCalling.sharedInstance(this);
        mFURenderer = FURenderer.getInstance();
        mFaceUnityDataFactory = new FaceUnityDataFactory(0);
        faceUnityView.bindDataFactory(mFaceUnityDataFactory);
        mTRTCCalling.createCustomRenderer(this, true, isFuEffect);

        //2.再打开摄像头
        mLayoutManagerTrtc.initVideoFactory(new VideoLayoutFactory(this));
        TRTCVideoLayout videoLayout = mLayoutManagerTrtc.allocCloudVideoView(userId);
        mTRTCCalling.openCamera(true, videoLayout.getVideoView());
    }

    private void initListener() {
        mBack.setOnClickListener(v -> {
            stopCameraAndFinish();
        });
        mCameraChange.setOnClickListener(v -> {
            isFrontCamera = !isFrontCamera;
            mTRTCCalling.switchCamera(isFrontCamera);
        });
    }

    private void stopCameraAndFinish() {
        mTRTCCalling.closeCamera();
        finish();
    }

    @Override
    public void onBackPressed() {
        stopCameraAndFinish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFuEffect && mFURenderer != null) {
            mFaceUnityDataFactory.bindCurrentRenderer();
        }
    }

}
