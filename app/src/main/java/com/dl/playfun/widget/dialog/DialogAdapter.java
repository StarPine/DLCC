package com.dl.playfun.widget.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dl.playfun.R;

import java.util.List;

public class DialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final List<Integer> closePosion;

    public DialogAdapter(@Nullable List<String> data, List<Integer> closePosion) {
        super(R.layout.item_string, data);
        this.closePosion = closePosion;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_content, s)
                .setTextColor(R.id.tv_content, 0xff969696);
        if (closePosion != null && closePosion.size() != 0) {
            for (int i = 0; i < closePosion.size(); i++) {
                if (closePosion.get(i) == baseViewHolder.getLayoutPosition()) {
                    baseViewHolder.setTextColor(R.id.tv_content, 0xff582197);
                    break;
                }
            }
        }
    }
}
