package com.fine.friendlycc.data.source;

import com.fine.friendlycc.bean.ApiConfigManagerBean;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.CrystalDetailsConfigBean;
import com.fine.friendlycc.bean.EvaluateObjBean;
import com.fine.friendlycc.bean.GameConfigBean;
import com.fine.friendlycc.bean.LocalGooglePayCache;
import com.fine.friendlycc.bean.OccupationConfigItemBean;
import com.fine.friendlycc.bean.SystemConfigBean;
import com.fine.friendlycc.bean.SystemConfigTaskBean;
import com.fine.friendlycc.bean.TokenBean;
import com.fine.friendlycc.bean.UserDataBean;

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
    void saveCityConfigAll(List<ConfigItemBean> configs);
    /**
     * 获取所有配置
     *
     * @return
     */
    List<ConfigItemBean> readCityConfigAll();
    //保存api配置
    void saveApiConfigManager(ApiConfigManagerBean apiConfigManager);
    //读取api配置
    ApiConfigManagerBean readApiConfigManagerEntity();

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
     * @return com.dl.playcc.entity.SystemConfigTaskEntity
     * @Desc TODO(读取本地任务中心配置)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/26
     */
    SystemConfigTaskBean readSystemConfigTask();

    /**
     * @return void
     * @Desc TODO(保存任务中心配置到本地)
     * @author 彭石林
     * @parame []
     * @Date 2021/8/26
     */
    void saveSystemConfigTask(SystemConfigTaskBean entity);

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
    void saveLoginInfo(TokenBean tokenEntity);

    /**
     * 读取登录信息
     *
     * @return
     */
    TokenBean readLoginInfo();

    /**
     * 清除登录信息
     */
    void logout();

    /**
     * 保存用户信息
     *
     * @param userDataEntity
     */
    void saveUserData(UserDataBean userDataEntity);

    /**
     * 读取用户信息
     *
     * @return
     */
    UserDataBean readUserData();

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
    void saveSystemConfig(SystemConfigBean config);

    /**
     * 读取系统配置
     *
     * @return
     */
    SystemConfigBean readSystemConfig();

     /**
     * 游戏图标配置
     *
     * @param configs
     */
    void saveGameConfig(List<GameConfigBean> configs);

    /**
     * 读取游戏图标
     * @return
     */
    List<GameConfigBean> readGameConfig();

    /**
     * 保存水晶兌換规则显示标记
     * @param configs
     */
    void saveCrystalDetailsConfig(CrystalDetailsConfigBean configs);

    /**
     * 读取水晶兌換规则显示标记
     * @return
     */
    CrystalDetailsConfigBean readCrystalDetailsConfig();

    /**
     * 保存身高配置
     *
     * @param configs
     */
    void saveHeightConfig(List<ConfigItemBean> configs);

    /**
     * 获取身高配置
     *
     * @return
     */
    List<ConfigItemBean> readHeightConfig();

    /**
     * 保存体重配置
     *
     * @param configs
     */
    void saveWeightConfig(List<ConfigItemBean> configs);

    /**
     * 获取体重配置
     *
     * @return
     */
    List<ConfigItemBean> readWeightConfig();

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
    void saveReportReasonConfig(List<ConfigItemBean> configs);

    /**
     * 获取举报原因配置
     *
     * @return
     */
    List<ConfigItemBean> readReportReasonConfig();

    /**
     * 保存女性评价标签配置
     *
     * @param configs
     */
    void saveFemaleEvaluateConfig(List<EvaluateObjBean> configs);

    /**
     * 保存男性评价标签配置
     *
     * @param configs
     */
    void saveMaleEvaluateConfig(List<EvaluateObjBean> configs);

    /**
     * 读取女士评价标签配置
     *
     * @return
     */
    List<EvaluateObjBean> readFemaleEvaluateConfig();

    /**
     * 读取男士评价标签
     *
     * @return
     */
    List<EvaluateObjBean> readMaleEvaluateConfig();

    /**
     * 读取评价标签
     *
     * @return
     */
    List<EvaluateObjBean> readEvaluateConfig();

    /**
     * 保存期望对象配置
     *
     * @param configs
     */
    void saveHopeObjectConfig(List<ConfigItemBean> configs);

    /**
     * 获取期望对象配置
     *
     * @return
     */
    List<ConfigItemBean> readHopeObjectConfig();

    /**
     * 保存职业配置
     *
     * @param configs
     */
    void saveOccupationConfig(List<OccupationConfigItemBean> configs);

    /**
     * 获取职业配置
     *
     * @return
     */
    List<OccupationConfigItemBean> readOccupationConfig();

    /**
     * 保存城市配置
     *
     * @param configs
     */
    void saveCityConfig(List<ConfigItemBean> configs);

    /**
     * 获取城市配置
     *
     * @return
     */
    List<ConfigItemBean> readCityConfig();

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