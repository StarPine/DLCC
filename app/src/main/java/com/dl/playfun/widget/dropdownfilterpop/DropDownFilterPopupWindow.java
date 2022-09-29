package com.dl.playfun.widget.dropdownfilterpop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.playfun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 石林
 */
public class DropDownFilterPopupWindow extends PopupWindow {
    private final Activity mActivity;

    private View mPopView;
    private RecyclerView recyclerView;

    private FilterAdapter adapter;

    private OnItemClickListener mListener;

    private List<Object> mDatas = new ArrayList<>();

    private int selectedPosition = 0;

    public DropDownFilterPopupWindow(Activity activity, List datas) {
        super(activity);
        this.mActivity = activity;
        this.mDatas = datas;
        init(activity);
        setPopupWindow();
    }

    public void setDatas(List mDatas) {
        this.mDatas = mDatas;
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);        //绑定布局
        mPopView = inflater.inflate(R.layout.pop_drop_down_filter, null);

        recyclerView = mPopView.findViewById(R.id.recycler_view);

        adapter = new FilterAdapter(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
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
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
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
//
//    @Override
//    public void showAsDropDown(View anchor) {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            Window dialogWindow = mActivity.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.alpha = 0.7f;
//            dialogWindow.setAttributes(lp);
//        }
//        super.showAsDropDown(anchor);
//    }
//
//    @Override
//    public void dismiss() {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            Window dialogWindow = mActivity.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.alpha = 1.0f;
//            dialogWindow.setAttributes(lp);
//        }
//
//        super.dismiss();
//    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        adapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void onItemClick(DropDownFilterPopupWindow popupWindow, int position);
    }

    public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.RecyclerHolder> {

        private final Context mContext;

        public FilterAdapter(RecyclerView recyclerView) {
            this.mContext = recyclerView.getContext();
        }

        @NonNull
        @Override
        public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_drop_down_filter, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
            Object name = mDatas.get(position);
            holder.tvName.setText(name.toString());
            holder.itemView.setTag(position);
            if (position == selectedPosition) {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
                holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray_middle));
                holder.tvName.setTypeface(Typeface.DEFAULT);
            }
            holder.itemView.setOnClickListener(v -> {
                int p = (int) v.getTag();
                selectedPosition = p;
                if (mListener != null) {
                    mListener.onItemClick(DropDownFilterPopupWindow.this, p);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
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
