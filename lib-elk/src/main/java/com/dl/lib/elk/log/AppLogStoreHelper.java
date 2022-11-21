package com.dl.lib.elk.log;

import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.dl.lib.elk.ElkFileUtil;
import com.dl.lib.util.MPThreadManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 未发送日志的缓存管理类
 */
public class AppLogStoreHelper {
    private static final String CACHE_FILE = "/AppElKLogStore.txt";

    //线程安全队列，它采用先进先出的规则
    private final ConcurrentLinkedQueue<AppLogEntity> cacheList = new ConcurrentLinkedQueue<>();
    //当前本地的缓存地址
    public String getLocalCachePath(){
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + CACHE_FILE;
    }

    private boolean isSaving = false;
    private long lastUpdateTime = 0;

    private boolean appAlive = true;

    private AppLogStoreHelper() {
        appAlive = true;
        loadFromFile();
    }

    public static AppLogStoreHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void loadFromFile() {
        MPThreadManager.CachedThreadPool.runCachedThreadPool(() -> {
            String cacheFilePath = null;
            try {
                Context context = Utils.getApp();
                if (context != null ) {
                    cacheFilePath = getLocalCachePath();
                    String cacheJson = ElkFileUtil.getString(cacheFilePath);
                    List<AppLogEntity> list = new Gson().fromJson(cacheJson,
                            new TypeToken<List<AppLogEntity>>() {
                            }.getType());
                    if (list != null && list.size() > 0) {
                        cacheList.addAll(list);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ElkFileUtil.deleteFile(cacheFilePath);
            }
        });
    }

    private void updateStoreFile() {
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime > 60 * 1000L) {
            lastUpdateTime = now;
            doUpdateStoreFile();
        }
    }

    private void doUpdateStoreFile() {
        if (isSaving) {
            return;
        }
        isSaving = true;
        MPThreadManager.CachedThreadPool.runCachedThreadPool(() -> {
            try {
                Context context = Utils.getApp();
                if (context != null ) {
                    List<AppLogEntity> list = new ArrayList<>(cacheList);
                    String cacheFilePath = getLocalCachePath();
                    String cacheJson = new Gson().toJson(list);
                    ElkFileUtil.writeData(cacheFilePath, cacheJson.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            isSaving = false;
        });
    }

    public synchronized AppLogEntity pollAppLogEntity() {
        AppLogEntity entity = null;
        if (appAlive) {
            entity = cacheList.poll();
            if (entity != null) {
                updateStoreFile();
            }
        }
        return entity;
    }
    //取出所有缓存文件
    public synchronized List<AppLogEntity> pollListAppLogEntity(){
        List<AppLogEntity> listData = new ArrayList<>();
        if (appAlive) {
            listData.addAll(cacheList);
            cacheList.clear();
        }
        return listData;
    }

    public synchronized void cacheListAppLogEntity(List<AppLogEntity> entity) {
        if (appAlive && entity != null) {
            cacheList.addAll(entity);
            updateStoreFile();
        }
    }



    public synchronized void cacheAppLogEntity(AppLogEntity entity) {
        if (appAlive && entity != null) {
            cacheList.add(entity);
            updateStoreFile();
        }
    }

    public void onAppExit() {
        appAlive = false;
        doUpdateStoreFile();
    }

    private static class SingletonHolder {
        private static final AppLogStoreHelper INSTANCE = new AppLogStoreHelper();
    }
}
