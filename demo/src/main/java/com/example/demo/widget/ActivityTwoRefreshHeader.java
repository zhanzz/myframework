package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.demo.R;
import com.framework.common.utils.UIHelper;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ArrowDrawable;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityTwoRefreshHeader extends ConstraintLayout implements RefreshHeader {
    private ProgressDrawable mProgressDrawable;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    /**
     * 箭头图片
     */
    private ImageView mArrowImageView;
    private ImageView mProgressView;
    /**
     * 状态提示TextView
     */
    private TextView mHintTextView;
    /**
     * 最后更新时间的TextView
     */
    private TextView mHeaderTimeView;

    private long mLastCurrentTimeMillis;

    public ActivityTwoRefreshHeader(Context context) {
        this(context, null, 0);
    }

    public ActivityTwoRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityTwoRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgressDrawable = new ProgressDrawable();
        inflate(context, R.layout.layout_activity_refresh_header, this);

        mArrowImageView = findViewById(R.id.pull_to_refresh_header_arrow);
        mArrowImageView.setImageDrawable(new ArrowDrawable());
        mHintTextView = findViewById(R.id.tv_head_tip);
        mHeaderTimeView = findViewById(R.id.pull_to_refresh_header_time);
        mProgressView = findViewById(R.id.pull_to_refresh_header_progress);
        mProgressView.setImageDrawable(mProgressDrawable);

        setMinimumHeight(UIHelper.dip2px(60));
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }


    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh: // 下拉刷新开始。正在下拉还没松手时调用
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.INVISIBLE);
                mHintTextView.setText("下拉可以刷新");
                mHeaderTimeView.setText("最后更新时间:" + dateFormat.format(new Date(mLastCurrentTimeMillis)));
                mArrowImageView.animate().rotation(0);
                break;
            case Refreshing: // 正在刷新。只调用一次
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressView.setVisibility(View.VISIBLE);
                mProgressDrawable.start();
                mHintTextView.setText("努力加载中...");
                break;
            case ReleaseToTwoLevel:
                mHintTextView.setText("松开进入活动页");
                break;
            case ReleaseToRefresh: // 下拉达到下拉布局高度回调
                mHintTextView.setText("松开立即刷新");
                mArrowImageView.animate().rotation(180);
                break;
            case RefreshFinish: // 刷新结束
                mLastCurrentTimeMillis = System.currentTimeMillis();
                break;
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mProgressDrawable.stop();//停止动画
        mProgressView.setVisibility(View.INVISIBLE);//隐藏动画

        if (success) {
            mHintTextView.setText("刷新完成了");
        } else {
            mHintTextView.setText("刷新失败了");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        mLastCurrentTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}