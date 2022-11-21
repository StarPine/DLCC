package com.dl.lib.elk.http.impl;

import com.dl.lib.elk.http.ElkApiService;
import com.dl.lib.elk.http.ElkHttpDataSource;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Author: 彭石林
 * Time: 2022/9/29 18:45
 * Description: This is ElkHttpDataSourceImpl
 */
public class ElkHttpDataSourceImpl implements ElkHttpDataSource {

    private volatile static ElkHttpDataSourceImpl INSTANCE = null;
    private final ElkApiService apiService;

    private ElkHttpDataSourceImpl(ElkApiService apiService) {
        this.apiService = apiService;
    }

    public static ElkHttpDataSourceImpl getInstance(ElkApiService apiService) {
        if (INSTANCE == null) {
            synchronized (ElkHttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ElkHttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<ResponseBody> postSendLogEvent(RequestBody requestBody) {
        return apiService.postSendLogEvent(requestBody);
    }
}
