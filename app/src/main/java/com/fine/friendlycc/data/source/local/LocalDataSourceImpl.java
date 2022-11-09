package com.fine.friendlycc.data.source.local;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.source.LocalDataSource;
import com.fine.friendlycc.entity.ApiConfigManagerEntity;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.CrystalDetailsConfigEntity;
import com.fine.friendlycc.entity.EvaluateObjEntity;
import com.fine.friendlycc.entity.GameConfigEntity;
import com.fine.friendlycc.entity.LocalGooglePayCache;
import com.fine.friendlycc.entity.LocalMessageIMEntity;
import com.fine.friendlycc.entity.OccupationConfigItemEntity;
import com.fine.friendlycc.entity.SystemConfigEntity;
import com.fine.friendlycc.entity.SystemConfigTaskEntity;
import com.fine.friendlycc.entity.TokenEntity;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.utils.StringUtil;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本地数据源，可配合Room框架使用
 *
 * @author goldze
 * @date 2019/3/26
 */
public class LocalDataSourceImpl implements LocalDataSource {
    private static final String KEY_VERSION_APP = "key_version_app";
    private static final String KEY_CHANNEL_AF = "key_channel_af";
    private static final String KEY_SYSTEM_CONFIG_TASK = "key_system_config_task";
    private static final String KEY_MESSAGE_TAG_USER = "key_message_tag_user";
    private static final String KEY_INVITE_CODE = "key_invity_code";
    private static final String KEY_CACHE_GOOGLE_PAY = "key_cache_google_pay";//谷歌支付缓存
    private static final String KEY_LOGIN_INFO = "key_login_info";
    private static final String KEY_USER_DATA = "key_user_data";
    private static final String KEY_IS_VERIFY_FACE = "key_is_verify_face";
    private static final String KEY_IS_NEW_USER = "key_is_new_user";
    private static final String KEY_NEED_VERIFY_FACE = "key_need_verify_face";
    private static final String KEY_PROGRAM_TIME_CONFIG = "key_program_time_config";
    private static final String KEY_HEIGHT_CONFIG = "key_height_config";
    private static final String KEY_WEIGHT_CONFIG = "key_weight_config";
    private static final String KEY_IS_CHAT_PUSH = "key_is_chat_push";
    private static final String KEY_SENSITIVE_WORDS = "key_sensitive_words";
    private static final String KEY_GAME_CONFIG = "key_game_config";
    private static final String KEY_CRYSTAL_CONFIG = "key_crystal_config";
    private static final String KEY_REPORT_REASON_CONFIG = "key_report_reason_config";
    private static final String KEY_FAMALE_EVALUATE_CONFIG = "key_female_evaluate_config";
    private static final String KEY_MALE_EVALUATE_CONFIG = "key_male_evaluate_config";
    private static final String KEY_HOPE_OBJECT_CONFIG = "key_hope_opject_config";
    private static final String KEY_OCCUPATION_CONFIG = "key_occupation_config";
    private static final String KEY_THEME_CONFIG = "key_theme_config";
    private static final String KEY_CITY_CONFIG = "key_city_config";
    private static final String KEY_SYSTEM_CONFIG = "key_system_config";
    private static final String KEY_LOCK_PASSWORD = "lock_password";
    private static final String KEY_CHAT_MESSAGE_ISSOUND = "chat_message_isSound";
    private static final String KEY_CHAT_MESSAGE_ISSHAKE = "chat_message_isShake";
    private static final String KEY_DEFAULT_HOME_PAGE_NAME = "default_home_page_name";
    private static final String KEY_API_CONFIG_MANAGER = "api_config_manager";
    private static final String KEY_CITY_CONFIG_ALL = "key_city_config_all";
    private static final String KEY_IS_FIRST = "is_first";
    private volatile static LocalDataSourceImpl INSTANCE = null;
    private final String cryptKey = "playfun@2022";
    private final MMKV kv = MMKV.mmkvWithID("cache", MMKV.SINGLE_PROCESS_MODE, cryptKey);

    private LocalDataSourceImpl() {
        //数据库Helper构建
    }

    public MMKV getMMKV(){
        return kv;
    }

    public static LocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void saveCityConfigAll(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        kv.encode(KEY_CITY_CONFIG_ALL, json);

    }

    @Override
    public List<ConfigItemEntity> readCityConfigAll() {
        String json = kv.decodeString(KEY_CITY_CONFIG_ALL);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }

