package com.fine.friendlycc.binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.ui.viewmodel.BaseParkItemViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/4 21:50
 */
public class RecyclerViewAdapter {
    @BindingAdapter(value = {"gridLayoutManager", "gridList"}, requireAll = false)
    public static <T> void setGridLayoutList(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory, List<T> items) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManagerFactory.create(recyclerView);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                BaseParkItemViewModel baseParkItemViewModel = (BaseParkItemViewModel) items.get(position);
                String  itemType = (String) baseParkItemViewModel.getItemType();
                if (itemType.equals("item_park_banner")){
                    return 2;
                }
                return 1;
            }
        });
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null)
            recyclerView.setLayoutManager(gridLayoutManager);

    }
}
