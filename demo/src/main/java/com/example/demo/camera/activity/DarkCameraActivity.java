package com.example.demo.camera.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.example.demo.camera.CustomLifecycle;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.camera.presenter.DarkCameraPresenter;
import com.example.demo.camera.view.IDarkCameraView;
import com.example.demo.R;
import com.framework.common.manager.PermissionManager;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DarkCameraActivity extends BaseActivity implements IDarkCameraView {
    private DarkCameraPresenter mPresenter;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private CustomLifecycle lifecycle = new CustomLifecycle();
    @Override
    public int getLayoutId() {
        return R.layout.activity_dark_camera;
    }

    @Override
    protected int getSystemBarColor() {
        return 0xff000000;
    }

    @Override
    protected void setStatusDarkBar(boolean isDark) {
        super.setStatusDarkBar(true);
    }

    @Override
    public void bindData() {
        lifecycle.doOnResume();
        initCamera();
    }

    @Override
    protected void onDestroy() {
        if(disposable!=null){
            disposable.dispose();
        }
        lifecycle.doOnDestroy();
        super.onDestroy();
    }

    @Override
    public void initEvent() {

    }

    private void initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    Uri resultUri;
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//        preview = new Preview.Builder()
//                .build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
//        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
//            @Override
//            public void analyze(@NonNull ImageProxy image) {
//                int rotationDegrees = image.getImageInfo().getRotationDegrees();
//                // insert your code here.
//                //Log.e("zhang","rotationDegrees="+rotationDegrees);
//                image.close();
//            }
//        });

        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(Surface.ROTATION_0)
                        .build();
        OrientationEventListener orientationEventListener = new OrientationEventListener((Context)this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }
                imageCapture.setTargetRotation(rotation);
            }
        };
        //orientationEventListener.enable();
        //orientationEventListener.disable();

        Camera camera = cameraProvider.bindToLifecycle(lifecycle, cameraSelector,imageCapture);
        startHart();
    }

    Disposable disposable;
    private void startHart() {
        disposable = Flowable.interval(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        takePic();
                    }
                });

    }

    private void takePic() {
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        } else {
            //Android Q以下版本
            //为了适配Android Q版本以下
            if(!PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                showToast("没有外部存储权限");
                return;
            }
            File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!fileDir.exists()) {
                if(!fileDir.mkdir()){
                    showToast("创建目录失败");
                    return;
                }
            }
            String mFilePath = fileDir.getAbsolutePath()+File.separator+fileName;
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();
        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                //非UI线程
                resultUri = outputFileResults.getSavedUri();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

            }
        });
    }


    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new DarkCameraPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DarkCameraActivity.class);
        context.startActivity(starter);
    }
}