package com.example.retrofitframemwork.login.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.alipay.ShowHtmlActivity;
import com.example.demo.anim.activity.AttrAnimActivity;
import com.example.demo.anim.activity.ViewAnimActivity;
import com.example.demo.anim.activity.activity.ViewPager2AnimActivity;
import com.example.demo.anim.activity.activity.ViewPagerAnimActivity;
import com.example.demo.contact.activity.PhoneListActivity;
import com.example.demo.coordinator_layout.activity.CoordinatorLayoutActivity;
import com.example.demo.coordinator_layout.activity.CoordinatorLayoutV2Activity;
import com.example.demo.db.activity.TestSqliteDataBaseActivity;
import com.example.demo.incremental_updating.activity.PatchActivity;
import com.example.demo.keybord.activity.TestInputActivity;
import com.example.demo.pagelist.activity.PageListActivity;
import com.example.demo.product_detail.activity.ProductDetailActivity;
import com.example.demo.recyclerview.activity.CustomManagerActivity;
import com.example.demo.some_test.activity.ActivityPermissionActivity;
import com.example.demo.some_test.activity.TestDiffAndHandlerActivity;
import com.example.demo.viewpager_fragment.activity.PageFragmentActivity;
import com.example.demo.vlayout.activity.StudyVlayoutActivity;
import com.example.demo.widget.TwoLevelRefreshHeader;
import com.example.demo.window.activity.StudyWindowActivity;
import com.example.reactnative.home.activity.RnMainActivity;
import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.TestDialogFragment;
import com.example.retrofitframemwork.login.LoginPresenter;
import com.example.retrofitframemwork.login.adapter.HomeAdapter;
import com.example.retrofitframemwork.login.view.ILoginView;
import com.example.retrofitframemwork.update.activity.UpDateActivity;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.data.ConfigOperation;
import com.framework.common.image_select.MultiImageSelectorActivity;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.DeviceUtils;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.NotificationUtil;
import com.framework.common.utils.TakePhotoUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.UserBean;
import com.framework.model.VersionInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 当代码改了没有反应要clean一下
 * 1035964576@qq.com   wo1035964576
 */
public class MainActivity extends BaseActivity implements ILoginView {

    LoginPresenter mPresenter;

