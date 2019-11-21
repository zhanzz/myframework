package com.example.demo.keybord.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demo.R;
import com.example.demo.R2;
import com.framework.common.base_mvp.BaseDialog;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class TestInputFragment extends BaseDialog {

    @BindView(R2.id.edit)
    EditText edit;
    @BindView(R2.id.image)
    ImageView image;
    Unbinder unbinder;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void setLayoutParams(WindowManager.LayoutParams params) {
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = UIHelper.dip2px(100);
        params.gravity = Gravity.CENTER;
        params.windowAnimations = 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test_input;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {
//        edit.requestFocus();
//        edit.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
//                mInputMethodManager.showSoftInput(edit, 0);
//            }
//        },300);

    }

    public static TestInputFragment newInstance() {
        Bundle args = new Bundle();
        TestInputFragment fragment = new TestInputFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
