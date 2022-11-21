package com.dl.lib.util.emulator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p>
 * Copyright (C) 2013, Vladislav Gingo Skoumal (http://www.skoumal.net)
 * 在原Repo基础上增加了一些判定
 */
public class EmulatorDetector {

    private static final String TAG = EmulatorDetector.class.getSimpleName();

    private static int rating = -1;

    public static boolean isEmulatorAbsolute() {
        if (Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("sdk_x86") ||
                Build.PRODUCT.contains("sdk_google") ||
                Build.PRODUCT.contains("Andy") ||
                Build.PRODUCT.contains("Droid4X") ||
                Build.PRODUCT.contains("nox") ||
                Build.PRODUCT.contains("vbox86p")) {
            return true;
        }
        if (Build.MANUFACTURER.equals("Genymotion") ||
                Build.MANUFACTURER.contains("Andy") ||
                Build.MANUFACTURER.contains("nox") ||
                Build.MANUFACTURER.contains("TiantianVM")) {
            return true;
        }
        if (Build.BRAND.contains("Andy")) {
            return true;
        }
        if (Build.DEVICE.contains("Andy") ||
                Build.DEVICE.contains("Droid4X") ||
                Build.DEVICE.contains("nox") ||
                Build.DEVICE.contains("vbox86p")) {
            return true;
        }
        if (Build.MODEL.contains("Emulator") ||
                Build.MODEL.equals("google_sdk") ||
                Build.MODEL.contains("Droid4X") ||
                Build.MODEL.contains("TiantianVM") ||
                Build.MODEL.contains("Andy") ||
                Build.MODEL.equals("Android SDK built for x86_64") ||
                Build.MODEL.equals("Android SDK built for x86")) {
            return true;
        }
        if (Build.HARDWARE.equals("vbox86") ||
                Build.HARDWARE.contains("nox") ||
                Build.HARDWARE.contains("ttVM_x86")) {
            return true;
        }

        return Build.FINGERPRINT.contains("generic/sdk/generic") ||
                Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
                Build.FINGERPRINT.contains("Andy") ||
                Build.FINGERPRINT.contains("ttVM_Hdragon") ||
                Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
                Build.FINGERPRINT.contains("vbox86p") ||
                Build.FINGERPRINT.contains("generic/vbox86p/vbox86p");
    }

    /**
     * Detects if app is currenly running on emulator, or real device.
     *
     * @return true for emulator, false for real devices
     */
    public static int isEmulator() {
        if (isEmulatorAbsolute()) {
            return -1;
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

            //夜神模拟器
            if (checkIsNotRealPhone()) {
                newRating += 10;
            }

            //BlueStacks 模拟器
            Sensor sensor = getLightSensor();
            if (sensor != null && sensor.toString().contains("BlueStacks")) {
                newRating += 10;
            }

            rating = newRating;
        }
        //return rating > 3;//不能再少了，否则有可能误判，若增减了新的嫌疑度判定属性，要重新评估该值
        return rating;//不能再少了，否则有可能误判，若增减了新的嫌疑度判定属性，要重新评估该值
    }

    public static Sensor getLightSensor() {
        SensorManager sensorManager = (SensorManager) Utils.getApp().getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    /**
     * 根据CPU是否为电脑来判断是否为模拟器
     * 返回:true 为模拟器
     */
    public static boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        return cpuInfo.contains("intel") || cpuInfo.contains("amd") || TextUtils.isEmpty(cpuInfo);
    }

    /**
     * 根据CPU是否为电脑来判断是否为模拟器(子方法)
     * 返回:String  模拟器的是空的，真机是有值的
     */
    public static String readCpuInfo() {
        String result;
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuilder sb = new StringBuilder();
            String readLine;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "default_real"; //默认是真手机
        }
        return result;
    }


    /**
     * Returns string with human-readable listing of Build.* parameters used in {@link #isEmulator()} method.
     *
     * @return all involved Build.* parameters and its values
     */
    public static String getDeviceListing() {
        return "Build.PRODUCT: " + Build.PRODUCT + "\n" +
                "Build.MANUFACTURER: " + Build.MANUFACTURER + "\n" +
                "Build.BRAND: " + Build.BRAND + "\n" +
                "Build.DEVICE: " + Build.DEVICE + "\n" +
                "Build.MODEL: " + Build.MODEL + "\n" +
                "Build.HARDWARE: " + Build.HARDWARE + "\n" +
                "Build.FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Build.TAGS: " + Build.TAGS + "\n" +
                "GL_RENDERER: " + android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_RENDERER) + "\n" +
                "GL_VENDOR: " + android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_VENDOR) + "\n" +
                "GL_VERSION: " + android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_VERSION) + "\n" +
                "GL_EXTENSIONS: " + android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_EXTENSIONS) + "\n";
    }
}