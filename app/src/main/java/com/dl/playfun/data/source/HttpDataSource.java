package com.dl.playfun.data.source;

import com.dl.playfun.data.source.http.response.BaseDataResponse;
import com.dl.playfun.data.source.http.response.BaseListDataResponse;
import com.dl.playfun.data.source.http.response.BaseResponse;
import com.dl.playfun.entity.AccostEntity;
import com.dl.playfun.entity.AdBannerEntity;
import com.dl.playfun.entity.AdUserBannerEntity;
import com.dl.playfun.entity.AddressEntity;
import com.dl.playfun.entity.AlbumPhotoEntity;
import com.dl.playfun.entity.AllConfigEntity;
import com.dl.playfun.entity.ApiConfigManagerEntity;
import com.dl.playfun.entity.ApplyMessageEntity;
import com.dl.playfun.entity.BaseUserBeanEntity;
import com.dl.playfun.entity.BlackEntity;
import com.dl.playfun.entity.BonusGoodsEntity;
import com.dl.playfun.entity.BoradCastMessageEntity;
import com.dl.playfun.entity.BroadcastEntity;
import com.dl.playfun.entity.BroadcastListEntity;
import com.dl.playfun.entity.BrowseNumberEntity;
import com.dl.playfun.entity.BubbleEntity;
import com.dl.playfun.entity.CallingInfoEntity;
import com.dl.playfun.entity.CallingInviteInfo;
import com.dl.playfun.entity.CallingStatusEntity;
import com.dl.playfun.entity.CashWalletEntity;
import com.dl.playfun.entity.ChatDetailCoinEntity;
import com.dl.playfun.entity.ChatRedPackageEntity;
import com.dl.playfun.entity.CheckNicknameEntity;
import com.dl.playfun.entity.ChooseAreaEntity;
import com.dl.playfun.entity.CityAllEntity;
import com.dl.playfun.entity.CoinExchangeBoxInfo;
import com.dl.playfun.entity.CoinPusherBalanceDataEntity;
import com.dl.playfun.entity.CoinPusherConverInfoEntity;
import com.dl.playfun.entity.CoinPusherDataInfoEntity;
import com.dl.playfun.entity.CoinPusherRoomHistoryEntity;
import com.dl.playfun.entity.CoinPusherRoomInfoEntity;
import com.dl.playfun.entity.CoinPusherRoomTagInfoEntity;
import com.dl.playfun.entity.CoinWalletEntity;
import com.dl.playfun.entity.CommentMessageEntity;
import com.dl.playfun.entity.ConfigItemEntity;
import com.dl.playfun.entity.CreateOrderEntity;
import com.dl.playfun.entity.DayRewardInfoEntity;
import com.dl.playfun.entity.DiamondInfoEntity;
import com.dl.playfun.entity.EjectEntity;
import com.dl.playfun.entity.EjectSignInEntity;
import com.dl.playfun.entity.EvaluateEntity;
import com.dl.playfun.entity.EvaluateMessageEntity;
import com.dl.playfun.entity.ExchangeEntity;
import com.dl.playfun.entity.ExchangeIntegraOuterEntity;
import com.dl.playfun.entity.ExclusiveAccostInfoEntity;
import com.dl.playfun.entity.FaceVerifyResultEntity;
import com.dl.playfun.entity.GameCoinBuy;
import com.dl.playfun.entity.GameCoinWalletEntity;
import com.dl.playfun.entity.GamePhotoAlbumEntity;
import com.dl.playfun.entity.GiftBagEntity;
import com.dl.playfun.entity.GiveMessageEntity;
import com.dl.playfun.entity.GoldDetailEntity;
import com.dl.playfun.entity.GoodsEntity;
import com.dl.playfun.entity.GoogleNearPoiBean;
import com.dl.playfun.entity.GooglePoiBean;
import com.dl.playfun.entity.IMTransUserEntity;
import com.dl.playfun.entity.ImUserSigEntity;
import com.dl.playfun.entity.IsChatEntity;
import com.dl.playfun.entity.LevelApiEntity;
import com.dl.playfun.entity.LevelPageInfoEntity;
import com.dl.playfun.entity.MallWithdrawTipsInfoEntity;
import com.dl.playfun.entity.MessageGroupEntity;
import com.dl.playfun.entity.MessageRuleEntity;
import com.dl.playfun.entity.NewsEntity;
import com.dl.playfun.entity.NoteInfoEntity;
import com.dl.playfun.entity.OccupationConfigItemEntity;
import com.dl.playfun.entity.ParkItemEntity;
import com.dl.playfun.entity.PhotoAlbumEntity;
import com.dl.playfun.entity.PriceConfigEntity;
import com.dl.playfun.entity.PrivacyEntity;
import com.dl.playfun.entity.ProfitMessageEntity;
import com.dl.playfun.entity.PushSettingEntity;
import com.dl.playfun.entity.RadioTwoFilterItemEntity;
import com.dl.playfun.entity.SignMessageEntity;
import com.dl.playfun.entity.SoundEntity;
import com.dl.playfun.entity.StatusEntity;
import com.dl.playfun.entity.SwiftMessageEntity;
import com.dl.playfun.entity.SystemMessageEntity;
import com.dl.playfun.entity.TagEntity;
import com.dl.playfun.entity.TaskAdEntity;
import com.dl.playfun.entity.TaskConfigEntity;
import com.dl.playfun.entity.TaskConfigItemEntity;
import com.dl.playfun.entity.TaskRewardReceiveEntity;
import com.dl.playfun.entity.TokenEntity;
import com.dl.playfun.entity.TopicalListEntity;
import com.dl.playfun.entity.TraceEntity;
import com.dl.playfun.entity.UnReadMessageNumEntity;
import com.dl.playfun.entity.UserCoinItemEntity;
import com.dl.playfun.entity.UserConnMicStatusEntity;
import com.dl.playfun.entity.UserDataEntity;
import com.dl.playfun.entity.UserDetailEntity;
import com.dl.playfun.entity.UserInfoEntity;
import com.dl.playfun.entity.UserProfitPageEntity;
import com.dl.playfun.entity.UserRemarkEntity;
import com.dl.playfun.entity.VersionEntity;
import com.dl.playfun.entity.VipInfoEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpDataSource {

    /**
     * @Desc TODO(支付购买聊天中的付费资源)
     * @author 彭石林
     * @parame [
     * msgKey    string	聊天中的msgKey
     * toUserId	int	对方的用户id]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/9/15
     */
    @FormUrlEncoded
    Observable<BaseResponse> mediaGalleryPay(String msgKey,Integer toUserId);

    /**
     * @Desc TODO(获取红包相片/影片评价)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<java.util.Map<java.lang.String,java.lang.Integer>>>
     * @Date 2022/9/17
     */
    Observable<BaseDataResponse<Map<String, Integer>>> mediaGalleryEvaluationQry(String msgKey, Integer toUserId);
    /**
     * @Desc TODO(红包相片/影片评价)
     * @author 彭石林
     * @parame [
     * msgKey	string	聊天中的msgKey
     *     toUserId	int	对方的用户id
     *     type	int	评价，1差评，2好评
     * ]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/9/17
     */
    Observable<BaseResponse> mediaGalleryEvaluationPut(
            String msgKey,
            Integer toUserId,
            Integer type
    );

    /**
     * @Desc TODO(红包照片设置以读)
     * @author 彭石林
     * @parame [msgKey, toUserId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/9/16
     */
    Observable<BaseResponse> mediaGallerySnapshotUnLock(String msgKey, Integer toUserId);

    /**
     * @Desc TODO(查询用户当前余额)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinPusherBalanceDataEntity>>
     * @Date 2022/9/6
     */
    @GET("api/iscan/balance")
    Observable<BaseDataResponse<CoinPusherBalanceDataEntity>> qryUserGameBalance();

    /**
     * @Desc TODO(推币机-查询历史中奖记录)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<java.util.List<com.dl.playfun.entity.CoinPusherRoomHistoryEntity>>>
     * @Date 2022/8/26
     */
    Observable<BaseDataResponse<List<CoinPusherRoomHistoryEntity>>> qryCoinPusherRoomHistory(Integer roomId);
    /**
     * @Desc TODO(推币机-开始游戏)
     * @author 彭石林
     * @parame [roomId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinPusherDataInfoEntity>>
     * @Date 2022/9/1
     */
    Observable<BaseDataResponse<CoinPusherDataInfoEntity>> playingCoinPusherStart(Integer roomId);
    /**
     * @Desc TODO(推币机-结束游戏)
     * @author 彭石林
     * @parame [roomId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/24
     */
    Observable<BaseResponse> playingCoinPusherClose(Integer roomId);
    /**
     * @Desc TODO(推币机-操作雨刷)
     * @author 彭石林
     * @parame [roomId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/24
     */
    Observable<BaseResponse> playingCoinPusherAct(Integer roomId);
    /**
     * @Desc TODO(推币机-投币)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/24
     */
    Observable<BaseDataResponse<CoinPusherBalanceDataEntity>> playingCoinPusherThrowCoin(Integer roomId);
    /**
     * @Desc TODO(推币机-钻石兑金币)
     * @author 彭石林
     * @parame [
     * amount	是	int	兑换值
     * type	是	int	兑换类型 1金币 2钻石 3搭讪卡
     * ]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/24
     */
    Observable<BaseDataResponse<CoinPusherBalanceDataEntity>> convertCoinPusherGoldsCoin(Integer id, Integer type);
    /**
     * @Desc TODO(推币机-兑换钻石)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/24
     */
    Observable<BaseDataResponse<CoinPusherBalanceDataEntity>> convertCoinPusherDiamonds(Integer id);
    /**
     * @Desc TODO(推币机-等级列表)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinPusherRoomTagInfoEntity>>
     * @Date 2022/8/24
     */
    Observable<BaseDataResponse<CoinPusherRoomTagInfoEntity>> qryCoinPusherRoomTagList();
    /**
     * @Desc TODO(推币机-设备列表)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinPusherRoomInfoEntity>>
     * @Date 2022/8/24
     */
    Observable<BaseDataResponse<CoinPusherRoomInfoEntity>> qryCoinPusherRoomList(Integer tagId);

    /**
     * @Desc TODO(推币机-兑换列表)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinPusherConverInfoEntity>>
     * @Date 2022/8/23
     */
    Observable<BaseDataResponse<CoinPusherConverInfoEntity>> qryCoinPusherConverList();
    /**
     * @Desc TODO(添加常联系人)
     * @author 彭石林
     * @parame [requestBody]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/16
     */
    Observable<BaseResponse> friendAddFrequent(RequestBody requestBody);
    /**
     * @Desc TODO(添加常联系人)
     * @author 彭石林
     * @parame [requestBody]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/16
     */
    Observable<BaseResponse> friendDeleteFrequent(RequestBody requestBody);
    /**
     * 获取专属搭讪
     * @return
     */
    Observable<BaseDataResponse<List<ExclusiveAccostInfoEntity>>> getExclusiveAccost();

    /**
     * 删除专属搭讪
     * @return
     */
    Observable<BaseDataResponse> delExclusiveAccost(int type);

    /**
     * 设置专属搭讪
     * @param type 设置专属搭讪
     * @param content 内容
     * @return
     */
    Observable<BaseDataResponse> setExclusiveAccost(Integer type, String content,int len);

    /**
     * 每日奖励
     * @return
     */
    Observable<BaseDataResponse<DayRewardInfoEntity>> getDayReward();

    /**
     * 获取昵称
     * @return
     */
    Observable<BaseDataResponse> getRandName();


    /**
     * 注册奖励
     * @return
     */
    Observable<BaseDataResponse<DayRewardInfoEntity>> getRegisterReward();


    /**
     * @Desc TODO(设置视讯广场封面图)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/8/2
     */
    Observable<BaseResponse> photoCallCover(Integer albumId, Integer type);

    /**
     * @return
     * @Desc TODO(获取所有城市内容)
     * @author 彭石林
     */
    Observable<BaseDataResponse<CityAllEntity>> getCityConfigAll();

    /**
     * 检查昵称是否被占用
     *
     * @param nickname
     * @return
     */
    Observable<BaseDataResponse<CheckNicknameEntity>> checkNickname(
            String nickname
    );

    /*
     * @Desc TODO(手机号码注册拉取所有地区)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse<com.dl.play.chat.entity.ChooseAreaEntity>>
     * @Date 2022/7/7
     */
    Observable<BaseDataResponse<ChooseAreaEntity>> getChooseAreaList();

    /**
     * @Desc TODO(广告列表获取  1：首页 2：广场页)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse<com.dl.playfun.entity.AdItemEntity>>
     * @Date 2022/7/25
     */
    Observable<BaseDataResponse<AdBannerEntity>> getRadioAdBannerList(int position);

    /**
     * @Desc TODO(用户广告位)
     * @author 彭石林
     * @parame [position]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Date 2022/7/26
     */
    @GET("api/userAd")
    Observable<BaseDataResponse<AdUserBannerEntity>> getUserAdList(Integer position);

    /**
     * 保存个人笔记内容
     * @param user_id
     * @param note
     * @return
     */
    Observable<BaseDataResponse> putNoteText(int user_id, String note);

    /**
     * 获取个人笔记内容
     * @param user_id
     * @return
     */
    Observable<BaseDataResponse<NoteInfoEntity>> getNoteText(int user_id);

    /**
     * @Desc TODO(广告列表获取  1：首页 2：广场页)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse<com.dl.playfun.entity.AdItemEntity>>
     * @Date 2022/7/25
     */
    Observable<BaseDataResponse<AdBannerEntity>> getMainAdBannerList(int position);

    /***
     * 水晶兑换弹窗提示
     * @param channel 渠道类型 ：1安卓  2ios
     * @return
     */
    Observable<BaseDataResponse<MallWithdrawTipsInfoEntity>> getMallWithdrawTipsInfo(Integer channel);

    /**
     * @Desc TODO(初始化api接口)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.ApiConfigManagerEntity>>
     * @Date 2022/7/2
     */
    Observable<BaseDataResponse<ApiConfigManagerEntity>> initApiConfig();

    /**
     * 注销账号
     *
     * @return
     */
    Observable<BaseResponse> cancellation();

    /*=====================================================任务中心相关接口=================================================*/
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(钻石兑换积分购买接口 。 积分ID)
     * @author 彭石林
     * @parame [id]
     * @Date 2021/9/23
     */
    @FormUrlEncoded
    Observable<BaseResponse> ExchangeIntegraBuy(Integer id);
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.ExchangeIntegraEntity>>
     * @Desc TODO(钻石兑换积分列表)
     * @author 彭石林
     * @parame []
     * @Date 2021/9/23
     */
    Observable<BaseDataResponse<ExchangeIntegraOuterEntity>> getExchangeIntegraListData();
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse>
     * @Desc TODO(任务中心广告位)
     * @author 彭石林
     * @parame []
     * @Date 2021/9/4
     */
    Observable<BaseListDataResponse<TaskAdEntity>> taskAdList();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(兑换提货)
     * @author 彭石林
     * @parame [permanent_city_ids, address_id]
     * @Date 2021/8/14
     */
    @FormUrlEncoded
    @POST("api/v2/exchange/supply")
    Observable<BaseResponse> subSupply(List<Integer> exchange_ids, Integer address_id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(删除收获地址)
     * @author 彭石林
     * @parame [id]
     * @Date 2021/8/16
     */
    @DELETE("/api/v2/address/{id}")
    Observable<BaseResponse> removeAddress(Integer id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.AddressEntity>>
     * @Desc TODO(查询用户默认收获地址)
     * @author 彭石林
     * @parame [id]
     * @Date 2021/8/13
     */
    @GET("api/v2/address/view")
    Observable<BaseDataResponse<AddressEntity>> getAddress(Integer id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.AddressEntity>>
     * @Desc TODO(查询用户所有收获地址)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/13
     */
    @GET("api/v2/address")
    Observable<BaseListDataResponse<AddressEntity>> getAddressList(Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(添加收货地址)
     * @author 彭石林
     * @parame [contacts, city, are, address, phone, is_default]
     * @Date 2021/8/13
     */
    @POST("api/v2/address")
    Observable<BaseResponse> createAddress(
            String contacts,
            String city,
            String are,
            String address,
            String phone,
            Integer is_default);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(修改收货地址)
     * @author 彭石林
     * @parame [contacts, city, are, address, phone, is_default]
     * @Date 2021/8/13
     */
    Observable<BaseResponse> updateAddress(
            Integer id,
            String contacts,
            String city,
            String are,
            String address,
            String phone,
            Integer is_default);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.ExchangeEntity>>
     * @Desc TODO(兑换记录)
     * @author 彭石林
     * @parame [page]
     * @Date 2021/8/10
     */
    Observable<BaseListDataResponse<ExchangeEntity>> qryExchange(Integer page, Integer status);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(积分商品兑换)
     * @author 彭石林
     * @parame [goodsId]
     * @Date 2021/8/10
     */
    Observable<BaseResponse> exchange(String goodsId);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < sBonusGoodsEntity>>
     * @Desc TODO(积分商品列表)
     * @author 彭石林
     * @parame [page]
     * @Date 2021/8/10
     */
    Observable<BaseListDataResponse<BonusGoodsEntity>> getBonusGoods(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(获取积分明细列表)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/9
     */
    Observable<BaseListDataResponse<GoldDetailEntity>> getGoldList(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(领取积分)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/9
     */
    Observable<BaseResponse> ToaskSubBonus(@Query("type") String key);

    /**
     * @param key
     * @Desc TODO(领取任务)
     * @author liaosf
     */
    Observable<BaseDataResponse<TaskRewardReceiveEntity>> TaskRewardReceive(@Query("slug") String key);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.TaskConfigListEntity>>
     * @Desc TODO(获取新手任务 、 每日任务)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/10
     */
    Observable<BaseDataResponse<List<TaskConfigItemEntity>>> getTaskListConfig();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < TaskConfigEntity>>
     * @Desc TODO(获取任务中心配置)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/7
     */
    Observable<BaseDataResponse<TaskConfigEntity>> getTaskConfig();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.EjectSignInEntity>>
     * @Desc TODO(每日签到 。 签到成功)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/6
     */
    Observable<BaseDataResponse<EjectSignInEntity>> reportEjectSignIn();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.EjectEntity>>
     * @Desc TODO(查询用户每日签到)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/6
     */
    Observable<BaseDataResponse<EjectEntity>> getEjectconfig();

    /*=====================================================任务中心相关接口=================================================*/

    /**
     * 第三方登录
     *
     * @param id       唯一ID
     * @param type     登录类型 facebook/line
     * @param email    邮箱
     * @param avatar   头像
     * @param nickName 昵称
     *                 String device_code 设备序列号
     * @return
     */
    Observable<BaseDataResponse<Map<String, String>>> authLoginPost(
            String id,
            String type,
            String email,
            String avatar,
            String nickName,
            String device_code,
            String business
    );

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.LevelApiEntity>>
     * @Desc TODO(主播调价)
     * @author 彭石林
     * @parame [requestBody]
     * @Date 2022/6/22
     */
    Observable<BaseDataResponse<LevelApiEntity>> adjustLevelPrice(RequestBody requestBody);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.UserLevelPageInfoEntity>>
     * @Desc TODO(用户等级功能页面)
     * @author 彭石林
     * @parame []
     * @Date 2022/6/21
     */
    Observable<BaseDataResponse<LevelPageInfoEntity>> getUserLevelPageInfo();

    /**
     * @Desc TODO(根据邮箱发送验证码)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/5/17
     */
    Observable<BaseResponse> sendEmailCode(String email);

    /**
     * @Desc TODO(绑定用户邮箱)
     * @author 彭石林
     * @parame [email, code, pass, type]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.UserDataEntity>>
     * @Date 2022/5/17
     */
    Observable<BaseResponse> bindUserEmail(
            String email, //邮箱账号
            String code, //验证码
            String pass, //账户密码
            Integer type //1绑定邮箱(邮箱/验证码) 2设置密码(两次密码) 3绑定邮箱(邮箱/验证码/两次密码) 4修改密码(验证码/两次密码)
    );

    /**
     * @Desc TODO(邮箱登录)
     * @author 彭石林
     * @parame [email, code, type]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/5/17
     */
    Observable<BaseDataResponse<UserDataEntity>> loginEmail(
            String email, //邮箱账号
            String code, //验证码/密码
            Integer type //1验证码登陆 2密码登陆
    );

    /**
     * 获取通话状态 需要两个人都已进入房间，分两种情况：1、已解散房间；2、未解散房间。房间已解散状态下部分字段返回0
     * @param roomId
     * @return
     */
    Observable<BaseDataResponse<CallingStatusEntity>> getCallingStatus(
            Integer roomId //房间号
    );

    /**
     * 获取房间状态，用于检查是否已解散
     * @param roomId
     * @return
     */
    Observable<BaseDataResponse<CallingStatusEntity>> getRoomStatus(
            Integer roomId //房间号
    );

    /**
     * @Desc TODO(IM用户Id转成数值id)
     * @author 彭石林
     * @parame [IMUserId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.IMTransUserEntity>>
     * @Date 2022/4/2
     */
    Observable<BaseDataResponse<IMTransUserEntity>> transUserIM(String IMUserId);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(查询用户状态)
     * @author 彭石林
     * @parame [user_id]
     * @Date 2022/6/8
     */
    Observable<BaseResponse> userMessageCollation(Integer user_id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.ChatDetailCoinEntity>>
     * @Desc TODO(拨打完成后调用查询总钻石 。 拨打发调用)
     * @author 彭石林
     * @parame [dismissRoom]
     * @Date 2022/3/21
     */
    Observable<BaseDataResponse<ChatDetailCoinEntity>> getTotalCoins(Integer dismissRoom);

    /**
     * @Desc TODO(游戏支付成功验签)
     * @author 彭石林
     * @parame [packageName, orderNumber, productId, token, type, event, serverId, roleId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/2/10
     */
    Observable<BaseResponse> GamePaySuccessNotify(
            String packageName,
            String orderNumber,
            List<String> productId,
            String token,
            int type,
            Integer event,
            String serverId,
            String roleId
    );
    /**
     * @Desc TODO()
     * @author 彭石林
     * @parame [serverId, roleId]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.GamePhotoAlbumEntity>>
     * @Date 2022/1/21
     */
    Observable<BaseDataResponse<GamePhotoAlbumEntity>> getGamePhotoAlbumList(String serverId, String roleId);
    /**
     * @Desc TODO(游戏在线状态 1在线 -1离线)
     * @author 彭石林
     * @parame [gameState]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/1/15
     */
    Observable<BaseResponse> setGameState(int gameState);
    /**
     * @Desc TODO(serverId 是 String 区服ID ， 最多45个字符
     *roleId 是 String 角色ID ， 最多45个字符
     *roleName 是 String 角色名称 ， 最多100个字符
     *avatarUrl 是 String 角色头像URL地址 ， 最多1000个字符)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/1/14
     */
    Observable<BaseResponse> commitRoleInfo(RequestBody requestBody);

    /**
     * @Desc TODO(修改用户性别)
     * @author 彭石林
     * @parame [sex]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2022/1/12
     */
    Observable<BaseResponse> upUserSex(Integer sex);

    /**
     * 聊天页面创建订单
     *
     * @param id      id 用户ID     当type为10时id传当前登陆用户id  当type为11时id传要解锁的用户ID
     * @param type    下单类型 1充值 2会员 3相册付费 4照片红包 5私聊 8发布动态 9发布节目 10一健打招呼钻石支付 11解锁社交账号
     * @param payType 1/余额支付 2/google支付 3/my_card支付 4/苹果支付
     * @return
     */
    Observable<BaseDataResponse<CreateOrderEntity>> createChatDetailOrder(
            Integer id,
            Integer type,
            Integer payType,
            Integer toUserId,
            Integer channel
    );

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.PriceConfigEntity.Current>>
     * @Desc TODO(男生获取未读消息 \ 清空)
     * @author 彭石林
     * @parame [toUserId, type]
     * @Date 2021/12/29
     */
    Observable<BaseDataResponse<PriceConfigEntity.Current>> getMaleRefundMsg(Integer toUserId, Integer type);

    /**
     * 余额不足提示
     * @param toUserId
     * @param type
     * @return
     */
    Observable<BaseDataResponse> getTips(Integer toUserId, Integer type,String isShow);

    /**
     * IM通话中追踪
     *
     * @param userId
     * @return
     */
    Observable<BaseResponse> addIMCollect(
            Integer userId,
            Integer type);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.Integer>>>
     * @Desc TODO(真人 / 女神提醒)
     * @author 彭石林
     * @parame [toUserId]
     * @Date 2021/12/24
     */
    Observable<BaseDataResponse<Map<String, Integer>>> verifyGoddessTips(Integer toUserId);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Desc TODO(IM价格配置)
     * @author 彭石林
     * @parame [to_user_id]
     * @Date 2021/12/20
     */
    Observable<BaseDataResponse<PriceConfigEntity>> getPriceConfig(Integer to_user_id);

    /**
     * @Desc TODO(破冰文案列表)
     * @author 彭石林
     * @parame [page, perPage]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CallingInfoEntity.SayHiList>>
     * @Date 2021/12/18
     */
    @GET("/calling/listSayHis")
    Observable<BaseDataResponse<CallingInfoEntity.SayHiList>> getSayHiList(Integer page,Integer perPage);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.CallingInfoEntity>>
     * @Desc TODO(通话中)
     * @author 彭石林
     * @parame [roomId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    Observable<BaseDataResponse<CallingInfoEntity>> getCallingInfo(
            Integer roomId, //房间号
            Integer callingType, //通话类型：1=语音，2=视频
            String fromUserId, //拔打人用户ID
            String toUserId//接收人用户ID
    );

    /**
     * @return io.reactivex.Observable<com.dl.playfun.entity.CallingInviteInfo>
     * @Desc TODO(IM聊天页面 拔打中 / 接收中)
     * @author 彭石林
     * @parame [appId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, String fromUserId, String toUserId, int callingSource);

    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, String fromUserId, String toUserId, int callingSource,int videoCallPushLogId);

    /**
     * 视讯推送评价
     * @param videoCallPushLogId
     * @param feedback
     * @return
     */
    Observable<BaseDataResponse> videoFeedback(long videoCallPushLogId, int feedback);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.entity.CallingInviteInfo>
     * @Desc TODO(IM聊天页面 拔打中 / 接收中)
     * @author 彭石林
     * @parame [appId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(Integer callingType, Integer fromUserId, Integer toUserId, Integer currentUserId);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Desc TODO(发送礼物)
     * @author 彭石林
     * @parame [gift_id, to_user_id, amount]
     * @Date 2021/12/9
     */
    Observable<BaseResponse> sendUserGift(RequestBody requestBody);

    /**
     * @Desc TODO(礼物背包接口)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.GiftBagEntity>>
     * @Date 2021/12/7
     */
    Observable<BaseDataResponse<GiftBagEntity>> getBagGiftInfo();

    /**
     * 刷新im凭证
     * @return
     */
    Observable<BaseDataResponse<ImUserSigEntity>> flushSign();

    /**
     * @Desc TODO(用户收益页面)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Date 2021/12/6
     */
    Observable<BaseDataResponse<UserProfitPageEntity>> getUserProfitPageInfo(Long currentUserId, Integer page, Integer perPage);

    /**
     * @Desc TODO(用户账户余额)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.CoinWalletEntity>>
     * @Date 2021/12/6
     */
    Observable<BaseDataResponse<CoinWalletEntity>> getUserAccount();

    /**
     * @Desc TODO(用户账户余额)
     * @author KL
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse<com.dl.playfun.entity.GameCoinWalletEntity>>
     */
    Observable<BaseDataResponse<GameCoinWalletEntity>> getUserAccountPageInfo();

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.BubbleEntity>>
     * @Desc TODO(回复收益气泡)
     * @author 彭石林
     * @parame []
     * @Date 2021/12/2
     */
    Observable<BaseDataResponse<BubbleEntity>> getBubbleEntity();

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.AccostEntity>>
     * @Desc TODO(获取批量搭讪用户列表)
     * @author 彭石林
     * @parame []
     * @Date 2021/11/30
     */
    Observable<BaseDataResponse<AccostEntity>> getAccostList(Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Desc TODO(批量搭讪)
     * @author 彭石林
     * @parame [userIds]
     * @Date 2021/11/30
     */
    Observable<BaseResponse> putAccostList(List<Integer> userIds);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Desc TODO(单次搭讪接口)
     * @author 彭石林
     * @parame [userId]
     * @Date 2021/11/30
     */
    Observable<BaseResponse> putAccostFirst(Integer userId);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < com.dl.playfun.entity.ParkItemEntity>>
     * @Desc TODO(电台首页)
     * @author 彭石林
     * @parame [
     * sex, 性别 1男 0女
     * city_id, 城市ID
     * theme_id, 主题ID
     * is_online, 在线状态 0/1
     * is_collect, 追蹤的人 0不看 1看
     * type 类别 1按发布时间 2按活动时间]
     * @Date 2021/10/26
     */
    Observable<BaseDataResponse<BroadcastListEntity>> getBroadcastHome(
            Integer sex,
            Integer city_id,
            Integer game_id,
            Integer is_online,
            Integer is_collect,
            Integer type,
            Integer page
    );

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < com.dl.playfun.entity.MessageRuleEntity>>
     * @Desc TODO(获取聊天 （ 相册 、 评价发送规则 ）)
     * @author 彭石林
     * @parame []
     * @Date 2021/10/22
     */
    Observable<BaseDataResponse<List<MessageRuleEntity>>> getMessageRule();

    /**
     * 屏蔽關鍵字
     * @return
     */
    Observable<BaseDataResponse> getSensitiveWords();

    /**
     * @Desc TODO(IM聊天相册)
     * @author 彭石林
     * @parame [user_id]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Date 2021/10/22
     */
    Observable<BaseDataResponse<PhotoAlbumEntity>> getPhotoAlbum(Integer user_id);
    /**
     * @Desc TODO(删除用户录音)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Date 2021/10/25
     */
    Observable<BaseResponse> removeUserSound();
    /**
     * @Desc TODO(上传录音文案)
     * @author 彭石林
     * @parame [paht]
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Date 2021/10/21
     */
    Observable<BaseDataResponse> putUserSound(String paht,Integer sound_time);

    /**
     * @Desc TODO(查询录音文案)
     * @author 彭石林
     * @parame []
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse>
     * @Date 2021/10/21
     */
    Observable<BaseListDataResponse<SoundEntity>> getUserSound(Integer page);

    /**
     * 发布心情
     *
     * @param describe   约会内容
     * @param start_date 开始日期
     * @param images     图片
     * @param is_comment 禁止评论(0/1)
     * @param is_hide    对同性隐藏(0/1)
     * @param longitude  经度
     * @param latitude   纬度
     * @param video      视频
     * @return
     */
    Observable<BaseResponse> topicalCreateMood(
            String describe,
            String start_date,
            List<String> images,
            Integer is_comment,
            Integer is_hide,
            Double longitude,
            Double latitude,
            String video,
            Integer news_type
    );

    /**
     * 我的动态-全部
     *
     * @param page 页码
     * @return
     */
    Observable<BaseListDataResponse<BroadcastEntity>> broadcastAll(Integer page);

    /**
     * 商品列表
     *
     * @param type 类型 vip:升级会员 recharge:充值 points积分商品
     * @return
     */
    Observable<BaseDataResponse<List<GoodsEntity>>> pointsGoodList();

    /**
     * 一键搭讪推送状态提交
     * @return
     */
    Observable<BaseResponse> pushGreet(Integer type);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.String>>>
     * @Desc TODO(查询用户是否在黑名单里面)
     * @author 彭石林
     * @parame [userId]
     * @Date 2021/9/17
     */
    Observable<BaseDataResponse<Map<String, String>>> isBlacklist(String userId);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < com.dl.playfun.entity.TaskAdEntity>>
     * @Desc TODO(查询会员中心配置)
     * @author 彭石林
     * @parame []
     * @Date 2021/9/14
     */
    Observable<BaseListDataResponse<TaskAdEntity>> rechargeVipList();

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.String>>>
     * @Desc TODO(查询用户是否离线)
     * @author 彭石林
     * @parame []
     * @Date 2021/9/9
     */
    Observable<BaseDataResponse<Map<String, String>>> isOnlineUser(String userId);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < BrowseNumberEntity>>
     * @Desc TODO(新增谁看我及粉丝数)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/4
     */
    Observable<BaseDataResponse<BrowseNumberEntity>> newsBrowseNumber();

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < com.dl.playfun.entity.TraceEntity>>
     * @Desc TODO(谁看过我列表)
     * @author 彭石林
     * @parame [page]
     * @Date 2021/8/4
     */
    Observable<BaseListDataResponse<TraceEntity>> toBrowse(Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < com.dl.playfun.entity.TraceEntity>>
     * @Desc TODO(查询粉丝列表)
     * @author 彭石林
     * @parame [page]
     * @Date 2021/8/3
     */
    Observable<BaseListDataResponse<TraceEntity>> collectFans(Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseListDataResponse < TraceEntity>>
     * @Desc TODO(查询追踪列表)
     * @author 彭石林
     * @parame [page]
     * @Date 2021/8/3
     */
    Observable<BaseListDataResponse<TraceEntity>> collect(Integer page);

    /**
     * 主动上报用户当前定位信息。用于完善用户资料
     *
     * @param latitude
     * @param longitude
     * @return
     */
    Observable<BaseResponse> reportUserLocation(String latitude, String longitude);

    /**
     * 查询本地历史谷歌支付订单。提交给后台
     *
     * @param map
     * @return
     */
    Observable<BaseResponse> repoetLocalGoogleOrder(Map<String, Object> map);

    /**
     * 创建订单
     *
     * @param id      id 用户ID     当type为10时id传当前登陆用户id  当type为11时id传要解锁的用户ID
     * @param type    下单类型 1充值 2会员 3相册付费 4照片红包 5私聊 8发布动态 9发布节目 10一健打招呼钻石支付 11解锁社交账号
     * @param payType 1/余额支付 2/google支付 3/my_card支付 4/苹果支付
     * @return
     */
    Observable<BaseDataResponse<CreateOrderEntity>> createOrderUserDetail(Integer id, Integer type, Integer payType, Integer number);

    /**
     * 免费订阅7天会员
     *
     * @param pay_type
     * @return
     */
    Observable<BaseDataResponse<Map<String, String>>> freeSevenDay(Integer pay_type, Integer goods_type);


    /**
     * 用户标签
     *
     * @param to_user_id
     * @return
     */
    Observable<BaseDataResponse<TagEntity>> tag(String to_user_id);

    /**
     * 绑定用户关系
     *
     * @param code
     * @param type 类型 1.appsflyer 2.用户注册填写 3用户注册后填写
     * @return
     */
    Observable<BaseResponse> userInvite(String code, Integer type, String channel);

    /**
     * 绑定用户城市
     *
     * @param city_id
     * @return
     */
    Observable<BaseResponse> isBindCity(Integer city_id);

    /**
     * 注册用户（简化流程）
     *
     * @param nickname
     * @param avatar
     * @param birthday
     * @param sex
     * @return
     */
    Observable<BaseDataResponse<UserDataEntity>> regUser(String nickname, String avatar, String birthday, Integer sex, String channel);

    /**
     * 上报用户当前坐标
     *
     * @param latitude
     * @param longitude
     * @return
     */
    Observable<BaseResponse> coordinate(Double latitude, Double longitude, String county_name, String province_name);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.SwiftMessageEntity>>
     * @Author 彭石林
     * @Description 获取聊天信息快捷语
     * @Date 2021/4/30 16:07
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param []
     **/
    Observable<BaseDataResponse<SwiftMessageEntity>> getSwiftMessage(Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseResponse>
     * @Author 彭石林
     * @Description 绑定第三方账号
     * @Date 2021/4/29 15:33
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [id, type]
     **/
    Observable<BaseResponse> bindAccount(String id, String type);

    /**
     * @return io.reactivex.Observable<com.dl.playfun.data.source.http.response.BaseDataResponse < com.dl.playfun.entity.UserDataEntity>>
     * @Author 彭石林
     * @Description 第三方登录
     * @Date 2021/4/27 17:30
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [phone, code]
     **/
    Observable<BaseDataResponse<UserDataEntity>> v2Login(String phone, String code, String device_code, String region_code);

    /**
     * 真人人脸图片
     *
     * @param imgUrl 阿里云图片Url
     * @return
     */
    Observable<BaseDataResponse<Map<String, String>>> imagFaceUpload(
            @Query("img") String imgUrl
    );

    /**
     * @return io.reactivex.Observable<com.dl.playfun.entity.VersionEntity>
     * @Author 彭石林
     * @Description 后台效验检测更新
     * @Date 2021/3/31 19:32
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [client]
     **/
    Observable<BaseDataResponse<VersionEntity>> detectionVersion(@Query("client") String client);

    /**
     * GoogleMaps附近搜索
     *
     * @return
     */
    Observable<BaseDataResponse<GoogleNearPoiBean>> nearSearchPlace(RequestBody requestBody);

    /**
     * GoogleMaps文本搜索
     * @return
     */
    Observable<BaseDataResponse<GooglePoiBean>> textSearchPlace(RequestBody requestBody);

    /**
     * 短信验证码
     *
     * @param phone
     * @return
     */
    Observable<BaseResponse> verifyCodePost(RequestBody requestBody);

    /**
     * 手机号注册
     *
     * @param email
     * @param password
     * @param code
     * @return
     */
    Observable<BaseDataResponse<TokenEntity>> register(String email, String password, String code);

    /**
     * 同意用户协议
     *
     * @return
     */
    Observable<BaseResponse> acceptUseAgreement();

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    Observable<BaseDataResponse<TokenEntity>> login(String phone, String password);

    /**
     * 第三方登录
     *
     * @param id       唯一ID
     * @param type     登录类型 facebook/line
     * @return
     */
    Observable<BaseDataResponse<UserDataEntity>> authLoginPost(
            String id,
            String type
    );

    /**
     * 首页列表
     *
     * @param cityId     城市ID
     * @param type
     * @param isOnline
     * @param sex
     * @param searchName
     * @param longitude
     * @param latitude
     * @return
     */
    Observable<BaseListDataResponse<ParkItemEntity>> homeList(
            Integer cityId,
            Integer type,
            Integer isOnline,
            Integer sex,
            String searchName,
            Double longitude,
            Double latitude,
            Integer page
    );

    /**
     * 个人中心
     *
     * @return
     */
    Observable<BaseDataResponse<UserInfoEntity>> getUserInfo();

    /**
     * 游戏地区
     *
     * @return
     */
    Observable<BaseDataResponse<List<RadioTwoFilterItemEntity>>> getGameCity();



    /**
     * 个人资料
     */
    Observable<BaseDataResponse<UserDataEntity>> getUserData();

    /**
     * 给用户加备注
     *
     * @param userId   要加备注的用户ID
     * @param nickname 名称
     * @param remark   备注说明
     * @return
     */
    Observable<BaseResponse> userRemark(Integer userId, String nickname, String remark);

    /**
     * 获取用户备注信息
     *
     * @param userId
     * @return
     */
    Observable<BaseDataResponse<UserRemarkEntity>> getUserRemark(Integer userId);

    /**
     * 修改头像
     *
     * @param avatar
     * @return
     */
    Observable<BaseResponse> updateAvatar(
            String avatar
    );

    /**
     * 修改个人资料
     *
     * @param nickname           昵称
     * @param permanent_city_ids 常驻城市 格式如:6,7,8
     * @param birthday           生日
     * @param occupation         职业
     * @param program_ids        交友节目 格式如:1,2,3
     * @param hope_object_ids    期望对象 格式如3,4,5
     * @param weixin             微信号
     * @param insgram            Insgram
     * @param is_weixin_show     是否显示微信
     * @param height             身高cm
     * @param weight             体重kg
     * @param desc               个人介绍
     * @return
     */
    Observable<BaseResponse> updateUserData(
            String nickname,
            List<Integer> permanent_city_ids,
            String birthday,
            String occupation,
            List<Integer> program_ids,
            List<Integer> hope_object_ids,
            String facebook,
            String insgram,
            Integer accountType,
            Integer is_weixin_show,
            Integer height,
            Integer weight,
            String desc
    );

    /**
     * 个人主页
     *
     * @param id 用户id
     * @return
     */
    Observable<BaseDataResponse<UserDetailEntity>> userMain(Integer id, Double longitude, Double latitude);

    /**
     * 修改个人资料
     *
     * @param theme_id    节目主题ID
     * @param address     地址
     * @param hope_object 期望对象 例：1,2,3,4
     * @param start_date  开始日期
     * @param end_time    结束时间
     * @param images      图片
     * @param is_comment  禁止评论(0/1)
     * @param is_hide     对同性隐藏(0/1)
     * @return
     */
    Observable<BaseResponse> topicalCreate(
            Integer theme_id,
            String describe,
            String address,
            List<Integer> hope_object,
            String start_date,
            Integer end_time,
            List<String> images,
            Integer is_comment,
            Integer is_hide,
            String address_name,
            Integer city_id,
            Double longitude,
            Double latitude,
            String video
    );

    /**
     * 报名
     *
     * @param id  节目ID
     * @param img 照片
     * @return
     */
    Observable<BaseResponse> singUp(
            Integer id,
            String img
    );

    /**
     * 黑名单列表
     *
     * @return
     */
    Observable<BaseListDataResponse<BlackEntity>> getBlackList(Integer page);

    /**
     * 加入黑名单
     *
     * @param user_id 节目ID
     * @return
     */
    Observable<BaseResponse> addBlack(
            Integer user_id);

    /**
     * 删除黑名单
     *
     * @param id
     * @return
     */

    Observable<BaseResponse> deleteBlack(
            Integer id
    );

    /**
     * 删除节目
     *
     * @param id
     * @return
     */

    Observable<BaseResponse> deleteTopical(
            Integer id
    );

    /**
     * 喜欢列表
     *
     * @return
     */
    Observable<BaseListDataResponse<ParkItemEntity>> getCollectList(
            int page,
            Double latitude,
            Double longitude
    );

    /**
     * 加入喜欢
     *
     * @param userId
     * @return
     */
    Observable<BaseResponse> addCollect(
            Integer userId);

    /**
     * 删除喜欢
     *
     * @param userId
     * @return
     */

    Observable<BaseResponse> deleteCollect(
            Integer userId
    );

    /**
     * 发布动态
     *
     * @param content 内容
     * @param images  图片
     * @return
     */
    Observable<BaseResponse> newsCreate(
            String content,
            List<String> images,
            Integer is_comment,
            Integer is_hide
    );

    /**
     * 动态详情
     *
     * @param id
     * @return
     */
    Observable<BaseDataResponse<NewsEntity>> newsDetail(
            Integer id);

    /**
     * 删除动态
     *
     * @param id
     * @return
     */
    Observable<BaseResponse> deleteNews(
            Integer id
    );

    /**
     * 电台首页
     *
     * @param type      1按发布时间 2按活动时间
     * @param theme_id  主题ID
     * @param is_online 按在线状态 排序
     * @param city_id   城市 ID
     * @param sex       性别 1男 0女
     * @return
     */
    Observable<BaseListDataResponse<BroadcastEntity>> broadcast(
            Integer type,
            Integer theme_id,
            Integer is_online,
            Integer city_id,
            Integer sex,
            Integer page);


    /**
     * 动态列表
     *
     * @param user_id 不传为当前登陆用户信息
     * @return
     */
    Observable<BaseListDataResponse<NewsEntity>> getNewsList(
            Integer user_id, Integer page);

    /**
     * 节目列表
     *
     * @param userId 不传为当前登陆用户信息
     * @return
     */
    Observable<BaseListDataResponse<TopicalListEntity>> getTopicalList(
            Integer userId, Integer page);

    /**
     * 举报
     *
     * @param id        数据ID
     * @param type      类型 home 个人主页 broadcast电台
     * @param reason_id 原因ID
     * @param images    图片
     * @param desc      描述
     * @return
     */
    Observable<BaseResponse> report(
            Integer id,
            String type,
            String reason_id,
            List<String> images,
            String desc
    );

    /**
     * 节目评论
     *
     * @param id         节目 ID
     * @param content    评论内容
     * @param to_user_id 目标用户ID
     * @return
     */

    Observable<BaseResponse> topicalComment(
            Integer id,
            String content,
            Integer to_user_id
    );

    /**
     * 动态评论
     *
     * @param id         评论 ID
     * @param content    评论内容
     * @param to_user_id 目标用户ID
     * @return
     */
    Observable<BaseResponse> newsComment(
            Integer id,
            String content,
            Integer to_user_id
    );

    /**
     * 是否可以评价
     *
     * @param userId
     * @return
     */
    Observable<BaseDataResponse<StatusEntity>> evaluateStatus(
            Integer userId
    );

    /**
     * 写评价
     *
     * @param user_id 被评价人用户id
     * @param tag_id  评价标签ID
     * @param img     当tag_id值为 5/6时 为必填
     * @return
     */
    Observable<BaseResponse> evaluateCreate(
            Integer user_id,
            Integer tag_id,
            String img
    );

    /**
     * 我的真实评价
     *
     * @return
     */
    Observable<BaseDataResponse<List<EvaluateEntity>>> evaluate(Integer userId);

    /**
     * 动态点赞
     *
     * @param id 动态ID
     * @return
     */
    Observable<BaseResponse> newsGive(
            Integer id
    );

    /**
     * 是否可以私聊
     *
     * @param userId 跟谁私聊的用户ID
     * @return
     */
    Observable<BaseDataResponse<IsChatEntity>> isChat(
            Integer userId
    );

    /**
     * @param userId 跟谁私聊的用户ID
     * @param type   类型 1私聊 2相册
     * @return
     */
    Observable<BaseResponse> useVipChat(
            Integer userId,
            Integer type
    );

    /**
     * 阅后即焚接口
     *
     * @param image_id 图片ID
     * @return
     */
    Observable<BaseResponse> imgeReadLog(
            Integer image_id);

    /**
     * 修改密码
     *
     * @param original_password 原密码
     * @param new_password      新密码
     * @return
     */
    Observable<BaseResponse> password(
            String original_password,
            String new_password
    );

    /**
     * 获取所有配置参数
     *
     * @return
     */
    Observable<BaseDataResponse<AllConfigEntity>> getAllConfig();

    /**
     * 获取节目时间配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getProgramTimeConfig();

    /**
     * 获取身高配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getHeightConfig();

    /**
     * 获取体重配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getWeightConfig();

    /**
     * 获取举报原因配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getReportReasonConfig();

    /**
     * 获取评价标签配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getEvaluateConfig();

    /**
     * 获取期望对象配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getHopeObjectConfig();

    /**
     * 获取职业配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<OccupationConfigItemEntity>>> getOccupationConfig();

    /**
     * 获取主题配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getThemeConfig();

    /**
     * 获取城市配置
     *
     * @return
     */
    Observable<BaseDataResponse<List<ConfigItemEntity>>> getCityConfig();


    /**
     * 申请浏览相册
     *
     * @param user_id 用户ID
     * @param img     图片ID
     * @return
     */
    Observable<BaseResponse> userVerify(
            Integer user_id,
            String img
    );

    /**
     * 商品列表
     *
     * @return
     */
    Observable<BaseDataResponse<DiamondInfoEntity>> goods();

    /**
     * 会员套餐
     *
     * @return
     */
    Observable<BaseDataResponse<VipInfoEntity>> vipPackages();

    /**
     * 上传验证图上
     *
     * @param imgPath
     * @return
     */
    Observable<BaseDataResponse> saveVerifyFaceImage(
            String imgPath
    );

    /**
     * 提现
     *
     * @param money 提现金额
     * @return
     */
    Observable<BaseResponse> cashOut(
            float money);

    /**
     * 发送短信
     *
     * @param phone 手机号
     * @return
     */
    Observable<BaseResponse> sendCcode(
            String phone
    );

    /**
     * 相册详情
     *
     * @param userId
     * @param type   1圖片 2視頻
     * @return
     */
    Observable<BaseListDataResponse<AlbumPhotoEntity>> albumImage(
            Integer userId,
            Integer type
    );

    /**
     * 上传到相册
     *
     * @param fileType   1图片 2视频
     * @param src        图片地址
     * @param isBurn     是否閱後即焚
     * @param videoImage 視頻首
     * @return
     */
    Observable<BaseResponse> albumInsert(
            Integer fileType,
            String src,
            Integer isBurn,
            @Field("video_img") String videoImage
    );

    /**
     * 删除相册照片
     *
     * @param id
     * @return
     */
    Observable<BaseDataResponse<List<AlbumPhotoEntity>>> delAlbumImage(
            Integer id
    );

    /**
     * 设置为阅后即焚照片
     *
     * @param imgId 照片ID
     * @return
     */
    Observable<BaseResponse> setBurnAlbumImage(
            Integer imgId,
            Boolean state
    );

    /**
     * 设置为红包照片
     *
     * @param imgId 照片ID
     * @return
     */
    Observable<BaseResponse> setRedPackageAlbumImage(
            Integer imgId,
            Boolean state
    );

    /**
     * 設置紅包視頻
     *
     * @param videoId
     * @param state
     * @return
     */
    Observable<BaseResponse> setRedPackageAlbumVideo(
            Integer videoId,
            Boolean state
    );

    /**
     * 查看真人认证结果
     *
     * @param bizId
     * @return
     */
    Observable<BaseDataResponse<FaceVerifyResultEntity>> faceVerifyResult(String bizId);

    /**
     * 获取用户认证状态
     *
     * @return
     */
    Observable<BaseDataResponse<StatusEntity>> faceIsCertification();

    /**
     * 获取用户隐私设置
     *
     * @return
     */
    Observable<BaseDataResponse<PrivacyEntity>> getPrivacy(
    );

    /**
     * 获取用户隐私设置
     *
     * @param privacyEntity
     * @return
     */

    Observable<BaseResponse> setPrivacy(
            PrivacyEntity privacyEntity
    );

    /**
     * 修改手机号
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return
     */

    Observable<BaseResponse> updatePhone(
            String phone,
            int code,
            String password
    );

    /**
     * 申请女神
     *
     * @param images
     * @return
     */
    Observable<BaseResponse> applyGoddess(
            List<String> images
    );

    /**
     * 查看女神申请状态
     *
     * @return
     */
    Observable<BaseDataResponse<StatusEntity>> applyGoddessResult();

    /**
     * 重置密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return
     */
    Observable<BaseResponse> resetPassword(
            String phone,
            int code,
            String password
    );

    /**
     * 设置性别
     *
     * @param sex 0女 1男
     * @return
     */
    Observable<BaseResponse> setSex(
            int sex
    );

    /**
     * 现金账户
     *
     * @return
     */
    Observable<BaseDataResponse<CashWalletEntity>> cashWallet();

    /**
     * 币账户
     *
     * @return
     */
    Observable<BaseDataResponse<CoinWalletEntity>> coinWallet();

    /**
     * 设置提现账号
     *
     * @param realName 收款人真实姓名
     * @param account  收款账户
     * @return
     */
    Observable<BaseResponse> setWithdrawAccount(
            String realName,
            String account
    );

    /**
     * 设置我的相册权限
     *
     * @param type  1公开 2付费解锁 3查看需要我验证
     * @param money 相册金额 当type为2才传入
     * @return
     */
    Observable<BaseResponse> setAlbumPrivacy(
            Integer type,
            Integer money
    );

    /**
     * 开启/关闭评论
     *
     * @param id        电台ID
     * @param isComment 1关闭 0开启
     * @return
     */
    Observable<BaseResponse> setComment(
            Integer id,
            Integer isComment
    );


    /**
     * 查看申请消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<ApplyMessageEntity>> getMessageApply(Integer page);

    /**
     * 电台消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<BoradCastMessageEntity>> getMessageBoradcast(Integer page);

    /**
     * 评论消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<CommentMessageEntity>> getMessageComment(Integer page);

    /**
     * 评价消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<EvaluateMessageEntity>> getMessageEvaluate(Integer page);

    /**
     * 点赞消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<GiveMessageEntity>> getMessageGive(Integer page);

    /**
     * 报名消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<SignMessageEntity>> getMessageSign(Integer page);

    /**
     * 系统消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<SystemMessageEntity>> getMessageSystem(Integer page);

    /**
     * 收益消息列表
     *
     * @return
     */
    Observable<BaseListDataResponse<ProfitMessageEntity>> getMessageProfit(Integer page);

    /**
     * 评价上诉
     *
     * @param messageId
     * @param tagId
     * @return
     */
    Observable<BaseResponse> evaluateAppeal(
            Integer messageId,
            Integer tagId
    );


    /**
     * 删除消息
     *
     * @param type 类型 system面具消息 sign报名消息 give点赞消息 evaluate评价消息 comment评论消息 broadcast电台消息 apply查看申请消息 profit收益消息
     * @param id   消息ID
     * @return
     */
    Observable<BaseResponse> deleteMessage(
            String type,
            Integer id
    );

    /**
     * 消息汇总列表
     *
     * @return
     */
    Observable<BaseDataResponse<List<MessageGroupEntity>>> getMessageList();

    /**
     * 获取推送设置
     *
     * @return
     */
    Observable<BaseDataResponse<PushSettingEntity>> getPushSetting();

    /**
     * 保存推送设置
     *
     * @return
     */
    Observable<BaseResponse> savePushSetting(
            PushSettingEntity pushSettingEntity
    );

    /**
     * 节目点赞
     *
     * @param id 节目ID
     * @return
     */
    Observable<BaseResponse> TopicalGive(
            Integer id
    );

    /**
     * 结束报名
     *
     * @param id 节目ID
     * @return
     */
    Observable<BaseResponse> TopicalFinish(
            @Field("id") Integer id
    );

    /**
     * 阅后即焚恢复
     *
     * @return
     */
    Observable<BaseResponse> burnReset();

    /**
     * 创建订单
     *
     * @param id
     * @param type    下单类型 1充值 2会员 3相册付费 4照片红包 5私聊 8发布动态 9发布节目 10一健打招呼钻石支付 11解锁社交账号
     * @param payType 1/余额支付 2/google支付 3/my_card支付 4/苹果支付
     * @return
     */
    Observable<BaseDataResponse<CreateOrderEntity>> createOrder(
            Integer id,
            Integer type,
            Integer payType,
            Integer toUserId
    );

    /**
     * 币支付订单
     *
     * @param orderNumber
     * @return
     */
    Observable<BaseResponse> coinPayOrder(
            String orderNumber
    );

    /**
     * 动态点赞详情
     *
     * @param id 动态ID
     * @return
     */
    Observable<BaseListDataResponse<BaseUserBeanEntity>> getNewsGiveList(
            Integer id, Integer page
    );

    /**
     * 节目点赞详情
     *
     * @param id 节目ID
     * @return
     */

    Observable<BaseListDataResponse<BaseUserBeanEntity>> getTopicalGiveList(
            Integer id, Integer page
    );

    /**
     * 节目详情
     *
     * @param id 节目ID
     * @return
     */
    Observable<BaseDataResponse<TopicalListEntity>> topicalDetail(Integer id);

    /**
     * 是否可以发布节目
     *
     * @return
     */
    Observable<BaseResponse> checkTopical();

    /**
     * 举报节目报名人
     *
     * @param id 报名ID
     * @return
     */
    Observable<BaseResponse> signUpReport(
            Integer id);

    /**
     * 发送钻石红包
     *
     * @param userId
     * @param money
     * @return
     */
    Observable<BaseDataResponse<ChatRedPackageEntity>> sendCoinRedPackage(
            Integer userId,
            Integer money,
            String desc
    );

    /**
     * 红包详情
     *
     * @param id
     * @return
     */
    Observable<BaseDataResponse<ChatRedPackageEntity>> getCoinRedPackage(
            int id
    );

    /**
     * 领取红包
     *
     * @param id
     * @return
     */
    Observable<BaseResponse> receiveCoinRedPackage(
            int id
    );

    /**
     * 用户币收益记录
     *
     * @param page
     * @return
     */
    Observable<BaseListDataResponse<UserCoinItemEntity>> userCoinEarnings(
            int page
    );

    /**
     * 用户是否允许连麦
     *
     * @param userId
     * @return
     */
    Observable<BaseDataResponse<UserConnMicStatusEntity>> userIsConnMic(
            int userId
    );

    /**
     * 支付成功回调
     *
     * @param packageName
     * @param productId
     * @param token
     * @param type        1普通支付 2订阅支付
     * @return
     */
    Observable<BaseResponse> paySuccessNotify(
            String packageName,
            String orderNumber,
            List<String> productId,
            String token,
            int type,
            Integer event
    );

    /**
     * 更新设备推送ID
     *
     * @param deviceId
     * @return
     */
    Observable<BaseResponse> pushDeviceToken(
            String deviceId,
            String version_number
    );


    /**
     * 回复相册申请
     *
     * @param applyId
     * @param status
     * @return
     */
    Observable<BaseResponse> replyApplyAlubm(
            int applyId,
            boolean status
    );

    /**
     * 查看申请消息照片
     *
     * @return
     */
    Observable<BaseResponse> checkApplyAlbumPhoto(
            int applyId
    );

    /**
     * 电台&節目发布检测
     *
     * @param type 1动态 2节目
     * @return
     */
    Observable<BaseDataResponse<StatusEntity>> publishCheck(
            int type
    );


    /**
     * 获取未读消息数
     *
     * @return
     */
    Observable<BaseDataResponse<UnReadMessageNumEntity>> getUnreadMessageNum();

    /**
     * 获取 游戏币兑换jm币兑换框信息
     *
     * @return
     */
    Observable<BaseDataResponse<CoinExchangeBoxInfo>> getCoinExchangeBoxInfo();

    /**
     * 游戏币兑换jm币
     *
     * @return
     */
    Observable<BaseResponse> exchangeCoins(int id);

    /**
     * 游戏币充值
     *
     * @return
     */
    Observable<BaseDataResponse<List<GameCoinBuy>>> buyGameCoins();

}
