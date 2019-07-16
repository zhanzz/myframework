package com.bigkoo.convenientbanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.adapter.CBPageAdapter;
import com.bigkoo.convenientbanner.holder.ViewAdapter;
import com.bigkoo.convenientbanner.listener.CBPageChangeListener;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bigkoo.convenientbanner.view.CBLoopViewPager;
import com.framework.common.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 页面翻转控件，极方便的广告栏
 * 支持无限循环，自动翻页，翻页特效
 *
 * @author Sai 支持自动翻页
 */
public class ConvenientBanner<T> extends FrameLayout {
    private List<T> mDatas;
    private Drawable[] mIndicatorDrawables;
    private CBPageChangeListener pageChangeListener;
    private OnItemClickListener onItemClickListener;
    private CBPageAdapter pageAdapter;
    private CBLoopViewPager viewPager;
    private TextView mIndicatorTxt;
    private ViewPagerScroller scroller;
    private LinearLayout loPageTurningPoint;
    private long autoTurningTime;
    private boolean isStop;//是否停止轮播
    private boolean canTurn = false;//是否自动轮播
    private boolean canLoop = false;//是否可以循环
    private int mIndicatorSpace;//指示器之间的间隔
    private int mIndicatorLeft, mIndicatorTop, mIndicatorRight, mIndicatorBottom;
    private WeakHandler handler = new WeakHandler();

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    private AdSwitchTask adSwitchTask;

    public ConvenientBanner(Context context) {
        this(context, null);
    }

