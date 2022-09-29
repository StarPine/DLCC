package com.dl.playfun.widget.picgridview;

import android.content.Context;

import java.util.List;

/**
 * @author wulei
 */
public class ClickImageGridViewAdapter extends ImageGridViewAdapter {

    public ClickImageGridViewAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
    }

    @Override
    protected void onImageItemClick(Context context, ImageGridView imageGridView, int index, List<ImageInfo> imageInfo) {
        super.onImageItemClick(context, imageGridView, index, imageInfo);

    }
}
