package com.fine.friendlycc.widget.recyclerview;

import androidx.recyclerview.widget.RecyclerView;


public class LineManagers {
    protected LineManagers() {
    }

    public static LineManagerFactory both() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new RecycleViewDivider(recyclerView.getContext(), RecycleViewDivider.LineDrawMode.BOTH);
            }
        };
    }

    public static LineManagerFactory both(final int dividerSize, final float paddingLeft, final float paddingTop, final float paddingRight, final float paddingBottom) {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                RecycleViewDivider recycleViewDivider = new RecycleViewDivider(recyclerView.getContext(), RecycleViewDivider.LineDrawMode.BOTH);
                recycleViewDivider.setDividerSize(dividerSize);
                recycleViewDivider.setPaddingTop(paddingTop);
                recycleViewDivider.setPaddingBottom(paddingBottom);
                recycleViewDivider.setPaddingLeft(paddingLeft);
                recycleViewDivider.setPaddingRight(paddingRight);
                return recycleViewDivider;
            }
        };
    }

    public static LineManagerFactory horizontal() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new RecycleViewDivider(recyclerView.getContext(), RecycleViewDivider.LineDrawMode.HORIZONTAL);
            }
        };
    }

    public static LineManagerFactory horizontal(final int dividerSize, final float paddingLeft, final float paddingRight) {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                RecycleViewDivider recycleViewDivider = new RecycleViewDivider(recyclerView.getContext(), RecycleViewDivider.LineDrawMode.HORIZONTAL);
                recycleViewDivider.setDividerSize(dividerSize);
                recycleViewDivider.setPaddingLeft(paddingLeft);
                recycleViewDivider.setPaddingRight(paddingRight);
                return recycleViewDivider;
            }
        };
    }

    public static LineManagerFactory vertical() {
        return new LineManagerFactory() {
            @Override
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new RecycleViewDivider(recyclerView.getContext(), RecycleViewDivider.LineDrawMode.VERTICAL);
            }
        };
    }

    public interface LineManagerFactory {
        RecyclerView.ItemDecoration create(RecyclerView recyclerView);
    }
}