    Random mRandom = new Random();
    @BindView(R.id.twoLevelHeader)
    TwoLevelHeader twoLevelHeader;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.image)
    SimpleDraweeView image;
    private HomeAdapter mAdapter;
    //storage/emulated/0/Android/data/下有.nomedia文件

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getParamData(Intent intent) {
        super.getParamData(intent);
    }

    //onStart后调用
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String content = (String) savedInstanceState.get("save");
        uri = (Uri) savedInstanceState.get("pic_uri");
        ToastUtil.show(this, content);
    }

    private String[] menus = new String[]{"vlayout", "fragmentStatePager", "versionUpdate",
            "reactNative", "scan", "testSome", "takePicture", "downloadApkAndInstall", "selectImage"
            , "phones", "testNetLink", "startOtherActivity", "SlidingPaneLayout", "testArouter", "sendBroadCast"
            , "blueTooth", "testSqliteDatabase", "studyWindow", "add_update", "permission", "pageList", "navigation"
            , "coordinatorLayout", "coordinatorLayoutV2", "customManager", "alipay", "testError","viewAnim","AttrViewAnim"
            ,"showDialogFragment","viewPagerAnim","productDetail"};

    @Override
    public void bindData() {
        String str = "中国";
        LogUtil.e(str);
        registerBroadcast();
        mAdapter = new HomeAdapter(recyclerView);
        mAdapter.setNewData(Arrays.asList(menus));
        mAdapter.setEnableLoadMore(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setBackgroundResource(R.drawable.selector_fff_f3f3f3);
        //OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        int id = R.attr.custom_orientation;
        AssetManager manager = getAssets();
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

        //结果不会受虚拟键是否展示的影响
        DisplayMetrics metric = new DisplayMetrics();
//API 17之后使用，获取的像素宽高包含虚拟键所占空间，在API 17之前通过反射获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        }
        int width = metric.widthPixels;  // 宽度（像素）
        int height = metric.heightPixels;  // 高度（像素）
        LogUtil.e("height",String.format("width=%d,height=%d",width,height));
 //获取的像素宽高不包含虚拟键所占空间
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;  // 宽度（像素）
        height = metric.heightPixels;  // 高度（像素）
        LogUtil.e("height",String.format("two;width=%d,height=%d",width,height));
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = recyclerView.getRootView().getMeasuredHeight();
                int barHeight = AppTools.getStatusBarHeight(MainActivity.this);
                int nHeight = AppTools.getNavigationBarHeight(MainActivity.this);
                //root;height=1920;barHeight=63;navHeight=126
                LogUtil.e("height",String.format("root;height=%d;barHeight=%d;navHeight=%d",height,barHeight,nHeight));
                //getRealHeight = getDisplayHeight+barHeight+navHeight;
            }
        },1000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("save", "mainactivity");
        outState.putParcelable("pic_uri", uri);
        super.onSaveInstanceState(outState);
        LogUtil.e("onSave=" + getClass().getSimpleName());
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
                    //requestNeedPermissions(120, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //requestNeedPermissions(120,Manifest.permission_group.STORAGE);
                    //false
                    //boolean value = ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA);
                    //ToastUtil.show(this,"11");
                    //ToastUtil.show(this,"22");
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
                    //Toast.makeText(this.getApplicationContext(),"11",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this.getApplicationContext(),"22",Toast.LENGTH_SHORT).show();
                    requestNeedPermissions(121, Manifest.permission.CAMERA);
                    break;
                case "downloadApkAndInstall":
                    //mPresenter.downLoadFile();
                    getResources().getString(R.string.bukenen);
                    break;
                case "selectImage":
                    MultiImageSelectorActivity.startMe(this, 5, 200);
                    break;
                case "phones":
                    PhoneListActivity.start(this, null);
                    break;
                case "testNetLink":
                    mPresenter.testLineRequest();
                    break;
                case "startOtherActivity"://属于不同的进程
                    LogUtil.e("start=pid=" + Process.myPid());
                    //ComponentName cn = new ComponentName("com.nanchen.rxjava2examples", "com.nanchen.rxjava2examples.module.rxjava2.operators.item.RxCreateActivity");
                    //intent.setComponent(cn);
                    /**
                     * 隐式跳转总结： https://developer.android.google.cn/guide/components/intents-filters
                     */
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("testscheme:123456"));
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
                    Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            LogUtil.e("zhang", "线程自已的处理器" + e.getMessage());
                        }
                    });
                    try {
                        ObjectHelper.requireNonNull(null, "The mapper returned a null ObservableSource");
                    } catch (Throwable t) {
                        //Exceptions.throwIfFatal(t);
                        RxJavaPlugins.onError(new CompositeException(t, t));
                        t.printStackTrace();
                    }
                    Main4Activity.start(this);
                    //Main2Activity.start(this);
                    break;
                case "testArouter":
                    // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                    //ARouter.getInstance().build("/test/activity").navigation();
                    // 2. 跳转并携带参数
