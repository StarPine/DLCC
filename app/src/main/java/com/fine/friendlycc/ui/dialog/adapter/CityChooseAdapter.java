package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class CityChooseAdapter extends RecyclerView.Adapter<CityChooseAdapter.RecyclerHolder> {

    private final Context mContext;
    private List<ConfigItemEntity> dataList = new ArrayList<>();

    private CityChooseAdapterListener cityChooseAdapterListener = null;
    private Integer currentChooseCityId;

    public CityChooseAdapter(RecyclerView recyclerView,Integer currentChooseCityId) {
        this.mContext = recyclerView.getContext();
        this.currentChooseCityId = currentChooseCityId;
    }

    public void setCurrentChooseCityId(Integer currentChooseCityId) {
        this.currentChooseCityId = currentChooseCityId;
        notifyDataSetChanged();
    }

    public CityChooseAdapterListener getCityChooseAdapterListener() {
        return cityChooseAdapterListener;
    }

    public void setCityChooseAdapterListener(CityChooseAdapterListener cityChooseAdapterListener) {
        this.cityChooseAdapterListener = cityChooseAdapterListener;
    }

    public void setData(List<ConfigItemEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_city_choose_dialog, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        ConfigItemEntity itemEntity = dataList.get(position);
        holder.tvName.setText(itemEntity.getName());
        holder.itemView.setTag(position);
        if (currentChooseCityId.equals(itemEntity.getId())) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.purple));
        } else {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.gray_dark));
        }
        holder.itemView.setOnClickListener(v -> {
            if (cityChooseAdapterListener != null) {
                int p = (int) v.getTag();
                cityChooseAdapterListener.onItemClick(dataList.get(p),p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CityChooseAdapterListener {
        void onItemClick(ConfigItemEntity itemEntity,int position);
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvName = null;

        private RecyclerHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}