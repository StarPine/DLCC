package com.fine.friendlycc.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.OccupationConfigItemEntity;
import com.fine.friendlycc.utils.ListUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.R;
import com.fine.friendlycc.widget.NineGridLockView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class MVDialog {
    private static volatile MVDialog INSTANCE;
    private Context context;
    private TypeEnum CHOOSRTYPE;
    private float textSize = 0;
    private float confirmTextSize = 0;
    private String titleString = "";
    private String contentString = "";
    private String confirmText = "";
    private String confirmTwoText = "";
    private List<String> stringList = new ArrayList<>();
    private List<Integer> closePosion;
    private Dialog dialog;
    private ConfirmOnclick confirmOnclick;
    private ConfirmTwoOnclick confirmTwoOnclick;
    private ConfirmMoneyOnclick confirmMoneyOnclick;
    private ClickList clickList;
    private ConfirmComment confirmComment;
    private boolean isCancelable = false;//弹出后会点击屏幕或物理返回键
    private boolean isNotClose = false;//不要警告dialog的关闭按钮
    private OnDismissListener onDismissListener;

    private MVDialog(Context context) {
        this.context = context;
    }

    public static MVDialog getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MVDialog.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MVDialog(context);
                }
            }
        } else {
            init(context);
        }
        return INSTANCE;
    }

    /**
     * 从新初始化值
     *
     * @param context
     */
    private static void init(Context context) {
        INSTANCE.context = context;
        INSTANCE.titleString = "";
        INSTANCE.contentString = "";
        INSTANCE.confirmText = "";
        INSTANCE.confirmTwoText = "";
        INSTANCE.stringList = new ArrayList<>();
        INSTANCE.dialog = null;
        INSTANCE.confirmOnclick = null;
        INSTANCE.confirmTwoOnclick = null;
        INSTANCE.confirmMoneyOnclick = null;
        INSTANCE.clickList = null;
        INSTANCE.isCancelable = false;
        INSTANCE.isNotClose = false;
        INSTANCE.onDismissListener = null;
    }

    /**
     * 获取职业dialog
     *
     * @return
     */
    public static Dialog getOccupationDialog(Context context, List<OccupationConfigItemEntity> configEntities, int id, ChooseOccupation chooseOccupation) {
        int itemPosion = 0;
        for (int i = 0; i < configEntities.size(); i++) {
            for (OccupationConfigItemEntity.ItemEntity item : configEntities.get(i).getItem()) {
                if (item.getId() == id) {
                    configEntities.get(i).setChoose(true);
                    item.setChoose(true);
                    itemPosion = i;
                }
            }
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_occupationcs, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (context.getResources().getDisplayMetrics().heightPixels / 3);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView rcvParent = contentView.findViewById(R.id.rcv_parent);
        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        rcvParent.setLayoutManager(new LinearLayoutManager(context));
        OccputonParentAdapter occputonParentAdapter = new OccputonParentAdapter(configEntities);
        OccputonItemAdapter occputonItemAdapter = new OccputonItemAdapter(configEntities.get(itemPosion).getItem());
        rcvTiem.setAdapter(occputonItemAdapter);
        rcvParent.setAdapter(occputonParentAdapter);
        rcvParent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                for (int j = 0; j < occputonParentAdapter.getData().size(); j++) {
                    if (i != j) {
                        occputonParentAdapter.getItem(j).setChoose(false);
                    }
                }
                occputonParentAdapter.getItem(i).setChoose(true);
                occputonParentAdapter.notifyDataSetChanged();
                occputonItemAdapter.setNewData(occputonParentAdapter.getItem(i).getItem());
                occputonItemAdapter.notifyDataSetChanged();
            }
        });
        rcvTiem.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                for (int j = 0; j < configEntities.size(); j++) {
                    for (OccupationConfigItemEntity.ItemEntity item : configEntities.get(j).getItem()) {
                        item.setChoose(false);
                    }
                }
                occputonItemAdapter.getItem(i).setChoose(true);
                occputonItemAdapter.notifyDataSetChanged();
                chooseOccupation.clickListItem(bottomDialog, occputonItemAdapter.getItem(i));
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 城市单选
     *
     * @param context
     * @param configEntities
     * @param ids
     * @param chooseCity
     * @return
     */
    public static Dialog getCityDialog(Context context, List<ConfigItemEntity> configEntities, Integer ids, raDioChooseCity chooseCity) {
        for (int i = 0; i < configEntities.size(); i++) {
            //控件多次调用后原来值已经发生变化。不是上次选中的默认去掉
            configEntities.get(i).setIsChoose(ids != null && configEntities.get(i).getId().intValue() == ids.intValue());
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_occupation, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (context.getResources().getDisplayMetrics().heightPixels / 3);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
        TextView tv_titles = contentView.findViewById(R.id.tv_titles);
        tv_titles.setText(R.string.playfun_address);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        CityAdapter cityAdapter = new CityAdapter(configEntities);
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
                cityAdapter.getItem(i).setIsChoose(true);
                cityAdapter.notifyDataSetChanged();
                chooseCity.clickListItem(bottomDialog, cityAdapter.getItem(i));
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 城市单选
     *
     * @param context
     * @param configEntities
     * @param ids
     * @param chooseCity
     * @return
     */
    public static Dialog getCityDialogs(Context context, List<ConfigItemEntity> configEntities, Integer ids, raDioChooseCity chooseCity) {
        for (int i = 0; i < configEntities.size(); i++) {
            //控件多次调用后原来值已经发生变化。不是上次选中的默认去掉
            configEntities.get(i).setIsChoose(ids != null && configEntities.get(i).getId().intValue() == ids.intValue());
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
        tv_titles.setText(R.string.playfun_address);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        CityAdapter cityAdapter = new CityAdapter(configEntities);
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
                cityAdapter.getItem(i).setIsChoose(true);
                cityAdapter.notifyDataSetChanged();
                chooseCity.clickListItem(bottomDialog, cityAdapter.getItem(i));
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 获取城市dialog
     *
     * @return
     */
    public static Dialog getCityDialog(Context context, List<ConfigItemEntity> configEntities, List<Integer> ids, ChooseCity chooseCity) {
        for (int i = 0; i < configEntities.size(); i++) {
            configEntities.get(i).setIsChoose(false);
            if (!ListUtils.isEmpty(ids)) {
                for (Integer id : ids) {
                    if (configEntities.get(i).getId().intValue() == id.intValue()) {
                        configEntities.get(i).setIsChoose(true);
                        break;
                    }
                }
            }
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_city, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (context.getResources().getDisplayMetrics().heightPixels / 3);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
        CityAdapter cityAdapter = new CityAdapter(configEntities);
        rcvTiem.setAdapter(cityAdapter);
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> id = new ArrayList<>();
                for (ConfigItemEntity config : configEntities) {
                    if (config.getIsChoose()) {
                        id.add(config.getId());
                    }
                }
                chooseCity.clickListItem(bottomDialog, id);
                bottomDialog.dismiss();
            }
        });
        rcvTiem.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (cityAdapter.getItem(i).getIsChoose()) {
                    cityAdapter.getItem(i).setIsChoose(false);
                    if (ids != null) {
                        for (int j = 0; j < ids.size(); j++) {
                            if (cityAdapter.getItem(i).getId() == ids.get(j)) {
                            }
                        }
                    }
                } else {
                    cityAdapter.getItem(i).setIsChoose(true);
                }
                cityAdapter.notifyDataSetChanged();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 获取手势解锁dialog
     *
     * @return
     */
    public static Dialog getLockDialog(Activity context, LockOnclick lockOnclick) {
        final int[] dayStr = new int[3];
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_lock, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.MyDialog);
        bottomDialog.setCanceledOnTouchOutside(false);
        bottomDialog.setCancelable(false);
        NineGridLockView lockView = contentView.findViewById(R.id.lock);
        ImageView ivContent = contentView.findViewById(R.id.iv_content);
        TextView tvExpain = contentView.findViewById(R.id.tv_expain);

        View mainContainerView = context.getWindow().getDecorView();
        mainContainerView.setDrawingCacheEnabled(true);
        mainContainerView.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(mainContainerView.getDrawingCache(), 0, 0, mainContainerView.getMeasuredWidth(),
                mainContainerView.getMeasuredHeight());
        Blurry.with(context)
                .radius(5)//模糊半径
                .sampling(8)//缩放大小，先缩小再放大
                .color(Color.argb(78, 255, 255, 255))//颜色
                .async()//是否异步
                .animate(300)
                .from(bp)//传入bitmap
                .into(ivContent);//显示View
        lockView.setBack(password -> {
            if (lockOnclick != null) {
                lockOnclick.checkinfpassword(bottomDialog, password, tvExpain);
            }
        });
        contentView.findViewById(R.id.tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lockOnclick != null) {
                    lockOnclick.logout(bottomDialog);
                }
            }
        });
        return bottomDialog;
    }

    /**
     * 获取节目时长
     *
     * @return
     */
    public static Dialog getEndTimeDialog(Context context, List<ConfigItemEntity> data, Integer id, ChooseEndTime chooseEndTime) {
        for (ConfigItemEntity c : data) {
            c.setIsChoose(id != null && c.getId().intValue() == id.intValue());
        }
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_buttom, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView recyclerView = contentView.findViewById(R.id.rcv_tiem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CityAdapter dialogAdapter = new CityAdapter(data);
        recyclerView.setAdapter(dialogAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                chooseEndTime.clickListItem(bottomDialog, dialogAdapter.getItem(i).getId());
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    public static Dialog AlertInviteWrite(Context context, boolean touchOutside, DilodAlertMessageInterface dilodAlertMessageInterface) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_invite_wite, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        EditText value = contentView.findViewById(R.id.value);

        value.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        Button button = contentView.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertMessageInterface.confirm(dialog, DialogInterface.BUTTON_POSITIVE, 0, value.getText().toString());
            }
        });
        return dialog;
    }

    /**
     * 最后第二步才选项弹框样式
     *
     * @return
     */
    public MVDialog chooseType(TypeEnum typeEnum) {
        this.CHOOSRTYPE = typeEnum;
        if (typeEnum == TypeEnum.BOTTOMLIST) {
            this.dialog = getBottomListDialog();
        } else if (typeEnum == TypeEnum.CENTENTLIST) {

        } else if (typeEnum == TypeEnum.CENTER) {
            this.dialog = getcenterDialog();
        } else if (typeEnum == TypeEnum.CENTERWARNED) {
            this.dialog = getcenterWarnedDialog();
        } else if (typeEnum == TypeEnum.BOTTOMCOMMENT) {
            this.dialog = getCommentDialog();
        } else if (typeEnum == TypeEnum.SET_MONEY) {
            this.dialog = getSetMoneyDialog();
        }
        return INSTANCE;
    }

    /**
     * 显示弹框
     */
    public void show() {
        if (this.CHOOSRTYPE == TypeEnum.BOTTOMLIST) {
            this.dialog.show();
        } else if (this.CHOOSRTYPE == TypeEnum.CENTENTLIST) {

        } else if (this.CHOOSRTYPE == TypeEnum.CENTER) {
            this.dialog.show();
        } else if (this.CHOOSRTYPE == TypeEnum.CENTERWARNED) {
            this.dialog.show();
        } else if (this.CHOOSRTYPE == TypeEnum.BOTTOMCOMMENT) {
            this.dialog.show();
        } else if (this.CHOOSRTYPE == TypeEnum.SET_MONEY) {
            this.dialog.show();
        }
    }

    public boolean isShowing() {
        if (this.dialog != null) {
            boolean isS = this.dialog.isShowing();
            return this.dialog.isShowing();
        }
        return false;
    }

    /**
     * 取消弹框
     */
    public void dismiss() {
        if (this.CHOOSRTYPE == TypeEnum.BOTTOMLIST) {
            this.dialog.dismiss();
        } else if (this.CHOOSRTYPE == TypeEnum.CENTENTLIST) {

        } else if (this.CHOOSRTYPE == TypeEnum.CENTER) {
            this.dialog.dismiss();
        } else if (this.CHOOSRTYPE == TypeEnum.CENTERWARNED) {
            this.dialog.dismiss();
        } else if (this.CHOOSRTYPE == TypeEnum.BOTTOMCOMMENT) {
            this.dialog.dismiss();
        } else if (this.CHOOSRTYPE == TypeEnum.SET_MONEY) {
            this.dialog.dismiss();
        }

    }

    /**
     * 点击外部关闭监听
     */
    public MVDialog setOnDismiss(OnDismissListener onDismiss) {
        this.onDismissListener = onDismiss;
        return INSTANCE;
    }

    /**
     * 设置确认按钮点击
     *
     * @param confirmOnclick
     * @return
     */
    public MVDialog setConfirmOnlick(ConfirmOnclick confirmOnclick) {
        this.confirmOnclick = confirmOnclick;
        return INSTANCE;
    }

    /**
     * 设置第二个确认按钮点击
     *
     * @param confirmTwoOnclick
     * @return
     */
    public MVDialog setConfirmTwoOnclick(ConfirmTwoOnclick confirmTwoOnclick) {
        this.confirmTwoOnclick = confirmTwoOnclick;
        return INSTANCE;
    }

    public MVDialog setConfirmMoneyOnclick(ConfirmMoneyOnclick confirmMoneyOnclick) {
        this.confirmMoneyOnclick = confirmMoneyOnclick;
        return INSTANCE;
    }

    /**
     * 设置list点击
     *
     * @param clickList
     * @return
     */
    public MVDialog setClickList(ClickList clickList) {
        this.clickList = clickList;
        return INSTANCE;
    }

    /**
     * 设置评论回调
     *
     * @param confirmComment
     * @return
     */
    public MVDialog seCommentConfirm(ConfirmComment confirmComment) {
        this.confirmComment = confirmComment;
        return INSTANCE;
    }

    /**
     * 设置
     *
     * @param titleString
     * @return
     */
    public MVDialog setTitele(String titleString) {
        this.titleString = titleString;
        return INSTANCE;
    }

    /**
     * 获取职业dialog
     *
     * @return
     */
