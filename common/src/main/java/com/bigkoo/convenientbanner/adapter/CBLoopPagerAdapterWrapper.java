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

package com.bigkoo.convenientbanner.adapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.view.CBLoopViewPager;

/**
 * A PagerAdapter wrapper responsible for providing a proper page to
 * LoopViewPager
 * 
 * This class shouldn't be used directly
 */
public class CBLoopPagerAdapterWrapper extends PagerAdapter {

    private final CBLoopViewPager mViewPager;
    private CBPageAdapter mAdapter;

    public void setBoundaryCaching(boolean flag) {
        mAdapter.setCache(flag);
    }

    private boolean canLoop;

    public CBLoopPagerAdapterWrapper(CBPageAdapter adapter, boolean canLoop, CBLoopViewPager viewPager) {
        this.mAdapter = adapter;
        this.mViewPager = viewPager;
        this.canLoop = canLoop;
        if(adapter==null){
            return;
        }
        adapter.registerDataSetObserver(new DataSetObserver(){
            public void onChanged(){
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.clearCache();
        super.notifyDataSetChanged();
    }

    public int toRealPosition(int position) {
        if (!canLoop)return position;
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = (position-1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;

        return realPosition;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;//让notifyDataSetChange有效
    }

    public int toInnerPosition(int realPosition) {
        if (!canLoop) return realPosition;
        int position = (realPosition + 1);
        return position;
    }

    private int getRealFirstPosition() {
        return canLoop ? 1 : 0;
    }

    private int getRealLastPosition() {
        return canLoop ? getRealFirstPosition() + getRealCount() - 1 : getRealCount() - 1;
    }

    @Override
    public int getCount() {
        return canLoop ? mAdapter.getCount() + 2 : getRealCount();
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);
        return mAdapter.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = toRealPosition(position);
        mAdapter.destroyItem(container, realPosition, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if(mAdapter != null)
            mAdapter.startUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }
}