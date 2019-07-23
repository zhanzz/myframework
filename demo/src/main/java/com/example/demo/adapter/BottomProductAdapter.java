package com.example.demo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.example.demo.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.framework.common.adapter.ViewHolder;
import com.framework.common.utils.FrescoUtils;
import com.framework.common.utils.ListUtils;
import com.framework.common.utils.UIHelper;
import com.framework.model.demo.ProductBean;
import java.util.ArrayList;
import java.util.List;
/**
 * @author zhangzhiqiang
 * @date 2019/6/26.
 * description：
 */
public class BottomProductAdapter extends DelegateAdapter.Adapter<ViewHolder>{
    private List<ProductBean> mList;
    private int mImageWidth;
    public BottomProductAdapter(){
        mImageWidth = (UIHelper.getDisplayWidth()-UIHelper.dip2px(5))/2;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper helper = new GridLayoutHelper(2);
        helper.setGap(UIHelper.dip2px(5));
        helper.setVGap(UIHelper.dip2px(5));
        return helper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = ViewHolder.createViewHolder(viewGroup.getContext(), viewGroup, R.layout.item_grid_product);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ProductBean commodity = mList.get(i);
        SimpleDraweeView iv=holder.getView(R.id.img_good);
        iv.getLayoutParams().height = mImageWidth;
        if(TextUtils.isEmpty(commodity.getThumPicture())){
            FrescoUtils.showThumb(commodity.getProductIcon(), iv,mImageWidth,mImageWidth);
        }else{
            FrescoUtils.showThumb(commodity.getThumPicture(),iv,mImageWidth,mImageWidth);
        }
        holder.setText(R.id.txt_good_name,commodity.getProductName());
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.TYPE_BOOTOM_PRODUCT;
    }

    @Override
    public int getItemCount() {
        return mList!=null ? mList.size():0;
    }

    public void addProduct(List<ProductBean> list,int pageIndex){
        if(mList==null){
            mList = new ArrayList<>();
        }
        if(pageIndex<=1){
            mList.clear();
        }
        if(!ListUtils.isEmpty(list)){
//            diffCallback.setList(mList);
//            diffCallback.setNewList(list);
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//            mList.addAll(list);
//            diffResult.dispatchUpdatesTo(this);

            mList.addAll(list);
            notifyItemRangeInserted(mList.size()-list.size(),list.size());

            //notifyDataSetChanged();
        }
    }

    private MyDiffUtil diffCallback = new MyDiffUtil();

    final static class MyDiffUtil extends DiffUtil.Callback{
        private List<ProductBean> oldList,newList;

        public void setList(List<ProductBean> list){
            oldList = list;
        }

        public void setNewList(List<ProductBean> newList) {
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
            return TextUtils.equals(oldList.get(oldItemPosition).getProductId(), newList.get(newItemPosition).getProductId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // 当areItemsTheSame返回true时，我们还需要判断两个item的内容是否相同
            // 此处以User的age作为两个item内容是否相同的依据
            // 即返回两个user的age是否相同
            return TextUtils.equals(oldList.get(oldItemPosition).getSkuId(),newList.get(newItemPosition).getSkuId());
        }
    }
}
