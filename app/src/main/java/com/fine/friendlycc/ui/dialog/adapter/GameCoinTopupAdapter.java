package com.fine.friendlycc.ui.dialog.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.api.AppGameConfig;
import com.fine.friendlycc.entity.GameCoinBuy;
import com.fine.friendlycc.R;
import com.fine.friendlycc.manager.ConfigManager;

import java.util.List;

/**
 * @author wulei
 */
public class GameCoinTopupAdapter extends RecyclerView.Adapter<GameCoinTopupAdapter.RecyclerHolder> {

    private final Context mContext;
    public List<GameCoinBuy> dataList;

    private GameCoinTopupAdapterListener gameCoinTopupAdapterListener = null;

    public GameCoinTopupAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public GameCoinTopupAdapterListener getCoinRechargeAdapterListener() {
        return gameCoinTopupAdapterListener;
    }

    public void setCoinRechargeAdapterListener(GameCoinTopupAdapterListener gameCoinTopupAdapterListener) {
        this.gameCoinTopupAdapterListener = gameCoinTopupAdapterListener;
    }

    public void setData(List<GameCoinBuy> goodsList) {
        dataList = (goodsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_coin_topup, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if(holder.itemView.getTag()!=null){
            return;
        }
        GameCoinBuy goodsEntity = dataList.get(position);
        holder.tvGoodsLabel.setText(goodsEntity.getGoodsLabel());
        holder.tvGoodsName.setText(goodsEntity.getGoodsName());
        holder.tvPayPrice.setText(goodsEntity.getPayPrice());
        //设置游戏货币图标。根据用户传递
        AppGameConfig appGameConfig = ConfigManager.getInstance().getAppRepository().readGameConfigSetting();
        if(!ObjectUtils.isEmpty(appGameConfig)){
            if(appGameConfig.getGamePlayCoinBigImg()!=0){
                holder.gameBigImg.setImageResource(appGameConfig.getGamePlayCoinBigImg());
            }
        }
        if(goodsEntity.getIsFirst() != null && goodsEntity.getIsFirst() == 1){
            holder.tvGoodsLabel.setVisibility(View.VISIBLE);
            holder.imgFirst.setVisibility(View.VISIBLE);
        }else{
            holder.tvGoodsLabel.setVisibility(View.GONE);
            holder.imgFirst.setVisibility(View.GONE);
            if(!StringUtils.isEmpty(goodsEntity.getGoodsLabel())){
                holder.tvGoodsName.setText(goodsEntity.getGoodsLabel());
            }
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(v -> {
            if (gameCoinTopupAdapterListener != null) {
                int p = (int) v.getTag();
                gameCoinTopupAdapterListener.onBuyClick(v, p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public interface GameCoinTopupAdapterListener {
        void onBuyClick(View view, int position);
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvGoodsLabel = null;
        TextView tvGoodsName = null;
        TextView tvPayPrice = null;
        ImageView imgFirst = null;
        //游戏货币图标
        ImageView gameBigImg = null;

        private RecyclerHolder(View itemView) {
            super(itemView);
            tvGoodsLabel = itemView.findViewById(R.id.tv_goods_label);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvPayPrice = itemView.findViewById(R.id.tv_pay_price);
            imgFirst = itemView.findViewById(R.id.img_first);
            gameBigImg = itemView.findViewById(R.id.game_big_img);
            tvGoodsLabel.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}