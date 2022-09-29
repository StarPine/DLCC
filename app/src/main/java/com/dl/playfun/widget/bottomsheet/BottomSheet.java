package com.dl.playfun.widget.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.playfun.R;

/**
 * @author wulei
 */
public class BottomSheet {

    public static final int BOTTOM_SHEET_TYPE_NORMAL = 0;
    public static final int BOTTOM_SHEET_TYPE_SINGLE_CHOOSE = 1;

    private BottomSheetView bottomSheetView;

    private BottomSheet() {
    }

    private BottomSheet(Builder builder) {
        bottomSheetView = new BottomSheetView(builder.activity, this, builder.datas);
        bottomSheetView.setType(builder.type);
        bottomSheetView.setCancelClickListener(builder.cancelClickListener);
        bottomSheetView.setItemSelectedListener(builder.itemSelectedListener);
        bottomSheetView.setCancelText(builder.cancelName);
        bottomSheetView.setTitleText(builder.titleText);
        bottomSheetView.setShowCancelButton(builder.showCancel);
        bottomSheetView.setShowTitle(builder.showTitle);
        bottomSheetView.setSelectedPosition(builder.selectIndex);
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
        void onCancelClick(BottomSheet bottomSheet);
    }

    public interface ItemSelectedListener {
        void onItemSelected(BottomSheet bottomSheet, int position);
    }

    public static class Builder {
        private final Activity activity;
        private int type;
        private String titleText;
        private int selectIndex = 0;
        private String[] datas;
        private boolean showCancel, showTitle;
        private String cancelName;
        private CancelClickListener cancelClickListener;
        private ItemSelectedListener itemSelectedListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
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

        public Builder setDatas(String[] datas) {
            this.datas = datas;
            return this;
        }

        public Builder setSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
            return this;
        }

        public Builder setOnItemSelectedListener(ItemSelectedListener itemSelectedListener) {
            this.itemSelectedListener = itemSelectedListener;
            return this;
        }

        public BottomSheet build() {
            return new BottomSheet(this);
        }
    }

    private static class BottomSheetView extends PopupWindow implements View.OnClickListener {

        private final Activity mActivity;
        private final BottomSheet bottomSheet;
        private View mPopView;
        private RecyclerView recyclerView;
        private TextView tvTitle;
        private TextView tvCancel;
        private BottomSheetAdapter adapter;

        private ItemSelectedListener itemSelectedListener;

        private CancelClickListener cancelClickListener;

        private int type = BOTTOM_SHEET_TYPE_NORMAL;
        private String[] mDatas;

        private int selectedPosition = 0;

        public BottomSheetView(Activity activity, BottomSheet bottomSheet, String[] datas) {
            super(activity);
            this.mActivity = activity;
            this.bottomSheet = bottomSheet;
            this.mDatas = datas;
            init(activity);
            setPopupWindow();
        }

        public ItemSelectedListener getItemSelectedListener() {
            return itemSelectedListener;
        }

        public void setItemSelectedListener(ItemSelectedListener itemSelectedListener) {
            this.itemSelectedListener = itemSelectedListener;
        }

        public CancelClickListener getCancelClickListener() {
            return cancelClickListener;
        }

        public void setCancelClickListener(CancelClickListener cancelClickListener) {
            this.cancelClickListener = cancelClickListener;
        }

        public void setDatas(String[] mDatas) {
            this.mDatas = mDatas;
            adapter.notifyDataSetChanged();
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        /**
         * 初始化
         *
         * @param context
         */
        private void init(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            mPopView = inflater.inflate(R.layout.view_bottom_sheet, null);

            recyclerView = mPopView.findViewById(R.id.recycler_view);
            tvTitle = mPopView.findViewById(R.id.tv_title);
            tvCancel = mPopView.findViewById(R.id.tv_cancel);

            adapter = new BottomSheetAdapter(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            tvCancel.setOnClickListener(this);
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

        public void setTitleText(String titleText) {
            tvTitle.setText(titleText);
        }

        public void setCancelText(String text) {
            tvCancel.setText(text);
        }

        public void setShowTitle(boolean show) {
            if (show) {
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
        }

        public void setShowCancelButton(boolean show) {
            if (show) {
                tvCancel.setVisibility(View.VISIBLE);
            } else {
                tvCancel.setVisibility(View.GONE);
            }
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
            if (view.getId() == R.id.tv_cancel) {
                if (cancelClickListener != null) {
                    cancelClickListener.onCancelClick(bottomSheet);
                } else {
                    this.dismiss();
                }
            }
        }

        public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.RecyclerHolder> {

            private final Context mContext;

            public BottomSheetAdapter(RecyclerView recyclerView) {
                this.mContext = recyclerView.getContext();
            }

            @NonNull
            @Override
            public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_sheet, parent, false);
                return new RecyclerHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
                String name = mDatas[position];
                holder.tvName.setText(name);
                holder.itemView.setTag(position);
                if (type == BottomSheet.BOTTOM_SHEET_TYPE_NORMAL) {
                    holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
                    holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    if (position == selectedPosition) {
                        holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
                        holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        holder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray_middle));
                        holder.tvName.setTypeface(Typeface.DEFAULT);
                    }
                }
                holder.itemView.setOnClickListener(v -> {
                    int p = (int) v.getTag();
                    selectedPosition = p;
                    if (itemSelectedListener != null) {
                        itemSelectedListener.onItemSelected(bottomSheet, p);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mDatas.length;
            }

            class RecyclerHolder extends RecyclerView.ViewHolder {
                TextView tvName = null;

                private RecyclerHolder(View itemView) {
                    super(itemView);
                    tvName = itemView.findViewById(R.id.tv_name);
                }
            }
        }

    }

}
