package com.dl.lib.elk.http;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

/**
 * Author: 彭石林
 * Time: 2022/9/29 18:44
 * Description: This is ElkHttpDataSource
 */
public interface ElkHttpDataSource {
    /**
     * @Desc TODO(上报elk-ljs日志打点)
     * @author 彭石林
     * @parame [requestBody]
     * @return io.reactivex.rxjava3.core.Observable<java.util.Map>
     * @Date 2022/9/29
     */
    Observable<ResponseBody> postSendLogEvent(@Body RequestBody requestBody);
}
