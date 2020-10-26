package com.example.demo.coordinator_layout.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.example.demo.R;
import com.framework.common.utils.UIHelper;

/***
 * View类中的
 * onStartNestedScroll是在api 21才加入的
 */
public class MoveTopView extends FrameLayout implements NestedScrollingParent2 {
    private ViewDragHelper viewDragHelper;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private View captureView;

    public MoveTopView(@NonNull Context context) {
        super(context);
        init();
    }

    public MoveTopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveTopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveTopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DraggableViewCallback());
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
//        boolean move = target.canScrollVertically(-1);
//        if(!move && captureView!=null){
//            viewDragHelper.captureChildView(captureView,0);
//            return true;
//        }else {
//            return false;
//        }
        return true;
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        captureView = findViewById(R.id.view_need_move);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
        //为了释放captureView
        if (captureView != null && type == ViewCompat.TYPE_TOUCH) {
            MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0);
            viewDragHelper.captureChildView(captureView, 0);
            Log.e("zhang", "startRelease");
            viewDragHelper.processTouchEvent(event);
            event.recycle();
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //这里只管手势滚动不处理fling
        boolean scrollUp = dyUnconsumed < 0 && !target.canScrollVertically(-1) && canScrollVertically(1) && type != ViewCompat.TYPE_NON_TOUCH;

        if ((scrollUp || isMiddle()) && captureView != null) {
            if (viewDragHelper.getCapturedView() == null) {
                viewDragHelper.captureChildView(captureView, 0);
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                viewDragHelper.abort();
                if ((captureView.getTop() - dyUnconsumed) > (getHeight() - UIHelper.dip2px(60))) {
                    ViewCompat.offsetTopAndBottom(captureView, getHeight() - UIHelper.dip2px(60) - captureView.getTop());
                } else {
                    ViewCompat.offsetTopAndBottom(captureView, -dyUnconsumed);
                }
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //这里不管手势滚动还是fling都处理  canScrollVertically表示的是框的上下移动
        boolean scrollUp = dy < 0 && !target.canScrollVertically(-1) && type != ViewCompat.TYPE_NON_TOUCH;

        //Log.e("zhang",String.format("dy=%s;scrollup=%b;cunsumed=%b;isMiddle=%b",dy,scrollUp,cunsumed,isMiddle()));
        if ((scrollUp || isMiddle()) && captureView != null) {
            if (viewDragHelper.getCapturedView() == null) {
                viewDragHelper.captureChildView(captureView, 0);
            }
            //viewDragHelper.settleCapturedViewAt(0,captureView.getTop()+dy);
            if (type == ViewCompat.TYPE_TOUCH) {
                viewDragHelper.abort();
                if ((captureView.getTop() - dy) > (getHeight() - UIHelper.dip2px(60))) {
                    ViewCompat.offsetTopAndBottom(captureView, getHeight() - UIHelper.dip2px(60) - captureView.getTop());
                } else {
                    ViewCompat.offsetTopAndBottom(captureView, -dy);
                }
                if(mListener!=null){
                    mListener.onScroll(captureView.getTop() - (getHeight() - captureView.getHeight()));
                }
            }
            consumed[1] = dy;
        }
    }

    boolean isMiddle() {
        int top = getHeight() - captureView.getHeight();
        if (captureView.getTop() > top) {
            return true;
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    /**
     * Created by 小嵩 on 2017/8/30.
     */

    public class DraggableViewCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (child.getId() == R.id.view_need_move) {
                return true;
            }
            return false;
        }

        /**
         * 子控件水平方向位置改变时触发
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e("zhang", "left=" + left);
            //屏蔽掉水平方向
            return 0;
        }

        /**
         * 子控件竖直方向位置改变时触发
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("zhang", "top=" + top);
            //不能滑出顶部
            int maxTop = getHeight() - child.getHeight();
            return Math.max(top, maxTop);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
//            if(child.getId()== R.id.v_red_box){
//                return getHeight()-child.getHeight();
//            }
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.e("zhang", "xvel=" + xvel + ";yvel=" + yvel);
//            viewDragHelper.flingCapturedView(0,0,0,getHeight()-releasedChild.getHeight());
            int mid = getHeight() - releasedChild.getHeight() / 2 - UIHelper.dip2px(60);
            Log.e("zhang", "onViewReleased");
            if (releasedChild.getTop() > mid) {
                if (viewDragHelper.smoothSlideViewTo(releasedChild,
                        0, getHeight() - UIHelper.dip2px(60))) {
                    ViewCompat.postInvalidateOnAnimation(MoveTopView.this);
                }
            } else {
                if (viewDragHelper.smoothSlideViewTo(releasedChild,
                        0, getHeight() - releasedChild.getHeight())) {
                    ViewCompat.postInvalidateOnAnimation(MoveTopView.this);
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        if (captureView!=null&&viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
            if(mListener!=null){
                mListener.onScroll(captureView.getTop() - (getHeight() - captureView.getHeight()));
            }
        }
    }

    public interface OnScrollListener{
        void onScroll(int scroll);
    }

    private OnScrollListener mListener;

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }
}
