package com.fine.friendlycc.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.DialogCityChooseBinding;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.ui.base.BaseDialog;
import com.fine.friendlycc.ui.dialog.adapter.CityChooseAdapter;
import com.fine.friendlycc.widget.recyclerview.LineManagers;

import java.util.List;
import java.util.Objects;


/**
 * Author: 彭石林
 * Time: 2022/7/25 18:16
 * Description: This is CityChooseDialog
 */
public class CityChooseDialog extends BaseDialog implements View.OnClickListener {

    private final Context mContext;
    private final List<ConfigItemBean> citys;
    private RecyclerView recyclerView;
    private TextView btnConfirm;
    private TextView btnCancel;
    private ConfigItemBean currentChooseEntity;
    private CityChooseAdapter adapter;
    private CityChooseDialogListener cityChooseDialogListener;

    private DialogCityChooseBinding binding;

    private Integer currentChooseCityId;



    public CityChooseDialog(Context context,List<ConfigItemBean> citys,Integer cityId) {
        super(context);
        this.mContext = context;
        this.citys = citys;
        this.currentChooseCityId = cityId==null?-1:cityId;
        initView();
    }

    public void setCityChooseDialogListener(CityChooseDialogListener cityChooseDialogListener) {
        this.cityChooseDialogListener = cityChooseDialogListener;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_city_choose, null, false);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        recyclerView = binding.recyclerView;
        btnConfirm = binding.btnConfirm;
        btnCancel = binding.btnCancel;
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.addItemDecoration(LineManagers.horizontal().create(recyclerView));
        adapter = new CityChooseAdapter(recyclerView,currentChooseCityId);
        adapter.setData(this.citys);
        recyclerView.setAdapter(adapter);

        adapter.setCityChooseAdapterListener((itemEntity,position) -> {
            currentChooseEntity = itemEntity;
            if(Objects.equals(currentChooseCityId, currentChooseEntity.getId())){
                return;
            }
            currentChooseCityId = currentChooseEntity.getId();
            adapter.setCurrentChooseCityId(currentChooseCityId);
        });
    }

    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        super.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm) {
            if (cityChooseDialogListener != null) {
                cityChooseDialogListener.onItemClick(this,currentChooseEntity);
            }
        } else if (view.getId() == R.id.btn_cancel) {
            this.dismiss();
        }
    }

    public interface CityChooseDialogListener {
        void onItemClick(CityChooseDialog dialog, ConfigItemBean itemEntity);
    }
}