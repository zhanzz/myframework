package com.example.demo.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ActivityBean;

import java.util.List;

/**
 * 活动列表图
 */
public class WareActAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MORE = 100;
    List<ActivityBean.ActBean> mList;
    Context mContext;
    ActivityBean.ActivityItemBean dataBean;
    int mHeight = 1;
    int mWidth = 0;
    int mPadding;
    private boolean mNeedMore;

    public WareActAdapter(Context context, ActivityBean.ActivityItemBean dataBean, int height) {
        mContext = context;
        init(dataBean, height);
    }

    private void init(ActivityBean.ActivityItemBean dataBean, int height) {
        mList = dataBean.getAdsList();
        this.dataBean = dataBean;
        float count = dataBean.getMaxShowNum();
        if (count < 0.001) {
            count = 4;
        }
        mPadding = UIHelper.dip2px(dataBean.getInnerBorder());
        List<Integer> paddings = dataBean.getExternalBorder();
        int leftPadding = 0, rightPadding = 0;
        int topPadding = 0, bottomPadding = 0;
        if (!ListUtils.isEmpty(paddings)) {//上、右、下、左
            if (paddings.size() == 4) {
                rightPadding = UIHelper.dip2px(paddings.get(1));
                leftPadding = UIHelper.dip2px(paddings.get(3));
                topPadding = UIHelper.dip2px(paddings.get(0));
                bottomPadding = UIHelper.dip2px(paddings.get(2));
            } else {
                leftPadding = rightPadding = UIHelper.dip2px(paddings.get(0));
            }
        }
        this.mHeight = height - topPadding - bottomPadding;
        mWidth = (int) ((UIHelper.getDisplayWidth() - (leftPadding + rightPadding) - (count - 1) * mPadding) / count);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType != TYPE_MORE) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.recyclerview_item_ware_act, viewGroup, false);
            return new ProductViewHolder(view);
        } else {
            View moreView = LayoutInflater.from(mContext).inflate(
                    R.layout.recyclerview_item_more, viewGroup, false);
            return new MoreViewHolder(moreView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) != TYPE_MORE) {
            final ActivityBean.ActBean bean = mList.get(i);
            ProductViewHolder holder = (ProductViewHolder) viewHolder;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            //调整一些大小
            params.width = mWidth;
            params.height = mHeight;
            if (i != 0) {
                params.leftMargin = mPadding;
            } else {
                params.leftMargin = 0;
            }
            FrescoUtils.showThumb(bean.getActPicUrl(), holder.image, mWidth, mHeight);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = mList != null ? mList.size() : 0;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && TextUtils.isEmpty(mList.get(position).getActPicUrl())) {//最后一个
            return TYPE_MORE;
        }
        return super.getItemViewType(position);
    }

    public void setNewData(ActivityBean.ActivityItemBean itemBean, int height) {
        MyDiffUtil diffUtil = new MyDiffUtil(mList, itemBean.getAdsList());
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil, false);
        init(itemBean, height);
        diffResult.dispatchUpdatesTo(this);
        //notifyDataSetChanged();
    }

    class MoreViewHolder extends RecyclerView.ViewHolder {
        public MoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        public ProductViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    final static class MyDiffUtil extends DiffUtil.Callback {
        private List<ActivityBean.ActBean> oldList, newList;

        public MyDiffUtil(List<ActivityBean.ActBean> oldList, List<ActivityBean.ActBean> newList) {
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
            return TextUtils.equals(oldList.get(oldItemPosition).getActPicUrl(), newList.get(newItemPosition).getActPicUrl());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getActPicUrl(), newList.get(newItemPosition).getActPicUrl());
        }
    }
}
