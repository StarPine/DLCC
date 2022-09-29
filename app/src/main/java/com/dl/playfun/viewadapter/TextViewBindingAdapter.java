package com.dl.playfun.viewadapter;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class TextViewBindingAdapter {
    @BindingAdapter("isBold")
    public static void setBold(TextView view, boolean isBold) {
        if (isBold) {
            view.setTypeface(null, Typeface.BOLD);
        } else {
            view.setTypeface(null, Typeface.NORMAL);
        }
    }
}