//                    ARouter.getInstance().build("/test/1")
//                            .withLong("key1", 666L)
//                            .withString("key3", "888")
//                            .withObject("key4", new UserBean())
//                            .navigation();
                    Intent intent11 = new Intent();
                    intent11.setData(Uri.parse("zhang://com.zhang.example/activityone?paramsone=8&parmastwo=asdf"));
                    startActivity(intent11);
                    break;
                case "sendBroadCast":
                    //Intent intent1 = new Intent("static_receiver");
                    //intent1.setClass(this, StaticBroadCastReceiver.class);
                    //sendBroadcast(intent1);
                    ToastUtil.show(getContext(), "这是新包");
                    break;
                case "blueTooth":
                    //BlueToothActivity.start(getContext());
                    ToastUtil.show(getContext(), "我真真真的是新包");
                    //TestAddActivity.start(getContext());
                    break;
                case "testSqliteDatabase":
                    TestSqliteDataBaseActivity.start(getContext());
                    break;
                case "studyWindow":
                    StudyWindowActivity.start(this);
                    break;
                case "add_update"://增量更新
                    PatchActivity.start(getContext());
                    break;
                case "permission":
                    ActivityPermissionActivity.start(getContext());
                    break;
                case "pageList":
                    PageListActivity.start(getContext());
                    break;
                case "navigation":
                    com.example.jetpack.nvigation.MainActivity.Companion.startMe(getContext());
                    //moveAppToFront(getContext());
                    //AppTools.isExternalStorageWritable();
                    //showBitmapOne();
                    //createFile("application/vnd.android.package-archive","test.apk");
                    //showBitmapOne();
                    //mPresenter.login("13695157045","1");
                    break;
                case "coordinatorLayout":
                    CoordinatorLayoutActivity.start(getContext());
                    break;
                case "coordinatorLayoutV2":
                    CoordinatorLayoutV2Activity.start(getContext());
                    break;
                case "customManager":
                    CustomManagerActivity.start(getContext());
                    break;
                case "alipay":
                    //ShowHtmlActivity.start(getContext());
                    showBitmapOne();
                    break;
                case "testError":
                    //TextView tv = null;
                    //tv.setText("haha");
                    /*
                     *仅当访问其他应用的文件时才需要权限，自已创建的不需要权限，当应用卸载后又不属于应用了，需要权限
                     */
                    Uri uri = Uri.parse("content://media/external/images/media/294");
                    try {
                        InputStream stream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        if (bitmap != null) {
                            ToastUtil.show(getContext(), "bitmap有值");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "viewAnim":
                    ViewAnimActivity.start(getContext());
                    break;
                case "AttrViewAnim":
                    AttrAnimActivity.start(getContext());
                    break;
                case "showDialogFragment":
                    //showFragment(4);
                    TestInputActivity.start(getContext());
                    break;
                case "viewPagerAnim":
                    //ViewPagerAnimActivity.start(getContext());
                    ViewPager2AnimActivity.start(getContext());
                    break;
                case "productDetail":
                    ProductDetailActivity.start(getContext());
                    break;
            }
        });
        smartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getApplication(), "上拉加载", Toast.LENGTH_SHORT).show();
                refreshLayout.finishLoadMore(2000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getApplication(), "下拉刷新", Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(2000);
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (oldState == RefreshState.TwoLevel) {
                    findViewById(R.id.second_floor_content).animate().alpha(0).setDuration(1000);
                }
            }
        });
        smartRefreshLayout.setEnableLoadMore(true);

        twoLevelHeader.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getApplication(), "打开二楼", Toast.LENGTH_SHORT).show();
                findViewById(R.id.second_floor_content).animate().alpha(1).setDuration(2000);
                return true;
            }
        });
    }

    private void showBitmapOne() {
        //image.setVisibility(View.VISIBLE);
        //调用TakePhotoUtil.takePhotoV2生成了uri却没有拍照，所以没有内容
        //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/IMG_1577687131791.jpg");
        //image.setImageBitmap(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean sl = Environment.isExternalStorageLegacy();
        }
        File file = new File("/storage/emulated/0/Pictures/IMG_1577687131791.jpg");
        boolean has = PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int target = getApplicationInfo().targetSdkVersion;
        File file1 = new File(getExternalCacheDir(), "1.txt");
        /**
         * 结论： 外部私有可以操作File，外部共有无法操作（报FileNotFoundException）
         */
        try {
            //FileOutputStream fileOutputStream = new FileOutputStream(file);
            //OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            FileWriter writer1 = new FileWriter(file1);
            writer1.write("haha");
            writer1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Intent setAlertIntent = new Intent(this, Main2Activity.class);
        setAlertIntent.putExtra("try", "i'm just have a try");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, setAlertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent = new Intent();
        intent.putExtra("a", pendingIntent);
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
    Uri uri;

    @Override
    public void passPermission(@NonNull List<String> permissions, int requestCode) {
        /***
         * 如果请求WRITE_EXTERNAL_STORAGE权限通过会同时授予READ_EXTERNAL_STORAGE
         * 如果请求READ_EXTERNAL_STORAGE权限，8.0之下会同时授予WRITE_EXTERNAL_STORAGE，8.0及以上不会
         */
        if (requestCode == 120 && permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            boolean ishas = PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ToastUtil.show(this, ishas + "");
            //mPresenter.checkUpdate();
        }
        if (requestCode == 121 && permissions.contains(Manifest.permission.CAMERA)) {
            showToast("通过了");
            //takePhoto();
            //uri = TakePhotoUtil.takePhotoV3(this, 1111);
            //uri = TakePhotoUtil.takePhotoV3(this,1111);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                File[] files = getExternalMediaDirs();
//                getExternalCacheDir();
//            }
        }
//        UserOperation opration = new UserOperation();
//        long start = System.currentTimeMillis();
//        for(int i=0;i<100;i++){
//            opration.setCacheWithoutKeyAsy(content+i);
//        }
//        Log.e("zhang","userTime="+(System.currentTimeMillis()-start));
    }

    @Override
    public void failPermission(@NonNull List<String> permissions, int requestCode) {
        super.failPermission(permissions, requestCode);
        if (requestCode == 121 && permissions.contains(Manifest.permission.CAMERA)) {
            showToast("没有通过");
        }
    }

    public void takePhoto() {
        File imagePath = new File(getExternalCacheDir(), "images");
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
        File newFile = new File(imagePath, "default_image.jpg");
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //android.os.FileUriExposedException
            contentUri = Uri.fromFile(newFile);
        } else {
            //7.0以上应用间只能用content:// 不管私有内部还是私有外部还是公有外部存储
            contentUri = FileProvider.getUriForFile(this,
                    BaseApplication.getApp().getPackageName() + ".FileProvider", newFile);
        }
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
        startActivityForResult(intent, 100);
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
                    File imagePath = new File(getExternalCacheDir(), "images");
                    if (!imagePath.exists()) {
                        imagePath.mkdirs();
                    }
                    File newFile = new File(imagePath, "default_image.jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
                    if (bitmap != null) {
                        ToastUtil.show(getContext(), "bitmap有值");
                    }
                    //image.setImageBitmap(bitmap);
                    break;
                case 1111:
//                    if(AppTools.isQOrHigher()){
//                    try {
//                        ParcelFileDescriptor pd = getContentResolver().openFileDescriptor(uri, "r");
//                        if (pd != null) {
//                            Bitmap bitmap1 = BitmapFactory.decodeFileDescriptor(pd.getFileDescriptor());
//                            image.setVisibility(View.VISIBLE);
//                            image.setImageBitmap(bitmap1);
//                            pd.close();
//                        }
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    }else{
                    //ProducerSequenceFactory -->getBasicDecodedImageSequence
                    image.setVisibility(View.VISIBLE);
                    FrescoUtils.showThumb(uri, image, new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                        }

                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                        }
                    });
                    break;
                case 200:
                    List<Uri> photoPath = data.getParcelableArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ListUtils.isEmpty(photoPath)) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("dir", "oaimage");
                        for (int i = 0; i < photoPath.size(); i++) {
                            params.put("img" + i, photoPath.get(i));
                        }
                        //Bitmap fbitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        //image.setImageBitmap(fbitmap);
                        //mPresenter.uploadFile(params);
                        try {
                            InputStream in = getContentResolver().openInputStream(photoPath.get(0));
                            long size = in.available();//842946
                            in.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 120:
                    String result = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
                    ToastUtil.show(this, result);
                    break;
                case WRITE_REQUEST_CODE:
                    Uri uri = data.getData();
                    break;
            }
        } else {
            switch (requestCode) {
                case 1111:
                    getContentResolver().delete(uri, null, null);
                    break;
                case WRITE_REQUEST_CODE:
                    Uri uri = data.getData();
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
        //可以共用同一个mReciver
        mReciver = new MyBroadCastReciver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciver, filter);
        registerReceiver(mReciver, filter);

        mDownReciver = new CompleteReceiver();
        //getName ==>com.example.retrofitframemwork.login.activity.MainActivity$CompleteReceiver
        //getSimpleName ==> CompleteReceiver
        //LogUtil.e("zhang",mDownReciver.getClass().getName());
        registerReceiver(mDownReciver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("onCreate=" + getClass().getSimpleName());
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
            unregisterReceiver(mReciver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReciver);
        }
        if (mDownReciver != null) {
            unregisterReceiver(mDownReciver);
        }
        LogUtil.e("调用了onDestroy=" + getClass().getSimpleName());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static final int WRITE_REQUEST_CODE = 43;

    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private void showNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //启动通知Activity时，拉起主页面Activity
        Intent msgIntent = new Intent();
        msgIntent.setClass(this, com.example.demo.viewpager_fragment.Main2Activity.class);


        Intent secondIntent = new Intent(this, com.example.demo.viewpager_fragment.Main2Activity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);
        Intent[] intents = new Intent[]{mainIntent, secondIntent};
        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //mainIntent.setAction(Intent.ACTION_MAIN);
        //mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        ;
        NotificationUtil.ChannelConfig.Builder builder = new NotificationUtil.ChannelConfig.Builder();
        builder.setChannelId("normal");
        builder.setChannelName("普通通知");
        String channelId = NotificationUtil.createChannelId(this, builder.build());
        // create and send notificaiton
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)//自己维护通知的消失
                .setContentTitle("我是标题")
                .setTicker("我是ticker")
                .setContentText("我是内容")
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        //将一个Notification变成悬挂式Notification
        mBuilder.setFullScreenIntent(pendingIntent, true);
        Notification notification = mBuilder.build();
        manager.notify(100, notification);
    }
}
