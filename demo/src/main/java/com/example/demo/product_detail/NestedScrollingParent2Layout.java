package com.example.demo.product_detail;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.example.demo.anim.activity.ViewAnimActivity;

/**
* Description:NestedScrolling2机制下的嵌套滑动，实现NestedScrollingParent2接口下，处理fling效果的区别
*/

public class NestedScrollingParent2Layout extends LinearLayout implements NestedScrollingParent2 {

   private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

   public NestedScrollingParent2Layout(Context context) {
       this(context, null);
   }

   public NestedScrollingParent2Layout(Context context, @Nullable AttributeSet attrs) {
       this(context, attrs, 0);
   }

   public NestedScrollingParent2Layout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);
       setOrientation(VERTICAL);
   }


   /**
    * 即将开始嵌套滑动，此时嵌套滑动尚未开始，由子控件的 startNestedScroll 方法调用
    *
    * @param child  嵌套滑动对应的父类的子类(因为嵌套滑动对于的父控件不一定是一级就能找到的，可能挑了两级父控件的父控件，child的辈分>=target)
    * @param target 具体嵌套滑动的那个子类
    * @param axes   嵌套滑动支持的滚动方向
    * @param type   嵌套滑动的类型，有两种ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
    * @return true 表示此父类开始接受嵌套滑动，只有true时候，才会执行下面的 onNestedScrollAccepted 等操作
    */
   @Override
   public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
       boolean scroll = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && isViewBoundary(target);
       Log.e("zhang",String.format("canScroll=%b;isViewBoundary=%s;target.canScrollVertically(1)=%b;target.canScrollVertically(-1)=%b",
               scroll,isViewBoundary(target),target.canScrollVertically(1),target.canScrollVertically(-1)));
       return true;
   }

    /**
     * view是否滚动到了边界
     * @param view
     * @return
     */
   boolean isViewBoundary(View view){
       return  !view.canScrollVertically(1) || !view.canScrollVertically(-1);
   }

   boolean isMiddle() {
       int value = getScrollY()%getMeasuredHeight();
       if(value>1&&value<getMeasuredHeight()-1){
           return true;
       }
       return false;
   }
   /**
    * 当onStartNestedScroll返回为true时，也就是父控件接受嵌套滑动时，该方法才会调用
    *
    * @param child
    * @param target
    * @param axes
    * @param type
    */
   @Override
   public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
       mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
   }

   /**
    * 在子控件开始滑动之前，会先调用父控件的此方法，由父控件先消耗一部分滑动距离，并且将消耗的距离存在consumed中，传递给子控件
    * 在嵌套滑动的子View未滑动之前
    * ，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
    *
    * @param target   具体嵌套滑动的那个子类
    * @param dx       水平方向嵌套滑动的子View想要变化的距离
    * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
    * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
    *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
    * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
    */
   @Override
   public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
       //这里不管手势滚动还是fling都处理  canScrollVertically表示的是框的上下移动
       boolean scrollUp = dy > 0 && !target.canScrollVertically(1) && canScrollVertically(1) && type!=ViewCompat.TYPE_NON_TOUCH;
       boolean scrollDown = dy < 0 && !target.canScrollVertically(-1)&& canScrollVertically(-1) && type!=ViewCompat.TYPE_NON_TOUCH;
       boolean cunsumed = scrollUp || scrollDown || isMiddle();

       //Log.e("zhang",String.format("dy=%s;scrollup=%b;cunsumed=%b;isMiddle=%b",dy,scrollUp,cunsumed,isMiddle()));
       if (cunsumed) {
           scrollBy(0, dy);
           consumed[1] = dy;
       }
   }

    @Override
    protected int computeVerticalScrollRange() {
        return getChildCount()*getMeasuredHeight();
    }

    /**
    * 在 onNestedPreScroll 中，父控件消耗一部分距离之后，剩余的再次给子控件，
    * 子控件消耗之后，如果还有剩余，则把剩余的再次还给父控件
    *
    * @param target       具体嵌套滑动的那个子类
    * @param dxConsumed   水平方向嵌套滑动的子控件滑动的距离(消耗的距离)
    * @param dyConsumed   垂直方向嵌套滑动的子控件滑动的距离(消耗的距离)
    * @param dxUnconsumed 水平方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
    * @param dyUnconsumed 垂直方向嵌套滑动的子控件未滑动的距离(未消耗的距离)
    */
   @Override
   public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
       //这里不管手势滚动还是fling都处理
       boolean scrollUp = dyUnconsumed > 0 && !target.canScrollVertically(1) && canScrollVertically(1) && type!=ViewCompat.TYPE_NON_TOUCH;
       boolean scrollDown = dyUnconsumed < 0 && !target.canScrollVertically(-1)&& canScrollVertically(-1) && type!=ViewCompat.TYPE_NON_TOUCH;
       boolean cunsumed = scrollUp || scrollDown;

       if (cunsumed) {
           scrollBy(0, dyUnconsumed);
       }
   }


   /**
    * 停止滑动
    *
    * @param target
    * @param type
    */
   @Override
   public void onStopNestedScroll(@NonNull View target, int type) {
       if (type == ViewCompat.TYPE_NON_TOUCH) {
           System.out.println("onStopNestedScroll");
       }

       mNestedScrollingParentHelper.onStopNestedScroll(target, type);
   }


   @Override
   public int getNestedScrollAxes() {
       return mNestedScrollingParentHelper.getNestedScrollAxes();
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       //ViewPager修改后的高度= 总高度-导航栏高度
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//       ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
//       layoutParams.height = getMeasuredHeight();
//       mContentView.setLayoutParams(layoutParams);
//       super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
       int count = getChildCount();
       for(int i=0;i<count;i++){
           getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
       }
   }

   @Override
   protected void onFinishInflate() {
       super.onFinishInflate();
   }

   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       super.onSizeChanged(w, h, oldw, oldh);
   }

    @Override
    public void scrollBy(int x, int dy) {
        int value = (getScrollY()+dy)%getMeasuredHeight();
        if(value<dy){
            dy = dy - value;
        }else if(value>(getMeasuredHeight()-Math.abs(dy))){
            dy = dy + (getMeasuredHeight()-value);
        }
        super.scrollBy(x, dy);
    }

    @Override
   public void scrollTo(int x, int y) {
//       Log.e("zhang","scrollTo:" + y);
       y = clamp(y,getHeight(),computeVerticalScrollRange());
       super.scrollTo(x, y);
       if(mListener!=null){
           mListener.onScroll(getScrollY(),getScrollY()/(getMeasuredHeight()-1));
       }
   }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my+n) > child) {
            return child-my;
        }
        return n;
    }

    public interface ScrollChangeListener{
       void onScroll(int scrollY,int index);
    }

    private ScrollChangeListener mListener;

    public void setScrollChangeListener(ScrollChangeListener listener){
        mListener = listener;
    }
}
