package com.fine.friendlycc.ui.login.register;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.databinding.FragmentRegisterBinding;
import com.fine.friendlycc.entity.ChooseAreaItemEntity;
import com.fine.friendlycc.entity.OverseasUserEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.login.LoginViewModel;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.StringUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author wulei
 */
public class RegisterFragment extends BaseToolbarFragment<FragmentRegisterBinding, LoginViewModel> {

    public Integer Google_Code = 101;
    CallbackManager callbackManager;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    private LoginManager loginManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBarUtils.setupStatusBar(this, true, true);
        return view;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_register;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //done 验证码框获取焦点
        viewModel.getCodeSuccess.observe(this,s -> {
            showInput(binding.etCode);
        });
        viewModel.setAreaSuccess.observe(this,s -> {
            if (TextUtils.isEmpty(viewModel.mobile.get()))
            showInput(binding.etPhone);
        });

    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //faceBook登录管理
        loginManager = LoginManager.getInstance();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        AppConfig.overseasUserEntity = null;
        showInput(binding.etPhone);
        ChooseAreaItemEntity areaCodeInfo = getAreaCodeInfo();
        if (areaCodeInfo == null){
            viewModel.getUserIpCode();
        }else {
            viewModel.areaCode.set(areaCodeInfo);
        }
    }

    private ChooseAreaItemEntity getAreaCodeInfo() {
        String areaCode = ConfigManager.getInstance().getAppRepository().readKeyValue("areaCode");
        if (StringUtil.isEmpty(areaCode)) {
            return null;
        }
        try {
            return new Gson().fromJson(areaCode, ChooseAreaItemEntity.class);
        }catch (Exception e){

        }
        return null;
    }


    //done 弹出键盘
    private void showInput(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn && loginManager != null) {
            loginManager.logOut();
            //viewModel.authLogin(accessToken.getUserId(), "facebook", null, null, null);
        }
        /**
         * 谷歌退出登录
         */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null && googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        binding.facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collection<String> collection = new ArrayList<String>();
                collection.add("email");
                loginManager.logIn(RegisterFragment.this, collection);
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                try {
                                    OverseasUserEntity overseasUserEntity = new OverseasUserEntity();
                                    if(!jsonObject.isNull("email")){
                                        overseasUserEntity.setEmail(jsonObject.getString("email"));
                                    }
                                    String token_for_business = null;
                                    if(!jsonObject.isNull("token_for_business")){
                                        token_for_business = jsonObject.getString("token_for_business");
                                    }

                                    Profile profile = Profile.getCurrentProfile();
                                    String phoneUrl = null;
                                    if (profile != null) {
                                        overseasUserEntity.setName(profile.getName());
                                        Uri uriFacebook = profile.getProfilePictureUri(500, 500);
                                        if(uriFacebook!=null){
                                            phoneUrl = uriFacebook.toString();
                                        }
                                    }
                                    overseasUserEntity.setPhoto(phoneUrl);
                                    AppConfig.overseasUserEntity = overseasUserEntity;
                                    viewModel.authLogin(loginResult.getAccessToken().getUserId(), "facebook", overseasUserEntity.getEmail(), null, null, token_for_business);
                                    AppContext.instance().logEvent(AppsFlyerEvent.LOG_IN_WITH_FACEBOOK);
                                } catch (Exception e) {
                                    Log.e("获取facebook关键资料", "异常原因: " + e.getMessage());
                                    // App code
                                    ToastUtils.showShort(R.string.playcc_error_facebook);
                                }
                            }
                        });
                        Bundle paramters = new Bundle();
                        paramters.putString("fields", "token_for_business");
                        request.setParameters(paramters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e("FaceBook登录", "取消登录");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        ToastUtils.showShort(R.string.playcc_error_facebook);
                    }
                });
        GoogleLogin();
        binding.googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, Google_Code);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Google_Code) {
            Task<GoogleSignInAccount> signedInAccountFromIntent = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(signedInAccountFromIntent);

        }
    }

    private void GoogleLogin() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void handleResult(Task<GoogleSignInAccount> googleData) {
        try {
            GoogleSignInAccount signInAccount = googleData.getResult(ApiException.class);
            if (signInAccount != null) {
                OverseasUserEntity overseasUserEntity = new OverseasUserEntity();
                overseasUserEntity.setEmail(signInAccount.getEmail());
                overseasUserEntity.setName(signInAccount.getDisplayName());
                overseasUserEntity.setPhoto(signInAccount.getPhotoUrl() == null ? null : String.valueOf(signInAccount.getPhotoUrl()));
                AppConfig.overseasUserEntity = overseasUserEntity;
                viewModel.authLogin(signInAccount.getId(), "google", overseasUserEntity.getEmail(), null, null, null);
                AppContext.instance().logEvent(AppsFlyerEvent.LOG_IN_WITH_GOOGLE);
            } else {
                Log.e("account", "si为空:" + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(R.string.playcc_error_google);
        }

    }

}
