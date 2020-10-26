package com.example.demo.coordinator_layout.activity;

import android.content.Context;
import android.content.Intent;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.coordinator_layout.presenter.MoveTopPresenter;
import com.example.demo.coordinator_layout.view.IMoveTopView;
import com.example.demo.R;

public class MoveTopActivity extends BaseActivity implements IMoveTopView {
    private MoveTopPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_move_top;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MoveTopPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MoveTopActivity.class);
        context.startActivity(starter);
    }
}