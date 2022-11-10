package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder;

import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.custom.ConfigUrl;
import com.tencent.custom.CustomIMTextEntity;
import com.tencent.custom.GiftEntity;
import com.tencent.custom.IMGsonUtils;
import com.tencent.custom.PhotoAlbumEntity;
import com.tencent.custom.PhotoAlbumItemRecyclerAdapter;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.component.face.FaceManager;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.MessageRecyclerView;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

import java.util.Map;

/**
 * 修改备注：自定义json文本信息类型
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/7/4 11:13
 *
 */
public class CustomTextMessageHolder extends TextMessageHolder {
    private ImageView mLeftView, mRightView;
    boolean isPayee = false;//当前账号是否为收款人
    private View tipView;
    private TextView customTipText;
    private CustomIMTextEntity customIMTextEntity;

    public CustomTextMessageHolder(View itemView) {
        super(itemView);
        mLeftView = itemView.findViewById(R.id.left_icon);
        mRightView = itemView.findViewById(R.id.right_icon);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_json_text;
    }

    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {

        initView();
        String extra = msg.getExtra();
        if (TUIChatUtils.isJSON2(extra)) {//自定义json文本消息
            String type = TUIChatUtils.json2Massage(extra, "type");
            if (type == null){
                setContentLayoutVisibility(false);
                return;
            }
            switch (type) {
                case TUIChatConstants.CoustomMassageType.MESSAGE_GIFT:
                    setGiftMessageItemView(msg, extra);
                    break;
                case TUIChatConstants.CoustomMassageType.CHAT_EARNINGS:
                    setChatEarningsItemView(extra,msg);
                    break;
                case TUIChatConstants.CoustomMassageType.MESSAGE_CUSTOM:
                    setCustomTypeItemView(extra,position,msg);
                    break;
                case TUIChatConstants.CoustomMassageType.MESSAGE_TAG:
                    FaceManager.handlerEmojiText(msgBodyText, TUIChatUtils.json2Massage(extra, "text"), false);
                    setBackColor(msg);
                    break;
                case TUIChatConstants.CoustomMassageType.MESSAGE_PHOTO:
                    setPhotoItemView(extra,position,msg);
                    break;
                case TUIChatConstants.CoustomMassageType.MESSAGE_CALLINGBUSY:
                    setCallingBusyItemView(msg,extra);
                    break;
                case TUIChatConstants.CoustomMassageType.SEND_VIOLATION_MESSAGE:
                    setViolationItemView(msg,extra,position);
                    break;
                default:
                    setContentLayoutVisibility(false);
                    break;

            }
        } else {
            super.layoutVariableViews(msg, position);
        }
    }

    private void initView() {
        mLeftView.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }

