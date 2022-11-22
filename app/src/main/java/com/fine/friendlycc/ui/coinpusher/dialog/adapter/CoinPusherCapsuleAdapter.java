package com.fine.friendlycc.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.R;
import com.fine.friendlycc.bean.CoinPusherConverInfoBean;
import com.fine.friendlycc.utils.StringUtil;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/23 15:57
 * Description: This is CoinPusherCapsuleAdapter
 */
public class CoinPusherCapsuleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CoinPusherConverInfoBean.GoldCoinInfo> itemData;

    private OnItemClickListener onItemClickListener;

    private int defaultItemSel = -1;

    public void setItemData(List<CoinPusherConverInfoBean.GoldCoinInfo> itemData) {
        this.itemData = itemData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDefaultSelect(int position) {
        if(itemData != null && itemData.size() > position){
            this.defaultItemSel = position;
            notifyDataSetChanged();
        }
    }

    public CoinPusherConverInfoBean.GoldCoinInfo getItemData(int position) {
        if(itemData != null && itemData.size() > position){
           return itemData.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coinpusher_capsule, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherConverInfoBean.GoldCoinInfo itemEntity = itemData.get(position);
            if (!ObjectUtils.isEmpty(itemEntity)) {
                itemViewHolder.tvName.setText(itemEntity.getName());
                itemViewHolder.tvCoin.setText(String.valueOf(itemEntity.getCoin()));

                Glide.with(itemViewHolder.imgIcon.getContext()).load(StringUtil.getFullImageUrl(itemEntity.getIcon()))
                        .error(R.drawable.img_pc_default_load)
                        .placeholder(R.drawable.img_pc_default_load)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(itemViewHolder.imgIcon);
                if(defaultItemSel!=-1){
                    if(defaultItemSel==position){
                        itemViewHolder.rlLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_yellow_radius8_checked));
                    }else{
                        itemViewHolder.rlLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_yellow_radius8_normal));
                    }
                }else{
                    itemViewHolder.rlLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_yellow_radius8_normal));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlLayout;
        ImageView imgIcon;
        TextView tvName;
        TextView tvCoin;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
            rlLayout = itemView.findViewById(R.id.rl_layout);

            tvName = itemView.findViewById(R.id.tv_name);
            tvCoin = itemView.findViewById(R.id.tv_coin);

            rlLayout.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        /**
         * item click listener
         */
        void onItemClick(int position);
    }
}