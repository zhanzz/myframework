package com.framework.common.exception;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import retrofit2.HttpException;

public class CustomException {

    /**
     * 操作取消
     */
    public static final int DISPOSED = 999;

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //解析错误
            ex = new ApiException(PARSE_ERROR, e.getMessage());
            return ex;
        } else if (e instanceof ConnectException) {
            //网络错误
            ex = new ApiException(NETWORK_ERROR, e.getMessage());
            return ex;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            ex = new ApiException(NETWORK_ERROR, e.getMessage());
            return ex;
        } else if(e instanceof HttpException){
            HttpException httpException = (HttpException) e;
            String message = e.getMessage();
            switch (httpException.code()){
                case 404://404 请求失败，请求所希望得到的资源未被在服务器上发现。
                    message = "请检查客户端是否有更新";
                    break;
                case 403:// 403 服务器已经理解请求，但是拒绝执行它。
                    message = "服务器禁止访问";
                    break;
                case 301: //301 被请求的资源已永久移动到新位置。
                    message = "被请求的资源已永久移动到新位置";
                    break;
                case 302: //302 请求的资源现在临时从不同的 URI 响应请求。
                    message = "请求的资源现在临时从不同的 URI 响应请求";
                    break;
                case 400://400 1、语义有误，当前请求无法被服务器理解。2、请求参数有误。
                    message = "请求无效";
                    break;
                case 401://401 当前请求需要用户验证。
                    message = "请求需要用户验证";
                    break;
                case 500://500 服务器遇到了一个未曾预料的状况，无法完成对请求的处理，会在程序码出错时出现。
                    message = "服务器错误";
                    break;
                case 501://501 服务器不支持当前请求所需要的某个功能。无法识别请求的方法。
                    message = "服务器无法识别请求";
                    break;
                case 502: //502 作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应。
                    message = "服务器无效响应";
                    break;
                case 503://503 由于临时的服务器维护或者过载，服务器当前无法处理请求。
                    message = "系统繁忙";
                    break;
            }
            ex = new ApiException(HTTP_ERROR,e.getMessage(),message);
            return ex;
        } else{
            //未知错误
            ex = new ApiException(UNKNOWN, e.getMessage());
            return ex;
        }
    }
}