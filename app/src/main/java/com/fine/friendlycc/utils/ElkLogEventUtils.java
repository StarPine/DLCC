package com.fine.friendlycc.utils;

import android.os.Build;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.dl.lib.util.MPDeviceUtils;
import com.dl.lib.util.emulator.EmulatorDetector;
import com.fine.friendlycc.app.AppConfig;
import com.fine.friendlycc.data.AppRepository;
import com.fine.friendlycc.entity.UserDataEntity;
import com.fine.friendlycc.manager.ConfigManager;
import com.fine.friendlycc.manager.LocaleManager;

import java.util.Map;

import me.goldze.mvvmhabit.http.NetworkUtil;

/**
 * Author: 彭石林
 * Time: 2022/10/10 18:30
 * Description: This is ElkLogEventUtils
 */
public class ElkLogEventUtils {

    public static String DEFAULT_LOCAL_LANGUAGE;

    public static String getDefaultLocalLanguage() {
        return DEFAULT_LOCAL_LANGUAGE;
    }

    public static void setDefaultLocalLanguage(String defaultLocalLanguage) {
        if(DEFAULT_LOCAL_LANGUAGE == null){
            DEFAULT_LOCAL_LANGUAGE = defaultLocalLanguage;
        }
    }

    private static int rating = -1;

