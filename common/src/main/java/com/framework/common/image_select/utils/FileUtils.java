package com.framework.common.image_select.utils;

import android.content.Context;
import android.os.Environment;

import com.framework.common.BaseApplication;
import com.framework.common.manager.CacheDirManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static File createTmpFile(Context context) {
        File pic = CacheDirManager.getCameraCacheDir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = "multi_image_" + timeStamp + "";
        File tmpFile = new File(pic, fileName + ".jpg");
        return tmpFile;
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
            FileUtils.deleteAllFiles(CacheDirManager.getCameraCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
