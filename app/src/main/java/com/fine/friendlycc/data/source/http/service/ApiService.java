package com.fine.friendlycc.data.source.http.service;

import com.fine.friendlycc.data.RetrofitHeadersConfig;
import com.fine.friendlycc.data.source.http.response.BaseDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseListDataResponse;
import com.fine.friendlycc.data.source.http.response.BaseResponse;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author goldze
 * @date 2017/6/15
 */

public interface ApiService {

    /*=====================================???????????????=====================================*/
    /**
    * @Desc TODO(????????????????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.CoinPusherBalanceDataEntity>>
    * @Date 2022/9/6
    */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/iscan/balance")
    Observable<BaseDataResponse<CoinPusherBalanceDataBean>> qryUserGameBalance();
    /**
    * @Desc TODO(??????????????????/????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<java.util.Map<java.lang.String,java.lang.Integer>>>
    * @Date 2022/9/17
    */
    @GET("api/chatResource/evaluation")
    Observable<BaseDataResponse<Map<String, Integer>>> mediaGalleryEvaluationQry(@Query("msgKey") String msgKey, @Query("toUserId") Integer toUserId);
    /**
    * @Desc TODO(????????????/????????????)
    * @author ?????????
    * @parame [
     * msgKey	string	????????????msgKey
     *     toUserId	int	???????????????id
     *     type	int	?????????1?????????2??????
     * ]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/9/17
    */
    @FormUrlEncoded
    @POST("api/chatResource/evaluation")
    Observable<BaseResponse> mediaGalleryEvaluationPut(
            @Field("msgKey")String msgKey,
            @Field("toUserId")Integer toUserId,
            @Field("type") Integer type
            );
    /**
    * @Desc TODO(????????????????????????)
    * @author ?????????
    * @parame [msgKey, toUserId]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/9/16
    */
    @GET("api/chatResource/readSnapshot")
    Observable<BaseResponse> mediaGallerySnapshotUnLock(@Query("msgKey") String msgKey, @Query("toUserId") Integer toUserId);

    /**
    * @Desc TODO(????????????????????????????????????)
    * @author ?????????
    * @parame [
     * msgKey    string	????????????msgKey
     * toUserId	int	???????????????id]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/9/15
    */
    @POST("api/chatResource/pay")
    @FormUrlEncoded
    Observable<BaseResponse> mediaGalleryPay(@Field("msgKey")String msgKey, @Field("toUserId") Integer toUserId);

    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<java.util.List<com.dl.playcc.entity.CoinPusherRoomHistoryEntity>>>
    * @Date 2022/8/26
    */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/iscan/history")
    Observable<BaseDataResponse<List<CoinPusherRoomHistoryBean>>> qryCoinPusherRoomHistory(@Query("roomId")Integer roomId);
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame [roomId]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.CoinPusherDataInfoEntity>>
    * @Date 2022/9/1
    */
    @POST("api/iscan/start")
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @FormUrlEncoded
    Observable<BaseDataResponse<CoinPusherDataInfoBean>> playingCoinPusherStart(@Field("roomId")Integer roomId);

    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame [roomId]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/24
    */
    @POST("api/iscan/end")
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @FormUrlEncoded
    Observable<BaseResponse> playingCoinPusherClose(@Field("roomId")Integer roomId);
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame [roomId]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/24
    */
    @Headers({RetrofitHeadersConfig.CoinPUsherConfig.API_TIMEOUT_HEADER,RetrofitHeadersConfig.PlayChat_API_URL})
    @POST("api/iscan/act")
    @FormUrlEncoded
    Observable<BaseResponse> playingCoinPusherAct(@Field("roomId")Integer roomId);
    /**
    * @Desc TODO(?????????-??????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/24
    */
    @Headers({RetrofitHeadersConfig.CoinPUsherConfig.API_TIMEOUT_HEADER,RetrofitHeadersConfig.PlayChat_API_URL})
    @FormUrlEncoded
    @POST("api/iscan/throwCoin")
    Observable<BaseDataResponse<CoinPusherBalanceDataBean>> playingCoinPusherThrowCoin(@Field("roomId")Integer roomId);
    /**
    * @Desc TODO(?????????-???????????????)
    * @author ?????????
    * @parame [
     * amount	???	int	?????????
     * type	???	int	???????????? 1?????? 2?????? 3?????????
     * ]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/24
    */
    @FormUrlEncoded
    @Headers({RetrofitHeadersConfig.CoinPUsherConfig.API_TIMEOUT_HEADER,RetrofitHeadersConfig.PlayChat_API_URL})
    @POST("api/iscan/goldCoin")
    Observable<BaseDataResponse<CoinPusherBalanceDataBean>> convertCoinPusherGoldsCoin(@Field("id")Integer id, @Field("type") Integer type);
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/24
    */
    @POST("api/iscan/diamonds")
    @Headers({RetrofitHeadersConfig.CoinPUsherConfig.API_TIMEOUT_HEADER,RetrofitHeadersConfig.PlayChat_API_URL})
    @FormUrlEncoded
    Observable<BaseDataResponse<CoinPusherBalanceDataBean>> convertCoinPusherDiamonds(@Field("id") Integer id);
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.CoinPusherRoomTagInfoEntity>>
    * @Date 2022/8/24
    */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/iscan/tag")
    Observable<BaseDataResponse<CoinPusherRoomTagInfoBean>> qryCoinPusherRoomTagList();
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.CoinPusherRoomInfoEntity>>
    * @Date 2022/8/24
    */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/iscan")
    Observable<BaseDataResponse<CoinPusherRoomInfoBean>> qryCoinPusherRoomList(@Query("tagId") Integer tagId);
    /**
    * @Desc TODO(?????????-????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.CoinPusherConverInfoEntity>>
    * @Date 2022/8/23
    */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/iscan/exchange")
    Observable<BaseDataResponse<CoinPusherConverInfoBean>> qryCoinPusherConverList();
    /*=====================================???????????????=====================================*/
    /**
    * @Desc TODO(??????????????????)
    * @author ?????????
    * @parame [requestBody]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/16
    */
    @Headers("Content-Type: application/json")
    @POST("calling/tim/friendAddFrequent")
    Observable<BaseResponse> friendAddFrequent(@Body RequestBody requestBody);
    /**
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [requestBody]
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Date 2022/8/16
     */
    @Headers("Content-Type: application/json")
    @POST("calling/tim/friendDeleteFrequent")
    Observable<BaseResponse> friendDeleteFrequent(@Body RequestBody requestBody);

    /**
     * ??????????????????
     * @return
     */
    @GET("api/getExclusiveAccost")
    Observable<BaseDataResponse<List<ExclusiveAccostInfoBean>>> getExclusiveAccost();

    /**
     * ????????????
     * @return
     */
    @GET("api/getRandName")
    Observable<BaseDataResponse> getRandName();

    /**
     * ????????????
     * @return
     */
    @GET("api/getDayReward")
    Observable<BaseDataResponse<DayRewardInfoBean>> getDayReward();

    /**
     * ????????????
     * @return
     */
    @GET("api/getRegisterReward")
    Observable<BaseDataResponse<DayRewardInfoBean>> getRegisterReward();

    /**
     * ??????????????????
     * @return
     */
    @GET("api/delExclusiveAccost")
    Observable<BaseDataResponse> delExclusiveAccost(@Query("type") int type);

    /**
     * ??????????????????
     * @param type ??????????????????
     * @param content ??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/setExclusiveAccost")
    Observable<BaseDataResponse> setExclusiveAccost(@Field("type")Integer type,
                                                    @Field("content")String content,
                                                    @Field("len")int len);


    /**
    * @Desc TODO(???????????????????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/8/2
    */
    @FormUrlEncoded
    @POST("api/user/CallCover")
    Observable<BaseResponse> photoCallCover(@Field("album_id")Integer albumId, @Field("type")Integer type);
    /**
     * @return
     * @Desc TODO(????????????????????????)
     * @author ?????????
     */
    @GET("api/v2/city")
    Observable<BaseDataResponse<CityAllBean>> getCityConfigAll();

