package com.dl.playfun.viewadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dl.playfun.utils.StringUtil;
import com.dl.playfun.R;

import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {
    Context context;
    List<String> list;

    public ImageViewPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(imageView).load(StringUtil.getFullImageWatermarkUrl(list.get(position))).apply(new RequestOptions().placeholder(R.drawable.default_placeholder_img).override(Target.SIZE_ORIGINAL)).into(imageView);
        container.addView(imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PictureSelectorUtil.previewImage(context, list, position);
//            }
//        });

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
