package com.fine.friendlycc.ui.home.accost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.app.AppsFlyerEvent;
import com.fine.friendlycc.data.source.http.exception.RequestException;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AccostEntity;
import com.fine.friendlycc.entity.AccostItemEntity;
import com.fine.friendlycc.event.LoadEvent;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.ui.base.BaseDialog;
import com.fine.friendlycc.utils.StringUtil;
import com.fine.friendlycc.utils.TimeUtils;
import com.fine.friendlycc.utils.Utils;
import com.fine.friendlycc.widget.image.CircleImageView;
import com.fine.friendlycc.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * Author: 彭石林
 * Time: 2021/12/1 10:40
 * Description: This is HomeAccostDialog
 */
public class HomeAccostDialog extends BaseDialog {
    //搭讪倒计时
    public static CountDownTimer downTimer = null;
    //搭讪倒计时
    public static CountDownTimer downChangeTimer = null;
    private int page = 1;
    private final Context mContext;
    List<AccostItemEntity> $listData;
    private boolean submit = false;
    private boolean isCountdown = false;
    //搭讪冷却时间
    private String expirationTime;
    //服务器当前时间
    private String dateTime;
    //是否有换一批冷却时间
    private String changeDownTime = null;
    private boolean isChangeDownTime = false;
    private long dowDownChangeTime;
    private int changeTime = 0;
    private long downTime;
    private View rootView;
    private CircleImageView item_entity_img1, item_entity_img2, item_entity_img3, item_entity_img4, item_entity_img5, item_entity_img6;
    private TextView item_entity_name1, item_entity_name2, item_entity_name3, item_entity_name4, item_entity_name5, item_entity_name6;
    private TextView item_entity_text1, item_entity_text2, item_entity_text3, item_entity_text4, item_entity_text5, item_entity_text6;
    private CheckBox item_entity_check1, item_entity_check2, item_entity_check3, item_entity_check4, item_entity_check5, item_entity_check6;
    private ImageView item_tag_img1, item_tag_img2, item_tag_img3, item_tag_img4, item_tag_img5, item_tag_img6;
    private LottieAnimationView item_lottie1, item_lottie2, item_lottie3, item_lottie4, item_lottie5, item_lottie6;

    private LinearLayout itemEntityLayout1, itemEntityLayout2, itemEntityLayout3, itemEntityLayout4, itemEntityLayout5, itemEntityLayout6;
    private ImageView iv_dialog_close;//关闭按钮
    private ImageView btn_submit;//提交按钮
    private TextView exp_time;//提示
    private DialogAccostClicksListener dialogAccostClicksListener;
    //换一批
    private LinearLayout refresh_layout;

    private TextView changeText;

    public HomeAccostDialog(Context context) {
        super(context);
        this.mContext = context;
        initView();
        getAccountList(false);
    }

    public void setDialogAccostClicksListener(DialogAccostClicksListener listener) {
        this.dialogAccostClicksListener = listener;
    }