    /**
    * @Desc TODO(获取当前用户的公共参数)
    * @author 彭石林
    * @parame []
    * @return java.lang.String
    * @Date 2022/10/10
    */
    public static String getUserDataEvent(){
        StringBuilder value = new StringBuilder();
        UserDataEntity userDataEntity = ConfigManager.getInstance().getAppRepository().readUserData();
        if(ObjectUtils.isNotEmpty(userDataEntity)){
            //用户的平台id
            //当前用户是否是vip
            //用户的昵称
            value
                //用户的平台id
                .append("`uid=").append(isNullConverterSky(userDataEntity.getId()))
                //当前用户是否是vip
                .append("`is_vip=").append(isNullConverterSky(userDataEntity.getIsVip()))
                //用户的昵称
                .append("`nickname=").append(isNullConverterSky(userDataEntity.getNickname()))
                //用户性别
                .append("`sex=").append(isNullConverterSky(userDataEntity.getSex()))
                //是不是工会主播
                .append("`is_anchor=").append(isNullConverterSky(userDataEntity.getAnchor()))
                //是否开启视频通话
                .append("`a_video=").append(isNullConverterSky(userDataEntity.isAllowVideo()))
                //是否开启语音通话
                .append("`a_audio=").append(isNullConverterSky(userDataEntity.isAllowAudio()));
        }
        return value.toString();
    }
    /**
    * @Desc TODO(获取公共参数)
    * @author 彭石林
    * @parame []
    * @return java.lang.String
    * @Date 2022/10/10
    */
    public static String getCommonFile(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                //当前所属平台
                .append("plat=Android")
                //app版本
                .append("`build_v=" + AppConfig.VERSION_CODE)
                .append("`version=" + AppConfig.VERSION_NAME)
                //playFun版本号
                .append("`project_v="+AppConfig.SDK_VERSION_NAME_PUSH)
                //型号
                .append("`devi=").append(MPDeviceUtils.MODEL)
                //手机名称	和plat相似，iOS使用Apple
                .append("`bd=").append(MPDeviceUtils.BRAND)
                //当前app的渠道id
                .append("`appId="+AppConfig.APPID)
                //当前属于手机语言
                .append("`la="+ DEFAULT_LOCAL_LANGUAGE)
                //如果是强制使用语言包，可以添加
                .append("`la_v="+ LocaleManager.getSystemLocale(Utils.getApp()))
                //是测试服还是正式服，“test”/"master"
                .append("`envir="+(AppConfig.isDebug ? "test" : "master"))
                //当前移动端所属的网络状况，例如wifi或者wwan
                .append("`nt="+ NetworkUtil.getAPNType(Utils.getApp()))
                //当前移动端所属的ip地址
                .append("`ip="+NetworkUtil.getIPAddress(Utils.getApp()))
                .append("`isNoNoEmulator="+isEmulator()) ;
        return stringBuilder.toString();
    }
    public static boolean isEmulator() {
        if (EmulatorDetector.isEmulatorAbsolute()) {
            return true;
        }
        int newRating = 0;
        if (rating < 0) {
            if (Build.PRODUCT.contains("sdk") ||
                    Build.PRODUCT.contains("Andy") ||
                    Build.PRODUCT.contains("ttVM_Hdragon") ||
                    Build.PRODUCT.contains("google_sdk") ||
                    Build.PRODUCT.contains("Droid4X") ||
                    Build.PRODUCT.contains("nox") ||
                    Build.PRODUCT.contains("sdk_x86") ||
                    Build.PRODUCT.contains("sdk_google") ||
                    Build.PRODUCT.contains("vbox86p")) {
                newRating++;
            }

            if (Build.MANUFACTURER.equals("unknown") ||
                    Build.MANUFACTURER.equals("Genymotion") ||
                    Build.MANUFACTURER.contains("Andy") ||
                    Build.MANUFACTURER.contains("MIT") ||
                    Build.MANUFACTURER.contains("nox") ||
                    Build.MANUFACTURER.contains("TiantianVM")) {
                newRating++;
            }

            if (Build.BRAND.equals("generic") ||
                    Build.BRAND.equals("generic_x86") ||
                    Build.BRAND.equals("TTVM") ||
                    Build.BRAND.contains("Andy")) {
                newRating++;
            }

            if (Build.DEVICE.contains("generic") ||
                    Build.DEVICE.contains("generic_x86") ||
                    Build.DEVICE.contains("Andy") ||
                    Build.DEVICE.contains("ttVM_Hdragon") ||
                    Build.DEVICE.contains("Droid4X") ||
                    Build.DEVICE.contains("nox") ||
                    Build.DEVICE.contains("generic_x86_64") ||
                    Build.DEVICE.contains("vbox86p")) {
                newRating++;
            }

            if (Build.MODEL.equals("sdk") ||
                    Build.MODEL.contains("Emulator") ||
                    Build.MODEL.equals("google_sdk") ||
                    Build.MODEL.contains("Droid4X") ||
                    Build.MODEL.contains("TiantianVM") ||
                    Build.MODEL.contains("Andy") ||
                    Build.MODEL.equals("Android SDK built for x86_64") ||
                    Build.MODEL.equals("Android SDK built for x86")) {
                newRating++;
            }

            if (Build.HARDWARE.equals("goldfish") ||
                    Build.HARDWARE.equals("vbox86") ||
                    Build.HARDWARE.contains("nox") ||
                    Build.HARDWARE.contains("ttVM_x86")) {
                newRating++;
            }

            if (Build.FINGERPRINT.contains("generic/sdk/generic") ||
                    Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
                    Build.FINGERPRINT.contains("Andy") ||
                    Build.FINGERPRINT.contains("ttVM_Hdragon") ||
                    Build.FINGERPRINT.contains("generic_x86_64") ||
                    Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
                    Build.FINGERPRINT.contains("vbox86p") ||
                    Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")) {
                newRating++;
            }
//
//            try {
//                File sharedFolder = new File(StorageUtils.getExternalNonoDir(), File.separatorChar + "windows" + File.separatorChar + "BstSharedFolder");
//                if (sharedFolder.exists()) {
//                    newRating += 10;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            rating = newRating;
        }
        return rating > 3;//不能再少了，否则有可能误判，若增减了新的嫌疑度判定属性，要重新评估该值
    }

    public static String getMediaSource(){
        AppRepository appRepository = ConfigManager.getInstance().getAppRepository();
        Map<String, String> mapData = appRepository.readOneLinkCode();
        if (ObjectUtils.isEmpty(mapData)) {
            return "";
        }
        //当前邀请码
        String code = mapData.get("code");
        //用户的来源信息
        String channel = mapData.get("channel");
        return "`code="+isNullConverterSky(code)+"`channel="+isNullConverterSky(channel);
    }

    /**
    * @Desc TODO(空指针转成空字符串)
    * @author 彭石林
    * @parame [obj]
    * @return java.lang.String
    * @Date 2022/10/10
    */
    public static String isNullConverterSky(Object obj){
        if(ObjectUtils.isEmpty(obj)){
            return "";
        }
        return String.valueOf(obj);
    }
}
