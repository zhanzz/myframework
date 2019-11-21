package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.example.demo.R;
import com.example.demo.widget.IndicatorView;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ActivityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/7/5.
 * description：
 */
public class MenuAdapter extends DelegateAdapter.Adapter <MenuAdapter.MenuViewHolder>{
    private ActivityBean.ActivityItemBean mItemBean;
    private int mCurrentPage;
    public MenuAdapter(ActivityBean.ActivityItemBean bean){
        mItemBean = bean;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        List<List<ActivityBean.ActBean>> mainPageDatas = new ArrayList<>();
        int pageNum = mItemBean.getCol()*mItemBean.getRow();
        List<ActivityBean.ActBean> list = mItemBean.getAdsList();
        handList(list,mItemBean.getRow(),mItemBean.getCol());
        int line = list.size() / pageNum;
        for (int i = 0; i < line; i++) {
            mainPageDatas.add(list.subList(pageNum * i, (i + 1) * pageNum));
        }
        if (list.size() % pageNum != 0) {
            mainPageDatas.add(list.subList(line * pageNum, list.size()));
        }
        float rate = 0;
        if(mainPageDatas.size()<=1){//一页
            rate = 75.0f/(17*mItemBean.getRow());
        }else{
            rate = 75.0f/(17*mItemBean.getRow()+2);
        }
        helper.setBgColor(0xffffffff);
        helper.setPadding(UIHelper.dip2px(10), UIHelper.dip2px(8),UIHelper.dip2px(10), 0);
        helper.setAspectRatio(rate);
        String url = mItemBean.getBackgroundImgUrl();
        if(TextUtils.isEmpty(url)){
            url = "res:///"+ R.drawable.menu_bg;//测试背景url
        }
        helper.setLayoutViewHelper(new BgListener(url,UIHelper.getDisplayWidth(), (int) (UIHelper.getDisplayWidth()/rate)));
        return helper;
    }

    /**
     * 重排序菜单的位置
     * @param data
     */
    private void handList(List<ActivityBean.ActBean> data,int mRows,int mColums) {
        if(ListUtils.isEmpty(data)){
            return;
        }
        List formatData = new ArrayList();

        int mOnePageSize = mRows * mColums;
        int mPageSize = data.size() / mOnePageSize;
        if (data.size() % mOnePageSize != 0) {
            mPageSize++;
        }
        for (int p = 0; p < mPageSize; p++) {
            for (int c = 0; c < mColums; c++) {
                for (int r = 0; r < mRows; r++) {
                    int index = c + r * mColums + p * mOnePageSize;
                    if (index < data.size()) {
                        formatData.add(data.get(index));
                    }
                }
            }
        }
        data.clear();
        data.addAll(formatData);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        RecyclerView recyclerView = new RecyclerView(parent.getContext());
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,mItemBean.getCol()*mItemBean.getRow());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),mItemBean.getRow(), LinearLayoutManager.HORIZONTAL,false);
        //gridLayoutManager.setInitialPrefetchItemCount(mItemBean.getCol()*mItemBean.getRow());
        recyclerView.setLayoutManager(gridLayoutManager);
        SecondPageGridViewAdapter adapter = new SecondPageGridViewAdapter(new ArrayList<ActivityBean.ActBean>(mItemBean.getAdsList()));
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new MenuItemDecoration(UIHelper.dip2px(8),0));
        frameLayout.addView(recyclerView);
        IndicatorView indicatorView = new IndicatorView(parent.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(UIHelper.dip2px(40),UIHelper.dip2px(3));
        params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        frameLayout.addView(indicatorView,params);
        return new MenuViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        final RecyclerView recyclerView = holder.mRecyclerView;
        SecondPageGridViewAdapter adapter = (SecondPageGridViewAdapter) recyclerView.getAdapter();
        List<ActivityBean.ActBean> oldData = adapter.getData();
        MyDiffUtil diffUtil = new MyDiffUtil(oldData,mItemBean.getAdsList());
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil,false);
        oldData.clear();oldData.addAll(mItemBean.getAdsList());
        diffResult.dispatchUpdatesTo(adapter);
        final IndicatorView indicatorView = holder.mIndicatorView;
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                indicatorView.setExtentAndRange(recyclerView.computeHorizontalScrollExtent(),recyclerView.computeHorizontalScrollRange());
                return true;
            }
        });
        MyScrollListener listener = new MyScrollListener(indicatorView);
        recyclerView.removeOnScrollListener(listener);
        recyclerView.addOnScrollListener(listener);
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_MENU;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    final class MenuViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView mRecyclerView;
        public IndicatorView mIndicatorView;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) ((ViewGroup)itemView).getChildAt(0);
            mIndicatorView = (IndicatorView) ((ViewGroup)itemView).getChildAt(1);
        }
    }

    final class MyScrollListener extends RecyclerView.OnScrollListener{
        IndicatorView indicatorView;
        public MyScrollListener(IndicatorView indicatorView){
            this.indicatorView = indicatorView;
        }
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            indicatorView.setOffset(recyclerView.computeHorizontalScrollOffset(),recyclerView.computeHorizontalScrollRange());
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof MyScrollListener){
                return true;
            }
            return false;
        }
    }

    final static class MyDiffUtil extends DiffUtil.Callback{
        private List<ActivityBean.ActBean> oldList,newList;
        public MyDiffUtil(List<ActivityBean.ActBean> oldList,List<ActivityBean.ActBean> newList){
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            // 返回旧数据的长度
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            // 返回新数据的长度
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // 返回两个item是否相同
            // 例如：此处两个item的数据实体是User类，所以以id作为两个item是否相同的依据
            // 即此处返回两个user的id是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getActName(),newList.get(newItemPosition).getActName());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getActName(),newList.get(newItemPosition).getActName());
        }
    }
}
