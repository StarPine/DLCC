package com.dl.playfun.ui.message.mediagallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.ActivitySnapshotVideoSettingBinding;
import com.dl.playfun.entity.MediaPayPerConfigEntity;
import com.dl.playfun.manager.ConfigManager;
import com.dl.playfun.ui.base.BaseActivity;
import com.dl.playfun.utils.AutoSizeUtils;
import com.dl.playfun.utils.ImmersionBarUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;

import java.math.BigDecimal;

import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/19 11:04
 * Description: This is MediaGalleryVideoSettingActivity
 */
public class MediaGalleryVideoSettingActivity extends BaseActivity<ActivitySnapshotVideoSettingBinding,MediaGalleryVideoSettingViewModel> {
    //本地价格模板
    private final String localPriceConfigSettingKey = "MediaGalleryVideoSettingKey";
    //本地缓存上次选择快照模板id
    private final String localSnapshotConfigIdSettingKey = "MediaGalleryVideoConfigIdSettingKey";

    private MediaPayPerConfigEntity.ItemEntity localCheckItemEntity = null;

    private final String TAG = MediaGalleryVideoSettingActivity.class.getCanonicalName();

    private String srcLocalPath = null;
    private boolean isPayState = false;
    private MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig;

    private SnapshotPhotoDialog snapshotPhotoDialog;

    private MediaPayPerConfigEntity.ItemEntity checkItemEntity;
    private Integer configId;


    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoSizeUtils.closeAdapt(getResources());
        GSYVideoManager.releaseAllVideos();
    }

    /**
     * @Desc TODO()
     * @author 彭石林
     * @parame [isPayState 是否付费, isVideoSetting 是否 视频, srcPath 文件信息]
     * @return android.content.Intent
     * @Date 2022/9/14
     */
    public static Intent createIntent(Context mContext, boolean isPayState, String srcPath, MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig){
        Intent snapshotIntent = new Intent(mContext,MediaGalleryVideoSettingActivity.class);
        snapshotIntent.putExtra("isPayState",isPayState);
        snapshotIntent.putExtra("srcPath",srcPath);
        snapshotIntent.putExtra("mediaPriceTmpConfig",mediaPriceTmpConfig);
        return snapshotIntent;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.activity_snapshot_video_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();
        Intent intent = getIntent();
        if(intent != null){
            srcLocalPath = intent.getStringExtra("srcPath");
            isPayState = intent.getBooleanExtra("isPayState",false);
            mediaPriceTmpConfig = (MediaPayPerConfigEntity.itemTagEntity) intent.getSerializableExtra("mediaPriceTmpConfig");
        }
    }

    @Override
    public MediaGalleryVideoSettingViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MediaGalleryVideoSettingViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.srcPath.set(srcLocalPath);
        viewModel.isPayState.set(isPayState);
        String localPriceValue = ConfigManager.getInstance().getAppRepository().readKeyValue(localPriceConfigSettingKey);
        String localConfigId = ConfigManager.getInstance().getAppRepository().readKeyValue(localSnapshotConfigIdSettingKey);

        configId = StringUtils.isEmpty(localConfigId) ? null : Integer.parseInt(localConfigId);
        if(!StringUtils.isEmpty(localPriceValue)){
            localCheckItemEntity = GsonUtils.fromJson(localPriceValue, MediaPayPerConfigEntity.ItemEntity.class);
        }
        if(ObjectUtils.isNotEmpty(localCheckItemEntity)){
            checkItemEntity = localCheckItemEntity;
        }else{
            checkItemEntity = mediaPriceTmpConfig.getContent().get(0);
            configId = mediaPriceTmpConfig.getConfigId();
        }

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.settingEvent.observe(this, unused -> {
            if(snapshotPhotoDialog==null){
                snapshotPhotoDialog = new SnapshotPhotoDialog(this,mediaPriceTmpConfig,localCheckItemEntity);
                snapshotPhotoDialog.setSnapshot(false);
                snapshotPhotoDialog.setSnapshotListener((itemEntity, configIds,isSnapshot) -> {
                    checkItemEntity = itemEntity;
                    this.configId = configIds;
                    localCheckItemEntity = checkItemEntity;
                    ConfigManager.getInstance().getAppRepository().putKeyValue(localPriceConfigSettingKey,GsonUtils.toJson(checkItemEntity));
                    ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotConfigIdSettingKey,String.valueOf(configIds));
                });
            }else{
                snapshotPhotoDialog.setLocalCheckItemEntity(localCheckItemEntity);
                snapshotPhotoDialog.init();
            }
            snapshotPhotoDialog.show();
        });
        //返回页面数据
        viewModel.setResultDataEvent.observe(this,filePath -> {
            MediaGalleryEditEntity mediaGalleryEditEntity = new MediaGalleryEditEntity();
            mediaGalleryEditEntity.setVideoSetting(true);
            mediaGalleryEditEntity.setStatePay(isPayState);
            mediaGalleryEditEntity.setAndroidLocalSrcPath(srcLocalPath);
            if(checkItemEntity!=null){
                mediaGalleryEditEntity.setUnlockPrice(new BigDecimal(checkItemEntity.getCoin()));
                mediaGalleryEditEntity.setMsgRenvenue(checkItemEntity.getProfit());
                mediaGalleryEditEntity.setConfigId(configId);
                mediaGalleryEditEntity.setConfigIndex(checkItemEntity.getConfigIndexString());
            }
            mediaGalleryEditEntity.setSrcPath(filePath);
            Intent intent = new Intent();
            intent.putExtra("mediaGalleryEditEntity", mediaGalleryEditEntity);
            setResult(2001,intent);
            finish();
        });
    }

    private void setVideoUri(StandardGSYVideoPlayer videoView, String url){
        //videoView.setUrl(url);
        // url = StringUtil.getFullAudioUrl(url);
        //videoView.loadCoverImage(url, R.drawable.default_placeholder_img);
        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();

        //默认缓存路径
        //使用lazy的set可以避免滑动卡的情况存在
        videoView.setUpLazy(url, true, null, null, "VideoPlay");

        //增加title
        videoView.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        videoView.getBackButton().setVisibility(View.GONE);
        videoView.getFullscreenButton().setVisibility(View.GONE);
        //设置全屏按键功能
        videoView.setRotateViewAuto(false);
        videoView.setLockLand(false);
        videoView.setPlayTag("SampleCoverVideoPlayer");
        //gsyVideoPlayer.c(true);
        videoView.setReleaseWhenLossAudio(true);
        videoView.setAutoFullWithSize(true);
        videoView.setShowFullAnimation(true);
        videoView.setIsTouchWiget(true);
        //循环
        //gsyVideoPlayer.setLooping(true);
        videoView.setNeedLockFull(true);
        //gsyVideoPlayer.setSpeed(2);

        videoView.setPlayPosition(0);

        videoView.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                if (!videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    //是否静音
                    GSYVideoManager.instance().setNeedMute(false);
                }
                if (videoView.getCurrentPlayer().isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setLastListener(videoView);
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                videoView.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });
    }


}
