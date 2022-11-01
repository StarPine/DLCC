package com.fine.friendlycc.ui.message.mediagallery;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.DialogSnapshotPhotoSettingBinding;
import com.fine.friendlycc.entity.MediaPayPerConfigEntity;
import com.fine.friendlycc.ui.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/9/9 17:55
 * Description: 发送付费照片、视频调节价格
 */
public class SnapshotPhotoDialog extends BaseDialog {

    private final DialogSnapshotPhotoSettingBinding binding;

    private final MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig;

    private SnapshotListener snapshotListener;

    private MediaPayPerConfigEntity.ItemEntity checkItemEntity;
    private Integer configId;

    private boolean isSnapshot = true;

    private MediaPayPerConfigEntity.ItemEntity localCheckItemEntity;

    public SnapshotListener getSnapshotListener() {
        return snapshotListener;
    }

    public void setSnapshotListener(SnapshotListener snapshotListener) {
        this.snapshotListener = snapshotListener;
    }

    public boolean isSnapshot() {
        return isSnapshot;
    }

    public void setSnapshot(boolean snapshot) {
        isSnapshot = snapshot;
        if(!isSnapshot()){
            binding.snapshotLayout.setVisibility(View.GONE);
        }else{
            binding.snapshotLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setSnapshotCheck(boolean check) {
        binding.snapshotCheckbox.setChecked(check);
    }

    public void setLocalCheckItemEntity(MediaPayPerConfigEntity.ItemEntity localCheckItemEntity) {
        this.localCheckItemEntity = localCheckItemEntity;
    }

    public SnapshotPhotoDialog(Context context, MediaPayPerConfigEntity.itemTagEntity mediaPriceTmpConfig, MediaPayPerConfigEntity.ItemEntity localCheckItemEntity) {
        super(context);
        this.mediaPriceTmpConfig = mediaPriceTmpConfig;
        this.localCheckItemEntity = localCheckItemEntity;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_snapshot_photo_setting, null, false);
        setCancelable(true);
        init();
    }
    public void init() {
        ArrayList<String> listData = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(mediaPriceTmpConfig)){
            List<MediaPayPerConfigEntity.ItemEntity> itemData = mediaPriceTmpConfig.getContent();
            int checkedIdx = 0;
            for (int i = 0; i < itemData.size(); i++) {
                listData.add(itemData.get(i).getCoin());
                if(localCheckItemEntity!=null){
                    if(localCheckItemEntity.getCoin()!=null && localCheckItemEntity.getProfit()!=null){
                        if(localCheckItemEntity.getCoin().equals(itemData.get(i).getCoin()) && localCheckItemEntity.getProfit().equals(itemData.get(i).getProfit())){
                            checkItemEntity = itemData.get(i);
                            checkedIdx = i;
                        }
                    }
                }
            }
            if(ObjectUtils.isNotEmpty(listData)){
                int size = listData.size();
                binding.seekbarPhoto.initData(listData, size-1);
                binding.seekbarPhoto.setProgress(checkedIdx);
                binding.seekbarPhotoView.setMax(size-1);
                binding.seekbarPhotoView.setProgress(checkedIdx);
                if(localCheckItemEntity!=null){
                    checkItemEntity = localCheckItemEntity;
                }else{
                    checkItemEntity = mediaPriceTmpConfig.getContent().get(0);
                }
                binding.tvCoin.setText(checkItemEntity.getCoin());
                binding.tvMoney.setText("+"+String.valueOf(checkItemEntity.getProfit()));
                configId = mediaPriceTmpConfig.getConfigId();

            }
        }

        //文字调整进度条宽度测量
        binding.seekbarPhoto.setMeasureWidthCallBack(width -> {
            ViewGroup.LayoutParams layoutParams = binding.seekbarPhotoView.getLayoutParams();
            layoutParams.width = width;
            binding.seekbarPhotoView.setLayoutParams(layoutParams);
        });
        binding.seekbarPhotoView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.seekbarPhoto.setProgress(seekBar.getProgress());
                if(getSnapshotListener()!=null){
                    if(ObjectUtils.isNotEmpty(mediaPriceTmpConfig) && ObjectUtils.isNotEmpty(mediaPriceTmpConfig.getContent())){
                        MediaPayPerConfigEntity.ItemEntity itemEntity = mediaPriceTmpConfig.getContent().get(seekBar.getProgress());
                        checkItemEntity = itemEntity;
                        binding.tvCoin.setText(itemEntity.getCoin());
                        binding.tvMoney.setText("+"+String.valueOf(itemEntity.getProfit()));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //拖动条停止拖动的时候调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        binding.tvBtn.setOnClickListener(v ->{
            if(getSnapshotListener()!=null){
                getSnapshotListener().confirm(checkItemEntity,configId,binding.snapshotCheckbox.isChecked());
            }
            dismiss();
        });

        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void show() {
        super.show();
    }

    public interface SnapshotListener{
        void confirm(MediaPayPerConfigEntity.ItemEntity itemEntity, Integer configId,boolean isSnapshot);
    }
}
