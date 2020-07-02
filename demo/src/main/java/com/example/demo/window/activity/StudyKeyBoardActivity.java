package com.example.demo.window.activity;

import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;
import androidx.print.PrintHelper;

import com.example.demo.databinding.ActivityStudyKeyBoardBinding;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.window.presenter.StudyKeyBoardPresenter;
import com.example.demo.window.view.IStudyKeyBoardView;
import com.example.demo.R;

public class StudyKeyBoardActivity extends BaseActivity implements IStudyKeyBoardView {
    private StudyKeyBoardPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_study_key_board;
    }

    @Override
    public void bindData() {
        PrintHelper he;
    }

    @Override
    public void initEvent() {
        //ActivityStudyKeyBoardBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_study_key_board)
        //ActivityStudyKeyBoardBinding binding2 = ActivityStudyKeyBoardBinding.inflate(getLayoutInflater());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new StudyKeyBoardPresenter();
        }
        return mPresenter;
    }
}