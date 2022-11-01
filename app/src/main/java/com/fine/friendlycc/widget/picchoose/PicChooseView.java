package com.fine.friendlycc.widget.picchoose;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class PicChooseView extends RecyclerView implements PicChooseAdapter.PicChooseAdapterListener {

    private final PicChooseAdapter adapter;
    private final List<PicChooseItemEntity> datas;
    private final List<PicChooseItemEntity> chooseMedias;

    private boolean showCamera;
    private int maxSelectNum = 9;
    private int gridCount = 3;

    private OnMediaOperateListener onMediaOperateListener;

    public PicChooseView(Context context) {
        this(context, null);
    }

    public PicChooseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), gridCount);
        this.setLayoutManager(layoutManage);

        adapter = new PicChooseAdapter(this);
        adapter.setPicChooseAdapterListener(this);

        this.setAdapter(adapter);

        datas = new ArrayList<>();
        chooseMedias = new ArrayList<>();
        PicChooseItemEntity itemEntity = new PicChooseItemEntity(PicChooseItemEntity.TYPE_ADD, "");
        datas.add(itemEntity);
        adapter.setData(datas);
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public int getGridCount() {
        return gridCount;
    }

    public void setGridCount(int gridCount) {
        this.gridCount = gridCount;
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), gridCount);
        this.setLayoutManager(layoutManage);
    }

    public List<PicChooseItemEntity> getChooseMedias() {
        return chooseMedias;
    }

    public OnMediaOperateListener getOnMediaOperateListener() {
        return onMediaOperateListener;
    }

    public void setOnMediaOperateListener(OnMediaOperateListener onMediaOperateListener) {
        this.onMediaOperateListener = onMediaOperateListener;
    }

    private void addPic() {
        int curMaxSelectNum = maxSelectNum;
        if (datas.get(datas.size() - 1).getType() == PicChooseItemEntity.TYPE_ADD){
            curMaxSelectNum = maxSelectNum - datas.size() + 1;
        }
        PictureSelectorUtil.selectImage((Activity) getContext(), showCamera, curMaxSelectNum, 50, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                if (onMediaOperateListener != null) {
                    for (LocalMedia localMedia : result) {

                        PicChooseItemEntity picChooseItemEntity = new PicChooseItemEntity(PicChooseItemEntity.TYPE_IMG, localMedia.getCompressPath());
                        if (PictureMimeType.MIME_TYPE_IMAGE.equals(localMedia.getMimeType()) || PictureMimeType.PNG_Q.equals(localMedia.getMimeType())) {
                            picChooseItemEntity.setMediaType(PicChooseItemEntity.MEDIA_TYPE_IMG);
                        } else if (PictureMimeType.MIME_TYPE_VIDEO.equals(localMedia.getMimeType())) {
                            picChooseItemEntity.setMediaType(PicChooseItemEntity.MEDIA_TYPE_VIDEO);
                        }
                        datas.add(datas.size() - 1, picChooseItemEntity);
                        chooseMedias.add(picChooseItemEntity);
                    }
                    if (datas.size() >= maxSelectNum + 1) {
                        datas.remove(datas.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                    onMediaOperateListener.onMediaChoosed(chooseMedias);
                }
            }

            @Override
            public void onCancel() {
                if (onMediaOperateListener != null) {
                    onMediaOperateListener.onMediaChooseCancel();
                }
            }
        });
    }

    @Override
    public void onItemDelClick(View view, int position) {
        PicChooseItemEntity delPic = datas.get(position);
        datas.remove(position);
        chooseMedias.remove(position);
        if (datas.size() == 0) {
            datas.add(new PicChooseItemEntity(PicChooseItemEntity.TYPE_ADD, ""));
        } else if (datas.get(datas.size() - 1).getType() != PicChooseItemEntity.TYPE_ADD) {
            datas.add(new PicChooseItemEntity(PicChooseItemEntity.TYPE_ADD, ""));
        }
        adapter.notifyDataSetChanged();
        if (onMediaOperateListener != null) {
            onMediaOperateListener.onMediaDelete(chooseMedias, delPic);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemAddClick() {
        addPic();
    }

    public interface OnMediaOperateListener {

        void onMediaChooseCancel();

        void onMediaChoosed(List<PicChooseItemEntity> medias);

        void onMediaDelete(List<PicChooseItemEntity> medias, PicChooseItemEntity delMedia);

//        void onMediaUploadSuccess(int index, String filePath);
//
//        void onAllMediaUploadSuccess(List<String> filePaths);
//
//        void onMediaUploadFaile(int index);
    }

}
