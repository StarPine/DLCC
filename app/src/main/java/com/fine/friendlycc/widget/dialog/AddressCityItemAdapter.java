package com.fine.friendlycc.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fine.friendlycc.entity.AddressCityItemEntity;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/13 18:43
 * Description: This is AddressCityItemAdapter
 */
public class AddressCityItemAdapter extends BaseQuickAdapter<AddressCityItemEntity, BaseViewHolder> {


    public AddressCityItemAdapter(@Nullable List<AddressCityItemEntity> data) {
        super(R.layout.item_string, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddressCityItemEntity item) {
        baseViewHolder.setText(R.id.tv_content, item.getRegion())
                .setTextColor(R.id.tv_content, item.getIsChoose() ? 0xff582197 : 0xff969696);

    }
}