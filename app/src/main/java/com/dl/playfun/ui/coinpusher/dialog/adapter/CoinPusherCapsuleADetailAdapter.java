package com.dl.playfun.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dl.playfun.R;
import com.dl.playfun.entity.CoinPusherConverInfoEntity;
import com.dl.playfun.utils.StringUtil;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/25 14:26
 * Description: This is CoinPusherConvertDetailAdapter
 */
public class CoinPusherCapsuleADetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem> itemData;

    private OnItemClickListener onItemClickListener;

    private int defaultItemSel = 0;

    public void setItemData(List<CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem> itemData) {
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

    public CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem getItemData(int position) {
        if(itemData != null && itemData.size() > position){
            return itemData.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coinpusher_conver_capsule_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherConverInfoEntity.GoldCoinInfo.GoldCoinItem itemEntity = itemData.get(position);
            if (!ObjectUtils.isEmpty(itemEntity)) {
                itemViewHolder.tvCoin.setText(String.valueOf(itemEntity.getName()));

                Glide.with(itemViewHolder.imgIcon.getContext()).load(StringUtil.getFullImageUrl(itemEntity.getIcon()))
                        .error(R.drawable.img_pc_default_load)
                        .placeholder(R.drawable.img_pc_default_load)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(itemViewHolder.imgIcon);
                if(defaultItemSel!=-1){
                    if(defaultItemSel==position){
                        itemViewHolder.llLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_yellow_radius8_checked));
                    }else{
                        itemViewHolder.llLayout.setBackground(null);
                    }
                }else{
                    itemViewHolder.llLayout.setBackground(null);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llLayout;
        ImageView imgIcon;
        TextView tvCoin;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llLayout = itemView.findViewById(R.id.ll_layout);
            imgIcon = itemView.findViewById(R.id.img_icon);
            tvCoin = itemView.findViewById(R.id.tv_coin);

            itemView.setOnClickListener(v -> {
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
