package com.fine.friendlycc.utils;

import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.R;
import com.fine.friendlycc.entity.ConfigItemEntity;
import com.fine.friendlycc.entity.OccupationConfigItemEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.google.gson.Gson;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StringUtil {
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    /**
     * 大写转小写
     */
    public static String toLowerCasea(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ('A' <= chars[i] && chars[i] <= 'Z') {
                chars[i] += 32;
            }
        }
        return String.valueOf(chars);
    }

    /**
     * @param str
     * @return
     */
    public static String toLowerCaseA(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ('a' <= chars[i] && chars[i] <= 'z') {
                chars[i] -= 32;
            }
        }
        return String.valueOf(chars);
    }

    //
    public static String getConfigValue(List<Integer> ids, List<ConfigItemEntity> data) {
        if (ids == null) {
            return "";
        }
        String result = "";
        if (data.size() != 0) {
            for (ConfigItemEntity config : data) {
                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i).intValue() == config.getId()) {
                        if (i == ids.size() - 1) {
                            result += config.getName();
                        } else {
                            result = result + config.getName() + "/";
                        }
                    }

                }
            }
        }
        return result;
    }

    public static String getOccputionValue(int id, List<OccupationConfigItemEntity> data) {
        String result = "";
        if (data.size() != 0) {
            for (OccupationConfigItemEntity config : data) {
                for (OccupationConfigItemEntity.ItemEntity item : config.getItem()
                ) {
                    if (id == item.getId()) {
                        result = item.getName();
                    }
                }
            }
        }
        return result;
    }

    public static String getFullImageUrl(String imgPath) {
        if (imgPath == null) {
            return "";
        } else {
            return AppConfig.IMAGE_BASE_URL + imgPath;
        }
    }

    public static String getFullAudioUrl(String urlPath) {
        if (urlPath == null) {
            return "";
        } else {
            return AppConfig.IMAGE_BASE_URL + urlPath;
        }
    }

    public static String getFullImageWatermarkUrl(String imgPath) {
        if (imgPath == null) {
            return "";
        } else if (imgPath.toLowerCase().startsWith("images/")) {
            if (imgPath.endsWith(".mp4")) {
                return AppConfig.IMAGE_BASE_URL + imgPath;
            } else {
                return AppConfig.IMAGE_BASE_URL + imgPath + "?x-oss-process=style/watermark";
            }
        }
        return imgPath;
    }

    public static String getFullThumbImageUrl(String imgPath) {
        if (imgPath == null) {
            return "";
        } else if (imgPath.toLowerCase().startsWith("images/")) {
            if (imgPath.endsWith(".mp4")) {
                String url = AppConfig.IMAGE_BASE_URL + imgPath;
                return AppConfig.IMAGE_BASE_URL + imgPath;
            } else {
                return AppConfig.IMAGE_BASE_URL + imgPath + "?x-oss-process=style/thumb";
            }
        }
        return imgPath;
    }

    public static String getWeekengString(int weekeng, Context context) {
        String str = "";
        switch (weekeng) {
            case 1:
                str = StringUtils.getString(R.string.playcc_monday);
                break;
            case 2:
                str = StringUtils.getString(R.string.playcc_tuesday);
                break;
            case 3:
                str = StringUtils.getString(R.string.playcc_wednesday);
                break;
            case 4:
                str = StringUtils.getString(R.string.playcc_thursday);
                break;
            case 5:
                str = StringUtils.getString(R.string.playcc_friday);
                break;
            case 6:
                str = StringUtils.getString(R.string.playcc_saturday);
                break;
            case 7:
                str = StringUtils.getString(R.string.playcc_sunday);
                break;
        }
        return str;
    }

    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * @return java.lang.String
     * @Desc TODO(根据ID获取心情文案)
     * @author 彭石林
     * @parame [id]
     * @Date 2021/10/18
     */
    public static String getDatingObjItem(int id) {
        String str = StringUtils.getString(R.string.playcc_user_detail_ta_dynamic);
        switch (id) {
            case 1:
                str = StringUtils.getString(R.string.playcc_mood_item_id1);
                break;
            case 2:
                str = StringUtils.getString(R.string.playcc_mood_item_id2);
                break;
            case 3:
                str = StringUtils.getString(R.string.playcc_mood_item_id3);
                break;
            case 4:
                str = StringUtils.getString(R.string.playcc_mood_item_id4);
                break;
            case 5:
                str = StringUtils.getString(R.string.playcc_mood_item_id5);
                break;
            case 6:
                str = StringUtils.getString(R.string.playcc_mood_item_id6);
                break;
        }
        return str;
    }

    /**
    * @Desc TODO(判断数据是否为空。是否是json)
    * @author 彭石林
    * @parame [str]
    * @return boolean
    * @Date 2022/9/2
    */
    public static boolean isJSONEmpty(String str){
        if(isEmpty(str)){
            return false;
        }
        return isJSON2(str);
    }

    /**
     * 判断数据是否为json类型
     * @param str
     * @return
     */
    public static boolean isJSON2(String str) {
        boolean result = false;
        try {
            new Gson().fromJson(str, Map.class);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;

    }

    public static boolean initIMInfo(TUIMessageBean info) {
        if (info != null) {
            String text = String.valueOf(info.getExtra());
            if (isJSON2(text) && text.indexOf("type") != -1) {
                Map<String, Object> map_data = new Gson().fromJson(text, Map.class);
                return map_data != null && map_data.get("type") != null;
            }
        }
        return false;

    }

    //done 设置每日标识
    public static String getDailyFlag(String key) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String format = formatter.format(date);
        String userId = ConfigManager.getInstance().getUserImID();
        return key + format + userId;
    }

}
