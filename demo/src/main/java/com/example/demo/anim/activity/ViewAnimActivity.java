package com.example.demo.anim.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.anim.presenter.ViewAnimPresenter;
import com.example.demo.anim.view.IViewAnimView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.PictureUtils;
import com.framework.common.utils.TakePhotoUtil;
import com.framework.common.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewAnimActivity extends BaseActivity implements IViewAnimView {
    @BindView(R2.id.frame_person)
    ImageView framePerson;
    @BindView(R2.id.translation_person)
    ImageView translationPerson;
    @BindView(R2.id.rotate_person)
    ImageView rotatePerson;
    @BindView(R2.id.alpha_person)
    ImageView alphaPerson;
    @BindView(R2.id.scale_person)
    ImageView scalePerson;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.btn_take)
    Button btnTake;
    @BindView(R2.id.image)
    SimpleDraweeView image;
    @BindView(R2.id.tv_read)
    TextView tvRead;
    private ViewAnimPresenter mPresenter;
    private File mPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_anim;
    }

    @Override
    public void bindData() {
        setTitle("视图动画");

        framePerson.setImageResource(R.drawable.frame_person_anim);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) framePerson.getDrawable();
        animationDrawable1.start();

        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_person);
        hyperspaceJump.setStartOffset(3000);
        translationPerson.startAnimation(hyperspaceJump);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_person);
        rotate.setStartOffset(3000);
        rotatePerson.startAnimation(rotate);

        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha_person);
        alpha.setStartOffset(3000);
        alphaPerson.startAnimation(alpha);

        //fillBefore 一start就为这个值
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_person);
        scale.setStartOffset(3000);
        scalePerson.startAnimation(scale);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ViewAnimPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ViewAnimActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mTempUri = savedInstanceState.getParcelable("uri");
            ToastUtil.show(this,"重新创建mTempUri="+mTempUri);
        }
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.btn_take, R2.id.image})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_take) {
            requestNeedPermissions(100,Manifest.permission.READ_EXTERNAL_STORAGE,
             Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
            //requestNeedPermissions(100,Manifest.permission.CAMERA);
        } else if (id == R.id.image) {

        }
    }

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if(requestCode==100 && permissions.contains(Manifest.permission.CAMERA)){
            //mTempUri = TakePhotoUtil.takePhoto(this,200);//content://com.example.retrofitframemwork.FileProvider/ext_dir/Pictures/IMG_1609378107860.jpg
            mTempUri = takePhoto();
//            File imagePath = new File(Environment.getExternalStorageDirectory(), "Pictures");
//            if (!imagePath.exists()) {
//                if(!imagePath.mkdir()){
//                    ToastUtil.show(this,"创建目录失败");
//                    return;
//                }
//            }
//            File newFile = new File(imagePath, "IMG_" + System.currentTimeMillis() + ".jpg");
//            try {
//                //android10 分区存储java.io.IOException: Permission denied
//                newFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }else if(requestCode==100 && permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

        }
    }

    private Uri mTempUri;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("uri",mTempUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    StringBuilder builder = new StringBuilder();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        builder.append("resultCode=").append(resultCode).append(";requestCode=").append(requestCode).append(mTempUri==null);
        tvRead.setText(builder);
        switch (requestCode){
            case 200:
                if(mTempUri!=null){
                    //ACTION_MEDIA_SCANNER_SCAN_FILE Deprecated in API level 29
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mPath)));
                }
                FrescoUtils.showThumb(mTempUri,image,image.getWidth(),image.getHeight());
                break;
        }
    }

    public Uri takePhoto() {
        /**                  getExternalCacheDir()     getCacheDir()  Environment.getExternalStorageDirectory()
         * 有读写权限          所有版本可行               7.0及以上可行
         *
         * 无读写权限          所有版本可行               7.0及以上可行
         *
         * ACTION_MEDIA_SCANNER_SCAN_FILE是否能在相册看见   否   否       是
         */
        //getExternalCacheDir()所有版本可行 getCacheDir() 7.0及以上可行。
        //拍照存放路径
        File imagePath = new File(getExternalCacheDir(), "Pictures");
        if (!imagePath.exists()) {
            if(!imagePath.mkdir()){
                ToastUtil.show(this,"创建目录失败");
                return null;
            }
        }
        File newFile = new File(imagePath, "IMG_" + System.currentTimeMillis() + ".jpg");
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //android.os.FileUriExposedException
            contentUri = Uri.fromFile(newFile);
        } else {
            //7.0以上应用间只能用content:// 不管私有内部还是私有外部还是公有外部存储
            contentUri = FileProvider.getUriForFile(this,
                    BaseApplication.getApp().getPackageName() + ".FileProvider", newFile);
        }
        mPath = newFile;
        /**
         * Uri.fromFile 对于拍照程序和安装程序都不可以我们应用访问私有内部存储
         * FileProvider对于拍照程序可以我们应用访问私有内部存储
         *
         * 所有android设备都有两个文件存储区域：内部存储和外部存储。
         * 这些名字来源于android的早期，当时大多数设备都提供内置的非易失性存储器（内部存储器），
         * 以及可移动存储介质，如micro-sd卡（外部存储器）。
         * 许多设备现在将永久存储空间划分为单独的“内部”和“外部”分区。因此，即使没有可移动存储介质，
         * 这两个存储空间始终存在，而且无论外部存储是否可移动，API行为都是相同的。
         */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        // 授予目录临时共享权限 //Intent中migrateExtraStreamToClipData有自动添加这两个权限
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, 200);
        return contentUri;
    }
}