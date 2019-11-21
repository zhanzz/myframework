package com.example.demo.incremental_updating;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.framework.common.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;


public class ApkUtils {

	public static boolean isInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return installed;
	}

	/**
	 * 获取已安装Apk文件的源Apk文件
	 * 如：/data/app/my.apk
	 * 
	 * @param context
	 * @param packageName
	 * @return  /data/app/com.example.retrofitframemwork-nGGl6QbcFStiiF73SYpr4g==/base.apk
	 */
	public static String getSourceApkPath(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName))
			return null;
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(packageName, 0);
			return appInfo.sourceDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	///data/app/com.example.retrofitframemwork-nGGl6QbcFStiiF73SYpr4g==/base.apk
	public static String getSourceApkPathTwo(Context context){
		return context.getPackageCodePath();
	}

	public static String[] getSourceApkPathThree(Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return context.getApplicationInfo().splitSourceDirs;//null
		}else {
			return new String[]{context.getPackageCodePath()};
		}
	}

	/**
	 * 安装Apk
	 * 
	 * @param context
	 * @param apkPath
	 */
	public static void installApk(Context context, String apkPath) {
		File file = new File(apkPath);
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(BaseApplication.getApp(), BaseApplication.getApp().getPackageName() + ".FileProvider", file);
		} else {
			uri = Uri.fromFile(file);
		}
		//BaseApplication.getApp().grantUriPermission("目标包名", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
		Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}