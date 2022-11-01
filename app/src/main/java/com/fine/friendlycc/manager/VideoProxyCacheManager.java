package com.fine.friendlycc.manager;

import com.danikula.videocache.HttpProxyCacheServer;
import com.fine.friendlycc.app.AppContext;

/**
 * @author litchi
 */
public class VideoProxyCacheManager {

    private static VideoProxyCacheManager mManager;

    private final HttpProxyCacheServer proxy;

    private VideoProxyCacheManager() {
        proxy = new HttpProxyCacheServer.Builder(AppContext.instance())
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(100)
                .build();
    }

    public static VideoProxyCacheManager getInstance() {
        if (mManager == null) {
            synchronized (ConfigManager.class) {
                if (mManager == null) {
                    mManager = new VideoProxyCacheManager();
                }
            }
        }
        return mManager;
    }

    public HttpProxyCacheServer getProxy() {
        return proxy;
    }
}
