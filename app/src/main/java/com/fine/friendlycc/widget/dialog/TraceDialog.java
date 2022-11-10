package com.fine.friendlycc.widget.dialog;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.GlideEngine;
import com.fine.friendlycc.entity.MallWithdrawTipsInfoEntity;
import com.fine.friendlycc.event.DailyAccostEvent;
import com.fine.friendlycc.ui.mine.vipsubscribe.VipPrivilegeItemViewModel;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;

/**
 * Author: 彭石林
 * Time: 2021/8/3 16:09
 * Description: This is TraceDialog
 */
public class TraceDialog {
    private static volatile TraceDialog INSTANCE;
    private Context context;
    private TypeEnum CHOOSRTYPE;
    private Dialog dialog;

    private String titleString = "";
    private String contentString = "";
    private String confirmText = "";
    private String confirmTwoText = "";
    private String cannelText = "";
    private int titleSize = 0;
    private int firstRewardId = 0;
    private int secondRewardId = 0;

    private int mTouchStartY;
    private int mTouchLastY;


    private ConfirmOnclick confirmOnclick;
    private ConfirmTwoOnclick confirmTwoOnclick;
    private ConfirmThreeOnclick confirmThreeOnclick;

    private CannelOnclick cannelOnclick;
    private int currentTop;

    private TraceDialog(Context context) {
        this.context = context;
    }

