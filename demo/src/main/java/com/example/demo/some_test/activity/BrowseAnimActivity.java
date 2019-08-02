package com.example.demo.some_test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.ChangeBounds;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.presenter.BrowseAnimPresenter;
import com.example.demo.some_test.view.IBrowseAnimView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowseAnimActivity extends BaseActivity implements IBrowseAnimView {
    @BindView(R2.id.tv_anim2)
    TextView tvAnim2;
    @BindView(R2.id.iv_anim2)
    SimpleDraweeView ivAnim2;
    private BrowseAnimPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browse_anim;
    }

    @Override
    public void bindData() {
        ivAnim2.setImageURI("http://imgsrc.baidu.com/imgad/pic/item/d000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg");
    }

    @Override
    public void initEvent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(new SharedElementCallback() {//onMapSharedElements标记当前页面中的共享元素
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    View view = ivAnim2;
                    names.clear();
                    sharedElements.clear();
                    sharedElements.put("image", view);
                }
            });
        }
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new BrowseAnimPresenter();
        }
        return mPresenter;
    }

    /**
     * @param context
     */
    public static void start(Activity context, View view) {
        Intent starter = new Intent(context, BrowseAnimActivity.class);
        if (view != null) {
            context.startActivity(starter, ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "image").toBundle());
        } else {
            context.startActivity(starter);
        }
    }
}