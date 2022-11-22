package com.dl.lib.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.dl.lib.util.emulator.EmulatorDetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 彭石林
 * Time: 2022/10/7 14:33
 * Description: 获取当前设备信息
 */
public class MPDeviceUtils {

    //获取设备基板名称
    static String BOARD;
    //获取设备引导程序版本号
    static String BOOTLOADER;
    //获取设备品牌
    public static String BRAND;
    //获取设备驱动名称
    static String DEVICE;
    //获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
    static String DISPLAY;
    //设备的唯一标识。由设备的多个信息拼接合成
    static String FINGERPRINT;
    //设备硬件名称，一般和基板名称一样（BOARD）
    static String HARDWARE;
    //设备主机地址
    static String HOST;
    //设备版本号
    static String ID;
    //获取手机的型号 设备名称。如：SM-N9100（三星Note4）
    public static String MODEL;
    //获取设备制造商。如：samsung
    static String MANUFACTURER;
    //产品的名称
    static String PRODUCT;
    //无线电固件版本号，通常是不可用的 显示
    static String RADIO;
    //设备标签。如release-keys或测试的test-keys
    static String TAGS;
    //设备时间
    static long TIME;
    //设备版本类型主要为”user” 或”eng”
    static String TYPE;
    //设备用户名 基本上都为android-build
    static String USER;
    //获取系统版本字符串
    static String RELEASE;
    //设备当前的系统开发代号，一般使用REL代替
    static String CODENAME;
    //系统源代码控制值，一个数字或者git哈希值
    static String INCREMENTAL;
    //系统的API级别，int数值类型
    static int SDK_INT;

    static String CPU_ABI;

    static String CPU_ABI2;

    static String GOOGLE_ID;

    static String GL_RENDERER;
    static String GL_VENDOR;
    static String GL_VERSION;
    static String GL_EXTENSIONS;

    //elk上报当前设备信息
    static volatile Map<String,String> elkAndroidDevices = new HashMap<>();

