package com.dl.playfun.widget.custom;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dl.playfun.entity.EvaluateItemEntity;
import com.dl.playfun.R;

import java.util.List;

public class FlowAdapter extends FlowLayout.Adapter<FlowAdapter.FlowViewHolder> {

    private static final String TAG = "FlowAdapter";

    private final Context mContext;
    private final List<EvaluateItemEntity> mContentList;

    public FlowAdapter(Context mContext, List<EvaluateItemEntity> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @Override
    public FlowViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_user_detail_evaluation, parent, false);
        return new FlowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlowViewHolder holder, int position) {
        String tagText = getTag(mContentList.get(position));
        SpannableString spannableString = new SpannableString(tagText);
        final float fontScale = mContext.getResources().getSystem().getDisplayMetrics().scaledDensity;
        int textSize = (int) (9 * fontScale + 0.5f);
        spannableString.setSpan(new AbsoluteSizeSpan(textSize), tagText.indexOf("("), tagText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.content.setText(spannableString);
    }

    public String getTag(EvaluateItemEntity evaluateEntity) {
        String name = evaluateEntity.getName();
        int number = evaluateEntity.getNumber();
        if (number > 999) {
            return name + " (999+)";
        } else {
            return name + " (" + number + ")";
        }
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class FlowViewHolder extends FlowLayout.ViewHolder {
        TextView content;

        public FlowViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.text);
        }
    }
}
