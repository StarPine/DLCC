package com.dl.lib.util;

import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Author: 彭石林
 * Time: 2022/9/26 18:21
 * Description: This is ElkFileUtil
 */
public class ElkFileUtil {

    /**
     * 读取文件转化成1个字符串
     */
    public static String getString(String path) throws IOException {
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

    public static void deleteFile(String filePath){
        if(!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
        }
    }

    public static void writeData(String cacheFilePath,byte[] byteData){
        File file = new File(cacheFilePath);
        if (!file.exists()) {
            try {
                if(file.createNewFile()){
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

