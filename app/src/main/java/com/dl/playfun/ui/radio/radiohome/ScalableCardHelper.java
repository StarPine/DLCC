package com.dl.playfun.ui.radio.radiohome;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.playfun.R;

import java.lang.ref.WeakReference;


/**
 * Created by yjwfn on 2017/9/21.
 */

public class ScalableCardHelper {

    private static final float STAY_SCALE = 0.95f;

    private String TAG = "ScalableCardHelper";
    private PagerSnapHelper snapHelper = new PagerSnapHelper();
//    private PagerSnapHelper snapHelper = new PagerSnapHelper(){
//        // 在 Adapter的 onBindViewHolder 之后执行
//        @Override
//        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
//            // TODO 找到对应的Index
//            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
//                return RecyclerView.NO_POSITION;
//            }
//
//            final View currentView = findSnapView(layoutManager);
//
//            if (currentView == null) {
//                return RecyclerView.NO_POSITION;
//            }
//
//            LinearLayoutManager myLayoutManager = (LinearLayoutManager) layoutManager;
//
//            int position1 = myLayoutManager.findFirstVisibleItemPosition();
//            int position2 = myLayoutManager.findLastVisibleItemPosition();
//
//            int currentPosition = layoutManager.getPosition(currentView);
//
//            if (currentPosition == RecyclerView.NO_POSITION) {
//                return RecyclerView.NO_POSITION;
//            }
//            if (currentPosition >= 0) {
//                pageScrolled();
//            }
//            return currentPosition;
//        }
//
//        // 在 Adapter的 onBindViewHolder 之后执行
//        @Nullable
//        @Override
//        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
//            // TODO 找到对应的View
//            return super.findSnapView(layoutManager);
//        }
//    };
    private RecyclerView recyclerView;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            pageScrolled();
        }
    };


    private void pageScrolled() {
        if (recyclerView == null || recyclerView.getChildCount() == 0)
            return;

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        View snapingView = snapHelper.findSnapView(layoutManager);
        int snapingViewPosition = recyclerView.getChildAdapterPosition(snapingView);
        View leftSnapingView = layoutManager.findViewByPosition(snapingViewPosition - 1);
        View rightSnapingView = layoutManager.findViewByPosition(snapingViewPosition + 1);
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            //获取最后一个可见view的位置
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            //可见第一个 和最后一个 相减数量大于1.且是居中的。其它均为缩小
            if(lastItemPosition!=snapingViewPosition ||  firstItemPosition!=snapingViewPosition){
                resizeScaleY(snapingView,1.2f);
                resizeScaleY(leftSnapingView,1f);
                resizeScaleY(rightSnapingView,1f);
                //当前仅仅是第一个可见
                resizeScaleY(snapingView,1.2f);
                if(firstItemPosition == snapingViewPosition){
                    resizeScaleY(rightSnapingView,1f);
                }else{
                    resizeScaleY(leftSnapingView,1f);
                }
            }
        }
    }

    public void resizeScaleY(View currentView,float scale) {
        if(currentView!=null){
            View imgUserAvatar = currentView.findViewById(R.id.rl_layout);
            if(imgUserAvatar != null){
                imgUserAvatar.setScaleY(scale);
            }
        }
    }

    public void attachToRecyclerView(final RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.addItemDecoration(new ScalableCardItemDecoration());
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                pageScrolled();
            }
        });
    }


    public static int getPeekWidth(RecyclerView recyclerView, View itemView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        boolean isVertical = layoutManager.canScrollVertically();
        int position = recyclerView.getChildAdapterPosition(itemView);
        //TODO RecyclerView使用wrap_content时，获取的宽度可能会是0。
        int parentWidth = recyclerView.getMeasuredWidth();
        int parentHeight = recyclerView.getMeasuredHeight(); //有时会拿到0
        parentWidth = parentWidth == 0 ? recyclerView.getWidth() : parentWidth;
        parentHeight = parentHeight == 0 ? recyclerView.getHeight() : parentHeight;
        int parentEnd = isVertical ? parentHeight : parentWidth;
        int parentCenter = parentEnd / 2;

        int itemSize = isVertical ? itemView.getMeasuredHeight() : itemView.getMeasuredWidth();

        if (itemSize == 0) {

            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            int widthMeasureSpec =
                    RecyclerView.LayoutManager.getChildMeasureSpec(parentWidth,
                            layoutManager.getWidthMode(),
                            recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(),
                            layoutParams.width, layoutManager.canScrollHorizontally());

            int heightMeasureSpec =
                    RecyclerView.LayoutManager.getChildMeasureSpec(parentHeight,
                            layoutManager.getHeightMode(),
                            recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(),
                            layoutParams.height, layoutManager.canScrollVertically());


            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            itemSize = isVertical ? itemView.getMeasuredHeight() : itemView.getMeasuredWidth();
        }


        /*
            计算ItemDecoration的大小，确保插入的大小正好使view的start + itemSize / 2等于parentCenter。
         */
        int startOffset = parentCenter - itemSize / 2;
        int endOffset = parentEnd - (startOffset + itemSize);

        return position == 0 ? startOffset : endOffset;
    }


    private static class ScalableCardItemDecoration extends RecyclerView.ItemDecoration {


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
            int position = holder.getAdapterPosition() == RecyclerView.NO_POSITION ? holder.getOldPosition() : holder.getAdapterPosition();
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            int itemCount = layoutManager.getItemCount();

            if(position != 0 && position != itemCount - 1){
                return;
            }

            int peekWidth = getPeekWidth(parent, view);
            boolean isVertical = layoutManager.canScrollVertically();
            //移除item时adapter position为-1。
            if (isVertical) {
                if (position == 0) {
                    outRect.set(0, peekWidth, 0, 0);
                } else if (position == itemCount - 1) {
                    outRect.set(0, 0, 0, peekWidth);
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            } else {
                if (position == 0) {
                    outRect.set(peekWidth, 0, 0, 0);
                } else if (position == itemCount - 1) {
                    outRect.set(0, 0, peekWidth, 0);
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context mContext,float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}