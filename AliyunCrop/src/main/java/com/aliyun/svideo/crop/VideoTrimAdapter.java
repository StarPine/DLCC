/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.aliyun.svideo.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aliyun.svideo.base.widget.SquareFrameLayout;
import com.aliyun.svideo.common.utils.DensityUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


public class VideoTrimAdapter extends BaseAdapter {
    private final Context mContext;
    private final int screenWidth;
    private ArrayList<SoftReference<Bitmap>> mBitmaps;

    public VideoTrimAdapter(Context context, ArrayList<SoftReference<Bitmap>> bitmaps) {

        mContext = context;

        this.mBitmaps = bitmaps;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        screenWidth = wm.getDefaultDisplay().getWidth();

    }

    /**
     * 添加一个元素
     */
    public void add(SoftReference<Bitmap> data) {
        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>();
        }
        mBitmaps.add(data);
        notifyDataSetChanged();


    }

    private int getItemCount() {

        return mBitmaps == null ? 0 : mBitmaps.size();
    }

    public void removeAllItems() {
        mBitmaps.clear();
    }


    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.alivc_crop_item_trim_video_thumbnail, parent, false);
            holder.thumblayout = convertView.findViewById(R.id.aliyun_video_tailor_frame);
            holder.thumbImage = convertView.findViewById(R.id.aliyun_video_tailor_img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        ViewGroup.LayoutParams lParam = holder.thumblayout.getLayoutParams();

        lParam.width = (screenWidth - DensityUtils.dip2px(mContext, 40)) / 10;

        holder.thumblayout.setLayoutParams(lParam);

        Bitmap bitmap = mBitmaps.get(position).get();
        if (bitmap != null) {
            holder.thumbImage.setImageBitmap(bitmap);
        }


        return convertView;
    }


    class ViewHolder {
        private SquareFrameLayout thumblayout;
        private ImageView thumbImage;
    }

}
