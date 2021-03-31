package com.example.demo.product_detail.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.product_detail.adapter.MenuAdapter;
import com.example.demo.product_detail.presenter.DragViewPresenter;
import com.example.demo.product_detail.view.IDragViewView;
import com.framework.common.BaseApplication;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.item_decoration.StaggeredGridHSpaceItemDecoration;
import com.framework.common.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DragViewActivity extends BaseActivity implements IDragViewView {
    @BindView(R2.id.menu_recycler_view)
    RecyclerView menuRecyclerView;
    @BindView(R2.id.move_txt)
    TextView moveTxt;
    private DragViewPresenter mPresenter;
    private MenuAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_drag_view;
    }

    @Override
    public void bindData() {
        List<String> contents = new ArrayList<>();
        for(int i=0;i<20;i++){
            contents.add("我的菜单"+i+ ((i%2==0) ? "菜单":""));
        }
        StaggeredGridHSpaceItemDecoration decoration = new StaggeredGridHSpaceItemDecoration(UIHelper.dipRes2px(R.dimen.dp_10),
                UIHelper.dipRes2px(R.dimen.dp_5),UIHelper.dipRes2px(R.dimen.dp_5));
        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) menuRecyclerView.getLayoutManager();
        if (manager != null) {
            manager.setSpanCount(2);
        }
        menuRecyclerView.addItemDecoration(decoration);
        menuRecyclerView.setBackgroundColor(0xffffff00);
        mAdapter = new MenuAdapter(contents,menuRecyclerView);

        Log.e("zhang",String.format("50=%s;100=%s;120=%s",UIHelper.dipRes2px(R.dimen.dp_50),UIHelper.dipRes2px(R.dimen.dp_100),
                UIHelper.dipRes2px(R.dimen.dp_120)));
        menuRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("zhang","height="+menuRecyclerView.getMeasuredHeight()+";120=="+UIHelper.dipRes2px(R.dimen.dp_120));
            }
        },2000);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new DragViewPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DragViewActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R2.id.move_txt,R2.id.btn_scroll})
    public void onClick(View view) {
        if(R.id.move_txt==view.getId()){
            showToast("点击了");
        }else if(R.id.btn_scroll==view.getId()){
            mAdapter.scrollTo("我的菜单0菜单");
        }
    }
}