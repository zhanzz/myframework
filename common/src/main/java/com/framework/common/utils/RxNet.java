package com.framework.common.utils;

import android.graphics.Bitmap;

import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.base_mvp.IBaseView;
import com.framework.common.callBack.FileCallBack;
import com.framework.common.callBack.FileUploadCallBack;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.data.Result;
import com.framework.common.exception.ApiException;
import com.framework.common.image_util.Compressor;
import com.framework.common.manager.CacheDirManager;
import com.framework.common.callBack.LoadCallBack;
import com.framework.common.manager.NetWorkManager;
import com.framework.common.net.UploadOnSubscribe;
import com.framework.common.retrofit.ApiSubscriber;
import com.framework.common.retrofit.SchedulerProvider;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author zhangzhiqiang
 * @date 2019/4/18.
 * description：
 */
public class RxNet {

    /**
     * 一般请求，返回数据带有body
     */
    public static <T> Disposable request(Observable<Result<T>> observable, final IBaseView view, final boolean isShowLoading, final RxNetCallBack<T> callBack) {
        if (isShowLoading) {
            view.showLoading();
        }
        ApiSubscriber apiSubscriber = new ApiSubscriber<Result<T>, T>() {
            @Override
            protected void onFail(ApiException ex) {
                if (isShowLoading) {
                    view.hideLoading();
                }
                if (callBack != null) {
                    callBack.onFailure(ex.getCode(), ex.getDisplayMessage());
                }
            }

            @Override
            public void onSuccess(T data, int code, String msg) {
                if (isShowLoading) {
                    view.hideLoading();
                }
                if (callBack != null) {
                    callBack.onSuccess(data, code, msg);
                }
            }
        };
        observable.compose(view.<Result<T>>bindUntilEvent(ActivityLifeCycleEvent.DESTROY))
                .compose(SchedulerProvider.getInstance().<Result<T>>applySchedulers())
                .subscribe(apiSubscriber);
        return apiSubscriber;
    }

    /**
     * 下载和上传
     */

    public static Disposable downLoadFile(String url, final FileCallBack callBack) {
        return NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST).create(BaseApi.class)
                .downloadFile(url)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (callBack != null) {
                            callBack.saveFile(responseBody);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (callBack != null) {
                            callBack.onFail(throwable);
                        }
                    }
                });
    }

    /**
     * 表单上传文件
     * @param params
     */
    public static <T> Disposable uploadFile(final String url, Map<String, Object> params, final FileUploadCallBack<T> callBack) {
        if (params == null || params.size() <= 0) {
            return null;
        }
        //压缩图片的observable
        Observable<Map<String, Object>> mapObservable = new Compressor(BaseApplication.getApp())
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .setQuality(80)
                .setConfig(Bitmap.Config.RGB_565)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(CacheDirManager.getTempFileDir().getAbsolutePath())
                .compressToFileAsObservable(params);

        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();
        params.put("UploadOnSubscribe", uploadOnSubscribe);
        Observable<ResponseBody> observable = mapObservable.flatMap(new Function<Map<String, Object>, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(Map<String, Object> params) throws Exception {
                return NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST).create(BaseApi.class)
                        .uploadFile(url,params);
            }
        });
//        Observable<ResponseBody> observable = NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST).create(BaseApi.class)
//                                .uploadFile(url,params);
        LoadCallBack loadCallBack = new LoadCallBack<ResponseBody>() {
            @Override
            protected void onProgress(String percent) {
                if(callBack!=null){
                    callBack.onProgress(percent);
                }
            }

            @Override
            protected void onSuccess(ResponseBody t) {
                if(callBack!=null){
                    callBack.parseBody(t);
                }
                CacheDirManager.deleteTempFile();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(callBack!=null){
                    callBack.onFail(e.getMessage());
                }
                CacheDirManager.deleteTempFile();
            }

            @Override
            public void dispose() {
                super.dispose();
                CacheDirManager.deleteTempFile();
            }
        };
        Observable.merge(Observable.create(uploadOnSubscribe),observable)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(loadCallBack);
        return loadCallBack;
    }
}
