package com.fine.friendlycc.ui.mine.setting.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.databinding.FragmentSettingAccountBinding;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.ui.login.GoogleApiError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @ClassName CommunityAccount
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/29 10:31
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class CommunityAccountFragment extends BaseToolbarFragment<FragmentSettingAccountBinding,CommunityAccountModel> {

    public Integer Google_Code = 101;
    CallbackManager callbackManager;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    private LoginManager loginManager;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_setting_account;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CommunityAccountModel initViewModel() {
        //faceBook登录管理
        loginManager = LoginManager.getInstance();
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(CommunityAccountModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
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
                loginManager.logIn(CommunityAccountFragment.this, collection);
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String userId = loginResult.getAccessToken().getUserId();
                        viewModel.bindAccount(userId, "facebook");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e("FaceBook登录", "取消登录");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        ToastUtils.showShort(R.string.playfun_error_facebook);
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
                viewModel.bindAccount(signInAccount.getId(), "google");
            } else {
                ToastUtils.showShort(R.string.playfun_error_google);
            }

        } catch (ApiException e) {
            e.printStackTrace();
            GoogleApiError.toastError(e.getStatusCode());
        }

    }
}