    /**
     * ???????????????????????????
     *
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/checkNickname")
    Observable<BaseDataResponse<CheckNicknameBean>> checkNickname(
            @Field("nickname") String nickname
    );
    /*
     * @Desc TODO(????????????????????????????????????)
     * @author ?????????
     * @parame []
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse<com.dl.play.chat.entity.ChooseAreaEntity>>
     * @Date 2022/7/7
     */
    @GET("calling/region/getList")
    Observable<BaseDataResponse<ChooseAreaBean>> getChooseAreaList();
    /**
     * @Desc TODO(??????????????????  1????????? 2????????????)
     * @author ?????????
     * @parame []
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse<com.dl.playcc.entity.AdItemEntity>>
     * @Date 2022/7/25
     */
    @GET("friendly/commercial/list")
    Observable<BaseDataResponse<AdBannerBean>> getRadioAdBannerList(@Query("position") int position);
    /**
    * @Desc TODO(???????????????)
    * @author ?????????
    * @parame [position]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
    * @Date 2022/7/26
    */
    @GET("friendly/plazaRecommend")
    Observable<BaseDataResponse<AdUserBannerBean>> getUserAdList(@Query("position") Integer position);
    /**
     * @Desc TODO(??????????????????  1????????? 2????????????)
     * @author ?????????
     * @parame []
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse<com.dl.playcc.entity.AdItemEntity>>
     * @Date 2022/7/25
     */
    @GET("friendly/homeRecommend")
    Observable<BaseDataResponse<AdBannerBean>> getMainAdBannerList(@Query("position") int position);
    /**
     * ????????????????????????
     * @param to_user_id
     * @param note
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v2/user/note")
    Observable<BaseDataResponse> putNoteText(@Field("to_user_id") int to_user_id, @Field("note") String note);

    /**
     * ????????????????????????
     * @param to_user_id
     * @return
     */
    @GET("/api/v2/user/note")
    Observable<BaseDataResponse<NoteInfoBean>> getNoteText(@Query("to_user_id") int to_user_id);

    /***
     * ????????????????????????
     * @param channel ???????????? ???1??????  2ios
     * @return
     */
    @GET("/calling/mall/getMallWithdrawTipsInfo")
    Observable<BaseDataResponse<MallWithdrawTipsInfoBean>> getMallWithdrawTipsInfo(@Query("channel") Integer channel);

    /**
    * @Desc TODO(?????????api??????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.ApiConfigManagerEntity>>
    * @Date 2022/7/2
    */
    @Headers(RetrofitHeadersConfig.DEFAULT_API_INIT_URL)
    @GET("friendly/open")
    Observable<BaseDataResponse<ApiConfigManagerBean>> initApiConfig();

    /**
     * ????????????
     *
     * @return
     */
    @POST("api/cancellation")
    Observable<BaseResponse> cancellation();

