package com.tencent.qcloud.tuicore.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tuicore.custom.GameConfigEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description：
 * @Author： liaosf
 * @Date： 2022/1/15 15:55
 * 修改备注：
 */
public class ConfigManagerUtil {
    private static ConfigManagerUtil mCacheManager;
    private final String cryptKey = "playfun@2022";
    private static final String KEY_GAME_CONFIG = "key_game_config";
    private static final String KEY_PLAY_GAME_FLAG = "key_play_game_flag";
    private static final String EXCHANGE_RULESFLAG_FLAG = "exchange_rulesflag_flag";
    private static final String KEY_IS_CHAT_PUSH = "key_is_chat_push";
    private final MMKV kv = MMKV.mmkvWithID("cache", MMKV.SINGLE_PROCESS_MODE, cryptKey);
    private Gson gson;

    public static ConfigManagerUtil getInstance() {
        if (mCacheManager == null) {
            synchronized (ConfigManagerUtil.class) {
                if (mCacheManager == null) {
                    mCacheManager = new ConfigManagerUtil();
                }
            }
        }
        return mCacheManager;
    }

    public List<GameConfigEntity> readGameConfig() {
        String json = kv.decodeString(KEY_GAME_CONFIG);
        if (json == null) {
            return new ArrayList<>();
        } else if (json.isEmpty()) {
            return new ArrayList<>();
        }
        if (gson == null ){
            gson = new Gson();
        }
        List<GameConfigEntity> list = gson.fromJson(json, new TypeToken<List<GameConfigEntity>>() {}.getType());
        return list;
    }

    public String getGameUrl(String gameChannel){
        if (gameChannel == null || gameChannel.equals("")){
            return "";
        }
        List<GameConfigEntity> gameConfigEntities = readGameConfig();

        if (isEmpty(gameConfigEntities)){
            return "";
        }
        String gameUrl= "";
        for (int i = 0; i < gameConfigEntities.size(); i++) {
            GameConfigEntity configItemEntity = gameConfigEntities.get(i);
            if (configItemEntity.getId().equals(gameChannel)){
//            if (configItemEntity.getId().equals("1642158125")){//模拟显示
                gameUrl = configItemEntity.getUrl();
            }
        }
        return gameUrl;
    }

    public boolean isPlayGameFlag() {
        return kv.decodeBool(KEY_PLAY_GAME_FLAG,false);
    }

    public void putPlayGameFlag(boolean playGameFlag) {
        kv.encode(KEY_PLAY_GAME_FLAG, playGameFlag);
    }

    public boolean getExchangeRulesFlag() {
        return kv.decodeBool(EXCHANGE_RULESFLAG_FLAG,false);
    }

    public void putExchangeRulesFlag(boolean playGameFlag) {
        kv.encode(EXCHANGE_RULESFLAG_FLAG, playGameFlag);
    }

    public static boolean isEmpty(final CharSequence obj) {
        return obj == null || obj.toString().length() == 0;
    }

    public static boolean isEmpty(final Collection obj) {
        return obj == null || obj.isEmpty();
    }

    public Boolean readChatPushStatus() {
        int isChatPush = kv.decodeInt(KEY_IS_CHAT_PUSH);
        return isChatPush == 1;
    }

}
