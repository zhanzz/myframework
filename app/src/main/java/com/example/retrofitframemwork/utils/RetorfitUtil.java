package com.example.retrofitframemwork.utils;

import com.example.retrofitframemwork.AppApi;
import com.example.retrofitframemwork.BuildConfig;
import com.framework.common.manager.NetWorkManager;

/**
 * @author zhangzhiqiang
 * @date 2019/4/18.
 * description：
 */
public class RetorfitUtil {
    public static <T>  T getRetorfitApi(Class<T> tClass){
        return NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST)
                .create(tClass);
    }

    public static AppApi getRetorfitApi(){
        return NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST)
                .create(AppApi.class);
    }
}
