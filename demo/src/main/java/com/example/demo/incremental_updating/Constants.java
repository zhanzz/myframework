package com.example.demo.incremental_updating;

import java.io.File;

import android.os.Environment;

public class Constants {

    public static final String PATCH_FILE = "apk.patch";

    /**
     * 在windows cmd 输入ipconfig 获得ip
     */
    public static final String URL_PATCH_DOWNLOAD = "http://192.168.198.84:8080/patchfile/apk.patch";
    //linux remote
    //public static final String URL_PATCH_DOWNLOAD = "http://www.dongnaoedu.com/"+PATCH_FILE;

    public static final String PACKAGE_NAME = "com.haocai.app.update";

    public static final String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    //new apk name
    public static final String NEW_APK_NAME = "dn_apk_new.apk";

    //新版本apk的目录
    public static final String NEW_APK_PATH = SD_CARD + File.separator + "dn_apk_new.apk";

    public static final String PATCH_FILE_PATH = SD_CARD + PATCH_FILE;

}