    //done 收益相关 兼容旧的收益消息
    private void setChatEarningsItemView(String extra, TUIMessageBean msg) {
        hideTimeView();
        initServerDataAndView(extra);
        LinearLayout.LayoutParams customHintTextLayoutParams = (LinearLayout.LayoutParams) customTipText.getLayoutParams();
        if (MessageRecyclerView.sex) {
            customHintTextLayoutParams.gravity = Gravity.END;
            customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), 62);
        } else {
            customHintTextLayoutParams.gravity = Gravity.START;
            customHintTextLayoutParams.leftMargin = dip2px(itemView.getContext(), 62);
        }
        if (customIMTextEntity != null) {
            //收益退回提示
            if (customIMTextEntity.getIsRefundMoney() != null) {
                if (!MessageRecyclerView.sex) {//女方
                    customTipText.setText(appContext.getString(R.string.custom_message_txt_girl));
                } else {//男方
                    customTipText.setText(appContext.getString(R.string.custom_message_txt_male));
                }
            } else {//收益水晶显示
                setProfitDetails(customIMTextEntity.getTextProfit(), customTipText);
            }
            customJsonMsgContentFrame.addView(tipView);
        }
    }

    private void setCustomTypeItemView(String extra, int position, TUIMessageBean msg) {
        hideWithAvatarView();
        initServerDataAndView(extra);
        if (customIMTextEntity != null) {

            //done 系统提示
            if (!TextUtils.isEmpty(customIMTextEntity.getContent())) {

                String likeText = "一鍵追蹤她，不再失聯噢 已追蹤";
                if (MessageRecyclerView.addLikeMsgId != null && msg.getId().equals(MessageRecyclerView.addLikeMsgId)) {
                    customIMTextEntity.setContent(likeText);
                    customIMTextEntity.setEvent(-1);
                    customIMTextEntity.setKey(null);
                }

                if (!TextUtils.isEmpty(customIMTextEntity.getKey())) {
                    if (customIMTextEntity.getContent().contains("<font>")) {
                        String fontText = "<font color='" + customIMTextEntity.getColor() + "'>" + customIMTextEntity.getKey() + "</font>";
                        String content = customIMTextEntity.getContent();
                        String CDATAText = content.replace("<font>" + customIMTextEntity.getKey() + "</font>", fontText);
                        customTipText.setText(Html.fromHtml(CDATAText));
                    } else {
                        customTipText.setText(matcherSearchText(customIMTextEntity.getColor(), customIMTextEntity.getContent(), customIMTextEntity.getKey()));
                    }
                } else {
                    customTipText.setText(customIMTextEntity.getContent());
                }

                if (customIMTextEntity.getGravity() != null) {
                    LinearLayout.LayoutParams customHintTextLayoutParams = (LinearLayout.LayoutParams) customTipText.getLayoutParams();
                    if (customIMTextEntity.getGravity().equals("left")) {
                        customHintTextLayoutParams.gravity = Gravity.START;
                        customHintTextLayoutParams.leftMargin = dip2px(itemView.getContext(), customIMTextEntity.getMargin());
                    } else if (customIMTextEntity.getGravity().equals("right")) {
                        customHintTextLayoutParams.gravity = Gravity.END;
                        customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), customIMTextEntity.getMargin());
                    } else {
                        customHintTextLayoutParams.gravity = Gravity.CENTER;
                    }
                    customTipText.setLayoutParams(customHintTextLayoutParams);
                }
                customTipText.setOnClickListener(v -> {
                    if (onItemClickListener != null)
                        onItemClickListener.onClickCustomText(position, msg, customIMTextEntity);
                });
            }

            if (customIMTextEntity.getIsRemindPay() != null) {

                int totalSeconds = customIMTextEntity.getTotalSeconds();
                int callingType = customIMTextEntity.getCallingType();
                String price = customIMTextEntity.getPrice();
                if (price == null){
                    price = "0";
                }
                //done 通话结束时间
                if (totalSeconds > 0 && callingType > 0) {
                    showWithAvatarView(msg.isSelf());
                    setBackColor(msg);
                    setCallingMsgIconStyle(msg, callingType);
                    msgBodyText.setText(itemView.getContext()
                            .getString(R.string.custom_message_call_message_deatail_time_msg, totalSeconds/3600, totalSeconds / 60, totalSeconds % 60));
                }

                //done 余额不足和收益提示
                if (customIMTextEntity.getIsRemindPay() == 1
                        && Double.parseDouble(price) <= 0 ) {
                    tipView = View.inflate(appContext, R.layout.custom_not_sufficient_view, null);
                    LinearLayout male_hint_layout = tipView.findViewById(R.id.male_hint_layout);
                    male_hint_layout.setOnClickListener(v -> {
                        if (onItemClickListener != null)
                            onItemClickListener.onClickDialogRechargeShow();
                    });
                } else {//正常收益提示
                    if (Double.parseDouble(price) > 0) {
                        if (customIMTextEntity.getPayeeImId() != null) {
                            isPayee = customIMTextEntity.getPayeeImId().equals(V2TIMManager.getInstance().getLoginUser());
                            if (isPayee) {//收款人显示
                                setProfitDetails(price, profitTip);
                                return;
                            }
                        } else {
                            if (!MessageRecyclerView.sex) {//女生显示
                                setProfitDetails(price, profitTip);
                                return;
                            }
                        }
                    }else {
                        if (totalSeconds <= 0){
                            setContentLayoutVisibility(false);
                            return;
                        }
                    }
                }
            }

            customJsonMsgContentFrame.addView(tipView);
        }
    }

    private void setViolationItemView(TUIMessageBean msg, String extra, int position) {
        FaceManager.handlerEmojiText(msgBodyText, TUIChatUtils.json2Massage(extra, "text"), false);
        setBackColor(msg);
        String busyContent = appContext.getString(R.string.custom_send_violation_message_tip);
        profitTip.setText(busyContent);
        profitTip.setVisibility(View.VISIBLE);
        statusImage.setVisibility(View.VISIBLE);
        sendingProgress.setVisibility(View.GONE);
    }

    private void setCallingBusyItemView(TUIMessageBean msg, String extra) {
//        String busyContent = appContext.getString(R.string.custom_message_book_next_call);
//        profitTip.setText(busyContent);
//        profitTip.setVisibility(View.VISIBLE);
        try {
            Map callData = IMGsonUtils.fromJson(TUIChatUtils.json2Massage(extra, "data"), Map.class);
            int callType = Double.valueOf(String.valueOf(callData.get("type"))).intValue();
            int status = Double.valueOf(String.valueOf(callData.get("inStatus"))).intValue();
            setBackColor(msg);
            setCallingMsgIconStyle(msg, callType);
            if (msg.isSelf()) {
                if (status == 2){
                    msgBodyText.setText(appContext.getString(R.string.playcc_in_game));
                }else {
                    msgBodyText.setText(appContext.getString(R.string.custom_message_other_busy));
                }
            } else {
                if (status == 2){
                    msgBodyText.setText(appContext.getString(R.string.playcc_in_game_missed));
                }else {
                    msgBodyText.setText(appContext.getString(R.string.custom_message_busy_missed));
                }
            }
        }catch (Exception e){
            setContentLayoutVisibility(false);
        }

    }

    private void setPhotoItemView(String extra, int position, TUIMessageBean msg) {
        hideTimeView();
        View photoView = View.inflate(appContext, R.layout.message_adapter_content_photo, null);

        ImageView ic_vip = photoView.findViewById(R.id.iv_vip);
        ImageView ic_certification = photoView.findViewById(R.id.iv_certification);
        TextView conversation_title = photoView.findViewById(R.id.conversation_title);
        ImageView photo_album_img = photoView.findViewById(R.id.photo_album_img);
        PhotoAlbumEntity photoAlbumEntity = IMGsonUtils.fromJson(TUIChatUtils.json2Massage(extra, "data"), PhotoAlbumEntity.class);
        if (photoAlbumEntity != null) {
            int sex = photoAlbumEntity.getSex();
            int isVip = photoAlbumEntity.getIsVip();
            int certification = photoAlbumEntity.getCertification();
            conversation_title.setText(photoAlbumEntity.getNickname());
            //加载头像
            Glide.with(TUIChatService.getAppContext()).load(ConfigUrl.getFullImageUrl(photoAlbumEntity.getAvatar()))
                    .error(R.drawable.photo_album_img_default)
                    .placeholder(R.drawable.photo_album_img_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo_album_img);
            if (sex == 1) {
                ic_vip.setImageResource(R.drawable.ic_vip);
                ic_vip.setVisibility(isVip == 1 ? View.VISIBLE : View.GONE);
                if (certification == 1) {
                    ic_certification.setVisibility(View.VISIBLE);
                } else {

                }
            } else {//女性用户
                ic_vip.setImageResource(R.drawable.ic_good_goddess);
                ic_vip.setVisibility(isVip == 1 ? View.VISIBLE : View.GONE);
                if (certification == 1) {
                    ic_certification.setVisibility(isVip == 1 ? View.GONE : View.VISIBLE);
                } else {
                    ic_certification.setVisibility(View.GONE);
                }
            }
            RecyclerView recyclerView = photoView.findViewById(R.id.photo_album_rcv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(photoView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            PhotoAlbumItemRecyclerAdapter adapter = new PhotoAlbumItemRecyclerAdapter(photoAlbumEntity.getImg());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((v, pos, itemEntity) -> {
                if (onItemClickListener != null)
                    onItemClickListener.clickToUserMain();
            });
            photoView.findViewById(R.id.photo_album_layout).setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.clickToUserMain();
            });
        }
        customJsonMsgContentFrame.addView(photoView);
    }

    /**
     * 修改通话消息相关样式
     * @param msg
     * @param callingType
     */
    private void setCallingMsgIconStyle(TUIMessageBean msg, int callingType) {
        if (msg.isSelf()) {
            mRightView.setBackgroundResource(callingType == 1 ?
                    R.drawable.custom_audio_right_img_2 : R.drawable.custom_video_right_img_1);
            mRightView.setVisibility(View.VISIBLE);
        } else {
            mLeftView.setBackgroundResource(callingType == 1 ?
                    R.drawable.custom_audio_left_img_2 : R.drawable.custom_video_left_img_1);
            mLeftView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 礼物消息
     *  @param msg
     * @param extra
     */
    private void setGiftMessageItemView(TUIMessageBean msg, String extra) {
        msgArea.setBackground(null);
        GiftEntity giftEntity = IMGsonUtils.fromJson(String.valueOf
                (TUIChatUtils.json2Massage(extra, "data")), GiftEntity.class);
        if (giftEntity != null) {
            View giftView = View.inflate(appContext, R.layout.custom_gift_view, null);
            RelativeLayout custom_gift_layout = giftView.findViewById(R.id.custom_gift_layout);
            ImageView gift_img = giftView.findViewById(R.id.gift_img);
            TextView gift_text = giftView.findViewById(R.id.gift_text);
            TextView gift_title = giftView.findViewById(R.id.gift_title);

            if (msg.isSelf()) {
                gift_title.setText(appContext.getString(R.string.custom_gift_left_title));
                gift_title.setTextColor(appContext.getResources().getColor(R.color.gift_right_color));
                gift_text.setTextColor(appContext.getResources().getColor(R.color.gift_right_txt_color));
                custom_gift_layout.setBackground(appContext.getResources().getDrawable(R.drawable.custom_right_gift_backdrop));
            } else {
                gift_title.setText(appContext.getString(R.string.custom_gift_right_title));
                gift_title.setTextColor(appContext.getResources().getColor(R.color.gift_left_color));
                gift_text.setTextColor(appContext.getResources().getColor(R.color.gift_left_txt_color));
                custom_gift_layout.setBackground(appContext.getResources().getDrawable(R.drawable.custom_left_gift_backdrop));
                if (giftEntity.getProfitTwd() != null) {
                    double total = giftEntity.getProfitTwd() * giftEntity.getAmount();
                    setProfitDetails(total+"", profitTip);
                }
            }
            gift_text.setText(giftEntity.getTitle() + " x" + giftEntity.getAmount());
            Glide.with(TUIChatService.getAppContext())
                    .load(ConfigUrl.getFullImageUrl(giftEntity.getImgPath()))
                    .error(R.drawable.photo_album_rcv_item_def_img)
                    .placeholder(R.drawable.photo_album_rcv_item_def_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gift_img);
            msgContentReservFrame.addView(giftView);
            msgContentFrame.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化系统消息的view和数据
     * @param extra
     */
    private void initServerDataAndView(String extra) {
        tipView = View.inflate(appContext, R.layout.message_adapter_content_server_tip, null);
        customTipText = tipView.findViewById(R.id.custom_tip_text);
        try {
            customIMTextEntity = IMGsonUtils.fromJson(TUIChatUtils.json2Massage(extra, "data"), CustomIMTextEntity.class);
        }catch (Exception ignored){
            setContentLayoutVisibility(false);
        }
    }

    /**
     * 设置内容字体和颜色
     * @param msg
     */
    public void setBackColor(TUIMessageBean msg) {
        if (properties.getChatContextFontSize() != 0) {
            msgBodyText.setTextSize(properties.getChatContextFontSize());
        }
        if (msg.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
        }
    }

}
