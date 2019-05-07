package com.framework.common.convert;

import android.support.annotation.Nullable;

import com.framework.common.net.UpLoadFileType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class FileConverterFactory extends Converter.Factory {

    public static FileConverterFactory create() {
        return new FileConverterFactory();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        //进行条件判断，如果传进来的 methodAnnotations 不包含 UpLoadFileType，则匹配失败
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof UpLoadFileType) {
                return new FileRequestBodyConverter();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return null;
    }
}