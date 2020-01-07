package com.framework.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.framework.common.R;
import com.framework.common.manager.PermissionManager;

import java.io.File;

/**
 * @author zhangzhiqiang
 * @date 2019/12/30.
 * description：
 */
public class TakePhotoUtil {
    public static Uri takePhoto(Activity context,int requestCode){
        if(!PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ToastUtil.show(context,"没有外部存储权限");
            return null;
        }
        //拍照存放路径
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!fileDir.exists()) {
            if(!fileDir.mkdir()){
                ToastUtil.show(context,"创建目录失败");
                return null;
            }
        }
        //图片名称
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //图片路径
        String mFilePath = fileDir.getAbsolutePath()+File.separator+fileName;
        //跳转到相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri=FileProvider7.getUriForFile(context,new File(mFilePath));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //跳转，需要在onActivityResult进行处理
        context.startActivityForResult(intent, requestCode);
        return uri;
        //拿到结果后刷新相册
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath)));
    }

    public static File takePhoto(Fragment fragment,int requestCode){
        Context context = fragment.getContext();
        if(context==null){
            return null;
        }
        if(!PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ToastUtil.show(context,"没有外部存储权限");
            return null;
        }
        //拍照存放路径
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!fileDir.exists()) {
            if(!fileDir.mkdir()){
                ToastUtil.show(context,"创建目录失败");
                return null;
            }
        }
        //图片名称
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //图片路径
        String mFilePath = fileDir.getAbsolutePath()+File.separator+fileName;
        //跳转到相机
        File file = new File(mFilePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri=FileProvider7.getUriForFile(context,file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //跳转，需要在onActivityResult进行处理
        fragment.startActivityForResult(intent, requestCode);
        return file;
        //拿到结果后刷新相册
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath)));
    }

    public static Uri takePhotoV2(Activity context){
        //获取图片沙盒文件夹
        File PICTURES = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(PICTURES==null){
            ToastUtil.show(context,"无法获取私有存储");
            return null;
        }
        //图片名称
        String mFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //图片路径
        String mFilePath = PICTURES.getAbsolutePath()+File.separator+mFileName;
        //跳转到相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri=FileProvider7.getUriForFile(context,new File(mFilePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, 1111);
        //相册无法看到图片,刷新也看不到
        return uri;
    }

    public static File takePhotoV2(Fragment fragment,int requestCode){
        Context context = fragment.getContext();
        if(context==null){
            return null;
        }
        //获取图片沙盒文件夹
        File PICTURES = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(PICTURES==null){
            ToastUtil.show(context,"无法获取私有存储");
            return null;
        }
        //图片名称
        String mFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //图片路径
        String mFilePath = PICTURES.getAbsolutePath()+File.separator+mFileName;
        //跳转到相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(mFilePath);
        Uri uri=FileProvider7.getUriForFile(context,file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, requestCode);
        //相册无法看到图片,刷新也看不到
        return file;
    }

    //保存到公共文件夹
    public static Uri takePhotoV3(Activity context,int requestCode){
        if(context==null){
            return null;
        }
        if(!AppTools.isExternalStorageWritable()){
            ToastUtil.show(context,"外部存储不可用");
            return null;
        }
        //拍照存放路径
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //设置参数
        Uri uri;
        //设置保存参数到ContentValues中
        ContentValues contentValues = new ContentValues();
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES);
        } else {
            //Android Q以下版本
            //为了适配Android Q版本以下
            if(!PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ToastUtil.show(context,"没有外部存储权限");
                return null;
            }
            File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!fileDir.exists()) {
                if(!fileDir.mkdir()){
                    ToastUtil.show(context,"创建目录失败");
                    return null;
                }
            }
            String mFilePath = fileDir.getAbsolutePath()+File.separator+fileName;
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //若生成了uri，则表示该文件添加成功
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, requestCode);
        //不用刷新相册库，会自动刷新
        return uri;
    }

    //保存到公共文件夹
    public static Uri takePhotoV3(Fragment fragment, int requestCode){
        Context context = fragment.getContext();
        if(context==null){
            return null;
        }
        if(!AppTools.isExternalStorageWritable()){
            ToastUtil.show(context,"外部存储不可用");
            return null;
        }
        //拍照存放路径
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        //设置参数
        Uri uri;
        //设置保存参数到ContentValues中
        ContentValues contentValues = new ContentValues();
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            //requestLegacyExternalStorage为true或false都可以存储在公共外部
            contentValues.put(MediaStore.Images.Media.IS_PENDING,1);//不会通知更新
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES);
        } else {
            //Android Q以下版本
            //为了适配Android Q版本以下
            if(!PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ToastUtil.show(context,"没有外部存储权限");
                return null;
            }
            File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!fileDir.exists()) {
                if(!fileDir.mkdir()){
                    ToastUtil.show(context,"创建目录失败");
                    return null;
                }
            }
            String mFilePath = fileDir.getAbsolutePath()+File.separator+fileName;
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        //Uri nonotify = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().build();
        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        //若生成了uri，则表示该文件添加成功
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager()) != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(intent, requestCode);
        }else{
            Toast.makeText(context, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
        //不用刷新相册库，会自动刷新
        return uri;
    }
}
