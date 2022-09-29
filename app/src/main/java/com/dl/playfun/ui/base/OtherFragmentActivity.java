package com.dl.playfun.ui.base;

import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dl.playfun.R;
import com.dl.playfun.viewmodel.BaseViewModel;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/24 14:06
 */
public class OtherFragmentActivity extends MySupportActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other_fragment);
        //初始化
        String className = getIntent().getExtras().getString(BaseViewModel.ParameterField.FRAGMENT_NAME);
        if (className == null || "".equals(className)) {
            throw new IllegalArgumentException("can not find page fragmentName");
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        try {
            Class<?> fragClass = Class.forName(className);//反射动态获取类
            Object obj = fragClass.newInstance();
            Fragment fragment = (Fragment) obj;//类型转换为Fragment
            //跳转
            transaction.replace(R.id.other_fragmentt, fragment);
            transaction.commit();
        } catch (Exception e) {
        }
    }
}


