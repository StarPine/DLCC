package com.fine.friendlycc.ui.dialog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.bean.GoodsBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class CoinRechargeAdapter extends RecyclerView.Adapter<CoinRechargeAdapter.RecyclerHolder> {

    private final Context mContext;
    private List<GoodsBean> dataList = new ArrayList<>();

    private CoinRechargeAdapterListener coinRechargeAdapterListener = null;

    public CoinRechargeAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public CoinRechargeAdapterListener getCoinRechargeAdapterListener() {
        return coinRechargeAdapterListener;
    }

    public void setCoinRechargeAdapterListener(CoinRechargeAdapterListener coinRechargeAdapterListener) {
        this.coinRechargeAdapterListener = coinRechargeAdapterListener;
    }

    public void setData(List<GoodsBean> goodsList) {
        this.dataList = goodsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_diamond_recharge, parent, false);
        return new RecyclerHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        GoodsBean goodsEntity = dataList.get(position);
        holder.good_text.setText(goodsEntity.getGiveCoinTitle());
        holder.good_name.setText(goodsEntity.getSymbol() + goodsEntity.getSalePrice());
        //是否vip
        if (goodsEntity.getType() == 2) {
            holder.layout1.setBackgroundResource(R.drawable.bg_diamond_recharge_item_vip);
            holder.good_text.setTextColor(ColorUtils.getColor(R.color.white));
            holder.good_name.setTextColor(ColorUtils.getColor(R.color.yellow_544));
        } else if (goodsEntity.getType() == 1){
            if (goodsEntity.getIsRecommend() == 1){
                holder.layout1.setBackgroundResource(R.drawable.bg_diamond_recharge_item_recommend);
            }else {
                holder.layout1.setBackgroundResource(R.drawable.bg_diamond_recharge_item_nomal);
            }
            holder.good_text.setTextColor(ColorUtils.getColor(R.color.toolbar_title_color));
            holder.good_name.setTextColor(ColorUtils.getColor(R.color.pseekbar_process));
        }
        setTipsBg(holder, goodsEntity);
        if (goodsEntity.getType() == 1) {
            if (goodsEntity.getLimit() == 1) { //是否首冲
                if (goodsEntity.getIsRecommend() == 1){
                    holder.flag.setBackgroundResource(R.drawable.bg_right_top_corner2);
                    holder.flag.setTextColor(ColorUtils.getColor(R.color.white));
                }else {
                    holder.flag.setTextColor(ColorUtils.getColor(R.color.toolbar_title_color));
                    holder.flag.setBackgroundResource(R.drawable.bg_right_top_corner);
                }
                holder.flag.setText(mContext.getString(R.string.playcc_diamond_detail_item_flag));
                holder.flag.setVisibility(View.VISIBLE);
            } else {
                holder.flag.setVisibility(View.GONE);
            }
        } else if (goodsEntity.getType() == 2) {
            holder.flag.setTextColor(ColorUtils.getColor(R.color.toolbar_title_color));
            holder.flag.setBackgroundResource(R.drawable.bg_right_top_corner);
            holder.flag.setText(mContext.getString(R.string.playcc_diamond_detail_item_flag2));
            holder.flag.setVisibility(View.VISIBLE);
        }else {
            holder.flag.setVisibility(View.GONE);
        }

        if (goodsEntity.getSelected()){
            holder.isChecked.setVisibility(View.VISIBLE);
        }else {
            holder.isChecked.setVisibility(View.GONE);
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(v -> {
            if (coinRechargeAdapterListener != null) {
                int p = (int) v.getTag();
                coinRechargeAdapterListener.itemViewOnClick(v, p);
            }
        });
    }

    private void setTipsBg(@NotNull RecyclerHolder holder, GoodsBean goodsEntity) {
        if (!ObjectUtils.isEmpty(goodsEntity.getGoodsDesc())) {
            holder.recharge_tips.setText(goodsEntity.getGoodsDesc());
            holder.recharge_tips.setVisibility(View.VISIBLE);
            if (goodsEntity.getType() == 1){
                if (goodsEntity.getIsRecommend() == 1){
                    holder.recharge_tips.setBackgroundResource(R.drawable.bg_diamond_recharge_item_tips_recommend);
                    return;
                }
            }
            holder.recharge_tips.setBackgroundResource(R.drawable.bg_diamond_recharge_item_child);
        } else {
            holder.recharge_tips.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CoinRechargeAdapterListener {
        void itemViewOnClick(View view, int position);
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout1;
        TextView flag;
        TextView good_text;
        TextView good_name;
        TextView recharge_tips;
        View isChecked;

        private RecyclerHolder(View itemView) {
            super(itemView);
            layout1 = itemView.findViewById(R.id.rl_layout);
            flag = itemView.findViewById(R.id.tv_flag);
            good_text = itemView.findViewById(R.id.tv_total_revenue);
            good_name = itemView.findViewById(R.id.tv_price);
            recharge_tips = itemView.findViewById(R.id.tv_recharge_tips);
            isChecked = itemView.findViewById(R.id.is_checked);
        }
    }
}