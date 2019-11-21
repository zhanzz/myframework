package com.framework.common.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import com.framework.common.BaseApplication;
import com.framework.common.image_select.utils.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PictureUtils {

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    public static String bitmapToString1(String path) {
        try {
            File file = scal(path);
            Bitmap bm = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            bm.recycle();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static File scal(String path) {
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 150 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                Matrix matrix = new Matrix();
                matrix.postRotate(readPictureDegree(path));
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        }
        return outputFile;
    }

    @SuppressLint("SimpleDateFormat")
    public static Uri createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = BaseApplication.getApp().getCacheDir();
        File image = null;
        try {
            image = File.createTempFile(imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
            try{
                Random random = new Random();
                int n = random.nextInt();
                String name = imageFileName + Integer.toString(n) + ".jpg";
                image = new File(BaseApplication.getApp().getFilesDir(),name);
                if(!image.exists()){
                    image.createNewFile();
                }
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
        return Uri.fromFile(image);
    }

    public static String getFilName(String filePath){
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        String realFileName = (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
        //加一些随机串
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        Random random = new Random();
        int n = random.nextInt(100);
        String name = imageFileName + Integer.toString(n) + realFileName;
        return name;
    }

    public static Uri createImageFile(String fileName) {
        File storageDir = FileManager.getCameraCacheDir();
        File image = null;
        try {
            image = new File(storageDir, fileName);
            image.createNewFile();
//			image = File.createTempFile(fileName, /* prefix */
//					".jpg", /* suffix */
//					storageDir /* directory */
//			);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (null != inputChannel) {
                    inputChannel.close();
                }
                if (null != outputChannel) {
                    outputChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到本地
     *
     * @param mBitmap
     */
    public static File saveBitmap(Bitmap mBitmap, String fileName) {
        File f = new File(createImageFile(fileName).getPath());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (null != fOut) {
                fOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    //获取系统相机存储图片的路径
    public static File getCameraDirFilePath() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (path.exists()) {
            //暂时注释掉其他情况的相册存储路径，在华为手机上测试的是100ANDRO,但是手机相机图库读取的是Camera
            File test1 = new File(path, "Camera/");
            if (test1.exists()) {
                path = test1;
            } else {
                File test2 = new File(path, "100ANDRO/");
                if (test2.exists()) {
                    path = test2;
                } else {
                    File test3 = new File(path, "100MEDIA/");
                    if (!test3.exists()) {
                        test3.mkdirs();
                    }
                    path = test3;
                }
            }
        } else {
            path = new File(path, "Camera/");
            path.mkdirs();
        }
        System.err.println("相机路径==" + path.getPath());
        return path;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }
}
