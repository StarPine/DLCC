package com.fine.friendlycc.api.login.email;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.api.AppGameConfig;
import com.fine.friendlycc.api.PlayFunAuthUserEntity;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.WebUrlViewActivity;
import com.fine.friendlycc.widget.custom.InputTextManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2022/4/28 14:52
 * Description: This is LoginEmailView
 */
public class LoginEmailViewFragment extends Fragment implements Consumer<Disposable>,View.OnClickListener {

    private Activity mActivity;
    private TextView pwdTextView;
    private Context mContext;
    private EditText editEmailAccount;
    private EditText editEmailCode;
    private ImageView imgBack;
    private Button btnSubmit;
    private Button btnCode;

    private AppGameConfig appGameConfig;

    private CompositeDisposable mCompositeDisposable;

    /**
     * 倒计时60秒，一次1秒
     */
    private CountDownTimer downTimer;
    private boolean isDownTime;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutResId(), container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        pwdTextView = view.findViewById(R.id.tv_pwd_view);
        pwdTextView.setOnClickListener(this);
        pwdTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线方法
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnCode = view.findViewById(R.id.btn_code);
        btnCode.setOnClickListener(this);

        editEmailAccount = view.findViewById(R.id.edit_email_account);
        editEmailCode = view.findViewById(R.id.edit_email_code);
        InputTextManager.with(this.getActivity())
                .addView(editEmailAccount)
                .addView(editEmailCode)
                .setMain(btnSubmit)
                .build();
        initWebUrlSettings(view);
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.dialog_email_login;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_pwd_view) {
            if (mActivity instanceof LoginEmailMangerActivity) {
                ((LoginEmailMangerActivity) mActivity).showFragment(1);
            }
        } else if (id == R.id.img_back) {
            mActivity.finish();
        } else if (id == R.id.btn_submit) {
            bindUserEmail(editEmailAccount.getText().toString(),editEmailCode.getText().toString());
        } else if (id == R.id.btn_code) {
            if (!isDownTime) {
                String userEmailCode = editEmailAccount.getText().toString();
                if(StringUtils.isTrimEmpty(userEmailCode)){
                    ToastUtils.showShort("请填写邮箱");
                    return;
                }
                sendUserEmailCode(userEmailCode);
            } else {
                ToastUtils.showShort("你以发送过验证码");
            }
        }
    }

    public void cancelDownTime(){
        if(isDownTime){
            downTimer.cancel();
            downTimer = null;
        }
    }

    @Override
    public void onDestroy() {
        cancelDownTime();
        super.onDestroy();
    }

    public void verifyCodeDownTime(){
        downTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isDownTime = true;
                btnCode.setText((millisUntilFinished / 1000 )+"s");
            }

            @Override
            public void onFinish() {
                isDownTime = false;
                btnCode.setText("获取");
            }
        }.start();
    }

    /**
     * @Desc TODO(隐私政策、用户协议是否隐藏)
     * @author 彭石林
     * @parame [viewRoot]
     * @return void
     * @Date 2022/4/29
     */
    public void initWebUrlSettings(View viewRoot){
        if(appGameConfig==null){
            appGameConfig = ConfigManager.getInstance().getAppRepository().readGameConfigSetting();
        }
        TextView text_view1 = viewRoot.findViewById(com.fine.friendlycc.R.id.text_view1);
        if(StringUtils.isEmpty(appGameConfig.getTermsOfServiceUrl())){
            text_view1.setVisibility(View.GONE);
        }else{
            text_view1.setVisibility(View.VISIBLE);
            text_view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, WebUrlViewActivity.class);
                    intent.putExtra("arg_web_url", appGameConfig.getTermsOfServiceUrl());
                    startActivity(intent);
                }
            });
        }

        TextView text_view2 = viewRoot.findViewById(com.fine.friendlycc.R.id.text_view2);
        if(StringUtils.isEmpty(appGameConfig.getPrivacyPolicyUrl())){
            text_view2.setVisibility(View.GONE);
        }else{
            text_view2.setVisibility(View.VISIBLE);
            text_view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, WebUrlViewActivity.class);
                    intent.putExtra("arg_web_url",appGameConfig.getPrivacyPolicyUrl());
                    startActivity(intent);
                }
            });
        }
        if(StringUtils.isEmpty(appGameConfig.getTermsOfServiceUrl()) && StringUtils.isEmpty(appGameConfig.getPrivacyPolicyUrl())){
            viewRoot.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
        }
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        if (disposable != null) {
            addSubscribe(disposable);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //View销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    private void showHUD() {
        if (mActivity instanceof LoginEmailMangerActivity) {
            ((LoginEmailMangerActivity) mActivity).showHUD();
        }
    }

    public void dismissHud() {
        if (mActivity instanceof LoginEmailMangerActivity) {
            ((LoginEmailMangerActivity) mActivity).dismissHud();
        }
    }

    /**
     * 发送邮箱验证码
     * @param userEmailCode
     */
    public void sendUserEmailCode(String userEmailCode){
        ConfigManager.getInstance().getAppRepository()
                .sendEmailCode(userEmailCode)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseResponse>(){
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        dismissHud();
                        verifyCodeDownTime();
                    }
                    @Override
                    public void onError(RequestException e) {
                        dismissHud();
                    }
                });
    }

    /**
     * 绑定用户邮箱
     * @param userEmail
     * @param userCode
     */
    public void bindUserEmail(String userEmail,String userCode){
        if(StringUtils.isTrimEmpty(userEmail)){
            ToastUtils.showShort("请填写邮箱");
            return;
        }
        if(StringUtils.isTrimEmpty(userCode)){
            ToastUtils.showShort("请填写验证码");
            return;
        }
        ConfigManager.getInstance().getAppRepository()
                .bindUserEmail(userEmail,userCode,null,1)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> showHUD())
                .subscribe(new BaseObserver<BaseDataResponse<UserDataEntity>>(){
                    @Override
                    public void onSuccess(BaseDataResponse<UserDataEntity> authLoginUserEntityBaseDataResponse) {
                        UserDataEntity authLoginUserEntity = authLoginUserEntityBaseDataResponse.getData();
                        TokenEntity tokenEntity = new TokenEntity(authLoginUserEntity.getToken(),authLoginUserEntity.getUserID(),authLoginUserEntity.getUserSig(), authLoginUserEntity.getIsContract());
                        ConfigManager.getInstance().getAppRepository().saveLoginInfo(tokenEntity);
                        if(authLoginUserEntity!=null){
                            AppContext.instance().mFirebaseAnalytics.setUserId(String.valueOf(authLoginUserEntity.getId()));
                            ConfigManager.getInstance().getAppRepository().saveUserData(authLoginUserEntity);
                            if (authLoginUserEntity.getCertification() == 1) {
                                ConfigManager.getInstance().getAppRepository().saveNeedVerifyFace(true);
                            }
                        }
                        cancelDownTime();
                        PlayFunAuthUserEntity playFunAuthUserEntity = new PlayFunAuthUserEntity();
                        playFunAuthUserEntity.setTypeLogin(4);
                        ((LoginEmailMangerActivity) mActivity).setResultPoP(playFunAuthUserEntity);
                    }
                    @Override
                    public void onComplete() {
                        dismissHud();
                    }
                });
    }
}
