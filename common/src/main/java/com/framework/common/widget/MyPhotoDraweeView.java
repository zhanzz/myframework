package com.framework.common.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;
/**
 * @author zhangzhiqiang 允许失败点击重试
 * @date 2019/5/27.
 * description：
 */
public class MyPhotoDraweeView extends PhotoDraweeView {
    private boolean isLoadFail;//图片是否加载失败
    public MyPhotoDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public MyPhotoDraweeView(Context context) {
        super(context);
    }

    public MyPhotoDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPhotoDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPhotoUri(String url) {
        super.setPhotoUri(Uri.parse(url));
    }

    @Override
    public void setPhotoUri(Uri uri, @Nullable Context context) {
        setEnableDraweeMatrix(false);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setCallerContext(context)
                .setUri(uri)
                .setOldController(getController())
                .setTapToRetryEnabled(true)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        setEnableDraweeMatrix(false);
                        isLoadFail = true;
                    }

                    @Override public void onFinalImageSet(String id, ImageInfo imageInfo,
                                                          Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        setEnableDraweeMatrix(true);
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                        isLoadFail = false;
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        super.onIntermediateImageFailed(id, throwable);
                        setEnableDraweeMatrix(false);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        super.onIntermediateImageSet(id, imageInfo);
                        setEnableDraweeMatrix(true);
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                    }
                })
                .build();
        setController(controller);

        setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if(isLoadFail){
                    //模拟点击事件
                    MotionEvent eventDown = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
                    MotionEvent eventUp = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
                    getController().onTouchEvent(eventDown);
                    getController().onTouchEvent(eventUp);
                    eventDown.recycle();
                    eventUp.recycle();
                }
            }
        });
    }
}
