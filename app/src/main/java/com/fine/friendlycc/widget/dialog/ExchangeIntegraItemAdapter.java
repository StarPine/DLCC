package com.fine.friendlycc.widget.dialog;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.entity.ExchangeIntegraEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/9/22 18:45
 * Description: This is ExchangeIntegraItemAdapter
 */
public class ExchangeIntegraItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public OnItemClickListener onItemClickListener;
    private int defaultItemSel = -1;
    private Typeface typeface = null;

    private List<ExchangeIntegraEntity> dataBeanList = null;

    public ExchangeIntegraItemAdapter(List<ExchangeIntegraEntity> list, Typeface typeface) {
        this.dataBeanList = list;
        this.typeface = typeface;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_exchange_dialog_item, parent, false);
        return new ExchangeIntegraItemViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemListener) {
        this.onItemClickListener = onItemListener;
    }

    public void setDefaultSelect(int position) {
        if(dataBeanList != null && dataBeanList.size() > position){
            this.defaultItemSel = position;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ExchangeIntegraItemViewHolder ItemViewHolder = ((ExchangeIntegraItemViewHolder) holder);
        if (!ObjectUtils.isEmpty(dataBeanList)) {
            ExchangeIntegraEntity itemEntity = dataBeanList.get(position);
            if(!ObjectUtils.isEmpty(itemEntity)){
                ItemViewHolder.title1.setText(itemEntity.getBonusName());
                ItemViewHolder.title2.setText(itemEntity.getCoinName());
                if(typeface!=null){
                    ItemViewHolder.title1.setTypeface(typeface);
                    ItemViewHolder.title2.setTypeface(typeface);
                }
                if(defaultItemSel!=-1){
                    if(defaultItemSel==position){
                        ItemViewHolder.rl_layout.setBackground(Utils.getApp().getDrawable(R.drawable.task_exchange_dialog_item_ed));
                        int whiteColor = ColorUtils.getColor(R.color.white);
                        ItemViewHolder.title1.setTextColor(whiteColor);
                        ItemViewHolder.title2.setTextColor(whiteColor);
                    }else{
                        ItemViewHolder.rl_layout.setBackground(Utils.getApp().getDrawable(R.drawable.task_exchange_dialog_item_no));
                        ItemViewHolder.title1.setTextColor(ColorUtils.getColor(R.color.black31));
                        ItemViewHolder.title2.setTextColor(ColorUtils.getColor(R.color.purple1));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataBeanList == null) {
            return 0;
        }
        return dataBeanList.size();
    }
    @Override//onCreateViewHolder(ViewGroup parent, int viewType)中的viewType参数由此获得
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onClick(View v, int index);
    }

    public class ExchangeIntegraItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_layout;
        TextView title1;
        TextView title2;

        public ExchangeIntegraItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_layout = itemView.findViewById(R.id.rl_layout);

            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);

            rl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
