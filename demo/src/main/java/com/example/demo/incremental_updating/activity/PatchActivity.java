package com.example.demo.incremental_updating.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.base_mvp.BaseActivity;
import com.example.demo.incremental_updating.presenter.PatchPresenter;
import com.example.demo.incremental_updating.view.IPatchView;
import com.example.demo.R;

public class PatchActivity extends BaseActivity implements IPatchView {
    private PatchPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patch;
    }

    @Override
    public void bindData() {

    }

    @Override
    public void initEvent() {
        View vi = findViewById(R.id.tv_num);
        vi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("zhang",String.format("rowx=%s;rowy=%s",event.getRawX(),event.getRawY()));
                return true;
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void startUpdate(View view){
        mPresenter.downLoadPatch();
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PatchPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PatchActivity.class);
        context.startActivity(starter);
    }
}