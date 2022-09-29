package com.dl.playfun.ui.coinpusher.dialog.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.dl.playfun.R;
import com.dl.playfun.databinding.ItemCoinpusherRoomHistoryBinding;
import com.dl.playfun.entity.CoinPusherRoomHistoryEntity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/26 15:31
 * Description: This is CoinPusherGameHistoryAdapter
 */
public class CoinPusherGameHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CoinPusherRoomHistoryEntity> itemData;

    public void setItemData(List<CoinPusherRoomHistoryEntity> itemData) {
        this.itemData = itemData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCoinpusherRoomHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_coinpusher_room_history, null, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(itemData)) {
            CoinPusherRoomHistoryEntity itemEntity = itemData.get(position);
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
        ItemCoinpusherRoomHistoryBinding binding;
        public ItemViewHolder(@NonNull ItemCoinpusherRoomHistoryBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
