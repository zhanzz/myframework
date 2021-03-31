package com.example.demo.camera;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.example.demo.R;
import com.example.demo.R2;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.ToastUtil;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity {
    @BindView(R2.id.previewView)
    PreviewView previewView;
    @BindView(R2.id.btn_take_pic)
    Button btnTakePic;
    @BindView(R2.id.toggleLight)
    ToggleButton toggleLight;
    @BindView(R2.id.sd_image)
    SimpleDraweeView sdImage;
    @BindView(R2.id.btn_cancel)
    Button btnCancel;
    @BindView(R2.id.btn_sure)
    Button btnSure;
    @BindView(R2.id.group_result)
    Group groupResult;
    @BindView(R2.id.group_take_pic)
    Group groupTakePic;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected boolean isFitSystemBar() {
        return true;
    }

    @Override
    protected int getSystemBarColor() {
        return 0x00000000;
    }

    @Override
    public void bindData() {
        requestNeedPermissions(100,Manifest.permission.CAMERA);
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if(requestCode==100){
            initCamera();
        }
    }

    @Override
    public void failPermission(@NonNull List<String> permissions, int requestCode) {
        if(requestCode==100){
            showToast("需要相机权限才能拍照");
            finish();
        }
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

    @Override
    public void initEvent() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    ImageCapture imageCapture;
    Preview preview;
    Uri resultUri;
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                int rotationDegrees = image.getImageInfo().getRotationDegrees();
                // insert your code here.
                //Log.e("zhang","rotationDegrees="+rotationDegrees);
                image.close();
            }
        });

        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
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

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector,imageCapture, imageAnalysis, preview);
    }

    @OnClick({R2.id.btn_take_pic, R2.id.toggleLight,R2.id.btn_cancel, R2.id.btn_sure})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_take_pic) {
            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES);
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
                    preview.setSurfaceProvider(null);
                    //非UI线程
                    groupResult.setVisibility(View.VISIBLE);
                    groupTakePic.setVisibility(View.INVISIBLE);
                    resultUri = outputFileResults.getSavedUri();
                    sdImage.setImageURI(resultUri);
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {

                }
            });
        } else if (id == R.id.toggleLight) {
            imageCapture.setFlashMode(toggleLight.isChecked() ? ImageCapture.FLASH_MODE_ON: ImageCapture.FLASH_MODE_OFF);
        }else if(id == R.id.btn_cancel){
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            groupResult.setVisibility(View.INVISIBLE);
            groupTakePic.setVisibility(View.VISIBLE);
            if (resultUri != null) {
                getContext().getContentResolver().delete(resultUri, null, null);
//                if(!TextUtils.isEmpty(mTmpUri.getPath())){
//                    new File(mTmpUri.getPath()).delete();
//                }
            }
        }else if(id == R.id.btn_sure){
            Intent intent = new Intent();
            intent.putExtra("data",resultUri);
            setResult(RESULT_OK,intent);
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CameraActivity.class);
        context.startActivity(starter);
    }
}