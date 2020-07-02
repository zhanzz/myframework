package com.framework.common.image_util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import com.facebook.common.util.UriUtil;
import com.framework.common.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class ImageUtil {

    private ImageUtil() {

    }

    static Uri compressImage(Uri imageFile, float reqWidth, float reqHeight, Bitmap.CompressFormat compressFormat, int quality,Bitmap.Config config, File destinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file = destinationPath.getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(destinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            Bitmap bitmap = decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight,config);
            if(bitmap==null){ //可能不是图片文件，则原路返回file
                return imageFile;
            }
            bitmap.compress(compressFormat, quality, fileOutputStream);
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return Uri.fromFile(destinationPath);
    }

    static Bitmap decodeSampledBitmapFromFile(Uri imageFile, float reqWidth, float reqHeight, Bitmap.Config config) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        if(imageFile==null){
            return null;
        }
        Bitmap scaledBitmap = null, bmp = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ParcelFileDescriptor pd = BaseApplication.getApp().getContentResolver().openFileDescriptor(imageFile,"r");
        if(pd==null){
            return null;
        }
        bmp = BitmapFactory.decodeFileDescriptor(pd.getFileDescriptor(), null,options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        if(actualHeight<=0||actualWidth<=0){
            return null;
        }

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = reqWidth / reqHeight;

        if (actualHeight > reqHeight || actualWidth > reqWidth) {
            //If Height is greater
            if (imgRatio < maxRatio) {
                imgRatio = reqHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) reqHeight;

            }  //If Width is greater
            else if (imgRatio > maxRatio) {
                imgRatio = reqWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) reqWidth;
            } else {
                actualHeight = (int) reqHeight;
                actualWidth = (int) reqWidth;
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig = config;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFileDescriptor(pd.getFileDescriptor(), null,options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,config);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
            pd.close();
            return null;
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2.0f,
                middleY - bmp.getHeight() / 2.0f, new Paint(Paint.FILTER_BITMAP_FLAG));
        bmp.recycle();
        ExifInterface exif = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exif = new ExifInterface(pd.getFileDescriptor());
            }else if(UriUtil.isLocalFileUri(imageFile) && !TextUtils.isEmpty(imageFile.getPath())){
                exif = new ExifInterface(imageFile.getPath());
            }
            Matrix matrix = new Matrix();
            if(exif!=null){
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
            }
            Bitmap old = scaledBitmap;
            scaledBitmap = Bitmap.createBitmap(old, 0, 0, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(), matrix, true);
            if(scaledBitmap!=old){
                old.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            pd.close();
        }

        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width=options.outWidth; //原始图片宽
        int height=options.outHeight; //原始图片高

        int inSampleSize=1; //采样率
        if(width>reqWidth || height>reqHeight) //原始的宽比目标宽大，或者原始高比目标高大
        {
            int widthRadio=Math.round(width *1.0f/reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }
}
