package com.dl.playfun.ui.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dl.playfun.entity.EvaluateItemEntity;
import com.dl.playfun.R;

import java.util.ArrayList;
import java.util.List;

public class MyEvaluateAdapter extends RecyclerView.Adapter<MyEvaluateAdapter.RecyclerHolder> {

    private final Context mContext;
    private final List<EvaluateItemEntity> dataList = new ArrayList<>();

    public MyEvaluateAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public void setData(List<EvaluateItemEntity> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_evaluate, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.tvNum.setText(String.valueOf(dataList.get(position).getNumber()));
        holder.tvName.setText(dataList.get(position).getName());
        if (dataList.get(position).getNumber() > 0) {
            holder.tvNum.setBackground(ContextCompat.getDrawable(mContext, R.drawable.purple_round_bg));
            holder.tvNum.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.tvNum.setBackground(ContextCompat.getDrawable(mContext, R.drawable.gray_round_bg));
            holder.tvNum.setTextColor(mContext.getResources().getColor(R.color.gray_dark));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvNum;
        TextView tvName;

        private RecyclerHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
