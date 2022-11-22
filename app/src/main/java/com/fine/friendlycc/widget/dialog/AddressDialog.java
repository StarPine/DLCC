package com.fine.friendlycc.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fine.friendlycc.bean.AddressCityBean;
import com.fine.friendlycc.bean.AddressCityItemBean;
import com.fine.friendlycc.R;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/13 18:02
 * Description: 地址弹窗口
 */
public class AddressDialog {
    /**
     * 城市单选
     *
     * @param context
     * @param configEntities
     * @param ids
     * @param chooseCity
     * @return
     */
    public static Dialog getCityDialogs(Context context, List<AddressCityBean> configEntities, String ids, AddessCityChooseCity chooseCity) {
        for (int i = 0; i < configEntities.size(); i++) {
            //控件多次调用后原来值已经发生变化。不是上次选中的默认去掉
            configEntities.get(i).setIsChoose(ids != null && configEntities.get(i).getCity().equals(ids));
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_occupation, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (int) (context.getResources().getDisplayMetrics().heightPixels / 1.7);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
        TextView tv_titles = contentView.findViewById(R.id.tv_titles);
        tv_titles.setText(R.string.playcc_dialog_choose_city);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        AddressCityAdapter cityAdapter = new AddressCityAdapter(configEntities);
        rcvTiem.setAdapter(cityAdapter);
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        rcvTiem.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                chooseCity.clickListItem(bottomDialog, cityAdapter.getItem(i));
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 城市单选区间
     *
     * @param context
     * @param configEntities
     * @param ids
     * @param chooseCity
     * @return
     */
    public static Dialog getCityItemDialogs(Context context, List<AddressCityItemBean> configEntities, String ids, AddessCityItemChooseCity chooseCity) {
        for (int i = 0; i < configEntities.size(); i++) {
            //控件多次调用后原来值已经发生变化。不是上次选中的默认去掉
            configEntities.get(i).setIsChoose(ids != null && configEntities.get(i).getPostalCode().equals(ids));
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_occupation, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (int) (context.getResources().getDisplayMetrics().heightPixels / 1.7);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
        TextView tv_titles = contentView.findViewById(R.id.tv_titles);
        tv_titles.setText(R.string.playcc_dialog_choose_city);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        AddressCityItemAdapter cityAdapter = new AddressCityItemAdapter(configEntities);
        rcvTiem.setAdapter(cityAdapter);
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        rcvTiem.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                chooseCity.clickListItem(bottomDialog, cityAdapter.getItem(i));
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    public interface AddessCityChooseCity {
        void clickListItem(Dialog dialog, AddressCityBean address);
    }

    public interface AddessCityItemChooseCity {
        void clickListItem(Dialog dialog, AddressCityItemBean address);
    }
}