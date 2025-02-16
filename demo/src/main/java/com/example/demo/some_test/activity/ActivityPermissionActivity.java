package com.example.demo.some_test.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.presenter.ActivityPremissionPresenter;
import com.example.demo.some_test.view.IActivityPremissionView;
import com.example.demo.widget.MyLinearLayout;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.StringUtils;
import com.framework.common.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityPermissionActivity extends BaseActivity implements IActivityPremissionView {
    @BindView(R2.id.btn_get_apk)
    Button btnGetApk;
    @BindView(R2.id.webView)
    WebView webView;
    @BindView(R2.id.btn_reload)
    Button btnReload;
    @BindView(R2.id.tv_test_span)
    TextView tvTestSpan;
    @BindView(R2.id.linear_container)
    MyLinearLayout linearContainer;
    @BindView(R2.id.rootSwipe)
    SwipeRefreshLayout rootSwipe;
    @BindView(R2.id.tv_change_type)
    TextView tvChangeType;
    private ActivityPremissionPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_premission;
    }

    @Override
    public void bindData() {
        //rootSwipe.canChildScrollUp();
        LogUtil.e("zhang", getClass().getSimpleName() + ";" + getTaskId() + ";pid=" + Process.myPid());
        /**
         * 当新的url即将加载到当前webview中时，
         * 给宿主应用程序一个接管控制权的机会。
         * 如果未提供webviewclient，默认情况下webview将要求活动管理器为url选择适当的处理程序。
         * 如果提供webviewclient，return{@code true}表示宿主应用程序处理url，
         * return{@code false}表示当前webview处理url。
         *
         * 不设置webviewclient，tel:1456 打电话可以，zhang://com.zhang.example/activityone?paramsone=8&parmastwo=asdf
         * 打开自定义应用不行
         *
         * 设置webviewclient都不行了，要自已处理
         */
        webView.loadUrl("http://192.168.1.19:8020/myApp/app.html");//
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //调用view.loadUrl会破坏Referer头
                if (url.startsWith("http")) {
                    return false;
                } else {
                    // 以下固定写法,表示跳转到第三方应用
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return true;
                }
            }
        });
        String content = "我是内容好吧";
        StringUtils.setListener(tvTestSpan, content, 0xffffff00, new String[]{"容好"}, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getContext(), "点击了我");
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ActivityPremissionPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ActivityPermissionActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R2.id.btn_get_apk, R2.id.btn_reload})
    public void onClick(View view) {
        /*Intent intent = new Intent();
        File file = new File(BaseApplication.getApp().getCacheDir(),"test.apk");
        Uri uri;
        uri = FileProvider.getUriForFile(BaseApplication.getApp(), BaseApplication.getApp().getPackageName() + ".FileProvider", file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setData(uri);
        setResult(RESULT_OK,intent);
        finish();*/
        /*if(view.getId()==R.id.btn_reload){
            //webView.reload();
            Intent intent = new Intent(this, Main2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
        //requestNeedPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        //LogUtil.e("permission", "value=" + ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final String[] gender = new String[]{"帅哥","美女"};
//                            AlertDialog.Builder builder1=new AlertDialog.Builder(getContext());
//                            builder1.setTitle("请选择你的性别");
//                            builder1.setItems(gender, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            builder1.show();
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        /**
         * android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@1f0bc76 is not valid; is your activity running?
         *         at android.view.ViewRootImpl.setView(ViewRootImpl.java:859)
         */
        //finish();
        //linearContainer.detachViewFromParent(btnReload);
        //linearContainer.removeAllViewsInLayout();

        /**
         * 简单来说ViewGroup 维护一个子类的View 数组
         *
         * attachViewToParent 和 DetachViewToparent 是直接操作这个数组，不会去调用requestlayout 去重绘。
         * removeView 和 addView 会主动调用requestlayout 和 invalidate 去强制重绘。
         * removeViewInLayout 和 addViewInLayout 不会去调用 requestLayout 和 invalidate 所以可以有效的在onlayout方法中调用。
         *
         * 2 和 3 都会通过 addViewInner 和 removeViewInternal 去操作
         * 第一种方式 通过 addInArray 和 removeFromArray 直接去 修改 子view的数组
         *
         * 如果是通过 addviewinner 和 removeViewInternal 会促发 layouttransition 做动画效果 还会 触发一些回调
         * 并调用 子view的attachwindow 和 detachwindow 然后 也会进行 addInArray 和 removeFromArray 去修改数组。
         *
         * 所以如果 只是单纯的数据变化 使用 attachViewtoParent 和 detachViewtoParent 不会对 子view 做变化。
         * 只是简单的移出 viewGroup的显示 attach 还是最好 配合 detach使用 因为 添加一个子view应该还需要做
         * 下子viewAttach操作 具体 相关代码 可能在attachInfo 之中找到关
         *
         * 然后这个子view数组 对viewgroup显示有什么影响呢 ？
         * viewgroup dispatchdraw 会使用 这个来对每个子view分派draw事件
         */
        tvChangeType.setSelected(!tvChangeType.isSelected());
        tvChangeType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.selector_small_black_arrow, 0);
    }

    @OnClick(R2.id.frame_click)
    public void onClick() {
        //ToastUtil.show(this, "点击了");
        //moveAppToFront(this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    private void moveAppToFront(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(10);
        ComponentName cn = runningTasks.get(0).topActivity;
        if (cn.getPackageName().equals(context.getPackageName())) {
            //activityManager.moveTaskToFront(taskInfo.id, 0)
//                break}
        }
    }


    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                File file = new File(path);
                try {
                    FileInputStream outputStream = new FileInputStream(file);
                    int x = outputStream.read();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tvChangeType.setText(path);
                Toast.makeText(getContext(),path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                File file = new File(path);
                try {
                    FileInputStream outputStream = new FileInputStream(file);
                    int x = outputStream.read();
                    outputStream.close();
                } catch (FileNotFoundException e) {//会报文件找不到异常
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tvChangeType.setText(path);
                Toast.makeText(getContext(),path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                tvChangeType.setText(path);
                Toast.makeText(ActivityPermissionActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("zhang","Permison=onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("zhang","Permison=onStop");
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("zhang","Permison=onDestroy");
    }
}