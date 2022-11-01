package com.fine.friendlycc.app;


/**
 * @ClassName AppsFlyerEvent
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/6/3 17:47
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public interface AppsFlyerEvent {

    String main_open = "main_open";//页面登录成功首次加载
    //静默登录
    String Silent_login = "Silent_login";
    //登录界面
    String Login_screen = "Login_screen";
    //facebook登录
    String LOG_IN_WITH_FACEBOOK = "LOG_IN_WITH_FACEBOOK";
    //google登录
    String LOG_IN_WITH_GOOGLE = "LOG_IN_WITH_GOOGLE";
    //手机登录
    String LOG_IN_WITH_PHONE_NUMBER = "LOG_IN_WITH_PHONE_NUMBER";
    //注册填写资料/注册完成
    String LOG_Edit_Profile = "LOG_Edit_Profile";
    //编辑个人资料
    String Edit_Profile = "Edit_Profile";
    //首次注册事件
    String register_start = "register_start";

    //广播
    String Broadcast = "Broadcast";
    //约会内容入口1、2、3、4、5、6	广播
    String Dating_1 = "Dating_1";
    String Dating_2 = "Dating_2";
    String Dating_3 = "Dating_3";
    String Dating_4 = "Dating_4";
    String Dating_5 = "Dating_5";
    String Dating_6 = "Dating_6";

    //发布內容	广播
    String Post1 = "Post1";
    //发布节目	广播	发布
    String Post_Dating = "Post_Dating";
    //发布动态	广播	发布
    String Post_Moment = "Post_Moment";
    //发布时间（筛选）	广播
    String Time_optional = "Time_optional";
    //发布时间	广播	发布时间（筛选）
    String Post_Time = "Post_Time";
    //约会时间	广播	发布时间（筛选）
    String Dating_Time = "Dating_Time";
    //不限性别（筛选）	广播
    String Gender_optional = "Gender_optional";
    //只看男士	广播	不限性别（筛选）
    String Male_Only = "Male_Only";
    //只看女士	广播	不限性别（筛选）
    String Female_Only = "Female_Only";
    //不限地区（筛选）	广播
    String Region = "Region";

    //点赞	广播	约会详情/约会详情	她/他的动态
    String Like = "Like";
    //留言	广播	约会详情/约会详情
    String Message = "Message";
    //报名约会	广播	约会详情
    String Apply = "Apply";
    //匿名检举	广播
    String Report = "Report";

    String Like_2 = "Like_2";//点赞2	广播	动态详情
    String Message_2 = "Message_2";//	留言2	广播	动态详情
    String Report_2 = "Report_2";//	匿名检举2	广播	动态详情
    // String Like_3 = "Like_3";//	点赞3	广播	动态详情
    String User_Page_1 = "User_Page_1";//	用户主页1	广播	动态详情
    String Message_3 = "Message_3";//	留言3	广播	活动详情
    String Apply_3 = "Apply_3";//	报名约会3	广播	活动详情
    String Report_3 = "Report_3";//	匿名检举3	广播	活动详情
    String User_Page_2 = "User_Page_2";//	用户主页2	广播	活动详情
    String Post_jia = "Post(+)";//發佈（+）	广播


    //各个约会入口	广播
    String Dating = "Dating";

    String Moment = "Moment"; //發佈動態	广播	發佈（+）
    String Dating_subject = "Dating_subject_";//用来做前端传值拼接 Dating_subject_1、Dating_subject_2....
//    String Dating_subject_1 = "Dating_subject_1";//	選擇約會節目1	广播	發佈（+）
//    String Dating_subject_2 = "Dating_subject_2";//	選擇約會節目2	广播	發佈（+）
//    String Dating_subject_3 = "Dating_subject_3";//	選擇約會節目3	广播	發佈（+）
//    String Dating_subject_4 = "Dating_subject_4";//	選擇約會節目4	广播	發佈（+）
//    String Dating_subject_5 = "Dating_subject_5";//	選擇約會節目5	广播	發佈（+）
//    String Dating_subject_6 = "Dating_subject_6";//	選擇約會節目6	广播	發佈（+）

    String Location = "Location";//	約會地點	广播	發佈（+）	選擇約會節目
    String Ideal_Person = "Ideal_Person"; //	約會對象	广播	發佈（+）	選擇約會節目
    String Dating_date = "Dating_date";    //約會日期	广播	發佈（+）	選擇約會節目
    String Dating_time = "Dating_time";    //約會時間	广播	發佈（+）	選擇約會節目
    String Upload_Photo = "Upload_Photo";//（+）	上傳照片（+）	广播	發佈（+）	選擇約會節目
    String Set_as_Private = "Set_as_Private";//	禁止留言	广播	發佈（+）	選擇約會節目
    String Hide_to_same_sex_users = "Hide_to_same_sex_users";//	對同性用戶隱藏	广播	發佈（+）	選擇約會節目
    String Post2 = "Post2";    //發佈	广播	發佈（+）	選擇約會節目


    String Nearby = "Nearby";//	附近
    String Nearby_Search = "Nearby_Search";//(符号)	搜索	附近
    String Nearby_Change_gender = "Nearby_Change_gender";//	切换性别	附近
    String Nearby_Nearby_2 = "Nearby_Nearby_2";//	附近（切换城市）	附近
    String Nearby_Online_First = "Nearby_Online_First";//	当前在线优先	附近
    String Nearby_1 = "Nearby_1";//	附近1（标签）	附近
    String Nearby_New = "Nearby_New";//	新注册	附近
    String Nearby_Goddess = "Nearby_Goddess";//	女神	附近
    String Nearby_VIP_Nearby = "Nearby_VIP_Nearby";//VIP	附近
    String Nearby_Follow = "Nearby_Follow";// (符号)	追蹤	附近	用戶主頁

    String Ranking = "Ranking";//	排行榜
    String User_Page_3 = "User_Page_3";//	用户主页3	排行榜

    String Messages = "Messages";//讯息
    String Chat = "Chat";//	聊天	讯息
    String System_Messages = "System_Messages";//	官方訊息	訊息
    String IM = "IM";//	im	訊息	聊天
    String IM_Unlock = "IM_Unlock";//	解鎖訊息	讯息	聊天	IM
    String IM_Get_Privilege = "IM_Get_Privilege";//	立即訂閱	讯息	聊天	IM
    String IM_Subscribe_Success = "IM_Subscribe_Success"; //IM-支付成功
    String IM_Subscribe = "IM_Subscribe";//	订阅	訊息	聊天	IM	解鎖訊息	立即訂閱
    String User_Page_4 = "User_Page_4";//	用户主页4	讯息	聊天

    //电子钱包
    String Wallet = "Wallet";
    //发起储值(各个商品按钮）	我的	钱包
    String Top_up = "Top_up";
    String Select_diamonds_purchase = "Select_diamonds_purchase";//	選擇購買鑽石數量	我的	電子錢包	儲值
    String One_Click_Purchase = "One_Click_Purchase";// 一鍵購買	我的	電子錢包	儲值	選擇購買鑽石數量
    String Successful_top_up = "Successful_top_up";//	儲值成功	我的	電子錢包	儲值	選擇購買鑽石數量	一鍵購買
    String Failed_to_top_up = "Failed_to_top_up";//	儲值失敗	我的	電子錢包	儲值	選擇購買鑽石數量	一鍵購買
    String success_diamond_top_up = "success_diamond_top_up";//充值钻石

    //追踪名单	我的
    String Following = "Following";
    String VIP_Center = "VIP Center";//會員中心	我的
    String VIP_1 = "VIP_1";//	套餐1	我的	會員中心
    String VIP_2 = "VIP_2";//	套餐2	我的	會員中心
    String VIP_3 = "VIP_3";//	套餐3	我的	會員中心
    String VIP_4 = "VIP_4";//	套餐4	我的	會員中心
    String Get_vip = "Get_vip";//	領取	我的	會員中心
    String Subscribe = "Subscribe";//	訂閱	我的	會員中心	領取
    String Subscribe_Successfully = "Subscribe_Successfully";//	訂閱成功	我的	會員中心	領取	訂閱
    String Failed_to_Subscribe = "Failed_to_Subscribe";//	訂閱失敗	我的	會員中心	領取	訂閱


    String Verify_Your_Profile = "Verify_Your_Profile";//	認證中心	我的
    //真人认证	我的
    String Identity_Verification = "Identity Verification";

    //上传照片（+）	我的	真人认证	真人认证
    String Add_photo = "Add photo（+）";
    //下一步	我的	真人认证	真人认证（上传照片）
    String Next_step = "Next step";
    //开始辨识	我的	真人认证	真人认证（脸部辨识）
    String Verify = "Verify";
    //女神认证	我的	真人认证
    //String Goddess_Verification = "Goddess Verification";
    //提交女神认证照片（+）	我的	真人认证	女神认证
    String Confirm = "Confirm";

    //我的廣播	我的
    String My_Post = "My_Post";

    //上传照片/影片	我的
    String Add_photos_Video = "Add photos/Video";
    //上傳照片
    String me_Upload_Photo = "me_Upload_Photo";
    //上传照片成功
    String me_Upload_Photo_succeed = "me_Upload_Photo_succeed";
    //设置红包照片	我的
    //String Paid = "Paid";

    String All_Recommended = "All_Recommended";//	公開	我的	相簿設計
    String Paid_me = "Paid_me";//	付費解鎖	我的	相簿設計
    String Ask_me = "Ask_me";//	查看前需通過我的授權	我的	相簿設計
    String cancel_me = "cancel_me";//取消	我的	相簿設計
    //相簿设定	我的
    String Photos_settings = "Photos settings";
    //隱私設定	我的
    String Privacy_Settings = "Privacy_Settings";
    //封锁设定	我的
    String Blocked_List = "Blocked List";
    //系统设定	我的
    String System_Settings = "System Settings";
    //用户回馈	我的
    String Contact_Us = "Contact_Us";
    //我的评价	我的
    String Impression = "Impression";
    //他/她的动态	用户主页
    String His_Her_Monents = "His/Her Monents";

    String Me = "Me"; //用户主页
    String Following_2 = "Following_2";//	追蹤	用戶主頁
    String View_photos = "View_photos";// 用户主页-查看照片
    String Impression_2 = "Impression_2";//	评价	用户主页
    String Send_message = "Send_message";//	发讯息	用户主页
    String Social_Account = "Social_Account";
    //升级VIP解锁社交账号	用户主页
    String Become_A_VIP = "Become_A_VIP";
    //非VIP解锁解锁社交账号	用户主页
    String Unlock_Now = "Unlock_Now";
    //VIP解锁社交账号	用户主页
    String Unlock = "Unlock";
    //升级VIP	IM
    String Get_Privilege = "Get_Privilege";
    //订阅	IM	Google Play
    String Subscribe_im = "Subscribe_im";

    //每日推荐弹窗的弹出次數（daily_recommend），
    String daily_recommend = "daily_recommend";
    //点击次数（daily_recommend_click）
    String daily_recommend_click = "daily_recommend_click";
    //用户主动关闭次数即点击X的次数(daily_recommend__close）)
    String daily_recommend__close = "daily_recommend__close";
    //倒计时完成关闭
    String daily_recommendTimeClose = "daily_recommendTimeClose";

    String visitor_locked = "visitor_locked";
    String visitor_unlocked = "visitor_unlocked";
    String unlock_my_visitor = "unlock_my_visitor";
    //签到天数
    String sign_day = "sign_day";
    //进入任务中心埋点
    String task_center = "task_center";
    //签到3天弹出VIP领取
    String sign_day3_vip = "sign_day3_vip";
    //签到7天弹出vip领取
    String sign_day7_vip = "sign_day7_vip";
    //查看弹窗的弹出次数	弹出时上报
    String pop_ad_show = "pop_ad_show";
    //查看弹窗点击次数	点击时上报
    String pop_ad_click = "pop_ad_click";
    String pop_ad_close = "pop_ad_close";

    //查看筛选“追踪的人”的人次
    String Follow_Only = "Follow_Only";
    //查看點擊“我的-我的聲音”按鈕的人次
    String Add_voice = "Add_voice";
    //查看進行錄音的人次
    String voice_record = "voice_record";
    //查看提交錄音的人次
    String voice_submit= "voice_submit";
    //查看在提交录音后进入任务中心的人次
    String voice_to_task_center = "voice_to_task_center";
    //查看在IM点击“谁看过我”的人次
    String chat_seen_me = "chat_seen_me";
    //看在私聊中点击“图片插入框任意图片（包括点击【>】）”的人次
    String Pchat_photo = "Pchat_photo";
    //查看在私聊中在“评价插入框”中提交评价的人次（包括点击【更多】进行评价）
    String Pchat_Evaluation = "Pchat_Evaluation";
    //  在用户主页点击心情图片，上报
    String View_moment = "View_moment";
    //在用户主页点击“正在发起约会”，上报
    String View_date = "View_date";
    //在用户主页点击播放声音的，上报
    String View_voice = "View_voice";
    //用戶进入天天福袋頁面时，上報
    String bag_daily = "bag_daily";
    //户積分不足，但钻石足够，点击福袋的【立即領取】，拉起“積分兌換框”，點擊【兌換】，能直接兑换成积分，上报
    String bag_id_ex_v = "bag_id_ex_v";

    //用戶点击福袋的【立即領取】，上報
    String bag_id_get = "bag_id_get";

    //用户点击【附近】后，在《附近》页面点击广告，上报
    String nearby_ad_id = "nearby_ad_id";
    //用户点击【讯息】后，在《讯息》页面点击广告，上报
    String im_ad_id = "im_ad_id";

    //谷歌支付调用失败-未成功唤醒充值确定
    String vip_google_arouse_error = "vip_google_arouse_error";
    //谷歌唤醒充值弹窗
    String vip_google_start = "vip_google_start_";
    //用户主动取消
    String vip_google_start_cancel = "vip_google_start_cancel";
    //支付失败
    String vip_google_play_result = "vip_google_pay_result_";
    //支付效验异常结果
    String vip_google_play_error = "vip_google_pay_error_";


    //谷歌支付调用失败-未成功唤醒充值确定
    String im_google_arouse_error = "im_google_arouse_error";
    //谷歌唤醒充值弹窗
    String im_google_start = "im_google_start_";
    //用户主动取消
    String im_google_start_cancel = "im_google_start_cancel";
    //支付失败
    String im_google_play_result = "im_google_pay_result_";
    //支付效验异常结果
    String im_google_play_error = "im_google_pay_error_";

    //任务
    //男用戶簽到第N天
    String sign_day1_male = "sign_day1_male";
    String sign_day2_male = "sign_day2_male";
    String sign_day3_male = "sign_day3_male";
    String sign_day4_male = "sign_day4_male";
    String sign_day5_male = "sign_day5_male";
    String sign_day6_male = "sign_day6_male";
    String sign_day7_male = "sign_day7_male";

    //女用戶簽到第N天
    String sign_day1_female = "sign_day1_female";
    String sign_day2_female = "sign_day2_female";
    String sign_day3_female = "sign_day3_female";
    String sign_day4_female = "sign_day4_female";
    String sign_day5_female = "sign_day5_female";
    String sign_day6_female = "sign_day6_female";
    String sign_day7_female = "sign_day7_female";

    //男用戶點擊完善個人檔案任務跳轉資料頁
    String task_improve_data_M = "task_improve_data_M";
    //男用戶點擊搭訕3人任務跳轉訊息頁
    String task_accost_M = "task_accost_M";
    //男用戶點擊語音/視訊聊天1次任務跳轉訊息頁
    String task_voice_video_call_M = "task_voice_video_call_M";
    //男用戶點擊報名活動1次/評論動態1次/點讚動態1次任務跳轉廣場頁
    String task_signup_com_like_M = "task_signup_com_like_M";
    //男用戶點擊上傳照片1張任務跳轉相冊頁
    String task_uploading_image_M = "task_uploading_image_M";

    //女用戶點擊首次回復信息任務跳轉聊天頁時觸發
    String task_answer = "task_answer";
    //女用戶點擊完成真人認真任務跳轉認證頁
    String task_auth_F = "task_auth_F";
    //女用戶點擊完善個人檔案任務跳轉資料頁
    String task_improve_data_F = "task_improve_data_F";
    //女用戶點擊語音/視訊聊天1次任務跳轉訊息頁
    String task_voice_video_call_F = "task_voice_video_call_F";
    //女用戶點擊上傳照片1張/上傳快照1張任務跳轉相冊頁
    String task_upload_image_snap_F = "task_upload_image_snap_F";
    //女用戶點擊發佈心情1條/發佈活動1次任務跳轉發佈動態頁
    String task_post_mood_date_F = "task_post_mood_date_F";
    //女用戶點擊評論動態1次任務跳轉廣告頁
    String task_commentary_F = "task_commentary_F";
    //女用戶點擊主動搭訕1次任務跳轉首頁
    String task_accost_F = "task_accost_F";

    //首页-搭讪
    //查看在首頁用戶列表點擊【搭讪】的人次
    String homepage_accost = "homepage_accost";
    //查看在首頁用戶列表點擊【聊一聊】的人次
    String homepage_chat = "homepage_chat";
    //查看在首頁點擊【批量搭讪】的人次
    String homepage_batch_accost = "homepage_batch_accost";
    //判断有多少用户成功批量搭讪
    String accost_chatup = "accost_chatup";
    //判断有多少用户有换一批搭讪对象的冲动
    String accost_change = "accost_change";
    //判断有多少用户关闭批量搭讪框
    String accost_close = "accost_close";

    //引导
    //女用户注册完，获得注册任务的奖励，即完成“完成注册”任务，上报
    String task_register = "task_register";
    //女用户在“完成注册”的完成任务弹框中，点击【马上去聊天赚钱】，上报
    String task_register_toChat = "task_register_toChat";
    //女用戶第一次回复信息獲得收益，即完成“首次回复信息”任务，上報
    String task_first_profit = "task_first_profit";
    //女用户在“首次回复信息”的完成任务弹框中，点击【马上去提现】，上报
    String task_first_profit_toWith = "task_first_profit_toWith";
    //女用户在新手引导流程中（即从点击【马上去提现】中过来的用户），完成真人认证，上报
    String task_auth_success = "task_auth_success";
    //女用户在新手引导流程中（即从点击【马上去提现】中过来的用户），真人认证失败，上报
    String task_auth_fail = "task_auth_fail";
    //女用户在“完成真人认证”的完成任务弹框中，点击【马上去提现】，上报
    String task_auth_toWithdrawal = "task_auth_toWithdrawal";



    //用戶在個人私聊頁，點擊通話圖標，選擇語音通話，上報
    String im_voice_call = "im_voice_call";
    //用戶在個人私聊頁，點擊通話圖標，選擇视频通話，上報
    String im_video_call = "im_video_call";
    //用戶在個人私聊頁，點擊禮物圖標上報
    String im_gifts = "im_gifts";
    //用戶在個人私聊頁，點擊禮物圖標弹出礼物列表，选取礼物，点击【赠送】，上報
    String im_send_gifts = "im_send_gifts";
    //用戶在個人私聊頁，點擊禮物圖標弹出礼物框，点击【充值】或者【首充有礼】
    String im_gifts_topup = "im_gifts_topup";
    //用戶在個人私聊頁，选取礼物送出，但钻石不足而弹出钻石充值框，上報
    String im_gifts_Insufficient_topup = "im_gifts_Insufficient_topup";
    //用戶在個人私聊的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，上报
    String im_gifts_topup_purchase = "im_gifts_topup_purchase";
    //用戶在個人私聊的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值成功，上
    String im_gifts_topup_success = "im_gifts_topup_success";
    //用戶在個人私聊的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值失败，上
    String im_gifts_topup_fail = "im_gifts_topup_fail";
    //用户在个人私聊页，钻石不足，在系统提示中点击【马上获取】，上报
    String im_topup = "im_topup";
    //用户在个人私聊页，钻石不足，在主动拉起的充值弹框，或者在系统提示中点击【马上获取】而拉起的充值弹框，点选数量，上报 1
    String im_topup_purchase = "im_topup_purchase";
    //用户在个人私聊页，钻石不足，在主动拉起的充值弹框，或者在系统提示中点击【马上获取】而拉起的充值弹框，点选数量充值成功，上报 1
    String im_topup_success = "im_topup_success";
    //用户在个人私聊页，钻石不足，在主动拉起的充值弹框，或者在系统提示中点击【马上获取】而拉起的充值弹框，点选数量充值失败，上报
    String im_topup_fail = "im_topup_fail";
    //用戶在個人私聊頁，點擊插入的系統信息中的【認證】按鈕，上報 1
    String im_tips_auth = "im_tips_auth";
    //用戶在個人私聊頁，點擊插入的系統信息中的【追踪】按鈕，上報 1
    String im_tips_follow = "im_tips_follow";
    //用戶在個人私聊頁，點擊插入的系統信息中的【送给Ta】按鈕，上報 1
    String im_tips_gifts = "im_tips_gifts";
    //用戶在個人私聊頁，點擊插入的系統信息中的【上传照片】按鈕，上報 1
    String im_tips_photo = "im_tips_photo";
    //用戶在個人私聊頁，點擊插入的系統信息中的【马上撩】按鈕，上報 1
    String im_tips_vv = "im_tips_v&v";
    //=========1V1語音============
    //用戶在通話過程匯總，點擊【送禮】，上報
    String voicecall_gift = "voicecall_gift";
    //通話過程中，公屏提示送禮，用戶點擊【送禮】，上報
    String voicecall_public_gift = "voicecall_public_gift";
    //用戶在语聊頁，在礼物列表，选取礼物，点击【赠送】，上報
    String voicecall_send_gift = "voicecall_send_gift";
    //用戶在语音聊天，點擊禮物圖標弹出礼物框，点击【充值】或者【首充有礼】，上報
    String voicecall_gift_topup = "voicecall_gift_topup";
    //用戶在语音聊天，选取礼物送出，但钻石不足而弹出钻石充值框，点击充值数量，上報
    String voicecall_gift_Ins_topup = "voicecall_gift_Ins_topup";
    //用戶在语音聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，上报
    String voicecall_gift_topup_pur = "voicecall_gift_topup_pur";
    // 用戶在语音聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值成功，上报
    String voicecall_gift_topup_suc = "voicecall_gift_topup_suc";
    //用戶在语音聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值失败，上报
    String voicecall_gift_topup_fail = "voicecall_gift_topup_fail";
    //语音聊天中，彈出通話倒計時提示，用戶點擊【充值】，上報
    String voicecall_topup = "voicecall_topup";
    //voicecall_topup_success1
    String voicecall_topup_success = "voicecall_topup_success";
    //用戶在语音聊天弹出的充值提示中，充值时长失败，上报
    String voicecall_topup_fail = "voicecall_topup_fail";
    //用戶在语音聊天中，點擊【+（追蹤）】，上報
    String voicecall_follow = "voicecall_follow";
    //用戶在语音聊天中，點擊破冰提示中的【換一換】，上報
    String voicecall_ice_change = "voicecall_ice_change";
    //用戶在通話過程中，點擊破冰提示中的【X关闭】，上報
    String voicecall_ice_close = "voicecall_ice_close";
    //男生语音聊天中，點右上角關閉，在彈框中選擇【追蹤並掛斷】，上報
    String voicecall_close_follow_M = "voicecall_close_follow_M";
    // 男生语音聊天中，點右上角關閉，在彈框中選擇【仅掛斷】，上報
    String voicecall_close_hangup_M = "voicecall_close_hangup_M";
    //男生语音聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String voicecall_close_goon_M = "voicecall_close_goon_M";
    //女生语音聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String voicecall_close_hangup_F = "voicecall_close_hangup_F";
    //女生在语音聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String voicecall_close_goon_F = "voicecall_close_goon_F";
    //==========1V1視頻==========
    //用戶在视频聊天過程中，點擊【送禮】，上報
    String videocall_gift = "videocall_gift";
    //视频聊天中，公屏提示送禮，用戶點擊【送禮】，上報
    String videocall_public_gift = "videocall_public_gift";
    //用戶在视频聊天中，在礼物列表，选取礼物，点击【赠送】，上報
    String videocall_send_gift = "videocall_send_gift";
    //用戶在视频聊天，點擊禮物圖標弹出礼物框，点击【充值】或者【首充有礼】，上報
    String videocall_gift_topup = "videocall_gift_topup";
    //用戶在视频聊天，选取礼物送出，但钻石不足而弹出钻石充值框，点击充值数量，上報
    String videocall_gift_Insu_topup = "videocall_gift_Insu_topup";
    //用戶在视频聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，上报 1
    String videocall_gift_topup_pur = "videocall_gift_topup_pur";
    //用戶在视频聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值成功，上报
    String videocall_gift_topup_suc = "videocall_gift_topup_suc";
    //用戶在视频聊天的礼物框中，主动充值或者被动（钻石不足）弹出的充值下拉框中，点选充值钻石的数量，充值失败，上报
    String videocall_gift_topup_fail = "videocall_gift_topup_fail";
    //视频聊天中，彈出通話倒計時提示，用戶點擊【充值】，上報
    String videocall_topup = "videocall_topup";
    //用戶在视频聊天弹出的充值提示中，充值时长成功，上报
    String videocall_topup_success = "videocall_topup_success";
    //用戶在视频聊天弹出的充值提示中，充值时长失败，上报
    String videocall_topup_fail = "videocall_topup_fail";
    // 用戶在视频聊天中，點擊【+（追蹤）】，上報
    String videocall_follow = "videocall_follow";
    //用戶在视频聊天中，點擊破冰提示中的【換一換】，上報
    String videocall_ice_change = "videocall_ice_change";
    //用戶在视频聊天中，點擊破冰提示中的【X关闭】，上報
    String videocall_ice_close = "videocall_ice_close";
    //男生视频聊天中，點右上角關閉，在彈框中選擇【追蹤並掛斷】，上報
    String videocall_close_follow_M = "videocall_close_follow_M";
    //男生视频聊天中，點右上角關閉，在彈框中選擇【仅掛斷】，上報
    String videocall_close_hangup_M = "videocall_close_hangup_M";
    //男生视频聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String videocall_close_goon_M = "videocall_close_goon_M";
    //女生视频聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String videocall_close_hangup_F = "videocall_close_hangup_F";
    //女生在视频聊天中，點右上角關閉，在彈框中選擇【继续聊】，上報
    String videocall_close_goon_F = "videocall_close_goon_F";
    //個人搭訕（男用戶發起）
    String greet_male = "greet_male";
    //個人搭訕（女用戶發起）
    String greet_female = "greet_female";
    //一鍵搭訕（男用戶發起）
    String one_click_greet_male = "one_click_greet_male";
    //一鍵搭訕（女用戶發起）
    String one_click_greet_female = "one_click_greet_female";
    //發起視頻（男用戶發起）
    String call_video_male = "call_video_male";
    //發起視頻（女用戶發起）
    String call_video_female = "call_video_female";
    //發起語音（男用戶發起）
    String call_voice_male = "call_voice_male";
    //發起語音（女用戶發起）
    String call_voice_female = "call_voice_female";
    //用户支付购买成功
    String pay_success = "pay_success";


}
