package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tencent.custom.ConfigUrl;
import com.tencent.custom.CustomIMTextEntity;
import com.tencent.custom.EvaluateItemEntity;
import com.tencent.custom.GiftEntity;
import com.tencent.custom.IMGsonUtils;
import com.tencent.custom.PhotoAlbumEntity;
import com.tencent.custom.PhotoAlbumItemEntity;
import com.tencent.custom.PhotoAlbumItemRecyclerAdapter;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.component.face.FaceManager;
import com.tencent.qcloud.tuikit.tuichat.ui.view.MyImageSpan;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.MessageRecyclerView;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageTextHolder extends MessageContentHolder {

    private TextView msgBodyText;
    private TextView chat_tips_tv,chat_system_tip_tv;
    private FrameLayout msg_content_fl_custom;

    public MessageTextHolder(View itemView) {
        super(itemView);
        msgBodyText = itemView.findViewById(R.id.msg_body_tv);
        chat_tips_tv = itemView.findViewById(R.id.chat_tips_tv);
        msg_content_fl_custom = itemView.findViewById(R.id.msg_content_fl_custom);
        chat_system_tip_tv = itemView.findViewById(R.id.chat_system_tip_tv);
    }

    /**
     * @return java.util.List<T>
     * @Desc TODO(将字符串转成List集合)
     * @author 彭石林
     * @parame [jsonString, cls]
     * @Date 2021/8/13
     */
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_text;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        msgBodyText.setVisibility(View.VISIBLE);
        chat_system_tip_tv.setVisibility(View.GONE);
        itemView.findViewById(R.id.content_tip).setVisibility(View.GONE);
        msg_content_fl_custom.removeAllViews();
        if (msg.getExtra() != null) {
            String text = String.valueOf(msg.getExtra());
            if (TUIChatUtils.isJSON2(text) && text.indexOf("type") != -1) {//做自定义通知判断
                Map<String, Object> map_data = new Gson().fromJson(String.valueOf(msg.getExtra()), Map.class);
                if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("toast_local")) {
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.VISIBLE);
                    chat_tips_tv.setTextSize(properties.getChatContextFontSize());
                    if (map_data.get("status") != null && map_data.get("status").equals("3") || map_data.get("status").equals("2")) {//发送真人认证提示 :已经发送过
                        String value = map_data.get("text").toString();
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(value);
                        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
                        stringBuilder.setSpan(blueSpan, value.length() - 4, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        chat_tips_tv.setText(stringBuilder);
                    } else {
                        FaceManager.handlerEmojiText(chat_tips_tv, map_data.get("text").toString(), false);
                    }
                    chat_tips_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickListener.onToastVipText(msg);
                        }
                    });
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_tag")) {//推送用户标签
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    msg_content_fl_custom.setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                    msgContentFrame.setVisibility(View.VISIBLE);
                    FaceManager.handlerEmojiText(msgBodyText, String.valueOf(map_data.get("text")), false);
                    setBackColor(msg);
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_countdown")) {//男方余额不足
                    msg_content_fl_custom.setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_photo")) {//弹窗相册
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    msg_content_fl_custom.setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.VISIBLE);
                    if (map_data.get("data") != null) {
                        PhotoAlbumEntity photoAlbumEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), PhotoAlbumEntity.class);//new Gson().fromJson(String.valueOf(map_data.get("data")), PhotoAlbumEntity.class);
                        if (photoAlbumEntity != null) {
                            int sex = photoAlbumEntity.getSex();
                            int isVip = photoAlbumEntity.getIsVip();
                            int certification = photoAlbumEntity.getCertification();
                            TextView conversation_title = itemView.findViewById(R.id.conversation_title);
                            conversation_title.setText(photoAlbumEntity.getNickname());
                            //加载头像
                            ImageView photo_album_img = itemView.findViewById(R.id.photo_album_img);
                            Glide.with(TUIChatService.getAppContext()).load(ConfigUrl.getFullImageUrl(photoAlbumEntity.getAvatar()))
                                    .error(R.drawable.photo_album_img_default)
                                    .placeholder(R.drawable.photo_album_img_default)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(photo_album_img);
                            if(sex==1){
                                if(isVip==1){
                                    ImageView ic_vip = itemView.findViewById(R.id.iv_vip);
                                    ic_vip.setImageResource(R.drawable.ic_vip);
                                    itemView.findViewById(R.id.iv_vip).setVisibility(View.VISIBLE);
                                }else{
                                    itemView.findViewById(R.id.iv_vip).setVisibility(View.GONE);
                                }
                                if(certification==1){
                                    itemView.findViewById(R.id.iv_certification).setVisibility(View.VISIBLE);
                                }else{

                                }
                            } else {//女性用户
                                if (isVip == 1) {
                                    ImageView ic_vip = itemView.findViewById(R.id.iv_vip);
                                    ic_vip.setImageResource(R.drawable.ic_good_goddess);
                                    itemView.findViewById(R.id.iv_vip).setVisibility(View.VISIBLE);
                                }else{
                                    itemView.findViewById(R.id.iv_vip).setVisibility(View.GONE);
                                }
                                if(certification==1){
                                    if(isVip==1){
                                        itemView.findViewById(R.id.iv_certification).setVisibility(View.GONE);
                                    }else{
                                        itemView.findViewById(R.id.iv_certification).setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    itemView.findViewById(R.id.iv_certification).setVisibility(View.GONE);
                                }
                            }
                            RecyclerView recyclerView = itemView.findViewById(R.id.photo_album_rcv);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            PhotoAlbumItemRecyclerAdapter adapter = new PhotoAlbumItemRecyclerAdapter(photoAlbumEntity.getImg());
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new PhotoAlbumItemRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(View v, int pos, PhotoAlbumItemEntity itemEntity) {
                                    //onItemClickListener.openUserImage(itemEntity);
                                    onItemClickListener.clickToUserMain();
                                }
                            });
                            itemView.findViewById(R.id.photo_album_right_acc).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //onItemClickListener.toUserHome();
                                    onItemClickListener.clickToUserMain();
                                }
                            });
                            itemView.findViewById(R.id.photo_album_layout_item).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.clickToUserMain();
                                }
                            });
                            itemView.findViewById(R.id.photo_album_layout).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.clickToUserMain();
                                }
                            });
                        }
                    }
                }else if(map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_evaluate")){//评价
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    if(!MessageRecyclerView.sex){
                        itemView.findViewById(R.id.im_evaluation_layout_sex).setBackgroundResource(R.drawable.im_evaluation_layout_male);
                    }else{
                        itemView.findViewById(R.id.im_evaluation_layout_sex).setBackgroundResource(R.drawable.im_evaluation_layout_gril);
                    }
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.VISIBLE);
                    if(map_data.get("data")!=null){
                        List<EvaluateItemEntity> photoAlbumList = getObjectList(String.valueOf(map_data.get("data")), EvaluateItemEntity.class);
                        if(photoAlbumList!=null){
                            EvaluateItemEntity evaluateItemEntity1 = photoAlbumList.get(0);
                            EvaluateItemEntity evaluateItemEntity2 = photoAlbumList.get(1);
                            EvaluateItemEntity evaluateItemEntity3 = photoAlbumList.get(2);
                            TextView evaluation_tag1 = itemView.findViewById(R.id.evaluation_tag1);
                            TextView evaluation_tag2 = itemView.findViewById(R.id.evaluation_tag2);
                            TextView evaluation_tag3 = itemView.findViewById(R.id.evaluation_tag3);
                            TextView evaluation_tag4 = itemView.findViewById(R.id.evaluation_tag4);
                            evaluation_tag1.setText(evaluateItemEntity1.getName());
                            evaluation_tag2.setText(evaluateItemEntity2.getName());
                            evaluation_tag3.setText(evaluateItemEntity3.getName());
                            evaluation_tag1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.onClickEvaluate(position,msg,evaluateItemEntity1,false);
                                }
                            });
                            evaluation_tag2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.onClickEvaluate(position,msg,evaluateItemEntity2,false);
                                }
                            });
                            evaluation_tag3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.onClickEvaluate(position,msg,evaluateItemEntity3,false);
                                }
                            });
                            evaluation_tag4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onItemClickListener.onClickEvaluate(position, msg, null, true);
                                }
                            });
                        }
                    }
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_gift")) {//礼物
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.GONE);
                    msg_content_fl_custom.setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    GiftEntity giftEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), GiftEntity.class);
                    if (giftEntity != null) {
                        View GiftView = View.inflate(itemView.getContext(), R.layout.custom_gift_view, null);
                        RelativeLayout custom_gift_layout = GiftView.findViewById(R.id.custom_gift_layout);
                        custom_gift_layout.setBackground(null);
                        ImageView gift_img = GiftView.findViewById(R.id.gift_img);
                        TextView gift_text = GiftView.findViewById(R.id.gift_text);
                        TextView gift_title = GiftView.findViewById(R.id.gift_title);

                        if (msg.isSelf()) {
                            gift_title.setText(itemView.getContext().getString(R.string.custom_gift_left_title));
                            gift_title.setTextColor(itemView.getResources().getColor(R.color.gift_right_color));
                            gift_text.setTextColor(itemView.getResources().getColor(R.color.gift_right_txt_color));
                            custom_gift_layout.setBackground(itemView.getContext().getDrawable(R.drawable.custom_right_gift_backdrop));
                            GiftView.findViewById(R.id.custom_gift_hint_text).setVisibility(View.GONE);
                        } else {
                            gift_title.setText(itemView.getContext().getString(R.string.custom_gift_right_title));
                            gift_title.setTextColor(itemView.getResources().getColor(R.color.gift_left_color));
                            gift_text.setTextColor(itemView.getResources().getColor(R.color.gift_left_txt_color));
                            custom_gift_layout.setBackground(itemView.getContext().getDrawable(R.drawable.custom_left_gift_backdrop));
                            TextView custom_gift_hint_text = GiftView.findViewById(R.id.custom_gift_hint_text);
                            custom_gift_hint_text.setVisibility(View.VISIBLE);
                            if (MessageRecyclerView.sex) {
                                if (giftEntity.getProfitDiamond() != null) {
                                    String custom_message_txt7 = itemView.getContext().getString(R.string.custom_message_txt7);
                                    String custom_message_txt7_val = (giftEntity.getProfitDiamond() * giftEntity.getAmount())+"";
                                    custom_gift_hint_text.setText(String.format(custom_message_txt7, custom_message_txt7_val));
                                }
                            } else {
                                if (giftEntity.getProfitTwd() != null) {
                                    double total = giftEntity.getProfitTwd() * giftEntity.getAmount();
                                    if (MessageRecyclerView.isCertification()) {
                                        String custom_message_txt2 = itemView.getContext().getString(R.string.profit);
                                        String format = String.format(custom_message_txt2, String.format("%.2f", total));
                                        SpannableString iconSpannable = new SpannableString(format);
                                        iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        custom_gift_hint_text.setText(iconSpannable);
                                    } else {
                                        String custom_message_txt2 = itemView.getContext().getString(R.string.custom_message_txt2_test2);
                                        SpannableString iconSpannable = matcherSearchText("#A72DFE", String.format(custom_message_txt2, String.format("%.2f", total)), itemView.getContext().getString(R.string.custom_message_txt1_key));
                                        iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        custom_gift_hint_text.setText(iconSpannable);
                                        custom_gift_hint_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onItemClickListener.onClickCustomText();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        if (!MessageRecyclerView.isFlagTipMoney() || msg.isSelf()) {
                            GiftView.findViewById(R.id.custom_gift_hint_text).setVisibility(View.GONE);
                        } else {
                            GiftView.findViewById(R.id.custom_gift_hint_text).setVisibility(View.VISIBLE);
                        }
                        gift_text.setText(giftEntity.getTitle() + " x" + giftEntity.getAmount());
                        Glide.with(TUIChatService.getAppContext())
                                .load(ConfigUrl.getFullImageUrl(giftEntity.getImgPath()))
                                .error(R.drawable.photo_album_rcv_item_def_img)
                                .placeholder(R.drawable.photo_album_rcv_item_def_img)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(gift_img);
                        msg_content_fl_custom.addView(GiftView);
                    }
                } else if (map_data != null && map_data.get("type") != null && (map_data.get("type").equals("message_custom") || map_data.get("type").equals("message_tracking"))) {//自定义消息体
                    msg_content_fl_custom.setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.VISIBLE);

                    TextView customHintText = itemView.findViewById(R.id.custom_hint_text);
                    CustomIMTextEntity customIMTextEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), CustomIMTextEntity.class);
                    LinearLayout.LayoutParams customHintTextLayoutParams2 = (LinearLayout.LayoutParams) customHintText.getLayoutParams();
                    if (map_data.get("type").equals("message_tracking")) {
                        itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                        itemView.findViewById(R.id.custom_sufficient_view).setVisibility(View.GONE);
                    } else {
                        if (customIMTextEntity != null) {
                            //callingtype：1语音，2视频
                            int totalSeconds = customIMTextEntity.getTotalSeconds();
                            int callingType = customIMTextEntity.getCallingType();
                            if (totalSeconds > 0 && callingType> 0){
                                customHintText = itemView.findViewById(R.id.content_tip);
                                msgContentFrame.setVisibility(View.VISIBLE);
                                itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                                if (msg.isSelf()) {
                                    if (properties.getRightChatContentFontColor() != 0) {
                                        msgBodyText.setTextColor(properties.getRightChatContentFontColor());
                                    }
                                    itemView.findViewById(R.id.left_call_img).setVisibility(View.GONE);
                                    itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                                    itemView.findViewById(R.id.right_call_img).setBackgroundResource(callingType == 1 ? R.drawable.custom_audio_right_img_1 : R.drawable.custom_video_right_img_1);
                                }else {
                                    itemView.findViewById(R.id.right_call_img).setVisibility(View.GONE);
                                    itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                                    itemView.findViewById(R.id.left_call_img).setBackgroundResource(callingType == 1 ? R.drawable.custom_audio_left_img_1 : R.drawable.custom_video_left_img_1);
                                    if (properties.getLeftChatContentFontColor() != 0) {
                                        msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
                                    }
                                }
                                if (properties.getChatContextFontSize() != 0) {
                                    msgBodyText.setTextSize(properties.getChatContextFontSize());
                                }
                                msgBodyText.setText(itemView.getContext().getString(R.string.custom_message_call_message_deatail_time_msg,totalSeconds/3600, totalSeconds/60,totalSeconds%60));
                            }else {
                                itemView.findViewById(R.id.custom_sufficient_view_tip).setVisibility(View.GONE);
                                msgContentFrame.setVisibility(View.GONE);
                                itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                            }
                            if (customIMTextEntity.getContent() == null) {
                                if (MessageRecyclerView.sex) {
                                    //余额不足-需要充值
                                    if (customIMTextEntity.getIsRemindPay() != null && customIMTextEntity.getIsRemindPay().intValue() > 0) {
                                        if (totalSeconds > 0 && callingType> 0){
                                            itemView.findViewById(R.id.custom_sufficient_view_tip).setVisibility(View.VISIBLE);
                                            itemView.findViewById(R.id.custom_sufficient_view_tip).setOnClickListener(v -> {
                                                onItemClickListener.onClickDialogRechargeShow();
                                            });
                                        }else {
                                            itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                                        }
                                        FrameLayout custom_sufficient_view = itemView.findViewById(R.id.custom_sufficient_view);
                                        View custom_not_sufficient_view = View.inflate(itemView.getContext(), R.layout.custom_not_sufficient_view, null);
                                        LinearLayout male_hint_layout = custom_not_sufficient_view.findViewById(R.id.male_hint_layout);
                                        male_hint_layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onItemClickListener.onClickDialogRechargeShow();
                                            }
                                        });
                                        customHintText.setVisibility(View.GONE);
                                        custom_sufficient_view.addView(custom_not_sufficient_view);
                                        custom_sufficient_view.setVisibility(View.VISIBLE);
                                    } else {
                                        customHintText.setVisibility(View.VISIBLE);
                                        itemView.findViewById(R.id.custom_sufficient_view).setVisibility(View.GONE);
                                        itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                                    }

                                } else {
                                    itemView.findViewById(R.id.custom_sufficient_view).setVisibility(View.GONE);
                                    if (Double.valueOf(customIMTextEntity.getPrice()).doubleValue() > 0) {
                                        if (MessageRecyclerView.isCertification()) {
                                            String custom_message_txt2 = itemView.getContext().getString(R.string.profit);
                                            String format = String.format(custom_message_txt2, customIMTextEntity.getPrice());
                                            SpannableString iconSpannable = new SpannableString(format);
                                            iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            customHintText.setText(iconSpannable);
                                        } else {
                                            customHintTextLayoutParams2.gravity = Gravity.CENTER;
                                            customHintTextLayoutParams2.rightMargin = dip2px(itemView.getContext(), 0);
                                            String custom_message_txt2 = itemView.getContext().getString(R.string.custom_message_txt2_test2);
                                            SpannableString iconSpannable = matcherSearchText("#A72DFE", String.format(custom_message_txt2, String.format("%.2f", customIMTextEntity.getPrice())), itemView.getContext().getString(R.string.custom_message_txt1_key));
                                            iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            customHintText.setText(iconSpannable);
                                            customHintText.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    onItemClickListener.onClickCustomText();
                                                }
                                            });
                                        }
                                    } else {
                                        if (customIMTextEntity.getIsRemindPay() != null && customIMTextEntity.getIsRemindPay().intValue() > 0) {
                                            String custom_message_txt3 = itemView.getContext().getString(R.string.custom_message_txt3);
                                            customHintText.setText(custom_message_txt3);
                                        } else {
                                            itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                                        }
                                    }
                                    customHintText.setVisibility(View.VISIBLE);

                                }
                            } else {
                                String likeText = "一鍵追蹤她，不再失聯噢 已追蹤";
                                if (MessageRecyclerView.addLikeMsgId != null && msg.getId().equals(MessageRecyclerView.addLikeMsgId)) {
                                    customIMTextEntity.setContent(likeText);
                                    customIMTextEntity.setEvent(-1);
                                    customIMTextEntity.setKey(null);
                                }
                                if (customIMTextEntity.getKey() != null) {
                                    //</font>
                                    if (customIMTextEntity.getContent().indexOf("<font>") != -1) {
                                        String fontText = "<font color='" + customIMTextEntity.getColor() + "'>" + customIMTextEntity.getKey() + "</font>";
                                        String content = customIMTextEntity.getContent();
                                        String CDATAText = content.replace("<font>" + customIMTextEntity.getKey() + "</font>", fontText);

                                        customHintText.setText(Html.fromHtml(CDATAText));
                                    } else {
                                        customHintText.setText(matcherSearchText(customIMTextEntity.getColor(), customIMTextEntity.getContent(), customIMTextEntity.getKey()));
                                    }
                                } else {
                                    customHintText.setText(customIMTextEntity.getContent());
                                }
                                if (customIMTextEntity.getGravity() != null) {
                                    LinearLayout.LayoutParams customHintTextLayoutParams = (LinearLayout.LayoutParams) customHintText.getLayoutParams();
                                    if (customIMTextEntity.getGravity().equals("left")) {
                                        customHintTextLayoutParams.gravity = Gravity.START;
                                        customHintTextLayoutParams.leftMargin = dip2px(itemView.getContext(), customIMTextEntity.getMargin());
                                    } else if (customIMTextEntity.getGravity().equals("right")) {
                                        customHintTextLayoutParams.gravity = Gravity.END;
                                        customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), customIMTextEntity.getMargin());
                                    } else {
                                        customHintTextLayoutParams.gravity = Gravity.CENTER;
                                    }
                                    customHintText.setLayoutParams(customHintTextLayoutParams);
                                }

                                customHintText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onItemClickListener.onClickCustomText(position, msg, customIMTextEntity);
                                    }
                                });
                            }
                            if (totalSeconds > 0 && callingType> 0){
                                itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                            }else {
                                if (!MessageRecyclerView.isFlagTipMoney()) {
                                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                                }else {
                                    itemView.findViewById(R.id.custom_layout).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("send_violation_message")) {//发送违规消息
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.message_sending_pb).setVisibility(View.GONE);
                    itemView.findViewById(R.id.message_status_iv).setVisibility(View.VISIBLE);
                    msgContentFrame.setVisibility(View.VISIBLE);
                    msgBodyText.setVisibility(View.VISIBLE);
                    msgBodyText.setText(map_data.get("text").toString());
                    String s = itemView.getContext().getString(R.string.custom_send_violation_message_tip);
                    chat_system_tip_tv.setVisibility(View.VISIBLE);
                    chat_system_tip_tv.setText(s);
                    chat_system_tip_tv.setTextSize(11);
                    msgBodyText.setTextColor(itemView.getResources().getColor(R.color.white));
                    msgBodyText.setTextSize(14);

                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("message_callingbusy")) {//忙線推送
                    View custom_gift_layout_max = itemView.findViewById(R.id.custom_gift_layout_max);
                    if (custom_gift_layout_max != null) {
                        custom_gift_layout_max.setVisibility(View.GONE);
                    }
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    msg_content_fl_custom.setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    String s = itemView.getContext().getString(R.string.custom_message_book_next_call);
                    TextView viewById = (TextView) itemView.findViewById(R.id.chat_system_tip_tv);
                    viewById.setVisibility(View.VISIBLE);
                    viewById.setText(s);
                    String data = (String) map_data.get("data");
                    Map<String, Object> callData = new Gson().fromJson(data, Map.class);
                    int callType = Double.valueOf(String.valueOf(callData.get("callingType"))).intValue();
                    if (properties.getChatContextFontSize() != 0) {
                        msgBodyText.setTextSize(properties.getChatContextFontSize());
                    }
                    if (msg.isSelf()) {
                        itemView.findViewById(R.id.right_call_img).setBackgroundResource(callType == 1 ?
                                R.drawable.custom_audio_right_img_2 : R.drawable.custom_video_right_img_1);
                        itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                        msgBodyText.setText(itemView.getContext().getString(R.string.custom_message_other_busy));
                        if (properties.getRightChatContentFontColor() != 0) {
                            msgBodyText.setTextColor(properties.getRightChatContentFontColor());
                        }
                    } else {
                        itemView.findViewById(R.id.left_call_img).setBackgroundResource(callType == 1 ?
                                R.drawable.custom_audio_left_img_2 : R.drawable.custom_video_left_img_1);
                        itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                        msgBodyText.setText(itemView.getContext().getString(R.string.custom_message_busy_missed));
                        if (properties.getLeftChatContentFontColor() != 0) {
                            msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
                        }
                    }
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("send_male_error")){//自定义消息体

                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.VISIBLE);
                    LinearLayout male_hint_error_layout = itemView.findViewById(R.id.male_hint_error_layout);
                    //点击唤醒充值
                    male_hint_error_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onClickDialogRechargeShow();
                        }
                    });
                } else if (map_data != null && map_data.get("type") != null && map_data.get("type").equals("chat_earnings")) {//收益提示
                    msg_content_fl_custom.setVisibility(View.GONE);
                    msgContentFrame.setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.GONE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.VISIBLE);
                    if (!MessageRecyclerView.isFlagTipMoney()) {
                        itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    } else {
                        itemView.findViewById(R.id.custom_layout).setVisibility(View.VISIBLE);
                    }
                    TextView customHintText = itemView.findViewById(R.id.custom_hint_text);
                    CustomIMTextEntity customIMTextEntity = IMGsonUtils.fromJson(String.valueOf(map_data.get("data")), CustomIMTextEntity.class);
                    String custom_message_txt2 = "";
                    LinearLayout.LayoutParams customHintTextLayoutParams = (LinearLayout.LayoutParams) customHintText.getLayoutParams();
                    if (customIMTextEntity.getIsRefundMoney() != null) {
                        if (MessageRecyclerView.sex) {
                            custom_message_txt2 = itemView.getContext().getString(R.string.custom_message_txt_male);
                            if (!msg.isSelf()) {
                                customHintTextLayoutParams.gravity = Gravity.END;
                                customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), 62);
                            } else {
                                customHintTextLayoutParams.gravity = Gravity.START;
                                customHintTextLayoutParams.leftMargin = dip2px(itemView.getContext(), 62);
                            }
                            customHintText.setText(custom_message_txt2);
                        } else {
                            custom_message_txt2 = itemView.getContext().getString(R.string.custom_message_txt_girl);
                            if (!msg.isSelf()) {
                                customHintTextLayoutParams.gravity = Gravity.END;
                                customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), 62);
                            } else {
                                customHintTextLayoutParams.gravity = Gravity.START;
                                customHintTextLayoutParams.leftMargin = dip2px(itemView.getContext(), 62);
                            }
                            customHintText.setText(custom_message_txt2);
                        }
                    } else {
                        if (MessageRecyclerView.isCertification()) {
                            custom_message_txt2 = itemView.getContext().getString(R.string.profit);
                            customHintTextLayoutParams.gravity = Gravity.END;
                            customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), 62);
                            String format = String.format(custom_message_txt2, customIMTextEntity.getTextProfit());
                            SpannableString iconSpannable = new SpannableString(format);
                            iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            customHintText.setText(iconSpannable);
                        } else {
                            customIMTextEntity.setEvent(11);
                            customHintTextLayoutParams.gravity = Gravity.CENTER;
                            customHintTextLayoutParams.rightMargin = dip2px(itemView.getContext(), 0);
                            custom_message_txt2 = itemView.getContext().getString(R.string.custom_message_txt1);
                            SpannableString iconSpannable = matcherSearchText("#A72DFE", String.format(custom_message_txt2, String.format("%.2f", customIMTextEntity.getTextProfit())), itemView.getContext().getString(R.string.custom_message_txt1_key));
                            iconSpannable.setSpan(new MyImageSpan(itemView.getContext(),R.drawable.icon_crystal),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            customHintText.setText(iconSpannable);
                        }
                    }

                    customHintText.setLayoutParams(customHintTextLayoutParams);
                    customHintText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onClickCustomText(position, msg, customIMTextEntity);
                        }
                    });
                } else {
                    itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                    itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                    itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                    FaceManager.handlerEmojiText(msgBodyText, msg.getExtra().toString(), false);
                }
            } else {//正常接收消息
                View custom_gift_layout_max = itemView.findViewById(R.id.custom_gift_layout_max);
                if (custom_gift_layout_max != null) {
                    custom_gift_layout_max.setVisibility(View.GONE);
                }
                itemView.findViewById(R.id.custom_error_layout).setVisibility(View.GONE);
                itemView.findViewById(R.id.custom_layout).setVisibility(View.GONE);
                msg_content_fl_custom.setVisibility(View.GONE);
                msgContentFrame.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.im_evaluation_layout).setVisibility(View.GONE);
                itemView.findViewById(R.id.photo_album_layout).setVisibility(View.GONE);
                itemView.findViewById(R.id.user_content).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.full_toast).setVisibility(View.GONE);
                String extra = msg.getExtra().toString();
                //信令消息
                if (msg.getV2TIMMessage() != null && msg.getV2TIMMessage().getCustomElem() != null && msg.getCustomElemData() != null) {
                    Map<String, Object> signallingData = IMGsonUtils.fromJson(new String(msg.getCustomElemData()), Map.class);
                    String customElem = new String(msg.getCustomElemData());
                    //音视频通话
                    if (customElem.indexOf("av_call") != -1) {
                        if (signallingData.get("actionType") != null) {
                            Map<String, Object> callData = IMGsonUtils.fromJson(String.valueOf(signallingData.get("data")), Map.class);
                            if (callData != null) {
                                int actionType = Double.valueOf(String.valueOf(signallingData.get("actionType"))).intValue();
                                int callType = Double.valueOf(String.valueOf(callData.get("call_type"))).intValue();
                                if (msg.isSelf()) {
                                    itemView.findViewById(R.id.left_call_img).setVisibility(View.GONE);
                                    if (actionType == 1) {//发起通话
                                        itemView.findViewById(R.id.right_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_right_img_1 : R.drawable.custom_video_right_img_1);
                                        itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 2) {//取消通话
                                        itemView.findViewById(R.id.right_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_right_img_2 : R.drawable.custom_video_right_img_1);
                                        itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 4) {//接听电话
                                        itemView.findViewById(R.id.right_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_right_img_2 : R.drawable.custom_video_right_img_1);
                                        itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 7) {//接听电话
                                        itemView.findViewById(R.id.right_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_right_img_2 : R.drawable.custom_video_right_img_1);
                                        itemView.findViewById(R.id.right_call_img).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    itemView.findViewById(R.id.right_call_img).setVisibility(View.GONE);
                                    if (actionType == 1) {//发起通话
                                        itemView.findViewById(R.id.left_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_left_img_1 : R.drawable.custom_video_left_img_1);
                                        itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 2) {//取消通话
                                        itemView.findViewById(R.id.left_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_left_img_2 : R.drawable.custom_video_left_img_1);
                                        itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 4) {//接听电话
                                        itemView.findViewById(R.id.left_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_left_img_2 : R.drawable.custom_video_left_img_1);
                                        itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                                    } else if (actionType == 7) {//接听电话
                                        itemView.findViewById(R.id.left_call_img).setBackgroundResource(callType == 1 ? R.drawable.custom_audio_left_img_2 : R.drawable.custom_video_left_img_1);
                                        itemView.findViewById(R.id.left_call_img).setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    itemView.findViewById(R.id.right_call_img).setVisibility(View.GONE);
                    itemView.findViewById(R.id.left_call_img).setVisibility(View.GONE);
                }
                if (extra != null && extra.indexOf("href") != -1 && extra.indexOf("</a>") != -1) {
                    CharSequence charSequence = Html.fromHtml(extra);
                    msgBodyText.setText(charSequence);
                    msgBodyText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickListener.onTextTOWebView(msg);
                        }
                    });
                } else {
                    FaceManager.handlerEmojiText(msgBodyText, msg.getExtra().toString(), false);
                }
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
    }

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

    /**
     * 正则匹配 返回值是一个SpannableString 即经过变色处理的数据
     */
    public SpannableString matcherSearchText(String color, String text, String keyword) {
        if (text == null || TextUtils.isEmpty(text)) {
            return SpannableString.valueOf("");
        }
        SpannableString spannableString = new SpannableString(text);
        //条件 keyword
        Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
        //匹配
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            //ForegroundColorSpan 需要new 不然也只能是部分变色
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //返回变色处理的结果
        return spannableString;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int imUserIdToSystemUserId(String userId) {
        try {
            String strId = userId.replaceFirst("ru_", "");
            return Integer.parseInt(strId);
        } catch (Exception e) {
            return 0;
        }
    }

}
