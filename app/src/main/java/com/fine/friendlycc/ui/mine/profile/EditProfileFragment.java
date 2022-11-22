package com.fine.friendlycc.ui.mine.profile;

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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.fine.friendlycc.app.CCApplication;
import com.fine.friendlycc.app.AppViewModelFactory;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.bean.OccupationConfigItemBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.ui.base.BaseToolbarFragment;
import com.fine.friendlycc.utils.DateUtil;
import com.fine.friendlycc.utils.ImmersionBarUtils;
import com.fine.friendlycc.utils.PictureSelectorUtil;
import com.fine.friendlycc.utils.Utils;
import com.fine.friendlycc.widget.dialog.MVDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.fine.friendlycc.BR;
import com.fine.friendlycc.R;
import com.fine.friendlycc.databinding.FragmentEditProfileBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wulei
 * 个人资料/个人信息
 */
public class EditProfileFragment extends BaseToolbarFragment<FragmentEditProfileBinding, EditProfileViewModel> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBarUtils.setupStatusBar(this, true, true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_edit_profile;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public EditProfileViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(EditProfileViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        CCApplication.instance().logEvent(AppsFlyerEvent.Edit_Profile);
    }

    private void clearNicknameFocus() {
        if (binding.edtNickname.isFocused()) {
            binding.edtNickname.clearFocus();
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.clickAvatar.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                chooseAvatar();
                clearNicknameFocus();
            }
        });
        viewModel.uc.clickBirthday.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                shouChooseBirthday();
                clearNicknameFocus();
            }
        });
        viewModel.uc.clickHeight.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                shouChooseHeight();
                clearNicknameFocus();
            }
        });
        viewModel.uc.clickOccupation.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                shouChooseOccupation();
                clearNicknameFocus();
            }
        });
        viewModel.uc.clickUploadingHead.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {

            }
        });
        viewModel.uc.clickWeight.observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                shouChooseWeight();
                clearNicknameFocus();
            }
        });
        viewModel.uc.showFlagClick.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                MVDialog.getInstance(EditProfileFragment.this.getContext())
                        .setTitele(getString(R.string.playcc_user_proflt))
                        .setContent(getString(R.string.playcc_user_proflt1))
                        .setConfirmText(getString(R.string.playcc_dialog_set_withdraw_account_confirm))
                        .setConfirmOnlick(dialog -> {
                            pop();
                        })
                        .setNotClose(true)
                        .chooseType(MVDialog.TypeEnum.CENTER)
                        .show();
            }
        });
    }

    private void chooseAvatar() {
        if (viewModel.userDataEntity.get() == null) {
            return;
        }
        PictureSelectorUtil.selectImageAndCrop(mActivity, true, 1, 1, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                viewModel.saveAvatar(result.get(0).getCutPath());
            }

            @Override
            public void onCancel() {
            }
        });
    }


    //选择职业dialog
    public void shouChooseOccupation() {
        if (viewModel.userDataEntity.get() == null) {
            return;
        }
//        MVDialog.ChooseOccupation chooseOccupation = new MVDialog.ChooseOccupation() {
//            @Override
//            public void clickListItem(Dialog dialog, OccupationConfigItemBean item) {
//                viewModel.userDataEntity.get().setOccupationId(item.getId());
//            }
//        };
//        MVDialog.getOccupationDialog(this.getContext(), viewModel.occupation, viewModel.userDataEntity.get().getOccupationId(), chooseOccupation);
        MVDialog.ChooseOccupation chooseOccupation = new MVDialog.ChooseOccupation() {
            @Override
            public void clickListItem(Dialog dialog, OccupationConfigItemBean.ItemEntity item) {
                viewModel.userDataEntity.get().setOccupationId(item.getId());

            }

//            @Override
//            public void clickListItem(Dialog dialog, OccupationConfigItemBean item) {
//                viewModel.userDataEntity.get().setOccupationId(item.getId());
//            }

//            @Override
//            public void clickListItem(Dialog dialog, OccupationConfigItemBean.ItemEntity item) {
//                viewModel.userDataEntity.get().setOccupationId(item.getId());
//            }
        };
        MVDialog.getOccupationDialog(this.getContext(), viewModel.occupation, viewModel.userDataEntity.get().getOccupationId() == null ? -1 : viewModel.userDataEntity.get().getOccupationId(), chooseOccupation);
    }

    //选择生日dialog
    public void shouChooseBirthday() {
        if (viewModel.userDataEntity.get() == null) {
            return;
        }
        Calendar selectedDate = Calendar.getInstance();
        if (viewModel.userDataEntity.get().getBirthday() != null) {
            try {
                selectedDate.setTime(Utils.formatday.parse(viewModel.userDataEntity.get().getBirthday()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar startDate = Calendar.getInstance();
        startDate.set(1931, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(DateUtil.getYear() - 18, DateUtil.getMonth() - 1, DateUtil.getCurrentMonthDay());
        TimePickerView pvTime = new TimePickerBuilder(this.getContext(), (date, v) -> {
            UserDataBean userEntity = viewModel.userDataEntity.get();
            userEntity.setBirthday(Utils.formatday.format(date));
            viewModel.userDataEntity.set(userEntity);
            viewModel.userDataEntity.notifyChange();
        })
                .setType(new boolean[]{true, true, true, false, false, false})//分别对应年月日时分秒，默认全部显示
                .setCancelText(getString(R.string.playcc_cancel))//取消按钮文字
                .setSubmitText(getString(R.string.playcc_confirm))//确认按钮文字
                .setContentTextSize(14)//滚轮文字大小
                .setSubCalSize(14)
                .setTitleSize(14)//标题文字大小
                .setTitleText(getString(R.string.playcc_fragment_edit_profile_brithday))//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(getResources().getColor(R.color.gray_dark))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.purple))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.purple))//取消按钮文字颜色
                .setTextColorCenter(getResources().getColor(R.color.purple))//设置选中项的颜色
                .setTextColorOut(getResources().getColor(R.color.gray_light))//设置没有被选中项的颜色
                .setTitleBgColor(0xffffffff)//标题背景颜色 Night mode
                .setBgColor(0xffffffff)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setItemVisibleCount(5)//设置最大可见数目
                .setDividerType(WheelView.DividerType.WRAP)
                .setLineSpacingMultiplier(2.8f)
                .setLabel("", "", "", "", "", "")
                .isDialog(true)//f是否显示为对话框样式
                .build();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        pvTime.show();
    }

    //选择身高dialog
    private void shouChooseHeight() {// 弹出选择器
        if (viewModel.userDataEntity.get() == null) {
            return;
        }
        final List<String> options1Items = new ArrayList<>();
        int posion = 0;
        for (int i = 0; i < viewModel.height.size(); i++) {
            options1Items.add(viewModel.height.get(i).getName());
            if (viewModel.userDataEntity.get().getHeight().intValue() == viewModel.height.get(i).getId().intValue()) {
                posion = i;
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                viewModel.userDataEntity.get().setHeight(viewModel.height.get(options1).getId());
            }
        })
                .setCancelText(getString(R.string.playcc_cancel))//取消按钮文字
                .setSubmitText(getString(R.string.playcc_confirm))//确认按钮文字
                .setContentTextSize(14)//滚轮文字大小
                .setSubCalSize(14)
                .setTitleSize(14)//标题文字大小
                .setTitleText(getString(R.string.playcc_height))//标题文字
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
                .setSelectOptions(posion)  //设置默认选中项
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

    //选择体重dialog
    private void shouChooseWeight() {// 弹出选择器
        if (viewModel.userDataEntity.get() == null) {
            return;
        }
        final List<String> options1Items = new ArrayList<>();
        int posion = 0;
        for (int i = 0; i < viewModel.weight.size(); i++) {
            options1Items.add(viewModel.weight.get(i).getName());
            if (viewModel.userDataEntity.get().getWeight().intValue() == viewModel.weight.get(i).getId().intValue()) {
                posion = i;
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                viewModel.userDataEntity.get().setWeight(viewModel.weight.get(options1).getId());
            }
        })
                .setCancelText(getString(R.string.playcc_cancel))//取消按钮文字
                .setSubmitText(getString(R.string.playcc_confirm))//确认按钮文字
                .setContentTextSize(14)//滚轮文字大小
                .setSubCalSize(14)
                .setTitleSize(14)//标题文字大小
                .setTitleText(getString(R.string.playcc_weight))//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(getResources().getColor(R.color.gray_dark))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.purple))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.purple))//取消按钮文字颜色
                .setTextColorCenter(getResources().getColor(R.color.purple))//设置选中项的颜色
                .setTextColorOut(getResources().getColor(R.color.gray_light))//设置没有被选中项的颜色
                .setItemVisibleCount(5)//设置最大可见数目
                .setLineSpacingMultiplier(2.8f)
                .setSelectOptions(posion)  //设置默认选中项
                .isDialog(true)//f是否显示为对话框样式
                .build();

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

        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.show();
    }
}