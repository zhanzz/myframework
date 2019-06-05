package com.framework.common.retrofit;

import com.framework.common.BuildConfig;
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

    public static <T>  T getMallRetorfitApi(Class<T> tClass){
        return NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_MALL_HOST)
                .create(tClass);
    }
}
