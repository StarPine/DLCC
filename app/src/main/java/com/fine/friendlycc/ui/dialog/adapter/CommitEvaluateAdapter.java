package com.fine.friendlycc.ui.dialog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.entity.EvaluateItemEntity;
import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.List;

public class CommitEvaluateAdapter extends RecyclerView.Adapter<CommitEvaluateAdapter.RecyclerHolder> {

    private final Context mContext;
    private final List<EvaluateItemEntity> dataList = new ArrayList<>();

    private OnItemClickListener onItemClickListener = null;

    public CommitEvaluateAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_commit_evaluate, parent, false);
        return new RecyclerHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        EvaluateItemEntity itemEntity = dataList.get(position);
        holder.tvName.setText(itemEntity.getName());
        if (!itemEntity.isSelected()) {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.elevation_gray_round_background));
            holder.tvName.setTextColor(mContext.getColor(R.color.gray_dark));
        } else {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.elevation_purple_dark_background));
            holder.tvName.setTextColor(mContext.getColor(R.color.white));
        }
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        private RecyclerHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