    static {
        BOARD = Build.BOARD;
        BOOTLOADER = Build.BOOTLOADER;
        BRAND = Build.BRAND;
        DEVICE = Build.DEVICE;
        DISPLAY = Build.DISPLAY;
        FINGERPRINT = Build.FINGERPRINT;
        HARDWARE = Build.HARDWARE;
        HOST = Build.HOST;
        ID = Build.ID;
        MODEL = Build.MODEL;
        MANUFACTURER = Build.MANUFACTURER;
        PRODUCT = Build.PRODUCT;
        RADIO = Build.RADIO;
        TAGS = Build.TAGS;
        TIME = Build.TIME;
        TYPE = Build.TYPE;
        USER = Build.USER;
        RELEASE = Build.VERSION.RELEASE;
        CODENAME = Build.VERSION.CODENAME;
        INCREMENTAL = Build.VERSION.INCREMENTAL;
        SDK_INT = Build.VERSION.SDK_INT;

        CPU_ABI = Build.CPU_ABI;
        CPU_ABI2 = Build.CPU_ABI2;
        GOOGLE_ID = getGoogleId();
        GL_RENDERER = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_RENDERER);
        GL_VENDOR = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_VENDOR);
        GL_VERSION = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_VERSION);
        GL_EXTENSIONS = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_EXTENSIONS);
    }

    /**
    * @Desc TODO(获取当前设备信息)
    * @author 彭石林
    * @return java.util.Map<java.lang.String,java.lang.String>
    * @Date 2022/10/7
    */
    public static Map<String, String> getDeviceInfo(){
        //当前设备信息
        Map<String,Object> deviceInfo = new HashMap<>();
        deviceInfo.put("BOARD",BOARD);
        deviceInfo.put("BOOTLOADER",BOOTLOADER);
        deviceInfo.put("BRAND",BRAND);
        deviceInfo.put("DEVICE",DEVICE);
        deviceInfo.put("DISPLAY",DISPLAY);
        deviceInfo.put("FINGERPRINT",FINGERPRINT);
        deviceInfo.put("HOST",HOST);
        deviceInfo.put("ID",ID);
        deviceInfo.put("MODEL",MODEL);
        deviceInfo.put("MANUFACTURER",MANUFACTURER);
        deviceInfo.put("PRODUCT",PRODUCT);
        deviceInfo.put("RADIO",RADIO);
        deviceInfo.put("TAGS",TAGS);
        deviceInfo.put("TIME",TIME);
        deviceInfo.put("TYPE",TYPE);
        deviceInfo.put("USER",USER);
        deviceInfo.put("RELEASE",RELEASE);
        deviceInfo.put("CODENAME",CODENAME);
        deviceInfo.put("INCREMENTAL",INCREMENTAL);
        deviceInfo.put("SDK_INT",SDK_INT);
        deviceInfo.put("GOOGLE_ID",GOOGLE_ID);
        deviceInfo.put("isEmulatorAbsolute", EmulatorDetector.isEmulatorAbsolute());
        deviceInfo.put("isEmulator",EmulatorDetector.isEmulator());
        deviceInfo.put("GL_RENDERER",GL_RENDERER);
        deviceInfo.put("GL_VENDOR",GL_VENDOR);
        deviceInfo.put("GL_VERSION",GL_VERSION);
        deviceInfo.put("GL_EXTENSIONS",GL_EXTENSIONS);
        //AttributionIdentifiers.getAttributionIdentifiers(Utils.getApp()).
        //设备信息存放集合
        Map<String,String > deviceData = new HashMap<>();
        deviceData.put("cpuInfo", GsonUtils.toJson(getDeviceCpuInfo()));
        deviceData.put("deviceInfo",GsonUtils.toJson(deviceInfo));
        return deviceData;
    }

    /**
    * @Desc TODO()
    * @author 彭石林
    *  [CPUName CPU名字 CPUHardWare cpu型号]
    * @return java.util.Map<java.lang.String,java.lang.String>
    * @Date 2022/10/7
    */
    public static Map<String,String> getDeviceCpuInfo() {
        Map<String,String> deviceCpuInfo = new HashMap<>();
        deviceCpuInfo.put("CPU_ABI",CPU_ABI);
        deviceCpuInfo.put("CPU_ABI2",CPU_ABI2);
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text;
            String last = "";
            int i = 0;
            while ((text = br.readLine()) != null) {
                last = text;
                if(i==0){
                    String[] array = text.split(":\\s+", 2);
                    deviceCpuInfo.put("CPUName",array[1]);
                    i++;
                }
            }
            //一般机型的cpu型号都会在cpuinfo文件的最后一行
            if (last.contains("Hardware")) {
                String[] hardWare = last.split(":\\s+", 2);
                deviceCpuInfo.put("CPUHardWare",hardWare[1]);
            }else{
                deviceCpuInfo.put("CPUHardWare",Build.HARDWARE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceCpuInfo;
    }

    public static String getDevId(){
        return BRAND+"_"+MPDeviceUtils.MANUFACTURER+"_"+MPDeviceUtils.MODEL+"_"+MPDeviceUtils.BOARD+"_"+MPDeviceUtils.DEVICE+"_"+MPDeviceUtils.PRODUCT+"_"+GOOGLE_ID;
    }

    /* renamed from: a */
    public static String getGoogleId() {
        Uri parse = Uri.parse("content://com.google.android.gsf.gservices");
        String str = "";
        try {
            Cursor query = Utils.getApp().getContentResolver().query(parse, null, null, new String[]{"android_id"}, null);
            if (query == null) {
                return str;
            }
            if (query.moveToFirst()) {
                if (query.getColumnCount() >= 2) {
                    String string = Long.toHexString(Long.parseLong(query.getString(1)));
                    query.close();
                    if (!TextUtils.isEmpty(string)) {
                        str = string.toUpperCase().trim();
                    }
                    return str;
                }
            }
            query.close();
            return str;
        } catch (NoClassDefFoundError | NullPointerException unused) {
            return str;
        }
    }

    /**
    * @Desc TODO(ELK打点上报当前设备信息)
    * @author 彭石林
    * @parame []
    * @return java.util.Map<java.lang.String,java.lang.String>
    * @Date 2022/10/8
    */
    public static Map<String,String> getElkAndroidData() {
        if(ObjectUtils.isEmpty(elkAndroidDevices)){
            elkAndroidDevices.put("devi",MODEL);
            elkAndroidDevices.put("bd",BRAND);
            //当前专属平台
            elkAndroidDevices.put("plat","Android");
        }
        return elkAndroidDevices;
    }
}
