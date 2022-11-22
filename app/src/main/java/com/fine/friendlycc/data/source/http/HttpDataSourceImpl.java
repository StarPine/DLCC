package com.fine.friendlycc.data.source.http;

import com.blankj.utilcode.util.GsonUtils;
import com.fine.friendlycc.data.source.HttpDataSource;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
import com.fine.friendlycc.data.source.http.service.ApiService;
import com.fine.friendlycc.bean.AccostBean;
import com.fine.friendlycc.bean.AdBannerBean;
import com.fine.friendlycc.bean.AdUserBannerBean;
import com.fine.friendlycc.bean.AddressBean;
import com.fine.friendlycc.bean.AlbumPhotoBean;
import com.fine.friendlycc.bean.AllConfigBean;
import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.bean.ApplyMessageBean;
import com.fine.friendlycc.bean.BaseUserBeanBean;
import com.fine.friendlycc.bean.BlackBean;
import com.fine.friendlycc.bean.BonusGoodsBean;
import com.fine.friendlycc.bean.BoradCastMessageBean;
import com.fine.friendlycc.bean.BroadcastBean;
import com.fine.friendlycc.bean.BroadcastListBean;
import com.fine.friendlycc.bean.BrowseNumberBean;
import com.fine.friendlycc.bean.BubbleBean;
import com.fine.friendlycc.bean.CallingInfoBean;
import com.fine.friendlycc.bean.CallingInviteInfo;
import com.fine.friendlycc.bean.CallingStatusBean;
import com.fine.friendlycc.bean.CashWalletBean;
import com.fine.friendlycc.bean.ChatDetailCoinBean;
import com.fine.friendlycc.bean.ChatRedPackageBean;
import com.fine.friendlycc.bean.CheckNicknameBean;
import com.fine.friendlycc.bean.ChooseAreaBean;
import com.fine.friendlycc.bean.CityAllBean;
import com.fine.friendlycc.bean.CoinExchangeBoxInfo;
import com.fine.friendlycc.bean.CoinPusherBalanceDataBean;
import com.fine.friendlycc.bean.CoinPusherConverInfoBean;
import com.fine.friendlycc.bean.CoinPusherDataInfoBean;
import com.fine.friendlycc.bean.CoinPusherRoomHistoryBean;
import com.fine.friendlycc.bean.CoinPusherRoomInfoBean;
import com.fine.friendlycc.bean.CoinPusherRoomTagInfoBean;
import com.fine.friendlycc.bean.CoinWalletBean;
import com.fine.friendlycc.bean.CommentMessageBean;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.CreateOrderBean;
import com.fine.friendlycc.bean.DayRewardInfoBean;
import com.fine.friendlycc.bean.DiamondInfoBean;
import com.fine.friendlycc.bean.EjectBean;
import com.fine.friendlycc.bean.EjectSignInBean;
import com.fine.friendlycc.bean.EvaluateBean;
import com.fine.friendlycc.bean.EvaluateMessageBean;
import com.fine.friendlycc.bean.ExchangeBean;
import com.fine.friendlycc.bean.ExchangeIntegraOuterBean;
import com.fine.friendlycc.bean.ExclusiveAccostInfoBean;
import com.fine.friendlycc.bean.FaceVerifyResultBean;
import com.fine.friendlycc.bean.GameCoinBuy;
import com.fine.friendlycc.bean.GameCoinWalletBean;
import com.fine.friendlycc.bean.GamePhotoAlbumBean;
import com.fine.friendlycc.bean.GiftBagBean;
import com.fine.friendlycc.bean.GiveMessageBean;
import com.fine.friendlycc.bean.GoldDetailBean;
import com.fine.friendlycc.bean.GoodsBean;
import com.fine.friendlycc.bean.GoogleNearPoiBean;
import com.fine.friendlycc.bean.GooglePoiBean;
import com.fine.friendlycc.bean.IMTransUserBean;
import com.fine.friendlycc.bean.ImUserSigBean;
import com.fine.friendlycc.bean.IsChatBean;
import com.fine.friendlycc.bean.LevelApiBean;
import com.fine.friendlycc.bean.LevelPageInfoBean;
import com.fine.friendlycc.bean.MallWithdrawTipsInfoBean;
import com.fine.friendlycc.bean.MessageGroupBean;
import com.fine.friendlycc.bean.MessageRuleBean;
import com.fine.friendlycc.bean.NewsBean;
import com.fine.friendlycc.bean.NoteInfoBean;
import com.fine.friendlycc.bean.OccupationConfigItemBean;
import com.fine.friendlycc.bean.ParkItemBean;
import com.fine.friendlycc.bean.PhotoAlbumBean;
import com.fine.friendlycc.bean.PriceConfigBean;
import com.fine.friendlycc.bean.PrivacyBean;
import com.fine.friendlycc.bean.ProfitMessageBean;
import com.fine.friendlycc.bean.PushSettingBean;
import com.fine.friendlycc.bean.RadioTwoFilterItemBean;
import com.fine.friendlycc.bean.SignMessageBean;
import com.fine.friendlycc.bean.SoundBean;
import com.fine.friendlycc.bean.StatusBean;
import com.fine.friendlycc.bean.SwiftMessageBean;
import com.fine.friendlycc.bean.SystemMessageBean;
import com.fine.friendlycc.bean.TagBean;
import com.fine.friendlycc.bean.TaskAdBean;
import com.fine.friendlycc.bean.TaskConfigBean;
import com.fine.friendlycc.bean.TaskConfigItemBean;
import com.fine.friendlycc.bean.TaskRewardReceiveBean;
import com.fine.friendlycc.bean.TokenBean;
import com.fine.friendlycc.bean.TopicalListBean;
import com.fine.friendlycc.bean.TraceBean;
import com.fine.friendlycc.bean.UnReadMessageNumBean;
import com.fine.friendlycc.bean.UserCoinItemBean;
import com.fine.friendlycc.bean.UserConnMicStatusBean;
import com.fine.friendlycc.bean.UserDataBean;
import com.fine.friendlycc.bean.UserDetailBean;
import com.fine.friendlycc.bean.UserInfoBean;
import com.fine.friendlycc.bean.UserProfitPageBean;
import com.fine.friendlycc.bean.UserRemarkBean;
import com.fine.friendlycc.bean.VersionBean;
import com.fine.friendlycc.bean.VipInfoBean;
import com.fine.friendlycc.utils.ApiUitl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author goldze
 * @date 2019/3/26
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private volatile static HttpDataSourceImpl INSTANCE = null;
    private final ApiService apiService;

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    public static HttpDataSourceImpl getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<BaseDataResponse<IMTransUserBean>> transUserIM(String IMUserId) {
        return apiService.transUserIM(IMUserId);
    }

    @Override
    public Observable<BaseResponse> userMessageCollation(Integer user_id) {
        return apiService.userMessageCollation(user_id);
    }

    @Override
    public Observable<BaseDataResponse<ChatDetailCoinBean>> getTotalCoins(Integer dismissRoom) {
        return apiService.getTotalCoins(dismissRoom);
    }

    @Override
    public Observable<BaseResponse> GamePaySuccessNotify(String packageName, String orderNumber, List<String> productId, String token, int type, Integer event, String serverId, String roleId) {
        return apiService.GamePaySuccessNotify(packageName, orderNumber, productId,token,type,event,serverId,roleId);
    }

    @Override
    public Observable<BaseDataResponse<GamePhotoAlbumBean>> getGamePhotoAlbumList(String serverId, String roleId) {
        return apiService.getGamePhotoAlbumList(serverId, roleId);
    }

    @Override
    public Observable<BaseResponse> setGameState(int gameState) {
        return apiService.setGameState(gameState);
    }

    @Override
    public Observable<BaseResponse> commitRoleInfo(RequestBody requestBody) {
        return apiService.commitRoleInfo(requestBody);
    }

    @Override
    public Observable<BaseResponse> upUserSex(Integer sex) {
        return apiService.upUserSex(sex);
    }

    @Override
    public Observable<BaseDataResponse<CreateOrderBean>> createChatDetailOrder(Integer id, Integer type, Integer payType, Integer toUserId, Integer channel) {
        return apiService.createChatDetailOrder(id, type, payType, toUserId, channel);
    }

    @Override
    public Observable<BaseDataResponse<PriceConfigBean.Current>> getMaleRefundMsg(Integer toUserId, Integer type) {
        return apiService.getMaleRefundMsg(toUserId, type);
    }

    @Override
    public Observable<BaseDataResponse> getTips(Integer toUserId, Integer type, String isShow) {
        return apiService.getTips(toUserId, type, isShow);
    }

    @Override
    public Observable<BaseResponse> addIMCollect(Integer userId, Integer type) {
        return apiService.addIMCollect(userId, type);
    }

    @Override
    public Observable<BaseDataResponse<Map<String, Integer>>> verifyGoddessTips(Integer toUserId) {
        return apiService.verifyGoddessTips(toUserId);
    }

    @Override
    public Observable<BaseDataResponse<PriceConfigBean>> getPriceConfig(Integer to_user_id) {
        return apiService.getPriceConfig(to_user_id);
    }

    @Override
    public Observable<BaseDataResponse<CallingInfoBean.SayHiList>> getSayHiList(Integer page, Integer perPage) {
        return apiService.getSayHiList(page, perPage);
    }

    @Override
    public Observable<BaseDataResponse<CallingInfoBean>> getCallingInfo(Integer roomId, Integer callingType, String fromUserId, String toUserId) {
        return apiService.getCallingInfo(roomId, callingType, fromUserId, toUserId);
    }

    @Override
    public Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, String fromUserId, String toUserId, int callingSource) {
        return apiService.callingInviteInfo(callingType, fromUserId, toUserId, callingSource);
    }

    @Override
    public Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, String fromUserId, String toUserId, int callingSource ,int videoCallPushLogId) {
        return apiService.callingInviteInfo(callingType, fromUserId, toUserId, callingSource,videoCallPushLogId);
    }

    @Override
    public Observable<BaseDataResponse> videoFeedback(long videoCallPushLogId, int feedback) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("videoCallPushLogId", videoCallPushLogId);
        map.put("feedback", feedback);
        return apiService.videoFeedback(ApiUitl.getBody(GsonUtils.toJson(map)));
    }


    @Override
    public Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, Integer fromUserId, Integer toUserId, Integer currentUserId) {
        return apiService.callingInviteInfo(callingType, fromUserId, toUserId, currentUserId);
    }

    @Override
    public Observable<BaseResponse> sendUserGift(RequestBody requestBody) {
        return apiService.sendUserGift(requestBody);
    }

    @Override
    public Observable<BaseDataResponse<GiftBagBean>> getBagGiftInfo() {
        return apiService.getBagGiftInfo();
    }

    @Override
    public Observable<BaseDataResponse<ImUserSigBean>> flushSign() {
        return apiService.flushSign();
    }

    @Override
    public Observable<BaseDataResponse<UserProfitPageBean>> getUserProfitPageInfo(Long currentUserId, Integer page, Integer perPage) {
        return apiService.getUserProfitPageInfo(currentUserId, page, perPage);
    }

    @Override
    public Observable<BaseDataResponse<CoinWalletBean>> getUserAccount() {
        return apiService.getUserAccount();
    }

    @Override
    public Observable<BaseDataResponse<GameCoinWalletBean>> getUserAccountPageInfo() {
        return apiService.getUserAccountPageInfo();
    }

    @Override
    public Observable<BaseDataResponse<BubbleBean>> getBubbleEntity() {
        return apiService.getBubbleEntity();
    }

    @Override
    public Observable<BaseDataResponse<AccostBean>> getAccostList(Integer page) {
        return apiService.getAccostList(page);
    }

    @Override
    public Observable<BaseResponse> putAccostList(List<Integer> userIds) {
        return apiService.putAccostList(userIds);
    }

    @Override
    public Observable<BaseResponse> putAccostFirst(Integer userId) {
        return apiService.putAccostFirst(userId);
    }

    @Override
    public Observable<BaseDataResponse<BroadcastListBean>> getBroadcastHome(Integer sex, Integer city_id, Integer game_id, Integer is_online, Integer is_collect, Integer type, Integer page) {
        return apiService.getBroadcastHome(sex, city_id, game_id, is_online, is_collect, type, page);
    }

    @Override
    public Observable<BaseDataResponse<List<MessageRuleBean>>> getMessageRule() {
        return apiService.getMessageRule();
    }

    @Override
    public Observable<BaseDataResponse> getSensitiveWords() {
        return apiService.getSensitiveWords();
    }

    @Override
    public Observable<BaseDataResponse<PhotoAlbumBean>> getPhotoAlbum(Integer user_id) {
        return apiService.getPhotoAlbum(user_id);
    }

    @Override
    public Observable<BaseResponse> removeUserSound() {
        return apiService.removeUserSound();
    }

    @Override
    public Observable<BaseDataResponse> putUserSound(String paht, Integer sound_time) {
        return apiService.putUserSound(paht, sound_time);
    }

    @Override
    public Observable<BaseListDataResponse<SoundBean>> getUserSound(Integer page) {
        return apiService.getUserSound(page);
    }

    @Override
    public Observable<BaseResponse> topicalCreateMood(String describe, String start_date, List<String> images, Integer is_comment, Integer is_hide, Double longitude, Double latitude, String video, Integer news_type) {
        return apiService.topicalCreateMood(describe, start_date, images, is_comment, is_hide, longitude, latitude, video, news_type);
    }

    @Override
    public Observable<BaseListDataResponse<BroadcastBean>> broadcastAll(Integer page) {
        return apiService.broadcastAll(page);
    }

    @Override
    public Observable<BaseDataResponse<List<GoodsBean>>> pointsGoodList() {
        return apiService.pointsGoodList("points");
    }

    @Override
    public Observable<BaseResponse> pushGreet(Integer type) {
        return apiService.pushGreet(type);
    }

    @Override
    public Observable<BaseDataResponse<Map<String, String>>> isBlacklist(String userId) {
        return apiService.isBlacklist(userId);
    }

    @Override
    public Observable<BaseListDataResponse<TaskAdBean>> rechargeVipList() {
        return apiService.rechargeVipList();
    }

    @Override
    public Observable<BaseDataResponse<Map<String, String>>> isOnlineUser(String userId) {
        return apiService.isOnlineUser(userId);
    }


    @Override
    public Observable<BaseDataResponse<BrowseNumberBean>> newsBrowseNumber() {
        return apiService.newsBrowseNumber();
    }

    @Override
    public Observable<BaseListDataResponse<TraceBean>> toBrowse(Integer page) {
        return apiService.toBrowse(page);
    }

    @Override
    public Observable<BaseListDataResponse<TraceBean>> collectFans(Integer page) {
        return apiService.collectFans(page);
    }

    @Override
    public Observable<BaseListDataResponse<TraceBean>> collect(Integer page) {
        return apiService.collect(page);
    }

    @Override
    public Observable<BaseResponse> reportUserLocation(String latitude, String longitude) {
        return apiService.reportUserLocation(latitude, longitude);
    }

    @Override
    public Observable<BaseResponse> repoetLocalGoogleOrder(Map<String, Object> map) {
        return apiService.repoetLocalGoogleOrder(map);
    }

    @Override
    public Observable<BaseDataResponse<CreateOrderBean>> createOrderUserDetail(Integer id, Integer type, Integer payType, Integer number) {
        return apiService.createOrderUserDetail(id, type, payType, number);
    }

    @Override
    public Observable<BaseDataResponse<Map<String, String>>> freeSevenDay(Integer pay_type, Integer goods_type) {
        return apiService.freeSevenDay(pay_type, goods_type);
    }

    @Override
    public Observable<BaseDataResponse<TagBean>> tag(String to_user_id) {
        return apiService.tag(to_user_id);
    }

    @Override
    public Observable<BaseResponse> userInvite(String code, Integer type, String channel) {
        return apiService.userInvite(code, type, channel);
    }

    @Override
    public Observable<BaseResponse> isBindCity(Integer city_id) {
        return apiService.isBindCity(city_id);
    }

    @Override
    public Observable<BaseDataResponse<UserDataBean>> regUser(String nickname, String avatar, String birthday, Integer sex, String channel) {
        return apiService.regUser(nickname, avatar, birthday, sex, channel);
    }

    @Override
    public Observable<BaseResponse> coordinate(Double latitude, Double longitude, String county_name, String province_name) {
        return apiService.coordinate(latitude, longitude, county_name, province_name);
    }

    @Override
    public Observable<BaseDataResponse<SwiftMessageBean>> getSwiftMessage(Integer page) {
        return apiService.getSwiftMessage(page);
    }

    @Override
    public Observable<BaseResponse> bindAccount(String id, String type) {
        return apiService.bindAccount(id, type);
    }

    @Override
    public Observable<BaseDataResponse<UserDataBean>> v2Login(String phone, String code, String device_code, String region_code) {
        return apiService.v2Login(phone, code, device_code,region_code);
    }

    @Override
    public Observable<BaseDataResponse<Map<String, String>>> imagFaceUpload(String imgUrl) {
        return apiService.imagFaceUpload(imgUrl);
    }

    @Override
    public Observable<BaseDataResponse<VersionBean>> detectionVersion(String client) {
        return apiService.detectionVersion(client);
    }

    @Override
    public Observable<BaseDataResponse<GoogleNearPoiBean>> nearSearchPlace(RequestBody requestBody) {
        return apiService.nearSearchPlace(requestBody);
    }

    @Override
    public Observable<BaseDataResponse<GooglePoiBean>> textSearchPlace(RequestBody requestBody) {
        return apiService.textSearchPlace(requestBody);
    }

    @Override
    public Observable<BaseResponse> verifyCodePost(RequestBody requestBody) {
        return apiService.verifyCodePost(requestBody);
    }


    /**
     * 注册
     *
     * @param phone    邮箱
     * @param password 密码
     * @return
     */
    @Override
    public Observable<BaseDataResponse<TokenBean>> register(String phone, String password, String code) {
        return apiService.registerPost(phone, password, code);
    }

    /**
     * 同意用户协议
     *
     * @return
     */
    @Override
    public Observable<BaseResponse> acceptUseAgreement() {
        return apiService.acceptUseAgreement(1);
    }

    /**
     * 注册
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @Override
    public Observable<BaseDataResponse<TokenBean>> login(String phone, String password) {
        return apiService.loginPost(phone, password);
    }

    @Override
    public Observable<BaseDataResponse<UserDataBean>> authLoginPost(String id, String type) {
        return apiService.authLoginPost(id, type);
    }

    /**
     * 首页列表
     *
     * @param cityId     城市ID
     * @param type       1附近 2最新注册 3女神
     * @param isOnline   在线优先
     * @param sex        性别 1男 0女
     * @param searchName 昵称/职业来搜索
     * @param longitude  经度
     * @param latitude   纬度
     * @return
     */
    @Override
    public Observable<BaseListDataResponse<ParkItemBean>> homeList(
            Integer cityId,
            Integer type,
            Integer isOnline,
            Integer sex,
            String searchName,
            Double longitude,
            Double latitude,
            Integer page) {
        return apiService.homeListGet(cityId, type, isOnline, sex, searchName, longitude, latitude, page);
    }

    @Override
    public Observable<BaseDataResponse<UserInfoBean>> getUserInfo() {
        return apiService.getUserInfo(1);
    }

    /**
     * 个人资料
     *
     * @return
     */
    @Override
    public Observable<BaseDataResponse<UserDataBean>> getUserData() {
        return apiService.getUserData(1);
    }

    /**
     * 游戏地区
     *
     * @return
     */
    @Override
    public Observable<BaseDataResponse<List<RadioTwoFilterItemBean>>> getGameCity() {
        return apiService.getGameCity("gameCity");
    }


    @Override
    public Observable<BaseResponse> userRemark(Integer userId, String nickname, String remark) {
        return apiService.userRemark(userId, nickname, remark);
    }

    @Override
    public Observable<BaseDataResponse<UserRemarkBean>> getUserRemark(Integer userId) {
        return apiService.getUserRemark(userId);
    }

    @Override
    public Observable<BaseResponse> updateAvatar(String avatar) {
        return apiService.updateAvatar(avatar);
    }


    @Override
    public Observable<BaseResponse> updateUserData(String nickname, List<Integer> permanent_city_ids, String birthday, String occupation, List<Integer> program_ids, List<Integer> hope_object_ids, String facebook, String insgram,Integer accountType, Integer is_weixin_show, Integer height, Integer weight, String desc) {
        return apiService.updateUserData(nickname, permanent_city_ids, birthday, occupation, program_ids, hope_object_ids, facebook, insgram,accountType, is_weixin_show, height, weight, desc);
    }

    @Override
    public Observable<BaseDataResponse<UserDetailBean>> userMain(Integer id, Double longitude, Double latitude) {
        return apiService.userMain(id, longitude, latitude);
    }

    @Override
    public Observable<BaseResponse> topicalCreate(Integer theme_id, String describe, String address, List<Integer> hope_object, String start_date, Integer end_time, List<String> images, Integer is_comment, Integer is_hide, String address_name, Integer city_id, Double longitude, Double latitude, String video) {
        return apiService.topicalCreate(theme_id, describe, address, hope_object, start_date, end_time, images, is_comment, is_hide, address_name, city_id, longitude, latitude, video);
    }

    @Override
    public Observable<BaseResponse> singUp(Integer id, String img) {
        return apiService.singUp(id, img);
    }

    @Override
    public Observable<BaseListDataResponse<BlackBean>> getBlackList(Integer page) {
        return apiService.getBlackList(page);
    }

    @Override
    public Observable<BaseResponse> addBlack(Integer user_id) {
        return apiService.addBlack(user_id);
    }

    @Override
    public Observable<BaseResponse> deleteBlack(Integer id) {
        return apiService.deleteBlack(id);
    }

    @Override
    public Observable<BaseResponse> deleteTopical(Integer id) {
        return apiService.deleteTopical(id);
    }

    @Override
    public Observable<BaseListDataResponse<ParkItemBean>> getCollectList(int page, Double latitude, Double longitude) {
        return apiService.getCollectList(page, latitude, longitude);
    }

    @Override
    public Observable<BaseResponse> addCollect(Integer userId) {
        return apiService.addCollect(userId);
    }

    @Override
    public Observable<BaseResponse> deleteCollect(Integer userId) {
        return apiService.deleteCollect(userId);
    }

    @Override
    public Observable<BaseResponse> newsCreate(String content, List<String> images, Integer is_comment, Integer is_hide) {
        return apiService.newsCreate(content, images, is_comment, is_hide);
    }

    @Override
    public Observable<BaseDataResponse<NewsBean>> newsDetail(Integer id) {
        return apiService.newsDetail(id);
    }

    @Override
    public Observable<BaseResponse> deleteNews(Integer id) {
        return apiService.deleteNews(id);
    }

    @Override
    public Observable<BaseListDataResponse<BroadcastBean>> broadcast(Integer type, Integer theme_id, Integer is_online, Integer city_id, Integer sex, Integer page) {
        return apiService.broadcast(type, theme_id, is_online, city_id, sex, page);
    }

    @Override
    public Observable<BaseListDataResponse<NewsBean>> getNewsList(Integer user_id, Integer page) {
        return apiService.getNewsList(user_id, page);
    }


    @Override
    public Observable<BaseListDataResponse<TopicalListBean>> getTopicalList(Integer userId, Integer page) {
        return apiService.getTopicalList(userId, page);
    }

    @Override
    public Observable<BaseResponse> report(Integer id, String type, String reasonId, List<String> images, String desc) {
        return apiService.report(id, type, reasonId, images, desc);
    }

    @Override
    public Observable<BaseResponse> topicalComment(Integer id, String content, Integer to_user_id) {
        return apiService.topicalComment(id, content, to_user_id);
    }

    @Override
    public Observable<BaseResponse> newsComment(Integer id, String content, Integer to_user_id) {
        return apiService.newsComment(id, content, to_user_id);
    }

    @Override
    public Observable<BaseDataResponse<StatusBean>> evaluateStatus(Integer userId) {
        return apiService.evaluateStatus(userId);
    }

    @Override
    public Observable<BaseResponse> evaluateCreate(Integer userId, Integer tagId, String img) {
        return apiService.evaluateCreate(userId, tagId, img);
    }

    @Override
    public Observable<BaseDataResponse<List<EvaluateBean>>> evaluate(Integer userId) {
        return apiService.evaluate(userId);
    }

    @Override
    public Observable<BaseResponse> newsGive(Integer id) {
        return apiService.newsGive(id);
    }

    @Override
    public Observable<BaseDataResponse<IsChatBean>> isChat(Integer userId) {
        return apiService.isChat(userId);
    }

    @Override
    public Observable<BaseResponse> useVipChat(Integer userId, Integer type) {
        return apiService.useVipChat(userId, type);
    }

    @Override
    public Observable<BaseResponse> imgeReadLog(Integer image_id) {
        return apiService.imgeReadLog(image_id);
    }

    @Override
    public Observable<BaseResponse> password(String original_password, String new_password) {
        return apiService.password(original_password, new_password);
    }

    @Override
    public Observable<BaseDataResponse<AllConfigBean>> getAllConfig() {
        return apiService.getAllConfig();
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getProgramTimeConfig() {
        return apiService.getSystemConfig("program_time");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getHeightConfig() {
        return apiService.getSystemConfig("height");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getWeightConfig() {
        return apiService.getSystemConfig("weight");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getReportReasonConfig() {
        return apiService.getSystemConfig("report_reason");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getEvaluateConfig() {
        return apiService.getSystemConfig("evaluate");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getHopeObjectConfig() {
        return apiService.getSystemConfig("hope_object");
    }

    @Override
    public Observable<BaseDataResponse<List<OccupationConfigItemBean>>> getOccupationConfig() {
        return apiService.getOccupationConfig("occupation");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getThemeConfig() {
        return apiService.getThemeConfig("theme");
    }

    @Override
    public Observable<BaseDataResponse<List<ConfigItemBean>>> getCityConfig() {
        return apiService.getSystemConfig("city");
    }

    @Override
    public Observable<BaseResponse> userVerify(Integer user_id, String img) {
        return apiService.userVerify(user_id, img);
    }

    @Override
    public Observable<BaseDataResponse<DiamondInfoBean>> goods() {
        return apiService.goods("recharge");
    }

    @Override
    public Observable<BaseDataResponse<VipInfoBean>> vipPackages() {
        return apiService.vipPackages("vip");
    }

    @Override
    public Observable<BaseDataResponse> saveVerifyFaceImage(String imgPath) {
        return apiService.saveVerifyFaceImage(imgPath);
    }

    @Override
    public Observable<BaseResponse> cashOut(float money) {
        return apiService.cashOut(money);
    }

    @Override
    public Observable<BaseResponse> sendCcode(String phone) {
        return apiService.sendCcode(phone);
    }

    @Override
    public Observable<BaseListDataResponse<AlbumPhotoBean>> albumImage(Integer userId, Integer type) {
        return apiService.albumImage(userId, type);
    }

    @Override
    public Observable<BaseResponse> albumInsert(Integer fileType, String src, Integer isBurn, String videoImage) {
        return apiService.albumInsert(fileType, src, isBurn, videoImage);
    }

    @Override
    public Observable<BaseDataResponse<List<AlbumPhotoBean>>> delAlbumImage(Integer id) {
        return apiService.delAlbumImage(id);
    }

    @Override
    public Observable<BaseResponse> setBurnAlbumImage(Integer imgId, Boolean state) {
        return apiService.setAlbumImage(imgId, 1, state ? 1 : 0);
    }

    @Override
    public Observable<BaseResponse> setRedPackageAlbumImage(Integer imgId, Boolean state) {
        return apiService.setAlbumImage(imgId, 2, state ? 1 : 0);
    }

    @Override
    public Observable<BaseResponse> setRedPackageAlbumVideo(Integer videoId, Boolean state) {
        return apiService.setAlbumImage(videoId, 3, state ? 1 : 0);
    }

    @Override
    public Observable<BaseDataResponse<FaceVerifyResultBean>> faceVerifyResult(String bizId) {
        return apiService.faceVerifyResult(bizId);
    }

    @Override
    public Observable<BaseDataResponse<StatusBean>> faceIsCertification() {
        return apiService.faceIsCertification();
    }

    @Override
    public Observable<BaseDataResponse<PrivacyBean>> getPrivacy() {
        return apiService.getPrivacy();
    }

    @Override
    public Observable<BaseResponse> setPrivacy(PrivacyBean privacyEntity) {
        return apiService.setPrivacy(
                privacyEntity.getHome() == null ? null : privacyEntity.getHome() ? 1 : 0,
                privacyEntity.getDistance() == null ? null : privacyEntity.getDistance() ? 1 : 0,
                privacyEntity.getOnlineIme() == null ? null : privacyEntity.getOnlineIme() ? 1 : 0,
                privacyEntity.getConnection() == null ? null : privacyEntity.getConnection() ? 1 : 0,
                privacyEntity.getNearby() == null ? null : privacyEntity.getNearby() ? 1 : 0,
                privacyEntity.getAllowAudio() == null ? null : privacyEntity.getAllowAudio() ? 1 : 0,
                privacyEntity.getAllowVideo() == null ? null : privacyEntity.getAllowVideo() ? 1 : 0
        );
    }

    @Override
    public Observable<BaseResponse> updatePhone(String phone, int code, String password) {
        return apiService.updatePhone(phone, code, password);
    }

    @Override
    public Observable<BaseResponse> applyGoddess(List<String> images) {
        return apiService.applyGoddess(images);
    }

    @Override
    public Observable<BaseDataResponse<StatusBean>> applyGoddessResult() {
        return apiService.applyGoddessResult();
    }

    @Override
    public Observable<BaseResponse> resetPassword(String phone, int code, String password) {
        return apiService.resetPassword(phone, code, password);
    }

    @Override
    public Observable<BaseResponse> setSex(int sex) {
        return apiService.setSex(sex);
    }

    @Override
    public Observable<BaseDataResponse<CashWalletBean>> cashWallet() {
        return apiService.cashWallet();
    }

    @Override
    public Observable<BaseDataResponse<CoinWalletBean>> coinWallet() {
        return apiService.coinWallet();
    }

    @Override
    public Observable<BaseResponse> setWithdrawAccount(String realName, String account) {
        return apiService.setWithdrawAccount(realName, account);
    }

    @Override
    public Observable<BaseResponse> setAlbumPrivacy(Integer type, Integer money) {
        return apiService.setAlbumPrivacy(type, money);
    }

    @Override
    public Observable<BaseResponse> setComment(Integer id, Integer isComment) {
        return apiService.setComment(id, isComment);
    }

    @Override
    public Observable<BaseListDataResponse<ApplyMessageBean>> getMessageApply(Integer page) {
        return apiService.getMessageApply(page);
    }

    @Override
    public Observable<BaseListDataResponse<BoradCastMessageBean>> getMessageBoradcast(Integer page) {
        return apiService.getMessageBoradcast(page);
    }

    @Override
    public Observable<BaseListDataResponse<CommentMessageBean>> getMessageComment(Integer page) {
        return apiService.getMessageComment(page);
    }

    @Override
    public Observable<BaseListDataResponse<EvaluateMessageBean>> getMessageEvaluate(Integer page) {
        return apiService.getMessageEvaluate(page);
    }

    @Override
    public Observable<BaseListDataResponse<GiveMessageBean>> getMessageGive(Integer page) {
        return apiService.getMessageGive(page);
    }

    @Override
    public Observable<BaseListDataResponse<SignMessageBean>> getMessageSign(Integer page) {
        return apiService.getMessageSign(page);
    }

    @Override
    public Observable<BaseListDataResponse<ProfitMessageBean>> getMessageProfit(Integer page) {
        return apiService.getMessageProfit(page);
    }

    @Override
    public Observable<BaseResponse> evaluateAppeal(Integer messageId, Integer tagId) {
        return apiService.evaluateAppeal(messageId, tagId);
    }

    @Override
    public Observable<BaseListDataResponse<SystemMessageBean>> getMessageSystem(Integer page) {
        return apiService.getMessageSystem(page);
    }

    @Override
    public Observable<BaseResponse> deleteMessage(String type, Integer id) {
        if ("system".equals(type)) {
            return apiService.deleteMessageSystem(id);
        } else if ("apply".equals(type)) {
            return apiService.deleteMessageApply(id);
        } else if ("broadcast".equals(type)) {
            return apiService.deleteMessageBroadcast(id);
        } else if ("comment".equals(type)) {
            return apiService.deleteMessageComment(id);
        } else if ("sign".equals(type)) {
            return apiService.deleteMessageSign(id);
        } else if ("give".equals(type)) {
            return apiService.deleteMessageGive(id);
        } else if ("evaluate".equals(type)) {
            return apiService.deleteMessageEvaluate(id);
        } else if ("profit".equals(type)) {
            return apiService.deleteMessageProfit(id);
        }
        throw new IllegalArgumentException("Unknown type : " + type);
    }

    @Override
    public Observable<BaseDataResponse<List<MessageGroupBean>>> getMessageList() {
        return apiService.getMessageList();
    }

    @Override
    public Observable<BaseDataResponse<PushSettingBean>> getPushSetting() {
        return apiService.getPushSetting();
    }

    @Override
    public Observable<BaseResponse> savePushSetting(PushSettingBean pushSettingEntity) {
        return apiService.savePushSetting(
                pushSettingEntity.getChat() == null ? null : pushSettingEntity.getChat() ? 1 : 0,
                pushSettingEntity.getSign() == null ? null : pushSettingEntity.getSign() ? 1 : 0,
                pushSettingEntity.getGive() == null ? null : pushSettingEntity.getGive() ? 1 : 0,
                pushSettingEntity.getComment() == null ? null : pushSettingEntity.getComment() ? 1 : 0,
                pushSettingEntity.getBroadcast() == null ? null : pushSettingEntity.getBroadcast() ? 1 : 0,
                pushSettingEntity.getApply() == null ? null : pushSettingEntity.getApply() ? 1 : 0,
                pushSettingEntity.getInvitation() == null ? null : pushSettingEntity.getInvitation() ? 1 : 0,
                pushSettingEntity.getSound() == null ? null : pushSettingEntity.getSound() ? 1 : 0,
                pushSettingEntity.getShake() == null ? null : pushSettingEntity.getShake() ? 1 : 0
        );
    }

    @Override
    public Observable<BaseResponse> TopicalGive(Integer id) {
        return apiService.TopicalGive(id);
    }

    @Override
    public Observable<BaseResponse> TopicalFinish(Integer id) {
        return apiService.TopicalFinish(id);
    }

    @Override
    public Observable<BaseResponse> burnReset() {
        return apiService.burnReset();
    }

    @Override
    public Observable<BaseDataResponse<CreateOrderBean>> createOrder(Integer id, Integer type, Integer payType, Integer toUserId) {
        return apiService.createOrder(id, type, payType, toUserId);
    }

    @Override
    public Observable<BaseResponse> coinPayOrder(String orderNumber) {
        return apiService.coinPayOrder(orderNumber);
    }

    @Override
    public Observable<BaseListDataResponse<BaseUserBeanBean>> getNewsGiveList(Integer id, Integer page) {
        return apiService.getNewsGiveList(id, page);
    }

    @Override
    public Observable<BaseListDataResponse<BaseUserBeanBean>> getTopicalGiveList(Integer id, Integer page) {
        return apiService.getTopicalGiveList(id, page);
    }

    @Override
    public Observable<BaseDataResponse<TopicalListBean>> topicalDetail(Integer id) {
        return apiService.topicalDetail(id);
    }

    @Override
    public Observable<BaseResponse> checkTopical() {
        return apiService.checkTopical();
    }

    @Override
    public Observable<BaseResponse> signUpReport(Integer id) {
        return apiService.signUpReport(id);
    }

    @Override
    public Observable<BaseDataResponse<ChatRedPackageBean>> sendCoinRedPackage(Integer userId, Integer money, String desc) {
        return apiService.sendCoinRedPackage(userId, money, desc);
    }

    @Override
    public Observable<BaseDataResponse<ChatRedPackageBean>> getCoinRedPackage(int id) {
        return apiService.getCoinRedPackage(id);
    }

    @Override
    public Observable<BaseResponse> receiveCoinRedPackage(int id) {
        return apiService.receiveCoinRedPackage(id);
    }

    @Override
    public Observable<BaseListDataResponse<UserCoinItemBean>> userCoinEarnings(int page) {
        return apiService.userCoinEarnings(page);
    }

    @Override
    public Observable<BaseDataResponse<UserConnMicStatusBean>> userIsConnMic(int userId) {
        return apiService.userIsConnMic(userId);
    }

    @Override
    public Observable<BaseResponse> paySuccessNotify(String packageName, String orderNumber, List<String> productId, String token, int type, Integer event) {
        return apiService.paySuccessNotify(packageName, orderNumber, productId, token, type, event);
    }

    @Override
    public Observable<BaseResponse> pushDeviceToken(String deviceId, String version_number) {
        return apiService.pushDeviceToken(deviceId, version_number);
    }

    @Override
    public Observable<BaseResponse> replyApplyAlubm(int applyId, boolean status) {
        return apiService.replyApplyAlubm(applyId, status ? 1 : 2);
    }

    @Override
    public Observable<BaseResponse> checkApplyAlbumPhoto(int applyId) {
        return apiService.checkApplyAlbumPhoto(applyId);
    }

    @Override
    public Observable<BaseDataResponse<StatusBean>> publishCheck(int type) {
        return apiService.publishCheck(type);
    }

    @Override
    public Observable<BaseDataResponse<UnReadMessageNumBean>> getUnreadMessageNum() {
        return apiService.getUnreadMessageNum();
    }

    @Override
    public Observable<BaseDataResponse<CoinExchangeBoxInfo>> getCoinExchangeBoxInfo() {
        return apiService.getCoinExchangeBoxInfo();
    }

    @Override
    public Observable<BaseResponse> exchangeCoins(int id) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("id", id);
        return apiService.exchangeCoins(ApiUitl.getBody(GsonUtils.toJson(map)));
    }

    @Override
    public Observable<BaseDataResponse<List<GameCoinBuy>>> buyGameCoins() {
        return apiService.buyGameCoins("recharge");
    }

    @Override
    public Observable<BaseDataResponse<ApiConfigManagerBean>> initApiConfig() {
        return apiService.initApiConfig();
    }

    @Override
    public Observable<BaseResponse> cancellation() {
        return apiService.cancellation();
    }

    @Override
    public Observable<BaseResponse> ExchangeIntegraBuy(Integer id) {
        return apiService.ExchangeIntegraBuy(id);
    }

    @Override
    public Observable<BaseDataResponse<ExchangeIntegraOuterBean>> getExchangeIntegraListData() {
        return apiService.getExchangeIntegraListData();
    }

    @Override
    public Observable<BaseListDataResponse<TaskAdBean>> taskAdList() {
        return apiService.taskAdList();
    }

    @Override
    public Observable<BaseResponse> subSupply(List<Integer> exchange_ids, Integer address_id) {
        return apiService.subSupply(exchange_ids,address_id);
    }

    @Override
    public Observable<BaseResponse> removeAddress(Integer id) {
        return apiService.removeAddress(id);
    }

    @Override
    public Observable<BaseDataResponse<AddressBean>> getAddress(Integer id) {
        return apiService.getAddress(id);
    }

    @Override
    public Observable<BaseListDataResponse<AddressBean>> getAddressList(Integer page) {
        return apiService.getAddressList(page);
    }

    @Override
    public Observable<BaseResponse> createAddress(String contacts, String city, String are, String address, String phone, Integer is_default) {
        return apiService.createAddress(contacts,city,are,address,phone,is_default);
    }

    @Override
    public Observable<BaseResponse> updateAddress(Integer id, String contacts, String city, String are, String address, String phone, Integer is_default) {
        return apiService.updateAddress(id, contacts, city, are, address, phone, is_default);
    }

    @Override
    public Observable<BaseListDataResponse<ExchangeBean>> qryExchange(Integer page, Integer status) {
        return apiService.qryExchange(page, status);
    }

    @Override
    public Observable<BaseResponse> exchange(String goodsId) {
        return apiService.exchange(goodsId);
    }

    @Override
    public Observable<BaseListDataResponse<BonusGoodsBean>> getBonusGoods(Integer page) {
        return apiService.getBonusGoods(page);
    }

    @Override
    public Observable<BaseListDataResponse<GoldDetailBean>> getGoldList(Integer page) {
        return apiService.getGoldList(page);
    }

    @Override
    public Observable<BaseResponse> ToaskSubBonus(String key) {
        return apiService.ToaskSubBonus(key);
    }

    @Override
    public Observable<BaseDataResponse<TaskRewardReceiveBean>> TaskRewardReceive(String key) {
        return apiService.TaskRewardReceive(key);
    }

    @Override
    public Observable<BaseDataResponse<List<TaskConfigItemBean>>> getTaskListConfig() {
        return apiService.getTaskListConfig();
    }

    @Override
    public Observable<BaseDataResponse<TaskConfigBean>> getTaskConfig() {
        return apiService.getTaskConfig();
    }

    @Override
    public Observable<BaseDataResponse<EjectSignInBean>> reportEjectSignIn() {
        return apiService.reportEjectSignIn();
    }

    @Override
    public Observable<BaseDataResponse<EjectBean>> getEjectconfig() {
        return apiService.getEjectconfig();
    }

    @Override
    public Observable<BaseDataResponse<Map<String, String>>> authLoginPost(String id, String type, String email, String avatar, String nickName, String device_code, String business) {
        return apiService.authLoginPost(id, type, email, avatar, nickName, device_code, business);
    }

    @Override
    public Observable<BaseDataResponse<LevelApiBean>> adjustLevelPrice(RequestBody requestBody) {
        return apiService.adjustLevelPrice(requestBody);
    }

    @Override
    public Observable<BaseDataResponse<LevelPageInfoBean>> getUserLevelPageInfo() {
        return apiService.getUserLevelPageInfo();
    }

    @Override
    public Observable<BaseResponse> sendEmailCode(String email) {
        return apiService.sendEmailCode(email);
    }

    @Override
    public Observable<BaseResponse> bindUserEmail(String email, String code, String pass, Integer type) {
        return apiService.bindUserEmail(email, code, pass, type);
    }

    @Override
    public Observable<BaseDataResponse<UserDataBean>> loginEmail(String email, String code, Integer type) {
        return apiService.loginEmail(email, code, type);
    }

    @Override
    public Observable<BaseDataResponse<CallingStatusBean>> getCallingStatus(Integer roomId) {
        return apiService.getCallingStatus(roomId);
    }

    @Override
    public Observable<BaseDataResponse<CallingStatusBean>> getRoomStatus(Integer roomId) {
        return apiService.getRoomStatus(roomId);
    }

    @Override
    public Observable<BaseDataResponse<AdBannerBean>> getMainAdBannerList(int position) {
        return apiService.getMainAdBannerList(position);
    }

    @Override
    public Observable<BaseDataResponse<MallWithdrawTipsInfoBean>> getMallWithdrawTipsInfo(Integer channel) {
        return apiService.getMallWithdrawTipsInfo(channel);
    }

    @Override
    public Observable<BaseResponse> photoCallCover(Integer albumId, Integer type) {
        return apiService.photoCallCover(albumId, type);
    }

    @Override
    public Observable<BaseDataResponse<CityAllBean>> getCityConfigAll() {
        return apiService.getCityConfigAll();
    }

    @Override
    public Observable<BaseDataResponse<CheckNicknameBean>> checkNickname(String nickname) {
        return apiService.checkNickname(nickname);
    }

    @Override
    public Observable<BaseDataResponse<ChooseAreaBean>> getChooseAreaList() {
        return apiService.getChooseAreaList();
    }

    @Override
    public Observable<BaseDataResponse<AdBannerBean>> getRadioAdBannerList(int position) {
        return apiService.getRadioAdBannerList(position);
    }

    @Override
    public Observable<BaseDataResponse<AdUserBannerBean>> getUserAdList(Integer position) {
        return apiService.getUserAdList(position);
    }

    @Override
    public Observable<BaseDataResponse> putNoteText(int user_id, String note) {
        return apiService.putNoteText(user_id, note);
    }

    @Override
    public Observable<BaseDataResponse<NoteInfoBean>> getNoteText(int user_id) {
        return apiService.getNoteText(user_id);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherBalanceDataBean>> qryUserGameBalance() {
        return apiService.qryUserGameBalance();
    }

    @Override
    public Observable<BaseResponse> mediaGalleryPay(String msgKey, Integer toUserId) {
        return apiService.mediaGalleryPay(msgKey, toUserId);
    }

    @Override
    public Observable<BaseDataResponse<Map<String, Integer>>> mediaGalleryEvaluationQry(String msgKey, Integer toUserId) {
        return apiService.mediaGalleryEvaluationQry(msgKey, toUserId);
    }

    @Override
    public Observable<BaseResponse> mediaGalleryEvaluationPut(String msgKey, Integer toUserId, Integer type) {
        return apiService.mediaGalleryEvaluationPut(msgKey, toUserId, type);
    }

    @Override
    public Observable<BaseResponse> mediaGallerySnapshotUnLock(String msgKey, Integer toUserId) {
        return apiService.mediaGallerySnapshotUnLock(msgKey, toUserId);
    }

    @Override
    public Observable<BaseDataResponse<List<CoinPusherRoomHistoryBean>>> qryCoinPusherRoomHistory(Integer roomId) {
        return apiService.qryCoinPusherRoomHistory(roomId);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherDataInfoBean>> playingCoinPusherStart(Integer roomId) {
        return apiService.playingCoinPusherStart(roomId);
    }

    @Override
    public Observable<BaseResponse> playingCoinPusherClose(Integer roomId) {
        return apiService.playingCoinPusherClose(roomId);
    }

    @Override
    public Observable<BaseResponse> playingCoinPusherAct(Integer roomId) {
        return apiService.playingCoinPusherAct(roomId);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherBalanceDataBean>> playingCoinPusherThrowCoin(Integer roomId) {
        return apiService.playingCoinPusherThrowCoin(roomId);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherBalanceDataBean>> convertCoinPusherGoldsCoin(Integer id, Integer type) {
        return apiService.convertCoinPusherGoldsCoin(id, type);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherBalanceDataBean>> convertCoinPusherDiamonds(Integer id) {
        return apiService.convertCoinPusherDiamonds(id);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherRoomTagInfoBean>> qryCoinPusherRoomTagList() {
        return apiService.qryCoinPusherRoomTagList();
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherRoomInfoBean>> qryCoinPusherRoomList(Integer tagId) {
        return apiService.qryCoinPusherRoomList(tagId);
    }

    @Override
    public Observable<BaseDataResponse<CoinPusherConverInfoBean>> qryCoinPusherConverList() {
        return apiService.qryCoinPusherConverList();
    }

    @Override
    public Observable<BaseResponse> friendAddFrequent(RequestBody requestBody) {
        return apiService.friendAddFrequent(requestBody);
    }

    @Override
    public Observable<BaseResponse> friendDeleteFrequent(RequestBody requestBody) {
        return apiService.friendDeleteFrequent(requestBody);
    }

    @Override
    public Observable<BaseDataResponse<List<ExclusiveAccostInfoBean>>> getExclusiveAccost() {
        return apiService.getExclusiveAccost();
    }

    @Override
    public Observable<BaseDataResponse> delExclusiveAccost(int type) {
        return apiService.delExclusiveAccost(type);
    }

    @Override
    public Observable<BaseDataResponse> setExclusiveAccost(Integer type, String content, int len) {
        return apiService.setExclusiveAccost(type, content,len);
    }

    @Override
    public Observable<BaseDataResponse<DayRewardInfoBean>> getDayReward() {
        return apiService.getDayReward();
    }

    @Override
    public Observable<BaseDataResponse> getRandName() {
        return apiService.getRandName();
    }

    @Override
    public Observable<BaseDataResponse<DayRewardInfoBean>> getRegisterReward() {
        return apiService.getRegisterReward();
    }
}