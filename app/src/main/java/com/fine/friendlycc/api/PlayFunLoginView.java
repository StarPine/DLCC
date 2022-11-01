package com.fine.friendlycc.api;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.WebUrlViewActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Author: 彭石林
 * Time: 2022/3/29 15:01
 * Description: This is PlayFunLoginView
 */
public class PlayFunLoginView extends DialogFragment implements Consumer<Disposable> {
    private CompositeDisposable mCompositeDisposable;
    //加载进度条
    private KProgressHUD hud;
    private Context mContext;
    private Activity mActivity;
    //谷歌登录
    public Integer Google_Code = 101;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    //facebook登录
    CallbackManager callbackManager;
    private LoginManager loginManager;

    private AuthLoginResultListener loginResultListener= null;

    private AppGameConfig appGameConfig = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置窗体背景色透明
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置宽高
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            //透明度
            layoutParams.dimAmount = 0.6f;
            //位置
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化适配--防止部分机型失效
        AutoSizeUtils.applyAdapt(this.getResources());
        View view = inflater.inflate(com.fine.friendlycc.R.layout.dialog_login_fragment, container, false);
        init(view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //去除Dialog默认头部
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
                {
                   return true;
                }
                return false;
            }
        });
        dialog.getWindow().setWindowAnimations(com.fine.friendlycc.R.style.BottomDialog_Animation);
    }

    @SuppressLint("WrongConstant")
    private void init(View viewRoot){
        mContext = viewRoot.getContext();
        //获取权限
        getPermission();
        //初始化Google Facebook
        initGoogleFaceBookLogin();
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
        ImageView faceBookLoginButton = viewRoot.findViewById(com.fine.friendlycc.R.id.facebook_login_button);
        ImageView googleLoginButton = viewRoot.findViewById(com.fine.friendlycc.R.id.google_login_button);
        CheckBox loginCheckBox = viewRoot.findViewById(com.fine.friendlycc.R.id.login_check);
        TextView text_view = viewRoot.findViewById(com.fine.friendlycc.R.id.text_view);
        text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheckBox.setChecked(true);
            }
        });
        faceBookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loginCheckBox.isChecked()) {
                    ToastUtils.showShort(com.fine.friendlycc.R.string.playfun_warn_agree_terms);
                    return;
                }
                Collection<String> collection = new ArrayList<String>();
                collection.add("email");
                loginManager.logIn(PlayFunLoginView.this, collection);
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String userId = loginResult.getAccessToken().getUserId();

