package com.dl.playfun.ui.mine.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dl.playfun.entity.ConfigItemEntity;
import com.dl.playfun.R;

import java.util.List;

public class HopeAdapter extends BaseQuickAdapter<ConfigItemEntity, BaseViewHolder> {
    public HopeAdapter(@Nullable List<ConfigItemEntity> data) {
        super(R.layout.recy_dialog_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ConfigItemEntity s) {
        baseViewHolder.setText(R.id.tv_name, s.getName());
        if (s.getIsChoose()) {
            baseViewHolder.setTextColor(R.id.tv_name, 0xffB35EDF);
        } else {
            baseViewHolder.setTextColor(R.id.tv_name, 0xff999999);
        }
    }
}
