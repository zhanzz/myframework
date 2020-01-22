package com.example.demo.coordinator_layout.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.coordinator_layout.adapter.TestBehaviorAdapter;
import com.example.demo.coordinator_layout.presenter.CoordinatorLayoutPresenter;
import com.example.demo.coordinator_layout.view.ICoordinatorLayoutView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.item_decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoordinatorLayoutActivity extends BaseActivity implements ICoordinatorLayoutView {
    @BindView(R2.id.my_list)
    RecyclerView myList;
    private CoordinatorLayoutPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coordinator_layout;
    }

    @Override
    public void bindData() {
        TestBehaviorAdapter adapter = new TestBehaviorAdapter(myList);
        List<String> list = new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add("item"+i);
        }
        adapter.setNewData(list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        myList.addItemDecoration(dividerItemDecoration);
        myList.setAdapter(adapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new CoordinatorLayoutPresenter();
        }
        return mPresenter;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CoordinatorLayoutActivity.class);
        context.startActivity(starter);
    }
}