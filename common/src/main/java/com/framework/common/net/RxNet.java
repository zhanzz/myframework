package com.framework.common.net;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.base_mvp.IBaseView;
import com.framework.common.callBack.FileCallBack;
import com.framework.common.callBack.FileUploadCallBack;
import com.framework.common.callBack.RxNetCallBack;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.data.LoadType;
import com.framework.common.data.Result;
import com.framework.common.data.operation.UserOperation;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import com.framework.common.image_select.utils.FileManager;
import com.framework.common.image_util.Compressor;
import com.framework.common.callBack.LoadCallBack;
import com.framework.common.manager.NetWorkManager;
import com.framework.common.retrofit.ApiSubscriber;
import com.framework.common.retrofit.SchedulerProvider;
import com.framework.common.BaseApi;
import com.framework.common.utils.Platform;

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
    public static @Nullable
    <T> Disposable request(Observable<Result<T>> observable, final IBaseView view, final LoadType loadType, final RxNetCallBack<T> callBack) {
        if(loadType.ordinal()>2 && !UserOperation.getInstance().isLogin()){
            return null;
        }
        if (loadType==LoadType.LOAD) {
            view.showLoading();
        }else if(loadType==LoadType.LOAD_DIALOG){
            view.showLoadingDialog();
        }
        ApiSubscriber apiSubscriber = new ApiSubscriber<T>() {
            @Override
            protected void onFail(ApiException ex) {
                hideLoad(loadType, view);
                if (callBack != null && ex.getCode()!= CustomException.DISPOSED) {
                    callBack.onFailure(ex.getCode(), ex.getDisplayMessage());
                }
            }

            @Override
            public void onSuccess(T data, int code, String msg) {
                hideLoad(loadType, view);
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

    private static void hideLoad(LoadType loadType, IBaseView view) {
        if (loadType==LoadType.LOAD) {
            view.hideLoading();
        }else if(loadType==LoadType.LOAD_DIALOG){
            view.hideLoadingDialog();
        }
    }
    /**
     * 下载
     */

    public static Disposable downLoadFile(String url, final FileCallBack callBack) {
        DownLoadObserver observer = new DownLoadObserver<ResponseBody>(callBack) {
            @Override
            protected void onFail(final ApiException ex) {
                Platform.get().defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail(ex);
                    }
                });
            }
        };
        NetWorkManager.getInstance().getRetorfit(BuildConfig.GLOBAL_HOST).create(BaseApi.class)
                .downloadFile(url)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().io())
                .subscribe(observer);
        return observer;
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
                .setMaxHeight(800)
                .setMaxWidth(400)
                .setQuality(80)
                .setConfig(Bitmap.Config.RGB_565)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(FileManager.getTempFileDir().getAbsolutePath())
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
                FileManager.deleteTempFile();
            }

            @Override
            protected void onFail(ApiException ex) {
                if(callBack!=null){
                    callBack.onFail(ex.getMessage());
                }
                FileManager.deleteTempFile();
            }
        };
        Observable.merge(Observable.create(uploadOnSubscribe),observable)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(loadCallBack);
        return loadCallBack;
    }
}
