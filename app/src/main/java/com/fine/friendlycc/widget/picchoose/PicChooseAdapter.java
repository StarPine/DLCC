package com.fine.friendlycc.widget.picchoose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.R;
import com.fine.friendlycc.widget.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class PicChooseAdapter extends RecyclerView.Adapter {

    public final static int ADD = 1001;
    public final static int IMAGE = 1002;

    private final Context mContext;
    private List<PicChooseItemEntity> dataList = new ArrayList<>();

    private PicChooseAdapterListener picChooseAdapterListener = null;

    public PicChooseAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public PicChooseAdapterListener getPicChooseAdapterListener() {
        return picChooseAdapterListener;
    }

    public void setPicChooseAdapterListener(PicChooseAdapterListener picChooseAdapterListener) {
        this.picChooseAdapterListener = picChooseAdapterListener;
    }

    public void setData(List<PicChooseItemEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType() == PicChooseItemEntity.TYPE_ADD ? ADD : IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ADD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_pic_choose_add, parent, false);
            return new AddRecyclerHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_pic_choose_item, parent, false);
            return new ImgRecyclerHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddRecyclerHolder) {
            holder.itemView.setOnClickListener(v -> {
                if (picChooseAdapterListener != null) {
                    picChooseAdapterListener.onItemAddClick();
                }
            });
        } else {
            ImgRecyclerHolder imgRecyclerHolder = (ImgRecyclerHolder) holder;
            GlideEngine.createGlideEngine().loadImage(mContext, dataList.get(position).getSrc(), imgRecyclerHolder.ivPhoto);
            imgRecyclerHolder.ivDel.setTag(position);
            imgRecyclerHolder.itemView.setTag(position);
            imgRecyclerHolder.itemView.setOnClickListener(v -> {
                int p = (int) v.getTag();
                if (picChooseAdapterListener != null) {
                    picChooseAdapterListener.onItemClick(v, p);
                }
            });
            imgRecyclerHolder.ivDel.setOnClickListener(v -> {
                if (picChooseAdapterListener != null) {
                    int p = (int) v.getTag();
                    picChooseAdapterListener.onItemDelClick(v, p);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface PicChooseAdapterListener {
        void onItemClick(View view, int position);

        void onItemDelClick(View view, int position);

        void onItemAddClick();
    }

    class AddRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView ivAdd = null;

        private AddRecyclerHolder(View itemView) {
            super(itemView);
            ivAdd = itemView.findViewById(R.id.iv_add_pic);
        }
    }

    class ImgRecyclerHolder extends RecyclerView.ViewHolder {
        RoundedImageView ivPhoto = null;
        ImageView ivDel = null;

        private ImgRecyclerHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            ivDel = itemView.findViewById(R.id.iv_del);
        }
    }
}