//                        facebook登录
                        //viewModel.authLogin(userId, "facebook", null, null, null);
                       // authLoginPost(userId,"facebook");
                        AppContext.instance().logEvent(AppsFlyerEvent.LOG_IN_WITH_FACEBOOK);
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            Log.e("当前facebook头像地址",profile.getProfilePictureUri(500,500).toString());
                        }
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                try {

                                    PlayFunAuthUserEntity playFunAuthUserEntity = new PlayFunAuthUserEntity();
                                    playFunAuthUserEntity.setAuthAccessToken(loginResult.getAccessToken().getToken());
                                    playFunAuthUserEntity.setAuthTokenUserId(loginResult.getAccessToken().getUserId());
                                    playFunAuthUserEntity.setAuthEmail(jsonObject.getString("email"));
                                    playFunAuthUserEntity.setAuthName(jsonObject.getString("name"));
                                    Log.e("拿到的facebook性别",jsonObject.getString("gender"));
                                    Profile profile = Profile.getCurrentProfile();
                                    String phoneUrl = null;
                                    if (profile != null) {
                                        phoneUrl = profile.getProfilePictureUri(500,500).toString();
                                    }
                                    playFunAuthUserEntity.setAuthPhone(phoneUrl);
                                    playFunAuthUserEntity.setTypeLogin(1);
                                    authLoginPost(playFunAuthUserEntity.getAuthTokenUserId(),"facebook",playFunAuthUserEntity);
                                } catch (Exception e) {
                                    Log.e("获取facebook关键资料", "异常原因: " + e.getMessage());
                                    // App code
                                    ToastUtils.showShort(R.string.playfun_error_facebook);
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
                        Log.e("FaceBook登入异常返回:", exception.getMessage());
                        loginResultListener.authLoginError(-1,1,exception.getMessage());
                        // App code
                        ToastUtils.showShort(com.fine.friendlycc.R.string.playfun_error_facebook);
                    }
                });
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginCheckBox.isChecked()) {
                    ToastUtils.showShort(com.fine.friendlycc.R.string.playfun_warn_agree_terms);
                    return;
                }
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, Google_Code);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == Google_Code) {
            Task<GoogleSignInAccount> signedInAccountFromIntent = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(signedInAccountFromIntent);
        }
    }

    private void handleResult(Task<GoogleSignInAccount> googleData) {
        try {
            GoogleSignInAccount signInAccount = googleData.getResult(ApiException.class);
            if (signInAccount != null) {
                PlayFunAuthUserEntity playFunAuthUserEntity = new PlayFunAuthUserEntity();
                playFunAuthUserEntity.setAuthAccessToken(null);
                playFunAuthUserEntity.setAuthTokenUserId(signInAccount.getId());
                playFunAuthUserEntity.setAuthEmail(signInAccount.getEmail());
                playFunAuthUserEntity.setAuthName(signInAccount.getDisplayName());
                playFunAuthUserEntity.setAuthPhone(signInAccount.getPhotoUrl()==null?null:String.valueOf(signInAccount.getPhotoUrl()));
                playFunAuthUserEntity.setTypeLogin(2);
                authLoginPost(playFunAuthUserEntity.getAuthTokenUserId(),"google",playFunAuthUserEntity);
                //谷歌登录成功回调
            } else {
                Log.e("account", "si" + "\n");
            }

        } catch (ApiException e) {
            String errorMessage = StringUtils.getString(R.string.playfun_error_google);
            switch (e.getStatusCode()) {
                case 2:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_2);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_2);
                    break;
                case 3:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_3);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_3);
                    break;
                case 4:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_4);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_4);
                    break;
                case 5:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_5);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_5);
                    break;
                case 6:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_6);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_6);
                    break;
                case 7:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_7);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_7);
                    break;
                case 8:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_8);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_8);
                    break;
                case 13:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_13);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_13);
                    break;
                case 14:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_14);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_14);
                    break;
                case 15:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_15);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_15);
                    break;
                case 16:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_16);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_16);
                    break;
                case 17:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_17);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_17);
                    break;
                case 20:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_20);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_20);
                    break;
                case 21:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_21);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_21);
                    break;
                case 22:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_22);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_22);
                    break;
                case 12500:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google_12500);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google_12500);
                    break;
                default:
                    ToastUtils.showLong(com.fine.friendlycc.R.string.playfun_error_google);
                    errorMessage = StringUtils.getString(R.string.playfun_error_google);
                    break;
            }
            loginResultListener.authLoginError(e.getStatusCode(),2,errorMessage);
        }

    }

    /**
    * @Desc TODO(获取权限)
    * @author 彭石林
    * @parame []
    * @return void
    * @Date 2022/3/29
    */
    @SuppressLint("WrongConstant")
    void getPermission(){
        try {
            PermissionUtils.permission(Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                }
            }).request();
        } catch (Exception e) {//防止不分机型因为权限获取系统不支持

        }
    }

    //初始化谷歌、facebook登录
    private void initGoogleFaceBookLogin() {
        mCompositeDisposable = new CompositeDisposable();
        //faceBook登录管理
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn && loginManager != null) {
            loginManager.logOut();
        }
        /**
         * 谷歌退出登录
         */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null && googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
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

    //第三方登录
    public void authLoginPost(String authId,String type,PlayFunAuthUserEntity playFunAuthUserEntity){
        ConfigManager.getInstance().getAppRepository().authLoginPost(authId,type)
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
                            playFunAuthUserEntity.setToken(authLoginUserEntity.getToken());
                            playFunAuthUserEntity.setUserID(authLoginUserEntity.getUserID());
                            playFunAuthUserEntity.setUserSig(authLoginUserEntity.getUserSig());
                            playFunAuthUserEntity.setIsContract(authLoginUserEntity.getIsContract());
                            playFunAuthUserEntity.setIsNewUser(authLoginUserEntity.getIsNewUser());
                            playFunAuthUserEntity.setIsBindGame(authLoginUserEntity.getIsBindGame());

                            AppContext.instance().mFirebaseAnalytics.setUserId(String.valueOf(authLoginUserEntity.getId()));
                            ConfigManager.getInstance().getAppRepository().saveUserData(authLoginUserEntity);
                            if (authLoginUserEntity.getCertification() == 1) {
                                ConfigManager.getInstance().getAppRepository().saveNeedVerifyFace(true);
                            }
                            if(loginResultListener!=null){
                                loginResultListener.authLoginSuccess(playFunAuthUserEntity);
                            }
                        }

                    }
                    @Override
                    public void onComplete() {
                        dismissHud();
                    }
                });
    }

    public void setLoginResultListener(AuthLoginResultListener loginResultListener){
        this.loginResultListener = loginResultListener;
    }
    private void showHUD(){

        if (hud == null) {
            ProgressBar progressBar = new ProgressBar(getContext());
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(com.fine.friendlycc.R.color.white), PorterDuff.Mode.SRC_IN);

            hud = KProgressHUD.create(mActivity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setBackgroundColor(getResources().getColor(com.fine.friendlycc.R.color.hud_background))
                    .setLabel(null)
                    .setCustomView(progressBar)
                    .setSize(100, 100)
                    .setCancellable(false);
        }
        hud.show();
    }

    public void dismissHud() {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }

    @Override
    public void dismiss() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.dismiss();
    }

    public Context getContext(){
        return mContext;
    }
}
