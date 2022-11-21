package com.fine.friendlycc.app;

import android.text.TextUtils;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.lib.elk.StatisticsAnalysis;
import com.fine.friendlycc.utils.ElkLogEventUtils;

import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2022/10/11 10:25
 * Description: This is ElkLogEventReport
 */
public class ElkLogEventReport {

    public final static String _expose = "expose";
    public final static String _click = "click";

    /**
     * 点击事件
     *
     * @param lt   所属模块
     * @param et   当前页面
     * @param ct   expose: 曝光 click：点击
     * @param dt   任意拓展
     */
    public static String commonClickString(String lt, String et, String ct, String dt){
        return getCommentFiled()+"`lt="+ ElkLogEventUtils.isNullConverterSky(lt)+"`et="+ElkLogEventUtils.isNullConverterSky(et)+"`ct="+ElkLogEventUtils.isNullConverterSky(ct)+"`dt="+ElkLogEventUtils.isNullConverterSky(dt);
    }

    /**
    * @Desc TODO(获取当前公共参数)
    * @author 彭石林
    * @parame []
    * @return java.lang.String
    * @Date 2022/10/11
    */
    private static String getCommentFiled(){
        return ElkLogEventUtils.getCommonFile()+ElkLogEventUtils.getUserDataEvent()+ElkLogEventUtils.getMediaSource();
    }
    //第三方绑定/登录
    public static class reportAuthModule{
        public static void  reportBindAuth(String ct, String email){
            String doSendStatistics = commonClickString("pageview","email_receive",ct,"bind")+"`email="+isNullConverterSky(email);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        public static void  reportLoginAuth(String ct, String email){
            String doSendStatistics = commonClickString("pageview","email_receive",ct,"login")+"`email="+isNullConverterSky(email);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
    }
    //登录模块
    public static class reportLoginModule{
        public static final String showPage = "showPage";
        //登录页面
        static final String loginPage = "loginPage";
        //手机号码登录页面
        static final String phoneLogin = "phoneLogin";

        //注册页面
        static final String register = "register";

        //交友意愿
        static final String datingPurpose = "datingPurpose";

        /**
        * @Desc TODO(登录页面)
        * @author 彭石林
        * @parame [lt, et, ct, dt]
        * @Date 2022/10/22
        */
        public static void reportClickLoginPage( String ct, String dt){
            String doSendStatistics = commonClickString("pageview",loginPage, ct, dt);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

        /**
         * @Desc TODO(手机号码登录页面)
         * @author 彭石林
         * @parame [lt, et, ct, dt]
         * @Date 2022/10/22
         */
        public static void reportClickPhoneLogin (String ct, String dt){
            String doSendStatistics = commonClickString("pageview",phoneLogin, ct, dt);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        /**
         * @Desc TODO(注册页面)
         * @author 彭石林
         * @parame [lt, et, ct, dt]
         * @Date 2022/10/22
         */
        public static void reportClickRegister(String ct, String dt,Integer sex, Integer age){
            String doSendStatistics = commonClickString("pageview",register,ct, dt);
            if(sex!=null){
                doSendStatistics += "`sex="+sex;
            }
            if(sex!=null){
                doSendStatistics += "`age="+age;
            }
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        
        /**
        * @Desc TODO(交友意愿)
        * @author 彭石林
        * @parame [ct, dt, option]
        * @return void
        * @Date 2022/10/22
        */
        public static void reportClickDatingPurpose(String ct, String dt, String option){
            String doSendStatistics = commonClickString("pageview",datingPurpose,ct, dt);
            if(!TextUtils.isEmpty(option)){
                doSendStatistics += "`option="+option;
            }
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

    }
    //启动页模块
    public static class reportSplashModule{
        public static void reportInit(){
            String doSendStatistics = commonClickString("pageview","start",_expose,"application");
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        public static void reportAFSources(String code, String source){
            String doSendStatistics = commonClickString("pageview","appInvitation",_expose,null)+
                    "`code="+isNullConverterSky(code)
                    +"`source="+isNullConverterSky(source);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
    }
    //砖石储值模块
    public static class reportCoinRecharge{

        public static void reportSheetViewOpen(int source){
            String doSendStatistics = commonClickString("pageview","topUpPage",_expose,"popTopUp")+"`source="+source;
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

        public static void reportSheetView(String ct,String dt){
            String doSendStatistics = commonClickString("pageview","topUpPage",ct,dt);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        public static void reportSheetPayView(Object price,int topUpRet,String spend_time,int errorMsg,String payType,String orderNo){
            String doSendStatistics = commonClickString("pageview","topUpPage",_click,"buy")+
                    "`price="+price
                    +"`topUpRet="+topUpRet
                    +"`spend_time="+spend_time
                    +"`errorMsg="+errorMsg
                    +"`payType="+payType
                    +"`orderNo="+orderNo;
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        public static void reportSheetPayView(Object price,int topUpRet,String spend_time,String payType,String orderNo){
            String doSendStatistics = commonClickString("pageview","topUpPage",_click,"buy")+
                    "`price="+price
                    +"`topUpRet="+topUpRet
                    +"`spend_time="+spend_time
                    +"`payType="+payType
                    +"`orderNo="+isNullConverterSky(orderNo);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

    }
    //红包模块打点
    public static class reportMediaGallery{
        //红包照片模块打点
        static final String lt= "redPackage";

        static String isVideoStr(boolean isVideo){
            return isVideo ? "video" : "image";
        }

        /**
        * @Desc TODO(查看资源的时候，添加这个打点，为了统计有多少人选择付费资源)
        * @author 彭石林
        * @parame [dt=image/video判断是视频还是图片, unlockPrice =当前付费资源解锁价格, stateSnapshot=是否是快照, statePhotoPay=是否是付费照片, msgKey=上报服务器返回的msgkey, stateVideoPay=是否是付费视频, isUnLocked=是否已读]
        * @Date 2022/10/11
        */
        public static void reportReadMediaGallery(boolean isVideo, String unlockPrice, String stateSnapshot, String msgKey, String statePay,String isUnLocked){
            String doSendStatistics = commonClickString(lt,"sourceCheck","click",isVideoStr(isVideo)) +
                     "`unlockPrice="+isNullConverterSky(unlockPrice)
                     +"`stateSnapshot="+isNullConverterSky(stateSnapshot)
                    +"`msgKey="+isNullConverterSky(msgKey)
                    +"`isUnLocked="+isNullConverterSky(isUnLocked);
            if (!isVideo){
                doSendStatistics += "`statePhotoPay="+statePay;
            }else{
                doSendStatistics += "`stateVideoPay="+statePay;
            }
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

        /**
        * @Desc TODO(评价当前资源的时候 ， 添加这个打点)
        * @author 彭石林
        * @parame [dt=image/video判断是视频还是图片, toUserId=评价谁, evaluteState=好评还是差评, msgKey=上报服务器返回的msgkey]
        * @Date 2022/10/11
        */
        public static void reportEvaluteMediaGallery(boolean isVideo, Object toUserId, String evaluteState, String msgKey){
            String doSendStatistics = commonClickString(lt,"evalute","click",isVideoStr(isVideo)) +
                    "`toUserId="+isNullConverterSky(toUserId)
                    +"`evaluteState="+isNullConverterSky(evaluteState)
                    +"`msgKey="+isNullConverterSky(msgKey);
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }

        /**
        * @Desc TODO(发送视频和图片资源的时候，添加这个打点统计)
        * @author 彭石林
        * @parame [dt=image/video判断是视频还是图片, toUserId=发送给谁, stateSnapshot=是否是快照, statePhotoPay=是否是付费照片, unlockPrice=当前付费资源解锁价格, stateVideoPay=是否是付费视频, configId, configIndexString]
        * @Date 2022/10/11
        */
        public static void reportSendMediaGallery(boolean isVideo, Object toUserId, String stateSnapshot, boolean statePay, BigDecimal unlockPrice, Integer configId, String configIndexString){
            String doSendStatistics = commonClickString(lt,"sourceSend","click",isVideoStr(isVideo)) +
                    "`unlockPrice="+isNullConverterSky(unlockPrice)
                    +"`stateSnapshot="+isNullConverterSky(stateSnapshot)
                    +"`toUserId="+isNullConverterSky(toUserId)
                    +"`configId="+isNullConverterSky(configId)
                    +"`configIndexString="+isNullConverterSky(configIndexString);
            if(!isVideo){
                doSendStatistics += "`statePhotoPay="+isNullConverterSky(statePay);
            }else{
                doSendStatistics += "`stateVideoPay="+isNullConverterSky(statePay);
            }
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
    }

    public static class reportBillingClientModule{
        /**
        * @Desc TODO(支付流程上报打点)
        * @author 彭石林
        * @parame [flowNode, flowCode]
        * @return void
        * @Date 2022/10/24
        */
        public static void reportBillingClientPayment(String flowNode, int flowCode){
            String doSendStatistics = commonClickString("googlePlay","lifecycle","Buy","Payment")+"`flowNode="+flowNode+"`flowCode="+flowCode;
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
        
        public static void reportBillingClientHistory(String flowNode,int flowCode){
            String doSendStatistics = commonClickString("googlePlay","lifecycle","Buy","History")+"`flowNode="+flowNode+"`flowCode="+flowCode;
            StatisticsAnalysis.doSendStatistics(doSendStatistics);
        }
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
        if(obj.equals("null")){
            return "";
        }
        return String.valueOf(obj);
    }
    public static String isNullConverterSky(String obj){
        if(StringUtils.isEmpty(obj)){
            return "";
        }
        if(obj.equals("null")){
            return "";
        }
        return obj;
    }
}
