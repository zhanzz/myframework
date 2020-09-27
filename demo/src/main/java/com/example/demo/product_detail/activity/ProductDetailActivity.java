package com.example.demo.product_detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.product_detail.adapter.TestAdapter;
import com.example.demo.widget.CountDownButton;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.product_detail.presenter.ProductDetailPresenter;
import com.example.demo.product_detail.view.IProductDetailView;
import com.example.demo.R;

public class ProductDetailActivity extends BaseActivity implements IProductDetailView {
    private ProductDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(0x00000000);
//            getWindow().setNavigationBarColor(0x00000000);
//        }
        //会到状态栏
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //会到状态栏
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //会到状态栏
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    public void bindData() {
        //此段代码可处理scrollView中焦点视图移动
//        ScrollView scrollView = findViewById(R.id.scrollView);
//        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        scrollView.setFocusable(true);
//        scrollView.setFocusableInTouchMode(true);
//        scrollView.setOnTouchListener((v, event) -> {
//            v.requestFocusFromTouch();
//            return false;
//        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TestAdapter adapter = new TestAdapter();
        recyclerView.setAdapter(adapter);
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("https://www.jianshu.com/p/3682dde60dbf");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
    }

    @Override
    public void initEvent() {

    }

    public void startCount(View view){
        ((CountDownButton)view).start();
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ProductDetailPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ProductDetailActivity.class);
        context.startActivity(starter);
    }
}