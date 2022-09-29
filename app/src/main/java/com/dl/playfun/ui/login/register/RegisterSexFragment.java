package com.dl.playfun.ui.login.register;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.databinding.FragmentRegisterSexBinding;
import com.dl.playfun.ui.base.BaseFragment;
import com.dl.playfun.ui.mine.profile.PerfectProfileViewModel;
import com.dl.playfun.utils.AutoSizeUtils;
import com.dl.playfun.utils.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Author: 彭石林
 * Time: 2022/7/8 15:26
 * Description: This is RegisterSexFragment
 */
public class RegisterSexFragment extends BaseFragment<FragmentRegisterSexBinding, PerfectProfileViewModel>{
    private String avatar;
    private String name;
    private int currentAge = 18;
    private final String GIRL_HEAD = "images/avatar/girl.png";
    private final String BOY_HEAD = "images/avatar/boy.png";

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AutoSizeUtils.applyAdapt(getResources());
        return R.layout.fragment_register_sex;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public PerfectProfileViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(PerfectProfileViewModel.class);
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle bundle = getArguments();
        if (bundle != null) {
            avatar = bundle.getString("avatar");
            name = bundle.getString("name");
        }
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.UserName.set(name);
        viewModel.setInvitationCode();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.uc.clickChooseMale.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if (viewModel.UserSex.get() == null) {
                    ToastUtils.showShort(R.string.playfun_fragment_perfect_sex_hint2);
                }
                viewModel.UserSex.set(1);
                binding.maleIcon.setAlpha(1f);
                binding.maleIconCheck.setVisibility(View.VISIBLE);
                binding.femaleIcon.setAlpha(0.5f);
                binding.femaleIconCheck.setVisibility(View.GONE);
            }
        });
        viewModel.uc.clickChooseGirl.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if (viewModel.UserSex.get() == null) {
                    ToastUtils.showShort(R.string.playfun_fragment_perfect_sex_hint2);
                }
                viewModel.UserSex.set(0);
                binding.maleIcon.setAlpha(0.5f);
                binding.maleIconCheck.setVisibility(View.GONE);
                binding.femaleIcon.setAlpha(1f);
                binding.femaleIconCheck.setVisibility(View.VISIBLE);
            }
        });
        viewModel.uc.getClickBirthday.observe(this, o -> {
            Calendar calendar = Calendar.getInstance();
            int month = (calendar.get(Calendar.MONTH) + 1);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String months = month < 10 ? "0" + month : String.valueOf(month);
            String days = day < 10 ? "0" + day : String.valueOf(day);
            viewModel.UserBirthday.set((calendar.get(Calendar.YEAR) - currentAge) + "-" + months + "-" + days);

            if (StringUtil.isEmpty(avatar)){
                if (viewModel.UserSex.get() == 1){
                    viewModel.regUser(BOY_HEAD);
                }else {
                    viewModel.regUser(GIRL_HEAD);
                }
            }else {
                viewModel.saveAvatar(avatar);
            }
        });

        viewModel.uc.chooseAgeClick.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showChooseAge();
            }
        });
    }


    //选择年齡
    private void showChooseAge() {// 弹出选择器
        final List<String> options1Items = new ArrayList<>();
        int position = 0;
        int defAge = 18;
        String ageText = StringUtils.getString(R.string.playfun_mine_age);
        for (int i = defAge; i <= 100; i++) {
            String format = String.format(ageText, i);
            options1Items.add(format);
            if (viewModel.userAge.get().equals(format)) {
                position = i - defAge;
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //当前options1 第一个等于=0  默认+18岁
                currentAge = options1 + defAge;
                viewModel.userAge.set(options1Items.get(options1));
            }
        })
        .setCancelText(getString(R.string.playfun_cancel))//取消按钮文字
        .setSubmitText(getString(R.string.playfun_confirm))//确认按钮文字
        .setContentTextSize(14)//滚轮文字大小
        .setSubCalSize(14)
        .setTitleSize(14)//标题文字大小
        .setTitleText(getString(R.string.playfun_perfect_tips_age))//标题文字
        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
        .setTitleColor(getResources().getColor(R.color.gray_dark))//标题文字颜色
        .setSubmitColor(getResources().getColor(R.color.purple))//确定按钮文字颜色
        .setCancelColor(getResources().getColor(R.color.purple))//取消按钮文字颜色
        .setTextColorCenter(getResources().getColor(R.color.purple))//设置选中项的颜色
        .setTextColorOut(getResources().getColor(R.color.gray_light))//设置没有被选中项的颜色
        .setTitleBgColor(0xffffffff)//标题背景颜色 Night mode
        .setBgColor(0xffffffff)//滚轮背景颜色 Night mode
        .setItemVisibleCount(5)//设置最大可见数目
        .setLineSpacingMultiplier(2.8f)
        .setSelectOptions(position)  //设置默认选中项
        .isDialog(true)//f是否显示为对话框样式
        .build();
        pvOptions.setPicker(options1Items);//一级选择器

        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            params.width = getResources().getDisplayMetrics().widthPixels;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }

        pvOptions.show();
    }

    @Override
    public void onDestroy() {
        AutoSizeUtils.closeAdapt(getResources());
        super.onDestroy();
    }

}