    public ConvenientBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner);
        canLoop = a.getBoolean(R.styleable.ConvenientBanner_canLoop, false);
        mIndicatorSpace = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorSpace, dpToPx(8));
        int margin = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorMargin, dpToPx(10));
        mIndicatorLeft = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorMarginLeft, margin);
        mIndicatorRight = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorMarginRight, margin);
        mIndicatorTop = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorMarginTop, margin);
        mIndicatorBottom = a.getDimensionPixelSize(R.styleable.ConvenientBanner_indicatorMarginBottom, margin);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        viewPager = new CBLoopViewPager(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(viewPager, params);

        loPageTurningPoint = new LinearLayout(context);
        loPageTurningPoint.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams point_params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        point_params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        point_params.setMargins(mIndicatorLeft, mIndicatorTop, mIndicatorRight, mIndicatorBottom);
        addView(loPageTurningPoint, point_params);

        mIndicatorTxt = new TextView(context);
        mIndicatorTxt.setTextSize(12.0f);
        mIndicatorTxt.setVisibility(GONE);
        FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        indicatorParams.setMargins(mIndicatorLeft, mIndicatorTop, mIndicatorRight, mIndicatorBottom);
        indicatorParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        addView(mIndicatorTxt, indicatorParams);
        initViewPagerScroll();

        adSwitchTask = new AdSwitchTask(this);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<ConvenientBanner> reference;

        AdSwitchTask(ConvenientBanner convenientBanner) {
            this.reference = new WeakReference<ConvenientBanner>(convenientBanner);
        }

        @Override
        public void run() {
            ConvenientBanner convenientBanner = reference.get();

            if (convenientBanner != null) {
                if (convenientBanner.viewPager != null && convenientBanner.canTurn) {
                    PagerAdapter adapter = convenientBanner.pageAdapter;
                    if (convenientBanner.isStop || adapter == null || adapter.getCount() <= 1) {
                        return;
                    }
                    int page = convenientBanner.viewPager.getCurrentItem() + 1;
                    convenientBanner.viewPager.setCurrentItem(page);
                    convenientBanner.handler.postDelayed(convenientBanner.adSwitchTask, convenientBanner.autoTurningTime);
                }
            }
        }
    }

    public ConvenientBanner setPages(ViewAdapter viewAdapter, List<T> datas) {
        this.mDatas = datas;
        pageAdapter = new CBPageAdapter(viewAdapter, mDatas);
        if(onItemClickListener!=null){
            pageAdapter.setOnItemClickListener(onItemClickListener);
        }
        viewPager.setAdapter(pageAdapter, canLoop);
        if (mIndicatorDrawables != null)
            setPageIndicator(mIndicatorDrawables);
        return this;
    }

    /**
     * 通知数据变化
     */
    public void notifyDataSetChanged() {
        if(pageAdapter!=null){
            pageAdapter.notifyDataSetChanged();
        }
        if (mIndicatorDrawables != null)
            setPageIndicator(mIndicatorDrawables);
    }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    public ConvenientBanner setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ConvenientBanner setPageIndicator(@DrawableRes int selectRes, @DrawableRes int normalRes) {
        Drawable[] drawables = new Drawable[2];
        drawables[0] = getResources().getDrawable(normalRes);
        drawables[1] = getResources().getDrawable(selectRes);
        setPageIndicator(drawables);
        return this;
    }

    /**
     * 底部指示器drawable图片
     *
     * @param pageDrawable
     */
    public ConvenientBanner setPageIndicator(Drawable[] pageDrawable) {
        //drawable检查
        if (pageDrawable == null || pageDrawable.length < 2 || pageDrawable[0] == null || pageDrawable[1] == null) {
            return this;
        }
        loPageTurningPoint.removeAllViews();
        this.mIndicatorDrawables = pageDrawable;
        if (mDatas == null || mDatas.size() < 2) return this;
        int normalWidth = Math.max(pageDrawable[0].getIntrinsicWidth(), dpToPx(5));
        int normalHeight = Math.max(pageDrawable[0].getIntrinsicHeight(), dpToPx(5));
        int selectWidth = Math.max(pageDrawable[1].getIntrinsicWidth(), dpToPx(5));
        int selectHeight = Math.max(pageDrawable[1].getIntrinsicHeight(), dpToPx(5));
        for (int count = 0, total = mDatas.size(); count < total; count++) {
            // 翻页指示的点
            View pointView = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            if (count == 0) {
                pointView.setBackground(mIndicatorDrawables[1]);
                params.width = selectWidth;
                params.height = selectHeight;
            } else {
                pointView.setBackground(mIndicatorDrawables[0]);
                params.width = normalWidth;
                params.height = normalHeight;
            }
            if (count != total - 1) {
                params.rightMargin = mIndicatorSpace;
            }
            loPageTurningPoint.addView(pointView, params);
        }

        if(pageChangeListener==null){
            pageChangeListener = new CBPageChangeListener(loPageTurningPoint,
                    pageDrawable);
            viewPager.addOnPageChangeListener(pageChangeListener);
        }else{
            pageChangeListener.setPageDrawable(pageDrawable);
        }
        pageChangeListener.onPageSelected(viewPager.getCurrentItem());

        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中 （RelativeLayout.CENTER_HORIZONTAL），居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     * @return
     */
    public ConvenientBanner setPageIndicatorAlign(PageIndicatorAlign align) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        if (align == PageIndicatorAlign.CENTER_HORIZONTAL) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        } else if (align == PageIndicatorAlign.ALIGN_PARENT_RIGHT) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        } else if (align == PageIndicatorAlign.ALIGN_PARENT_LEFT) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        }
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     * @return
     */
    public boolean isTurning() {
        return canTurn;
    }

    /***
     * 开始翻页
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    public ConvenientBanner startTurning(long autoTurningTime) {
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        stopTurning();
        isStop = false;
        handler.postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        isStop = true;
        handler.removeCallbacks(adSwitchTask);
    }

    /**
     * 自定义翻页动画效果
     *
     * @param transformer
     * @return
     */
    public ConvenientBanner setPageTransformer(PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    //获取当前的页面index
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getCurrentItem();
        }
        return -1;
    }

    //设置当前的页面index
    public void setCurrentItem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
    }

    public ConvenientBanner removeOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        viewPager.removeOnPageChangeListener(onPageChangeListener);
        return this;
    }
    /**
     * 设置翻页监听器
     *
     * @param onPageChangeListener
     * @return
     */
    public ConvenientBanner addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        viewPager.addOnPageChangeListener(onPageChangeListener);
        return this;
    }

    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    /**
     * 监听item点击
     * @param onItemClickListener
     */
    public ConvenientBanner setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if(pageAdapter!=null){
            pageAdapter.setOnItemClickListener(onItemClickListener);
        }
        return this;
    }

    /**
     * 设置ViewPager的滚动速度
     *
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    public CBLoopViewPager getViewPager() {
        return viewPager;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
        notifyDataSetChanged();
    }

    public int dpToPx(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    public void setIndicatorSpace(int indicatorSpace) {
        if (mIndicatorSpace == indicatorSpace) {
            return;
        }
        this.mIndicatorSpace = indicatorSpace;
        for (int i = 0, count = loPageTurningPoint.getChildCount(); i < count; i++) {
            View view = loPageTurningPoint.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.rightMargin = mIndicatorSpace;
        }
        if (loPageTurningPoint.getChildCount() > 0) {
            loPageTurningPoint.requestLayout();
        }
    }

    public void setIndicatorMargin(int... indicatorMargin) {//左上右下
        if (indicatorMargin != null) {
            if (indicatorMargin.length == 1) {
                mIndicatorLeft = mIndicatorTop = mIndicatorRight = mIndicatorBottom = indicatorMargin[0];
            } else if (indicatorMargin.length == 2) {//左右，上下
                mIndicatorLeft = indicatorMargin[0];
                mIndicatorRight = indicatorMargin[0];
                mIndicatorTop = indicatorMargin[1];
                mIndicatorBottom = indicatorMargin[1];
            } else if (indicatorMargin.length == 3) {
                mIndicatorLeft = indicatorMargin[0];
                mIndicatorRight = indicatorMargin[0];
                mIndicatorTop = indicatorMargin[1];
                mIndicatorBottom = indicatorMargin[2];
            } else if (indicatorMargin.length == 4) {
                mIndicatorLeft = indicatorMargin[0];
                mIndicatorTop = indicatorMargin[1];
                mIndicatorRight = indicatorMargin[2];
                mIndicatorBottom = indicatorMargin[3];
            }
            FrameLayout.LayoutParams params = (LayoutParams) loPageTurningPoint.getLayoutParams();
            params.setMargins(mIndicatorLeft, mIndicatorTop, mIndicatorRight, mIndicatorBottom);
            if (loPageTurningPoint.getVisibility() == View.VISIBLE) {
                loPageTurningPoint.setLayoutParams(params);
            }
            params = (LayoutParams) mIndicatorTxt.getLayoutParams();
            params.setMargins(mIndicatorLeft, mIndicatorTop, mIndicatorRight, mIndicatorBottom);
            if (mIndicatorTxt.getVisibility() == View.VISIBLE) {
                mIndicatorTxt.setLayoutParams(params);
            }
        }
    }

    /**
     * 清除资源
     */
    public void clear() {
        stopTurning();
        if(pageAdapter!=null){
            pageAdapter.clearCache();
        }
        viewPager.setAdapter(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopTurning();
        super.onDetachedFromWindow();
    }
}
