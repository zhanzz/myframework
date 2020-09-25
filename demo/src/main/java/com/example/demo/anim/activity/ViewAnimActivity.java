package com.example.demo.anim.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.anim.SplashScreen;
import com.example.demo.anim.presenter.ViewAnimPresenter;
import com.example.demo.anim.view.IViewAnimView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAnimActivity extends BaseActivity implements IViewAnimView {
    @BindView(R2.id.frame_person)
    ImageView framePerson;
    @BindView(R2.id.translation_person)
    ImageView translationPerson;
    @BindView(R2.id.rotate_person)
    ImageView rotatePerson;
    @BindView(R2.id.alpha_person)
    ImageView alphaPerson;
    @BindView(R2.id.scale_person)
    ImageView scalePerson;
    private ViewAnimPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_anim;
    }

    @Override
    public void bindData() {
        setTitle("视图动画");

        framePerson.setImageResource(R.drawable.frame_person_anim);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) framePerson.getDrawable();
        animationDrawable1.start();

        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_person);
        hyperspaceJump.setStartOffset(3000);
        translationPerson.startAnimation(hyperspaceJump);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_person);
        rotate.setStartOffset(3000);
        rotatePerson.startAnimation(rotate);

        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha_person);
        alpha.setStartOffset(3000);
        alphaPerson.startAnimation(alpha);

        //fillBefore 一start就为这个值
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_person);
        scale.setStartOffset(3000);
        scalePerson.startAnimation(scale);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ViewAnimPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ViewAnimActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}