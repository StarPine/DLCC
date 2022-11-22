package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/9 14:11
 * Description: This is GiftBagCardDetailAdapter
 */
public class GiftBagCardDetailAdapter extends RecyclerView.Adapter<GiftBagCardDetailAdapter.ItemViewHolder> {

    private final Context mContext;
    private final List<GiftBagBean.propEntity> listData;

    private OnClickDetailListener onClickDetailListener = null;
    private boolean isDarkShow = false;

    //道具卡类型 1啪啪卡 2聊天卡 3语音卡 4視頻卡
    private int propType = 0;

    public GiftBagCardDetailAdapter(RecyclerView recyclerView, List<GiftBagBean.propEntity> data, boolean isDarkShow, int prop_type) {
        this.mContext = recyclerView.getContext();
        this.listData = data;
        this.isDarkShow = isDarkShow;
        this.propType = prop_type;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_bag_card_item_rcv, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = holder;
        if(listData!=null && listData.size() > 0) {
            int index = position;
            GiftBagBean.propEntity propEntity = listData.get(index);
            if (isDarkShow) {
                itemViewHolder.title.setTextColor(Color.parseColor("#F1F2F9"));
            } else {
                itemViewHolder.title.setTextColor(Color.parseColor("#333333"));
            }
            String totalText = null;
            itemViewHolder.title.setText(String.valueOf(propEntity.getName()));
            if (propType == 3) {
                totalText = String.valueOf(propEntity.getTotal().intValue() - 1);
            } else if (propType == 4) {
                totalText = String.valueOf(propEntity.getTotal().intValue() - 1);
            } else {
                totalText = String.valueOf(propEntity.getTotal().intValue());
            }
            itemViewHolder.number_text.setText(totalText);
            Glide.with(TUIChatService.getAppContext()).load(StringUtil.getFullImageUrl(propEntity.getIcon()))
                    .error(R.drawable.default_avatar)
                    .placeholder(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.icon_url);
            itemViewHolder.detail_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickDetailListener != null) {
                        onClickDetailListener.clickDetailCheck(index, propEntity, itemViewHolder.detail_layout);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(listData==null){
            return 0;
        }
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_url;
        TextView title;
        TextView number_text;
        LinearLayout detail_layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            detail_layout = itemView.findViewById(R.id.detail_layout);
            icon_url = itemView.findViewById(R.id.icon_url);
            number_text = itemView.findViewById(R.id.number_text);
            title = itemView.findViewById(R.id.title);
        }
    }
    public void setOnClickListener(OnClickDetailListener onClickListener){
        this.onClickDetailListener = onClickListener;
    }

    public interface  OnClickDetailListener{
        void clickDetailCheck(int position,GiftBagBean.propEntity itemEntity,LinearLayout detail_layout);
    }
}