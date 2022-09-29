package com.dl.playfun.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.dl.playfun.R;
import com.dl.playfun.databinding.ItemCoinpusherRoomListBinding;
import com.dl.playfun.entity.CoinPusherRoomDeviceInfo;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/24 15:51
 * Description: This is CoinPusherRoomListAdapter
 */
public class CoinPusherRoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CoinPusherRoomDeviceInfo> itemData;

    private CoinPusherRoomTagAdapter.OnItemClickListener onItemClickListener;

    public void setItemData(List<CoinPusherRoomDeviceInfo> itemData) {
        this.itemData = itemData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(CoinPusherRoomTagAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCoinpusherRoomListBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_coinpusher_room_list, null, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherRoomDeviceInfo itemEntity = itemData.get(position);
            if (!ObjectUtils.isEmpty(itemEntity)) {
                itemViewHolder.binding.setItemEntity(itemEntity);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemData == null ? 0 : itemData.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemCoinpusherRoomListBinding binding;
        public ItemViewHolder(@NonNull ItemCoinpusherRoomListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            itemView.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    public CoinPusherRoomDeviceInfo getItemEntity(int position) {
        if(itemData==null || itemData.size()<position){
            return null;
        }
       return itemData.get(position);
    }
}
