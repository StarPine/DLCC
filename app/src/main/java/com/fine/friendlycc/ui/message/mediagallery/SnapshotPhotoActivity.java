package com.fine.friendlycc.ui.message.mediagallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.databinding.ActivitySnapshotPhotoSettingBinding;
import com.fine.friendlycc.entity.MediaPayPerConfigEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;
import com.fine.friendlycc.ui.base.BaseActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.fine.friendlycc.utils.ImmersionBarUtils;

import java.math.BigDecimal;

import me.goldze.mvvmhabit.utils.StringUtils;

/**
 * Author: 彭石林
 * Time: 2022/9/9 12:02
 * Description: This is SnapshotPhotoActivity
 */
public class SnapshotPhotoActivity extends BaseActivity<ActivitySnapshotPhotoSettingBinding,SnapshotPhotoViewModel> {
    //本地价格模板
    private final String localPriceConfigSettingKey = "MediaGalleryPhotoSettingKey";
    //本地缓存上次选择快照设置
    private final String localSnapshotCheckSettingKey = "MediaGalleryPhotoCheckSettingKey";
    //本地缓存上次选择快照设置
    private final String localSnapshotPayCheckSettingKey = "localSnapshotPayCheckSettingKey";
    //本地缓存上次选择快照模板id
    private final String localSnapshotConfigIdSettingKey = "MediaGalleryPhotoConfigIdSettingKey";
    private boolean localSnapshotCheck = false;
    private MediaPayPerConfigEntity.ItemEntity localCheckItemEntity = null;

    private String srcLocalPath = null;
    private boolean isPayState = false;

    private MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig;
    private SnapshotPhotoDialog snapshotPhotoDialog;

    private MediaPayPerConfigEntity.ItemEntity checkItemEntity;
    private Integer configId;

    private boolean isAdmin = false;