    public static TraceDialog getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TraceDialog.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TraceDialog(context);
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
        INSTANCE.cannelText = "";
        INSTANCE.confirmOnclick = null;
        INSTANCE.confirmTwoOnclick = null;
        INSTANCE.cannelOnclick = null;
    }

    /**
     * 最后第二步才选项弹框样式
     *
     * @return
     */
    public TraceDialog chooseType(TypeEnum typeEnum) {
        this.CHOOSRTYPE = typeEnum;
        if (typeEnum == TypeEnum.CENTER) {
            this.dialog = getcenterDialog();
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
        }else {
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
        }else {
            this.dialog.dismiss();
        }

    }

    /**
     * 设置
     *
     * @param titleString
     * @return
     */
    public TraceDialog setTitle(String titleString) {
        this.titleString = titleString;
        return INSTANCE;
    }

    public TraceDialog setContent(String content) {
        this.contentString = content;
        return INSTANCE;
    }

    public TraceDialog setTitleSize(int size) {
        this.titleSize = size;
        return INSTANCE;
    }

    public TraceDialog setFirstRewardId(int resId) {
        this.firstRewardId = resId;
        return INSTANCE;
    }

    public TraceDialog setSecondRewardId(int resId) {
        this.secondRewardId = resId;
        return INSTANCE;
    }

    /**
     * 设置确定按钮文案
     *
     * @param confirmText
     * @return
     */
    public TraceDialog setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        return INSTANCE;
    }

    /**
     * 设置第二个按钮文案
     *
     * @param confirmTwoText
     * @return
     */
    public TraceDialog setConfirmTwoText(String confirmTwoText) {
        this.confirmTwoText = confirmTwoText;
        return INSTANCE;
    }

    public TraceDialog setCannelText(String cannelText) {
        this.cannelText = cannelText;
        return INSTANCE;
    }

    /**
     * 设置确认按钮点击
     *
     * @param confirmOnclick
     * @return
     */
    public TraceDialog setConfirmOnlick(ConfirmOnclick confirmOnclick) {
        this.confirmOnclick = confirmOnclick;
        return INSTANCE;
    }

    public TraceDialog setConfirmTwoOnlick(ConfirmTwoOnclick confirmTwoOnclick) {
        this.confirmTwoOnclick = confirmTwoOnclick;
        return INSTANCE;
    }

    public TraceDialog setConfirmThreeOnlick(ConfirmThreeOnclick confirmThreeOnclick) {
        this.confirmThreeOnclick = confirmThreeOnclick;
        return INSTANCE;
    }



    public TraceDialog setCannelOnclick(CannelOnclick cannelOnclick) {
        this.cannelOnclick = cannelOnclick;
        return INSTANCE;
    }

    /**
     * 获取
     *
     * @return
     */
    private Dialog getcenterDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_trace, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        TextView title = contentView.findViewById(R.id.tv_title);
        Button confirmBtn = contentView.findViewById(R.id.confirm);
        Button cannelwBtn = contentView.findViewById(R.id.cannel);
        if (StringUtils.isEmpty(titleString)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(titleString);
        }
        if (titleSize != 0){
            title.setTextSize(titleSize);
        }

        if (StringUtils.isEmpty(confirmText)) {
            confirmBtn.setVisibility(View.GONE);
        } else {
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setText(confirmText);
        }

        if (StringUtils.isEmpty(cannelText)) {
            cannelwBtn.setVisibility(View.GONE);
        } else {
            cannelwBtn.setVisibility(View.VISIBLE);
            cannelwBtn.setText(cannelText);
        }

        cannelwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cannelOnclick != null) {
                    cannelOnclick.cannel(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    /**
     * 水晶兑换dialog
     * @return
     * @param data
     */
    public Dialog getCrystalExchange(MallWithdrawTipsInfoEntity data) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_crystal_exchange, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        Button confirmBtn = contentView.findViewById(R.id.confirm);
        TextView title = contentView.findViewById(R.id.title);
        TextView questionMark = contentView.findViewById(R.id.question_mark);
        TextView questionMark2 = contentView.findViewById(R.id.question_mark2);
        TextView price1 = contentView.findViewById(R.id.price1);
        TextView price2 = contentView.findViewById(R.id.price2);
        TextView crystal1 = contentView.findViewById(R.id.crystal1);
        TextView crystal2 = contentView.findViewById(R.id.crystal2);
        if (data != null){
            title.setText(data.getTitle());
            price1.setText(""+data.getGoodsList().get(0).getQuantity());
            price2.setText(""+data.getGoodsList().get(1).getQuantity());
            crystal1.setText(""+data.getGoodsList().get(0).getProfits());
            crystal2.setText(""+data.getGoodsList().get(1).getProfits());
        }

        questionMark.setText("???");
        questionMark2.setText("???");
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    /**
     * 会话列表长按menu
     * @return
     * @param conversationInfo
     */
    public Dialog convasationItemMenuDialog(ConversationInfo conversationInfo){
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_conversation_item_menu, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        TextView topChat = contentView.findViewById(R.id.tv_chat_top);
        TextView delChat = contentView.findViewById(R.id.tv_del_chat);
        TextView delBannedAccount = contentView.findViewById(R.id.tv_del_banned_account);
        if (conversationInfo.isTop()){
            topChat.setText(R.string.quit_chat_top);
        }else {
            topChat.setText(R.string.playcc_top_chat);
        }
        setDelBannedVisibility(conversationInfo, delBannedAccount);
        topChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        delChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmTwoOnclick != null) {
                    confirmTwoOnclick.confirmTwo(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        delBannedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmThreeOnclick != null) {
                    confirmThreeOnclick.confirmThree(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        return bottomDialog;
    }

    /**
     * 设置一键删除封号账号的item可见度
     * @param conversationInfo
     * @param delBannedAccount
     */
    private void setDelBannedVisibility(ConversationInfo conversationInfo, TextView delBannedAccount) {
        List<String> users = new ArrayList<String>();
        users.add(conversationInfo.getId());
        //获取用户资料
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                for (V2TIMUserFullInfo res : v2TIMUserFullInfos) {
                    int level = res.getLevel();
                    if (level == 6){
                        delBannedAccount.setVisibility(View.VISIBLE);
                    }else {
                        delBannedAccount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {
                Log.e("获取用户信息失败", "getUsersProfile failed: " + code + " desc");
            }
        });
    }

    public Dialog TraceVipDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_trace_vip, null);
        bottomDialog.setContentView(contentView);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView tv_content = contentView.findViewById(R.id.tv_content);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        Button confirmBtn = contentView.findViewById(R.id.confirm);
        if (!StringUtils.isEmpty(titleString)) {
            title.setText(titleString);
        }
        if (!StringUtils.isEmpty(contentString)) {
            tv_content.setText(contentString);
        }

        if (StringUtils.isEmpty(confirmText)) {
            confirmBtn.setVisibility(View.GONE);
        } else {
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setText(confirmText);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    public Dialog AlertTaskBonus() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_task_gold, null);
        bottomDialog.setContentView(contentView);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView tv_content = contentView.findViewById(R.id.tv_content);

        if (!StringUtils.isEmpty(titleString)) {
            title.setText(titleString);
        } else {
            title.setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isEmpty(contentString)) {
            tv_content.setText(contentString);
        } else {
            tv_content.setVisibility(View.GONE);
        }

        Button confirmBtn = contentView.findViewById(R.id.confirm);
        Button confirmtowBtn = contentView.findViewById(R.id.two_confirm);

        if (!StringUtils.isEmpty(confirmText)) {
            confirmBtn.setText(confirmText);
        }
        if (!StringUtils.isEmpty(confirmTwoText)) {
            confirmtowBtn.setText(confirmTwoText);
        } else {
            confirmtowBtn.setVisibility(View.GONE);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        confirmtowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmTwoOnclick != null) {
                    confirmTwoOnclick.confirmTwo(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        return bottomDialog;
    }

    public Dialog verticalButtonDialog() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_vertical_two_button, null);
        bottomDialog.setContentView(contentView);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView tv_content = contentView.findViewById(R.id.tv_content);

        if (!StringUtils.isEmpty(titleString)) {
            title.setText(titleString);
        } else {
            title.setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isEmpty(contentString)) {
            tv_content.setText(contentString);
        } else {
            tv_content.setVisibility(View.GONE);
        }

        Button button_top = contentView.findViewById(R.id.button_top);
        Button button_below = contentView.findViewById(R.id.button_below);

        if (!StringUtils.isEmpty(confirmText)) {
            button_top.setText(confirmText);
        }
        if (!StringUtils.isEmpty(confirmTwoText)) {
            button_below.setText(confirmTwoText);
        } else {
            button_below.setVisibility(View.GONE);
        }

        button_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        button_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmTwoOnclick != null) {
                    confirmTwoOnclick.confirmTwo(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });

        return bottomDialog;
    }

    public Dialog AlertTaskBonuSubHint(boolean ishowVip) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_task_gold1, null);
        bottomDialog.setContentView(contentView);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        TextView title = contentView.findViewById(R.id.tv_title);
        LinearLayout tv_content = contentView.findViewById(R.id.content);

        TextView bottom_vip = contentView.findViewById(R.id.bottom_vip);
        if (!StringUtils.isEmpty(titleString)) {
            title.setText(titleString);
        } else {
            title.setVisibility(View.INVISIBLE);
        }

        if (ishowVip) {
            tv_content.setVisibility(View.VISIBLE);
            bottom_vip.setVisibility(View.VISIBLE);
        } else {
            tv_content.setVisibility(View.GONE);
            bottom_vip.setVisibility(View.GONE);
        }

        Button confirmBtn = contentView.findViewById(R.id.confirm);
        Button cannelBtn = contentView.findViewById(R.id.cannel);

        if (!StringUtils.isEmpty(confirmText)) {
            confirmBtn.setText(confirmText);
        }
        if (!StringUtils.isEmpty(cannelText)) {
            cannelBtn.setText(cannelText);
        } else {
            cannelBtn.setVisibility(View.GONE);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
            }
        });

        cannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cannelOnclick != null) {
                    cannelOnclick.cannel(bottomDialog);
                }
            }
        });

        return bottomDialog;
    }

    //非VIP弹窗
    public Dialog AlertAudioPlayer(Integer money) {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_audio_success, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.setCancelable(false);
        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);
        TextView cannelBtn = contentView.findViewById(R.id.btn_cannel);
        TextView titleFen = contentView.findViewById(R.id.title_fen);
        titleFen.setText(String.format(StringUtils.getString(R.string.playcc_radio_daily_atendance_fen1), money));

        ImageView cannelImage = contentView.findViewById(R.id.iv_dialog_close);
        cannelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        cannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cannelOnclick != null) {
                    cannelOnclick.cannel(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    //弹出权限获取提示
    public Dialog AlertCallAudioPermissions() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_permissions_media_video, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.setCancelable(false);

        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);

        ImageView cannelImage = contentView.findViewById(R.id.iv_dialog_close);

        cannelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cannelOnclick != null) {
                    cannelOnclick.cannel(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("确定");
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }
                bottomDialog.dismiss();
            }
        });
        return bottomDialog;
    }

    //女性真人认证体现提示
    public Dialog alertCertificationGirl() {
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_certification_girl_tw_money, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.setCancelable(false);

        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        //设置背景透明,去四个角
        bottomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //设置宽度充满屏幕
        Window window = bottomDialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return bottomDialog;
    }

    /**
     * @Desc TODO(签到成功弹窗)
     * @author 彭石林
     * @parame [isCard, text, message]
     * @return android.app.Dialog
    * @Date 2021/12/11
    */
    public Dialog AlertTaskSignDay(Integer isCard,String text,String message){
        Dialog bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.alert_daily_attendance_success, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        ImageView title_img = contentView.findViewById(R.id.title_img);
        if(isCard==1){
            title_img.setImageResource(R.drawable.task_sign_card_img);
        }
        TextView title_fen = contentView.findViewById(R.id.title_fen);
        title_fen.setText(String.valueOf(text));
        TextView content_text = contentView.findViewById(R.id.content_text);
        content_text.setText(String.valueOf(message));

        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOnclick != null) {
                    confirmOnclick.confirm(bottomDialog);
                }else{
                    bottomDialog.dismiss();
                }
            }
        });
        ImageView iv_dialog_close = contentView.findViewById(R.id.iv_dialog_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cannelOnclick != null) {
                    cannelOnclick.cannel(bottomDialog);
                } else {
                    bottomDialog.dismiss();
                }
            }
        });
        //设置背景透明,去四个角
        bottomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //设置宽度充满屏幕
        Window window = bottomDialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return bottomDialog;
    }

    public Dialog getImageDialog(Context context,String drawable){
        Dialog bottomDialog = new Dialog(context, R.style.ShowImageDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        ImageView imageView = contentView.findViewById(R.id.imageView);
        if (drawable != null){
            Glide.with(context)
                    .load(StringUtil.getFullImageUrl(drawable))
                    .fitCenter()//防止部分账号图片被拉伸
//                    .error(R.drawable.radio_dating_img_default) //异常时候显示的图片
//                    .placeholder(R.drawable.radio_dating_img_default) //加载成功前显示的图片
//                    .fallback( R.drawable.radio_dating_img_default) //url为空的时候,显示的图片
                    .into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        //设置背景透明,去四个角
        bottomDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //设置宽度充满屏幕
        Window window = bottomDialog.getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ShowImageDialogAnimation);
        return bottomDialog;
    }

    /**
     * vip挽留弹框
     * @return
     * @param vipPrivilegeList
     */
    public Dialog vipRetainDialog(List<VipPrivilegeItemViewModel> vipPrivilegeList){
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_vip_retain, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);

        Button buy = contentView.findViewById(R.id.btn_buy);
        TextView tv_give_up = contentView.findViewById(R.id.tv_give_up);
        LinearLayout ll_privileges = contentView.findViewById(R.id.ll_privileges);
        if (vipPrivilegeList != null && vipPrivilegeList.size() > 0){
            for (int i = 0; i < vipPrivilegeList.size(); i++) {
                if (i >= 3)break;
                ll_privileges.addView(getPrivilegesView(vipPrivilegeList.get(i)));
            }
        }

        buy.setOnClickListener(v -> dialog.dismiss());
        tv_give_up.setOnClickListener(v -> {
            if (cannelOnclick != null){
                cannelOnclick.cannel(dialog);
            }
        });
        return dialog;
    }

    /**
     * 钻石充值挽留弹框
     * @return
     */
    public Dialog rechargeRetainDialog() {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_recharge_retain, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        Button again = contentView.findViewById(R.id.btn_again);
        Button confirm = contentView.findViewById(R.id.btn_confirm);
        again.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            if (confirmOnclick != null){
                confirmOnclick.confirm(dialog);
            }
        });
        return dialog;
    }
    /***
     * 每日奖励弹框
     * @param isUnableEvent 是否限制外界事件
     * @param dayGiveCoin   明天钻石奖励数量
     * @param dayGiveVideoCard  明天视频卡奖励数量
     * @param fristRewardNum  第一个奖励数量
     * @param secondRewardNum 第二个奖励数量
     * @return
     */
    public Dialog dayRewardDialog(boolean isUnableEvent, int dayGiveCoin, int dayGiveVideoCard, int fristRewardNum, int secondRewardNum) {
        String content = null;
        String fristRewardTips = null;
        String secondRewardTips = null;
        if (dayGiveCoin > 0 && dayGiveVideoCard > 0) {
            content = String.format(context.getString(R.string.playcc_reward_tips), dayGiveCoin + "", dayGiveVideoCard + "");
        }
        if (dayGiveCoin > 0 && dayGiveVideoCard <= 0) {
            content = String.format(context.getString(R.string.playcc_reward_tips2), dayGiveCoin + "");
        }
        if (dayGiveVideoCard > 0 && dayGiveCoin <= 0) {
            content = String.format(context.getString(R.string.playcc_reward_tips3), dayGiveVideoCard + "");
        }
        if (fristRewardNum > 0 || secondRewardNum > 0) {
            if (fristRewardNum > 0){
                fristRewardTips = String.format(context.getString(R.string.playcc_coin_earnings_money_add), fristRewardNum + "");
            }
            if (secondRewardNum > 0){
                secondRewardTips = String.format(context.getString(R.string.playcc_coin_earnings_money_add), secondRewardNum + "");
            }
        }
        return rewardDialog(isUnableEvent, fristRewardTips, secondRewardTips, content);
    }

    /**
     * 注册奖励
     *
     * @param isUnableEvent
     * @param fristRewardNum
     * @param secondRewardNum
     * @return
     */
    public Dialog registerRewardDialog(boolean isUnableEvent, int fristRewardNum, int secondRewardNum) {
        String contentTip = context.getString(R.string.playcc_reward_tips4);
        String fristRewardTips = context.getString(R.string.playcc_reward_card_tips) + fristRewardNum;
        String secondRewardTips = context.getString(R.string.playcc_reward_card_tips2) + secondRewardNum;
        return rewardDialog(isUnableEvent, fristRewardTips, secondRewardTips, contentTip);
    }

    /**
     * 奖励弹框
     *
     * @param isUnableEvent
     * @param fristRewardTips
     * @param secondRewardTips
     * @param contentTip
     * @return
     */
    public Dialog rewardDialog(boolean isUnableEvent, String fristRewardTips, String secondRewardTips, String contentTip) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_day_reward, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        if (isUnableEvent) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }

        Button btnConfirm = contentView.findViewById(R.id.btn_confirm);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvContent = contentView.findViewById(R.id.tv_content);
        LinearLayout llDoule = contentView.findViewById(R.id.ll_doule);
        LinearLayout llDoule2 = contentView.findViewById(R.id.ll_doule2);
        LinearLayout llSingle = contentView.findViewById(R.id.ll_single);
        ImageView iv_single_diamond = contentView.findViewById(R.id.iv_single_diamond);
        ImageView iv_doule_frist_reward = contentView.findViewById(R.id.iv_doule_frist_reward);
        ImageView iv_doule_second_reward = contentView.findViewById(R.id.iv_doule_second_reward);
        TextView tv_doule_diamond_number = contentView.findViewById(R.id.tv_doule_diamond_number);
        TextView tv_doule_video_card_number = contentView.findViewById(R.id.tv_doule_video_card_number);
        TextView tv_single_diamond_number = contentView.findViewById(R.id.tv_single_diamond_number);

        if (!TextUtils.isEmpty(titleString)) {
            tvTitle.setText(titleString);
        }
        if (firstRewardId != 0){
            iv_doule_frist_reward.setImageDrawable(context.getDrawable(firstRewardId));
        }
        if (secondRewardId != 0){
            iv_doule_second_reward.setImageDrawable(context.getDrawable(secondRewardId));
        }
        if (fristRewardTips != null && secondRewardTips != null){
            llSingle.setVisibility(View.GONE);
            llDoule.setVisibility(View.VISIBLE);
            llDoule2.setVisibility(View.VISIBLE);
            tv_doule_diamond_number.setText(fristRewardTips);
            tv_doule_video_card_number.setText(secondRewardTips);
        }else {
            llSingle.setVisibility(View.VISIBLE);
            llDoule.setVisibility(View.GONE);
            llDoule2.setVisibility(View.GONE);
            if (fristRewardTips != null){
                iv_single_diamond.setImageDrawable(context.getDrawable(R.drawable.icon_diamond));
                tv_single_diamond_number.setText( fristRewardTips);
            }
            if (secondRewardTips != null){
                iv_single_diamond.setImageDrawable(context.getDrawable(R.drawable.icon_video_card));
                tv_single_diamond_number.setText(secondRewardTips);
            }

        }
        if (contentTip == null) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(contentTip);
        }

        btnConfirm.setOnClickListener(v -> {
            firstRewardId = 0;
            secondRewardId = 0;
            titleString = "";
            if (confirmOnclick != null) {
                RxBus.getDefault().post(new DailyAccostEvent());
                confirmOnclick.confirm(dialog);
            }
        });
        return dialog;
    }

    private View getPrivilegesView(VipPrivilegeItemViewModel vipPrivilegeItemViewModel) {
        View privilegesView = LayoutInflater.from(context).inflate(R.layout.item_dialog_vip_privilege, null);
        ImageView ivPrivilegeIcon = privilegesView.findViewById(R.id.iv_privilege_icon);
        TextView tvPrivilegeTitle = privilegesView.findViewById(R.id.tv_privilege_title);
        TextView tvPrivilegeDesc = privilegesView.findViewById(R.id.tv__privilege_desc);
        String desc = vipPrivilegeItemViewModel.itemEntity.get().getDesc();
        String img = vipPrivilegeItemViewModel.itemEntity.get().getImg();
        String title = vipPrivilegeItemViewModel.itemEntity.get().getTitle();
        GlideEngine.createGlideEngine().loadImage(context, StringUtil.getFullThumbImageUrl(img), ivPrivilegeIcon);
        tvPrivilegeDesc.setText(desc);
        tvPrivilegeTitle.setText(title);

        return privilegesView;
    }

    public Dialog getVideoEvaluationDialog(String headUrl) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_video_evaluation, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        Window window = dialog.getWindow();
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        //设置宽度充满屏幕
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        ImageView close = contentView.findViewById(R.id.iv_close);
        ImageView userHead = contentView.findViewById(R.id.iv_user_head);
        LinearLayout notHappy = contentView.findViewById(R.id.ll_not_happy);
        LinearLayout happy = contentView.findViewById(R.id.ll_happy);

        close.setOnClickListener(v -> dialog.dismiss());
        notHappy.setOnClickListener(v -> {
            dialog.dismiss();
            if (confirmOnclick != null) {
                confirmOnclick.confirm(dialog);
            }
        });
        happy.setOnClickListener(v -> {
            dialog.dismiss();
            if (confirmTwoOnclick != null) {
                confirmTwoOnclick.confirmTwo(dialog);
            }
        });
        Glide.with(context)
                .load(StringUtil.getFullImageUrl(headUrl))
                .error(R.drawable.default_avatar) //异常时候显示的图片
                .placeholder(R.drawable.default_avatar) //加载成功前显示的图片
                .fallback( R.drawable.default_avatar) //url为空的时候,显示的图片
                .into(userHead);

        return dialog;
    }

    public Dialog getVideoPushDialog(String headUrl, boolean isMale, boolean isVip, int age ,int seconds,String nickname) {
        Dialog dialog = new Dialog(context, R.style.TransparentDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_video_push, null);
        dialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        //设置动画
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.TopDialog_Animation);
        window.setGravity(Gravity.TOP);
        //设置宽度充满屏幕
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        ImageView ivCallReject = contentView.findViewById(R.id.iv_call_reject);
        ImageView ivCallAccept = contentView.findViewById(R.id.iv_call_accept);
        ImageView userHead = contentView.findViewById(R.id.iv_user_head);
        ImageView ivGoddess = contentView.findViewById(R.id.iv_goddess);
        TextView userName = contentView.findViewById(R.id.tv_user_name);
        TextView userAge = contentView.findViewById(R.id.tv_age);
        TextView tvTime = contentView.findViewById(R.id.tv_time);
        RelativeLayout rl_support = contentView.findViewById(R.id.rl_support);
        View statusView = contentView.findViewById(R.id.status_bar_view);

        if (statusView != null) {
            ImmersionBarUtils.setupStatusBar((Activity) context, dialog, true, true);
            ImmersionBar.setStatusBarView((Activity) context, statusView);
        }

        ivCallReject.setOnClickListener(v -> {
            dialog.dismiss();
        });
        ivCallAccept.setOnClickListener(v -> {
            dialog.dismiss();
            if (confirmTwoOnclick != null) {
                confirmTwoOnclick.confirmTwo(dialog);
            }
        });

        if (!isMale && isVip){
            ivGoddess.setVisibility(View.VISIBLE);
        }else {
            ivGoddess.setVisibility(View.GONE);
        }

        dialog.setOnDismissListener(dialog1 -> {
            if (confirmOnclick != null) {
                confirmOnclick.confirm(dialog);
            }
        });

        userAge.setText(age+"");
        userName.setText(nickname);
        Glide.with(context)
                .load(StringUtil.getFullImageUrl(headUrl))
                .error(R.drawable.default_avatar) //异常时候显示的图片
                .placeholder(R.drawable.default_avatar) //加载成功前显示的图片
                .fallback( R.drawable.default_avatar) //url为空的时候,显示的图片
                .into(userHead);
        //倒计时
        startCountdown(dialog, tvTime,seconds);
        //滑动事件
        int top = ConvertUtils.dp2px(10);
        currentTop = top;
        rl_support.setOnTouchListener((v, event) -> {
            RelativeLayout.LayoutParams rlSupportLayoutParams = (RelativeLayout.LayoutParams) rl_support.getLayoutParams();
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mTouchLastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchStartY = mTouchLastY - (int) event.getRawY();
                    currentTop = currentTop - mTouchStartY;
                    mTouchLastY = (int) event.getRawY();
                    if (currentTop <= top){
                        rlSupportLayoutParams.topMargin = currentTop;
                    }else {
                        currentTop = top;
                        rlSupportLayoutParams.topMargin = top;
                    }
                    rl_support.setLayoutParams(rlSupportLayoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    if (rlSupportLayoutParams.topMargin < -10){
                        dialog.dismiss();
                    }else {
                        currentTop = top;
                        rlSupportLayoutParams.topMargin = top;
                        rl_support.setLayoutParams(rlSupportLayoutParams);
                    }

                    break;
            }
            return true;
        });

        return dialog;
    }

    private void startCountdown(Dialog dialog, TextView tvTime, int seconds) {
        ValueAnimator animator = ValueAnimator.ofInt(10,0);
        //设置时间
        animator.setDuration(seconds*1000);
        //均匀显示
        animator.setInterpolator(new LinearInterpolator());
        //监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                tvTime.setText(value+"s");
                if(value==0){
                    dialog.dismiss();
                }
            }
        });
        animator.start();
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


    public interface ConfirmOnclick {
        void confirm(Dialog dialog);
    }

    public interface ConfirmTwoOnclick {
        void confirmTwo(Dialog dialog);
    }

    public interface ConfirmThreeOnclick {
        void confirmThree(Dialog dialog);
    }

    public interface CannelOnclick {
        void cannel(Dialog dialog);
    }
}
