package com.fine.friendlycc.viewadapter;

import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.fine.friendlycc.app.AppContext;

/**
 * Author: 彭石林
 * Time: 2021/8/4 14:21
 * Description: 设置文字模糊工具类
 */
public class TextFuzzyBindAdapter {
    @BindingAdapter(value = {"tracVipTextMF", "traceIsVip"})
    public static void setVipTextMF(TextView textView, String tracVipTextMF, boolean traceIsVip) {
        if (traceIsVip) {
            textView.setPadding(0, 0, 0, 0);
            textView.setText(tracVipTextMF);
            textView.setBackgroundResource(0);
        } else {
            textView.setText(" " + tracVipTextMF + "  ");
            textView.setPadding(20, 0, 20, 0);
            applyBlurMaskFilter(textView, BlurMaskFilter.Blur.NORMAL);
            textView.setAlpha(0.9f);
        }
    }

    // Custom method to apply BlurMaskFilter to a TextView text
    protected static void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {
        // Initialize a new BlurMaskFilter instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        BlurMaskFilter filter = new BlurMaskFilter(13, style);
        tv.getPaint().setMaskFilter(filter);
    }


    @BindingAdapter(value = {"traceIsVip"})
    public static void setVupFuzzyImg(ImageView imageView, boolean traceIsVip) {
        if (traceIsVip) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setAlpha(0.91f);
            imageView.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"textTypeDinBold"})
    public static void textTypeDinBold(TextView textView, boolean textTypeDinBold) {
        if (textTypeDinBold) {
            Typeface typeface = Typeface.createFromAsset(AppContext.instance().getAssets(), "DIN-Bold.TTF");
            textView.setTypeface(typeface);
        }
    }
}
