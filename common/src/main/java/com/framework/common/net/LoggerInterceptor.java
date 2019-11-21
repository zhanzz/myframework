package com.framework.common.net;

import android.text.TextUtils;
import com.framework.common.utils.LogUtil;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttp";
    private boolean showResponse;

    public LoggerInterceptor(boolean showResponse) {
        this.showResponse = showResponse;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            LogUtil.e(TAG, "========request'log=======");
            LogUtil.e(TAG, "method : " + request.method());
            LogUtil.e(TAG, "url : " + url);
            if (headers != null && headers.size() > 0) {
                LogUtil.e(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogUtil.e(TAG, "contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtil.e(TAG, "params : " + bodyToString(request));
                    } else {
                        LogUtil.e(TAG, "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            LogUtil.e(TAG, "========request'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response logForResponse(Response response) {
        try {
            //===>response log
            LogUtil.e(TAG, "========response'log=======");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            LogUtil.e(TAG, "url : " + clone.request().url());
            LogUtil.e(TAG, "code : " + clone.code());
            //LogUtil.e(TAG, "protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                LogUtil.e(TAG, "message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        LogUtil.e(TAG, "contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            LogUtil.e(TAG, "responseBody's content : " + resp);

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            LogUtil.e(TAG, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    } else {
                        String resp = body.string();
                        LogUtil.e(TAG, "responseBody's content : " + resp);

                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    }
                }
            }

            LogUtil.e(TAG, "========response'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}