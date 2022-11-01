package com.fine.friendlycc.api.login.email;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.WebUrlViewActivity;
import com.fine.friendlycc.widget.custom.InputTextManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Author: 彭石林
 * Time: 2022/4/28 16:05
 * Description: This is LoginEmailPwdViewFragment
 */
public class LoginEmailPwdViewFragment extends Fragment implements Consumer<Disposable>,View.OnClickListener {

    private Activity mActivity;
    private Context mContext;
    private TextView pwdTextView;
    private ImageView imgBack;
    private EditText editEmailAccount;
    private EditText editEmailPwd;
    private Button btnSubmit;
    private AppGameConfig  appGameConfig;

    private CompositeDisposable mCompositeDisposable;

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
        editEmailAccount = view.findViewById(R.id.edit_email_account);
        editEmailPwd = view.findViewById(R.id.edit_email_pwd);
        InputTextManager.with(this.getActivity())
                .addView(editEmailAccount)
                .addView(editEmailPwd)
                .setMain(btnSubmit)
                .build();
        initWebUrlSettings(view);
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
        TextView text_view1 = viewRoot.findViewById(R.id.text_view1);
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

        TextView text_view2 = viewRoot.findViewById(R.id.text_view2);
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

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.dialog_email_pwd_login;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_pwd_view || id == R.id.img_back) {
            backView();
        } else if (id == R.id.btn_submit) {
            PlayFunAuthUserEntity playFunAuthUserEntity = new PlayFunAuthUserEntity();
            playFunAuthUserEntity.setTypeLogin(4);
            ((LoginEmailMangerActivity) mActivity).setResultPoP(playFunAuthUserEntity);
        }
    }

    public void backView(){
        if(mActivity instanceof LoginEmailMangerActivity){
            ((LoginEmailMangerActivity) mActivity).showFragment(0);
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

}
