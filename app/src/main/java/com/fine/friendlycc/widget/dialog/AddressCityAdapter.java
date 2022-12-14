package com.fine.friendlycc.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fine.friendlycc.bean.AddressCityBean;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/13 17:57
 * Description: This is AddressCityAdapter
 */
public class AddressCityAdapter extends BaseQuickAdapter<AddressCityBean, BaseViewHolder> {


    public AddressCityAdapter(@Nullable List<AddressCityBean> data) {
        super(R.layout.item_string, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddressCityBean item) {
        baseViewHolder.setText(R.id.tv_content, item.getCity())
                .setTextColor(R.id.tv_content, item.getIsChoose() ? 0xff582197 : 0xff969696);

    }
}