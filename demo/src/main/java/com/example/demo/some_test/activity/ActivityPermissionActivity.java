package com.example.demo.some_test.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.presenter.ActivityPremissionPresenter;
import com.example.demo.some_test.view.IActivityPremissionView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.StringUtils;
import com.framework.common.utils.ToastUtil;

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
    private ActivityPremissionPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_premission;
    }

    @Override
    public void bindData() {
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
        webView.loadUrl("http://192.168.198.84:8020/myApp/app.html");//
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        StringUtils.setListener(tvTestSpan, content, 0xffffff00, new String[]{"容好"},false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getContext(),"点击了我");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
    }
}