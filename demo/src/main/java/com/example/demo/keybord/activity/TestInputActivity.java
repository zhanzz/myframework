package com.example.demo.keybord.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.keybord.fragment.TCInputTextMsgDialog;
import com.example.demo.keybord.presenter.TestInputPresenter;
import com.example.demo.keybord.view.ITestInputView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestInputActivity extends BaseActivity implements ITestInputView {
    @BindView(R2.id.scroll_view)
    View scrollView;
    @BindView(R2.id.linear_container)
    LinearLayout linearContainer;
    private TestInputPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_input;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {
        //键盘弹出，DecorView的高度不会变，android.R.id.content内容的高度会变化
        /**
         * adjustResize (与上部下部、有无滚动视图无关)将android.R.id.content内容高度变少键盘高度，以容出键盘的展示
         * 收起 getHeight=2244;linearLayout=2159;scrollView=1744;linear=2159
         * 弹出 getHeight=2244;linearLayout=1144;scrollView=729;linear=1144
         *
         * adjustPan  (与上部下部、有无滚动视图无关)将window(windowTop)移动以输入框位置与被键盘挡住之差的距离，以容出键盘的展示
         * 收起 getHeight=2244;linearLayout=2159;scrollView=1744;linear=2159
         * 弹出 getHeight=2244;linearLayout=2159;scrollView=1744;linear=2159
         * y=0;decorViewTransY=0.0;decorViewTop=0;out=-872
         */
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                String content = String.format("getHeight=%s;linearLayout=%s;scrollView=%s;linear=%s",getWindow().getDecorView().getHeight(),linearContainer.getHeight(),scrollView.getHeight(),
                        findViewById(android.R.id.content).getHeight());
                Log.e("zhang", content);
                View view = findViewById(android.R.id.content);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] out = new int[2];
                        view.getLocationOnScreen(out);
                        Log.e("zhang",String.format("y=%s;decorViewTransY=%s;decorViewTop=%s;out=%s",
                                getWindow().getAttributes().y,linearContainer.getTranslationY(),linearContainer.getTop(),out[1]));
                    }
                },300);

            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestInputPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestInputActivity.class);
        context.startActivity(starter);
    }

    @OnClick(R2.id.tv_show_dialog)
    public void onClick() {
        //TestInputFragment.newInstance().showNow(getSupportFragmentManager(),"testInput");
        TCInputTextMsgDialog mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}