package com.fine.friendlycc.manager;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LocaleManager {
    public static final String dlAppLanguageLocal = "dlAppLanguageLocal";

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale(Context mContext) {
        String localeText = readLocalCache(mContext);
        if(StringUtils.isEmpty(localeText)){
            localeText = mContext.getString(R.string.playcc_local_language_val);
        }
        return new Locale(localeText);
    }

    public static void putLocalCacheApply(Context context,String local){
        // MMKVUtil.getInstance().putKeyValue(dlAppLanguageLocal,local);
        writeData(getLocalCachePath(context),local.getBytes());
        //CommSharedUtil.getInstance(AppContext.instance()).putString(dlAppLanguageLocal,local);
    }

    public static String readLocalCache(Context mContext){
        try {
            return getString(getLocalCachePath(mContext));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return CommSharedUtil.getInstance(mContext).getString(dlAppLanguageLocal);
        //return MMKVUtil.getInstance().readKeyValue(dlAppLanguageLocal);
    }


    public static Context setLocal(Context context) {
        return updateResources(context, getSystemLocale(context));
    }

    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static void onConfigurationChanged(Context context){
        setLocal(context);
    }

    //当前本地的缓存地址
    public static String getLocalCachePath(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/dlAppLanguageLocal.txt";
    }

    /**
     * 读取文件转化成1个字符串
     */
    private static String getString(String path) throws IOException {
        //转成file类型
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }
        InputStreamReader read = null;
        BufferedReader br = null;
        StringBuilder stringBuffer = new StringBuilder();
        try {
            //先读入再放入缓冲流里面按行读取
            read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            br = new BufferedReader(read);
            String readText;
            while ((readText = br.readLine()) != null) {
                stringBuffer.append(readText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(read != null){
                read.close();
            }
            if(br != null){
                br.close();
            }
        }
        return stringBuffer.length() == 0 ? null : stringBuffer.toString();
    }

    private static void deleteFile(String filePath){
        if(!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
        }
    }

    private static void writeData(String cacheFilePath,byte[] byteData){
        File file = new File(cacheFilePath);
        if (file.exists()) {
            try {
                if(file.isFile()){
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file,false));
                    bufferedOutputStream.write(byteData);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
