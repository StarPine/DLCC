package com.fine.friendlycc.calling.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ObjectUtils;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.event.AudioCallingCancelEvent;
import com.fine.friendlycc.calling.viewmodel.AudioCallingViewModel2;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.widget.dialog.TraceDialog;
import com.google.gson.Gson;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.ActivityCallWaiting2Binding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.model.util.TUICallingConstants;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.bus.RxBus;
import me.tatarka.bindingcollectionadapter2.BR;

public class CCLineAudioActivity extends BaseActivity<ActivityCallWaiting2Binding, AudioCallingViewModel2> {

    private CallingInviteInfo callingInviteInfo;
    //拨打方UserId
    private String callUserId;
    private String toId;
    private Integer roomId;
    private TUICalling.Role role;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.setLocal(newBase));
    }

    /**
     * 就算你在Manifest.xml设置横竖屏切换不重走生命周期。横竖屏切换还是会走这里

     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig!=null){
            LocaleManager.setLocal(this);
        }
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocal(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LocaleManager.setLocal(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBarUtils.setupStatusBar(this, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImmersionBarUtils.setupStatusBar(this, true, true);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_call_waiting2;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioCallingViewModel2 initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(AudioCallingViewModel2.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        Intent intent = getIntent();
        role = (TUICalling.Role) intent.getExtras().get(TUICallingConstants.PARAM_NAME_ROLE);
        //被动接收
        String[] userIds = intent.getExtras().getStringArray(TUICallingConstants.PARAM_NAME_USERIDS);
        if (userIds != null && userIds.length > 0) {
            toId = userIds[0];
        }
        //主动呼叫
        callUserId = intent.getExtras().getString(TUICallingConstants.PARAM_NAME_SPONSORID);
        String userData = intent.getExtras().getString("userProfile");
        if (userData != null) {
            callingInviteInfo = new Gson().fromJson(userData, CallingInviteInfo.class);
        }

    }

    @Override
    public void initData() {
        super.initData();
        if (role == TUICalling.Role.CALL) {//主动呼叫
            callUserId = V2TIMManager.getInstance().getLoginUser();
            if (callingInviteInfo != null) {
                viewModel.init(callUserId, toId, role, callingInviteInfo.getRoomId());
                viewModel.callingInviteInfoField.set(callingInviteInfo);
                if (callingInviteInfo.getPaymentRelation().getPayerUserId() == Injection.provideDemoRepository().readUserData().getId()
                        && ConfigManager.getInstance().getTipMoneyShowFlag()) {
                    if (!ObjectUtils.isEmpty(callingInviteInfo.getMessages()) && callingInviteInfo.getMessages().size() > 0) {
                        String valueData = "";
                        for (String value : callingInviteInfo.getMessages()) {
                            valueData += value + "\n";
                        }
                        viewModel.maleBinding.set(valueData);
                    }
                }
                viewModel.start();
            }
        } else {//被动接听
            toId = V2TIMManager.getInstance().getLoginUser();
            viewModel.init(callUserId, toId, role);
            viewModel.getCallingInvitedInfo(1, callUserId);
        }
        try {
            new RxPermissions(this)
                    .request(Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {

                        } else {
                            if(this.isFinishing()){
                                return;
                            }
                            TraceDialog.getInstance(CCLineAudioActivity.this)
                                    .setCannelOnclick(new TraceDialog.CannelOnclick() {
                                        @Override
                                        public void cannel(Dialog dialog) {
                                            viewModel.cancelCallClick();
                                        }
                                    })
                                    .setConfirmOnlick(new TraceDialog.ConfirmOnclick() {
                                        @Override
                                        public void confirm(Dialog dialog) {
                                            new RxPermissions(CCLineAudioActivity.this)
                                                    .request(Manifest.permission.RECORD_AUDIO)
                                                    .subscribe(granted -> {
                                                        if (!granted) {
                                                            viewModel.cancelCallClick();
                                                        }
                                                    });
                                        }
                                    }).AlertCallAudioPermissions().show();
                        }
                    });
        } catch (Exception e) {

        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.backViewEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void unused) {
                finish();
            }
        });

        viewModel.startAudioActivity.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer roomId) {
                Intent intent = new Intent(CCLineAudioActivity.this,AudioCallChatingActivity.class);
                intent.putExtra("fromUserId", callUserId);
                intent.putExtra("toUserId", toId);
                intent.putExtra("mRole", role);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_zoom_in, R.anim.anim_stay);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RxBus.getDefault().post(new AudioCallingCancelEvent());
    }
}