    private void initView() {
        //读取本地冷却时间
        if(ConfigManager.getInstance().getAppRepository().readUserData() != null)
            changeDownTime = readKeyValue(ConfigManager.getInstance().getAppRepository().readUserData().getId() + "_homeAccost");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        rootView = inflater.inflate(R.layout.dialog_home_accost_list, null);
        exp_time = rootView.findViewById(R.id.exp_time);
        changeText = rootView.findViewById(R.id.change_text);
        itemEntityLayout1 = rootView.findViewById(R.id.item_entity_layout1);
        itemEntityLayout2 = rootView.findViewById(R.id.item_entity_layout2);
        itemEntityLayout3 = rootView.findViewById(R.id.item_entity_layout3);
        itemEntityLayout4 = rootView.findViewById(R.id.item_entity_layout4);
        itemEntityLayout5 = rootView.findViewById(R.id.item_entity_layout5);
        itemEntityLayout6 = rootView.findViewById(R.id.item_entity_layout6);
        item_entity_img1 = rootView.findViewById(R.id.item_entity_img1);
        item_entity_img2 = rootView.findViewById(R.id.item_entity_img2);
        item_entity_img3 = rootView.findViewById(R.id.item_entity_img3);
        item_entity_img4 = rootView.findViewById(R.id.item_entity_img4);
        item_entity_img5 = rootView.findViewById(R.id.item_entity_img5);
        item_entity_img6 = rootView.findViewById(R.id.item_entity_img6);
        item_entity_name1 = rootView.findViewById(R.id.item_entity_name1);
        item_entity_name2 = rootView.findViewById(R.id.item_entity_name2);
        item_entity_name3 = rootView.findViewById(R.id.item_entity_name3);
        item_entity_name4 = rootView.findViewById(R.id.item_entity_name4);
        item_entity_name5 = rootView.findViewById(R.id.item_entity_name5);
        item_entity_name6 = rootView.findViewById(R.id.item_entity_name6);
        item_entity_text1 = rootView.findViewById(R.id.item_entity_text1);
        item_entity_text2 = rootView.findViewById(R.id.item_entity_text2);
        item_entity_text3 = rootView.findViewById(R.id.item_entity_text3);
        item_entity_text4 = rootView.findViewById(R.id.item_entity_text4);
        item_entity_text5 = rootView.findViewById(R.id.item_entity_text5);
        item_entity_text6 = rootView.findViewById(R.id.item_entity_text6);
        item_entity_check1 = rootView.findViewById(R.id.item_entity_check1);
        item_entity_check2 = rootView.findViewById(R.id.item_entity_check2);
        item_entity_check3 = rootView.findViewById(R.id.item_entity_check3);
        item_entity_check4 = rootView.findViewById(R.id.item_entity_check4);
        item_entity_check5 = rootView.findViewById(R.id.item_entity_check5);
        item_entity_check6 = rootView.findViewById(R.id.item_entity_check6);
        item_tag_img1 = rootView.findViewById(R.id.item_tag_img1);
        item_tag_img2 = rootView.findViewById(R.id.item_tag_img2);
        item_tag_img3 = rootView.findViewById(R.id.item_tag_img3);
        item_tag_img4 = rootView.findViewById(R.id.item_tag_img4);
        item_tag_img5 = rootView.findViewById(R.id.item_tag_img5);
        item_tag_img6 = rootView.findViewById(R.id.item_tag_img6);

        item_lottie1 = rootView.findViewById(R.id.item_lottie1);
        item_lottie2 = rootView.findViewById(R.id.item_lottie2);
        item_lottie3 = rootView.findViewById(R.id.item_lottie3);
        item_lottie3 = rootView.findViewById(R.id.item_lottie3);
        item_lottie4 = rootView.findViewById(R.id.item_lottie4);
        item_lottie5 = rootView.findViewById(R.id.item_lottie5);
        item_lottie6 = rootView.findViewById(R.id.item_lottie6);

        item_lottie1.setImageAssetsFolder("images/");
        item_lottie2.setImageAssetsFolder("images/");
        item_lottie3.setImageAssetsFolder("images/");
        item_lottie4.setImageAssetsFolder("images/");
        item_lottie5.setImageAssetsFolder("images/");
        item_lottie6.setImageAssetsFolder("images/");

        item_lottie1.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie1.removeAnimatorListener(this);
                item_lottie1.setVisibility(View.GONE);
            }
        });
        item_lottie2.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie2.removeAnimatorListener(this);
                item_lottie2.setVisibility(View.GONE);
            }
        });
        item_lottie3.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie3.removeAnimatorListener(this);
                item_lottie3.setVisibility(View.GONE);
            }
        });
        item_lottie4.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie4.removeAnimatorListener(this);
                item_lottie4.setVisibility(View.GONE);
            }
        });
        item_lottie5.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie5.removeAnimatorListener(this);
                item_lottie5.setVisibility(View.GONE);
            }
        });
        item_lottie6.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                item_lottie6.removeAnimatorListener(this);
                item_lottie6.setVisibility(View.GONE);
            }
        });


        btn_submit = rootView.findViewById(R.id.btn_submit);
        Glide.with(getContext()).asGif().load(R.drawable.btn_gif_accost)
                .error(R.drawable.btn_gif_accost)
                .placeholder(R.drawable.btn_gif_accost)
                .into(btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit) {
                    if (isCountdown) {
                        ToastUtils.showShort(R.string.playfun_text_time_not_up);
                        return;
                    } else {
                        submitAccostList();
                    }
                } else {
                    ToastUtils.showShort(R.string.playfun_text_run_out);
                    return;
                }

            }
        });

        iv_dialog_close = rootView.findViewById(R.id.iv_dialog_close);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAccostClicksListener != null) {
                    dialogAccostClicksListener.onCancelClick(HomeAccostDialog.this);
                } else {
                    dismiss();
                }
            }
        });
        refresh_layout = rootView.findViewById(R.id.refresh_layout);
        refresh_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.instance().logEvent(AppsFlyerEvent.accost_change);
                //有冷却时间
                if (changeDownTime != null) {
                    try {
                        long lastTimeChangeTime = Utils.format.parse(changeDownTime).getTime();
                        long dayTime = System.currentTimeMillis();
                        //上次点击时间+冷却时间少于当前时间证明冷却完毕
                        if ((lastTimeChangeTime + changeTime) < dayTime) {
                            //刷新
                            getAccountList(true);
                        } else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    getAccountList(true);
                }
            }
        });
    }

    public void show() {
        //设置背景透明,去四个角
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(rootView);
        //设置宽度充满屏幕
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void getAccountList(boolean isChange) {
        int cutPage = 1;
        if (isChange) {
            page++;
            cutPage = page;
        }
        ConfigManager.getInstance().getAppRepository().getAccostList(cutPage)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseDataResponse<AccostEntity>>() {
                    @Override
                    public void onSuccess(BaseDataResponse<AccostEntity> accostEntityBaseDataResponse) {
                        RxBus.getDefault().post(new LoadEvent(false));
                        AccostEntity accostEntity = accostEntityBaseDataResponse.getData();
                        if (!ObjectUtils.isEmpty(accostEntity)) {
                            submit = accostEntity.getSubmit() == 1;
                            expirationTime = accostEntity.getExpirationTime();
                            dateTime = accostEntity.getDateTime();
                            changeTime = accostEntity.getChangeTime() * 1000;
                            if (isChange) {
                                changeDownTime = Utils.format.format(new Date());
                                putKeyValue(ConfigManager.getInstance().getAppRepository().readUserData().getId() + "_homeAccost", changeDownTime);
                            }
                            //显示灰色字体开启定时器。
                            if (changeDownTime != null) {
                                try {
                                    long lastTimeChangeTime = Utils.format.parse(changeDownTime).getTime();
                                    long dayTime = System.currentTimeMillis();
                                    //有冷却时间
                                    dowDownChangeTime = (lastTimeChangeTime + changeTime) - dayTime;
                                    if (dowDownChangeTime > 0) {
                                        changeText.setTextColor(ColorUtils.getColor(R.color.task_xuxian));
                                        downChangeTime();
                                    } else {
                                        changeText.setTextColor(ColorUtils.getColor(R.color.accost_down_time_change));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (submit) {
                                if (!ObjectUtils.isEmpty(expirationTime)) {
                                    try {
                                        long lastTime = Utils.format.parse(expirationTime).getTime();
                                        long doTime = Utils.format.parse(dateTime).getTime();
                                        downTime = (lastTime - doTime);
                                        if (downTime < 100 || downTime <= 0) {
                                            exp_time.setVisibility(View.GONE);
                                        } else {
                                            String time = TimeUtils.getFormatTime((int) downTime / 1000);
                                            exp_time.setText(time);
                                            btn_submit.setImageResource(R.drawable.btn_accost_nomal);
                                            downTime();
                                        }
                                    } catch (Exception e) {

                                    }

                                }
                            } else {
                                btn_submit.setImageResource(R.drawable.btn_accost_nomal);
                                exp_time.setVisibility(View.VISIBLE);
                                exp_time.setTextColor(ColorUtils.getColor(R.color.black));
                                exp_time.setText(mContext.getString(R.string.playfun_text_accost_empty));
                            }
                            $listData = accostEntity.getData();
                            if ($listData != null) {
                                int size = $listData.size();
                                if (size > 0) {
                                    AccostItemEntity itemEntity1 = $listData.get(0);
                                    loadImage(itemEntity1.getAvatar(), item_entity_img1);
                                    item_entity_name1.setText(itemEntity1.getNickname());
                                    item_entity_text1.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity1.getAge()));
                                    if (itemEntity1.getCertification() == 1) {
                                        item_tag_img1.setVisibility(View.VISIBLE);
                                        item_tag_img1.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity1.getIsVip() == 1) {
                                        item_tag_img1.setVisibility(View.VISIBLE);
                                        item_tag_img1.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout1.setVisibility(View.GONE);
                                }

                                if (size > 1) {
                                    AccostItemEntity itemEntity2 = $listData.get(1);
                                    loadImage(itemEntity2.getAvatar(), item_entity_img2);
                                    item_entity_name2.setText(itemEntity2.getNickname());
                                    item_entity_text2.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity2.getAge()) );
                                    if (itemEntity2.getCertification() == 1) {
                                        item_tag_img2.setVisibility(View.VISIBLE);
                                        item_tag_img2.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity2.getIsVip() == 1) {
                                        item_tag_img2.setVisibility(View.VISIBLE);
                                        item_tag_img2.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout2.setVisibility(View.GONE);
                                }
                                if (size > 2) {
                                    AccostItemEntity itemEntity3 = $listData.get(2);
                                    loadImage(itemEntity3.getAvatar(), item_entity_img3);
                                    item_entity_name3.setText(itemEntity3.getNickname());
                                    item_entity_text3.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity3.getAge()));
                                    if (itemEntity3.getCertification() == 1) {
                                        item_tag_img3.setVisibility(View.VISIBLE);
                                        item_tag_img3.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity3.getIsVip() == 1) {
                                        item_tag_img3.setVisibility(View.VISIBLE);
                                        item_tag_img3.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout3.setVisibility(View.GONE);
                                }
                                if (size > 3) {
                                    AccostItemEntity itemEntity4 = $listData.get(3);
                                    loadImage(itemEntity4.getAvatar(), item_entity_img4);
                                    item_entity_name4.setText(itemEntity4.getNickname());
                                    item_entity_text4.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity4.getAge()));

                                    if (itemEntity4.getCertification() == 1) {
                                        item_tag_img4.setVisibility(View.VISIBLE);
                                        item_tag_img4.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity4.getIsVip() == 1) {
                                        item_tag_img4.setVisibility(View.VISIBLE);
                                        item_tag_img4.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout4.setVisibility(View.GONE);
                                }
                                if (size > 4) {
                                    itemEntityLayout5.setVisibility(View.VISIBLE);
                                    AccostItemEntity itemEntity5 = $listData.get(4);
                                    loadImage(itemEntity5.getAvatar(), item_entity_img5);
                                    item_entity_name5.setText(itemEntity5.getNickname());
                                    item_entity_text5.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity5.getAge()));
                                    if (itemEntity5.getCertification() == 1) {
                                        item_tag_img5.setVisibility(View.VISIBLE);
                                        item_tag_img5.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity5.getIsVip() == 1) {
                                        item_tag_img5.setVisibility(View.VISIBLE);
                                        item_tag_img5.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout5.setVisibility(View.GONE);
                                }
                                if (size > 5) {
                                    itemEntityLayout6.setVisibility(View.VISIBLE);
                                    AccostItemEntity itemEntity6 = $listData.get(5);
                                    loadImage(itemEntity6.getAvatar(), item_entity_img6);
                                    item_entity_name6.setText(itemEntity6.getNickname());
                                    item_entity_text6.setText(String.format(StringUtils.getString(R.string.playfun_mine_age), itemEntity6.getAge()));
                                    if (itemEntity6.getCertification() == 1) {
                                        item_tag_img6.setVisibility(View.VISIBLE);
                                        item_tag_img6.setImageResource(R.drawable.ic_real_man);
                                    } else if (itemEntity6.getIsVip() == 1) {
                                        item_tag_img6.setVisibility(View.VISIBLE);
                                        item_tag_img6.setImageResource(R.drawable.ic_vip);
                                    }
                                } else {
                                    itemEntityLayout6.setVisibility(View.GONE);
                                }

                            } else {
                                if (refresh_layout != null) {
                                    refresh_layout.setVisibility(View.GONE);
                                }
                                btn_submit.setEnabled(true);
                                btn_submit.setImageResource(R.drawable.btn_accost_nomal);
                                exp_time.setVisibility(View.VISIBLE);
                                exp_time.setTextColor(ColorUtils.getColor(R.color.black));
                                exp_time.setText(mContext.getString(R.string.playfun_text_accost_empty2));
                            }
                        }
                    }

                    @Override
                    public void onError(RequestException e) {
                        super.onError(e);
                        RxBus.getDefault().post(new LoadEvent(false));
                    }
                });
    }

    private void loadImage(String imgPath, CircleImageView imageView) {
        Glide.with(mContext).load(StringUtil.getFullImageUrl(imgPath))
                .error(R.drawable.default_avatar)
                .placeholder(R.drawable.default_avatar)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private void submitAccostList() {
        List<Integer> dataAccessList = new ArrayList<Integer>();
        if ($listData != null) {
            int size = $listData.size();
            if (size > 0) {
                if (item_entity_check1.isChecked()) {
                    dataAccessList.add($listData.get(0).getId());
                }
            }
            if (size > 1) {
                if (item_entity_check2.isChecked()) {
                    dataAccessList.add($listData.get(1).getId());
                }
            }
            if (size > 2) {
                if (item_entity_check3.isChecked()) {
                    dataAccessList.add($listData.get(2).getId());
                }
            }
            if (size > 3) {
                if (item_entity_check4.isChecked()) {
                    dataAccessList.add($listData.get(3).getId());
                }
            }
            if (size > 4) {
                if (item_entity_check5.isChecked()) {
                    dataAccessList.add($listData.get(4).getId());
                }
            }
            if (size > 5) {
                if (item_entity_check6.isChecked()) {
                    dataAccessList.add($listData.get(5).getId());
                }
            }
        }
        if (!ObjectUtils.isEmpty(dataAccessList) && dataAccessList.size() > 0) {
            if (dialogAccostClicksListener != null) {
                RxBus.getDefault().post(new LoadEvent(true));
                putAccostList(dataAccessList);
                //dialogAccostClicksListener.onSubmitClick(this, dataAccessList);
            } else {
                dismiss();
            }
        } else {
            ToastUtils.showShort(R.string.playfun_text_accost_error2);
        }

    }

    //倒计时开始
    private void downTime() {

        /**
         * 倒计时一次1秒
         */
        isCountdown = true;
        downTimer = new CountDownTimer(downTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = TimeUtils.getFormatTime((int) millisUntilFinished / 1000);
                exp_time.setTextColor(ColorUtils.getColor(R.color.white));
                exp_time.setText(time);
                exp_time.setVisibility(View.VISIBLE);
                btn_submit.setImageResource(R.drawable.btn_accost_nomal);
            }

            @Override
            public void onFinish() {
                isCountdown = false;
                submit = true;
                exp_time.setVisibility(View.GONE);
                Glide.with(getContext()).asGif().load(R.drawable.btn_gif_accost)
                        .error(R.drawable.btn_gif_accost)
                        .placeholder(R.drawable.btn_gif_accost)
                        .into(btn_submit);
                //RxBus.getDefault().post(new MessageTagEvent(null, false));
            }
        };
        downTimer.start();
    }

    //换一批倒计时开始
    private void downChangeTime() {

        /**
         * 倒计时一次1秒
         */
        isChangeDownTime = true;
        downChangeTimer = new CountDownTimer(dowDownChangeTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                isChangeDownTime = false;
                changeText.setTextColor(ColorUtils.getColor(R.color.accost_down_time_change));
                //RxBus.getDefault().post(new MessageTagEvent(null, false));
            }
        };
        downChangeTimer.start();
    }

    /**
     * @return void
     * @Desc TODO(播放动画)
     * @author 彭石林
     * @parame []
     * @Date 2021/12/10
     */
    public void lottieAnimationView() {
        if (!item_lottie1.isAnimating() && item_entity_check1.isChecked()) {
            item_lottie1.setVisibility(View.VISIBLE);
            item_lottie1.setAnimation(R.raw.accost_animation);
            item_lottie1.playAnimation();
        }
        if (!item_lottie2.isAnimating() && item_entity_check2.isChecked()) {
            item_lottie2.setVisibility(View.VISIBLE);
            item_lottie2.setAnimation(R.raw.accost_animation);
            item_lottie2.playAnimation();
        }
        if (!item_lottie3.isAnimating() && item_entity_check3.isChecked()) {
            item_lottie3.setVisibility(View.VISIBLE);
            item_lottie3.setAnimation(R.raw.accost_animation);
            item_lottie3.playAnimation();
        }
        if (!item_lottie4.isAnimating() && item_entity_check4.isChecked()) {
            item_lottie4.setVisibility(View.VISIBLE);
            item_lottie4.setAnimation(R.raw.accost_animation);
            item_lottie4.playAnimation();
        }
        if (!item_lottie5.isAnimating() && item_entity_check5.isChecked()) {
            item_lottie5.setVisibility(View.VISIBLE);
            item_lottie5.setAnimation(R.raw.accost_animation);
            item_lottie5.playAnimation();
        }
        if (!item_lottie6.isAnimating() && item_entity_check6.isChecked()) {
            item_lottie6.setVisibility(View.VISIBLE);
            item_lottie6.setAnimation(R.raw.accost_animation);
            item_lottie6.playAnimation();
        }
    }

    //批量搭讪
    public void putAccostList(List<Integer> userIds) {
        try {
            //男女点击一键搭讪
            AppContext.instance().logEvent(ConfigManager.getInstance().isMale() ? AppsFlyerEvent.one_click_greet_male : AppsFlyerEvent.one_click_greet_female);
        }catch (Exception ignored){

        }
        ConfigManager.getInstance().getAppRepository().putAccostList(userIds)
                .doOnSubscribe(this)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        AppContext.instance().logEvent(AppsFlyerEvent.accost_chatup);
                        lottieAnimationView();
                        ToastUtils.showShort(R.string.playfun_text_accost_success);
                        getAccountList(false);
                    }

                    @Override
                    public void onError(RequestException e) {
                        RxBus.getDefault().post(new LoadEvent(false));
                    }
                });
    }

    //存储键值对
    public void putKeyValue(String key, String value) {
        ConfigManager.getInstance().getAppRepository().putKeyValue(key, value);
    }

    public String readKeyValue(String key) {
        return ConfigManager.getInstance().getAppRepository().readKeyValue(key);
    }


    public interface DialogAccostClicksListener {
        void onSubmitClick(HomeAccostDialog dialog, List<Integer> listData);

        void onCancelClick(HomeAccostDialog dialog);
    }
}
