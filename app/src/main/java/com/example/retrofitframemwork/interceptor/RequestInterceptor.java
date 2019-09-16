package com.example.retrofitframemwork.interceptor;

import android.text.TextUtils;

import com.framework.common.data.operation.UserOperation;
import com.framework.common.BuildConfig;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * 请求拦截器，修改请求header
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("terminalId", "006")
                .addHeader("app_key","android")
                .addHeader("os","android")
                .addHeader("user-agent","android")
                .addHeader("version", String.valueOf(BuildConfig.VERSION_CODE))
                .addHeader("Authorization", TextUtils.isEmpty(UserOperation.getInstance().getToken()) ? "" : UserOperation.getInstance().getToken())
                .build();
        return chain.proceed(request);
    }
}