package com.example.demo.some_test.activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Button;
import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.adapter.TestDiffAdapter;
import com.example.demo.some_test.presenter.TestDiffAndHandlerPresenter;
import com.example.demo.some_test.view.ITestDiffAndHandlerView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.LogUtil;
import com.framework.model.demo.TestDiffBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestDiffAndHandlerActivity extends BaseActivity implements ITestDiffAndHandlerView {
    @BindView(R2.id.btn_change)
    Button btnChange;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private TestDiffAndHandlerPresenter mPresenter;
    private TestDiffAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_test_diff_and_handler;
    }
    AdSwitchTask adSwitchTask;
    @Override
    public void bindData() {
        List<TestDiffBean> list = new ArrayList<>();
        for(int i=0,count=5;i<count;i++){
            list.add(new TestDiffBean(i,"StringContent"+i));
        }
        mAdapter = new TestDiffAdapter();
        mAdapter.submitList(list);
        mAdapter.setDatas(list);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                LogUtil.e("回收了");
            }
        });

        btnChange.setText("0");
        adSwitchTask = new AdSwitchTask(btnChange);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestDiffAndHandlerPresenter();
        }
        return mPresenter;
    }

    @OnClick(R2.id.btn_change)
    public void onClick() {
//        List<TestDiffBean> old = mAdapter.getDatas();
//        List<TestDiffBean> newList = new ArrayList<>(old);
//        for(int i=5,count=10;i<count;i++){
//            newList.add(new TestDiffBean(i,"StringContent"+i));
//        }
//        newList.set(0,new TestDiffBean(0,"我是改变的内容"));
//        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtil(old,newList),false);
//        //mAdapter.setDatas(newList);//以新数据为依据
//        //diffResult.dispatchUpdatesTo(mAdapter);
//        mAdapter.submitList(newList);
//        btnChange.removeCallbacks(adSwitchTask);
//        btnChange.postDelayed(adSwitchTask,5000);
        mAdapter.notifyItemChanged(0);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<Button> reference;

        AdSwitchTask(Button convenientBanner) {
            this.reference = new WeakReference<Button>(convenientBanner);
        }

        @Override
        public void run() {
            Button convenientBanner = reference.get();
            if (convenientBanner != null) {
                int count = Integer.valueOf(convenientBanner.getText().toString());
                convenientBanner.setText(String.valueOf(++count));
                LogUtil.e("count="+count);
                convenientBanner.postDelayed(this, 5000);
            }
        }
    }

    final static class MyDiffUtil extends DiffUtil.Callback{
        private List<TestDiffBean> oldList,newList;
        public MyDiffUtil(List<TestDiffBean> oldList,List<TestDiffBean> newList){
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            // 返回旧数据的长度
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            // 返回新数据的长度
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // 返回两个item是否相同
            // 例如：此处两个item的数据实体是User类，所以以id作为两个item是否相同的依据
            // 即此处返回两个user的id是否相同
            return oldList.get(oldItemPosition).getId()==newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getContent(),newList.get(newItemPosition).getContent());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestDiffAndHandlerActivity.class);
        context.startActivity(starter);
    }
}