package com.example.demo.anim.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.anim.AnimatorPath.AnimatorPath;
import com.example.demo.anim.AnimatorPath.PathEvaluator;
import com.example.demo.anim.AnimatorPath.PathPoint;
import com.example.demo.anim.presenter.PathAnimPresenter;
import com.example.demo.anim.view.IPathAnimView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PathAnimActivity extends BaseActivity implements IPathAnimView {
    @BindView(R2.id.fab)
    FloatingActionButton fab;
    private PathAnimPresenter mPresenter;

    private AnimatorPath path;//声明动画集合

    @Override
    public int getLayoutId() {
        return R.layout.activity_path_anim;
    }

    @Override
    public void bindData() {
        setPath();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new PathAnimPresenter();
        }
        return mPresenter;
    }

    /*设置动画路径*/
    public void setPath() {
        path = new AnimatorPath();
        path.moveTo(0, 0);
        path.lineTo(400, 400);
        path.secondBesselCurveTo(600, 200, 800, 400); //订单
        path.thirdBesselCurveTo(100, 600, 900, 1000, 200, 1200);
    }

    /**
     * 设置动画
     *
     * @param view         使用动画的View
     * @param propertyName 属性名字
     * @param path         动画路径集合
     */
    private void startAnimatorPath(View view, String propertyName, AnimatorPath path) {
        ObjectAnimator anim = ObjectAnimator.ofObject(this, propertyName, new PathEvaluator(), path.getPoints().toArray());
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(3000);
        anim.start();
    }

    /**
     * 设置View的属性通过ObjectAnimator.ofObject()的反射机制来调用
     *
     * @param newLoc
     */
    public void setFab(PathPoint newLoc) {
        fab.setTranslationX(newLoc.mX);
        fab.setTranslationY(newLoc.mY);
    }

    @OnClick(R2.id.fab)
    public void onClick() {
        //startAnimatorPath(fab, "fab", path);
        startAnimatorPath(fab);
    }

    private void startAnimatorPath(View view) {
        Path path = new Path();
        path.moveTo(60,60);
        path.lineTo(460,460);
        path.quadTo(660, 260, 860, 460); //订单
        path.cubicTo(160,660,960,1060,260,1260);
        ObjectAnimator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ObjectAnimator.ofFloat(view, "translationX","translationY", path);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(3000);
            anim.start();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PathAnimActivity.class);
        context.startActivity(starter);
    }
}