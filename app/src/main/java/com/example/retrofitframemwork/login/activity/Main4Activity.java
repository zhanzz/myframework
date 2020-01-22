package com.example.retrofitframemwork.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.presenter.Main4Presenter;
import com.example.retrofitframemwork.login.view.IMain4View;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.StringUtils;
import com.framework.common.utils.ToastUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main4Activity extends BaseActivity implements IMain4View {
    @BindView(R.id.btn_4jump)
    Button btn4jump;
    @BindView(R.id.slidingPanelLayout)
    SlidingPaneLayout slidingPanelLayout;
    @BindView(R.id.iv_matrix)
    ImageView ivMatrix;
    @BindView(R.id.tv_test_span)
    TextView tvTestSpan;
    @BindView(R.id.tv_test_span2)
    TextView tvTestSpan2;
    private Main4Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main4;
    }

    @Override
    public void bindData() {
        LogUtil.e("zhang", "oncreate;"+getClass().getSimpleName() + ";" + getTaskId() + ";pid=" + Process.myPid());
        slidingPanelLayout.setSliderFadeColor(Color.TRANSPARENT);//设置面板的侧滑渐变色
        slidingPanelLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //
            }

            @Override
            public void onPanelOpened(View panel) {

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.e("zhang", "onNewIntent;"+getClass().getSimpleName() + ";" + getTaskId() + ";pid=" + Process.myPid());
        super.onNewIntent(intent);
    }

    @Override
    public void initEvent() {
        tvTestSpan.clearComposingText();
        String content = String.format(Locale.CHINA, "%s笔(详情)", 0);
        StringUtils.setListener(tvTestSpan, content, 0xff0000ff, new String[]{"详情)"},true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestSpan.setTextColor(0xff0000ff);
                ToastUtil.show(getContext(), "点击了我");
            }
        });
        StringUtils.setListener(tvTestSpan2, content, 0xfff88210, new String[]{"(详情)"}, false,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getContext(), "点击了我");
            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new Main4Presenter();
        }
        return mPresenter;
    }

    @OnClick(R.id.btn_4jump)
    public void onClick() {
        Main2Activity.start(this,2);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main4Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}