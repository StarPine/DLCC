package com.fine.friendlycc.viewadapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.R;
import com.fine.friendlycc.transformations.MvBlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author wulei
 */
public class BurnThumbImageViewAdapter {
    @BindingAdapter(value = {"burnThumbImgPath", "isBurn", "burnStatus"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String burnThumbImgPath, boolean isBurn, int burnStatus) {
        try {
            boolean isVideo = false;
            if (burnThumbImgPath.toLowerCase().endsWith(".mp4")) {
                isVideo = true;
            }
            String fullUrl = StringUtil.getFullThumbImageUrl(burnThumbImgPath);
            RequestManager requestManager = Glide.with(imageView.getContext());
            if (isVideo) {
                requestManager.setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1)
                                .centerCrop()
                );
            }
            if (isBurn) {
                requestManager.load(fullUrl)
                        .apply(bitmapTransform(new MvBlurTransformation(25)))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_placeholder_img)
                                .error(R.drawable.default_placeholder_img))
                        .into(imageView);
            } else {
                requestManager.load(fullUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_placeholder_img)
                                .error(R.drawable.default_placeholder_img))
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
