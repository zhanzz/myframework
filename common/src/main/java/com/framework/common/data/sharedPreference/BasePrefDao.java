package com.framework.common.data.sharedPreference;

import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.utils.GsonUtils;
import java.lang.reflect.ParameterizedType;

public class BasePrefDao<T> {

    private final String VERSION_CODE = "version_code";
    private TinyDB tinyDB;
    private Class<T> clazz;

    public BasePrefDao(String cacheName, int versionCode) {
        Class clazz = null;
        if(getClass().getGenericSuperclass() instanceof Class){ //不是ParameterizedType说明泛型参数没有实例化
            clazz = Object.class;
        }else{
            clazz = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        this.clazz = clazz;
        tinyDB = new TinyDB(BaseApplication.getApp(), cacheName);
        if(versionCode > tinyDB.getInt(VERSION_CODE)){
            onUpgrade();
            tinyDB.putInt(VERSION_CODE, versionCode);
        }
    }

    public void setVersionCode(){
        tinyDB.putInt(VERSION_CODE, BuildConfig.VERSION_CODE);
    }

    /**
     * 如果应用升级还需保存此数据可覆写此方法
     */
    public void onUpgrade(){
        tinyDB.clear();
    }

    public void setCacheWithoutKey(String content){
        tinyDB.putString(clazz.getSimpleName(), content);
    }

    public void setCacheWithoutKeyAsy(String content){
        tinyDB.putStringAsy(clazz.getSimpleName(), content);
    }

    public void setCacheWithKey(String key, String content){
        tinyDB.putString(key, content);
    }

    public void setCacheWithKeyAsy(String key, String content){
        tinyDB.putStringAsy(key, content);
    }

    public TinyDB getTinyDB() {
        return tinyDB;
    }

    public T getFromCacheWithKey(String key){
        String content = tinyDB.getString(key);
        return GsonUtils.parseFromString(content, clazz);
    }

    public T getFromCacheWithoutKey(){
        String content = tinyDB.getString(clazz.getSimpleName());
        T bean = GsonUtils.parseFromString(content, clazz);
        if(bean==null){
            try {
                bean = clazz.newInstance();//须确保有默认构造方法
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public T getData(){
        return getFromCacheWithoutKey();
    }

    public void setData(T data){
        String content = GsonUtils.parseToString(data);
        setCacheWithoutKeyAsy(content);
    }

    public void setDataSyn(T data){
        String content = GsonUtils.parseToString(data);
        setCacheWithoutKey(content);
    }
}
