package com.framework.common.callBack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author zhangzhiqiang
 * @date 2019/4/26.
 * description：
 */
public abstract class FileUploadCallBack<T>{
    public abstract void onSuccess(T result);
    public abstract void onFail(String msg);
    public void onProgress(String progress){}

    public void parseBody(ResponseBody body){
        String string = null;
        try {
            string = body.string();
            if(getClass().getGenericSuperclass() instanceof Class){ //不是ParameterizedType说明泛型参数没有实例化
                onSuccess((T)string);
            }else{
                Type entityClass =  ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                onSuccess((T) JSON.parseObject(string,entityClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
