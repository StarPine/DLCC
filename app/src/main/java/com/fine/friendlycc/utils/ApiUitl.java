package com.fine.friendlycc.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.fine.friendlycc.bean.AddressBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @ClassName ApiUitl
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/7 16:12
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class ApiUitl {
    private static List<Field> sMetricsFields;
    public static boolean isShow = false;
    public static boolean issendMessageTag = false;
    public static AddressBean $address = null;

    //刷新签到天数
    public static boolean taskRefresh = false;
    //移动到任务中心下面
    public static boolean taskBottom = false;
    //移动到任务中心顶部
    public static boolean taskTop = false;
    //跳转到福袋页面
    public static boolean taskDisplay = false;

    /**
    * @Desc TODO(Base64加密)
    * @author 彭石林
    * @parame [value]
    * @return java.lang.String
    * @Date 2022/2/12
    */
    public static String Base64Encode(String value){
        return new String(Base64.encode(value.getBytes(),Base64.DEFAULT));
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * @return okhttp3.RequestBody
     * @Desc TODO(封装请求体转化为RequestBody)
     * @author 彭石林
     * @parame [body]
     * @Date 2022/1/14
     */
    public static RequestBody getBody(String body) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
    }

    /**
     * 取出a标签的href值
     *
     * @param str
     * @return
     */
    public static String getRegHref(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("href='(.+?)'");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            if (matcher.group() == null) {
                return null;
            }
            return matcher.group(1);
        }
        return null;
    }

    //获取设备序列号
    public static String getSerialNumber() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        // API >= 9 的设备才有 android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // 如果用户更新了系统或 root 了他们的设备，该 API 将会产生重复记录
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }

        // 最后，组合上述值并生成 UUID 作为唯一 ID
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        //Timber.v("isCameraCanuse="+canUse);
        return canUse;
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

    public static String getAndroidId() {
        String android = "";
        String google = "";
        try {
            StringBuffer sbuffer = new StringBuffer();
            sbuffer.append(DeviceUtils.getManufacturer() + "@@")
                    .append(DeviceUtils.getModel() + "@@")
                    .append(DeviceUtils.getSDKVersionName() + "@@")
                    .append(DeviceUtils.getMacAddress());
            android = sbuffer.toString();
        } catch (Exception e) {

        }
        try {
            google = getGoogleId();
        } catch (Exception e) {
        }

        return google + "&" + android;
    }

    /**
     * 根据传入的日期减去两天
     *
     * @param date
     * @return
     */
    public static Date toDayMinTwo(Date date) {
        Calendar calendar = Calendar.getInstance();
        //减2天
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        return calendar.getTime();
    }

    /**
     * Description: 判断一个时间是否在一个时间段内 </br>
     *
     * @param nowTime   当前时间 </br>
     * @param beginTime 开始时间 </br>
     * @param endTime   结束时间 </br>
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        return beginTime.getTime() <= nowTime.getTime() && nowTime.getTime() <= endTime.getTime();
    }

    /**
     * @return java.lang.String
     * @Desc TODO(获取Assets目录下的文件)
     * @author 彭石林
     * @parame [context, fileName]
     * @Date 2021/8/13
     */
    public static String getAssetsJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {

        }
        return stringBuilder.toString();
    }

    /**
     * @return java.util.List<T>
     * @Desc TODO(将字符串转成List集合)
     * @author 彭石林
     * @parame [jsonString, cls]
     * @Date 2021/8/13
     */
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public static void saveBitmap(Context context, Bitmap bitmap, String bitName, CallBackUploadFileNameCallback callBackUploadFileNameCallback) {
        String fileName;
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fileName = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + bitName;
        } else {
            if (Build.BRAND.equals("xiaomi")) { // 小米手机
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
            } else if (Build.BRAND.equals("Huawei")) {
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
            } else {  // Meizu 、Oppo
                fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
            }
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
                Log.e("当前图片路径", file.getPath());
                if (callBackUploadFileNameCallback != null) {
                    callBackUploadFileNameCallback.success(fileName);
                }
                // 插入图库
                //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public static void saveBitmap(Bitmap bitmap, String fileName, SaveBitmapListener saveBitmapListener) {
        File file;
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
                saveBitmapListener.saveCallback(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveBitmapListener.saveCallback(false);
        }
    }

    //获取当前时间的缩写名
    public static String getDateTimeFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(Long.parseLong(System.currentTimeMillis() + ""));
        return sdf.format(date);
    }

    /**
     * @param ctx
     * @return
     */
    public static String getDiskCacheDir(Context ctx) {
        File filesDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (filesDir == null) {
            return "";
        }
        return filesDir.getPath();
    }

    public interface CallBackUploadFileNameCallback {
        void success(String fileName);
    }

    public interface SaveBitmapListener {
        void saveCallback(boolean flag);
    }
}