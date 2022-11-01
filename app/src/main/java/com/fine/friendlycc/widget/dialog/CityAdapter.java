package com.fine.friendlycc.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.R;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<ConfigItemEntity, BaseViewHolder> {

    public CityAdapter(@Nullable List<ConfigItemEntity> data) {
        super(R.layout.item_string, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ConfigItemEntity item) {
        baseViewHolder.setText(R.id.tv_content, item.getName())
                .setTextColor(R.id.tv_content, item.getIsChoose() ? 0xff582197 : 0xff969696);

    }
}
