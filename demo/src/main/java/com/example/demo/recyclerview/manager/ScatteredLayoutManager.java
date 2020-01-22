package com.example.demo.recyclerview.manager;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.framework.common.utils.LogUtil;

/**
 * <pre>
 *     author : Harvey
 *     time   : 2018/08/09
 *     desc   : 一次性布局完成，没有滚出屏幕回收复用
 * </pre>
 */
public class ScatteredLayoutManager extends RecyclerView.LayoutManager {

    private int verticalScrollOffset;
    private int offsetH = 0;
    private int leftMargin, rightMargin;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                recycler.getViewForPosition(0).getLayoutParams();
        leftMargin = params.leftMargin;
        rightMargin = params.rightMargin;
        //先把所有item从RecyclerView中detach
        detachAndScrapAttachedViews(recycler);
        verticalScrollOffset = 0;
        layoutItem(recycler);
    }

    private void layoutItem(RecyclerView.Recycler recycler) {
        offsetH = getPaddingTop();

        int width = (Resources.getSystem().getDisplayMetrics().widthPixels
                - getRightDecorationWidth(recycler.getViewForPosition(0))
                - getLeftDecorationWidth(recycler.getViewForPosition(0))
                - leftMargin - rightMargin) / 3;
        int round = 0;
        boolean last = false;
        for (int i = 0; i < getItemCount(); i++) {
            if(i==getItemCount()-1){
                last = true;
            }
            View view = recycler.getViewForPosition(i);
            addView(view); // 因为detach过所以重新添加
            measureChildWithMargins(view, 0, 0);
            int height = getDecoratedMeasuredHeight(view);

            Rect mTmpRect = new Rect();
            calculateItemDecorationsForChild(view, mTmpRect);
            int type = i - round;
            switch (type) {
                case 0:
                    layoutDecoratedWithMargins(view, 0, offsetH, 2 * width, offsetH + height);
					break;
                case 1:
                    layoutDecoratedWithMargins(view, 2 * width, offsetH,
                            3 * width, offsetH + height / 2);
					break;
                case 2:
                    layoutDecoratedWithMargins(view, 2 * width, offsetH + height / 2,
                            3 * width, offsetH + height);
                    offsetH = offsetH + height;
					break;
                case 3:
                    layoutDecoratedWithMargins(view, 0, offsetH,
                            width, offsetH + height / 2);
					break;
                case 4:
                    layoutDecoratedWithMargins(view, 0, offsetH + height / 2,
                            width, offsetH + height);
					break;
                case 5:
                    layoutDecoratedWithMargins(view, width, offsetH,
                            3 * width, offsetH + height);
                    offsetH = offsetH + height;
					break;
                default:
                    layoutDecoratedWithMargins(view, 0, offsetH, 3 * width,
                            offsetH + height);
                    offsetH = offsetH + height;
					break;
            }
            if(last && type!=5 &&type!=2){
                if(type==1 || type==4){
                    offsetH = offsetH + height;
                }else {
                    offsetH = offsetH + height/2;
                }
            }
            if (i + 1 - round == 6)
                round += 6;
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
        //实际要滑动的距离
        int travel = dy;
        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > offsetH - getVerticalSpace()) {//如果滑动到最底部
            travel = offsetH - getVerticalSpace() - verticalScrollOffset;
        }
        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;
        // 调用该方法通知view在y方向上移动指定距离
        offsetChildrenVertical(-travel);

        return travel;
    }

    private int getVerticalSpace() {
        //计算RecyclerView的可用高度，除去上下Padding值
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}