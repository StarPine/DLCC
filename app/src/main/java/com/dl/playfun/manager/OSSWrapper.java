package com.dl.playfun.manager;


import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.dl.playfun.app.AppConfig;
import com.dl.playfun.app.AppContext;
import com.dl.playfun.data.AppRepository;

public class OSSWrapper {

    private static final OSSWrapper WRAPPER = new OSSWrapper();
    private OSSClient mClient = null;

    private OSSWrapper() {
        AppRepository appRepository = ConfigManager.getInstance().getAppRepository();
        OSSAuthCredentialsProvider authCredentialsProvider = new OSSAuthCredentialsProvider(appRepository.readApiConfigManagerEntity().getPlayFunApiUrl()+AppConfig.STS_SERVER_URL);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
        conf.setHttpDnsEnable(true);//设置是否开启DNS配置 默认为true 表开启，需要关闭可以设置为false。

        // SDCard_path\OSSLog\logs.csv，高版本系统无权限
        // OSSLog.enableLog();

        mClient = new OSSClient(AppContext.instance(), AppConfig.OSS_ENDPOINT, authCredentialsProvider, conf);
    }

    public static OSSWrapper sharedWrapper() {
        return WRAPPER;
    }

    public OSSClient getClient() {
        return mClient;
    }
}