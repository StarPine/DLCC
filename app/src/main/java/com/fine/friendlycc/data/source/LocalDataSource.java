package com.fine.friendlycc.data.source;

import com.fine.friendlycc.entity.ApiConfigManagerEntity;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.CrystalDetailsConfigEntity;
import com.fine.friendlycc.entity.EvaluateObjEntity;
import com.fine.friendlycc.entity.GameConfigEntity;
import com.fine.friendlycc.entity.LocalGooglePayCache;
import com.fine.friendlycc.entity.OccupationConfigItemEntity;
import com.fine.friendlycc.entity.SystemConfigEntity;
import com.fine.friendlycc.entity.SystemConfigTaskEntity;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.entity.UserDataEntity;

import java.util.List;
import java.util.Map;

/**
 * @author wulei
 * @date 2019/3/26
 */
public interface LocalDataSource {
    /**
     * 保存城市配置
     *
     * @param configs
     */
    void saveCityConfigAll(List<ConfigItemEntity> configs);
    /**
     * 获取所有配置
     *
     * @return
     */
    List<ConfigItemEntity> readCityConfigAll();
    //保存api配置
    void saveApiConfigManager(ApiConfigManagerEntity apiConfigManager);
    //读取api配置
    ApiConfigManagerEntity readApiConfigManagerEntity();

    //保存键值对
    void putKeyValue(String key, String value);

    String readKeyValue(String key);

    //收益开关配置
    Integer readSwitches(String key);

    void putSwitches(String key, Integer value);

    //真人认证
    Boolean readVerifyGoddessTipsUser(String key);

    void putVerifyGoddessTipsUser(String key, String value);

    /**
     * 保存是否第一次进入
     *
     * @param
     */
    void saveVersion(String code);

    /**
     * 读取是否第一次进入
     *
     * @return
     */
    Boolean readVersion();

    /**
     * 清除缓存在本地的渠道标识
     */
    void clearChannelAF();

    /**
     * 读取缓存在本地的邀请码
     *
     * @return
     */
    String readChannelAF();

    //私讯推送状态
    Boolean readChatPushStatus();

    void saveChatPushStatus(int value);

    /**
     * 保存邀请码到本地
     */
    void saveChannelAF(String channel);

    /**
     * @return com.dl.playfun.entity.SystemConfigTaskEntity
     * @Desc TODO(读取本地任务中心配置)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/26
     */
    SystemConfigTaskEntity readSystemConfigTask();

    /**
     * @return void
     * @Desc TODO(保存任务中心配置到本地)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/26
     */
    void saveSystemConfigTask(SystemConfigTaskEntity entity);

    /**
     * 读取上次推送消息
     *
     * @return
     */
    Map<String, String> redMessageTagUser();

    /**
     * 保存本地推送消息
     */
    void saveMessageTagUser(Map map);

    /**
     * 清除缓存在本地的邀请码
     */
    void clearOneLinkCode();

    /**
     * 读取缓存在本地的邀请码
     *
     * @return
     */
    Map<String, String> readOneLinkCode();

    /**
     * 保存邀请码到本地
     *
     * @param linkCode
     */
    void saveOneLinkCode(String linkCode);

    /**
     * 清楚订单缓存
     */
    void clearGooglePayCache();

    LocalGooglePayCache readGooglePlay();

    /**
     * 保存谷歌本次支付
     */
    void saveGooglePlay(LocalGooglePayCache localGooglePayCache);


    /**
     * 保存登录信息
     *
     * @param tokenEntity
     */
    void saveLoginInfo(TokenEntity tokenEntity);

    /**
     * 读取登录信息
     *
     * @return
     */
    TokenEntity readLoginInfo();

    /**
     * 清除登录信息
     */
    void logout();

    /**
     * 保存用户信息
     *
     * @param userDataEntity
     */
    void saveUserData(UserDataEntity userDataEntity);

    /**
     * 读取用户信息
     *
     * @return
     */
    UserDataEntity readUserData();

    /**
     * 保存是否已认证
     *
     * @param isVerifyFace
     */
    void saveIsVerifyFace(Boolean isVerifyFace);

    /**
     * 读取是否已认证
     *
     * @return
     */
    Boolean readIsVerifyFace();

    /**
     * 保存是否新用户
     *
     * @param isVerifyFace
     */
    void saveIsNewUser(Boolean isVerifyFace);

    /**
     * 读取是否新用户
     *
     * @return
     */
    Boolean readIsNewUser();

    /**
     * 保存是否需要刷脸认证
     *
     * @param needVerifyFace
     */
    void saveNeedVerifyFace(boolean needVerifyFace);

    /**
     * 读取是否需要刷脸认证
     *
     * @return
     */
    boolean readNeedVerifyFace();

