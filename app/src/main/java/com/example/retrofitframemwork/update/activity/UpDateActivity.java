package com.example.retrofitframemwork.update.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.retrofitframemwork.BuildConfig;
import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.adapter.UpDateAdapter;
import com.example.retrofitframemwork.login.service.UpdateService;
import com.example.retrofitframemwork.update.presenter.UpDatePresenter;
import com.example.retrofitframemwork.update.view.IUpDateView;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.data.ConfigBean;
import com.framework.common.data.ConfigOperation;
import com.framework.common.manager.PermissionManager;
import com.framework.common.utils.AppTools;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.ToastUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.VersionInfo;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

public class UpDateActivity extends BaseActivity implements IUpDateView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    private UpDatePresenter mPresenter;
    private VersionInfo mVersionInfo;
    public final static String APK_FILE_NAME = "framework_test.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.alpha_in, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_up_date;
    }

    @Override
    public void getParamData(Intent intent) {
        mVersionInfo = (VersionInfo) intent.getSerializableExtra("bean");
    }

    @Override
    public void bindData() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.5f;
        lp.width = UIHelper.getDisplayWidth() * 3 / 4;
        lp.windowAnimations = R.style.DialogAnimation;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UpDateAdapter(mVersionInfo.getList()));

        setFinishOnTouchOutside(false);//点击空白处，窗体不关闭
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.alpha_out);
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new UpDatePresenter();
        }
        return mPresenter;
    }

    @OnClick({R.id.tv_update, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_update://普通更新
                upDataByService();
                //if (updateByDownLoadManager()) return;
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    private void upDataByService() {
        File apkFileDir;
        if (PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            apkFileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            apkFileDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        if(apkFileDir==null){
            ToastUtil.show(this,"存储不可用");
            return;
        }
        File apkFile = new File(apkFileDir,APK_FILE_NAME);
        if (!checkoutNeedDownLoad(apkFile.getAbsolutePath())) {
            installApk();
        } else {
            Intent intent = new Intent(this, UpdateService.class);
            intent.putExtra(UpdateService.EXTRA_DIRECTORY,apkFileDir.getAbsolutePath());
            intent.putExtra(UpdateService.EXTRA_FILE_NAME,APK_FILE_NAME);
            intent.putExtra(UpdateService.EXTRA_URL,mVersionInfo.getUrl());
            startService(intent);
        }
    }

    private boolean updateByDownLoadManager() {
        if (!downLoadMangerIsEnable(this)) {
            openSetting();
            return true;
        }
        File apkFile;
        if (PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
        } else {
            apkFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
        }
        DownloadManager mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (!checkoutNeedDownLoad(apkFile.getAbsolutePath())) {
            installApk();
            finish();
            return true;
        } else {
            //有新的下载了，清除旧的包
            ConfigOperation operation = new ConfigOperation();
            ConfigBean bean = operation.getData();
            if (bean.downLoadId > 0) {
                mDownloadManager.remove(bean.downLoadId);
            }
        }
        String apkUrl = mVersionInfo.getUrl();
        Uri resource = Uri.parse(apkUrl);
        DownloadManager.Request request = new DownloadManager.Request(resource);
        request.setMimeType("application/vnd.android.package-archive");
        //下载的本地路径，表示设置下载地址为SD卡的Download文件夹，文件名为mobileqq_android.apk。
        if (!isExternalStorageWritable()) {
            ToastUtil.show(this, "无可用的存储空间！");
            return true;
        }
        if (PermissionManager.getInstance().hasPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME);
        } else {
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME);
        }
        //一些非必要的设置
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(getString(R.string.app_name) + "版本更新");
        long downLoadId = mDownloadManager.enqueue(request);
        ConfigOperation operation = new ConfigOperation();
        ConfigBean bean = operation.getData();
        bean.downLoadId = downLoadId;
        operation.setData(bean);
        return false;
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

    private boolean checkoutNeedDownLoad(String absolutePath) {
        int code = AppTools.getApkCode(this, absolutePath);
        int serviceCode = mVersionInfo.getVersioncode();
        return serviceCode > code;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 下载管理器是否可用
     *
     * @param context
     * @return
     */
    private boolean downLoadMangerIsEnable(Context context) {
        int state = context.getApplicationContext().getPackageManager()
                .getApplicationEnabledSetting("com.android.providers.downloads");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                    state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED);
        } else {
            return !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                    state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER);
        }
    }

    private void openSetting() {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + "com.android.providers.downloads"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }

    public static void start(Context context, VersionInfo info) {
        Intent starter = new Intent(context, UpDateActivity.class);
        starter.putExtra("bean", info);
        context.startActivity(starter);
    }
}