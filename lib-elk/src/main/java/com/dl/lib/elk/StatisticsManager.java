package com.dl.lib.elk;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dl.lib.elk.http.ElkApiRepository;
import com.dl.lib.elk.http.ElkHttpDataSource;
import com.dl.lib.elk.log.AppLogEntity;
import com.dl.lib.elk.log.AppLogStoreHelper;
import com.dl.lib.util.GZIPUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class StatisticsManager {

    private IStatisticsConfig mStatisticsConfig;
    private ElkHttpDataSource elkApiRepository;

    private StatisticsManager() {
    }

    private static class SingletonHolder {
        private static final StatisticsManager INSTANCE = new StatisticsManager();
    }

    public static StatisticsManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setStatisticsConfig(IStatisticsConfig statisticsConfig) {
        mStatisticsConfig = statisticsConfig;
    }

    public IStatisticsConfig getStatisticsConfig() {
        return mStatisticsConfig;
    }

    public void sendStatistics(String statisticsString) {

        String postString = "lt=sx`" + "\n" + statisticsString;
        try {
            getOkHttpClient().postSendLogEvent(getTextBody(GZIPUtils.compress(postString)))
                .subscribeOn(Schedulers.io()).subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            if (responseBody != null) {
                                String responseString = responseBody.string();
                                boolean success = false;
                                JSONObject jsonObject = null;
                                if (!StringUtils.isTrimEmpty(responseString)) {
                                    jsonObject = new JSONObject(responseString);
                                    if (jsonObject.has("retcode")) {
                                        int retcode = jsonObject.optInt("retcode");
                                        success = retcode == 0;
                                    }
                                }

                                if (success) {
                                    // 发送成功后尝试发送之前失败的日志
                                    sendLocalCacheStatistics(AppLogStoreHelper.getInstance().pollListAppLogEntity());
                                } else {
                                    // 发送失败后缓存日志
                                    AppLogStoreHelper.getInstance().cacheAppLogEntity(new AppLogEntity(statisticsString));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                        @Override
                public void onError(Throwable e) {
                        Log.e("ELK当前请求异常信息",e.getMessage());
                    // 发送失败后缓存日志
                    AppLogStoreHelper.getInstance().cacheAppLogEntity(new AppLogEntity(statisticsString));
                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public void sendLocalCacheStatistics(final List<AppLogEntity> cacheList){
        if(ObjectUtils.isEmpty(cacheList)){
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("lt=sx`\n");

        for(AppLogEntity cacheEntity : cacheList){
            stringBuilder.append(cacheEntity.statisticsString).append("\n");
        }
        String postString = stringBuilder.toString();
        try {
            getOkHttpClient().postSendLogEvent(getTextBody(GZIPUtils.compress(postString)))
                    .subscribeOn(Schedulers.io()).subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                if (responseBody != null) {
                                    String responseString = responseBody.string();
                                    boolean success = false;
                                    JSONObject jsonObject = null;
                                    if (!StringUtils.isTrimEmpty(responseString)) {
                                        jsonObject = new JSONObject(responseString);
                                        if (jsonObject.has("retcode")) {
                                            int retcode = jsonObject.optInt("retcode");
                                            success = retcode == 0;
                                        }
                                    }

                                    if (success) {
                                        // 发送成功后尝试发送之前失败的日志
                                        AppLogEntity entity = AppLogStoreHelper.getInstance().pollAppLogEntity();
                                        if (entity != null) {
                                            sendStatistics(entity.statisticsString);
                                        }
                                    } else {
                                        // 发送失败后缓存日志
                                        AppLogStoreHelper.getInstance().cacheListAppLogEntity(cacheList);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e("ELK当前请求异常信息",e.getMessage());
                            // 发送失败后缓存日志
                            AppLogStoreHelper.getInstance().cacheListAppLogEntity(cacheList);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * @return okhttp3.RequestBody
     * @Desc TODO(封装请求体转化为RequestBody)
     * @author 彭石林
     * @parame [body]
     * @Date 2022/1/14
     */
    public static RequestBody getTextBody(String dataBody) {
        return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),dataBody);
    }

    /**
     * @return okhttp3.RequestBody
     * @Desc TODO(封装请求体转化为RequestBody)
     * @author 彭石林
     * @parame [body]
     * @Date 2022/1/14
     */
    public static RequestBody getTextBody(byte[] dataBody) {
        return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),dataBody);
    }

    public ElkHttpDataSource getOkHttpClient() {
        if (elkApiRepository == null) {
            synchronized (StatisticsManager.class){
                elkApiRepository = ElkApiRepository.getInstance();
            }
        }
        return elkApiRepository;

    }

    public interface AppLogCallback {
        void success(String responseString);

        void failure(Call call, IOException e);
    }
}
