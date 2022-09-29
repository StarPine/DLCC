package com.dl.playfun.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.playfun.R;
import com.dl.playfun.entity.ExchangeIntegraEntity;

import java.util.List;

import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.LayoutManagers;

/**
 * Author: 彭石林
 * Time: 2021/9/22 17:43
 * Description: 积分兑换弹窗
 */
public class TaskFukubukuroDialog {
    public static int SEL_EXCHANGE_POSTION = -1;
    /*
    * @Desc TODO(钻石兑换积分弹窗)
    * @author 彭石林
    * @parame [context, touchOutside, selected, dataList]
    * @return android.app.Dialog
    * @Date 2021/9/22
    */
    public static Dialog exchangeIntegralDialog(Context context, boolean touchOutside,String memoney,String meCoinValue,int selected, List<ExchangeIntegraEntity> dataList,ExchangeIntegraleClick exchangeIntegraleClick) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        //设置不能点击外部隐藏
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exchange_integral, null);

        TextView title =  contentView.findViewById(R.id.title);
        String titleText = StringUtils.getString(R.string.dialog_exchange_integral_content_2);
        Spannable span = new SpannableString(titleText);
        span.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.huise)), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(ColorUtils.getColor(R.color.purple1)), 7, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);

        TextView money = contentView.findViewById(R.id.money);
        money.setText(memoney);
        TextView coinValue = contentView.findViewById(R.id.coin_value);
        coinValue.setText(meCoinValue);


        RecyclerView exchange_rc = contentView.findViewById(R.id.exchange_rc);
        LayoutManagers.LayoutManagerFactory layoutManagerFactory = LayoutManagers.grid(2);
        exchange_rc.setLayoutManager(layoutManagerFactory.create(exchange_rc));
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "DIN-Bold.TTF");
        ExchangeIntegraItemAdapter adapter = new ExchangeIntegraItemAdapter(dataList,typeface);
        exchange_rc.setAdapter(adapter);

        adapter.setDefaultSelect(selected);
        SEL_EXCHANGE_POSTION = selected;
        adapter.setOnItemClickListener((v, index) -> {
            //选中
            SEL_EXCHANGE_POSTION = index;
            adapter.setDefaultSelect(SEL_EXCHANGE_POSTION);
        });

        Button btn_confirm = contentView.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SEL_EXCHANGE_POSTION==-1){
                    return;
                }
                exchangeIntegraleClick.clickSelectItem(dialog,dataList.get(SEL_EXCHANGE_POSTION));
            }
        });
        ImageView iv_close = contentView.findViewById(R.id.iv_dialog_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(contentView);
        return dialog;
    }
    public interface ExchangeIntegraleClick {
        void clickSelectItem(Dialog dialog, ExchangeIntegraEntity itemEntity);
    }


}
