package com.framework.common.retrofit;

import com.framework.common.data.Result;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class ResponseTransformer {

    public static <T extends Result> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.onErrorResumeNext(new ErrorResumeFunction<T>())
                        .flatMap(new ResponseFunction<T>());
            }
        };
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends T>> {

        @Override
        public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T extends Result> implements Function<T, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(T tResponse) throws Exception {
            if (tResponse != null) {
                if (tResponse.getCode() == 1) {//业务成功
                    return Observable.just(tResponse);
                } else {
                    return Observable.error(new ApiException(tResponse.getCode(), tResponse.getMessage()));
                }
            } else {
                return Observable.error(new ApiException(-1, "数据为空"));
            }
        }
    }
}