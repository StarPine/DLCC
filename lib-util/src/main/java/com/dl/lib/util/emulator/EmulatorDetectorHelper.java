package com.dl.lib.util.emulator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmulatorDetectorHelper {
    private static final String[] PKG_NAMES = {
            "com.mumu.launcher", //网易mumu

            "com.ami.duosupdater.ui",
            "com.ami.launchmetro",
            "com.ami.syncduosservices", //ami

            "com.bluestacks.home",
            "com.bluestacks.windowsfilemanager",
            "com.bluestacks.settings",
            "com.bluestacks.bluestackslocationprovider",
            "com.bluestacks.appsettings",
            "com.bluestacks.bstfolder",
            "com.bluestacks.BstCommandProcessor",
            "com.bluestacks.s2p",
            "com.bluestacks.setup",
            "com.bluestacks.appmart", //蓝叠

            "com.tiantian.ime",
            "com.kaopu001.tiantianserver",
            "com.kaopu001.tiantianime", // 天天

            "com.kpzs.helpercenter", //靠谱助手

            "com.genymotion.superuser",
            "com.genymotion.clipboardproxy", //genymotion

            "com.uc.xxzs.keyboard",
            "com.uc.xxzs",    //uc

            "com.blue.huang17.agent",
            "com.blue.huang17.launcher",
            "com.blue.huang17.ime",  //blue

            "com.microvirt.launcher",
            "com.microvirt.guide",
            "com.microvirt.market",
            "com.microvirt.memuime", //逍遥

            "cn.itools.vm.launcher",
            "cn.itools.vm.proxy",
            "cn.itools.vm.softkeyboard",
            "cn.itools.avdmarket",  //itools

            "com.syd.IME", //手游岛

            "com.bignox.app.store.hd",
            "com.bignox.launcher",
            "com.bignox.app.phone",
            "com.bignox.app.noxservice",  //夜神
            "com.android.noxpush",

            "com.haimawan.push", //海马玩
            "me.haima.helpcenter",

            "com.windroy.launcher",
            "com.windroy.superuser",
            "com.windroy.launcher",
            "com.windroy.ime", //windroy

            "com.android.flysilkworm", //雷电

            "com.android.emu.inputservice", //emu

            "me.le8.androidassist", //le8

            "com.vphone.helper",
            "com.vphone.launcher", //vphone

            "com.duoyi.giftcenter.giftcenter" //多益
    };

    private static final String[] PATHS = { "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props",
            "/dev/socket/qemud",
            "/dev/qemu_pipe",
            "/dev/socket/baseband_genyd",
            "/dev/socket/genyd"
    };

    private static final String[] FILES = {"/data/data/com.android.flysilkworm", "/data/data/com.bluestacks.filemanager"};

    private static DetectorResult detectorResult = null;

    /**
     * 检测函数
     * @param paramContext
     * @return
     */
    public static  DetectorResult startDetector(Context paramContext){
        if(detectorResult != null) {
            return detectorResult;
        }

        try {
            List pathList;
            //先检测安装的应用包名
            pathList = getInstalledSimulatorPackages(paramContext);

            //再检测特征文件
            if (pathList.size() == 0) {
                for (int i = 0; i < PATHS.length; i++) {
                    if (!new File(PATHS[i]).exists()) continue;
                        pathList.add(PATHS[i]);
                }
            }

            //蓝叠模拟器比较特殊，增加此逻辑
            if (pathList.size() == 0) {
                pathList = loadApps(paramContext);
            }

            detectorResult = new DetectorResult();
            detectorResult.isSimulator = (pathList.size() == 0 ? null : pathList.toString()) != null;
            detectorResult.path = pathList.size() > 0 ? String.valueOf(pathList.get(0)) : "";
            detectorResult.simulatorName = getSimulatorBrand(pathList);
            return detectorResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detectorResult;

    }

    private static List getInstalledSimulatorPackages(Context context) {
        ArrayList localArrayList = new ArrayList();
        try {
            for (String pkgName : PKG_NAMES)
                try {
                    PackageInfo info = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
                    if ((info != null) && (info.applicationInfo != null) &&
                            ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)) {//系统应用
                        localArrayList.add(pkgName);
                    }
                } catch (PackageManager.NameNotFoundException ignored) {
                }
            if (localArrayList.size() == 0) {
                for (String file : FILES) {
                    if (new File(file).exists())
                        localArrayList.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localArrayList;
    }

    private static List loadApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<String> list = new ArrayList<>();
        try {
            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent, 0);
            //for循环遍历ResolveInfo对象获取包名和类名
            for (int i = 0; i < apps.size(); i++) {
                ResolveInfo info = apps.get(i);
                String packageName = info.activityInfo.packageName;
                if (!TextUtils.isEmpty(packageName)) {
                    if (packageName.contains("bluestacks")) {
                        list.add("蓝叠");
                        return list;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private static String getSimulatorBrand(List<String> list) {
        if (list.size() == 0)
            return "";
        String pkgName = list.get(0);
        if (pkgName.contains("mumu")) {
            return "mumu";
        } else if (pkgName.contains("ami")) {
            return "AMIDuOS";
        } else if (pkgName.contains("bluestacks")) {
            return "蓝叠";
        } else if (pkgName.contains("kaopu001") || pkgName.contains("tiantian")) {
            return "天天";
        } else if (pkgName.contains("kpzs")) {
            return "靠谱助手";
        } else if (pkgName.contains("genymotion")) {
            if (Build.MODEL.contains("iTools")) {
                return "iTools";
            } else if ((Build.MODEL.contains("ChangWan"))) {
                return "畅玩";
            } else {
                return "genymotion";
            }
        } else if (pkgName.contains("uc")) {
            return "uc";
        } else if (pkgName.contains("blue")) {
            return "blue";
        } else if (pkgName.contains("microvirt")) {
            return "逍遥";
        } else if (pkgName.contains("itools")) {
            return "itools";
        } else if (pkgName.contains("syd")) {
            return "手游岛";
        } else if (pkgName.contains("bignox")) {
            return "夜神";
        } else if (pkgName.contains("haimawan")) {
            return "海马玩";
        } else if (pkgName.contains("windroy")) {
            return "windroy";
        } else if (pkgName.contains("flysilkworm")) {
            return "雷电";
        } else if (pkgName.contains("emu")) {
            return "emu";
        } else if (pkgName.contains("le8")) {
            return "le8";
        } else if (pkgName.contains("vphone")) {
            return "vphone";
        } else if (pkgName.contains("duoyi")) {
            return "多益";
        }
        return "";
    }

    public static class DetectorResult implements Serializable {
        public boolean isSimulator;
        public String path;
        public String simulatorName;
    }
}
