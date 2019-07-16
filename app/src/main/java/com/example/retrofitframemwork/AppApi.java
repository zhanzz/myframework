package com.example.retrofitframemwork;

import com.framework.common.data.Result;
import com.framework.model.TestBean;
import com.framework.model.UserEntity;
import com.framework.model.VersionInfo;

import java.util.Map;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author zhangzhiqiang
 * @date 2019/4/15.
 * description：
 */
public interface AppApi {
    @POST("v2/shop/login")
    Call<String> login(@Body Map<String,Object> params);

    @POST("v2/shop/login")
    Observable<String> loginByRxJava(@Body Map<String,Object> params);

    @POST("v1/login")
    Observable<Result<UserEntity>> loginByResponse(@Body Map<String,Object> params);

    @POST("v1/Attendance/UserShiftDetails")
    Observable<Result<TestBean>> loadPageData(@Body Map<String,Object> params);

    @POST("https://app.zhidianlife.com/life-app-apis/apis/v1/mall/version/queryLatestVersion")
    Observable<Result<VersionInfo>> checkUpdate();
}
