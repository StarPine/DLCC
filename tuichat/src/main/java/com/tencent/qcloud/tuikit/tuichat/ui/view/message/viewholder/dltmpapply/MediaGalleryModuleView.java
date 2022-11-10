package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.dltmpapply;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.custom.IMGsonUtils;
import com.tencent.custom.MvBlurTransformation;
import com.tencent.custom.PhotoGalleryPayEntity;
import com.tencent.custom.VideoGalleryPayEntity;
import com.tencent.custom.tmp.CloudCustomDataMediaGalleryEntity;
import com.tencent.custom.tmp.CustomDlTempMessage;
import com.tencent.qcloud.tuicore.custom.CustomConstants;
import com.tencent.qcloud.tuicore.custom.CustomDrawableUtils;
import com.tencent.qcloud.tuicore.custom.entity.MediaGalleryEditEntity;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder.CustomDlTempMessageHolder;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author: 彭石林
 * Time: 2022/9/17 10:55
 * Description: IM发送图片、视频模块
 */
public class MediaGalleryModuleView extends BaseMessageModuleView {

    private final String TAG = "MediaGalleryContent";

    public MediaGalleryModuleView(CustomDlTempMessageHolder customDlTempMessageHolder) {
        super(customDlTempMessageHolder);
    }

    public void layoutVariableViews(TUIMessageBean msg, FrameLayout rootView, int position, CustomDlTempMessage.MsgBodyInfo msgModuleInfo){
        switch (msgModuleInfo.getCustomMsgType()){
            case CustomConstants.MediaGallery.PHOTO_GALLERY:
                PhotoGalleryPayEntity customImageMessageBean = IMGsonUtils.fromJson(IMGsonUtils.toJson(msgModuleInfo.getCustomMsgBody()),PhotoGalleryPayEntity.class);
                LoadMediaGalleryPhoto(msg, rootView, customImageMessageBean);
                break;
            case CustomConstants.MediaGallery.VIDEO_GALLERY:
                VideoGalleryPayEntity customVideoMessageBean = IMGsonUtils.fromJson(IMGsonUtils.toJson(msgModuleInfo.getCustomMsgBody()),VideoGalleryPayEntity.class);
                LoadMediaGalleryVideo(msg, rootView, customVideoMessageBean);
                break;
            default:
                //默认展示解析不出的模板提示
                customDlTempMessageHolder.defaultLayout(rootView, msg.isSelf(),position,msg);
                break;
        }
    }

