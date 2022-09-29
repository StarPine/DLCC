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

import com.dl.playfun.entity.RadioTwoFilterItemEntity;
import com.dl.playfun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 石林
 */
public class DropDownTwoFilterPopupWindow extends PopupWindow {
    private final Activity mActivity;

    private View mPopView;
    private RecyclerView recyclerViewLeft,recyclerViewRight;

    private FilterLeftAdapter leftAdapter;
    private FilterRightAdapter rightAdapter;

    private OnTwoFItemClickListener mListener;

    private List<RadioTwoFilterItemEntity> mDatas = new ArrayList<>();
    private List<RadioTwoFilterItemEntity.CityBean> mRightDatas = new ArrayList<>();

    private int selectedPositionLeft = 0;
    public int selectedPositionRigth = 0;

    public DropDownTwoFilterPopupWindow(Activity activity, List<RadioTwoFilterItemEntity> datas) {
        super(activity);
        this.mActivity = activity;
        this.mDatas = datas;
        init(activity);
        setPopupWindow();
    }

    public void setDatas(List<RadioTwoFilterItemEntity> mDatas) {
        this.mDatas = mDatas;
        leftAdapter.notifyDataSetChanged();
    }
    
    
    
    public void setRightDatas(List<RadioTwoFilterItemEntity.CityBean> mDatas) {
        this.mRightDatas = mDatas;
        rightAdapter.notifyDataSetChanged();
    }
    

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);        //绑定布局
        mPopView = inflater.inflate(R.layout.pop_drop_down_two_filter, null);

        recyclerViewLeft = mPopView.findViewById(R.id.recycler_view_left);
        recyclerViewRight = mPopView.findViewById(R.id.recycler_view_right);

        leftAdapter = new FilterLeftAdapter(recyclerViewLeft);
        recyclerViewLeft.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewLeft.setAdapter(leftAdapter);

        rightAdapter = new FilterRightAdapter(recyclerViewRight);
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewRight.setAdapter(rightAdapter);


    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
//        int heightPixels = outMetrics.heightPixels;
//        this.setHeight(heightPixels/2);// 设置弹出窗口的高
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

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPositionLeft = selectedPosition;
        leftAdapter.notifyDataSetChanged();
        if (mListener != null) {
            mListener.onItemClickLeft(DropDownTwoFilterPopupWindow.this, selectedPositionLeft);
        }
    }

    public void setOnTwoFItemClickListener(OnTwoFItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnTwoFItemClickListener {
        void onItemClickLeft(DropDownTwoFilterPopupWindow popupWindow, int position);
        void onItemClickRight(DropDownTwoFilterPopupWindow popupWindow, int position);
    }



    public class FilterLeftAdapter extends RecyclerView.Adapter<FilterLeftAdapter.LeftRecyclerHolder> {

        private final Context mContext;

        public FilterLeftAdapter(RecyclerView recyclerView) {
            this.mContext = recyclerView.getContext();
        }

        @NonNull
        @Override
        public LeftRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_drop_down_filter_left, parent, false);
            return new LeftRecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LeftRecyclerHolder holder, int position) {
            RadioTwoFilterItemEntity twoFilterItemEntity = mDatas.get(position);
            holder.tvName.setText(twoFilterItemEntity.getName());
            holder.itemView.setTag(position);
            setTextColor(holder,position);
            holder.itemView.setOnClickListener(v -> {
                int p = (int) v.getTag();
                selectedPositionLeft = p;
                if (mListener != null) {
                    mListener.onItemClickLeft(DropDownTwoFilterPopupWindow.this, p);
                }
                notifyDataSetChanged();
            });
        }

        private void setTextColor(@NonNull LeftRecyclerHolder holder, int position) {
            if (position == selectedPositionLeft) {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
                holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray_middle));
                holder.tvName.setTypeface(Typeface.DEFAULT);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class LeftRecyclerHolder extends RecyclerView.ViewHolder {
            TextView tvName = null;

            private LeftRecyclerHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
            }
        }
    }

    public class FilterRightAdapter extends RecyclerView.Adapter<FilterRightAdapter.RightRecyclerHolder> {

        private final Context mContext;

        public FilterRightAdapter(RecyclerView recyclerView) {
            this.mContext = recyclerView.getContext();
        }

        @NonNull
        @Override
        public RightRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_drop_down_filter_right, parent, false);
            return new RightRecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RightRecyclerHolder holder, int position) {
            holder.tvName.setText(mRightDatas.get(position).getName());
            holder.itemView.setTag(position);
            if (position == selectedPositionRigth) {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
                holder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray_middle));
                holder.tvName.setTypeface(Typeface.DEFAULT);
            }
            holder.itemView.setOnClickListener(v -> {
                int p = (int) v.getTag();
                selectedPositionRigth = p;
                if (mListener != null) {
                    mListener.onItemClickRight(DropDownTwoFilterPopupWindow.this, p);
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return mRightDatas.size();
        }

        class RightRecyclerHolder extends RecyclerView.ViewHolder {
            TextView tvName = null;

            private RightRecyclerHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
            }
        }
    }

}
