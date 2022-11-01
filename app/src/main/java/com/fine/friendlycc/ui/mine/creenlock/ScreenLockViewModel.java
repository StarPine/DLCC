package com.fine.friendlycc.ui.mine.creenlock;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.viewmodel.BaseViewModel;
import com.fine.friendlycc.R;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

import static com.blankj.utilcode.util.ColorUtils.getColor;

public class ScreenLockViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<String> passwordExplain = new ObservableField<>("");
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<Boolean> isExplainVisibility = new ObservableField<>(false);
    public ObservableField<Integer> colorval = new ObservableField<>(getColor(R.color.gray_middle));
    //退出
    public BindingCommand cancelClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pop();
        }
    });
    UIChangeObservable uc = new UIChangeObservable();
    private String password;
    private boolean isUpdata = false;

    public ScreenLockViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);

    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        password = model.readPassword();
        if (StringUtil.isEmpty(password)) {
            isUpdata = false;
            title.set(StringUtils.getString(R.string.playfun_setting_screen_lock));
        } else {
            isUpdata = true;
            title.set(StringUtils.getString(R.string.playfun_update_screen_lock));
        }
    }

    public void setPassword(String str) {
        if (StringUtil.isEmpty(str)) {
            return;
        }
        if (!StringUtil.isEmpty(str)) {
            if (str.length() < 4) {
                isExplainVisibility.set(true);
                passwordExplain.set(StringUtils.getString(R.string.playfun_least_fount_points));
            } else {
                if (StringUtil.isEmpty(password)) {
                    password = str;
                    isExplainVisibility.set(true);
                    passwordExplain.set(StringUtils.getString(R.string.playfun_please_again_draw));
                } else {
                    if (password.equals(str)) {
                        if (isUpdata) {
                            uc.bindupdata.call();
                        } else {
                            isExplainVisibility.set(true);
                            colorval.set(getColor(R.color.green));
                            passwordExplain.set(StringUtils.getString(R.string.playfun_setting_success));
                            model.savePassword(str);
                            CountDownTimer timer = new CountDownTimer(1000, 100) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    pop();
                                }
                            }.start();
                        }
                    } else {
                        if (isUpdata) {
                            isExplainVisibility.set(true);
                            colorval.set(getColor(R.color.red_7c));
                            passwordExplain.set(StringUtils.getString(R.string.playfun_draw_wrong_again_draw));
                            CountDownTimer timer = new CountDownTimer(500, 100) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    isExplainVisibility.set(false);
                                }
                            }.start();
                        } else {
                            password = "";
                            isExplainVisibility.set(true);
                            colorval.set(getColor(R.color.red_7c));
                            passwordExplain.set(StringUtils.getString(R.string.playfun_two_draw_different));
                        }
                    }
                }

            }
        }
    }

    public void setNewPassword() {
        password = "";
        title.set(StringUtils.getString(R.string.playfun_setting_screen_lock));
        isUpdata = false;
    }

    public void cancelPassword() {
        model.savePassword("");
        pop();
    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> bindupdata = new SingleLiveEvent<>();
    }
}