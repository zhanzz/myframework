package com.example.demo.glide.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.demo.R;
import com.example.demo.databinding.ActivityStudyGlideBinding;
import com.example.demo.glide.presenter.StudyGlidePresenter;
import com.example.demo.glide.view.IStudyGlideView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyGlideActivity extends BaseActivity implements IStudyGlideView {

    private StudyGlidePresenter mPresenter;

    private ActivityStudyGlideBinding mViewBinding;
    private String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091115%2F5zowkpt3qcr5zowkpt3qcr.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619764653&t=67d5106186608cd36404d5fb69034162";
    @Override
    public int getLayoutId() {
        return R.layout.activity_study_glide;
    }

    @Override
    protected boolean isUseBinding() {
        return true;
    }

    @Override
    public ActivityStudyGlideBinding getViewBinding() {
        if(mViewBinding == null){
            mViewBinding = ActivityStudyGlideBinding.inflate(getLayoutInflater());
        }
        return mViewBinding;
    }

    @Override
    public void bindData() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ResolveInfo info = getPackageManager().resolveActivity(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        Glide.with(this).load(url)
                .placeholder(R.drawable.ic_launcher)
                .centerCrop()
                .into(mViewBinding.ivBox);
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new BitmapImageViewTarget(mViewBinding.ivChang){
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                });
    }

    @Override
    public void initEvent() {
        mViewBinding.ivChang.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Glide.with(StudyGlideActivity.this)
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                                showLoadingDialog();
                            }

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                hideLoadingDialog();

                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                hideLoadingDialog();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                return true;
            }
        });
    }

    @Override
    public StudyGlidePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new StudyGlidePresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyGlideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}