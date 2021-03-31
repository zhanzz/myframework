package com.example.demo.recyclerview.manager;

import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.framework.common.utils.UIHelper;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;

//RecyclerView.SmoothScroller.ScrollVectorProvider
public class LooperLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider{
    private static final String TAG = "LooperLayoutManager";
    private boolean looperEnable;
    private int preDistance = UIHelper.dip2px(50);
    private LinearSmoothScroller smoothScroller;
    private List<OnPageChangeListener> onPageChangeListeners = new ArrayList<>();
    private int START = -1;
    private int END = 1;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        //标注1.如果当前时准备状态，直接返回
        if (state.isPreLayout()) {
            return;
        }
        //标注2.将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int autualWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //标注3.初始化，将在屏幕内的view填充
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //标注4.测量itemView的宽高
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidthWithMargin(itemView);
            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int margin = lp.leftMargin + lp.rightMargin;
            int height = getDecoratedMeasuredHeight(itemView);
            //标注5.根据itemView的宽高进行布局
            layoutDecoratedWithMargins(itemView, autualWidth, 0, autualWidth + width, height);

            autualWidth += width;
            //标注6.如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
            if (autualWidth > getWidth()) {
                break;
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //标注1.横向滑动的时候，对左右两边按顺序填充itemView
        int travl = fill(dx, recycler, state);
        if (travl == 0) {
            return 0;
        }

        //2.滑动
        offsetChildrenHorizontal(-travl);

        //3.回收已经不可见的itemView
        recyclerHideView(dx, recycler, state);
        return travl;
    }

    /**
     * 左右滑动的时候，填充
     */
    private int fill(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx > 0) {
            //标注1.向左滚动
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return 0;
            }
            int lastPos = getPosition(lastView);
            //标注2.可见的最后一个itemView完全滑进来了，需要补充新的
            if (lastView.getRight() <= getWidth()+preDistance) {
                View scrap = null;
                //标注3.判断可见的最后一个itemView的索引，
                // 如果是最后一个，则将下一个itemView设置为第一个，否则设置为当前索引的下一个
                if (lastPos == getItemCount() - 1) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(0);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(lastPos + 1);
                }
                if (scrap == null) {
                    return dx;
                }
                //标注4.将新的itemViewadd进来并对其测量和布局
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                Rect outBounds = new Rect();
                getDecoratedBoundsWithMargins(lastView,outBounds);
                int height = getDecoratedMeasuredHeight(scrap);
                int width = getDecoratedMeasuredWidthWithMargin(scrap);
                layoutDecoratedWithMargins(scrap,outBounds.right, 0,
                        outBounds.right+width, height);
                return dx;
            }else if(lastPos == getItemCount() - 1){
                if(!looperEnable){
                    if (lastView.getRight()- dx < getWidth()) {
                        return lastView.getRight()-getWidth();
                    }
                }
            }
        } else {
            //向右滚动
            View firstView = getChildAt(0);
            if (firstView == null) {
                return 0;
            }
            int firstPos = getPosition(firstView);

            if (firstView.getLeft() >= -preDistance) {
                View scrap = null;
                if (firstPos == 0) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(getItemCount() - 1);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(firstPos - 1);
                }
                if (scrap == null) {
                    return 0;
                }
                addView(scrap, 0);
                measureChildWithMargins(scrap,0,0);
                Rect outBounds = new Rect();
                getDecoratedBoundsWithMargins(firstView,outBounds);
                int height = getDecoratedMeasuredHeight(scrap);
                int width = getDecoratedMeasuredWidthWithMargin(scrap);
                final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) firstView.getLayoutParams();
                layoutDecoratedWithMargins(scrap, outBounds.left-width, 0,
                        outBounds.left, height);
            }else if(firstPos == 0){
                if(!looperEnable){
                    if (firstView.getLeft() - dx > 0) {
                        return firstView.getLeft();
                    }
                }
            }
        }
        return dx;
    }

    /**
     * 回收界面不可见的view
     */
    private void recyclerHideView(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dx > 0) {
                //标注1.向左滚动，移除左边不在内容里的view
                if (view.getRight() < 0) {
                    removeAndRecycleView(view, recycler);
                    Log.d(TAG, "循环: 移除 一个view childCount=" + getChildCount());
                }
            } else {
                //标注2.向右滚动，移除右边不在内容里的view
                if (view.getLeft() > getWidth()) {
                    removeAndRecycleView(view, recycler);
                    Log.d(TAG, "循环: 移除 一个view childCount=" + getChildCount());
                }
            }
        }

    }

    public boolean isLooperEnable() {
        return looperEnable;
    }

    public void setLooperEnable(boolean looperEnable) {
        this.looperEnable = looperEnable;
    }

    public int getDecoratedMeasuredWidthWithMargin(@NonNull View child) {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredWidth(child)+ lp.leftMargin + lp.rightMargin;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext());
        scroller.setTargetPosition(normalizedPos(position));
        startSmoothScroll(scroller);
        smoothScroller = scroller;
        int curItem = scroller.getTargetPosition();
        dispatchOnPageSelected(curItem);
    }

    private int normalizedPos(int position){
        int itemCount = getItemCount();
        if (canLoop()) {
            return  position % itemCount;
        } else {
            if (position >= itemCount)
                return itemCount - 1;
            else return  position;
        }
    }

    private boolean canLoop() {
        return getItemCount()>1 && looperEnable;
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        int itemCount = getItemCount();
        if (getChildCount() == 0 || itemCount == 0) {
            return null;
        }

        LinearSmoothScroller scroller = smoothScroller;
        int firstChildPos = getPosition(getChildAt(0));
        int direction;
        if (canLoop() && scroller != null && (scroller.isRunning() || scroller.isPendingInitialRun())) {
            int distancePos = targetPosition - firstChildPos;
            if (targetPosition < firstChildPos) {
                // start
                if (itemCount > 2 * abs(distancePos))
                    direction = START;
                else
                    direction=END;
            } else {
                // end
                if (itemCount > 2 * abs(distancePos))
                    direction = END;
                else
                    direction = START;
            }
            return new PointF(direction, 0f);
        }
        if (targetPosition < firstChildPos)
            direction = START;
        else
            direction = END;
        return new PointF(direction, 0f);
    }

    public void addPageChangeListener(OnPageChangeListener listener) {
        if (!onPageChangeListeners.contains(listener)) {
            onPageChangeListeners.add(listener);
        }
    }

    public void removePageChangeListener(OnPageChangeListener listener) {
        onPageChangeListeners.remove(listener);
    }

    private void dispatchOnPageSelected(int pos) {
        for (OnPageChangeListener list :onPageChangeListeners) {
            list.onPageSelected(pos);
        }
    }

    private void dispatchOnPageScrollStateChanged(int state) {
        for (OnPageChangeListener list :onPageChangeListeners) {
            list.onPageScrollState(state);
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int pos);
        void onPageScrollState(int state);
    }
}