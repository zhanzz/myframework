package com.example.demo.viewpager_fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.viewpager_fragment.adapter.ExpandAdapter;
import com.example.demo.viewpager_fragment.presenter.ExpandRecyclerViewPresenter;
import com.example.demo.viewpager_fragment.view.IExpandRecyclerViewView;
import com.example.demo.widget.ActivityRefreshHeader;
import com.example.demo.widget.MySmartRefreshLayout;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpandRecyclerViewActivity extends BaseActivity implements IExpandRecyclerViewView {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.smartRefreshLayout)
    MySmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.refreshHead)
    ActivityRefreshHeader refreshHead;
    @BindView(R2.id.btn_remove)
    Button btnRemove;
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
        //adapter.expandAll();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (refreshHead.isLoadActivity()) {
                    ToastUtil.show(ExpandRecyclerViewActivity.this, "进入活动页面");
                    smartRefreshLayout.finishRefresh();
                } else {
                    ToastUtil.show(ExpandRecyclerViewActivity.this, "进入刷新中");
                    smartRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            smartRefreshLayout.finishRefresh();
                        }
                    }, 3000);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }
        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.btn_remove)
    public void onClick() {
        TextView v = (TextView) recyclerView.getChildAt(0);
        Log.e("zhang",v.getText()+";"+v.toString());
        recyclerView.removeView(v);
    }
}