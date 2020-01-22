package com.example.demo.recyclerview.manager;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends RecyclerView.LayoutManager {
    private int mFirstVisiPos;//屏幕可见的第一个View的Position
    private int mLastVisiPos;//屏幕可见的最后一个View的Position
    private int verticalScrollOffset;
    private int offsetH = 0;//布局锚点
    private int leftMargin, rightMargin;
    private int smallWidth = 0;//1/3的宽

    private SparseArray<Rect> mItemRects = new SparseArray<>();//key 是View的position，保存View的bounds 和 显示标志，

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return super.isAutoMeasureEnabled();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            mItemRects.clear();
            verticalScrollOffset=0;
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                recycler.getViewForPosition(0).getLayoutParams();
        leftMargin = params.leftMargin;
        rightMargin = params.rightMargin;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount() - 1;
        offsetH = getPaddingTop();
        if(getChildCount()==0){
            mItemRects.clear();
        }
        if(verticalScrollOffset>0){
            addMore=true;//上滑加载更多数据以后，
            offsetH=getDecoratedTop(getChildAt(0));
            mFirstVisiPos=getPosition(getChildAt(0));
        }
        //先把所有item从RecyclerView中detach
        detachAndScrapAttachedViews(recycler);

        layoutItem(recycler);
    }
    private boolean addMore=false;
    private void layoutItem(RecyclerView.Recycler recycler) {
        layoutItem(recycler, 0);
    }

    private void layoutItem(RecyclerView.Recycler recycler, int dy) {

        if (smallWidth == 0) {
            smallWidth = (Resources.getSystem().getDisplayMetrics().widthPixels
                    - getRightDecorationWidth(recycler.getViewForPosition(0))
                    - getLeftDecorationWidth(recycler.getViewForPosition(0))
                    - leftMargin - rightMargin) / 3;
        }
        if (dy >= 0) {
            //往上滑动，底部可能需要添加新的item
            int minPosition = 0;
            mLastVisiPos = getItemCount() - 1;
            if(getChildCount()>0){
                View lastChild = getChildAt(getChildCount() - 1);
                int lastPosition = getPosition(lastChild) ;
                minPosition=lastPosition+1;
                if(lastPosition!=getItemCount()-1&&!addMore){
                    minPosition = lastPosition + 1;
                    offsetH = getDecoratedBottom(lastChild);//测试发现快速滑动可能出现问题，因为最后一个显示的child的index=6，下边要添加7和8的时候，getoffsetH就不对了，所以得判断下
                    if(minPosition%3!=0){
                        offsetH=getDecoratedTop(lastChild);
                        if(minPosition%6==5){
                            offsetH=getDecoratedTop(lastChild)-lastChild.getHeight();
                        }
                    }
                }

                //移除顶部即将不可见的item,要移除一次就是3个
                View child;
                if(mLastVisiPos!=lastPosition){
                    while (getChildCount() > 2 && getDecoratedBottom(child = getChildAt(2)) < getPaddingTop()) {
                        System.out.println("remove child=====3个==" + getPosition(child)+"=="+getDecoratedBottom(child)+"/"+child.getHeight());
                        removeAndRecycleView(child, recycler);
                        removeAndRecycleView(getChildAt(1), recycler);
                        removeAndRecycleView(getChildAt(0), recycler);
                    }
                }
            }

            if(addMore){
                addMore=false;
                minPosition=mFirstVisiPos;
            }
            System.out.println("dy=== " + dy + " ========" + minPosition + "/" + mLastVisiPos + "=========" + offsetH+ "/" + getHeight());
            for (int i = minPosition; i <= mLastVisiPos; i++) {

                if (offsetH - dy > getHeight() - getPaddingBottom()) {
                    mLastVisiPos = i;
                    System.out.println("dy>0 break========" + mLastVisiPos + "====" + offsetH + "/" + dy + "/" + getHeight());
                    break;
                }
                addViewBottom(recycler, i);
            }

        } else {
            //顶部可能需要添加新的item
            int maxPosition = getItemCount() - 1;
            mFirstVisiPos = 0;
            if (getChildCount() > 0) {
                View firstChild = getChildAt(0);
                maxPosition = getPosition(firstChild) - 1;

                //移除底部不可见的,
                View child;
                while (true) {
                    if(getChildCount()==0 || maxPosition<0){
                        break;
                    }
                    int topPre= getDecoratedTop(child = getChildAt(getChildCount() - 1));
                    int removeTop=getHeight() - getPaddingBottom()+child.getHeight();
                    System.out.println("remove child=======" + getPosition(child)+"==="+topPre+"==="+getHeight()+"=="+child.getHeight());
                    if(topPre>removeTop){
                        removeAndRecycleView(child, recycler);
                    }else{
                        break;
                    }
                }
            }
            for (int i = maxPosition; i >= mFirstVisiPos; i = i - 3) {//如果顶部要添加，那么一次至少3个
                Rect rect = mItemRects.get(i);
                if (rect.bottom - verticalScrollOffset - dy < getPaddingTop()) {
                    mFirstVisiPos = i + 1;
                    return;
                }
                addViewTop(recycler, i);
                addViewTop(recycler, i - 1);
                addViewTop(recycler, i - 2);
            }

        }

    }

    private void addViewTop(RecyclerView.Recycler recycler, int i) {
        if (i < 0) {
            return;
        }
        View child = recycler.getViewForPosition(i);
        addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
        measureChildWithMargins(child, 0, 0);
        Rect rect = mItemRects.get(i);
        layoutDecoratedWithMargins(child, rect.left, rect.top - verticalScrollOffset, rect.right, rect.bottom - verticalScrollOffset);
    }

    private void addViewBottom(RecyclerView.Recycler recycler, int i) {
        if (i > getItemCount() - 1) {
            return;
        }
        View view = recycler.getViewForPosition(i);
        addView(view);
        measureChildWithMargins(view, 0, 0);
        int height = getDecoratedMeasuredHeight(view);
        System.out.println("addViewBottom=======" + i + "/verticalScrollOffset=" + verticalScrollOffset + "======" + offsetH + "====" + height + "====" + getHeight());
        Rect rect = new Rect();
        switch (i % 6) {
            case 0:
                rect.set(0, offsetH, 2 * smallWidth, offsetH + height);
                layoutDecoratedWithMargins(view, 0, offsetH, 2 * smallWidth, offsetH + height);
                break;
            case 1:
                rect.set(2 * smallWidth, offsetH,
                        3 * smallWidth, offsetH + height / 2);
                layoutDecoratedWithMargins(view, 2 * smallWidth, offsetH,
                        3 * smallWidth, offsetH + height / 2);
                break;
            case 2:
                rect.set(2 * smallWidth, offsetH + height / 2,
                        3 * smallWidth, offsetH + height);
                layoutDecoratedWithMargins(view, 2 * smallWidth, offsetH + height / 2,
                        3 * smallWidth, offsetH + height);
                this.offsetH = this.offsetH + height;
                break;
            case 3:
                rect.set(0, offsetH,
                        smallWidth, offsetH + height / 2);
                layoutDecoratedWithMargins(view, 0, offsetH,
                        smallWidth, offsetH + height / 2);
                break;
            case 4:
                rect.set(0, offsetH + height / 2,
                        smallWidth, offsetH + height);
                layoutDecoratedWithMargins(view, 0, offsetH + height / 2,
                        smallWidth, offsetH + height);
                break;
            case 5:
                rect.set(smallWidth, offsetH,
                        3 * smallWidth, offsetH + height);
                layoutDecoratedWithMargins(view, smallWidth, offsetH,
                        3 * smallWidth, offsetH + height);
                this.offsetH = this.offsetH + height;
                break;
        }
        rect.offset(0, verticalScrollOffset);
        mItemRects.put(i, rect);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        //手指往上滑dy为正，手指往下滑dy为负
        layoutItem(recycler, dy);//先添加布局，再位移

        if (dy<0) {
            View firstChild = getChildAt(0);
            if (getPosition(firstChild) == 0 && getDecoratedTop(firstChild) - dy > 0) {
               //第一个child的position是0，并且top即将大于0，那就不能移动
                dy=getDecoratedTop(firstChild);
            }
        }else { //判断最后一个child的底部是否即将跑到屏幕底部的上边
            int bottom=getDecoratedBottom(getChildAt(getChildCount()-1));
            if(bottom- dy < getHeight() - getPaddingBottom()){
                dy=bottom-getHeight()+getPaddingBottom();
            }
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += dy;
        // 调用该方法通知view在y方向上移动指定距离
        offsetChildrenVertical(-dy);
        System.out.println("scrollvertical===========" + dy + "=====" + verticalScrollOffset+"==offsetH===="+offsetH);
        return dy;
    }
}
