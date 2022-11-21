package com.dl.lib.elk.http;

import com.dl.lib.elk.ElkRetrofitClient;
import com.dl.lib.elk.http.impl.ElkHttpDataSourceImpl;

/**
 * Author: 彭石林
 * Time: 2022/9/29 18:48
 * Description: This is ElkApiRepository
 */
public class ElkApiRepository {
    /**
    * @Desc TODO(获取操作数据仓库)
    * @author 彭石林
    * @parame []
    * @return com.dl.lib.elk.http.ElkHttpDataSource
    * @Date 2022/9/29
    */
    public static ElkHttpDataSource getInstance() {
        //网络API服务
        ElkApiService apiService = ElkRetrofitClient.getInstance().create(ElkApiService.class);
        //两条分支组成一个数据仓库
        return ElkHttpDataSourceImpl.getInstance(apiService);
    }
}
