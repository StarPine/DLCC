package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.bean.GoodsBean;
import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/9/23 18:39
 * Description: This is CoinExchargeIntegralAdapter
 */
public class CoinExchargeIntegralAdapter extends RecyclerView.Adapter<CoinExchargeIntegralAdapter.RecyclerHolder> {

    private final Context mContext;
    private List<GoodsBean> dataList = new ArrayList<>();

    private CoinExchargeIntegralAdapterListener coinExchargeIntegralAdapterListener = null;

    public CoinExchargeIntegralAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public CoinExchargeIntegralAdapterListener getCoinExchargeIntegralAdapterListener() {
        return coinExchargeIntegralAdapterListener;
    }

    public void setCoinExchargeIntegralAdapterListener(CoinExchargeIntegralAdapterListener coinRechargeAdapterListener) {
        this.coinExchargeIntegralAdapterListener = coinRechargeAdapterListener;
    }

    public void setData(List<GoodsBean> goodsList) {
        this.dataList = goodsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_coin_excharge_integral, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        GoodsBean goodsEntity = dataList.get(position);
        holder.tvName.setText(goodsEntity.getGoodsName());
        holder.tvPrice.setText(goodsEntity.getSymbol() + goodsEntity.getSalePrice());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinExchargeIntegralAdapterListener != null) {
                    int p = (int) v.getTag();
                    coinExchargeIntegralAdapterListener.onBuyClick(v, p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CoinExchargeIntegralAdapterListener {
        void onBuyClick(View view, int position);
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvName = null;
        TextView tvPrice = null;

        private RecyclerHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            Typeface typeface = Typeface.createFromAsset(CCApplication.instance().getAssets(), "DIN-Bold.TTF");
            tvName.setTypeface(typeface);
        }
    }
}