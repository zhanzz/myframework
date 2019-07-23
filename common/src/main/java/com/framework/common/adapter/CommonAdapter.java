package com.framework.common.adapter;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import com.framework.common.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected List<T> mDatas;
    public static final int TYPE_NOT_FOUND = -404;
    private SparseIntArray layouts;
    protected OnItemClickListener<T> mOnItemClickListener;


    public CommonAdapter(@LayoutRes int layoutId) {
        addItemType(0,layoutId);
    }

    public CommonAdapter() {
        //此方法内需调用addItemType方法
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), parent, getLayoutId(viewType));
        setListener(parent, holder, viewType);
        return holder;
    }

    public abstract void convert(ViewHolder holder, T t,int position);

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas!=null ? mDatas.size():0;
        return itemCount;
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas, int page) {
        if (page <= 1) {//第一页或是刷新数据
            reloadData(datas);
        } else {
            addMoreData(datas);
        }
    }

    public void reloadData(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        if (!ListUtils.isEmpty(datas)) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void addMoreData(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (!ListUtils.isEmpty(datas)) {
            mDatas.addAll(datas);
            //notifyDataSetChanged();
            notifyItemRangeInserted(mDatas.size() - datas.size(), datas.size());
        }
        compatibilityDataSizeChanged(datas == null ? 0 : datas.size());
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mDatas == null ? 0 : mDatas.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void reset() {
        if (mDatas != null) {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }
}
