package com.bigkoo.convenientbanner.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.holder.ViewAdapter;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.List;
/**
 * Created by Sai on 15/7/29.
 */
public class CBPageAdapter<T> extends PagerAdapter{
    private final RecycleBin recycleBin;
    private boolean cache = false;//是否缓存视图
    protected List<T> mDatas;
    private OnItemClickListener<T> onItemClickListener;
    private ViewAdapter mViewAdapter;

    public CBPageAdapter(ViewAdapter viewAdapter, List<T> datas) {
        recycleBin = new RecycleBin();
        this.mViewAdapter = viewAdapter;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public final Object instantiateItem(ViewGroup container, final int position) {
        if(mViewAdapter!=null){
            int viewType = mViewAdapter.getItemType(position);
            View view = null;
            if (cache) {
                view = recycleBin.getScrapView(viewType);
            }
            if(view==null){
                view = mViewAdapter.createView(container.getContext(),position);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position,mDatas.get(position));
                    }
                }
            });
            if (mDatas != null && mDatas.size()>position)
                mViewAdapter.UpdateUI(view, position, mDatas.get(position));
            container.addView(view);
            return view;
        }
        return null;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        if(mViewAdapter!=null){
            if (cache) {
                int viewType = mViewAdapter.getItemType(position);
                recycleBin.addScrapView(view,viewType);
            }
        }
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setViewAdapter(ViewAdapter viewAdapter){
        this.mViewAdapter = viewAdapter;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public void clearCache() {
        recycleBin.clear();
    }
}
