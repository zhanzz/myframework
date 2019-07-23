/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigkoo.convenientbanner.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bigkoo.convenientbanner.adapter.CBLoopPagerAdapterWrapper;
import com.bigkoo.convenientbanner.adapter.CBPageAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * <p/>
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * <p/>
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * <p/>
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter,
 * additional changes in the adapter must be done.
 * The adapter must be prepared to create 2 extra items e.g.:
 * <p/>
 * The original adapter creates 4 items: [0,1,2,3]
 * The modified adapter will have to create 6 items [0,1,2,3,4,5]
 * with mapping realPosition=(position-1)%count
 * [0->3, 1->0, 2->1, 3->2, 4->3, 5->0]
 */
public class CBLoopViewPager extends ViewPager {

    private static final boolean DEFAULT_BOUNDARY_CASHING = true;

    List<OnPageChangeListener> mOuterPageChangeListeners = new ArrayList<>();
    private CBLoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;

    private boolean canLoop = true;//viewPager是否可循环滚动
    private boolean isCanScroll = true;//viewPager是否可以手势滑动

    public CBLoopViewPager(Context context) {
        super(context);
        init();
    }

    public CBLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.addOnPageChangeListener(onPageChangeListener);
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    public void setAdapter(CBPageAdapter adapter, boolean canLoop) {
        mAdapter = new CBLoopPagerAdapterWrapper(adapter, canLoop, this);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    public int getFristItem() {
        return canLoop ? mAdapter.getRealCount() : 0;
    }

    public int getLastItem() {
        return mAdapter.getRealCount() - 1;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public int getRealCurrentItem() {
        return mAdapter != null ? super.getCurrentItem() : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = 0;
        try {
            realItem = mAdapter.toInnerPosition(item);
        } catch (NullPointerException e) {
        }
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        mOuterPageChangeListeners.remove(listener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private boolean mIsChange;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListeners != null) {
                    for (OnPageChangeListener listener : mOuterPageChangeListeners) {
                        if (listener != null) {
                            listener.onPageSelected(realPosition);
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            mIsChange = true;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);
            }
            if (mOuterPageChangeListeners != null) {
                for (OnPageChangeListener listener : mOuterPageChangeListeners) {
                    if (listener != null) {
                        listener.onPageScrolled(realPosition,
                                positionOffset, positionOffsetPixels);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = CBLoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);

                if (canLoop && state != ViewPager.SCROLL_STATE_SETTLING
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    if (mIsChange) {
                        mIsChange = false;
                        //如果是0或者最后一个View，为了无限循环,滚动结束会预先跳到相反的View，如0跳最后，最后跳0
                        setCurrentItem(realPosition, false);//如果为false，就不会刷新视图，也就出现第一次加载的时候往前滚，会有空白View。
                    }
                }
            }
            if (mOuterPageChangeListeners != null) {
                for (OnPageChangeListener listener : mOuterPageChangeListeners) {
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    };

    public boolean isCanLoop() {
        return canLoop;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        if (mAdapter != null) {
            mAdapter.setCanLoop(canLoop);
        }
    }
}
