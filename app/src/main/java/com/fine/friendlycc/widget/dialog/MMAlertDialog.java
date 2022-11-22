package com.fine.friendlycc.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.bean.EvaluateItemBean;
import com.fine.friendlycc.ui.message.chatdetail.ChatDetailEvaluateAdapter;
import com.fine.friendlycc.utils.StringUtil;

import java.util.List;


public class MMAlertDialog {
    public static Integer selectedPosition = -1;


    public static AlertDialog WithdrawAccountDialog(Context context, boolean touchOutside, String account, String name, WithdrawAccountDialogListener withdrawAccountDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.dialog_set_withdraw_account, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        EditText edtAccount = view.findViewById(R.id.edt_account);
        EditText edtName = view.findViewById(R.id.edt_name);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        ImageView ivClose = view.findViewById(R.id.iv_dialog_close);
        //根据是否有焦点更新背景（这里只是演示使用，其实这种更换背景的效果可以通过Selector来实现）

        final AlertDialog dialogFinal = dialog;
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withdrawAccountDialogListener.onivClose(dialogFinal, DialogInterface.BUTTON_POSITIVE);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = edtAccount.getText().toString();
                String name = edtName.getText().toString();
                withdrawAccountDialogListener.onConfirmClick(dialogFinal, DialogInterface.BUTTON_POSITIVE, account, name);
            }
        });

        if (!StringUtils.isEmpty(account)) {
            edtAccount.setText(account);
        }
        if (!StringUtils.isEmpty(name)) {
            edtName.setText(name);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                edtAccount.clearFocus();
                edtName.clearFocus();
                withdrawAccountDialogListener.setOnDismissListener();
            }
        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ConvertUtils.dp2px(300), ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        //   dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setWindowAnimations(R.style.AnimMM);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //弹出系统键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return dialog;
    }

    /**
     * @return android.app.AlertDialog
     * @Author 彭石林
     * @Description 真人认证弹窗
     * @Date 2021/4/19 16:35
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [context, touchOutside, account, name, withdrawAccountDialogListener]
     **/
    public static AlertDialog RegisterFaceDialog(Context context, boolean touchOutside, Uri imgUrl, DilodAlertInterface dilodAlertInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.layout_dialog_register_face, null);
        ImageView faceImage = view.findViewById(R.id.img_face);
        faceImage.setImageURI(imgUrl);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        ImageView ivClose = view.findViewById(R.id.iv_dialog_close);

        final AlertDialog dialogFinal = dialog;
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertInterface.confirm(dialogFinal, DialogInterface.BUTTON_POSITIVE, 0);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertInterface.cancel(dialogFinal, DialogInterface.BUTTON_POSITIVE);
            }
        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ConvertUtils.dp2px(300), ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        //   dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setWindowAnimations(R.style.AnimMM);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //弹出系统键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return dialog;
    }

    public static AlertDialog DialogNotification(Context context, boolean touchOutside, DilodAlertInterface dilodAlertInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_dialog_notification, null);
        ImageView notification_cannle = view.findViewById(R.id.notification_cannle);
        ImageView notification_ok = view.findViewById(R.id.notification_ok);
        final AlertDialog dialogFinal = dialog;
        notification_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertInterface.confirm(dialogFinal, DialogInterface.BUTTON_POSITIVE, 0);
            }
        });
        notification_cannle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertInterface.cancel(dialogFinal, DialogInterface.BUTTON_POSITIVE);
            }
        });
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(ConvertUtils.dp2px(300), ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }

    public static AlertDialog RegUserAlert(Context context, boolean touchOutside, RegUserAlertInterface regUserAlertInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_dialog_hint, null);
        Button button = view.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regUserAlertInterface.confirm(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog isAlertVipMonetyunlock(Context context, boolean touchOutside, String titleString, String contentString, String confirmText, String confirmTwoText, AlertVipMonetyunlockInterface alertVipMonetyunlockInterface) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog dialog = builder.create();
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_title, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - (context.getResources().getDisplayMetrics().widthPixels / 5);
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView content = contentView.findViewById(R.id.tv_content);
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

        Button contentBtn = contentView.findViewById(R.id.btn_confirm);
        Button contentTowBtn = contentView.findViewById(R.id.btn_confirm_two);
        contentView.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVipMonetyunlockInterface.cannel(dialog);
            }
        });
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVipMonetyunlockInterface.confirm(dialog, 1);
//                ToastUtils.showShort("确定");
            }
        });
        contentTowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVipMonetyunlockInterface.confirmTwo(dialog, 1);
            }
        });
        if (StringUtil.isEmpty(confirmText)) {
            contentBtn.setVisibility(View.GONE);
        } else {
            contentBtn.setText(confirmText);
        }
        if (StringUtil.isEmpty(confirmTwoText)) {
            contentTowBtn.setText(context.getResources().getString(R.string.playcc_confirm));
            contentTowBtn.setVisibility(View.GONE);
        } else {
            contentTowBtn.setText(confirmTwoText);
            contentTowBtn.setVisibility(View.VISIBLE);
        }
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(contentView);
        return dialog;
    }
    //

    public static AlertDialog AlertShareVip(Context context, boolean touchOutside, RegUserAlertInterface regUserAlertInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_share_vip, null);
        Button button = view.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regUserAlertInterface.confirm(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });
        ImageView btn_close = view.findViewById(R.id.iv_dialog_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }

    public static AlertDialog AlertShareVipGet(Context context, boolean touchOutside, DilodAlertMessageInterface dilodAlertMessageInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_share_vip_get, null);
        EditText value = view.findViewById(R.id.value);
        Button button = view.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertMessageInterface.confirm(dialog, DialogInterface.BUTTON_POSITIVE, 0, value.getText().toString());
            }
        });
        ImageView cannel = view.findViewById(R.id.iv_dialog_close);
        cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertMessageInterface.cancel(dialog, DialogInterface.BUTTON_POSITIVE);
            }
        });
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //弹出系统键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return dialog;
    }

    public static AlertDialog AlertInviteWrite(Context context, boolean touchOutside, DilodAlertMessageInterface dilodAlertMessageInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View view = View.inflate(context, R.layout.alert_invite_wite, null);
        EditText value = view.findViewById(R.id.value);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //弹出系统键盘
        //window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        });
        Button button = view.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilodAlertMessageInterface.confirm(dialog, DialogInterface.BUTTON_POSITIVE, 0, value.getText().toString());
            }
        });
        return dialog;
    }

    public static Dialog DialogChatDetail(Context context, boolean touchOutside, int DefaultSel, List<EvaluateItemBean> listData, DilodAlertInterface dilodAlertInterface) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_chat__detail_evaluate, null);
        selectedPosition = DefaultSel;
        RecyclerView recyclerView =  contentView.findViewById(R.id.rcv_evaluate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);

        ChatDetailEvaluateAdapter adapter = new ChatDetailEvaluateAdapter(listData);
        adapter.setDefSelect(DefaultSel);
        adapter.setOnItemClickListener(new ChatDetailEvaluateAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos, EvaluateItemBean itemEntity) {
                selectedPosition = pos;
                adapter.setDefSelect(pos);
            }
        });
        recyclerView.setAdapter(adapter);
        Button btn_submit = contentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilodAlertInterface.confirm(dialog,DialogInterface.BUTTON_POSITIVE,selectedPosition);
            }
        });
        ImageView iv_dialog_close = contentView.findViewById(R.id.iv_dialog_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
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
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }
    //删除声音提示
    public static Dialog AlertAudioRemove(Context context, boolean touchOutside,DilodAlertInterface dilodAlertInterface){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_audio_remove, null);

        Button btn_submit = contentView.findViewById(R.id.btn_confirm);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilodAlertInterface.confirm(dialog,DialogInterface.BUTTON_POSITIVE,0);
            }
        });
        ImageView iv_dialog_close = contentView.findViewById(R.id.iv_dialog_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilodAlertInterface.cancel(dialog,DialogInterface.BUTTON_POSITIVE);
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(contentView);
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }
    //删除声音提示
    public static Dialog AlertAudioPermissions(Context context, boolean touchOutside,DilodAlertInterface dilodAlertInterface){
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(touchOutside);
        dialog.setCancelable(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_audio_permission, null);

        TextView btn_submit = contentView.findViewById(R.id.btn_confirm);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilodAlertInterface.confirm(dialog,DialogInterface.BUTTON_POSITIVE,0);
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(contentView);
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    public interface DilodAlertInterface {
        // 确认操作
        void confirm(DialogInterface dialog, int which, int sel_Index);

        // 取消操作
        void cancel(DialogInterface dialog, int which);
    }

    public interface WithdrawAccountDialogListener {
        void onivClose(DialogInterface dialog, int which);

        void onConfirmClick(DialogInterface dialog, int which, String account, String name);

        void setOnDismissListener();
    }

    public interface DilodAlertMessageInterface {
        // 确认操作
        void confirm(DialogInterface dialog, int which, int sel_Index, String swiftMessageEntity);

        // 取消操作
        void cancel(DialogInterface dialog, int which);
    }

    public interface RegUserAlertInterface {
        // 确认操作
        void confirm(DialogInterface dialog, int which);
    }

    public interface AlertVipMonetyunlockInterface {
        // 确认操作
        void confirm(DialogInterface dialog, int which);

        // 确认操作
        void confirmTwo(DialogInterface dialog, int which);

        void cannel(DialogInterface dialog);
    }

    public interface AlertAddressesInterface {
        // 确认操作
        void toAddress(DialogInterface dialog, boolean isEmpty);

        // 取消操作
        void confirmSub(DialogInterface dialog);
    }

    //删除声音提示
    public static Dialog AlertAccountCancell(Context context, RegUserAlertInterface regUserAlertInterface) {
        Dialog dialog = new Dialog(context);
        //强制引导去应用市场更新
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_account_cancell, null);

        TextView btn_submit = contentView.findViewById(R.id.btn_sub);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regUserAlertInterface.confirm(dialog, 0);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置背景透明,去四个角
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(contentView);
        //设置宽度充满屏幕
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

}