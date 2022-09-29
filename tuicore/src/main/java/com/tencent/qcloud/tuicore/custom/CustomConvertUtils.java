package com.tencent.qcloud.tuicore.custom;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.custom.entity.CustomBaseEntity;
import com.tencent.qcloud.tuicore.custom.entity.CustomMsgTypeEntity;
import com.tencent.qcloud.tuicore.custom.entity.VideoPushEntity;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Author: 彭石林
 * Time: 2022/9/2 17:44
 * Description: This is CustomConvertUtils
 */
public class CustomConvertUtils {

    private final static String TAG = "CustomConvertUtils";

    /**
    * @Desc TODO(IM自定义消息转Map)
    * @author 彭石林
    * @parame [v2TIMCustomElem]
    * @return java.util.Map
    * @Date 2022/9/2
    */
    public static Map<String,Object> CustomMassageConvertMap(V2TIMCustomElem v2TIMCustomElem){
        if(v2TIMCustomElem != null){
            if(v2TIMCustomElem.getData()!=null && v2TIMCustomElem.getData().length>0){
                String customData = new String(v2TIMCustomElem.getData());
                if (!TextUtils.isEmpty(customData)) {
                    Map<String, Object> mapData = new Gson().fromJson(customData, Map.class);
                    if(null != mapData && !mapData.isEmpty()){
                        if(mapData.get(TUIConstants.Message.CUSTOM_BUSINESS_ID_KEY)!=null){
                            String businessID = String.valueOf(mapData.get(TUIConstants.Message.CUSTOM_BUSINESS_ID_KEY));
                            //判断 businessID 是内部定义模板
                            if(!TextUtils.isEmpty(businessID) && businessID.equals(CustomConstants.Message.CUSTOM_BUSINESS_ID_KEY)){
                                if(mapData.get(CustomConstants.Message.CUSTOM_CONTENT_BODY)==null){
                                    return null;
                                }
                                String contentBody = String.valueOf(mapData.get(CustomConstants.Message.CUSTOM_CONTENT_BODY));
                                if(isJSONEmpty(contentBody)){
                                   return new Gson().fromJson(contentBody, Map.class);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
    * @Desc TODO(模型转换为指定的数据)
    * @author 彭石林
    * @parame [
     * mapData, 数据模型
     * moduleName 模块名
     * ]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    * @Date 2022/9/2
    */
    public static Map<String,Object> ConvertMassageModule(final Map<String,Object> mapData,final String key,final String moduleName,final String contentKey){
        //推币机模块
        if(mapData.containsKey(key)){
            if(Objects.equals(mapData.get(key), moduleName)){
                String coinPusherString = String.valueOf(mapData.get(contentKey));
                if(CustomConvertUtils.isJSONEmpty(coinPusherString)){
                    return new Gson().fromJson(coinPusherString,Map.class);
                }
            }
        }
        return null;
    }
    /**
    * @Desc TODO(效验模型是否存在)
    * @author 彭石林
    * @parame [mapData, key, moduleName]
    * @return boolean
    * @Date 2022/9/5
    */
    public static boolean ContainsMessageModuleKey(final Map<String,Object> mapData,final String key,final String moduleName){
        if(mapData.containsKey(key)){
            return Objects.equals(mapData.get(key), moduleName);
        }
        return false;
    }

    /**
     * @Desc TODO(判断数据是否为空。是否是json)
     * @author 彭石林
     * @parame [str]
     * @return boolean
     * @Date 2022/9/2
     */
    public static boolean isJSONEmpty(String str){
        if(TextUtils.isEmpty(str)){
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
        } catch (Exception ignored) {
        }
        return result;

    }

    public static Object customMassageAnalyzeModule(String customData, String moduleName, String typeName, Type classType) {
        try {
            CustomBaseEntity customBaseEntity = new Gson().fromJson(customData, classType);
            String msgModuleName = customBaseEntity.getContentBody().getMsgModuleName();
            if (moduleName.equals(msgModuleName)){
                return customMassageAnalyzeType(customBaseEntity.getContentBody().getContentBody(),typeName);
            }
        }catch (Exception e){
            Log.i("CustomConvertUtils", "module解析异常");
        }
        return null;
    }

    public static Object customMassageAnalyzeType(CustomMsgTypeEntity msgTypeEntity, String typeName) {
        try {
            String customMsgType = msgTypeEntity.getCustomMsgType();
            if (customMsgType.equals(typeName)){
                return msgTypeEntity.getCustomMsgBody();
            }
        }catch (Exception e){
            Log.i("CustomConvertUtils", "type解析异常");
        }
        return null;
    }


}
