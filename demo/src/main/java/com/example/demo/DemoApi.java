package com.example.demo;

import com.framework.common.data.Result;
import com.framework.model.demo.ActivityBean;
import com.framework.model.demo.PresellBean;
import com.framework.model.demo.ProductBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author zhangzhiqiang
 * @date 2019/5/24.
 * description：
 */
public interface DemoApi {
    @POST("promotion/api/mobile/v1/promotion/preOrderProducts")
    Observable<Result<PresellBean>> loadCategoryData(@Body Map<String,Object> params);

    @GET("https://img2.zhidianlife.com/zos/20181206095202.json")
    Observable<Result<ActivityBean>> getHomeData();

    @POST("https://app.zhidianlife.com/life-mobile-mall/apis/v2/index/products")
    Observable<Result<List<ProductBean>>> getHomeProducts(@Body Map<String,String> params);
}