    /*=====================================================????????????????????????=================================================*/
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(?????????????????????????????? ??? ??????ID)
     * @author ?????????
     * @parame [id]
     * @Date 2021/9/23
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v2/bonus/buy")
    @FormUrlEncoded
    Observable<BaseResponse> ExchangeIntegraBuy(@Field("id") Integer id);
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.ExchangeIntegraEntity>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/9/23
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/bonus/list")
    Observable<BaseDataResponse<ExchangeIntegraOuterBean>> getExchangeIntegraListData();
    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse>
     * @Desc TODO(?????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/9/4
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/task/ad")
    Observable<BaseListDataResponse<TaskAdBean>> taskAdList();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [permanent_city_ids, address_id]
     * @Date 2021/8/14
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @FormUrlEncoded
    @POST("api/v2/exchange/supply")
    Observable<BaseResponse> subSupply(@Field("exchange_ids[]") List<Integer> exchange_ids, @Field("address_id") Integer address_id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [id]
     * @Date 2021/8/16
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @DELETE("/api/v2/address/{id}")
    Observable<BaseResponse> removeAddress(@Path("id") Integer id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.AddressEntity>>
     * @Desc TODO(??????????????????????????????)
     * @author ?????????
     * @parame [id]
     * @Date 2021/8/13
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/address/view")
    Observable<BaseDataResponse<AddressBean>> getAddress(@Query("id") Integer id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.AddressEntity>>
     * @Desc TODO(??????????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/13
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/address")
    Observable<BaseListDataResponse<AddressBean>> getAddressList(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [contacts, city, are, address, phone, is_default]
     * @Date 2021/8/13
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v2/address")
    Observable<BaseResponse> createAddress(
            @Query("contacts") String contacts,
            @Query("city") String city,
            @Query("are") String are,
            @Query("address") String address,
            @Query("phone") String phone,
            @Query("is_default") Integer is_default);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [contacts, city, are, address, phone, is_default]
     * @Date 2021/8/13
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @PUT("api/v2/address")
    Observable<BaseResponse> updateAddress(
            @Query("id") Integer id,
            @Query("contacts") String contacts,
            @Query("city") String city,
            @Query("are") String are,
            @Query("address") String address,
            @Query("phone") String phone,
            @Query("is_default") Integer is_default);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseListDataResponse < com.dl.play.chat.entity.ExchangeEntity>>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [page]
     * @Date 2021/8/10
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/exchange")
    Observable<BaseListDataResponse<ExchangeBean>> qryExchange(@Query("page") Integer page, @Query("status") Integer status);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [goodsId]
     * @Date 2021/8/10
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v2/exchange")
    Observable<BaseResponse> exchange(@Query("goods_id") String goodsId);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < sBonusGoodsEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [page]
     * @Date 2021/8/10
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/bonusGoods")
    Observable<BaseListDataResponse<BonusGoodsBean>> getBonusGoods(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/9
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/bonus")
    Observable<BaseListDataResponse<GoldDetailBean>> getGoldList(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/9
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v2/bonus")
    Observable<BaseResponse> ToaskSubBonus(@Query("type") String key);

    /**
     * @param key
     * @Desc TODO(????????????)
     * @author liaosf
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v4/task/receive")
    Observable<BaseDataResponse<TaskRewardReceiveBean>> TaskRewardReceive(@Query("slug") String key);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.TaskConfigListEntity>>
     * @Desc TODO(?????????????????? ??? ????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/10
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v4/task/list")
    Observable<BaseDataResponse<List<TaskConfigItemBean>>> getTaskListConfig();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < TaskConfigBean>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/7
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v4/task")
    Observable<BaseDataResponse<TaskConfigBean>> getTaskConfig();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.EjectSignInEntity>>
     * @Desc TODO(???????????? ??? ????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/6
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @POST("api/v4/signIn")
    Observable<BaseDataResponse<EjectSignInBean>> reportEjectSignIn();

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.EjectEntity>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/6
     */
    @Headers(RetrofitHeadersConfig.PlayChat_API_URL)
    @GET("api/v2/signIn/eject")
    Observable<BaseDataResponse<EjectBean>> getEjectconfig();

    /*=====================================================????????????????????????=================================================*/

    /**
     * ???????????????
     *
     * @param id          ??????ID
     * @param type        ???????????? facebook/line
     * @param email       ??????
     * @param avatar      ??????
     * @param nickName    ??????
     * @param device_code ???????????????
     * @return
     */
    @Headers(RetrofitHeadersConfig.NO_TOKEN_CHECK)
    @FormUrlEncoded
    @POST("api/auth/login")
    Observable<BaseDataResponse<Map<String, String>>> authLoginPost(
            @Field("id") String id,
            @Field("type") String type,
            @Field("email") String email,
            @Field("avatar") String avatar,
            @Field("nickname") String nickName,
            @Field("device_code") String device_code,
            @Field("business_token") String business
    );

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.LevelApiEntity>>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [requestBody]
     * @Date 2022/6/22
     */
    @POST("calling/userLevel/adjustPrice")
    @Headers("Content-Type: application/json")
    Observable<BaseDataResponse<LevelApiBean>> adjustLevelPrice(@Body RequestBody requestBody);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.UserLevelPageInfoEntity>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2022/6/21
     */
    @GET("/calling/userLevel/getUserLevelPageInfo")
    Observable<BaseDataResponse<LevelPageInfoBean>> getUserLevelPageInfo();

    /**
    * @Desc TODO(???????????????????????????)
    * @author ?????????
    * @parame []
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/5/17
    */
    @FormUrlEncoded
    @POST("api/email/send")
    Observable<BaseResponse> sendEmailCode(@Field("email")String email);

    /**
    * @Desc TODO(??????????????????)
    * @author ?????????
    * @parame [email, code, pass, type]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse<com.dl.playcc.entity.UserDataEntity>>
    * @Date 2022/5/17
    */
    @FormUrlEncoded
    @POST("api/email/bind")
    Observable<BaseResponse> bindUserEmail(
            @Field("email")String email, //????????????
            @Field("code") String code, //?????????
            @Field("pass") String pass, //????????????
            @Field("type") Integer type //1????????????(??????/?????????) 2????????????(????????????) 3????????????(??????/?????????/????????????) 4????????????(?????????/????????????)
    );
    
    /**
    * @Desc TODO(????????????)
    * @author ?????????
    * @parame [email, code, type]
    * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
    * @Date 2022/5/17
    */
    @FormUrlEncoded
    @POST("api/email/login")
    Observable<BaseDataResponse<UserDataBean>> loginEmail(
            @Field("email")String email, //????????????
            @Field("code") String code, //?????????/??????
            @Field("type") Integer type //1??????????????? 2????????????
    );

    /**
     * ??????????????????
     * ??????????????????????????????????????????????????????1?????????????????????2???????????????????????????????????????????????????????????????0
     *
     * @param roomId
     * @return
     */
    @GET("/calling/getCallingStatus")
    Observable<BaseDataResponse<CallingStatusBean>> getCallingStatus(
            @Query("roomId") Integer roomId //?????????
    );

    /**
     * ????????????????????????????????????????????????
     *
     * @param roomId
     * @return
     */
    @GET("/calling/getRoomStatus")
    Observable<BaseDataResponse<CallingStatusBean>> getRoomStatus(
            @Query("roomId") Integer roomId //?????????
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.IMTransUserEntity>>
     * @Desc TODO(IM??????Id????????????id)
     * @author ?????????
     * @parame [IMUserId]
     * @Date 2022/4/2
     */
    @FormUrlEncoded
    @POST("api/im/transUser")
    Observable<BaseDataResponse<IMTransUserBean>> transUserIM(@Field("imId") String IMUserId);


    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [user_id]
     * @Date 2022/6/8
     */
    @GET("api/user/status")
    Observable<BaseResponse> userMessageCollation(@Query("user_id") Integer user_id);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.data.source.http.response.BaseDataResponse < com.dl.play.chat.entity.ChatDetailCoinEntity>>
     * @Desc TODO(???????????????????????????????????? ??? ???????????????)
     * @author ?????????
     * @parame [dismissRoom]
     * @Date 2022/3/21
     */
    @GET("calling/userAccount/getTotalCoins")
    Observable<BaseDataResponse<ChatDetailCoinBean>> getTotalCoins(@Query("dismissRoom") Integer dismissRoom);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame [packageName, orderNumber, productId, token, type, event, serverId, roleId]
     * @Date 2022/2/10
     */
    @FormUrlEncoded
    @POST("api/order/googleNotify")
    Observable<BaseResponse> GamePaySuccessNotify(
            @Field("package_name") String packageName,
            @Field("order_number") String orderNumber,
            @Field("product_id[]") List<String> productId,
            @Field("token") String token,
            @Field("type") int type,
            @Field("event") Integer event,
            @Field("serverId") String serverId,
            @Field("roleId") String roleId
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.GamePhotoAlbumEntity>>
     * @Desc TODO()
     * @author ?????????
     * @parame [serverId, roleId]
     * @Date 2022/1/21
     */
    @GET("/calling/albumImage/list")
    Observable<BaseDataResponse<GamePhotoAlbumBean>> getGamePhotoAlbumList(@Query("serverId") String serverId, @Query("roleId") String roleId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(?????????????????? 1?????? - 1??????)
     * @author ?????????
     * @parame [gameState]
     * @Date 2022/1/15
     */
    @FormUrlEncoded
    @POST("api/game/state")
    Observable<BaseResponse> setGameState(@Field("gameState") int gameState);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(serverId ??? String ??????ID ??? ??????45?????????
     *roleId ??? String ??????ID ??? ??????45?????????
     *roleName ??? String ???????????? ??? ??????100?????????
     *avatarUrl ??? String ????????????URL?????? ??? ??????1000?????????)
     * @author ?????????
     * @parame []
     * @Date 2022/1/14
     */
    @Headers("Content-Type: application/json")
    @POST("/calling/gameRole/commitRoleInfo")
    Observable<BaseResponse> commitRoleInfo(@Body RequestBody requestBody);

    /**
     * ???????????????
     *
     * @param id   ??????ID
     * @param type ???????????? facebook/line
     * @return
     */
    @FormUrlEncoded
    @POST("api/auth/login")
    Observable<BaseDataResponse<UserDataBean>> authLoginPost(
            @Field("id") String id,
            @Field("type") String type
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [sex]
     * @Date 2022/1/12
     */
    @FormUrlEncoded
    @POST("api/user/sex")
    Observable<BaseResponse> upUserSex(@Field("sex") Integer sex);

    /**
     * ????????????????????????
     *
     * @param id      id ??????ID     ???type???10???id?????????????????????id  ???type???11???id?????????????????????ID
     * @param type    ???????????? 1?????? 2?????? 3???????????? 4???????????? 5?????? 8???????????? 9???????????? 10??????????????????????????? 11??????????????????
     * @param payType 1/???????????? 2/google?????? 3/my_card?????? 4/????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/order")
    Observable<BaseDataResponse<CreateOrderBean>> createChatDetailOrder(
            @Field("id") Integer id,
            @Field("type") Integer type,
            @Field("pay_type") Integer payType,
            @Field("toUserId") Integer toUserId,
            @Field("channel") Integer channel
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.PriceConfigEntity.Current>>
     * @Desc TODO(???????????????????????? \ ??????)
     * @author ?????????
     * @parame [toUserId, type]
     * @Date 2021/12/29
     */
    @GET("api/refundMsg")
    Observable<BaseDataResponse<PriceConfigBean.Current>> getMaleRefundMsg(@Query("to_user_id") Integer toUserId, @Query("type") Integer type);


    /**
     * ??????????????????
     *
     * @param toUserId
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("api/tips")
    Observable<BaseDataResponse> getTips(@Field("to_user_id") Integer toUserId, @Field("type") Integer type, @Field("is_show") String isShow);

    /**
     * IM???????????????
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("api/collect")
    Observable<BaseResponse> addIMCollect(
            @Field("user_id") Integer userId,
            @Field("type") Integer type);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.Integer>>>
     * @Desc TODO(?????? / ????????????)
     * @author ?????????
     * @parame [toUserId]
     * @Date 2021/12/24
     */
    @GET("api/goddessTips")
    Observable<BaseDataResponse<Map<String, Integer>>> verifyGoddessTips(@Query("to_user_id") Integer toUserId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
     * @Desc TODO(IM????????????)
     * @author ?????????
     * @parame [to_user_id]
     * @Date 2021/12/20
     */
    @GET("api/priceConfig")
    Observable<BaseDataResponse<PriceConfigBean>> getPriceConfig(@Query("to_user_id") Integer to_user_id);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.CallingInfoEntity.SayHiList>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [page, perPage]
     * @Date 2021/12/18
     */
    @GET("/calling/listSayHis")
    Observable<BaseDataResponse<CallingInfoBean.SayHiList>> getSayHiList(@Query("page") Integer page, @Query("perPage") Integer perPage);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.CallingInfoEntity>>
     * @Desc TODO(?????????)
     * @author ?????????
     * @parame [roomId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    @GET("/calling/getCallingInfo/v2")
    Observable<BaseDataResponse<CallingInfoBean>> getCallingInfo(
            @Query("roomId") Integer roomId, //?????????
            @Query("callingType") Integer callingType, //???????????????1=?????????2=??????
            @Query("inviterImId") String inviterImId, //???????????????ID
            @Query("receiverImId") String toUserId//???????????????ID
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.entity.CallingInviteInfo>
     * @Desc TODO(IM???????????? ????????? / ?????????)
     * @author ?????????
     * @parame [appId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    @GET("/calling/getCallingInvitedInfo/v2")
    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(
            @Query("callingType") Integer callingType,
            @Query("inviterImId") String inviterImId,
            @Query("receiverImId") String receiverImId,
            @Query("callingSource") Integer callingSource
    );

    @GET("/calling/getCallingInvitedInfo/v2")
    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(
            @Query("callingType") Integer callingType,
            @Query("inviterImId") String inviterImId,
            @Query("receiverImId") String receiverImId,
            @Query("callingSource") Integer callingSource,
            @Query("callingSourceId") Integer videoCallPushLogId
    );

    /**
     * ??????????????????
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("/calling/videoCallPushLog/feedback")
    Observable<BaseDataResponse> videoFeedback(@Body RequestBody requestBody);

    /**
     * @return io.reactivex.Observable<com.dl.play.chat.entity.CallingInviteInfo>
     * @Desc TODO(IM???????????? ????????? / ?????????)
     * @author ?????????
     * @parame [appId, callingType, fromUserId, toUserId, currentUserId]
     * @Date 2021/12/13
     */
    @GET("/calling/getCallingInvitedInfo")
    Observable<BaseDataResponse<CallingInviteInfo>> callingInviteInfo(
            @Query("callingType") Integer callingType,
            @Query("fromUserId") Integer fromUserId,
            @Query("toUserId") Integer toUserId,
            @Query("currentUserId") Integer currentUserId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [gift_id, to_user_id, amount]
     * @Date 2021/12/9
     */
    @Headers("Content-Type: application/json")
    @POST("/calling/gift/sendGift")
    Observable<BaseResponse> sendUserGift(@Body RequestBody requestBody);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.GiftBagEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/12/7
     */
    @GET("api/gift")
    Observable<BaseDataResponse<GiftBagBean>> getBagGiftInfo();

    /**
     * ??????im??????
     * @return
     */
    @GET("api/flushSign")
    Observable<BaseDataResponse<ImUserSigBean>> flushSign();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/12/6
     */
    @GET("calling/userProfit/getUserProfitPageInfo")
    Observable<BaseDataResponse<UserProfitPageBean>> getUserProfitPageInfo(@Query("currentUserId") Long currentUserId, @Query("page") Integer page, @Query("perPage") Integer perPage);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.CoinWalletEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/12/6
     */
    @GET("api/account")
    Observable<BaseDataResponse<CoinWalletBean>> getUserAccount();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.GameCoinWalletEntity>>
     * @Desc TODO(??????????????????)
     * @author KL
     * @parame []
     */
    @GET("calling/userAccount/getUserAccountPageInfo")
    Observable<BaseDataResponse<GameCoinWalletBean>> getUserAccountPageInfo();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.BubbleEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/12/2
     */
    @GET("api/bubble")
    Observable<BaseDataResponse<BubbleBean>> getBubbleEntity();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.AccostEntity>>
     * @Desc TODO(??????????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/11/30
     */
    @GET("api/accost")
    Observable<BaseDataResponse<AccostBean>> getAccostList(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [userIds]
     * @Date 2021/11/30
     */
    @POST("api/accost")
    @FormUrlEncoded
    Observable<BaseResponse> putAccostList(@Field("user_ids[]") List<Integer> userIds);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [userId]
     * @Date 2021/11/30
     */
    @POST("api/accost/first")
    @FormUrlEncoded
    Observable<BaseResponse> putAccostFirst(@Field("user_id") Integer userId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < com.dl.playcc.entity.ParkItemEntity>>
     * @Desc TODO(????????????)
     * @author ?????????
     * @parame [
     * sex, ?????? 1??? 0???
     * city_id, ??????ID
     * ???????????? 1?????? 2?????????
     * is_online, ???????????? 0/1
     * is_collect, ???????????? 0?????? 1???
     * type ?????? 1??????????????? 2???????????????]
     * @Date 2021/10/26
     */
    @GET("friendly/v4/plaza/info")
    Observable<BaseDataResponse<BroadcastListBean>> getBroadcastHome(
            @Query("sex") Integer sex,
            @Query("city_id") Integer city_id,
            @Query("game_channel") Integer game_id,
            @Query("is_online") Integer is_online,
            @Query("is_collect") Integer is_collect,
            @Query("type") Integer type,
            @Query("page") Integer page
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < com.dl.playcc.entity.MessageRuleEntity>>
     * @Desc TODO(???????????? ??? ?????? ??? ?????????????????? ???)
     * @author ?????????
     * @parame []
     * @Date 2021/10/22
     */
    @GET("api/v2/user/rule")
    Observable<BaseDataResponse<List<MessageRuleBean>>> getMessageRule();

    /**
     * ???????????????
     *
     * @return
     */
    @GET("friendly/config/getBlackWords")
    Observable<BaseDataResponse> getSensitiveWords();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
     * @Desc TODO(IM????????????)
     * @author ?????????
     * @parame [user_id]
     * @Date 2021/10/22
     */
    @GET("api/user/im/image")
    Observable<BaseDataResponse<PhotoAlbumBean>> getPhotoAlbum(@Query("user_id") Integer user_id);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/10/25
     */
    @GET("api/v2/userSound/del")
    Observable<BaseResponse> removeUserSound();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [paht]
     * @Date 2021/10/21
     */
    @FormUrlEncoded
    @POST("api/v2/userSound")
    Observable<BaseDataResponse> putUserSound(@Field("sound") String paht, @Field("sound_time") Integer sound_time);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/10/21
     */
    @GET("api/v2/userSound")
    Observable<BaseListDataResponse<SoundBean>> getUserSound(@Query("page") Integer page);

    /**
     * ????????????
     *
     * @param theme_id     ????????????ID
     * @param describe     ????????????
     * @param address      ??????
     * @param hope_object  ???????????? ??????1,2,3,4
     * @param start_date   ????????????
     * @param end_time     ????????????
     * @param images       ??????
     * @param is_comment   ????????????(0/1)
     * @param is_hide      ???????????????(0/1)
     * @param address_name ????????????
     * @param city_id      ??????id
     * @param longitude    ??????
     * @param latitude     ??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/topical/create")
    Observable<BaseResponse> topicalCreate(
            @Field("theme_id") Integer theme_id,
            @Field("content") String describe,
            @Field("address") String address,
            @Field("hope_object[]") List<Integer> hope_object,
            @Field("start_date") String start_date,
            @Field("end_time") Integer end_time,
            @Field("images[]") List<String> images,
            @Field("is_comment") Integer is_comment,
            @Field("is_hide") Integer is_hide,
            @Field("address_name") String address_name,
            @Field("city_id") Integer city_id,
            @Field("longitude") Double longitude,
            @Field("latitude") Double latitude,
            @Field("video") String video

    );

    /**
     * ????????????
     *
     * @param describe   ????????????
     * @param start_date ????????????
     * @param images     ??????
     * @param is_comment ????????????(0/1)
     * @param is_hide    ???????????????(0/1)
     * @param longitude  ??????
     * @param latitude   ??????
     * @param video      ??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/news/create")
    Observable<BaseResponse> topicalCreateMood(
            @Field("content") String describe,
            @Field("start_date") String start_date,
            @Field("images[]") List<String> images,
            @Field("is_comment") Integer is_comment,
            @Field("is_hide") Integer is_hide,
            @Field("longitude") Double longitude,
            @Field("latitude") Double latitude,
            @Field("video") String video,
            @Field("news_type") Integer news_type
    );

    /**
     * ????????????-??????
     *
     * @param page ??????
     * @return
     */
    @GET("api/v2/broadcast/news")
    Observable<BaseListDataResponse<BroadcastBean>> broadcastAll(@Query("page") Integer page);

    /**
     * ????????????
     *
     * @param type ?????? vip:???????????? recharge:?????? points????????????
     * @return
     */
    @GET("api/goods")
    Observable<BaseDataResponse<List<GoodsBean>>> pointsGoodList(@Query("type") String type);

    /**
     * ?????????????????? type  1?????? 2?????? 3VIP
     *
     * @return
     */
    @POST("api/pushGreet")
    @FormUrlEncoded
    Observable<BaseResponse> pushGreet(@Field("type") Integer type);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.String>>>
     * @Desc TODO(????????????????????????????????????)
     * @author ?????????
     * @parame [userId]
     * @Date 2021/9/17
     */
    @GET("api/blacklist/isBlacklist")
    Observable<BaseDataResponse<Map<String, String>>> isBlacklist(@Query("to_user_id") String userId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < com.dl.playcc.entity.TaskAdEntity>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/9/14
     */
    @GET("api/v2/user/recharge")
    Observable<BaseListDataResponse<TaskAdBean>> rechargeVipList();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < java.util.Map < java.lang.String, java.lang.String>>>
     * @Desc TODO(????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/9/9
     */
    @GET("api/v2/user/isOnline")
    Observable<BaseDataResponse<Map<String, String>>> isOnlineUser(@Query("user_id") String userId);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < BrowseNumberBean>>
     * @Desc TODO(???????????????????????????)
     * @author ?????????
     * @parame []
     * @Date 2021/8/4
     */
    @GET("friendly/v4/user/peopleNumber")
    Observable<BaseDataResponse<BrowseNumberBean>> newsBrowseNumber();

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < com.dl.playcc.entity.TraceEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [page]
     * @Date 2021/8/4
     */
    @GET("api/collect/toBrowse")
    Observable<BaseListDataResponse<TraceBean>> toBrowse(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < com.dl.playcc.entity.TraceEntity>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [page]
     * @Date 2021/8/3
     */
    @GET("api/collect/fans")
    Observable<BaseListDataResponse<TraceBean>> collectFans(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseListDataResponse < TraceBean>>
     * @Desc TODO(??????????????????)
     * @author ?????????
     * @parame [page]
     * @Date 2021/8/3
     */
    @GET("api/collect")
    Observable<BaseListDataResponse<TraceBean>> collect(@Query("page") Integer page);

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param latitude
     * @param longitude
     * @return
     */
    @POST("api/user/coordinate")
    Observable<BaseResponse> reportUserLocation(@Query("latitude") String latitude, @Query("longitude") String longitude);

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param map
     * @return
     */
    @POST("api/v2/order/diff")
    Observable<BaseResponse> repoetLocalGoogleOrder(@Body Map<String, Object> map);


    /**
     * ????????????
     *
     * @param id      id ??????ID     ???type???10???id?????????????????????id  ???type???11???id?????????????????????ID
     * @param type    ???????????? 1?????? 2?????? 3???????????? 4???????????? 5?????? 8???????????? 9???????????? 10??????????????????????????? 11??????????????????
     * @param payType 1/???????????? 2/google?????? 3/my_card?????? 4/????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/order")
    Observable<BaseDataResponse<CreateOrderBean>> createOrderUserDetail(
            @Field("id") Integer id,
            @Field("type") Integer type,
            @Field("pay_type") Integer payType,
            @Field("number") Integer number
    );

    /**
     * ????????????7?????????
     *
     * @param pay_type
     * @return
     */
    @POST("api/v2/order")
    Observable<BaseDataResponse<Map<String, String>>> freeSevenDay(@Query("pay_type") Integer pay_type, @Query("goods_type") Integer goods_type);

    /**
     * ????????????
     *
     * @param to_user_id
     * @return
     */
    @GET("api/v2/user/tag")
    Observable<BaseDataResponse<TagBean>> tag(@Query("to_user_id") String to_user_id);

    /**
     * ??????????????????
     *
     * @param code
     * @param type ?????? 1.appsflyer 2.?????????????????? 3?????????????????????
     * @return
     */
    @POST("api/inviteCode")
    Observable<BaseResponse> userInvite(@Query("code") String code, @Query("type") Integer type, @Query("channel") String channel);

    /**
     * ??????????????????
     *
     * @param city_id
     * @return
     */
    @POST("friendly/v4/city")
    Observable<BaseResponse> isBindCity(@Query("city_id") Integer city_id);

    /**
     * ??????????????????????????????
     *
     * @param nickname
     * @param avatar
     * @param birthday
     * @param sex
     * @return
     */
    @POST("api/v2/user")
    Observable<BaseDataResponse<UserDataBean>> regUser(@Query("nickname") String nickname, @Query("avatar") String avatar, @Query("birthday") String birthday, @Query("sex") Integer sex, @Query("channel") String channel);

    /**
     * ????????????????????????
     *
     * @param latitude
     * @return
     */
    @POST("api/user/coordinate")
    Observable<BaseResponse> coordinate(@Query("latitude") Double latitude, @Query("longitude") Double longitud, @Query("county_name") String county_name, @Query("province_name") String province_name);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.SwiftMessageEntity>>
     * @Author ?????????
     * @Description ???????????????????????????
     * @Date 2021/4/30 16:07
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param []
     **/
    @GET("api/chat/text")
    Observable<BaseDataResponse<SwiftMessageBean>> getSwiftMessage(@Query("page") Integer page);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseResponse>
     * @Author ?????????
     * @Description ?????????????????????
     * @Date 2021/4/29 15:33
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [id, type]
     **/
    @POST("/api/auth/bindAccount")
    Observable<BaseResponse> bindAccount(@Query("id") String id, @Query("type") String type);

    /**
     * ??????????????????
     *
     * @param imgUrl ???????????????Url
     * @return
     */
    @POST("/api/aliyun/compareFaces")
    Observable<BaseDataResponse<Map<String, String>>> imagFaceUpload(
            @Query("img") String imgUrl
    );

    /**
     * @return io.reactivex.Observable<com.dl.playcc.data.source.http.response.BaseDataResponse < com.dl.playcc.entity.UserDataEntity>>
     * @Author ?????????
     * @Description ???????????????
     * @Date 2021/4/27 17:30
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [phone, code]
     **/
    @POST("api/v2/login")
    Observable<BaseDataResponse<UserDataBean>> v2Login(@Query("phone") String phone, @Query("code") String code, @Query("device_code") String device_code, @Query("region_code") String region_code);

    /**
     * @return io.reactivex.Observable<com.dl.playcc.entity.VersionEntity>
     * @Author ?????????
     * @Description ????????????????????????
     * @Date 2021/3/31 19:32
     * @Phone 16620350375
     * @email 15616314565@163.com
     * Param [client]
     **/
    @GET("friendly/version")
    Observable<BaseDataResponse<VersionBean>> detectionVersion(@Query("client") String client);

    /**
     * GoogleMaps????????????
     *
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("/calling/googleMapApi/nearSearchPlace")
    Observable<BaseDataResponse<GoogleNearPoiBean>> nearSearchPlace(@Body RequestBody requestBody);

    /**
     * GoogleMaps????????????
     *
     * @return
     */
    @POST("/calling/googleMapApi/textSearchPlace")
    Observable<BaseDataResponse<GooglePoiBean>> textSearchPlace(@Body RequestBody requestBody);

    /**
     * ?????????????????????
     *
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("calling/captcha/sendCaptcha")
    Observable<BaseResponse> verifyCodePost(
            @Body RequestBody requestBody
    );

    /**
     * ??????
     *
     * @param phone    ????????????
     * @param password ??????
     * @param code     ?????????
     * @return
     */
    @Headers(RetrofitHeadersConfig.NO_TOKEN_CHECK)
    @FormUrlEncoded
    @POST("api/register")
    Observable<BaseDataResponse<TokenBean>> registerPost(
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("code") String code
    );

    /**
     * ????????????????????????
     *
     * @param accept
     * @return
     */
    @FormUrlEncoded
    @POST("api/contract")
    Observable<BaseResponse> acceptUseAgreement(
            @Field("is_contract") Integer accept
    );

    /**
     * ??????
     *
     * @param phone    ?????????
     * @param password ??????
     * @return
     */
    @Headers(RetrofitHeadersConfig.NO_TOKEN_CHECK)
    @FormUrlEncoded
    @POST("api/login")
    Observable<BaseDataResponse<TokenBean>> loginPost(
            @Field("phone") String phone,
            @Field("password") String password
    );

    /**
     * ????????????
     *
     * @param cityId     ??????ID
     * @param type       1?????? 2???????????? 3??????
     * @param isOnline   ????????????
     * @param sex        ?????? 1??? 0???
     * @param searchName ??????/???????????????
     * @param longitude  ??????
     * @param latitude   ??????
     * @return
     */
    @GET("friendly/frist")
    Observable<BaseListDataResponse<ParkItemBean>> homeListGet(
            @Query("city_id") Integer cityId,
            @Query("type") Integer type,
            @Query("is_online") Integer isOnline,
            @Query("sex") Integer sex,
            @Query("search_name") String searchName,
            @Query("longitude") Double longitude,
            @Query("latitude") Double latitude,
            @Query("page") Integer page
    );

    /**
     * ????????????
     *
     * @return ??????????????? 0???????????? 1?????????
     */
    @GET("friendly/user/info")
    Observable<BaseDataResponse<UserInfoBean>> getUserInfo(@Query("invite_version") Integer invite_version);

    /**
     * ????????????
     *
     * @return
     */
    @GET("friendly/user/data")
    Observable<BaseDataResponse<UserDataBean>> getUserData(@Query("invite_version") Integer invite_version);

    /**
     * ??????????????????
     *
     * @param userId   ?????????????????????ID
     * @param nickname ??????
     * @param remark   ????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/remark")
    Observable<BaseResponse> userRemark(
            @Field("user_id") Integer userId,
            @Field("nickname") String nickname,
            @Field("remark") String remark
    );

    /**
     * ??????????????????
     *
     * @param userId
     * @return
     */
    @GET("api/user/remark")
    Observable<BaseDataResponse<UserRemarkBean>> getUserRemark(
            @Query("user_id") Integer userId
    );

    /**
     * ????????????
     *
     * @param avatar
     * @return
     */
    @FormUrlEncoded
    @POST("api/user")
    Observable<BaseResponse> updateAvatar(
            @Field("avatar") String avatar
    );

    /**
     * ??????????????????
     *
     * @param nickname           ??????
     * @param permanent_city_ids ???????????? ?????????:6,7,8
     * @param birthday           ??????
     * @param occupation_id      ??????
     * @param program_ids        ???????????? ?????????:1,2,3
     * @param hope_object_ids    ???????????? ?????????3,4,5
     * @param is_weixin_show     ??????????????????
     * @param height             ??????cm
     * @param weight             ??????kg
     * @param desc               ????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user")
    Observable<BaseResponse> updateUserData(
            @Field("nickname") String nickname,
            @Field("permanent_city_ids[]") List<Integer> permanent_city_ids,
            @Field("birthday") String birthday,
            @Field("occupation_id") String occupation_id,
            @Field("program_ids[]") List<Integer> program_ids,
            @Field("hope_object_ids[]") List<Integer> hope_object_ids,
            @Field("facebook") String facebook,
            @Field("insgram") String insgram,
            @Field("account_type") Integer accountType,
            @Field("is_weixin_show") Integer is_weixin_show,
            @Field("height") Integer height,
            @Field("weight") Integer weight,
            @Field("desc") String desc
    );

    /**
     * ????????????
     *
     * @param id ??????id
     * @return
     */
    @GET("api/user/{id}")
    Observable<BaseDataResponse<UserDetailBean>> userMain(
            @Path("id") int id,
            @Query("longitude") Double longitude,
            @Query("latitude") Double latitude
    );


    /**
     * ????????????
     *
     * @param id ??????ID
     * @return
     */
    @GET("api/topical/{id}")
    Observable<BaseDataResponse<TopicalListBean>> topicalDetail(@Path("id") Integer id);

    /**
     * ??????
     *
     * @param id  ??????ID
     * @param img ??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/topical/signUp")
    Observable<BaseResponse> singUp(
            @Field("id") Integer id,
            @Field("img") String img
    );

    /**
     * ???????????????
     *
     * @return
     */
    @GET("api/blacklist")
    Observable<BaseListDataResponse<BlackBean>> getBlackList(@Query("page") Integer page);

    /**
     * ???????????????
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("api/blacklist")
    Observable<BaseResponse> addBlack(
            @Field("user_id") Integer user_id);

    /**
     * ???????????????
     *
     * @param id
     * @return
     */
    @DELETE("api/blacklist/{id}")
    Observable<BaseResponse> deleteBlack(
            @Path("id") Integer id
    );

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @DELETE("api/topical/{id}")
    Observable<BaseResponse> deleteTopical(
            @Path("id") Integer id
    );

    /**
     * ????????????
     *
     * @return
     */
    @GET("api/collect")
    Observable<BaseListDataResponse<ParkItemBean>> getCollectList(
            @Query("page") Integer page,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude
    );

    /**
     * ????????????
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("api/collect")
    Observable<BaseResponse> addCollect(
            @Field("user_id") Integer userId);

    /**
     * ????????????
     *
     * @param userId
     * @return
     */
    @DELETE("api/collect/{user_id}")
    Observable<BaseResponse> deleteCollect(
            @Path("user_id") Integer userId
    );

    /**
     * ????????????
     *
     * @param content    ??????
     * @param images     ??????
     * @param is_comment ???????????????0/1???
     * @param is_hide    ??????????????????0/1
     * @return
     */
    @FormUrlEncoded
    @POST("api/news/create")
    Observable<BaseResponse> newsCreate(
            @Field("content") String content,
            @Field("images[]") List<String> images,
            @Field("is_comment") Integer is_comment,
            @Field("is_hide") Integer is_hide
    );

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @GET("api/news/{id}")
    Observable<BaseDataResponse<NewsBean>> newsDetail(@Path("id") Integer id);

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @DELETE("api/news/{id}")
    Observable<BaseResponse> deleteNews(
            @Path("id") Integer id
    );


    /**
     * ????????????
     *
     * @param type      1??????????????? 2???????????????
     * @param theme_id  ??????ID
     * @param is_online ??????????????? ??????
     * @param city_id   ?????? ID
     * @param sex       ?????? 1??? 0???
     * @return
     */
    @GET("api/v2/broadcast")
    Observable<BaseListDataResponse<BroadcastBean>> broadcast(
            @Query("type") Integer type,
            @Query("theme_id") Integer theme_id,
            @Query("is_online") Integer is_online,
            @Query("city_id") Integer city_id,
            @Query("sex") Integer sex,
            @Query("page") Integer page);

    /**
     * ????????????
     *
     * @param user_id ?????????????????????????????????
     * @return
     */
    @GET("api/news")
    Observable<BaseListDataResponse<NewsBean>> getNewsList(
            @Query("user_id") Integer user_id,
            @Query("page") Integer page);


    /**
     * ????????????
     *
     * @param user_id ?????????????????????????????????
     * @return
     */
    @GET("/api/topical")
    Observable<BaseListDataResponse<TopicalListBean>> getTopicalList(
            @Query("user_id") Integer user_id,
            @Query("page") Integer page);


    /**
     * ??????
     *
     * @param id        ??????ID
     * @param type      ?????? home ???????????? broadcast??????
     * @param reason_id ??????ID
     * @param images    ??????
     * @param desc      ??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/report/create")
    Observable<BaseResponse> report(
            @Field("id") Integer id,
            @Field("type") String type,
            @Field("reason_id") String reason_id,
            @Field("images[]") List<String> images,
            @Field("desc") String desc
    );

    /**
     * ????????????
     *
     * @param id         ?????? ID
     * @param content    ????????????
     * @param to_user_id ????????????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/topical/comment")
    Observable<BaseResponse> topicalComment(
            @Field("id") Integer id,
            @Field("content") String content,
            @Field("to_user_id") Integer to_user_id
    );

    /**
     * ????????????
     *
     * @param id         ?????? ID
     * @param content    ????????????
     * @param to_user_id ????????????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/news/comment")
    Observable<BaseResponse> newsComment(
            @Field("id") Integer id,
            @Field("content") String content,
            @Field("to_user_id") Integer to_user_id
    );


    /**
     * ??????????????????
     *
     * @param userId
     * @return
     */
    @GET("api/evaluate/{id}")
    Observable<BaseDataResponse<StatusBean>> evaluateStatus(
            @Path("id") Integer userId
    );

    /**
     * ?????????
     *
     * @param user_id ??????????????????id
     * @param tag_id  ????????????ID
     * @param img     ???tag_id?????? 5/6??? ?????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/evaluate/create")
    Observable<BaseResponse> evaluateCreate(
            @Field("user_id") Integer user_id,
            @Field("tag_id") Integer tag_id,
            @Field("img") String img
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/evaluate")
    Observable<BaseDataResponse<List<EvaluateBean>>> evaluate(
            @Query("user_id") Integer userId
    );

    /**
     * ????????????
     *
     * @param id ??????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/news/give")
    Observable<BaseResponse> newsGive(
            @Field("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param userId ?????????????????????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/isChat")
    Observable<BaseDataResponse<IsChatBean>> isChat(
            @Field("user_id") Integer userId
    );

    /**
     * @param userId ?????????????????????ID
     * @param type   ?????? 1?????? 2??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/useVip")
    Observable<BaseResponse> useVipChat(
            @Field("user_id") Integer userId,
            @Field("type") Integer type
    );

    /**
     * ??????????????????
     *
     * @param imageId ??????ID
     * @return
     */
    @GET("api/image-read-log")
    Observable<BaseResponse> imgeReadLog(
            @Query("image_id") Integer imageId);

    /**
     * ????????????
     *
     * @param original_password ?????????
     * @param new_password      ?????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/password")
    Observable<BaseResponse> password(
            @Field("original_password") String original_password,
            @Field("new_password") String new_password
    );

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("api/config")
    Observable<BaseDataResponse<AllConfigBean>> getAllConfig();

    /**
     * ??????????????????
     *
     * @param type ?????? program_time??????????????? height:?????? weight:?????? report_reason??????????????? evaluate:???????????? hope_object:???????????? city:??????
     * @return
     */
    @GET("api/config")
    Observable<BaseDataResponse<List<ConfigItemBean>>> getSystemConfig(
            @Query("type") String type
    );

    /**
     * ??????????????????
     *
     * @param type
     * @return
     */
    @GET("api/config")
//@GET("api/config")
    Observable<BaseDataResponse<List<OccupationConfigItemBean>>> getOccupationConfig(
            @Query("type") String type
    );

    /**
     * ??????????????????
     *
     * @param type
     * @return
     */
    @GET("api/config")
    Observable<BaseDataResponse<List<ConfigItemBean>>> getThemeConfig(
            @Query("type") String type
    );

    /**
     * ???????????????????????????
     *
     * @param type
     * @return
     */
    @GET("api/config")
    Observable<BaseDataResponse<List<RadioTwoFilterItemBean>>> getGameCity(
            @Query("type") String type
    );


    /**
     * ??????????????????
     *
     * @param user_id ??????ID
     * @param img     ??????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/verify")
    Observable<BaseResponse> userVerify(
            @Field("user_id") Integer user_id,
            @Field("img") String img
    );

    /**
     * ????????????
     *
     * @param type ?????? vip:???????????? recharge:??????
     * @return
     */
    @GET("api/goods")
    Observable<BaseDataResponse<DiamondInfoBean>> goods(
            @Query("type") String type);


    /**
     * ????????????
     *
     * @param type ?????? vip:???????????? recharge:??????
     * @return
     */
    @GET("api/goods")
    Observable<BaseDataResponse<VipInfoBean>> vipPackages(
            @Query("type") String type);


    /**
     * ??????????????????
     *
     * @param file ??????ID
     * @return
     */
    @Multipart
    @POST("api/upload")
    Observable<BaseDataResponse<String>> imagUpload(
            @Part MultipartBody.Part file
    );

    /**
     * ??????????????????
     *
     * @param imgPath
     * @return
     */
    @FormUrlEncoded
    @POST("api/aliyun/upload")
    Observable<BaseDataResponse> saveVerifyFaceImage(
            @Field("img") String imgPath
    );

    /**
     * ??????
     *
     * @param money ????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/cashOut")
    Observable<BaseResponse> cashOut(
            @Field("money") float money);

    /**
     * ????????????
     *
     * @param phone ?????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/verify-code")
    Observable<BaseResponse> sendCcode(
            @Field("phone") String phone
    );


    /**
     * ????????????
     *
     * @param userId
     * @param type   1?????? 2??????
     * @return
     */
    @GET("api/user/AlbumImage")
    Observable<BaseListDataResponse<AlbumPhotoBean>> albumImage(
            @Query("user_id") Integer userId,
            @Query("type") Integer type
    );

    /**
     * ??????????????????
     *
     * @param imgId ??????ID
     * @param type  1?????????????????? 2???????????? 3????????????
     * @param state ?????? 0??? 1???
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/AlbumImage")
    Observable<BaseResponse> setAlbumImage(
            @Field("id") Integer imgId,
            @Field("type") Integer type,
            @Field("boolean") Integer state
    );

    /**
     * ???????????????
     *
     * @param fileType   1?????? 2??????
     * @param src        ????????????
     * @param isBurn     ??????????????????
     * @param videoImage ?????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/AlbumImage/insert")
    Observable<BaseResponse> albumInsert(
            @Field("file_type") Integer fileType,
            @Field("src") String src,
            @Field("is_burn") Integer isBurn,
            @Field("video_img") String videoImage

    );

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @DELETE("api/user/AlbumImage/{id}")
    Observable<BaseDataResponse<List<AlbumPhotoBean>>> delAlbumImage(
            @Path("id") Integer id
    );


    /**
     * ????????????????????????
     *
     * @param bizId
     * @return
     */
    @GET("api/aliyun/verifyResult/{BizId}")
    Observable<BaseDataResponse<FaceVerifyResultBean>> faceVerifyResult(
            @Path("BizId") String bizId
    );

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("api/user/isCertification")
    Observable<BaseDataResponse<StatusBean>> faceIsCertification();

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("friendly/user/private")
    Observable<BaseDataResponse<PrivacyBean>> getPrivacy();

    /**
     * ????????????????????????
     *
     * @param isHome       ????????????????????????(0/1)
     * @param isDistance   ???????????????????????????(0/1)
     * @param isOnlineTIme ?????????????????????????????????(0/1)
     * @param isNearby     3KM????????????(0/1)
     * @param isConnection ?????????????????????????????????????????????(0/1)
     * @return
     */

    @FormUrlEncoded
    @POST("api/user/privacy")
    Observable<BaseResponse> setPrivacy(
            @Field("is_home") Integer isHome,
            @Field("is_distance") Integer isDistance,
            @Field("is_online_time") Integer isOnlineTIme,
            @Field("is_connection") Integer isConnection,
            @Field("is_nearby") Integer isNearby,
            @Field("allowAudio") Integer allowAudio,
            @Field("allowVideo") Integer allowVideo
    );

    /**
     * ???????????????
     *
     * @param phone    ?????????
     * @param code     ?????????
     * @param password ??????
     * @return
     */

    @FormUrlEncoded
    @POST("api/user/phone")
    Observable<BaseResponse> updatePhone(
            @Field("phone") String phone,
            @Field("code") int code,
            @Field("password") String password
    );

    /**
     * ????????????
     *
     * @param images
     * @return
     */
    @FormUrlEncoded
    @POST("api/apply_goddess")
    Observable<BaseResponse> applyGoddess(
            @Field("images[]") List<String> images
    );

    /**
     * ????????????????????????
     * status ?????? -1????????? 0???????????? 1???????????? 2?????????
     *
     * @return
     */
    @GET("api/apply_goddess/show")
    Observable<BaseDataResponse<StatusBean>> applyGoddessResult();

    /**
     * ????????????
     *
     * @param phone    ?????????
     * @param code     ?????????
     * @param password ??????
     * @return
     */

    @FormUrlEncoded
    @POST("api/resetPassword")
    Observable<BaseResponse> resetPassword(
            @Field("phone") String phone,
            @Field("code") int code,
            @Field("password") String password
    );

    /**
     * ????????????
     *
     * @param sex 0??? 1???
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/sex")
    Observable<BaseResponse> setSex(
            @Field("sex") int sex
    );

    /**
     * ????????????
     *
     * @return
     */
    @GET("api/userAccount/account")
    Observable<BaseDataResponse<CashWalletBean>> cashWallet();

    /**
     * ?????????
     *
     * @return
     */
    @GET("api/userCoin/account")
    Observable<BaseDataResponse<CoinWalletBean>> coinWallet();

    /**
     * ??????????????????
     *
     * @param realName ?????????????????????
     * @param account  ????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/account")
    Observable<BaseResponse> setWithdrawAccount(
            @Field("realname") String realName,
            @Field("account_number") String account
    );

    /**
     * ????????????????????????
     *
     * @param type  1?????? 2???????????? 3?????????????????????
     * @param money ???????????? ???type???2?????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/album")
    Observable<BaseResponse> setAlbumPrivacy(
            @Field("type") Integer type,
            @Field("money") Integer money
    );

    /**
     * ??????/????????????
     *
     * @param id        ??????ID
     * @param isComment 1?????? 0??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/broadcast/isComment")
    Observable<BaseResponse> setComment(
            @Field("id") Integer id,
            @Field("isComment") Integer isComment
    );


    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("api/message/apply")
    Observable<BaseListDataResponse<ApplyMessageBean>> getMessageApply(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/broadcast")
    Observable<BaseListDataResponse<BoradCastMessageBean>> getMessageBoradcast(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/comment")
    Observable<BaseListDataResponse<CommentMessageBean>> getMessageComment(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/evaluate")
    Observable<BaseListDataResponse<EvaluateMessageBean>> getMessageEvaluate(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/give")
    Observable<BaseListDataResponse<GiveMessageBean>> getMessageGive(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/sign")
    Observable<BaseListDataResponse<SignMessageBean>> getMessageSign(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/system")
    Observable<BaseListDataResponse<SystemMessageBean>> getMessageSystem(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/message/profit")
    Observable<BaseListDataResponse<ProfitMessageBean>> getMessageProfit(
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("friendly/chat")
    Observable<BaseDataResponse<List<MessageGroupBean>>> getMessageList();


    /**
     * ????????????
     *
     * @param messageId
     * @param tagId
     * @return
     */
    @FormUrlEncoded
    @POST("api/message/evaluate/appeal")
    Observable<BaseResponse> evaluateAppeal(
            @Field("id") Integer messageId,
            @Field("tag_id") Integer tagId
    );

    /**
     * ????????????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/apply/{id}")
    Observable<BaseResponse> deleteMessageApply(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/broadcast/{id}")
    Observable<BaseResponse> deleteMessageBroadcast(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/comment/{id}")
    Observable<BaseResponse> deleteMessageComment(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/evaluate/{id}")
    Observable<BaseResponse> deleteMessageEvaluate(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/give/{id}")
    Observable<BaseResponse> deleteMessageGive(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/sign/{id}")
    Observable<BaseResponse> deleteMessageSign(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/system/{id}")
    Observable<BaseResponse> deleteMessageSystem(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @DELETE("api/message/profit/{id}")
    Observable<BaseResponse> deleteMessageProfit(
            @Path("id") Integer id
    );


    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/user/push")
    Observable<BaseDataResponse<PushSettingBean>> getPushSetting();

    /**
     * ??????????????????
     *
     * @param isChat       ??????????????????(0/1)
     * @param isSign       ??????????????????(0/1)
     * @param isGive       ???????????????(0/1)
     * @param isComment    ???????????????(0/1)
     * @param isBroadcast  ???????????????(0/1)
     * @param isApply      ?????????????????????????????????(0/1)
     * @param isInvitation ?????????????????????(0/1)
     * @param isSound      ??????(0/1)
     * @param isShake      ??????(0/1)
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/push")
    Observable<BaseResponse> savePushSetting(
            @Field("is_chat") Integer isChat,
            @Field("is_sign") Integer isSign,
            @Field("is_give") Integer isGive,
            @Field("is_comment") Integer isComment,
            @Field("is_broadcast") Integer isBroadcast,
            @Field("is_apply") Integer isApply,
            @Field("is_invitation") Integer isInvitation,
            @Field("is_sound") Integer isSound,
            @Field("is_shake") Integer isShake
    );

    /**
     * ????????????
     *
     * @param id ??????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/topical/give")
    Observable<BaseResponse> TopicalGive(
            @Field("id") Integer id
    );

    /**
     * ????????????
     *
     * @param id ??????ID
     * @return
     */
    @GET("api/topical/finish/{id}")
    Observable<BaseResponse> TopicalFinish(
            @Path("id") Integer id
    );

    /**
     * ??????????????????
     *
     * @return
     */
    @GET("api/user/burnReset")
    Observable<BaseResponse> burnReset();

    /**
     * ????????????
     *
     * @param id      id ??????ID     ???type???10???id?????????????????????id  ???type???11???id?????????????????????ID
     * @param type    ???????????? 1?????? 2?????? 3???????????? 4???????????? 5?????? 8???????????? 9???????????? 10??????????????????????????? 11??????????????????
     * @param payType 1/???????????? 2/google?????? 3/my_card?????? 4/????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/order")
    Observable<BaseDataResponse<CreateOrderBean>> createOrder(
            @Field("id") Integer id,
            @Field("type") Integer type,
            @Field("pay_type") Integer payType,
            @Field("toUserId") Integer toUserId
    );

    /**
     * ???????????????
     *
     * @param orderNumber
     * @return
     */
    @FormUrlEncoded
    @POST("api/order/balancePay")
    Observable<BaseResponse> coinPayOrder(
            @Field("order_number") String orderNumber
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @GET("api/news/give/{id}")
    Observable<BaseListDataResponse<BaseUserBeanBean>> getNewsGiveList(
            @Path("id") Integer id,
            @Query("page") Integer page
    );

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @return
     */
    @GET("api/topical/give/{id}")
    Observable<BaseListDataResponse<BaseUserBeanBean>> getTopicalGiveList(
            @Path("id") Integer id,
            @Query("page") Integer page
    );

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("api/topical/check")
    Observable<BaseResponse> checkTopical();

    /**
     * ?????????????????????
     *
     * @param id ??????ID
     * @return
     */
    @FormUrlEncoded
    @POST("api/topical/signUpReport")
    Observable<BaseResponse> signUpReport(
            @Field("id") Integer id);

    /**
     * ??????????????????
     *
     * @param userId
     * @param money
     * @return
     */
    @FormUrlEncoded
    @POST("api/redPackage/send")
    Observable<BaseDataResponse<ChatRedPackageBean>> sendCoinRedPackage(
            @Field("user_id") Integer userId,
            @Field("money") Integer money,
            @Field("desc") String desc
    );

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @GET("api/redPackage/{id}")
    Observable<BaseDataResponse<ChatRedPackageBean>> getCoinRedPackage(
            @Path("id") int id
    );

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @GET("api/redPackage/receive")
    Observable<BaseResponse> receiveCoinRedPackage(
            @Query("id") int id
    );

    /**
     * ?????????????????????
     *
     * @param page
     * @return
     */
    @GET("api/userCoin")
    Observable<BaseListDataResponse<UserCoinItemBean>> userCoinEarnings(
            @Query("page") int page
    );

    /**
     * ????????????????????????
     *
     * @param userId
     * @return
     */
    @GET("api/user/isConnection")
    Observable<BaseDataResponse<UserConnMicStatusBean>> userIsConnMic(
            @Query("user_id") int userId
    );

    /**
     * ??????????????????
     *
     * @param packageName
     * @param orderNumber
     * @param productId
     * @param token
     * @param type        1???????????? 2????????????
     * @param event       ????????????????????????????????????????????????????????????????????????
     * @return
     */
    @FormUrlEncoded
    @POST("api/order/googleNotify")
    Observable<BaseResponse> paySuccessNotify(
            @Field("package_name") String packageName,
            @Field("order_number") String orderNumber,
            @Field("product_id[]") List<String> productId,
            @Field("token") String token,
            @Field("type") int type,
            @Field("event") Integer event
    );

    /**
     * ??????????????????ID
     *
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("api/device")
    Observable<BaseResponse> pushDeviceToken(
            @Field("device_id") String deviceId,
            @Field("version_number") String version_number
    );


    /**
     * ??????????????????
     *
     * @param applyId
     * @param status
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/verify-status")
    Observable<BaseResponse> replyApplyAlubm(
            @Field("apply_id") int applyId,
            @Field("status") int status
    );

    /**
     * ????????????????????????
     *
     * @return
     */
    @GET("api/user/verify")
    Observable<BaseResponse> checkApplyAlbumPhoto(
            @Query("apply_id") int applyId
    );

    /**
     * ??????&??????????????????
     *
     * @param type 1?????? 2??????
     * @return
     */
    @FormUrlEncoded
    @POST("api/news/publishCheck")
    Observable<BaseDataResponse<StatusBean>> publishCheck(
            @Field("type") int type
    );

    @GET("api/message/unread")
    Observable<BaseDataResponse<UnReadMessageNumBean>> getUnreadMessageNum();


    /**
     * ???????????????jm??????????????????
     *
     * @return
     */
    @GET("calling/userAccount/getCoinExchangeBoxInfo")
    Observable<BaseDataResponse<CoinExchangeBoxInfo>> getCoinExchangeBoxInfo();

    /**
     * ???????????????jm??????????????????
     *
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("calling/userAccount/exchangeCoins")
    Observable<BaseResponse> exchangeCoins(@Body RequestBody requestBody);

    /**
     * ?????????????????????
     *
     * @param type ?????? vip:???????????? recharge:??????
     * @return
     */
    @GET("api/goods")
    Observable<BaseDataResponse<List<GameCoinBuy>>> buyGameCoins(
            @Query("type") String type);
}