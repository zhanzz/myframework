package com.example.demo.alipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.example.demo.R;
import com.framework.common.utils.ToastUtil;

public class ShowHtmlActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle extras = null;
//        try {
//            extras = getIntent().getExtras();
//        } catch (Exception e) {
//            finish();
//            return;
//        }
//        if (extras == null) {
//            finish();
//            return;
//        }
//        String url = null;
//        try {
//            url = extras.getString("url");
//        } catch (Exception e) {
//            finish();
//            return;
//        }
//        if (TextUtils.isEmpty(url)) {
//            // 测试H5支付，必须设置要打开的url网站
//            new AlertDialog.Builder(ShowHtmlActivity.this).setTitle("警告")
//                    .setMessage("必须配置需要打开的url 站点，请在PayDemoActivity类的h5Pay中配置")
//                    .setPositiveButton("确定", new OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            finish();
//                        }
//                    }).show();
//
//        }
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout, params);

        mWebView = new WebView(getApplicationContext());
        params.weight = 1;
        mWebView.setVisibility(View.VISIBLE);
        layout.addView(mWebView, params);

        WebSettings settings = mWebView.getSettings();
        settings.setRenderPriority(RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        //mWebView.loadUrl(url);
        String content = "<html><form id='submit' name='submit' action='https://openapi.alipaydev.com/gateway.do?charset=utf-8' method='POST' style='display:none;'><input  name='method' value='alipay.trade.wap.pay'/><input  name='version' value='1.0'/><input  name='app_id' value='2016101800715503'/><input  name='format' value='json'/><input  name='timestamp' value='2020-02-21 10:22:36'/><input  name='sign_type' value='RSA2'/><input  name='notify_url' value='http://*/notify/alipay/wappay'/><input  name='charset' value='utf-8'/><input  name='return_url' value='http://*/alipay/wappayreturn'/><input  name='biz_content' value='{\"body\":\"手机网站支付描述信息\",\"out_trade_no\":\"20200221102010275\",\"product_code\":\"QUICK_WAP_WAY\",\"subject\":\"手机网站支付测试\",\"total_amount\":\"0.01\"}'/><input  name='sign' value='X+KKgi/6X7j/sAxru20jRSVL3FMfPRMNEA9CJrJhwCw/jBZMTUPLamGp1oBH4cc4bQ1RFx9ikcEYd7UfY8S+0rV219cyJ16csvXjsU6xgvmniixrevSYo88ByUJfg2ZMUvHzHbNJ9liEV4rgp6CD1D/bC7mM9c62egiQR1gjDSuPtCBo+FIybx0AXNCeCrFjW22+hMXuMyB8hPIrZlSr5vBYGTnDWJID73ya2JEiqBZ8TCjdfuKZp9qcy9wt7H14lU0Ljf1F0TBfgByAIK8z3FnK+vbPsRuZuhg2njKlHMUNCzts4VdPpkh6PMFpNRQ3ci5nG/DFHMI62t3UQpNj6A=='/><input type='submit' style='display:none;'></form><script>document.forms['submit'].submit();</script></html>";
        mWebView.loadDataWithBaseURL("",content,"text/html","UTF-8","");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                try {
                    ToastUtil.show(ShowHtmlActivity.this,"我要启动了");
                    // 以下固定写法,表示跳转到第三方应用
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                }
                return true;
            }

            /**
             * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
             */
            final PayTask task = new PayTask(ShowHtmlActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    final String url=result.getReturnUrl();
                    if(!TextUtils.isEmpty(url)){
                        ShowHtmlActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl(url);
                            }
                        });
                    }
                }
            });

            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if(isIntercepted){
                return true;
            }else {
                return false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }

    public static void start(Context context) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        Intent starter = new Intent(context, ShowHtmlActivity.class);
        context.startActivity(starter);
    }
}
