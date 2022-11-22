package com.fine.friendlycc.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fine.friendlycc.bean.OccupationConfigItemBean;
import com.fine.friendlycc.R;

import java.util.List;

public class OccputonParentAdapter extends BaseQuickAdapter<OccupationConfigItemBean, BaseViewHolder> {

    public OccputonParentAdapter(@Nullable List<OccupationConfigItemBean> data) {
        super(R.layout.item_occupation_parent, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, OccupationConfigItemBean configEntity) {
        baseViewHolder.setText(R.id.tv_content, configEntity.getName())
                .setTextColor(R.id.tv_content, configEntity.isChoose() ? 0xffffffff : 0xff333333);
        //.setBackgroundColor(R.id.tv_content, configEntity.isChoose()?0xffA72DFE:0xffF8F8F7);
        baseViewHolder.getView(R.id.tv_content).setSelected(configEntity.isChoose());
    }
}