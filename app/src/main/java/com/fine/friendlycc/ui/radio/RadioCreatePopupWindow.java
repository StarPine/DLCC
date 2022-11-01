package com.fine.friendlycc.ui.radio;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fine.friendlycc.R;

public class RadioCreatePopupWindow extends PopupWindow implements View.OnClickListener {

    private final Context context;
    private final viewOnClick viewOnClick;
    private View mView;
    private TextView tvActivity;
    private TextView tvDynamic;

    public RadioCreatePopupWindow(Context context, viewOnClick viewOnClick) {
        this.context = context;
        this.viewOnClick = viewOnClick;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_radio_create, null);
        tvActivity = mView.findViewById(R.id.tv_activity);
        tvActivity.setOnClickListener(this);

        tvDynamic = mView.findViewById(R.id.tv_dynamic);
        tvDynamic.setOnClickListener(this);
        // 设置外部可点击
        this.setOutsideTouchable(false);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.mView);
        // 设置弹出窗体的宽和高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.shop_popup_window_anim);
    }

    @Override
    public void onClick(View view) {
        viewOnClick.click(view.getId());
        dismiss();
    }

    public interface viewOnClick {
        void click(int viewId);
    }
}
