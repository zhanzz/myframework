package com.example.retrofitframemwork.login.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.util.Pools;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo.contact.activity.PhoneListActivity;
import com.example.demo.some_test.activity.TestDiffAndHandlerActivity;
import com.example.demo.viewpager_fragment.activity.PageFragmentActivity;
import com.example.demo.vlayout.activity.StudyVlayoutActivity;
import com.example.demo.widget.TwoLevelRefreshHeader;
import com.example.reactnative.home.activity.RnMainActivity;
import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.TestDialogFragment;
import com.example.retrofitframemwork.login.LoginPresenter;
import com.example.retrofitframemwork.login.adapter.HomeAdapter;
import com.example.retrofitframemwork.login.view.ILoginView;
import com.example.retrofitframemwork.update.activity.UpDateActivity;
import com.framework.common.BaseApplication;
import com.framework.common.BuildConfig;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.data.ConfigOperation;
import com.framework.common.image_select.MultiImageSelectorActivity;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.widget.drawable.ImageLoadingDrawable;
import com.framework.model.UserBean;
import com.framework.model.VersionInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends BaseActivity implements ILoginView {

    LoginPresenter mPresenter;

    Random mRandom = new Random();
    @BindView(R.id.twoLevelHeader)
    TwoLevelRefreshHeader twoLevelHeader;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.image)
    ImageView image;
    private HomeAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getParamData(Intent intent) {
        super.getParamData(intent);
    }

    private String[] menus = new String[]{"vlayout", "fragmentStatePager", "versionUpdate",
            "reactNative", "scan", "testSome", "takePicture","downloadApkAndInstall","selectImage"
    ,"phones","testNetLink","startOtherActivity","SlidingPaneLayout"};

    @Override
    public void bindData() {
        registerBroadcast();
        mAdapter = new HomeAdapter(recyclerView);
        mAdapter.setNewData(Arrays.asList(menus));
        mAdapter.setEnableLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

//        String url = "file://asdfs/saf/test.txt";
//        File file;
//        /*
//        fileName=test.txt;filePath=/data/user/0/com.example.retrofitframemwork/cache/test.txt7702278795384564477.tmp
//         */
//        try {
//            String fileName = Uri.parse(url).getLastPathSegment();
//            file = File.createTempFile(fileName, null, getCacheDir());
//            LogUtil.e("fileName="+fileName+";filePath="+file.getAbsolutePath());
//        } catch (IOException e) {
//            // Error while creating file
//        }
    }

    @Override
    public void initEvent() {
        LogUtil.e("onCreate=" + getClass().getSimpleName() + ";pid=" + Process.myPid());
        //0.34
        //LogUtil.e("String.format=" + String.format("%.2f", 0.336f));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (mAdapter.getItem(position)) {
                case "vlayout":
                    StudyVlayoutActivity.start(getContext());
                    break;
                case "fragmentStatePager":
                    PageFragmentActivity.start(getContext());
                    break;
                case "versionUpdate":
                    mPresenter.checkUpdate();
                    break;
                case "reactNative":
                    RnMainActivity.start(getContext());
                    break;
                case "scan":
                    CaptureActivity.startMeForResult(MainActivity.this, 120);
                    break;
                case "testSome":
                    TestDiffAndHandlerActivity.start(this);
                    break;
                case "takePicture":
                    requestNeedPermissions(Manifest.permission.CAMERA);
                    break;
                case "downloadApkAndInstall":
                    mPresenter.downLoadFile();
                    break;
                case "selectImage":
                    MultiImageSelectorActivity.startMe(this,5,200);
                    break;
                case "phones":
                    PhoneListActivity.start(this,null);
                    break;
                case "testNetLink":
                    mPresenter.testLineRequest();
                    break;
                case "startOtherActivity"://属于不同的进程
                    LogUtil.e("start=pid="+Process.myPid());
                    //ComponentName cn = new ComponentName("com.nanchen.rxjava2examples", "com.nanchen.rxjava2examples.module.rxjava2.operators.item.RxCreateActivity");
                    Intent intent = new Intent("xx");
                    intent.addCategory("android.intent.category.DEFAULT");
                    //intent.setComponent(cn);
                    startActivity(intent);
                    break;
                case "SlidingPaneLayout":
//                    RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            LogUtil.e("zhang","有错误了"+throwable.getMessage());
//                        }
//                    });
                    //Main4Activity.start(this);
//                    Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//                        @Override
//                        public void uncaughtException(Thread t, Throwable e) {
//                            LogUtil.e("zhang","线程自已的处理器"+e.getMessage());
//                        }
//                    });
//                    try {
//                        ObjectHelper.requireNonNull(null, "The mapper returned a null ObservableSource");
//                    } catch (Throwable t) {
//                        //Exceptions.throwIfFatal(t);
//                        RxJavaPlugins.onError(new CompositeException(t, t));
//                        t.printStackTrace();
//                    }
                    Main4Activity.start(this);
                    break;
            }
        });
        twoLevelHeader.setOnTwoLevelListener(refreshLayout -> false);
    }

    @Override
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new LoginPresenter();
        }
        return mPresenter;
    }

    private void typeTwo(Map<String, Object> parmas) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        NetWorkManager.getInstance().getRetorfit(AppInterfaceValue.HOST)
