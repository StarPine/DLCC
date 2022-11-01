package com.fine.friendlycc.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.entity.CoinPusherConverInfoEntity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/25 18:24
 * Description: 金币兑换砖石 CoinPusherConvertAdapter
 */
public class CoinPusherConvertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CoinPusherConverInfoEntity.DiamondsInfo> itemData;
    private CoinPusherCapsuleADetailAdapter.OnItemClickListener onItemClickListener;
    private int defaultItemSel = -1;

    private int maxValue = 0;

    public void setItemData(List<CoinPusherConverInfoEntity.DiamondsInfo> itemData,int maxValue) {
        this.itemData = itemData;
        this.maxValue = maxValue;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(CoinPusherCapsuleADetailAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDefaultSelect(int position) {
        if(itemData != null && itemData.size() > position){
            this.defaultItemSel = position;
            notifyDataSetChanged();
        }
    }

    public void setMaxValuerSelect(int maxValue) {
        if(itemData != null && itemData.size() > 0){
            this.maxValue = maxValue;
            notifyDataSetChanged();
        }
    }

    public CoinPusherConverInfoEntity.DiamondsInfo getItemData(int position) {
        if(itemData != null && itemData.size() > position){
            return itemData.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coinpusher_conver_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherConverInfoEntity.DiamondsInfo itemEntity = itemData.get(position);
            if (!ObjectUtils.isEmpty(itemEntity)) {
                itemViewHolder.tvCoin.setText(String.valueOf(itemEntity.getGoldValue()));
                if(defaultItemSel!=-1){
                    if(defaultItemSel==position){
                        itemViewHolder.flLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_radius20_red));
                    }else{
                        itemViewHolder.flLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_radius20_shade));
                    }
                }else{
                    itemViewHolder.flLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_radius20_shade));
                }
                if(itemEntity.getGoldValue() > maxValue){
                    itemViewHolder.flLayout.setBackground(Utils.getApp().getDrawable(R.drawable.shape_radius20_shade));
                    itemViewHolder.tvCoin.setTextColor(ColorUtils.getColor(R.color.gray_light));
                }else{
                    itemViewHolder.tvCoin.setTextColor(ColorUtils.getColor(R.color.black));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flLayout;
        TextView tvCoin;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            flLayout = itemView.findViewById(R.id.fl_layout);
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
