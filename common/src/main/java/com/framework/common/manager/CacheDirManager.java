package com.framework.common.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;

import com.framework.common.BaseApplication;
import com.framework.common.utils.ListUtils;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * @author zhangzhiqiang
 * @date 2019/4/30.
 * description：应用文件目录管理类
 */
public class CacheDirManager {
    public static File getCameraCacheDir(){
        Context context = BaseApplication.getApp();
        File appCacheDir = null;
        String cacheDir = "cameraCache";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }else{
            appCacheDir = new File(context.getExternalCacheDir(),cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = new File(context.getCacheDir(),cacheDir);
            if(!appCacheDir.exists()){
                appCacheDir.mkdirs();
            }
        }
        return appCacheDir;
    }

    /**
     * 需要删除的文件都可以放在这里
     * @return
     */
    public static File getTempFileDir(){
        Context context = BaseApplication.getApp();
        File appCacheDir = null;
        String cacheDir = "temp";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }else{
            appCacheDir = new File(context.getExternalCacheDir(),cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = new File(context.getCacheDir(),cacheDir);
            if(!appCacheDir.exists()){
                appCacheDir.mkdirs();
            }
        }
        return appCacheDir;
    }

    public static void deleteTempFile() {
        File[] files = getTempFileDir().listFiles();
        if(!ListUtils.isEmpty(files)){
            for(File file:files){
                if(file!=null){
                    file.delete();
                }
            }
        }
    }
}