//                .create(AppApi.class)
//                .loginByRxJava(parmas)
//                .compose(SchedulerProvider.getInstance().<String>applySchedulers())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e("zhang", "ok=" + s);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e("zhang", "fail=" + throwable.getMessage());
//                    }
//                });
    }

    private void typeOne(Map<String, Object> parmas) {
//        NetWorkManager.getInstance().getRetorfit(AppInterfaceValue.HOST)
//                .create(AppApi.class)
//                .login(parmas).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("zhang", "ok=" + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("zhang", "fail:" + t.getMessage());
//            }
//        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.tv_show:
//                passPermission("");
//                TestServiceAndNoticeActivity.start(this,null);
//                installApk();
//                mPresenter.downLoadFile();
            //getProcessName(this);
            //mPresenter.login("13695157045","1");
//                showFragment(0);
//                Main2Activity.start(this);
            //SettingActivity.start(this,null);
            //break;
            case R.id.image:
//                RnMainActivity.start(this);
//                requestNeedPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
                mPresenter.checkUpdate();
//                TestServiceAndNoticeActivity.start(this,null);
//                TestDiffAndHandlerActivity.start(this);
//                StudyVlayoutActivity.start(this);
//                TestInputActivity.start(this);
//                ExpandRecyclerViewActivity.start(this);
//                PageFragmentActivity.start(this);
//                Main2Activity.starts(this);
//                MultiImageSelectorActivity.startMe(this,5,200);
//                showFragment(mRandom.nextInt());
                break;
        }
//
//        mPresenter.uploadFile();
//         重新构造Uri：content://
        //requestNeedPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //passPermission("");
        //showFragment(mRandom.nextInt());
//        TestFragment fragment = TestFragment.newInstance(2);
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
//        showLoadingDialog();
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showLoading();
//            }
//        },1000);
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoadingDialog();
//            }
//        },3000);
//        tvShow.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoading();
//            }
//        },6000);
    }

    private void installApk() {
        File dir;
        if (PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            dir = BaseApplication.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        File file = new File(dir.getAbsolutePath(), "framework_test.apk");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BaseApplication.getApp(), BaseApplication.getApp().getPackageName() + ".FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void showFragment(final int position) {
        Log.e("zhang", "ppposition=" + position);
        final int pposition = position;
        if (frament == null) {
            frament = new TestDialogFragment();
        }
        frament.setActionListener(new TestDialogFragment.ActionListener() {
            @Override
            public void onPrice(String price) {
                //Log.e("zhang", "position=" + pposition);
                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        });
        frament.showNow(getSupportFragmentManager(), "changePrice");
    }

    TestDialogFragment frament;

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        }
        if (permissions.contains(Manifest.permission.CAMERA)) {
            File imagePath = new File(getCacheDir(), "images");
            if (!imagePath.exists()) {
                imagePath.mkdirs();
            }
            File newFile = new File(imagePath, "default_image.jpg");
            Uri contentUri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //android.os.FileUriExposedException
                contentUri = Uri.fromFile(newFile);//7.0以上应用间只能用content:// 不管私有内部还是私有外部还是公有外部存储
            } else {
                contentUri = FileProvider.getUriForFile(this,
                        BaseApplication.getApp().getPackageName() + ".FileProvider", newFile);
            }
            /**
             * Uri.fromFile 对于拍照程序和安装程序都不可以我们应用访问私有内部存储
             * FileProvider对于拍照程序可以我们应用访问私有内部存储（而安装程序却不可以）
             */

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            // 授予目录临时共享权限 //Intent中migrateExtraStreamToClipData有自动添加这两个权限
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 100);
        }
//        UserOperation opration = new UserOperation();
//        long start = System.currentTimeMillis();
//        for(int i=0;i<100;i++){
//            opration.setCacheWithoutKeyAsy(content+i);
//        }
//        Log.e("zhang","userTime="+(System.currentTimeMillis()-start));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void loginSucess(UserBean bean) {

    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        //image.setImageBitmap(bitmap);
    }

    @Override
    public void onShowUpdateDialog(VersionInfo bean) {
        UpDateActivity.start(this, bean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//先于restart调用
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    File imagePath = new File(getCacheDir(), "images");
                    if (!imagePath.exists()) {
                        imagePath.mkdirs();
                    }
                    File newFile = new File(imagePath, "default_image.jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    break;
                case 200:
                    List<String> photoPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ListUtils.isEmpty(photoPath)) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("dir", "oaimage");
                        for (int i = 0; i < photoPath.size(); i++) {
                            params.put("img" + i, new File(photoPath.get(i)));
                        }
                        File file = new File(photoPath.get(0));
                        Bitmap fbitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        image.setImageBitmap(fbitmap);
                    }
                    break;
                case 120:
                    String result = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                    ToastUtil.show(this, result);
                    break;
            }
        }
    }

    String content = "";

    public static String getProcessName(Context cxt) {
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    MyBroadCastReciver mReciver;
    CompleteReceiver mDownReciver;

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.frameWork.test");
        mReciver = new MyBroadCastReciver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciver, filter);

        mDownReciver = new CompleteReceiver();
        //getName ==>com.example.retrofitframemwork.login.activity.MainActivity$CompleteReceiver
        //getSimpleName ==> CompleteReceiver
        LogUtil.e("zhang",mDownReciver.getClass().getName());
        registerReceiver(mDownReciver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate="+getClass().getSimpleName());
    }

    public static class MyBroadCastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.show(context, "收到了通知");
        }
    }

    @Override
    protected void onDestroy() {
        if (mReciver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReciver);
        }
        if (mDownReciver != null) {
            unregisterReceiver(mDownReciver);
        }
        LogUtil.e("调用了onDestroy="+getClass().getSimpleName());
        super.onDestroy();
    }

    class CompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // get complete download id
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            ConfigOperation operation = new ConfigOperation();
            if (completeDownloadId == operation.getData().downLoadId) {
                installApk();
            }
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
}
