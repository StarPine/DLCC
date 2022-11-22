package com.fine.friendlycc.ui.message.chatdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/10/23 19:14
 * Description: This is ChatDetailEvaluateAdapter
 */
public class ChatDetailEvaluateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public OnItemClickListener onItemListener;
    List<EvaluateItemBean> dataBeanList = new ArrayList<>();
    private int defItem = -1;

    public ChatDetailEvaluateAdapter(List<EvaluateItemBean> list) {
        dataBeanList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat__detail_evaluate, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        if (dataBeanList != null && dataBeanList.size() > 0) {
            if (defItem == position) {
                itemViewHolder.evaluation_tag.setTextColor(CCApplication.instance().getResources().getColor(R.color.white));
                itemViewHolder.evaluation_tag.setBackgroundResource(R.drawable.radius_purple_ed);
            } else {
                itemViewHolder.evaluation_tag.setTextColor(CCApplication.instance().getResources().getColor(R.color.purple1));
                itemViewHolder.evaluation_tag.setBackgroundResource(R.drawable.radius_purple);
            }
            itemViewHolder.evaluation_tag.setText(dataBeanList.get(position).getName());
        }
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override//onCreateViewHolder(ViewGroup parent, int viewType)中的viewType参数由此获得
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        if (dataBeanList == null) {
            return 0;
        }
        return dataBeanList.size();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View v, int pos, EvaluateItemBean itemEntity);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView evaluation_tag;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            evaluation_tag = itemView.findViewById(R.id.evaluation_tag);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClick(v, getLayoutPosition(), dataBeanList.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
}