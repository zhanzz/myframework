package com.framework.common.image_select.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import com.framework.common.BaseApplication;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.ListUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileManager {

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
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && !AppTools.isQOrHigher()) {
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
    /**
     * For example, photos captured by your app or other downloaded files should be saved as public files.
     * @param context
     * @return
     */
    public static File createTmpFile(Context context) {
        File pic = FileManager.getCameraCacheDir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = "multi_image_" + timeStamp + "";
        File tmpFile = new File(pic, fileName + ".jpg");
        return tmpFile;
    }

    public static String createFileName(){
        Random random = new Random();
        int value = random.nextInt();
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date())+"_"+value;
    }

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    //删除无效的压缩的文件
    public static void deleteCachePic() {
        try {
            FileManager.deleteAllFiles(getCameraCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
