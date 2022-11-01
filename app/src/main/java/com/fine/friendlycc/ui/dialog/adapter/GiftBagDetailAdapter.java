package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.entity.GiftBagEntity;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/7 17:09
 * Description: This is GiftBagDetailAdapter
 */
public class GiftBagDetailAdapter extends RecyclerView.Adapter<GiftBagDetailAdapter.ItemViewHolder>{

    private final Context mContext;
    private List<GiftBagEntity.giftEntity> listData = null;

    private OnClickDetailListener onClickDetailListener = null;
    private boolean isDarkShow = false;


    public GiftBagDetailAdapter(RecyclerView recyclerView, List<GiftBagEntity.giftEntity> data, boolean isDarkShow) {
        this.mContext = recyclerView.getContext();
        this.listData = data;
        this.isDarkShow = isDarkShow;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift_bag_item_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = holder;
        if (listData != null && listData.size() > 0) {
            int index = position;
            GiftBagEntity.giftEntity itemEntity = listData.get(index);
            itemViewHolder.title.setText(itemEntity.getName());
            if (isDarkShow) {
                itemViewHolder.title.setTextColor(Color.parseColor("#F1F2F9"));
            } else {
                itemViewHolder.title.setTextColor(Color.parseColor("#333333"));
            }
            itemViewHolder.money.setText(String.valueOf(itemEntity.getMoney()));
            Glide.with(TUIChatService.getAppContext()).load(StringUtil.getFullImageUrl(itemEntity.getImg()))
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.icon_url);
            itemViewHolder.detail_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickDetailListener != null) {
                        onClickDetailListener.clickDetailCheck(index, itemEntity, itemViewHolder.detail_layout);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(listData==null){
            return 0;
        }
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_url;
        TextView title;
        TextView money;
        LinearLayout detail_layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            detail_layout = itemView.findViewById(R.id.detail_layout);
            icon_url = itemView.findViewById(R.id.icon_url);
            title = itemView.findViewById(R.id.title);
            money = itemView.findViewById(R.id.money);
        }
    }

    public void setOnClickListener(OnClickDetailListener onClickListener){
        this.onClickDetailListener = onClickListener;
    }

    public interface  OnClickDetailListener{
        void clickDetailCheck(int position,GiftBagEntity.giftEntity itemEntity,LinearLayout detail_layout);
    }
}
