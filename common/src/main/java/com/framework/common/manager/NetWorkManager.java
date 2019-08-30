package com.framework.common.manager;

import com.framework.common.BuildConfig;
import com.framework.common.convert.FastJsonConverterFactory;
import com.framework.common.convert.FileConverterFactory;
import com.framework.common.net.HttpsUtils;
import com.framework.common.net.LoggerInterceptor;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
/**
 * Created by Zaifeng on 2018/2/28.
 * API初始化类
 */
public class NetWorkManager{

    private static NetWorkManager mInstance;
    private static Map<String,Retrofit> retrofitMap=new HashMap<>();
    private static OkHttpClient okHttpClient;
    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    public static OkHttpClient getOkHttpClient() {
        if(okHttpClient==null){
            NetWorkManager.getInstance().init();
        }
        return okHttpClient;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init(Interceptor ...interceptors) {
        boolean debug = BuildConfig.DEBUG_ENVIRONMENT;
        // 初始化okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)      //设置连接超时
                .readTimeout(30, TimeUnit.SECONDS)         //设置读超时
                .writeTimeout(30, TimeUnit.SECONDS)        //设置写超时
                .retryOnConnectionFailure(true)            //是否自动重连
                // 设置https配置，此处忽略了所有证书
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HttpsUtils.UnSafeHostnameVerifier());
        if(interceptors!=null&&interceptors.length>0){
            for(Interceptor interceptor:interceptors){
                builder.addInterceptor(interceptor);
            }
        }
        if(!debug){
            builder.proxy(Proxy.NO_PROXY);//不使用代理
        }else{
            builder.addInterceptor(new LoggerInterceptor(true));
        }
        okHttpClient = builder.build();
    }

    public Retrofit getRetorfit(String hostName){
        // 初始化Retrofit
        Retrofit retrofit = retrofitMap.get(hostName);
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(hostName)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//创建同步的转换工厂
                    .addConverterFactory(FileConverterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
            retrofitMap.put(hostName,retrofit);
        }
        return retrofit;
    }
}