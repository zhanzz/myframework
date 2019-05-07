package com.framework.common.utils;

import com.framework.common.data.Result;
import com.framework.common.net.UpLoadFileType;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author zhangzhiqiang
 * @date 2019/4/20.
 * description：
 */
public interface BaseApi {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String   fileUrl);

    @UpLoadFileType
    @POST
    Observable<ResponseBody> uploadFile(@Url String  fileUrl, @Body Map<String,Object> requestBody);
}