    //设置展示收益内容
    private void tvProfitTipTmp(TextView tvProfitTip,FrameLayout flContainer,ImageView imgUnlock,String moneyTmpText, BigDecimal redPackageRevenue){
        if(redPackageRevenue!=null && redPackageRevenue.doubleValue()>0){
            tvProfitTip.setText(String.format(moneyTmpText,redPackageRevenue.setScale(2, RoundingMode.HALF_UP)));
            LinearLayout.LayoutParams flParams = (LinearLayout.LayoutParams) flContainer.getLayoutParams();
            LinearLayout.LayoutParams tvParams = (LinearLayout.LayoutParams) tvProfitTip.getLayoutParams();
            flParams.gravity = Gravity.END;
            tvParams.gravity = Gravity.END;
            flContainer.setLayoutParams(flParams);
            tvProfitTip.setLayoutParams(tvParams);
            tvProfitTip.setVisibility(View.VISIBLE);
            imgUnlock.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @Desc TODO(付费照片or普通照片or快照)
     * @author 彭石林
     * @parame [msg, rootView, videoGalleryPayEntity]
     * @return void
     * @Date 2022/9/21
     */
    public void LoadMediaGalleryPhoto(TUIMessageBean msg, FrameLayout rootView, PhotoGalleryPayEntity photoGalleryPayEntity){
        //自定义头部消息体转换
        CloudCustomDataMediaGalleryEntity cloudCustomDataMediaGalleryEntity = getCloudCustomDataConvert(msg.getV2TIMMessage().getCloudCustomData());
        if(cloudCustomDataMediaGalleryEntity == null){
            cloudCustomDataMediaGalleryEntity = new CloudCustomDataMediaGalleryEntity();
        }
        View customImageView = View.inflate(getContext(), R.layout.tmp_message_photo_gallery_layout, null);
        FrameLayout flContainer = customImageView.findViewById(R.id.fl_container);
        //快照角标
        FrameLayout fLTlLayout = customImageView.findViewById(R.id.fl_tl_layout);
        //底部布局
        RelativeLayout rlLayout = customImageView.findViewById(R.id.rl_layout);
        //解锁金额布局
        RelativeLayout fLUnlockLayout = customImageView.findViewById(R.id.fl_unlock_layout);
        //解锁成功状态
        ImageView imgUnlock = customImageView.findViewById(R.id.img_unlock);
        //焚毁布局
        LinearLayout llBurned = customImageView.findViewById(R.id.ll_burned);
        //收益提示
        TextView tvProfitTip = customImageView.findViewById(R.id.tv_profit_tip);
        boolean stateSnapshot = false;
        //付费照片
        if(photoGalleryPayEntity.isStatePhotoPay()){
            rlLayout.setVisibility(View.VISIBLE);
            //快照
            stateSnapshot  = photoGalleryPayEntity.isStateSnapshot();
            fLTlLayout.setVisibility(stateSnapshot ? View.VISIBLE : View.GONE);
            if(stateSnapshot){
                CustomDrawableUtils.generateDrawable(fLTlLayout, Color.parseColor("#717477"),
                        null,8,null,null,8,
                        null,null,null,null,null,null);
            }
            //是否解锁
            if(!cloudCustomDataMediaGalleryEntity.isUnLocked()){
                //已经解锁
                fLUnlockLayout.setVisibility(View.GONE);
                if(msg.isSelf()){
                    String tmpMoneyText = getContext().getString(R.string.dl_tmp_mediagallery_text);
                    tvProfitTipTmp(tvProfitTip,flContainer,imgUnlock,tmpMoneyText,cloudCustomDataMediaGalleryEntity.getRedPackageRenvenue());
                }else{
                    if(cloudCustomDataMediaGalleryEntity.isRead()){
                        fLTlLayout.setVisibility(View.GONE);
                        llBurned.setVisibility(View.VISIBLE);
                        imgUnlock.setVisibility(View.GONE);
                    }else{
                        fLTlLayout.setVisibility(stateSnapshot ? View.VISIBLE : View.GONE);
                        llBurned.setVisibility(View.GONE);
                    }
                }
            }else{
                fLUnlockLayout.setVisibility(View.VISIBLE);
                //当前收费价格
                TextView tvCoin = customImageView.findViewById(R.id.tv_coin);
                if(tvCoin!=null){
                    isTextNumberShow(tvCoin,photoGalleryPayEntity.getUnlockPrice());
                }
            }
        }else{
            //非付费照片
            //   普通照片： stateSnapshot = false
            //   普通快照： stateSnapshot = true
            if(photoGalleryPayEntity.isStateSnapshot()){
                stateSnapshot = true;
                if(cloudCustomDataMediaGalleryEntity.isRead() && !msg.isSelf()){
                    llBurned.setVisibility(View.VISIBLE);
                    imgUnlock.setVisibility(View.GONE);
                    fLTlLayout.setVisibility(View.GONE);
                }else{
                    llBurned.setVisibility(View.GONE);
                    fLTlLayout.setVisibility(View.VISIBLE);
                }
                CustomDrawableUtils.generateDrawable(fLTlLayout, Color.parseColor("#717477"),
                        null,8,null,null,8,
                        null,null,null,null,null,null);
            }else{
                //隐藏快照标识
                fLTlLayout.setVisibility(View.GONE);
            }
            rlLayout.setVisibility(View.GONE);
        }
        RequestOptions override;
        boolean isFuzzy = stateSnapshot;
        if(!msg.isSelf()){
            if(!stateSnapshot){//不是快照
                //付费照片
                if(photoGalleryPayEntity.isStatePhotoPay()){
                    //已经解锁
                    if(!cloudCustomDataMediaGalleryEntity.isUnLocked()){
                        //隐藏付费layout相关功能
                        rlLayout.setVisibility(View.GONE);
                        //以读
                        isFuzzy = cloudCustomDataMediaGalleryEntity.isRead();
                    }else{
                        isFuzzy = true;
                    }
                }
            }
        }else{
            isFuzzy = false;
        }
        if(isFuzzy){
            override = RequestOptions.bitmapTransform(new MvBlurTransformation(90)).override(dp2px(getContext(),86), dp2px(getContext(),154));
        }else{
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            override = new RequestOptions().override(dp2px(getContext(),86), dp2px(getContext(),154));
        }
        ImageView imgContent = customImageView.findViewById(R.id.img_content);
        String imagePath = TUIChatUtils.getFullImageUrl(photoGalleryPayEntity.getImgPath());
        //本地资源存在
        if(!TextUtils.isEmpty(photoGalleryPayEntity.getAndroidLocalSrcPath())){
            //判断本地资源是否存在
            File imageFile = new File(photoGalleryPayEntity.getAndroidLocalSrcPath());
            if(imageFile.exists()){
                imagePath = photoGalleryPayEntity.getAndroidLocalSrcPath();
            }
        }
        Glide.with(TUIChatService.getAppContext())
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(override.error(R.drawable.pro_loading_logo_error_min).placeholder(R.drawable.pro_loading_logo))
                .into(imgContent);
        rootView.addView(customImageView);
        CloudCustomDataMediaGalleryEntity finalCloudCustomDataMediaGalleryEntity = cloudCustomDataMediaGalleryEntity;
        rootView.setOnClickListener(v -> {
            if (customDlTempMessageHolder.onItemClickListener != null) {
                MediaGalleryEditEntity mediaGalleryEditEntity = new MediaGalleryEditEntity();
                if(finalCloudCustomDataMediaGalleryEntity !=null){
                    mediaGalleryEditEntity.setMsgKeyId(finalCloudCustomDataMediaGalleryEntity.getMsgKey());
                }
                mediaGalleryEditEntity.setSelfSend(msg.isSelf());
                mediaGalleryEditEntity.setSrcPath(photoGalleryPayEntity.getImgPath());
                mediaGalleryEditEntity.setVideoSetting(false);
                mediaGalleryEditEntity.setUnlockPrice(photoGalleryPayEntity.getUnlockPrice());
                mediaGalleryEditEntity.setStatePay(photoGalleryPayEntity.isStatePhotoPay());
                mediaGalleryEditEntity.setStateSnapshot(photoGalleryPayEntity.isStateSnapshot());
                mediaGalleryEditEntity.setStateUnlockPhoto(!finalCloudCustomDataMediaGalleryEntity.isUnLocked());
                mediaGalleryEditEntity.setReadLook(finalCloudCustomDataMediaGalleryEntity.isRead());
                customDlTempMessageHolder.onItemClickListener.onMediaGalleryClick(mediaGalleryEditEntity);
            }
        });
    }

    /**
    * @Desc TODO(付费影片or普通影片)
    * @author 彭石林
    * @parame [msg, rootView, videoGalleryPayEntity]
    * @return void
    * @Date 2022/9/21
    */
    public void LoadMediaGalleryVideo(TUIMessageBean msg, FrameLayout rootView, VideoGalleryPayEntity videoGalleryPayEntity){
        //自定义头部消息体转换
       CloudCustomDataMediaGalleryEntity cloudCustomDataMediaGalleryEntity = getCloudCustomDataConvert(msg.getV2TIMMessage().getCloudCustomData());
        if(cloudCustomDataMediaGalleryEntity==null){
            cloudCustomDataMediaGalleryEntity = new CloudCustomDataMediaGalleryEntity();
        }
        View customImageView = View.inflate(getContext(), R.layout.tmp_message_video_gallery_layout, null);
        //底部布局
        RelativeLayout rlLayout = customImageView.findViewById(R.id.rl_layout);
        //解锁金额布局
        RelativeLayout fLUnlockLayout = customImageView.findViewById(R.id.fl_unlock_layout);
        //解锁成功状态
        ImageView imgUnlock = customImageView.findViewById(R.id.img_unlock);
        //收益提示
        TextView tvProfitTip = customImageView.findViewById(R.id.tv_profit_tip);
        FrameLayout flContainer = customImageView.findViewById(R.id.fl_container);
        //付费视频
        if(videoGalleryPayEntity.isStateVideoPay()){
            rlLayout.setVisibility(View.VISIBLE);
                //是否已解锁
                if(!cloudCustomDataMediaGalleryEntity.isUnLocked()){
                    //发送方是自己
                    if(msg.isSelf()) {
                        fLUnlockLayout.setVisibility(View.GONE);
                        imgUnlock.setVisibility(View.VISIBLE);
                        //收益
                        String tmpMoneyText = getContext().getString(R.string.dl_tmp_mediagallery_text2);
                        tvProfitTipTmp(tvProfitTip,flContainer,imgUnlock,tmpMoneyText,cloudCustomDataMediaGalleryEntity.getRedPackageRenvenue());
                    }else{
                        rlLayout.setVisibility(View.GONE);
                    }
                }else{
                    fLUnlockLayout.setVisibility(View.VISIBLE);
                    imgUnlock.setVisibility(View.GONE);
                }
            //当前收费价格
            TextView tvCoin = customImageView.findViewById(R.id.tv_coin);
            if(tvCoin!=null){
                if(videoGalleryPayEntity.getUnlockPrice()!=null && videoGalleryPayEntity.getUnlockPrice().intValue() > 0){
                    tvCoin.setVisibility(View.VISIBLE);
                    tvCoin.setText(String.valueOf(videoGalleryPayEntity.getUnlockPrice().intValue()));
                }else{
                    tvCoin.setVisibility(View.GONE);
                }
            }
        }else{
            //非付费视频
            rlLayout.setVisibility(View.GONE);
        }
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(dp2px(getContext(),8));
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions override = RequestOptions.bitmapTransform(roundedCorners).override(dp2px(getContext(),86), dp2px(getContext(),154));
        ImageView imgContent = customImageView.findViewById(R.id.img_content);
        String imagePath = TUIChatUtils.getFullImageUrl(videoGalleryPayEntity.getSrcPath());
        //本地资源存在
        if(!TextUtils.isEmpty(videoGalleryPayEntity.getAndroidLocalSrcPath())){
            //判断本地资源是否存在
            File videoFile = new File(videoGalleryPayEntity.getAndroidLocalSrcPath());
            if(videoFile.exists()){
                imagePath = videoGalleryPayEntity.getAndroidLocalSrcPath();
            }
        }
        Glide.with(TUIChatService.getAppContext())
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(override.error(R.drawable.pro_loading_logo_error_min).placeholder(R.drawable.pro_loading_logo))
                .into(imgContent);
        rootView.addView(customImageView);
        CloudCustomDataMediaGalleryEntity finalCloudCustomDataMediaGalleryEntity = cloudCustomDataMediaGalleryEntity;
        rootView.setOnClickListener(v -> {
            if (customDlTempMessageHolder.onItemClickListener != null) {
                MediaGalleryEditEntity mediaGalleryEditEntity = new MediaGalleryEditEntity();
                    mediaGalleryEditEntity.setMsgKeyId(finalCloudCustomDataMediaGalleryEntity.getMsgKey());
                    mediaGalleryEditEntity.setStateUnlockPhoto(!finalCloudCustomDataMediaGalleryEntity.isUnLocked());
                mediaGalleryEditEntity.setSelfSend(msg.isSelf());
                mediaGalleryEditEntity.setSrcPath(videoGalleryPayEntity.getSrcPath());
                mediaGalleryEditEntity.setVideoSetting(true);
                mediaGalleryEditEntity.setUnlockPrice(videoGalleryPayEntity.getUnlockPrice());
                mediaGalleryEditEntity.setStatePay(videoGalleryPayEntity.isStateVideoPay());
                customDlTempMessageHolder.onItemClickListener.onMediaGalleryClick(mediaGalleryEditEntity);
            }
        });
    }

    private void isTextNumberShow(TextView textView, BigDecimal bigDecimal) {
        if(bigDecimal!=null && bigDecimal.intValue() > 0){
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(bigDecimal.intValue()));
        }else{
            textView.setVisibility(View.GONE);
        }
    }

    /**
    * @Desc TODO(获取自定义消息提)
    * @author 彭石林
    * @parame [customDlTempMessage]
    * @return com.tencent.custom.tmp.CloudCustomDataMediaGalleryEntity
    * @Date 2022/9/17
    */
    private CloudCustomDataMediaGalleryEntity getCloudCustomDataConvert(String customDlTempMessage){
        //自定义消息体。外层
        CloudCustomDataMediaGalleryEntity cloudCustomDataMediaGalleryEntity = null;
        if(TUIChatUtils.isJSON2(customDlTempMessage)){
            cloudCustomDataMediaGalleryEntity = IMGsonUtils.fromJson(customDlTempMessage,CloudCustomDataMediaGalleryEntity.class);
        }
        return cloudCustomDataMediaGalleryEntity;
    }
}
