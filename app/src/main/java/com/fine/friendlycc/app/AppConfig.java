package com.fine.friendlycc.app;

import com.fine.friendlycc.BuildConfig;
import com.fine.friendlycc.bean.OverseasUserBean;

/**
 * @author wulei
 */
public class AppConfig {
    public static boolean isOpenDialog = false;
    public static boolean isRegister = false;
    public static boolean isRegisterAccost = false;
    public static boolean isMainPage = true;

    public static final String KEY_DL_AES_ENCRY = "key_dl_aes_encry";

    //是否开启日志打印
    public static final boolean isDebug = BuildConfig.DEBUG;
    /**
     * 版本编号
     */
    public static final Integer VERSION_CODE = BuildConfig.VERSION_CODE;
    public static final String VERSION_NAME = BuildConfig.VERSION_NAME;
    public static final String VERSION = "1.4.62";
    //上报
    public static final String SDK_VERSION_NAME_PUSH = "1.3.0";
    //source 来源ID 1667448177=PlayCC
    public static final String APPID = "1667448177";
    public static String DEVICE_CODE = "";
    /**
     * 女
     */
    public static final int FEMALE = 0;
    /**
     * 男
     */
    public static final int MALE = 1;
    /**
     * 腾讯IM离线推送ID
     */
    public static final int GOOGLE_FCM_PUSH_BUZID = BuildConfig.GOOGLE_FCM_PUSH_BUZID;
    /**
     * 客服名称
     */
    public static final String CHAT_SERVICE_USER_NAME = "administrator";
    /**
     * 客服id
     */
    public static final String CHAT_SERVICE_USER_ID = "administrator";
    /**
     * 联系客服id
     */
    public static final String CHAT_SERVICE_USER_ID_SEND = BuildConfig.CHAT_SERVICE_USER_ID_SEND;

    /**
     * 服务端默认接口HOST
     */
    public static final String DEFAULT_API_URL = BuildConfig.DEFAULT_API_URL;

    public static final String IMAGE_BASE_URL = BuildConfig.IMAGE_BASE_URL;
    // 访问的endpoint地址
    public static final String OSS_ENDPOINT = "https://oss-ap-southeast-1.aliyuncs.com";
    // oss缩放图片格式
    public static final String OSS_END_RESIZE = "?x-oss-process=image/resize,m_lfit,h_%s,w_%s";
//    // 或者根据工程sts_local_server目录中本地鉴权服务脚本代码启动本地STS 鉴权服务器。详情参见sts_local_server 中的脚本内容。
    public static final String STS_SERVER_URL =  "/api/aliyun/sts";//STS 地址
    public static final String BUCKET_NAME = BuildConfig.BUCKET_NAME;

    //临时存放第三方登录用户信息。用来注册默认读取
    public static OverseasUserBean overseasUserEntity = null;
    //记录推币机离开页面没有投币的状态展示弹窗
    public static boolean CoinPusherGameNotPushed = false;
    /**
     * 服务条款url
     */
    public static final String TERMS_OF_SERVICE_URL = "https://sites.google.com/view/playcc-user";
    /**
     * 隐私政策url
     */
    public static final String PRIVACY_POLICY_URL = "https://sites.google.com/view/playcc-privacy";

    //新IM 发送图片oss文件夹定义
    public static String OSS_CUSTOM_FILE_NAME_CHAT = "chat";
    //H5充值网址
    public static final String  PAY_RECHARGE_URL = "/recharge/recharge.html";
    /**
     * @Desc TODO(跳转页面地址)
     * @author 彭石林
     * @parame
     * @return
     * @Date 2021/8/9
     */
    public static String homePageName = "";
    /**
     * 用户主动点击退出登录
     */
    public static boolean userClickOut = false;

    /**
     * @Desc TODO(阿里云剪辑是否触发)
     * @author 彭石林
     * @Date 2021/10/29
     */
    public static boolean isCorpAliyun = false;

    /**
     * @Desc TODO(福袋网址)
     * @author 彭石林
     * @parame
     * @return
     * @Date 2021/11/6
     */
    public static String FukubukuroWebUrl = null;

    public static final String GAME_SOURCES_HEADER_KEY = "game_sources_header_key";

    public static final String GAME_SOURCES_AES_KEY = "game_sources_key";
    //游戏activity绝对路径 （实际包名+类名）
    public static final String GAME_SOURCES_ACTIVITY_NAME = "game_sources_activity_name";
    //游戏请求头APP id
    public static final String GAME_SOURCES_APP_ID = "game_sources_app_id";
    //游戏配置类
    public static final String GAME_SOURCES_APP_CONFIG = "game_sources_app_config";
    //登录方式
    public static String LOGIN_TYPE = "0";
    public static boolean isTest = false;
    public static boolean isInit = true;
}