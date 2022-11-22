package com.fine.friendlycc.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.R;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<ConfigItemBean, BaseViewHolder> {

    public CityAdapter(@Nullable List<ConfigItemBean> data) {
        super(R.layout.item_string, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ConfigItemBean item) {
        baseViewHolder.setText(R.id.tv_content, item.getName())
                .setTextColor(R.id.tv_content, item.getIsChoose() ? 0xff582197 : 0xff969696);

    }
}