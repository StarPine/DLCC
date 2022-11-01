package com.fine.friendlycc.widget.dialog.version.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.utils.ApiUitl;
import com.fine.friendlycc.widget.dialog.MVDialog;

import java.util.List;

/*
 *@Author 彭石林
 *@Description 更新APK提示弹窗
 *@Date 2020/9/29 22:27
 *@Phone 16620350375
 *@email 15616314565@163.com
 *Param
 *@return
 **/
public class UpdateDialogView {

    private static volatile UpdateDialogView INSTANCE;
    private Context context;
    private CancelOnclick cancelOnclick;
    /**
     * 文件存储路径
     */
    private static String paths = "";
    /**
     * 安卓弹出对话框
     */
    private Dialog dialog;
    /**
     * 自定义进度条
     */
    NumberProgressBar npb;
    /**
     * 进度条百分比
     */
    int progressNpb = 0;

    /**
     * UI主线程同步显示UI
     */
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    npb.setProgress(progressNpb);
                    break;
            }
        }
    };

    public static UpdateDialogView getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MVDialog.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UpdateDialogView(context);
                }
            }
        } else {
            init(context);
        }
        return INSTANCE;
    }

    private UpdateDialogView(Context context) {
        this.context = context;
    }

    private static void init(Context context) {
        INSTANCE.context = context;
    }

    /**
     * @return
     * @Author 彭石林
     * @Description 弹出对话框提示用书是否更新程序
     * @Date 2020/9/26 21:58
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [mActivity, title, update_info, apkUrl, apkName]
     **/
    public UpdateDialogView getUpdateDialogView(String title, String update_info, String apkUrl, boolean isUpdate, String apkName, String appStoreLink) {
        //初始化自定义对话框
        dialog = new Dialog(context, R.style.UpdateAppDialog);
        LinearLayout popView = (LinearLayout) LayoutInflater.
                from(context).inflate(R.layout.update_app_dialog, null);
        //安装路径默认采用应用程序file路径
        paths = context.getApplicationContext().getFilesDir().getAbsolutePath();
        npb = popView.findViewById(R.id.npb); //升级进度条
        //标题
        TextView tv_title = popView.findViewById(R.id.tv_title);
        tv_title.setText(title);
        //更新内容
        TextView tv_update_info = popView.findViewById(R.id.tv_update_info);
        tv_update_info.setText(update_info);
        Button btn_ok = popView.findViewById(R.id.btn_ok); //升级按钮
        ImageView iv_close = popView.findViewById(R.id.iv_close);//取消升级按钮
        LinearLayout ll_close = popView.findViewById(R.id.ll_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        if (isUpdate) {
            ll_close.setVisibility(View.GONE);
            //强制引导去应用市场更新
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
                }
            });
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appStoreLink != null && appStoreLink.length() > 0) {
                    if(isApplicationAvilible(context,"com.android.vending")){
                        String googleId = ApiUitl.getGoogleId();
                        if(!StringUtils.isEmpty(googleId) && !StringUtils.isTrimEmpty(googleId)){
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(appStoreLink.trim()));
                            intent.setPackage("com.android.vending");
                            context.startActivity(intent);
                        }else{
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(appStoreLink.trim()));
                            context.startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(appStoreLink.trim()));
                        context.startActivity(intent);
                    }
                    return;
                }
                if (!StringUtils.isEmpty(apkUrl)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(apkUrl.trim()));
                    context.startActivity(intent);
                }
            }
        });

        dialog.setContentView(popView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.setOnDismissListener(dialog1 -> {
            if (cancelOnclick != null)
                cancelOnclick.cancel();
        });
        return INSTANCE;
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.dismiss();
    }

    /**
     * 设置取消按钮点击
     *
     * @param cancelOnclick
     * @return
     */
    public UpdateDialogView setConfirmOnlick(CancelOnclick cancelOnclick) {
        this.cancelOnclick = cancelOnclick;
        return INSTANCE;
    }

    public interface CancelOnclick {
        void cancel();
    }

    /**
     * 根据包名判断应用是否存在
     * @param packageName
     * @return
     */
    public boolean checkApk(String packageName) {
        if (packageName == null || "".equals(packageName)){
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return info != null;

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }
}
