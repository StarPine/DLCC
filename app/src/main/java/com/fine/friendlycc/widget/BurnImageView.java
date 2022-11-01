package com.fine.friendlycc.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.data.source.http.observer.BaseObserver;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.entity.AlbumPhotoEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.utils.StringUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.fine.friendlycc.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author litchi
 */
public class BurnImageView extends RelativeLayout {

    private static boolean isLongClickModule = false;
    private static float startX = 0;
    private static float startY = 0;
    private static Timer timer = null;
    long touchDownTime = 0;
    Disposable countDownDisposable = null;
    private PhotoView photoView;
    private ViewGroup loadView;
    private ViewGroup burnMsgView;
    private RoundProcess roundProcess;
    private boolean imgLoadSuccess;
    private int maxProgress;
    private boolean sended;
    private AlbumPhotoEntity burnImgEnt;
    private boolean addWaterMark;
    private BindingCommand burnCommand;
    private BindingCommand tapCommand;
    private boolean isBurn;
    private int burnStatus;

    public BurnImageView(Context context) {
        super(context);
        initWithContext(context);
    }

    public BurnImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public BurnImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        LayoutInflater.from(context).inflate(R.layout.burn_image_view, this);
        photoView = findViewById(R.id.photo_view);
        loadView = findViewById(R.id.rl_loading);
        burnMsgView = findViewById(R.id.ll_burn_msg);
        roundProcess = findViewById(R.id.round_process);

        loadView.setVisibility(View.GONE);
        burnMsgView.setVisibility(View.GONE);
        roundProcess.setVisibility(View.GONE);

        maxProgress = ConfigManager.getInstance().getBurnTime();
        roundProcess.setMaxProgress(maxProgress);

        photoView.setOnViewTapListener((view, x, y) -> {
            if (tapCommand != null) {
                tapCommand.execute();
            }
        });
        burnMsgView.setMotionEventSplittingEnabled(false);

        burnMsgView.setOnTouchListener((v, ev) -> {
            if (!imgLoadSuccess) {
                return false;
            }
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (burnImgEnt.getIsRedPackage() == 1 && burnImgEnt.getIsPay() == 0) { //新增红包阅后即焚
                        return false;
                    }

                    touchDownTime = System.currentTimeMillis();
                    startX = ev.getX();
                    startY = ev.getY();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isLongClickModule = true;
                            v.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateBurn();
                                }
                            });
                        }
                    }, 500); // 按下时长设置
                    break;
                case MotionEvent.ACTION_MOVE:
//                            double deltaX = Math.sqrt((ev.getX() - startX) * (ev.getX() - startX) + (ev.getY() - startY) * (ev.getY() - startY));
//                            if (deltaX > 20 && timer != null) { // 移动大于20像素
//                                timer.cancel();
//                                timer = null;
//                            }
//                            if (isLongClickModule) {
//                                //添加你长按之后的方法
//                                //getDrawingXY();
//                                timer = null;
//
//                            }
                    break;
                case MotionEvent.ACTION_UP:
                    long i = System.currentTimeMillis() - touchDownTime;
                    if (i < 500) {
                        if (tapCommand != null) {
                            tapCommand.execute();
                        }
                    }
                    touchDownTime = 0;
                    if (isLongClickModule) {
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                burnStatus = 1;
                                if (!isBurn) {
                                    isBurn = true;
                                    showImg();
                                }
                                stopCountDown();
                                if (burnCommand != null && !sended) {
                                    sended = true;
                                    burnCommand.execute();
                                }
                            }
                        });
                    }
                    isLongClickModule = false;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    break;
                default:
                    isLongClickModule = false;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
            }
            return true;
        });
    }

    public void setAll(AlbumPhotoEntity burnImgEnt, boolean addWaterMark, BindingCommand burnCommand, BindingCommand tapCommand) {
        this.burnImgEnt = burnImgEnt;
        this.addWaterMark = addWaterMark;
        this.burnCommand = burnCommand;
        this.tapCommand = tapCommand;
        isBurn = burnImgEnt.getIsBurn() == 1 || (burnImgEnt.getIsRedPackage() == 1 && burnImgEnt.getIsPay() == 0);
        burnStatus = burnImgEnt.getBurnStatus();
        showImg();
    }

    public void showImg() {
        String url = StringUtil.getFullImageUrl(burnImgEnt.getSrc());
        if (addWaterMark) {
            url = StringUtil.getFullImageWatermarkUrl(burnImgEnt.getSrc());
        }
        if (isBurn) {
            if (burnStatus == 1) {
                showBurnedMsg();
            } else {
                showBurnMsg();
            }
//            showLoadingView(true);
            Glide.with(getContext())
                    .load(url)
                    .apply(bitmapTransform(new BlurTransformation(100)))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.black_background)
                            .error(R.drawable.black_background))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            showLoadingView(false);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            showLoadingView(false);
                            imgLoadSuccess = true;
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(photoView);
        } else {
            hideBurnMsg();
            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.black_background)
                            .error(R.drawable.black_background))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(photoView);
        }
    }

    private void startCountDown() {
        roundProcess.setVisibility(View.VISIBLE);
        countDownDisposable = Flowable.intervalRange(0, maxProgress + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    roundProcess.setProgress(maxProgress - aLong.intValue());
                })
                .doOnComplete(() -> {
                    roundProcess.setProgress(0);
                    roundProcess.setVisibility(View.GONE);
                    if (!isBurn) {
                        isBurn = true;
                        burnStatus = 1;
                        showImg();
                    }
                    if (burnCommand != null && !sended) {
                        sended = true;
                        burnCommand.execute();
                    }
                })
                .subscribe();
    }

    private void updateBurn() {
        Injection.provideDemoRepository().imgeReadLog(burnImgEnt.getId())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(disposable -> {
                    showLoadingView(true);
                })
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        isBurn = false;
                        showImg();
                        startCountDown();
                    }

                    @Override
                    public void onComplete() {
                        showLoadingView(false);
                    }
                });
    }

    private void stopCountDown() {
        roundProcess.setVisibility(View.GONE);
        if (countDownDisposable != null) {
            countDownDisposable.dispose();
        }
    }

    public void showLoadingView(boolean show) {
        loadView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showBurnMsg() {
        burnMsgView.setVisibility(View.VISIBLE);
    }

    public void hideBurnMsg() {
        burnMsgView.setVisibility(View.GONE);
    }

    public void showBurnedMsg() {
        burnMsgView.setVisibility(View.GONE);
    }

}
