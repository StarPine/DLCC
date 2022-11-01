package com.fine.friendlycc.viewadapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.fine.friendlycc.R;

/**
 * @author wulei
 */
public class PointAdapter {
    @BindingAdapter(value = {"PointSize"}, requireAll = false)
    public static void setPoint(LinearLayout linearLayout, Integer viewSize) {
        try {
            if (viewSize != null) {
                linearLayout.removeAllViews();
                if (viewSize <= 1) {
                    return;
                }
                for (int i = 0; i < viewSize; i++) {
                    ImageView imageView = new ImageView(linearLayout.getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(6), SizeUtils.dp2px(6));
                    params.setMargins(SizeUtils.dp2px(5), 0, SizeUtils.dp2px(5), 0);
                    imageView.setLayoutParams(params);
                    imageView.setBackground(ResourceUtils.getDrawable(R.drawable.point_white));
                    linearLayout.addView(imageView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = {"PointPositon"}, requireAll = false)
    public static void setPosition(LinearLayout linearLayout, Integer positon) {
        try {
            if (linearLayout.getChildCount() > 0) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    ImageView imageView = (ImageView) linearLayout.getChildAt(i);
                    imageView.setBackground(ResourceUtils.getDrawable(R.drawable.point_white));
                    if (i == positon) {
                        imageView.setBackground(ResourceUtils.getDrawable(R.drawable.point_purple));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
