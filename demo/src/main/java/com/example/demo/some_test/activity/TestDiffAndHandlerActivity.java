package com.example.demo.some_test.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.R2;
import com.example.demo.some_test.adapter.TestDiffAdapter;
import com.example.demo.some_test.presenter.TestDiffAndHandlerPresenter;
import com.example.demo.some_test.view.ITestDiffAndHandlerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.TestDiffBean;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestDiffAndHandlerActivity extends BaseActivity implements ITestDiffAndHandlerView {
    @BindView(R2.id.btn_change)
    Button btnChange;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_context)
    TextView tvContext;
    @BindView(R2.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R2.id.tv_anim)
    TextView tvAnim;
    @BindView(R2.id.iv_anim)
    SimpleDraweeView ivAnim;
    private TestDiffAndHandlerPresenter mPresenter;
    private TestDiffAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_diff_and_handler;
    }

    AdSwitchTask adSwitchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(new ChangeBounds());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void bindData() {
        List<TestDiffBean> list = new ArrayList<>();
        for (int i = 0, count = 5; i < count; i++) {
            list.add(new TestDiffBean(i, "StringContent" + i));
        }
        mAdapter = new TestDiffAdapter();
        mAdapter.submitList(list);
        mAdapter.setDatas(list);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
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

        testGhostView();
        ivAnim.setImageURI("http://imgsrc.baidu.com/imgad/pic/item/d000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg");
    }

    private void testGhostView() {
        ColorDrawable colorDrawable = new ColorDrawable(Color.RED);
        colorDrawable.setAlpha(100);
        colorDrawable.setBounds(0, 0, 200, 200);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            tvContext.getOverlay().add(colorDrawable);
        }
        frameLayout.setRight(frameLayout.getLeft() + UIHelper.dip2px(800));
        frameLayout.setBottom(frameLayout.getTop() + UIHelper.dip2px(200));
        addGhost(tvContext, frameLayout);
    }

    private void addGhost(View view, ViewGroup viewGroup) {
        try {
            Class ghostViewClass = Class.forName("android.view.GhostView");
            Method addGhostMethod = ghostViewClass.getMethod("addGhost", View.class,
                    ViewGroup.class, Matrix.class);
            View ghostView = (View) addGhostMethod.invoke(null, view, viewGroup, null);
            ghostView.setBackgroundColor(Color.YELLOW);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initEvent() {
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                if(!ListUtils.isEmpty(sharedElements)){
                    for(View view:sharedElements){
                        view.setVisibility(View.VISIBLE);//解决fresco共享元素动画bug,返回空白
                    }
                }
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }
        });
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
//        mAdapter.notifyItemChanged(0);
        //tvContext.setText("Change To another");
        BrowseAnimActivity.start(this, ivAnim);
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
                LogUtil.e("count=" + count);
                convenientBanner.postDelayed(this, 5000);
            }
        }
    }

    final static class MyDiffUtil extends DiffUtil.Callback {
        private List<TestDiffBean> oldList, newList;

        public MyDiffUtil(List<TestDiffBean> oldList, List<TestDiffBean> newList) {
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
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getContent(), newList.get(newItemPosition).getContent());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TestDiffAndHandlerActivity.class);
        context.startActivity(starter);
    }
}