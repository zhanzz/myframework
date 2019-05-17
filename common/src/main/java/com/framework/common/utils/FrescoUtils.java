package com.framework.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.info.ImagePerfData;
import com.facebook.drawee.backends.pipeline.info.ImagePerfDataListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by zhangzhiqiang on 2016/9/21.
 */
public class FrescoUtils {

    public static void pause(){
        Fresco.getImagePipeline().pause();
    }

    public static void resume(){
        Fresco.getImagePipeline().resume();
    }

    public static void showThumb(String url, SimpleDraweeView draweeView, int width, int height){
        if(!TextUtils.isEmpty(url)) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setRotationOptions(RotationOptions.autoRotate())
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .setOldController(draweeView.getController())
                    //.setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            draweeView.setController(controller);
        }else {
            draweeView.setImageURI(Uri.EMPTY);
        }
    }


    /**
     * 宽高的单位是px
     * @param url
     * @param draweeView
     * @param width
     * @param height
     */
    public static void showThumbQiNiuPx(String url, SimpleDraweeView draweeView, int width, int height){
        showThumb(url,draweeView,width,height);
    }

    /**
     * 使用七牛的图片，不需要宽高
     * @param url
     * @param draweeView
     */
    public static void showThumb(String url, SimpleDraweeView draweeView){
        if (url==null||url.length()==0) return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setRotationOptions(RotationOptions.autoRotate())
//                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                //.setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }

    /**
     * 使用七牛的图片，不需要宽高
     * @param url
     * @param draweeView
     */
    public static void showThumb(String url, SimpleDraweeView draweeView,Postprocessor postprocessor){
        if (url==null||url.length()==0) return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setPostprocessor(postprocessor)
                .setRotationOptions(RotationOptions.autoRotate())
//                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                //.setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }

    public interface Callback{
        void downloadComplete(Bitmap drawable);
    }

    /***
     * 下载图片返回Bitmap
     * @param context
     * @param uri
     * @param callback
     */
    public static void setDataSubscriber(Context context, Uri uri,final Callback callback){
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap  = closeableBitmap.getUnderlyingBitmap();
                        if(bitmap != null && !bitmap.isRecycled()) {
                            //you can use bitmap here(bitmap);
                            callback.downloadComplete(fromFresco(bitmap));
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }
            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                // handle failure
            }
        };
        getBitmap(context, uri, dataSubscriber);
    }

    /**
     *
     * @param context
     * @param uri
     * @param dataSubscriber
     */
    public static void getBitmap(Context context, Uri uri, DataSubscriber dataSubscriber){
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
//        if(width > 0 && height > 0){
//            builder.setResizeOptions(new ResizeOptions(width, height));
//        }
        ImageRequest request = builder.build();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, context);
        dataSource.subscribe(dataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }

    public static Bitmap fromFresco(Bitmap bitmap) {
        if(bitmap!=null && !bitmap.isRecycled()){
            return bitmap.copy(bitmap.getConfig(),bitmap.isMutable());
        }
        return bitmap;
    }
}
