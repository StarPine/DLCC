package com.dl.lib.elk.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author: 彭石林
 * Time: 2022/9/29 15:36
 * Description: This is ElkService
 */
public interface ElkApiService {
    /**
    * @Desc TODO(上报elk-ljs日志打点)
    * @author 彭石林
    * @parame [requestBody]
    * @return io.reactivex.rxjava3.core.Observable<java.util.Map>
    * @Date 2022/9/29
    */
    @POST("glean?chk=1a5663ff&zip=gzip&vno=1-1664434391333&uuid=&app=00648133339a")
    @Headers({"Content-Encoding:gzip"})
    Observable<ResponseBody> postSendLogEvent(@Body RequestBody requestBody);
}
