package com.fine.friendlycc.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.entity.CoinPusherRoomTagInfoEntity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/24 12:16
 * Description: This is CoinPusherRoomTagAdapter
 */
public class CoinPusherRoomTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    private List<CoinPusherRoomTagInfoEntity.DeviceTag> itemData;

    private OnItemClickListener onItemClickListener;

    private int defaultItemSel = -1;


    public void setItemData(List<CoinPusherRoomTagInfoEntity.DeviceTag> itemData) {
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

    public CoinPusherRoomTagInfoEntity.DeviceTag getItemData(int position) {
        if(itemData != null && itemData.size() > position){
            return itemData.get(position);
        }
        return null;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coinpusher_room_tag, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherRoomTagInfoEntity.DeviceTag itemEntity = itemData.get(position);
            if (!ObjectUtils.isEmpty(itemEntity)) {
                itemViewHolder.tvName.setText(itemEntity.getName());
                if(itemEntity.getIsRecommend()==1){
                    itemViewHolder.tvName.setTextColor(ColorUtils.getColor(R.color.pseekbar_process));
                }else{
                    itemViewHolder.tvName.setTextColor(ColorUtils.getColor(R.color.black));
                }
                if(defaultItemSel!=-1){
                    if(defaultItemSel==position){
                        itemViewHolder.imgIcon.setImageResource(R.drawable.img_coinpusher_tag_checked);
                    }else{
                        itemViewHolder.imgIcon.setImageResource(R.drawable.img_coinpusher_tag_normal);
                    }
                }else{
                    itemViewHolder.imgIcon.setImageResource(R.drawable.img_coinpusher_tag_normal);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvName;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
            tvName = itemView.findViewById(R.id.tv_name);
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