    /**
     * 保存系统配置
     *
     * @param config
     */
    void saveSystemConfig(SystemConfigEntity config);

    /**
     * 读取系统配置
     *
     * @return
     */
    SystemConfigEntity readSystemConfig();

     /**
     * 游戏图标配置
     *
     * @param configs
     */
    void saveGameConfig(List<GameConfigEntity> configs);

    /**
     * 读取游戏图标
     * @return
     */
    List<GameConfigEntity> readGameConfig();

    /**
     * 保存水晶兌換规则显示标记
     * @param configs
     */
    void saveCrystalDetailsConfig(CrystalDetailsConfigEntity configs);

    /**
     * 读取水晶兌換规则显示标记
     * @return
     */
    CrystalDetailsConfigEntity readCrystalDetailsConfig();

    /**
     * 保存身高配置
     *
     * @param configs
     */
    void saveHeightConfig(List<ConfigItemEntity> configs);

    /**
     * 获取身高配置
     *
     * @return
     */
    List<ConfigItemEntity> readHeightConfig();

    /**
     * 保存体重配置
     *
     * @param configs
     */
    void saveWeightConfig(List<ConfigItemEntity> configs);

    /**
     * 获取体重配置
     *
     * @return
     */
    List<ConfigItemEntity> readWeightConfig();

    /**
     * 保存屏蔽關鍵字
     *
     * @param configs
     */
    void saveSensitiveWords(List<String> configs);

    /**
     * 获取屏蔽關鍵字
     *
     * @return
     */
    List<String> readSensitiveWords();

    /**
     * 保存举报原因配置
     *
     * @param configs
     */
    void saveReportReasonConfig(List<ConfigItemEntity> configs);

    /**
     * 获取举报原因配置
     *
     * @return
     */
    List<ConfigItemEntity> readReportReasonConfig();

    /**
     * 保存女性评价标签配置
     *
     * @param configs
     */
    void saveFemaleEvaluateConfig(List<EvaluateObjEntity> configs);

    /**
     * 保存男性评价标签配置
     *
     * @param configs
     */
    void saveMaleEvaluateConfig(List<EvaluateObjEntity> configs);

    /**
     * 读取女士评价标签配置
     *
     * @return
     */
    List<EvaluateObjEntity> readFemaleEvaluateConfig();

    /**
     * 读取男士评价标签
     *
     * @return
     */
    List<EvaluateObjEntity> readMaleEvaluateConfig();

    /**
     * 读取评价标签
     *
     * @return
     */
    List<EvaluateObjEntity> readEvaluateConfig();

    /**
     * 保存期望对象配置
     *
     * @param configs
     */
    void saveHopeObjectConfig(List<ConfigItemEntity> configs);

    /**
     * 获取期望对象配置
     *
     * @return
     */
    List<ConfigItemEntity> readHopeObjectConfig();

    /**
     * 保存职业配置
     *
     * @param configs
     */
    void saveOccupationConfig(List<OccupationConfigItemEntity> configs);

    /**
     * 获取职业配置
     *
     * @return
     */
    List<OccupationConfigItemEntity> readOccupationConfig();

    /**
     * 保存城市配置
     *
     * @param configs
     */
    void saveCityConfig(List<ConfigItemEntity> configs);

    /**
     * 获取城市配置
     *
     * @return
     */
    List<ConfigItemEntity> readCityConfig();

    /**
     * 保存手势密码
     *
     * @param password
     */
    void savePassword(String password);

    /**
     * 获取手势密码
     *
     * @return
     */
    String readPassword();

    /**
     * 保存自定义消息状态
     *
     * @param msgId  消息ID
     * @param status 1:已焚毁 2:已领取 3:已过期
     */
    void saveChatCustomMessageStatus(String msgId, int status);

    /**
     * 读取自定义消息状态
     *
     * @param msgId 消息ID
     * @return
     */
    int readCahtCustomMessageStatus(String msgId);

    /**
     * 保存聊天通知声音提示
     *
     * @param isSound
     */
    void saveChatMessageIsSound(Boolean isSound);

    /**
     * 获取聊天通知声音提示
     *
     * @return
     */
    Boolean readChatMessageIsSound();

    /**
     * 保存聊天通知声震动提示
     *
     * @param isShake
     */
    void saveChatMessageIsShake(Boolean isShake);

    /**
     * 获取聊天通知震动提示
     *
     * @return
     */
    Boolean readChatMessageIsShake();

    /**
     * 保存首页默认展示页面
     *
     * @param pageName
     */
    void saveDefaultHomePageConfig(String pageName);

    /**
     * 读取首页默认展示页面
     *
     * @return
     */
    String readDefaultHomePageConfig();
}
