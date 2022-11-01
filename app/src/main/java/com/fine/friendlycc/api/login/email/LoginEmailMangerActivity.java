package com.fine.friendlycc.api.login.email;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.api.PlayFunAuthUserEntity;
import com.fine.friendlycc.ui.WebUrlViewActivity;
import com.fine.friendlycc.utils.AutoSizeUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Author: 彭石林
 * Time: 2022/4/28 15:05
 * Description: This is LoginEmailMangerAvtivity
 */
public class LoginEmailMangerActivity extends AppCompatActivity {

    //加载进度条
    private KProgressHUD hud;

    private Fragment[] mFragments = new Fragment[2];
    private int curIndex;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoSizeUtils.applyAdapt(this.getResources());
        this.setContentView(R.layout.actitvity_email_manger_dialog);
        if (savedInstanceState != null) {
            curIndex = savedInstanceState.getInt("curIndex");
        }
        if(FragmentUtils.findFragment(getSupportFragmentManager(), LoginEmailViewFragment.class)!=null){
            mFragments[0] = FragmentUtils.findFragment(getSupportFragmentManager(), LoginEmailViewFragment.class);
        }else{
            mFragments[0] = (new LoginEmailViewFragment());
        }
        if(FragmentUtils.findFragment(getSupportFragmentManager(), LoginEmailPwdViewFragment.class)!=null){
            mFragments[1] = FragmentUtils.findFragment(getSupportFragmentManager(), LoginEmailPwdViewFragment.class);
        }else{
            mFragments[1] = (new LoginEmailPwdViewFragment());
        }
            FragmentUtils.add(getSupportFragmentManager(), mFragments, R.id.fl_container, curIndex);
    }

    public void showFragment(int idx){
        FragmentUtils.showHide(idx,mFragments);
    }

    public void setResultPoP(PlayFunAuthUserEntity playFunAuthUserEntity){
        Intent intent = getIntent();
        intent.putExtra("authUser", playFunAuthUserEntity);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
    * @Desc TODO(跳转webview页面)
    * @author 彭石林
    * @parame [webUrl]
    * @return void
    * @Date 2022/4/29
    */
    public void startWebActivity(String webUrl){
        Intent intent = new Intent(this, WebUrlViewActivity.class);
        intent.putExtra("arg_web_url", webUrl);
        startActivity(intent);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("curIndex", curIndex);
    }

    @Override
    public void onBackPressed() {
        if(FragmentUtils.getTopShow(getSupportFragmentManager()) instanceof LoginEmailPwdViewFragment){
            showFragment(0);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy(){
        FragmentUtils.removeAll(getSupportFragmentManager());
        super.onDestroy();
    }

    void showHUD(){
        if (hud == null) {
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(com.fine.friendlycc.R.color.white), PorterDuff.Mode.SRC_IN);

            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setBackgroundColor(getResources().getColor(com.fine.friendlycc.R.color.hud_background))
                    .setLabel(null)
                    .setCustomView(progressBar)
                    .setSize(100, 100)
                    .setCancellable(false);
        }
        hud.show();
    }

    void dismissHud() {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }
}
