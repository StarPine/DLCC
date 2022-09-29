package com.dl.playfun.app;

import com.dl.playfun.data.AppRepository;
import com.dl.playfun.data.source.HttpDataSource;
import com.dl.playfun.data.source.LocalDataSource;
import com.dl.playfun.data.source.http.HttpDataSourceImpl;
import com.dl.playfun.data.source.http.RetrofitClient;
import com.dl.playfun.data.source.http.service.ApiService;
import com.dl.playfun.data.source.local.LocalDataSourceImpl;

/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 *
 * @author goldze
 * @date 2019/3/26
 */
public class Injection {
    public static AppRepository provideDemoRepository() {
        //网络API服务
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return AppRepository.getInstance(httpDataSource, localDataSource);
    }
}