    /**
    * @Desc TODO(添加本地消息记录IM)
    * @author 彭石林
    * @parame [eventId, MsgId, sendTime]
    * @return void
    * @Date 2021/10/25
    */
    public void putLocalMessageIM(String eventId,String MsgId,long sendTime){
        LocalMessageIMEntity localMessageIMEntity = new LocalMessageIMEntity(MsgId,sendTime);
        if (ObjectUtils.isEmpty(localMessageIMEntity)) {
            return;
        }
        String json = GsonUtils.toJson(localMessageIMEntity);
        kv.encode(eventId, json);
    }
    /**
    * @Desc TODO(读取本地消息记录IM)
    * @author 彭石林
    * @parame [eventId]
    * @return com.dl.playfun.entity.LocalMessageIMEntity
    * @Date 2021/10/25
    */
    public LocalMessageIMEntity readLocalMessageIM(String eventId) {
        String json = kv.decodeString(eventId);
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        return GsonUtils.fromJson(json, LocalMessageIMEntity.class);
    }
    /**
    * @Desc TODO(删除本地消息记录IM)
    * @author 彭石林
    * @parame [eventId]
    * @return void
     * @Date 2021/10/25
     */
    public void removeLocalMessage(String eventId) {
        kv.remove(eventId);
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveApiConfigManager(ApiConfigManagerEntity apiConfigManager) {
        if(ObjectUtils.isEmpty(apiConfigManager)){
            return;
        }
        String json = GsonUtils.toJson(apiConfigManager);
        kv.encode(KEY_API_CONFIG_MANAGER, json);
    }

    @Override
    public ApiConfigManagerEntity readApiConfigManagerEntity() {
        String json = kv.decodeString(KEY_API_CONFIG_MANAGER);
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        return GsonUtils.fromJson(json, ApiConfigManagerEntity.class);
    }

    @Override
    public void putKeyValue(String key, String value) {
        kv.encode(key, value);
    }

    @Override
    public String readKeyValue(String key) {
        return kv.decodeString(key);
    }

    @Override
    public Integer readSwitches(String key) {
        return kv.decodeInt(key, 0);
    }

    @Override
    public void putSwitches(String key, Integer value) {
        kv.encode(key, value);
    }

    @Override
    public Boolean readVerifyGoddessTipsUser(String key) {
        return kv.decodeBool(key);
    }

    @Override
    public void putVerifyGoddessTipsUser(String key, String value) {
        kv.encode(key, value);
    }

    @Override
    public void saveVersion(String code) {
        kv.encode(KEY_VERSION_APP, code);
    }

    @Override
    public Boolean readVersion() {
        return kv.decodeBool(KEY_VERSION_APP);
    }

    @Override
    public void clearChannelAF() {
        kv.remove(KEY_CHANNEL_AF);
    }

    @Override
    public String readChannelAF() {
        return kv.decodeString(KEY_CHANNEL_AF);
    }

    @Override
    public void saveChannelAF(String channel) {
        kv.encode(KEY_CHANNEL_AF, channel);
    }

    @Override
    public SystemConfigTaskEntity readSystemConfigTask() {
        String json = kv.decodeString(KEY_SYSTEM_CONFIG_TASK);
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        return GsonUtils.fromJson(json, SystemConfigTaskEntity.class);
    }

    @Override
    public void saveSystemConfigTask(SystemConfigTaskEntity entity) {
        if (ObjectUtils.isEmpty(entity)) {
            return;
        }
        String json = GsonUtils.toJson(entity);
        kv.encode(KEY_SYSTEM_CONFIG_TASK, json);
    }

    @Override
    public Map<String, String> redMessageTagUser() {
        String json = kv.decodeString(KEY_MESSAGE_TAG_USER);
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        return GsonUtils.fromJson(json, Map.class);
    }

    @Override
    public void saveMessageTagUser(Map map) {
        if (ObjectUtils.isEmpty(map)) {
            return;
        }
        String json = GsonUtils.toJson(map);
        kv.encode(KEY_MESSAGE_TAG_USER, json);
    }

    @Override
    public void clearOneLinkCode() {
        kv.remove(KEY_INVITE_CODE);
    }

    @Override
    public Map<String, String> readOneLinkCode() {
        String linkCode = kv.decodeString(KEY_INVITE_CODE);
        if (StringUtil.isEmpty(linkCode)) {
            return null;
        }
        return GsonUtils.fromJson(linkCode, Map.class);
    }

    @Override
    public void saveOneLinkCode(String linkCode) {
        if (StringUtil.isEmpty(linkCode)) {
            return;
        }
        kv.encode(KEY_INVITE_CODE, linkCode);
    }

    /**
     * 清楚本地支付缓存
     */

    @Override
    public void clearGooglePayCache() {
        kv.remove(KEY_CACHE_GOOGLE_PAY);
    }

    /**
     * 读取谷歌离线支付单
     *
     * @return
     */
    @Override
    public LocalGooglePayCache readGooglePlay() {
        String json = kv.decodeString(KEY_CACHE_GOOGLE_PAY);
        if (json == null) {
            return null;
        } else if (json.isEmpty()) {
            return null;
        }
        LocalGooglePayCache localGooglePayCache = GsonUtils.fromJson(json, LocalGooglePayCache.class);
        return localGooglePayCache;
    }

    @Override
    public void saveGooglePlay(LocalGooglePayCache localGooglePayCache) {
        if (localGooglePayCache == null) {
            return;
        }
        String json = GsonUtils.toJson(localGooglePayCache);
        kv.encode(KEY_CACHE_GOOGLE_PAY, json);
    }

    @Override
    public void saveLoginInfo(TokenEntity tokenEntity) {
        if (tokenEntity == null) {
            return;
        }
        String json = GsonUtils.toJson(tokenEntity);
        kv.encode(KEY_LOGIN_INFO, json);
    }

    @Override
    public TokenEntity readLoginInfo() {
        String json = kv.decodeString(KEY_LOGIN_INFO);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return GsonUtils.fromJson(json, TokenEntity.class);
    }

    @Override
    public void logout() {
        kv.remove(KEY_LOGIN_INFO);
        kv.remove(KEY_USER_DATA);
        kv.remove(KEY_LOCK_PASSWORD);
    }

    @Override
    public void saveUserData(UserDataEntity userDataEntity) {
        String json = GsonUtils.toJson(userDataEntity);
        kv.encode(KEY_USER_DATA, json);
    }

    @Override
    public UserDataEntity readUserData() {
        String json = kv.decodeString(KEY_USER_DATA);
        if (json == null) {
            return null;
        } else if (json.isEmpty()) {
            return null;
        }
        UserDataEntity userDataEntity = GsonUtils.fromJson(json, UserDataEntity.class);
        return userDataEntity;
    }

    @Override
    public void saveIsVerifyFace(Boolean isVerifyFace) {
        kv.encode(KEY_IS_VERIFY_FACE, isVerifyFace);
    }

    @Override
    public Boolean readIsVerifyFace() {
        return kv.decodeBool(KEY_IS_VERIFY_FACE);
    }

    @Override
    public void saveIsNewUser(Boolean isNewUser) {
        kv.encode(KEY_IS_NEW_USER, isNewUser);
    }

    @Override
    public Boolean readIsNewUser() {
        return kv.decodeBool(KEY_IS_NEW_USER);
    }



    @Override
    public void saveNeedVerifyFace(boolean needVerifyFace) {
        kv.encode(KEY_NEED_VERIFY_FACE, needVerifyFace);
    }

    @Override
    public boolean readNeedVerifyFace() {
        return kv.decodeBool(KEY_NEED_VERIFY_FACE, false);
    }

    @Override
    public void saveSystemConfig(SystemConfigEntity config) {
        String json = GsonUtils.toJson(config);
        kv.encode(KEY_SYSTEM_CONFIG, json);
    }

    @Override
    public SystemConfigEntity readSystemConfig() {
        String json = kv.decodeString(KEY_SYSTEM_CONFIG);
        if (json != null) {
            SystemConfigEntity systemConfigEntity = GsonUtils.fromJson(json, SystemConfigEntity.class);
            return systemConfigEntity;
        }
        return null;
    }

    @Override
    public Boolean readChatPushStatus() {
        int isChatPush = kv.decodeInt(KEY_IS_CHAT_PUSH);
        return isChatPush == 1;
    }

    @Override
    public void saveChatPushStatus(int value) {
        kv.encode(KEY_IS_CHAT_PUSH, value);
    }

    @Override
    public void saveHeightConfig(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_HEIGHT_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<ConfigItemEntity> readHeightConfig() {
        String json = kv.decodeString(KEY_HEIGHT_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveWeightConfig(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_WEIGHT_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<ConfigItemEntity> readWeightConfig() {
        String json = kv.decodeString(KEY_WEIGHT_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public List<String> readSensitiveWords() {
        String json = kv.decodeString(KEY_SENSITIVE_WORDS);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> list = GsonUtils.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveSensitiveWords(List<String> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_SENSITIVE_WORDS, json);
        System.out.println(b);
    }

    @Override
    public void saveGameConfig(List<GameConfigEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_GAME_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<GameConfigEntity> readGameConfig() {
        String json = kv.decodeString(KEY_GAME_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<GameConfigEntity> list = GsonUtils.fromJson(json, new TypeToken<List<GameConfigEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveCrystalDetailsConfig (CrystalDetailsConfigEntity configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_CRYSTAL_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public CrystalDetailsConfigEntity readCrystalDetailsConfig() {
        String json = kv.decodeString(KEY_CRYSTAL_CONFIG);
        if (json != null) {
            CrystalDetailsConfigEntity crystalDetailsConfigEntity = GsonUtils.fromJson(json, CrystalDetailsConfigEntity.class);
            return crystalDetailsConfigEntity;
        }
        return null;
    }

    @Override
    public void saveReportReasonConfig(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_REPORT_REASON_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<ConfigItemEntity> readReportReasonConfig() {
        String json = kv.decodeString(KEY_REPORT_REASON_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }


    @Override
    public void saveFemaleEvaluateConfig(List<EvaluateObjEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_FAMALE_EVALUATE_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<EvaluateObjEntity> readFemaleEvaluateConfig() {
        String json = kv.decodeString(KEY_FAMALE_EVALUATE_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<EvaluateObjEntity> list = GsonUtils.fromJson(json, new TypeToken<List<EvaluateObjEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveMaleEvaluateConfig(List<EvaluateObjEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_MALE_EVALUATE_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<EvaluateObjEntity> readMaleEvaluateConfig() {
        String json = kv.decodeString(KEY_MALE_EVALUATE_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<EvaluateObjEntity> list = GsonUtils.fromJson(json, new TypeToken<List<EvaluateObjEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public List<EvaluateObjEntity> readEvaluateConfig() {
        if (readUserData() != null && readUserData().getSex() != null && readUserData().getSex() == 1) {
            return readMaleEvaluateConfig();
        }
        return readFemaleEvaluateConfig();
    }

    @Override
    public void saveHopeObjectConfig(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_HOPE_OBJECT_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<ConfigItemEntity> readHopeObjectConfig() {
        String json = kv.decodeString(KEY_HOPE_OBJECT_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveOccupationConfig(List<OccupationConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_OCCUPATION_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<OccupationConfigItemEntity> readOccupationConfig() {
        String json = kv.decodeString(KEY_OCCUPATION_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<OccupationConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<OccupationConfigItemEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void saveCityConfig(List<ConfigItemEntity> configs) {
        String json = GsonUtils.toJson(configs);
        boolean b = kv.encode(KEY_CITY_CONFIG, json);
        System.out.println(b);
    }

    @Override
    public List<ConfigItemEntity> readCityConfig() {
        String json = kv.decodeString(KEY_CITY_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConfigItemEntity> list = GsonUtils.fromJson(json, new TypeToken<List<ConfigItemEntity>>() {
        }.getType());
        return list;
    }

    @Override
    public void savePassword(String password) {
        boolean b = kv.encode(KEY_LOCK_PASSWORD, password);
        System.out.println(b);
    }

    @Override
    public String readPassword() {
        String password = kv.decodeString(KEY_LOCK_PASSWORD);
        return password;
    }

    @Override
    public void saveChatCustomMessageStatus(String msgId, int status) {
        kv.encode(msgId, status);
    }

    @Override
    public int readCahtCustomMessageStatus(String msgId) {
        return kv.decodeInt(msgId, 0);
    }

    @Override
    public void saveChatMessageIsSound(Boolean isSound) {
        boolean b = kv.encode(KEY_CHAT_MESSAGE_ISSOUND, isSound);
        System.out.println(b);
    }

    @Override
    public Boolean readChatMessageIsSound() {
        Boolean isSound = kv.decodeBool(KEY_CHAT_MESSAGE_ISSOUND, true);
        return isSound;
    }

    @Override
    public void saveChatMessageIsShake(Boolean isShake) {
        boolean b = kv.encode(KEY_CHAT_MESSAGE_ISSHAKE, isShake);
        System.out.println(b);
    }

    @Override
    public Boolean readChatMessageIsShake() {
        Boolean isShake = kv.decodeBool(KEY_CHAT_MESSAGE_ISSHAKE, true);
        return isShake;
    }

    @Override
    public void saveDefaultHomePageConfig(String pageName) {
        kv.encode(KEY_DEFAULT_HOME_PAGE_NAME, pageName);
    }

    @Override
    public String readDefaultHomePageConfig() {
        return kv.decodeString(KEY_DEFAULT_HOME_PAGE_NAME, "home");
    }
}
