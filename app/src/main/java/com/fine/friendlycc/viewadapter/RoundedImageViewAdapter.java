package com.fine.friendlycc.viewadapter;

import androidx.databinding.BindingAdapter;

import com.fine.friendlycc.widget.roundedimageview.RoundedImageView;

public class RoundedImageViewAdapter {

    @BindingAdapter({"riv_border_color"})
    public static void setBorderColor(RoundedImageView view, Integer color) {
        view.setBorderColor(color);
    }

    @BindingAdapter({"riv_border_width"})
    public static void setBorderWidth(RoundedImageView view, Float width) {
        view.setBorderWidth(width);
    }
}
