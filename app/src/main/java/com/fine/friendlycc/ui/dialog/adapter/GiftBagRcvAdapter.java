package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.entity.GiftBagAdapterEntity;
import com.fine.friendlycc.entity.GiftBagEntity;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/7 16:32
 * Description: This is GiftBagRcvAdapter
 */
public class GiftBagRcvAdapter extends RecyclerView.Adapter<GiftBagRcvAdapter.GiftBagRcvHolder> {

    private final Context mContext;
    private List<GiftBagAdapterEntity> itemData = null;

    private OnClickRcvDetailListener onClickDetailListener = null;

    private boolean isDarkShow = false;

    public GiftBagRcvAdapter(RecyclerView recyclerView, List<GiftBagAdapterEntity> dataList, boolean isDarkShow) {
        this.mContext = recyclerView.getContext();
        this.itemData = dataList;
        this.isDarkShow = isDarkShow;
    }

    @NonNull
    @Override
    public GiftBagRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift_bag_item_rcv, parent, false);
        return new GiftBagRcvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftBagRcvHolder holder, int position) {
        GiftBagRcvHolder itemViewHolder = holder;
        if(itemData!=null && itemData.size()>0){
            int index = position;
            GridLayoutManager layoutManage = new GridLayoutManager(mContext, 5);
            itemViewHolder.rcvDetail.setLayoutManager(layoutManage);
            GiftBagDetailAdapter giftBagDetailAdapter = new GiftBagDetailAdapter(itemViewHolder.rcvDetail, itemData.get(index).getGiftBagEntity(), isDarkShow);
            giftBagDetailAdapter.setOnClickListener(new GiftBagDetailAdapter.OnClickDetailListener() {
                @Override
                public void clickDetailCheck(int position, GiftBagEntity.giftEntity itemEntity, LinearLayout detail_layout) {
                    if(onClickDetailListener!=null){
                        onClickDetailListener.clickRcvDetailCheck(position,itemEntity,detail_layout,index);
                    }
                }
            });
            itemViewHolder.rcvDetail.setAdapter(giftBagDetailAdapter);
        }
    }

    @Override
    public int getItemCount() {
        if(itemData==null){
            return 0;
        }
        return itemData.size();
    }

    class GiftBagRcvHolder extends RecyclerView.ViewHolder {
        RecyclerView rcvDetail;

        private GiftBagRcvHolder(View itemView) {
            super(itemView);
            rcvDetail = itemView.findViewById(R.id.rcv_detail);
        }
    }

    public void setOnClickListener(OnClickRcvDetailListener onClickListener){
        this.onClickDetailListener = onClickListener;
    }
    public interface  OnClickRcvDetailListener{
        void clickRcvDetailCheck(int position,GiftBagEntity.giftEntity itemEntity,LinearLayout detail_layout,int rcvPosition);
    }
}
