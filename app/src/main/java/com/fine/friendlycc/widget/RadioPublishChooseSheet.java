package com.fine.friendlycc.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fine.friendlycc.R;

/**
 * @author wulei
 */
public class RadioPublishChooseSheet {

    private RadioPublishChooseSheetView bottomSheetView;

    private RadioPublishChooseSheet() {
    }

    private RadioPublishChooseSheet(Builder builder) {
        bottomSheetView = new RadioPublishChooseSheetView(builder.activity, this);
        bottomSheetView.setCancelClickListener(builder.cancelClickListener);
        bottomSheetView.setTypeSelectedListener(builder.typeSelectedListener);
        bottomSheetView.setDismissListener(builder.dismissListener);
    }

    public void show() {
        if (bottomSheetView != null) {
            bottomSheetView.show();
        }
    }

    public void dismiss() {
        if (bottomSheetView != null) {
            bottomSheetView.dismiss();
        }
    }

    public interface CancelClickListener {
        void onCancelClick(RadioPublishChooseSheet bottomSheet);
    }

    public interface TypeSelectedListener {
        void onTypeSelected(RadioPublishChooseSheet bottomSheet, int type);
    }

    public interface DismissListener {
        void onDismiss(RadioPublishChooseSheet bottomSheet);
    }

    public static class Builder {
        private final Activity activity;
        private String titleText;
        private boolean showCancel, showTitle;
        private String cancelName;
        private CancelClickListener cancelClickListener;
        private TypeSelectedListener typeSelectedListener;
        private DismissListener dismissListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String titleText) {
            this.showTitle = true;
            this.titleText = titleText;
            return this;
        }

        public Builder setCancelButton(String cancelName, CancelClickListener cancelListener) {
            this.showCancel = true;
            this.cancelName = cancelName;
            this.cancelClickListener = cancelListener;
            return this;
        }

        public Builder setOnTypeSelectedListener(TypeSelectedListener typeSelectedListener) {
            this.typeSelectedListener = typeSelectedListener;
            return this;
        }

        public Builder setDismissListener(DismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        public RadioPublishChooseSheet build() {
            return new RadioPublishChooseSheet(this);
        }
    }

    private static class RadioPublishChooseSheetView extends PopupWindow implements View.OnClickListener {

        private final Activity mActivity;
        private final RadioPublishChooseSheet bottomSheet;
        private View mPopView;
        private ImageView ivClose;
        private TextView tvProgram;
        private TextView tvDynamic;
        private TypeSelectedListener typeSelectedListener;

        private CancelClickListener cancelClickListener;

        private DismissListener dismissListener;

        public RadioPublishChooseSheetView(Activity activity, RadioPublishChooseSheet bottomSheet) {
            super(activity);
            this.mActivity = activity;
            this.bottomSheet = bottomSheet;
            init(activity);
            setPopupWindow();
        }

        public TypeSelectedListener getTypeSelectedListener() {
            return typeSelectedListener;
        }

        public void setTypeSelectedListener(TypeSelectedListener typeSelectedListener) {
            this.typeSelectedListener = typeSelectedListener;
        }

        public CancelClickListener getCancelClickListener() {
            return cancelClickListener;
        }

        public void setCancelClickListener(CancelClickListener cancelClickListener) {
            this.cancelClickListener = cancelClickListener;
        }

        public DismissListener getDismissListener() {
            return dismissListener;
        }

        public void setDismissListener(DismissListener dismissListener) {
            this.dismissListener = dismissListener;
        }

        /**
         * 初始化
         *
         * @param context
         */
        private void init(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mPopView = inflater.inflate(R.layout.radio_publish_choose_sheet, null);

            ivClose = mPopView.findViewById(R.id.iv_close);
            tvProgram = mPopView.findViewById(R.id.tv_program);
            tvDynamic = mPopView.findViewById(R.id.tv_dynamic);

            tvDynamic.setOnClickListener(this);
            tvProgram.setOnClickListener(this);
            ivClose.setOnClickListener(this);
        }

        /**
         * 设置窗口的相关属性
         */
        @SuppressLint("InlinedApi")
        private void setPopupWindow() {
            this.setContentView(mPopView);// 设置View
            this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
            this.setFocusable(true);// 设置弹出窗口可
            this.setAnimationStyle(R.style.popup_window_anim);// 设置动画
            this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
            mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int height = mPopView.findViewById(R.id.pop_container).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }

        @Override
        public void dismiss() {
            if (mActivity != null && !mActivity.isFinishing()) {
                Window dialogWindow = mActivity.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.alpha = 1.0f;
                dialogWindow.setAttributes(lp);
            }
            if (dismissListener != null) {
                dismissListener.onDismiss(bottomSheet);
            }
            super.dismiss();
        }

        public void show() {

            if (mActivity != null && !mActivity.isFinishing()) {
                Window dialogWindow = mActivity.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.alpha = 0.5f;
                dialogWindow.setAttributes(lp);
            }
            this.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_close) {
                if (cancelClickListener != null) {
                    cancelClickListener.onCancelClick(bottomSheet);
                } else {
                    this.dismiss();
                }
            } else if (view.getId() == R.id.tv_program) {
                if (typeSelectedListener != null) {
                    typeSelectedListener.onTypeSelected(bottomSheet, 1);
                }
            } else if (view.getId() == R.id.tv_dynamic) {
                if (typeSelectedListener != null) {
                    typeSelectedListener.onTypeSelected(bottomSheet, 2);
                }
            }
        }

    }

}
