package com.fine.friendlycc.ui.task;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.entity.TaskConfigItemEntity;
import com.fine.friendlycc.event.MainTabEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.certification.certificationfemale.CertificationFemaleFragment;
import com.fine.friendlycc.ui.certification.certificationmale.CertificationMaleFragment;
import com.fine.friendlycc.ui.mine.audio.TapeAudioFragment;
import com.fine.friendlycc.ui.mine.myphotoalbum.MyPhotoAlbumFragment;
import com.fine.friendlycc.ui.mine.profile.EditProfileFragment;
import com.fine.friendlycc.ui.radio.issuanceprogram.IssuanceProgramFragment;
import com.fine.friendlycc.utils.ExceptionReportUtils;

import org.jetbrains.annotations.NotNull;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;

/**
 * Author: 彭石林
 * Time: 2021/8/9 15:32
 * Description: This is TaskCenterItemViewModel
 */
public class TaskCenterItemViewModel extends MultiItemViewModel<TaskCenterViewModel> {
    //字体改变
    public ObservableField<Boolean> extTypeDinBold = new ObservableField<>(true);
    public ObservableField<TaskConfigItemEntity> itemEntity = new ObservableField<>();
    public TaskCenterViewModel taskCenterViewModel;
    public int type;
    public BindingCommand toFragment = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!ObjectUtils.isEmpty(itemEntity.get().getLink())) {
                String link = itemEntity.get().getLink();
                if (link.trim().equals("Certification")) {//跳转认证中心
                    AppContext.instance().logEvent(AppsFlyerEvent.Verify_Your_Profile);
                    if (ConfigManager.getInstance().isMale()) {
                        taskCenterViewModel.start(CertificationMaleFragment.class.getCanonicalName());
                        return;
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_auth_F);
                        AppContext.instance().logEvent(AppsFlyerEvent.task_answer);
                        taskCenterViewModel.start(CertificationFemaleFragment.class.getCanonicalName());
                        return;
                    }
                } else if (link.trim().equals("EditProfile")) {//完善个人资料
                    if (isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_improve_data_M);
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_improve_data_F);
                    }
                    taskCenterViewModel.start(EditProfileFragment.class.getCanonicalName());
                } else if (link.trim().equals("PublishDynamic")) {// 發佈一條圖文動態
                    if (!isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_post_mood_date_F);
                    }
                    taskCenterViewModel.start(IssuanceProgramFragment.class.getCanonicalName());
                } else if (link.trim().equals("IssuanceProgram") || link.trim().equals("broadcast")) {// 跳转廣場-報名約會1次,點讚動態1次，評論動態1次
                    if (isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_signup_com_like_M);
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_commentary_F);
                    }
                    RxBus.getDefault().post(new MainTabEvent("plaza"));
                } else if (link.trim().equals("MyPhotoAlbum")) {// 上傳本人三張照片-發佈相冊1張
                    if (isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_uploading_image_M);
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_upload_image_snap_F);
                    }
                    taskCenterViewModel.start(MyPhotoAlbumFragment.class.getCanonicalName());
                } else if (link.trim().equals("home")) {// 跳转首页-主動搭訕3人
                    if (isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_accost_M);
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_accost_F);
                    }
                    RxBus.getDefault().post(new MainTabEvent("home"));
                } else if (link.trim().equals("message")) {// 跳转消息中心-視頻電話1次，語音聊天1次
                    if (isMale) {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_voice_video_call_M);
                    } else {
                        AppContext.instance().logEvent(AppsFlyerEvent.task_voice_video_call_F);
                    }
                    RxBus.getDefault().post(new MainTabEvent("message"));
                } else if (link.trim().equals("invite")) {//男-女 邀请
                    try {
                        viewModel.touserInviteWeb();
                    } catch (Exception e) {
                        ExceptionReportUtils.report(e);
                    }
                } else if (link.trim().equals("userSound")) {
                    taskCenterViewModel.start(TapeAudioFragment.class.getCanonicalName());
                }
            }
        }
    });
    public BindingCommand ToaskSubBonus = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            int dex = 0;
            //拿到position
            dex = taskCenterViewModel.daily_task_observableList.indexOf(TaskCenterItemViewModel.this);
            if(dex!=-1){
                taskCenterViewModel.ToaskSubBonus(itemEntity.get().getSulg(), type, dex);
            }
        }
    });
    private final boolean isMale;

    public TaskCenterItemViewModel(@NonNull @NotNull TaskCenterViewModel viewModel, TaskConfigItemEntity taskConfigItemEntity, int type) {
        super(viewModel);
        this.taskCenterViewModel = viewModel;
        this.itemEntity.set(taskConfigItemEntity);
        this.type = type;
        this.isMale = ConfigManager.getInstance().isMale();
    }

    public boolean newsTitleAnimate() {
        int status = itemEntity.get().getStatus();
        if (status != 2) {
            return type == 0;
        } else {
            return false;
        }
    }

    public int newsTitleShow() {
        int status = itemEntity.get().getStatus();
        if (status != 2) {
            if (type == 0) {
                return View.VISIBLE;
            }
            return View.GONE;
        } else {
            return View.GONE;
        }
    }

    public int isShowRemark() {
        if (ObjectUtils.isEmpty(itemEntity.get().getTotalNumber())) {
            return View.GONE;
        }
        return View.VISIBLE;
    }

    public String getRemark() {
        String data = StringUtils.getString(R.string.save);
        return data + " " + itemEntity.get().getFinishNumber() + "/" + itemEntity.get().getTotalNumber();
    }

    public String getRightTitle() {
        String key = itemEntity.get().getName();
        int status = itemEntity.get().getStatus();
        if (status == 1) {
            return StringUtils.getString(R.string.playfun_receive);
        } else if (status == 2) {
            if (type == 1) {
                return StringUtils.getString(R.string.task_fragment_sign_ed);
            }
            return StringUtils.getString(R.string.playfun_received);
        } else {
            if (key.equals("invite")) {//男-女 邀请
                return StringUtils.getString(R.string.task_fragment_task_new3);
            }
            if (ConfigManager.getInstance().isMale()) {
                return "+" + Double.valueOf(itemEntity.get().getRewardValue()).intValue();
            } else {
                return "+" + itemEntity.get().getRewardValue();
            }

        }
    }

    //显示卡、台币
    public Boolean cardViewShow() {
        int status = itemEntity.get().getStatus();
        int reward_type = itemEntity.get().getRewardType();
        if (status != 1 && status != 2) {
            //台币
            return reward_type != 1;
        }
        return false;
    }

    public Drawable cardViewShowImg() {
        int reward_type = itemEntity.get().getRewardType();
        //2聊天卡 3语音卡 4视频卡 5搭讪卡
        if (reward_type == 2) {
            return Utils.getApp().getDrawable(R.drawable.task_item_img3);
        } else if (reward_type == 3) {
            return Utils.getApp().getDrawable(R.drawable.task_item_img5);
        } else if (reward_type == 4) {
            return Utils.getApp().getDrawable(R.drawable.task_item_img2);
        } else if (reward_type == 5) {
            return Utils.getApp().getDrawable(R.drawable.task_item_img4);
        } else {
            return null;
        }
    }
}