    /**
    * @Desc TODO()
    * @author 彭石林
    * @parame [isPayState 是否付费, isVideoSetting 是否 视频, srcPath 文件信息]
    * @return android.content.Intent
    * @Date 2022/9/14
    */
    public static Intent createIntent(Context mContext, boolean isPayState, String srcPath,boolean isAdmin,MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig){
        Intent snapshotIntent = new Intent(mContext,SnapshotPhotoActivity.class);
        snapshotIntent.putExtra("isPayState",isPayState);
        snapshotIntent.putExtra("srcPath",srcPath);
        snapshotIntent.putExtra("mediaPriceTmpConfig",mediaPriceTmpConfig);
        snapshotIntent.putExtra("isAdmin",isAdmin);
        return snapshotIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(this.getResources());
        return R.layout.activity_snapshot_photo_setting;
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
            isAdmin = intent.getBooleanExtra("isAdmin",false);
            mediaPriceTmpConfig = (MediaPayPerConfigEntity.itemTagEntity) intent.getSerializableExtra("mediaPriceTmpConfig");
        }
    }

    @Override
    public SnapshotPhotoViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SnapshotPhotoViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.srcPath.set(srcLocalPath);
        viewModel.isPayState.set(isPayState);
        viewModel.isAdmin.set(isAdmin);
        AppRepository appRepository = ConfigManager.getInstance().getAppRepository();
        if(isPayState){
            localSnapshotCheck =  Boolean.parseBoolean(appRepository.readKeyValue(localSnapshotPayCheckSettingKey));
        }else{
            localSnapshotCheck =  Boolean.parseBoolean(appRepository.readKeyValue(localSnapshotCheckSettingKey));
        }
        viewModel.isBurn.set(localSnapshotCheck);
        //是付费照片
        if(isPayState){
            String localPriceValue = appRepository.readKeyValue(localPriceConfigSettingKey);
            String localConfigId = appRepository.readKeyValue(localSnapshotConfigIdSettingKey);
            configId = StringUtils.isEmpty(localConfigId) ? null : Integer.parseInt(localConfigId);
            if(!StringUtils.isEmpty(localPriceValue)){
                localCheckItemEntity = GsonUtils.fromJson(localPriceValue, MediaPayPerConfigEntity.ItemEntity.class);
            }
            if(ObjectUtils.isNotEmpty(localCheckItemEntity)){
                checkItemEntity = localCheckItemEntity;
            }else{
                checkItemEntity = mediaPriceTmpConfig.getContent().get(0);
                this.configId = mediaPriceTmpConfig.getConfigId();
            }
        }
        //选择的是图片
        GlideEngine.createGlideEngine().loadImage(this, srcLocalPath,binding.imgContent,binding.imgLong,R.drawable.playfun_loading_logo_placeholder_max,R.drawable.playfun_loading_logo_error);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.settingEvent.observe(this, unused -> {
            if(snapshotPhotoDialog==null){
                snapshotPhotoDialog = new SnapshotPhotoDialog(this,mediaPriceTmpConfig,localCheckItemEntity);
                snapshotPhotoDialog.setSnapshotCheck(localSnapshotCheck);
                snapshotPhotoDialog.setSnapshotListener((itemEntity, configIds,isSnapshot) -> {
                    checkItemEntity = itemEntity;
                    this.configId = configIds;
                    viewModel.isBurn.set(isSnapshot);
                    localCheckItemEntity = itemEntity;
                    localSnapshotCheck = isSnapshot;
                    ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotConfigIdSettingKey,String.valueOf(configIds));
                    ConfigManager.getInstance().getAppRepository().putKeyValue(localPriceConfigSettingKey,GsonUtils.toJson(checkItemEntity));
                    if(isPayState){
                        ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotPayCheckSettingKey, String.valueOf(isSnapshot));
                    }else{
                        ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotCheckSettingKey, String.valueOf(isSnapshot));
                    }
                });
            }else{
                snapshotPhotoDialog.setSnapshotCheck(localSnapshotCheck);
                snapshotPhotoDialog.setLocalCheckItemEntity(localCheckItemEntity);
                snapshotPhotoDialog.init();
            }
            snapshotPhotoDialog.show();
        });
        //返回页面数据
        viewModel.setResultDataEvent.observe(this,filePath -> {
            MediaGalleryEditEntity mediaGalleryEditEntity = new MediaGalleryEditEntity();
            mediaGalleryEditEntity.setVideoSetting(false);
            mediaGalleryEditEntity.setStatePay(isPayState);
            mediaGalleryEditEntity.setAndroidLocalSrcPath(srcLocalPath);
            if(checkItemEntity!=null && isPayState){
                mediaGalleryEditEntity.setUnlockPrice(new BigDecimal(checkItemEntity.getCoin()));
                mediaGalleryEditEntity.setMsgRenvenue(checkItemEntity.getProfit());
                mediaGalleryEditEntity.setConfigId(this.configId);
                mediaGalleryEditEntity.setConfigIndex(checkItemEntity.getConfigIndexString());
            }
            mediaGalleryEditEntity.setSrcPath(filePath);
            mediaGalleryEditEntity.setStateSnapshot(isAdmin ? false :viewModel.isBurn.get());
            if(isPayState){
                ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotPayCheckSettingKey, String.valueOf(mediaGalleryEditEntity.isStateSnapshot()));
            }else{
                ConfigManager.getInstance().getAppRepository().putKeyValue(localSnapshotCheckSettingKey, String.valueOf(mediaGalleryEditEntity.isStateSnapshot()));
            }
            Intent intent = new Intent();
            intent.putExtra("mediaGalleryEditEntity", mediaGalleryEditEntity);
            setResult(2001,intent);
            finish();
        });
    }

    @Override
    public void onDestroy() {
        if(snapshotPhotoDialog!=null){
            snapshotPhotoDialog.dismiss();
            snapshotPhotoDialog = null;
        }
        super.onDestroy();
    }

}
