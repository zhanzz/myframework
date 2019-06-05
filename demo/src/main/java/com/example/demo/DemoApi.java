package com.example.demo;

import com.framework.common.data.Result;
import com.framework.model.demo.PresellBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public interface DemoApi {

    @POST("promotion/api/mobile/v1/promotion/preOrderProducts")
    Observable<Result<PresellBean>> loadCategoryData(@Body Map<String,Object> params);
}
