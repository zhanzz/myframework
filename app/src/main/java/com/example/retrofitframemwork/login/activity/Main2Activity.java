package com.example.retrofitframemwork.login.activity;

import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.retrofitframemwork.R;
import com.example.retrofitframemwork.login.adapter.TestAdapter;
import com.example.retrofitframemwork.login.presenter.TestPresenter;
import com.example.retrofitframemwork.login.view.ITestView;
import com.example.retrofitframemwork.utils.Events;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.framework.common.adapter.BaseAdapter;
import com.framework.common.adapter.HeaderAndFooterWrapper;
import com.framework.common.base_mvp.BaseActivity;
import com.framework.common.base_mvp.BasePresenter;
import com.framework.common.manager.EventBusUtils;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.LogUtil;
import com.framework.common.utils.UIHelper;
import com.framework.common.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends BaseActivity implements ITestView {
    @BindView(R.id.constraintlayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TestAdapter mAdapter;
    @BindView(R.id.llSearch)
    TextView llSearch;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.test_tv)
    TextView testTv;
    @BindView(R.id.test_et)
    EditText testEt;
    private TestPresenter mPresenter;
    HeaderAndFooterWrapper mHeaderAndFooterAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public boolean isFitSystemBar() {
        return true;
    }

    @Override
    public void bindData() {
        LogUtil.e("bindData");
        //refreshLayout.setFitsSystemWindows(true);
        //mAdapter.setNewData(data);
        refreshLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                refreshLayout.getRefreshHeader().getView().setPadding(0, UIHelper.getStatusHeight(), 0, 0);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        //refreshLayout.setHeaderInsetStart(UIHelper.getStatusHeight());
        refreshLayout.setVisibility(View.INVISIBLE);
        mAdapter = new TestAdapter(recyclerView);
        mHeaderAndFooterAdapter = new HeaderAndFooterWrapper(mAdapter);
        mAdapter.setSmartRefreshLayout(refreshLayout);
        mAdapter.setRealControlMoreEnable(false);
        mHeaderAndFooterAdapter.setLoadEndText("暂无更多数据");
        mHeaderAndFooterAdapter.setEmptyView(ViewUtil.createEmptyView(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("string" + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mPresenter.refreshData(true);

        llSearch.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.e("onNewIntent");//startActivities会调用onCreate和onNewIntent
    }

    @Override
    public void initEvent() {
        mAdapter.setEnableLoadMore(false);
        mAdapter.setRefreshListener(new BaseAdapter.RefreshListener() {
            @Override
            public void onRefresh() {
                EventBusUtils.post(new Events.LoginOut());
                mPresenter.refreshData(false);
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData();
            }
        });
    }

    @Override
    public void reloadData() {
        mPresenter.refreshData(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new TestPresenter();
        }
        return mPresenter;
    }

    public static void starts(Context context) {
        Intent[] intents = new Intent[2];
        Intent starter = new Intent(context, Main2Activity.class);
        intents[0] = starter;
        Intent starter2 = new Intent(context, Main3Activity.class);
        intents[1] = starter2;
        context.startActivities(intents); //结论：singleTask,Main2Activity会被记录在栈底，虽然此时还没有启动，但就好像已经在栈底了一样。会清除它之后启动的页面
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Main2Activity.class);
        context.startActivity(starter);
    }

    @Override
    public void onPageData(List<String> data, int currentPage) {
        refreshLayout.setVisibility(View.VISIBLE);
        mAdapter.setOrAddData(data, currentPage, mPresenter.PAGE_SIZE);
        //mHeaderAndFooterAdapter.loadComplete(data,mPresenter.PAGE_SIZE);
    }

    @Override
    public void onLoadFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void reduce() {
        //设置收缩状态时的布局
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) llSearch.getLayoutParams();
        layoutParams.width = UIHelper.dip2px(200);
        layoutParams.setMargins(UIHelper.dip2px(80), UIHelper.dip2px(10),
                UIHelper.dip2px(10), UIHelper.dip2px(10));
        llSearch.setLayoutParams(layoutParams);
        //开始动画
        beginDelayedTransition(constraintLayout);
    }

    /**
     * 开始动画
     */
    private void beginDelayedTransition(ViewGroup view) {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(300);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }

    @OnClick(R.id.llSearch)
    public void onClick() {
        //reduce();
        //loadBitMap();
        //LogUtil.e(testEt.isFocusable() +";"+ testEt.isFocusableInTouchMode() + "===="+testTv.isFocusable()+";"+testTv.isFocusableInTouchMode());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ValueAnimator valueAnimator = ValueAnimator.ofObject(new MyTypeEvaluator(), new Point(0, 0), new Point(0, 0));
//                //设置持续时间
//                valueAnimator.setDuration(2000);
//                //设置加速时间插值器
//                valueAnimator.setInterpolator(new MyInperpolator());
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {    //设置监听器
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        //将最新计算出的属性值设置给ImageView
//                        Point point = (Point) animation.getAnimatedValue();
//                        //ivBall.setX(point.x);
//                        //ivBall.setY(point.y);
//
//                        LogUtil.e(Thread.currentThread().getName());
//                    }
//                });
//                Looper.prepare();
//                //开启动画
//                valueAnimator.start();
//                llSearch.setX(0);
//                llSearch.setY(200);
//            }
//        }).start();
        //Main3Activity.start(this);
        Intent intent = new Intent();
        intent.setAction("com.frameWork.test");
        //intent.setClass(this, MainActivity.MyBroadCastReciver.class);//设置了显示，动态注册无法接收
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * 41      * 自定义时间插值器，这里实现了线性时间插值器
     * 42
     */
    class MyInperpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float input) {
            return input;
        }
    }

    /**
     * 53      * 实现的自己的TypeEvaluator
     * 54
     */
    class MyTypeEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            Point point = new Point();
            point.x = startValue.x + fraction * 500;
            point.y = startValue.y + fraction * 500;

            return point;
        }
    }


    /**
     * 69      * 保存坐标信息
     * 70
     */
    class Point {
        float x;
        float y;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private void loadBitMap() {
        FrescoUtils.showThumb("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557985668377&di=de105d36589518d06bb621f76b851988&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201409%2F21%2F20140921190342_jdhBY.thumb.700_0.jpeg"
                , simpleDraweeView, new BasePostprocessor() {
                    @Override
                    public void process(Bitmap bitmap) {
                        Palette p = Palette.from(bitmap)
                                .setRegion(0, 0, bitmap.getWidth(), bitmap.getHeight() / 20)
                                .generate();
                        Palette.Swatch color = p.getDominantSwatch();
                        LogUtil.e("color=" + color);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (color == null) {
                                    llSearch.setBackgroundColor(Color.RED);
                                } else {
                                    llSearch.setBackgroundColor(color.getRgb());
                                }
                            }
                        });
                    }
                });
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
