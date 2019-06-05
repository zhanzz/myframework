package com.example.demo.viewpager_fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.adapter.ExpandAdapter;
import com.example.demo.viewpager_fragment.adapter.SectionAdapter;
import com.example.demo.viewpager_fragment.presenter.ExpandRecyclerViewPresenter;
import com.example.demo.viewpager_fragment.view.IExpandRecyclerViewView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandRecyclerViewActivity extends BaseActivity implements IExpandRecyclerViewView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private ExpandRecyclerViewPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_expand_recycler_view;
    }

    @Override
    public void bindData() {
        //List<SectionEntity> list = mPresenter.getData();
        //SectionAdapter adapter = new SectionAdapter(list);
        List<MultiItemEntity> multi = mPresenter.getMultiData();
        ExpandAdapter adapter = new ExpandAdapter(multi);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ExpandRecyclerViewPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ExpandRecyclerViewActivity.class);
        context.startActivity(starter);
    }
}