//    public static Dialog getOccupationDialog(Context context, List<OccupationConfigItemEntity> configEntities, int id, ChooseOccupation chooseOccupation) {
//        for (int i = 0; i < configEntities.size(); i++) {
//            if (configEntities.get(i).getId() == id) {
//                configEntities.get(i).setChoose(true);
//            }else{//控件多次调用后原来值已经发生变化。不是上次选中的默认去掉
//                configEntities.get(i).setChoose(false);
//            }
//        }
//        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
//        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_occupation, null);
//        bottomDialog.setContentView(contentView);
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
//        layoutParams.height = context.getResources().getDisplayMetrics().heightPixels - (context.getResources().getDisplayMetrics().heightPixels / 3);
//        contentView.setLayoutParams(layoutParams);
//        contentView.offsetLeftAndRight(100);
//        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
//        RecyclerView rcvTiem = contentView.findViewById(R.id.rcv_tiem);
//        rcvTiem.setLayoutManager(new LinearLayoutManager(context));
//        OccputonParentAdapter cityAdapter = new OccputonParentAdapter(configEntities);
//        rcvTiem.setAdapter(cityAdapter);
//        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomDialog.dismiss();
//            }
//        });
//        rcvTiem.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                cityAdapter.getItem(i).setChoose(true);
//                cityAdapter.notifyDataSetChanged();
//                chooseOccupation.clickListItem(bottomDialog, cityAdapter.getItem(i));
//                bottomDialog.dismiss();
//            }
//        });
//        bottomDialog.show();
//        return bottomDialog;
//    }

    /**
     * 设置内容
     *
     * @param contentString
     * @return
     */
    public MVDialog setContent(String contentString) {
        this.contentString = contentString;
        return INSTANCE;
    }

    /**
     * 设置确定按钮文案
     *
     * @param confirmText
     * @return
     */
    public MVDialog setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        return INSTANCE;
    }

    /**
     * 设置第二个按钮文案
     *
     * @param confirmTwoText
     * @return
     */
    public MVDialog setConfirmTwoText(String confirmTwoText) {
        this.confirmTwoText = confirmTwoText;
        return INSTANCE;
    }

    /**
     * 设置list Data
     *
     * @param stringList
     * @return
     */
    public MVDialog setListData(List<String> stringList) {
        this.stringList = stringList;
        return INSTANCE;
    }

    /**
     * 设置list 有颜色位置
     *
     * @param closePosion
     * @return
     */
    public MVDialog setColorsPosion(List<Integer> closePosion) {
        this.closePosion = closePosion;
        return INSTANCE;
    }

    /**
     * 设置dialog是否点击外部取消
     *
     * @param cancelable
     * @return
     */
    public MVDialog setCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        return INSTANCE;
    }

    /**
     * 设置警告dialog不要关闭按钮
     *
     * @param notClose
     * @return
     */
    public MVDialog setNotClose(boolean notClose) {
        this.isNotClose = notClose;
        return INSTANCE;
    }

    /**
     * 获取底部dialog
     *
     * @return
     */
    private Dialog getBottomListDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_buttom, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        RecyclerView recyclerView = contentView.findViewById(R.id.rcv_tiem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DialogAdapter dialogAdapter = new DialogAdapter(stringList, closePosion);
        recyclerView.setAdapter(dialogAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                clickList.clickListItem(INSTANCE, i);
            }
        });
        return bottomDialog;
    }

    /**
     * 获取底部评论dialog
     *
     * @return
     */
    private Dialog getCommentDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_buttom_comment, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        contentView.offsetLeftAndRight(100);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        EditText editText = contentView.findViewById(R.id.et_comment);
        TextView tvSize = contentView.findViewById(R.id.tv_size);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvSize.setText(editText.getText().toString().length() + "/120");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        Button confirm = contentView.findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmComment != null) {
                    confirmComment.clickListItem(dialog, editText.getText().toString().trim());
                }
            }
        });
        return bottomDialog;
    }

    public MVDialog setTextSize(float textSize){
        this.textSize = textSize;
        return INSTANCE;
    }

    public MVDialog setConfirmTextSize(float textSize){
        this.confirmTextSize = textSize;
        return INSTANCE;
    }

    public MVDialog setTitleSize(float textSize){
        this.textSize = textSize;
        return INSTANCE;
    }
    /**
     * 设置
     *
     * @param titleString
     * @return
     */
    public MVDialog setTitle(String titleString) {
        this.titleString = titleString;
        return INSTANCE;
    }
    /**
     * 获取
     *
     * @return
     */
    private Dialog getcenterDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_title, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView content = contentView.findViewById(R.id.tv_content);
        Button contentBtn = contentView.findViewById(R.id.btn_confirm);
        Button contentTowBtn = contentView.findViewById(R.id.btn_confirm_two);
        if (textSize != 0){
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        }
        if (confirmTextSize != 0){
            contentBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,confirmTextSize);
        }

        if (StringUtils.isEmpty(titleString)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(titleString);
        }
        if (StringUtils.isEmpty(contentString)) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(contentString);
        }
        if (isNotClose) {
            contentView.findViewById(R.id.iv_dialog_close).setVisibility(View.GONE);
        }
        bottomDialog.setCanceledOnTouchOutside(isCancelable);
        bottomDialog.setCancelable(isCancelable);
        contentView.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(bottomDialog);
                }
            }
        });
        if (StringUtil.isEmpty(confirmText)) {
            contentBtn.setVisibility(View.VISIBLE);
            contentBtn.setText(context.getResources().getString(R.string.playfun_confirm));
        } else {
            contentBtn.setText(confirmText);
            contentBtn.setVisibility(View.VISIBLE);
        }
        if (StringUtil.isEmpty(confirmTwoText)) {
            contentTowBtn.setText(context.getResources().getString(R.string.playfun_confirm));
            contentTowBtn.setVisibility(View.GONE);
        } else {
            contentTowBtn.setText(confirmTwoText);
            contentTowBtn.setVisibility(View.VISIBLE);
        }
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(INSTANCE);
                }
                bottomDialog.dismiss();
            }
        });
        contentTowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmTwoOnclick != null) {
                    confirmTwoOnclick.confirm(INSTANCE);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    public Dialog getTop2BottomDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_top_to_bottom, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView content = contentView.findViewById(R.id.tv_content);
        Button contentBtn = contentView.findViewById(R.id.btn_confirm);
        Button contentTowBtn = contentView.findViewById(R.id.btn_confirm_two);
        if (textSize != 0){
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        }
        if (confirmTextSize != 0){
            contentBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,confirmTextSize);
        }

        if (StringUtils.isEmpty(titleString)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(titleString);
        }
        if (StringUtils.isEmpty(contentString)) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(contentString);
        }
        if (isNotClose) {
            contentView.findViewById(R.id.iv_dialog_close).setVisibility(View.GONE);
        }
        bottomDialog.setCanceledOnTouchOutside(isCancelable);
        bottomDialog.setCancelable(isCancelable);
        contentView.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(bottomDialog);
                }
            }
        });
        if (StringUtil.isEmpty(confirmText)) {
            contentBtn.setVisibility(View.VISIBLE);
            contentBtn.setText(context.getResources().getString(R.string.playfun_confirm));
        } else {
            contentBtn.setText(confirmText);
            contentBtn.setVisibility(View.VISIBLE);
        }
        if (StringUtil.isEmpty(confirmTwoText)) {
            contentTowBtn.setText(context.getResources().getString(R.string.playfun_confirm));
            contentTowBtn.setVisibility(View.GONE);
        } else {
            contentTowBtn.setText(confirmTwoText);
            contentTowBtn.setVisibility(View.VISIBLE);
        }
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(INSTANCE);
                }
                bottomDialog.dismiss();
            }
        });
        contentTowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmTwoOnclick != null) {
                    confirmTwoOnclick.confirm(INSTANCE);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    /**
     * 设置价格Dialog
     *
     * @return
     */
    private Dialog getSetMoneyDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_set_money, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        EditText edtMoney = contentView.findViewById(R.id.edt_money);
        Button contentBtn = contentView.findViewById(R.id.btn_confirm);
        if (StringUtils.isEmpty(titleString)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(titleString);
        }
        contentView.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        if (StringUtil.isEmpty(confirmText)) {
            contentBtn.setText(context.getResources().getString(R.string.playfun_confirm));
        } else {
            contentBtn.setText(confirmText);
        }
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = edtMoney.getText().toString();
                if (confirmMoneyOnclick != null) {
                    confirmMoneyOnclick.confirm(INSTANCE, money);
                }
            }
        });
        bottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                edtMoney.clearFocus();
                edtMoney.setFocusable(false);
                onDismissListener.onDismiss(bottomDialog);
            }
        });

        return bottomDialog;
    }

    private Dialog getcenterWarnedDialog() {
        final Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_sex_warn, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        if (isCancelable) {
            bottomDialog.setCancelable(false);
        }

        TextView title = contentView.findViewById(R.id.tv_title);
        TextView content = contentView.findViewById(R.id.tv_content);
        if (StringUtils.isEmpty(titleString)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(titleString);
        }
        content.setText(contentString);
        if (isNotClose) {
            contentView.findViewById(R.id.iv_dialog_close).setVisibility(View.GONE);
        }
        contentView.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        Button contentBtn = contentView.findViewById(R.id.btn_confirm);
        if (StringUtils.isEmpty(confirmText)) {
            contentBtn.setText(context.getResources().getString(R.string.playfun_confirm));
        } else {
            contentBtn.setText(confirmText);
        }
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(INSTANCE);
                }
                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
        return bottomDialog;
    }

    /**
     * 样式类型
     * 再设置其他数据和文案后在选择这个样式，然后调用
     */

    public enum TypeEnum {
        BOTTOMLIST,//底部列表
        CENTENTLIST,//中部列表
        CENTER,//中部提示
        CENTERWARNED,//中部警告
        BOTTOMCOMMENT,//底部评论列表
        //        BOTTOMLIST,//底部列表
        SET_MONEY//设置价格
    }

    public interface DilodAlertMessageInterface {
        // 确认操作
        void confirm(DialogInterface dialog, int which, int sel_Index, String swiftMessageEntity);

        // 取消操作
        void cancel(DialogInterface dialog, int which);
    }


    public interface OnDismissListener {
        void onDismiss(Dialog dialog);
    }

    public interface ConfirmOnclick {
        void confirm(MVDialog dialog);
    }

    public interface ConfirmTwoOnclick {
        void confirm(MVDialog dialog);
    }

    public interface ClickList {
        void clickListItem(MVDialog dialog, int posion);
    }

    public interface ChooseOccupation {
        //void clickListItem(Dialog dialog, OccupationConfigItemEntity item);
        void clickListItem(Dialog dialog, OccupationConfigItemEntity.ItemEntity item);
    }

    public interface ChooseOccupations {
        void clickListItem(Dialog dialog, Integer ids);
    }

    public interface ChooseCity {
        void clickListItem(Dialog dialog, List<Integer> ids);
    }

    public interface raDioChooseCity {
        void clickListItem(Dialog dialog, ConfigItemEntity ids);
    }

    public interface ChooseDay {
        void clickListItem(Dialog dialog, String day);
    }

    public interface ChooseEndTime {
        void clickListItem(Dialog dialog, int id);
    }

    public interface ConfirmComment {
        void clickListItem(Dialog dialog, String comment);
    }

    public interface ConfirmMoneyOnclick {
        void confirm(MVDialog dialog, String money);
    }

    public interface LockOnclick {
        void logout(Dialog dialog);

        void checkinfpassword(Dialog dialog, String password, TextView tvExpain);
    }

    /**
     * 案例
     *
     * MVDialog.getInstance(MyTrendsFragment.this.getContext())
     *                                 .setContent("确定删除这条动态吗？")
     *                                 .setConfirmOnlick(new MVDialog.ConfirmOnclick() {
     *                                     @Override
     *                                     public void confirm(MVDialog dialog) {
     *                                      dialog.dismiss();
     *                                     }
     *                                 })
     *                                 .chooseType(MVDialog.TypeEnum.CENTER)
     *                                 .show();
     *
     *
     *
     *
     *                         List<Integer> clorse = new ArrayList<>();
     *                         clorse.add(0);
     *                         List<String> data = new ArrayList<>();
     *                         data.add("确定");
     *                         data.add("取消");
     *                         MVDialog.getInstance(MyTrendsFragment.this.getContext())
     *                                 .setColorsPosion(clorse)
     *                                 .setListData(data)
     *                                 .setClickList(new MVDialog.ClickList() {
     *                                     @Override
     *                                     public void clickListItem(MVDialog dialog, int posion) {
     *                                         ToastUtils.showShort("确定:"+posion);
     *                                     }
     *                                 })
     *                                 .chooseType(MVDialog.TypeEnum.BOTTOMLIST)
     *                                 .show();
     */

}
